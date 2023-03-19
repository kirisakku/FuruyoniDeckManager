package com.example.furuyonideckmanager

import CsvUtil.convertCsvToStringArray
import CsvUtil.getClassifiedCsvData
import CsvUtil.isAnotherExist
import PartsUtil.*
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_choose_cards.*
import kotlinx.android.synthetic.main.deck_item.*
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
    // 追加札表示ダイアログ
    val additionalCardDialog = AdditionalCardDialogs();

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
            val cardNameButton = targetButtons.get("card");
            cardNameButton?.setText(targetData.get("actionName"));
            // 背景色設定
            if (cardNameButton != null) {
                setButtonBackgroundColor(cardNameButton, targetData)
            }
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
            // カード名が無かったらハンドラを取り除いて終了
            if (targetData.get("actionName") == "") {
                cardButton?.setOnClickListener(null);
                continue;
            }

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
    @RequiresApi(Build.VERSION_CODES.N)
    fun setCheckBoxHandlers(cardCsv: List<Map<String, String>>, isLeft: Boolean) {
        var checkBoxes = if (isLeft == true) getRightMegamiCheckBoxes() else getLeftMegamiCheckBoxes();

        // 通常カードのチェックボックスにハンドラ設定
        for (i in 0..6) {
            val checkBox = checkBoxes[i];
            checkBox.setOnCheckedChangeListener {_, isChecked ->
                val targetData = cardCsv[i];
                if (isChecked) {
                    chosenNormalCards.add(targetData.values.toList());
                } else {
                    chosenNormalCards.removeIf{it[0] == targetData.get("no") && it[6] == targetData.get("megamiName")}
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
            // 活性/非活性制御
            if (cardCsv[i].get("actionName") == "") {
                checkBox.isEnabled = false;
                checkBox.isChecked = false;
                continue;
            } else {
                checkBox.isEnabled = true;
            }
            // ハンドラ設定
            checkBox.setOnCheckedChangeListener {_, isChecked ->
                if (isChecked) {
                    chosenSpecialCards.add(cardCsv[i].values.toList());
                } else {
                    chosenSpecialCards.removeIf{it[0] == cardCsv[i].get("no") && it[6] == cardCsv[i].get("megamiName")}
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

    fun setUnselectedAlpha(originImageView: ImageView, a1ImageView: ImageView, a2ImageView: ImageView) {
        originImageView.alpha = 0.25F;
        a1ImageView.alpha = 0.25F;
        a2ImageView.alpha = 0.25F;
    }

    /**
     * メガミimageViewに画像とイベントハンドラを設定。
     * @param imageView 画像とイベントハンドラ設定対象のボタン。
     * @param megamiName メガミ名。
     * @param megamiButtonList メガミのボタン一覧。
     * @param cardCsvList カードのCSVデータ一覧。
     * @param extraCardCsvList 追加札カードのCSVデータ一覧。
     * @param isLeft 左側のメガミかどうか。
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun setMegamiButton(
        imageView: ImageView,
        megamiName: String,
        megamiButtonList: Array<Map<String, Button?>>,
        cardCsvList: List<Map<String, String>>,
        extraCardCsvList: List<Map<String, String>>,
        isLeft: Boolean
    ) {
        // 画像設定
        setImageToImageView("$megamiName.jpg", imageView, assets);

        // 一律非選択の見た目にするための関数を選択
        var setAlphaFunc = { -> setUnselectedAlpha(megamiImage0_edit, megamiImage0_A1_edit, megamiImage0_A2_edit)};

        if (isLeft == false) {
            setAlphaFunc = { -> setUnselectedAlpha(megamiImage1_edit, megamiImage1_A1_edit, megamiImage1_A2_edit)};
        }
        var additionalButton = if (isLeft)  additionalCardButton0 else additionalCardButton1;

        // ハンドラ設定
        imageView.setOnClickListener {
            // 一律非選択の見た目に設定
            setAlphaFunc();
            // 対象のメガミのalphaを1にして選択の見た目にする
            imageView.alpha = 1F;
            // カード名ボタンの見た目設定
            setButtonsView(megamiButtonList, cardCsvList);
            // カード表示画面に遷移するためのハンドラ設定
            setButtonsHandler(megamiButtonList, cardCsvList);
            // チェックボックスにハンドラ設定
            setCheckBoxHandlers(cardCsvList, isLeft);
            // 追加札ボタン設定
            setAdditionalCardButton(additionalButton, extraCardCsvList, isLeft);
            // メガミ選択状態を更新
            if (isLeft) {
                megami0Name = megamiName;
            } else {
                megami1Name = megamiName;
            }
        }
    }

    /**
     * 追加札ボタンの設定
     * @param additionalButton 追加札一覧ボタン。
     * @param extraCardList 追加札一覧。
     * @param isLeft 左側のメガミかどうか。
     */
    fun setAdditionalCardButton(
        additionalButton: ImageButton,
        extraCardList: List<Map<String, String>>,
        isLeft: Boolean
    ) {
        if (isExtraExist(extraCardList) == false) {
            // 追加札一覧ボタンを非活性
            additionalButton.visibility = INVISIBLE;
        } else {
            // 追加札一覧ボタンを活性化
            additionalButton.visibility = VISIBLE;
            setShowAdditionalCardsHandler(additionalButton, extraCardList, isLeft);
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

    /**
     * 追加札一覧の画面を表示するハンドラ
     * @param extraCardCsvList 追加札一覧
     * @param isLeft 左側のメガミかどうか
     */
    fun showAdditionalCardsHandler(extraCardCsvList: List<Map<String, String>>, isLeft: Boolean) {
        val dialogArgs = Bundle();
        dialogArgs.putStringArray("extraCardList", convertCsvToStringArray(extraCardCsvList));
        additionalCardDialog.arguments = dialogArgs;
        additionalCardDialog.show(supportFragmentManager, "additionalCard_dialog");
    }

    /**
     * 追加札ボタンにハンドラを設定
     * @param additionalCardButton ハンドラ設定対象の追加札ボタン
     * @param extraCardList 追加札一覧。
     * @param isLeft 左側のメガミかどうか
     */
    fun setShowAdditionalCardsHandler(
        additionalCardButton: ImageButton,
        extraCardCsvList: List<Map<String, String>>,
        isLeft: Boolean
    ) {
        additionalCardButton.setOnClickListener {
            showAdditionalCardsHandler(extraCardCsvList, isLeft);
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
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

        // カード情報の取得
        // オリジン、A-1、A-2に分類されたcsvDataを取得
        val classifiedCardList0 = getClassifiedCsvData(res.getIdentifier(megami0Name, "raw", packageName), res, context, megami0Name!!);
        val classifiedCardList1 = getClassifiedCsvData(res.getIdentifier(megami1Name, "raw", packageName), res, context, megami1Name!!);
        // オリジン
        val originCardList0 = classifiedCardList0.get("origin");
        val originCardList1 = classifiedCardList1.get("origin");
        // a1
        val a1CardList0 = classifiedCardList0.get("a1");
        val a1CardList1 = classifiedCardList1.get("a1");
        // a2
        val a2CardList0 = classifiedCardList0.get("a2");
        val a2CardList1 = classifiedCardList1.get("a2");
        // 追加札
        // オリジン
        val originExtraCardList0 = classifiedCardList0.get("extra-origin");
        val originExtraCardList1 = classifiedCardList1.get("extra-origin");
        // a1
        val a1ExtraCardList0 = classifiedCardList0.get("extra-a1");
        val a1ExtraCardList1 = classifiedCardList1.get("extra-a1");
        // a2
        val a2ExtraCardList0 = classifiedCardList0.get("extra-a2");
        val a2ExtraCardList1 = classifiedCardList1.get("extra-a2");

        if (originCardList0 == null || originCardList1 == null) {
            return;
        }

        // ボタン一覧を取得する
        val megamiButtonList0 = getLeftMegamiCardButtons();
        val megamiButtonList1 = getRightMegamiCardButtons();

        // 各ボタン初期化処理
        // オリジン
        setMegamiButton(megamiImage0_edit, megami0Name!!, megamiButtonList0, originCardList0, originExtraCardList0!!, true);
        setMegamiButton(megamiImage1_edit, megami1Name!!, megamiButtonList1, originCardList1, originExtraCardList1!!, false);

        // A1
        if (isAnotherExist(a1CardList0)) {
            setMegamiButton(megamiImage0_A1_edit, megami0Name + "_a1", megamiButtonList0, a1CardList0!!, a1ExtraCardList0!!, true);
        }
        if (isAnotherExist(a1CardList1)) {
            setMegamiButton(megamiImage1_A1_edit, megami1Name + "_a1", megamiButtonList1, a1CardList1!!, a1ExtraCardList1!!, false);
        }

        // A2
        if (isAnotherExist(a2CardList0)) {
            setMegamiButton(megamiImage0_A2_edit, megami0Name + "_a2", megamiButtonList0, a2CardList0!!, a2ExtraCardList0!!, true);
        }
        if (isAnotherExist(a2CardList1)) {
            setMegamiButton(megamiImage1_A2_edit, megami1Name + "_a2", megamiButtonList1, a2CardList1!!, a2ExtraCardList1!!, false);
        }

        // 初期状態を設定するためにonClickを実行
        megamiImage0_edit.performClick();
        megamiImage1_edit.performClick();

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