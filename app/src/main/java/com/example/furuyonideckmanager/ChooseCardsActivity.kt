package com.example.furuyonideckmanager

import CsvUtil.classifiedCsvData
import CsvUtil.readRawCsv
import PartsUtil.setButtonStyles
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_choose_cards.*
import java.io.*
import java.util.*

class ChooseCardsActivity : AppCompatActivity(), DeckNameDialog.Listener {
    private lateinit var realm: Realm;

    // 通常札の選択されたカード
    val chosenNormalCards = mutableListOf<List<String>>();
    // 切札の選択されたカード
    val chosenSpecialCards = mutableListOf<List<String>>();
    // 選ばれたメガミその1
    var megami0Name: String? = null;
    // 選ばれたメガミその2
    var megami1Name: String? = null;
    // ダイアログ
    val dialog = DeckNameDialog();

    /**
     * 左側のメガミのボタン情報を取得。
     * @return ボタンの種類とボタンのマップの配列を返します。
     */
    fun getLeftMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to megami0_card0_view, "type0" to megami0_type00_view, "type1" to megami0_type01_view),
            mapOf("card" to megami0_card1_view, "type0" to megami0_type10_view, "type1" to megami0_type11_view),
            mapOf("card" to megami0_card2_view, "type0" to megami0_type20_view, "type1" to megami0_type21_view),
            mapOf("card" to megami0_card3_view, "type0" to megami0_type30_view, "type1" to megami0_type31_view),
            mapOf("card" to megami0_card4_view, "type0" to megami0_type40_view, "type1" to megami0_type41_view),
            mapOf("card" to megami0_card5_view, "type0" to megami0_type50_view, "type1" to megami0_type51_view),
            mapOf("card" to megami0_card6_view, "type0" to megami0_type60_view, "type1" to megami0_type61_view),
            mapOf("card" to megami0_s_card0_view, "type0" to megami0_s_type00_view, "type1" to megami0_s_type01_view),
            mapOf("card" to megami0_s_card1_view, "type0" to megami0_s_type10_view, "type1" to megami0_s_type11_view),
            mapOf("card" to megami0_s_card2_view, "type0" to megami0_s_type20_view, "type1" to megami0_s_type21_view),
            mapOf("card" to megami0_s_card3_view, "type0" to megami0_s_type30_view, "type1" to megami0_s_type31_view)
        );
    }

    /**
     * 右側のメガミのボタン情報を取得。
     * @return ボタンの種類とボタンのマップの配列を返します。
     */
    fun getRightMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to megami1_card0_view, "type0" to megami1_type00_view, "type1" to megami1_type01_view),
            mapOf("card" to megami1_card1_view, "type0" to megami1_type10_view, "type1" to megami1_type11_view),
            mapOf("card" to megami1_card2_view, "type0" to megami1_type20_view, "type1" to megami1_type21_view),
            mapOf("card" to megami1_card3_view, "type0" to megami1_type30_view, "type1" to megami1_type31_view),
            mapOf("card" to megami1_card4_view, "type0" to megami1_type40_view, "type1" to megami1_type41_view),
            mapOf("card" to megami1_card5_view, "type0" to megami1_type50_view, "type1" to megami1_type51_view),
            mapOf("card" to megami1_card6_view, "type0" to megami1_type60_view, "type1" to megami1_type61_view),
            mapOf("card" to megami1_s_card0_view, "type0" to megami1_s_type00_view, "type1" to megami1_s_type01_view),
            mapOf("card" to megami1_s_card1_view, "type0" to megami1_s_type10_view, "type1" to megami1_s_type11_view),
            mapOf("card" to megami1_s_card2_view, "type0" to megami1_s_type20_view, "type1" to megami1_s_type21_view),
            mapOf("card" to megami1_s_card3_view, "type0" to megami1_s_type30_view, "type1" to megami1_s_type31_view)
        );
    }

    /**
     * ボタンの見た目を設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     */
    fun setButtonsView(buttons: Array<Map<String, Button?>>, csvData: List<Map<String, String>>) {
        for (i in csvData.indices) {
            val targetData = csvData[i];
            val targetButtons = buttons[i];
            // カード名設定
            targetButtons.get("card")?.setText(targetData.get("actionName"));
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData.get("mainType").orEmpty());
            setButtonStyles(targetButtons.get("type1"), targetData.get("subType").orEmpty());
        }
    }

    /**
     * ボタンにハンドラを設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     */
    fun setButtonsHandler(buttons: Array<Map<String, Button?>>, cardCsv: List<Map<String, String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];

            // ハンドラ定義
            val cardButton = targetButtons.get("card");
            cardButton?.setOnClickListener {
                // 画面遷移
                val intent = Intent(this, ShowCardActivity::class.java);
                // 画像データ
                val image = targetData.get("fileName");
                // 画像データを渡す
                intent.putExtra("IMAGE_FILE_NAME", image);
                startActivity(intent);
            }
        }
    }

    /**
     * 登録ボタンの活性/非活性状態切替。
     */
    fun setRegisterButtonEnable() {
        // 作成ボタンの活性状態を変える
        // 通常札が7枚かつ切札が3枚の場合は活性にする
        if (chosenNormalCards.size == 7 && chosenSpecialCards.size == 3) {
            registerDeck.setEnabled(true);
        } else {
            // それ以外は非活性
            registerDeck.setEnabled(false);
        }
    }

    /**
     * 右側のメガミのチェックボックス一覧を取得。
     * @return 右側のメガミのチェックボックスの一覧を返します。
     */
    fun getRightMegamiCheckBoxes(): Array<CheckBox> {
        return arrayOf(
            megami0_check0,
            megami0_check1,
            megami0_check2,
            megami0_check3,
            megami0_check4,
            megami0_check5,
            megami0_check6,
            megami0_s_check0,
            megami0_s_check1,
            megami0_s_check2,
            megami0_s_check3
        );
    }

    /**
     * 左側のメガミのチェックボックス一覧を取得。
     * @return 左側のメガミのチェックボックスの一覧を返します。
     */
    fun getLeftMegamiCheckBoxes(): Array<CheckBox> {
        return arrayOf(
            megami1_check0,
            megami1_check1,
            megami1_check2,
            megami1_check3,
            megami1_check4,
            megami1_check5,
            megami1_check6,
            megami1_s_check0,
            megami1_s_check1,
            megami1_s_check2,
            megami1_s_check3
        );
    }

    /**
     * チェックボックスに対してハンドラを設定。
     * @param cardCsv カードのcsvデータ。
     */
    fun setCheckBoxHandlers(cardCsv: List<Map<String, String>>, isRight: Boolean) {
        var checkBoxes = if (isRight == true) getRightMegamiCheckBoxes() else getLeftMegamiCheckBoxes();

        // 通常カードのチェックボックスにハンドラ設定
        for (i in 0..6) {
            val checkBox = checkBoxes[i];
            checkBox.setOnCheckedChangeListener {_, isChecked ->
                val targetData = cardCsv[i];
                if (isChecked) {
                    chosenNormalCards.add(targetData.values.toList());
                } else {
                    chosenNormalCards.remove(targetData.values.toList());
                }

                // TODO: 関数化したい
                // テキスト更新
                cardCount_main_normal.setText((chosenNormalCards.size).toString());
                // 色更新
                val textColor = if (chosenNormalCards.size != 7) "#CC0000" else "#000000";
                cardCount_main_normal.setTextColor(Color.parseColor((textColor)));
                // ボタン活性/非活性制御
                setRegisterButtonEnable();
            }
        }

        // 切札のチェックボックスにハンドラ設定
        for (i in 7..10) {
            val checkBox = checkBoxes[i];
            checkBox.setOnCheckedChangeListener {_, isChecked ->
                if (isChecked) {
                    chosenSpecialCards.add(cardCsv[i].values.toList());
                } else {
                    chosenSpecialCards.remove(cardCsv[i].values.toList());
                }

                // テキスト更新
                cardCount_main_special.setText((chosenSpecialCards.size).toString());
                // 色更新
                val textColor = if (chosenSpecialCards.size != 3) "#CC0000" else "#000000";
                cardCount_main_special.setTextColor(Color.parseColor((textColor)));
                // ボタン活性/非活性制御
                setRegisterButtonEnable();
            }
        }
    }

    /**
     * デッキ情報をデッキ情報管理リストに追加。
     * @param deckFileName 新規に作成したデッキのファイル名。
     */
    fun addDeckToList(deckFileName: String) {
        // DBにデッキ情報を書き込む
        realm.beginTransaction();
        realm.createObject<Deck>(deckFileName).apply {
            title = dialog.getInput().toString()
            megami0 = megami0Name ?: ""
            megami1 = megami1Name ?: ""
            comment = ""
        }
        realm.commitTransaction();
    }

    /**
     * デッキのカード一覧情報をcsvファイルとして書き込み。
     * @param deckFileName 新規に作成したデッキのファイル名。
     * @param csvData デッキのcsvデータ。
     */
    fun saveFile(deckFileName: String, csvData: String) {
        File(applicationContext.filesDir, deckFileName).writer().use {
            try {
                it.write(csvData);
                addDeckToList(deckFileName);
                Toast.makeText(applicationContext, "デッキを登録しました", Toast.LENGTH_SHORT).show();
            }
            catch (e: IOException) {
                Toast.makeText(applicationContext, "デッキ登録に失敗しました。もう1度試してみて下さい", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * ダイアログの「登録」ボタン押下時の処理。
     */
    override fun register() {
        val normalCsvDataList = chosenNormalCards.map{it.joinToString(",")};
        val specialCsvDataList = chosenSpecialCards.map{it.joinToString(",")};
        val normalCsvData = normalCsvDataList.joinToString("\n");
        val specialCsvData = specialCsvDataList.joinToString("\n");
        // 連結
        val resultCsvData = normalCsvData + "\n" + specialCsvData;
        // ファイル名作成
        val fileName = UUID.randomUUID().toString() + ".csv";
        // データ保存
        saveFile(fileName, resultCsvData);
        // デッキ参照画面に遷移
        val intent = Intent(this, DeckListActivity::class.java);
        startActivity(intent);
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 処理なし
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_cards)
        var config = RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        val res = resources;
        val context = applicationContext;

        var chosenMegami = intent.getStringArrayExtra("CHOSEN_MEGAMI")
        megami0Name = chosenMegami?.get(0);
        megami1Name = chosenMegami?.get(1);
        if (megami0Name == null || megami1Name ==null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // メガミ画像の設定
        setImageToImageView(megami0Name + ".jpg", megamiImage0_view, assets);
        setImageToImageView(megami1Name + ".jpg", megamiImage1_view, assets);

        // カード情報の取得
        // csvをparse
        val csvData0 = readRawCsv(res.getIdentifier(megami0Name, "raw", packageName), res, context);
        val csvData1 = readRawCsv(res.getIdentifier(megami1Name, "raw", packageName), res, context);
        // オリジン、A-1、A-2に分類
        val classifiedCardList0 = classifiedCsvData(csvData0);
        val classifiedCardList1 = classifiedCsvData(csvData1);
        // オリジン
        val originCardList0 = classifiedCardList0.get("origin");
        val originCardList1 = classifiedCardList1.get("origin");
        val a1CardList0 = classifiedCardList0.get("A-1");
        val a1CardList1 = classifiedCardList1.get("A-1");
        val a2CardList0 = classifiedCardList0.get("A-2");
        val a2CardList1 = classifiedCardList1.get("A-2");

        if (originCardList0 == null || originCardList1 == null) {
            return;
        }

        // 画面の初期化
        // ボタン一覧を取得する
        val megamiButtonList0 = getLeftMegamiCardButtons();
        val megamiButtonList1 = getRightMegamiCardButtons();
        // ボタンの見た目設定
        // 最初はオリジンで設定する
        setButtonsView(megamiButtonList0, originCardList0);
        setButtonsView(megamiButtonList1, originCardList1);

        // カード表示画面に遷移するためのハンドラ設定
        setButtonsHandler(megamiButtonList0, originCardList0);
        setButtonsHandler(megamiButtonList1, originCardList1);

        // チェックボックスにハンドラ設定
        setCheckBoxHandlers(originCardList0, true);
        setCheckBoxHandlers(originCardList1, false);

        // 登録
        registerDeck.setOnClickListener {
            dialog.show(supportFragmentManager, "register_dialog");
        }
    }

    override fun onDestroy() {
        super.onDestroy();
        realm.close();
    }
}