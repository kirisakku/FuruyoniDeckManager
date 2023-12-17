package com.example.furuyonideckmanager

import CsvUtil.removeCsvFile
import DeckAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.Sort
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_deck_list.*

class DeleteConfirmDialog: DialogFragment() {
    interface Listener {
        fun confirm(csvName: String);
        fun cancel();
    }
    private var listener: Listener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        when (context) {
            is Listener -> listener = context;
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());
        // 表示内容設定
        val deckName = arguments?.getString("DECK_TITLE");

        builder.setMessage("デッキ「$deckName」を削除します。よろしいですか？");

        builder.setPositiveButton("削除") {_, _ ->
            val csvName = arguments?.getString("DECK_CSV");
            if (csvName != null) {
                listener?.confirm(csvName);
            }
        }
        builder.setNegativeButton("キャンセル") {_, _ ->
            listener?.cancel();
        }
        return builder.create();
    }
}


class DeckListActivity : AppCompatActivity(), DeleteConfirmDialog.Listener {
    private lateinit var realm: Realm;

    /**
     * 画面をリロード。
     */
    private fun reload() {
        val intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
    }

    /**
     * ダイアログの「削除」ボタン押下時の処理。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun confirm(csvName: String) {
        // デッキリストから対象行を削除
        realm.executeTransaction {
            val target = realm.where<Deck>().contains("fileName", csvName).findAll();
            target.deleteFirstFromRealm();
        }

        // csvファイルを削除
        removeCsvFile(csvName, applicationContext);

        // 再描画
        reload();
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 何もしない
    }

    /**
     * データが無い時の画面を生成。
     */
    fun createEmptyView() {
        noDeckError0.visibility = VISIBLE;
        noDeckError1.visibility = VISIBLE;
        registerDeckButton.visibility = VISIBLE;
        registerDeckButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java);
            startActivity(intent);
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        var config = RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();
        itemList.layoutManager = LinearLayoutManager(this);
        val deckList = realm.where<Deck>().findAll().sort("date", Sort.DESCENDING);

        //　データが無ければ専用の画面にする
        if (deckList.size == 0) {
            createEmptyView();
            return;
        }

        // 以下はデータがあるケース

        val context = this;
        // アダプタの作成＆設定
        val adapter = DeckAdapter(deckList, context)
        adapter.setListener(
            object: DeckAdapter.Listener {
                override fun onDeleteButtonClick(view: View, item: Deck) {
                    val dialog = DeleteConfirmDialog();
                    // データ受け渡し
                    val dialogArgs = Bundle();
                    dialogArgs.putString("DECK_TITLE", item.title);
                    dialogArgs.putString("DECK_CSV", item.fileName);
                    dialogArgs.putBundle("DIALOG_ARGS", dialogArgs);
                    dialog.arguments = dialogArgs;
                    // ダイアログ表示
                    dialog.show(supportFragmentManager, "delete_dialog");
                }

                override fun onDeckNameButtonClick(view: View, item: Deck) {
                    // 画面遷移
                    val intent = Intent(context, ViewDeckActivity::class.java);

                    // デッキ名を渡す
                    intent.putExtra("DECK_TITLE", item.title);
                    // デッキ名を渡す
                    intent.putExtra("DECK_FILENAME", item.fileName);
                    // メガミ情報を渡す
                    val selectedMegamiArray: Array<String> = arrayOf(item.megami0, item.megami1);
                    intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)

                    startActivity(intent);
                }
            }
        )
        itemList.adapter = adapter;
    }

    override fun onDestroy() {
        super.onDestroy();
        realm.close();
    }
}