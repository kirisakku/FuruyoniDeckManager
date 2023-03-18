package com.example.furuyonideckmanager

import CsvUtil.getClassifiedCsvData
import CsvUtil.readInternalFile
import PartsUtil.setButtonStyles
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_view_deck.*

class ViewDeckActivity : AppCompatActivity(), CommentDialog.Listener {
    private lateinit var realm: Realm;
    val dialog = CommentDialog();

    override fun update() {
        // 画面のコメント部分を更新
        comment.setText(dialog.getInput());

        // DBを更新
        // 更新対象のデータを検索
        var deckCSV = intent.getStringExtra("DECK_CSV");
        var targetData = realm.where<Deck>().equalTo("fileName", deckCSV).findFirst();
        realm.executeTransaction {
            targetData?.comment = dialog.getInput().toString();
        }
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 処理なし
    }

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
     * @param chosenCardCsv 選ばれたカードのcsvデータ
     */
    fun setButtonsView(buttons: Array<Map<String, Button?>>, cardCsv: List<Map<String, String>>, chosenCardCsv: List<List<String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];
            // カード名設定
            targetButtons.get("card")?.setText(targetData.get("actionName"));
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData.get("mainType").orEmpty());
            setButtonStyles(targetButtons.get("type1"), targetData.get("subType").orEmpty());
            // 活性か非活性か（リストにあれば活性）
            // TODO: ここの処理は別関数に分けたほうが綺麗な気がする
            val isEnable = chosenCardCsv.any{it[1] == targetData.get("actionName")}
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
     * @oaran context コンテキスト。
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
     * コメントにハンドラを設定。
     */
    fun setCommentHandler() {
        comment.setOnClickListener {
            val dialogArgs = Bundle();
            val comment = comment.text.toString();
            dialogArgs.putString("comment", comment);
            dialog.arguments = dialogArgs;
            dialog.show(supportFragmentManager, "comment_dialog");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deck);
        realm = Realm.getDefaultInstance();

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
        val splitedName0 = megami0.split('_');
        val splitedName1 = megami1.split('_');
        // オリジナルのメガミ名を取得
        val originMegamiName0 = splitedName0[0];
        val originMegamiName1 = splitedName1[0];
        // オリジン、A-1、A-2に分類されたcsvDataを取得
        val classifiedCardList0 = getClassifiedCsvData(res.getIdentifier(originMegamiName0, "raw", packageName), res, context);
        val classifiedCardList1 = getClassifiedCsvData(res.getIdentifier(originMegamiName1, "raw", packageName), res, context);
        // 対応するカードリストを取得
        var cardList0 = classifiedCardList0.get("origin");
        var cardList1 = classifiedCardList1.get("origin");
        if (splitedName0.count() > 1) {
            cardList0 = classifiedCardList0.get(splitedName0[1]);
        }
        if (splitedName1.count() > 1) {
            cardList1 = classifiedCardList1.get(splitedName1[1]);
        }

        if (cardList0 == null || cardList1 == null) {
            return;
        }

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
        setButtonsView(megamiButtonList0, cardList0, deckCardList);
        setButtonsView(megamiButtonList1, cardList1, deckCardList);

        // カード表示画面に遷移するためのハンドラ設定
        setButtonsHandler(megamiButtonList0, cardList0);
        setButtonsHandler(megamiButtonList1, cardList1);

        // コメントの設定
        var targetData = realm.where<Deck>().equalTo("fileName", deckCSV).findFirst();
        comment.setText(targetData?.comment);
        // コメント部分にコメント編集用のハンドラを設定
        setCommentHandler();
    }

    override fun onDestroy() {
        super.onDestroy();
        realm.close();
    }
}