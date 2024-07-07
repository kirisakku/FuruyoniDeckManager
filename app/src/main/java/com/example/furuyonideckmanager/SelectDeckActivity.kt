package com.example.furuyonideckmanager

import CsvUtil.removeCsvFile
import DeckAdapter
import DeckAdapter2
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
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
import kotlinx.android.synthetic.main.activity_select_deck.*
import java.lang.Exception

class SelectDeckActivity : AppCompatActivity(), DeleteConfirmDialog.Listener {
    private lateinit var realm: Realm;
    private var selectedData: Deck? = null;

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_deck);

        var megami0 = intent.getStringExtra("MEGAMI0");
        var megami1 = intent.getStringExtra("MEGAMI1");
        val uuid = intent.getStringExtra("UUID");
        val target = intent.getStringExtra("TARGET");
        if (megami0 == null || megami1 == null || uuid == null || target == null) {
            throw Exception();
        }

        realm = RealmManager.getRealm();
        itemList.layoutManager = LinearLayoutManager(this);
        val deckList = realm.where<Deck>().equalTo("megami0", megami0).equalTo("megami1", megami1)
            .or()
            .equalTo("megami0", megami1).equalTo("megami1", megami0).findAll()
            .sort("date", Sort.DESCENDING);

        fun createDeck() {
            val intent = Intent(this, ChooseCardsActivity::class.java);

            // 選ばれたメガミの情報を渡す
            val selectedMegamiArray: Array<String> = arrayOf(megami0, megami1);
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray);
            intent.putExtra("UUID", uuid);
            intent.putExtra("TARGET", target)

            startActivity(intent);
        }

        //　データが無ければ専用の画面にする
        if (deckList.size == 0) {
            noDeckError0.visibility = VISIBLE;
            noDeckError1.visibility = VISIBLE;
            registerDeckButton.visibility = VISIBLE;
            setDeckButton.visibility = INVISIBLE;
            createDeckButton.visibility = INVISIBLE;

            registerDeckButton.setOnClickListener {
                createDeck();
            }

            return;
        }

        // 以下はデータがあるケース
        var targetDeckId = intent.getStringExtra("TARGET_DECKID");
        targetDeckId = if (targetDeckId != null) targetDeckId else "";
        if (targetDeckId != null) {
            selectedData = deckList.find{it.fileName == targetDeckId}
            if (selectedData != null) {
                setDeckButton.isEnabled = true;
            }
        }

        val context = this;
        // アダプタの作成＆設定
        val adapter = DeckAdapter2(deckList, context, targetDeckId);
        adapter.setListener(
            object: DeckAdapter2.Listener {
                override fun onRadioButtonClick(view: View, item: Deck) {
                    selectedData = item;
                    // 設定ボタンの活性化
                    setDeckButton.isEnabled = true;
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
                    // この画面固有のフラグ
                    intent.putExtra("EDITABLE", false);

                    startActivity(intent);
                }
            }
        )
        itemList.adapter = adapter;

        setDeckButton.setOnClickListener {
            // DB情報更新
            // 更新対象のデータを検索
            if (selectedData != null) {
                DBUtil.registerSelectedDeck(realm, uuid, selectedData!!.fileName, selectedData!!.title, target);
            }
            // 画面遷移
            val newIntent = Intent(applicationContext, ThreeMegamiDeckRegisterActivity::class.java);
            newIntent.putExtra("UUID", uuid);

            startActivity(newIntent);
        }

        createDeckButton.setOnClickListener {
            createDeck();
        }
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