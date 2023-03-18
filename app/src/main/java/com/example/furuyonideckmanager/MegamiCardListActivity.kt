package com.example.furuyonideckmanager

import CsvUtil.convertCsvToStringArray
import CsvUtil.getClassifiedCsvData
import CsvUtil.isAnotherExist
import PartsUtil.*
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.android.synthetic.main.activity_megami_card_list.*
import kotlinx.android.synthetic.main.deck_item.*
import java.io.*
import java.util.*

class MegamiCardListActivity : AppCompatActivity() {
    private lateinit var realm: Realm;

    // 追加札表示ダイアログ
    val additionalCardDialog = AdditionalCardDialogs();

    /**
     * 左側のメガミのボタン情報を取得。
     * @return ボタンの種類とボタンのマップの配列を返します。
     */
    fun getMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to card0, "type0" to type00, "type1" to type01),
            mapOf("card" to card1, "type0" to type10, "type1" to type11),
            mapOf("card" to card2, "type0" to type20, "type1" to type21),
            mapOf("card" to card3, "type0" to type30, "type1" to type31),
            mapOf("card" to card4, "type0" to type40, "type1" to type41),
            mapOf("card" to card5, "type0" to type50, "type1" to type51),
            mapOf("card" to card6, "type0" to type60, "type1" to type61),
            mapOf("card" to s_card0, "type0" to s_type00, "type1" to s_type01),
            mapOf("card" to s_card1, "type0" to s_type10, "type1" to s_type11),
            mapOf("card" to s_card2, "type0" to s_type20, "type1" to s_type21),
            mapOf("card" to s_card3, "type0" to s_type30, "type1" to s_type31),
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
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun setMegamiButton(
        imageView: ImageView,
        megamiName: String,
        megamiButtonList: Array<Map<String, Button?>>,
        cardCsvList: List<Map<String, String>>,
        extraCardCsvList: List<Map<String, String>>
    ) {
        // 画像設定
        setImageToImageView("$megamiName.jpg", imageView, assets);

        // 一律非選択の見た目にするための関数を選択
        var setAlphaFunc = { -> setUnselectedAlpha(megamiImageOrigin, megamiImageA1, megamiImageA2)};

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
            // 追加札ボタン設定
            setAdditionalCardButton(additionalCardButton, extraCardCsvList);
        }
    }

    /**
     * 追加札ボタンの設定
     * @param additionalButton 追加札一覧ボタン。
     * @param extraCardList 追加札一覧。
     */
    fun setAdditionalCardButton(
        additionalButton: ImageButton,
        extraCardList: List<Map<String, String>>
    ) {
        if (isExtraExist(extraCardList) == false) {
            // 追加札一覧ボタンを非活性
            additionalButton.visibility = INVISIBLE;
        } else {
            // 追加札一覧ボタンを活性化
            additionalButton.visibility = VISIBLE;
            setShowAdditionalCardsHandler(additionalButton, extraCardList);
        }
    }

    /**
     * 追加札一覧の画面を表示するハンドラ
     * @param extraCardCsvList 追加札一覧
     */
    fun showAdditionalCardsHandler(extraCardCsvList: List<Map<String, String>>) {
        val dialogArgs = Bundle();
        dialogArgs.putStringArray("extraCardList", convertCsvToStringArray(extraCardCsvList));
        additionalCardDialog.arguments = dialogArgs;
        additionalCardDialog.show(supportFragmentManager, "additionalCard_dialog");
    }

    /**
     * 追加札ボタンにハンドラを設定
     * @param additionalCardButton ハンドラ設定対象の追加札ボタン
     * @param extraCardList 追加札一覧。
     */
    fun setShowAdditionalCardsHandler(
        additionalCardButton: ImageButton,
        extraCardCsvList: List<Map<String, String>>,
    ) {
        additionalCardButton.setOnClickListener {
            showAdditionalCardsHandler(extraCardCsvList);
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_megami_card_list)

        var config = RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        val res = resources;
        val context = applicationContext;

        var megamiName = intent.getStringExtra("CHOSEN_MEGAMI")
        if (megamiName == null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // カード情報の取得
        // オリジン、A-1、A-2に分類されたcsvDataを取得
        val classifiedCardList = getClassifiedCsvData(res.getIdentifier(megamiName, "raw", packageName), res, context);
        // オリジン
        val originCardList = classifiedCardList.get("origin");
        // a1
        val a1CardList = classifiedCardList.get("a1");
        // a2
        val a2CardList = classifiedCardList.get("a2");

        // 追加札
        // オリジン
        val originExtraCardList = classifiedCardList.get("extra-origin");
        // a1
        val a1ExtraCardList = classifiedCardList.get("extra-a1");
        // a2
        val a2ExtraCardList = classifiedCardList.get("extra-a2");

        if (originCardList == null) {
            return;
        }

        // ボタン一覧を取得する
        val megamiButtonList = getMegamiCardButtons();

        // 各ボタン初期化処理
        // オリジン
        setMegamiButton(megamiImageOrigin, megamiName, megamiButtonList, originCardList, originExtraCardList!!);

        // A1
        if (isAnotherExist(a1CardList)) {
            setMegamiButton(megamiImageA1, megamiName + "_a1", megamiButtonList, a1CardList!!, a1ExtraCardList!!);
        }

        // A2
        if (isAnotherExist(a2CardList)) {
            setMegamiButton(megamiImageA2, megamiName + "_a2", megamiButtonList, a2CardList!!, a2ExtraCardList!!);
        }

        // 初期状態を設定するためにonClickを実行
        megamiImageOrigin.performClick();
    }

    override fun onDestroy() {
        super.onDestroy();
        realm.close();
    }
}