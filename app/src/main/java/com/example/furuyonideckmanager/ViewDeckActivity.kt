package com.example.furuyonideckmanager

import CsvUtil.readInternalFile
import CsvUtil.readRawCsv
import PartsUtil.setButtonStyles
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_view_deck.*

class ViewDeckActivity : AppCompatActivity() {
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
     * @param chosenCardCsv 選ばれたーカードのcsvデータ
     */
    fun setButtonsView(buttons: Array<Map<String, Button?>>, cardCsv: List<List<String>>, chosenCardCsv: List<List<String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];
            // カード名設定
            targetButtons.get("card")?.setText(targetData[1]);
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData[2]);
            setButtonStyles(targetButtons.get("type1"), targetData[3]);
            // 活性か非活性か（リストにあれば活性）
            // TODO: ここの処理は別関数に分けたほうが綺麗な気がする
            val isEnable = chosenCardCsv.any{it[1] == targetData[1]}
            targetButtons.get("card")?.isEnabled = isEnable;
            if (isEnable == false) {
                targetButtons.get("card")?.alpha = 0.75F;
                targetButtons.get("type0")?.alpha = 0.25F;
                targetButtons.get("type1")?.alpha = 0.25F;
            }
        }
    }

    /**
     * ボタンにハンドラを設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     */
    fun setButtonsHandler(buttons: Array<Map<String, Button?>>, cardCsv: List<List<String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];

            // ハンドラ定義
            val cardButton = targetButtons.get("card");
            cardButton?.setOnClickListener {
                // 画面遷移
                val intent = Intent(this, ShowCardActivity::class.java);
                // 画像データ
                val image = targetData[4];
                // 画像データを渡す
                intent.putExtra("IMAGE_FILE_NAME", image);
                startActivity(intent);
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deck);

        val res = resources;
        val context = applicationContext;

        // データの取り出し
        var deckTitle = intent.getStringExtra("DECK_TITLE");
        var chosenMegami = intent.getStringArrayExtra("CHOSEN_MEGAMI");
        var deckCSV = intent.getStringExtra("DECK_CSV");

        // タイトル設定
        deckName.setText(deckTitle);
        // 画像設定
        val megami0 = chosenMegami?.get(0);
        val megami1 = chosenMegami?.get(1);
        if (megami0 == null || megami1 ==null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // メガミ画像の設定
        setImageToImageView(megami0 + ".jpg", megamiImage0_view, assets);
        setImageToImageView(megami1 + ".jpg", megamiImage1_view, assets);

        // カード情報の取得
        val megamiCardList0 = readRawCsv(res.getIdentifier(megami0, "raw", packageName), res, context);
        val megamiCardList1 = readRawCsv(res.getIdentifier(megami1, "raw", packageName), res, context);
        var deckCardList: List<List<String>> = listOf();
        // csvファイルからデッキ情報を読み込む
        if (deckCSV != null) {
            deckCardList = readInternalFile(deckCSV, applicationContext);
        }

        // 画面の初期化
        // ボタン一覧を取得する
        val megamiButtonList0 = getLeftMegamiCardButtons();
        val megamiButtonList1 = getRightMegamiCardButtons();
        // ボタンの見た目設定
        setButtonsView(megamiButtonList0, megamiCardList0, deckCardList);
        setButtonsView(megamiButtonList1, megamiCardList1, deckCardList);

        // カード表示画面に遷移するためのハンドラ設定
        setButtonsHandler(megamiButtonList0, megamiCardList0);
        setButtonsHandler(megamiButtonList1, megamiCardList1);
    }
}