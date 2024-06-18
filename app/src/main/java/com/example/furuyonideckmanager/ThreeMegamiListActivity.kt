package com.example.furuyonideckmanager

import CsvUtil.removeCsvFile
import DeckAdapter
import ThreeMegamiAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_deck_list.itemList
import kotlinx.android.synthetic.main.activity_deck_list.noDeckError0
import kotlinx.android.synthetic.main.activity_deck_list.noDeckError1
import kotlinx.android.synthetic.main.activity_deck_list.registerDeckButton
import kotlinx.android.synthetic.main.activity_three_megami_list.*

class DeleteGroupConfirmDialog: DialogFragment() {
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
        val deckName = arguments?.getString("GROUP_TITLE");

        builder.setMessage("「$deckName」を削除します。よろしいですか？");

        builder.setPositiveButton("削除") {_, _ ->
            val id = arguments?.getString("UUID");
            if (id != null) {
                listener?.confirm(id);
            }
        }
        builder.setNegativeButton("キャンセル") {_, _ ->
            listener?.cancel();
        }
        return builder.create();
    }
}


class ThreeMegamiListActivity : AppCompatActivity(), DeleteGroupConfirmDialog.Listener {
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
     * @param id 対象データのUUID。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun confirm(id: String) {
        // デッキリストから対象行を削除
        realm.executeTransaction {
            val target = realm.where<ThreeMegami>().contains("id", id).findAll();
            target.deleteFirstFromRealm();
        }

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
        registerGroupButton.visibility = VISIBLE;
        registerGroupButton.setOnClickListener {
            val intent = Intent(this, ThreeMegamiRegisterActivity::class.java);
            startActivity(intent);
        }
    }

    /**
     * 削除されたデッキデータのクリーンアップ処理。
     * @param targetData 開こうとするデータ
     */
    fun cleanUpDeletedData(targetData: ThreeMegami) {
        // 各デッキが存在するか確認
        // ない場合は削除処理を行う
        if (targetData.deck01id != "") {
            val deck01data = realm.where<Deck>().contains("fileName", targetData.deck01id).findFirst();
            if (deck01data == null) {
                realm.executeTransaction {
                    targetData?.deck01id = "";
                    targetData?.deck01name = "";
                }
            }
        }
        if (targetData.deck12id != "") {
            val deck12data = realm.where<Deck>().contains("fileName", targetData.deck12id).findFirst();
            if (deck12data == null) {
                realm.executeTransaction {
                    targetData?.deck12id = "";
                    targetData?.deck12name = "";
                }
            }
        }
        if (targetData.deck20id != "") {
            val deck20data = realm.where<Deck>().contains("fileName", targetData.deck20id).findFirst();
            if (deck20data == null) {
                realm.executeTransaction {
                    targetData?.deck20id = "";
                    targetData?.deck20name = "";
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_megami_list);

        realm = Realm.getDefaultInstance();

        val currentVersion = realm.version
        Log.d("RealmVersion", "Current Realm schema version: $currentVersion")

        itemList.layoutManager = LinearLayoutManager(this);
        val groupList = realm.where<ThreeMegami>().findAll().sort("date", Sort.DESCENDING);

        //　データが無ければ専用の画面にする
        if (groupList.size == 0) {
            createEmptyView();
            return;
        }

        // 以下はデータがあるケース

        val context = this;
        // アダプタの作成＆設定
        val adapter = ThreeMegamiAdapter(groupList, context)
        adapter.setListener(
            object: ThreeMegamiAdapter.Listener {
                override fun onGroupDeleteButtonClick(view: View, item: ThreeMegami) {
                    val dialog = DeleteGroupConfirmDialog();
                    // データ受け渡し
                    val dialogArgs = Bundle();
                    dialogArgs.putString("GROUP_TITLE", item.title);
                    dialogArgs.putString("UUID", item.id);

                    dialog.arguments = dialogArgs;
                    // ダイアログ表示
                    dialog.show(supportFragmentManager, "deletegroup_dialog");
                }

                override fun onGroupNameButtonClick(view: View, item: ThreeMegami) {
                    // 対象データ取得
                    val targetData = realm.where<ThreeMegami>().equalTo("id", item.id).findFirst();

                    if (targetData == null) {
                        throw Exception();
                    }

                    // 削除されているデータのクリーンアップ
                    cleanUpDeletedData(targetData);

                    // 画面遷移
                    val intent = Intent(context, ThreeMegamiDeckRegisterActivity::class.java);

                    // UUID
                    intent.putExtra("UUID", item.id);

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item)
    }
}