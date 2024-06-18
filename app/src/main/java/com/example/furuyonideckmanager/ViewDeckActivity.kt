package com.example.furuyonideckmanager

import CsvUtil.getClassifiedCsvData
import CsvUtil.readInternalFile
import PartsUtil.setButtonStyles
import SetImageUtil.setImageToImageView
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_view_deck.*

class EditConfirmDialog: DialogFragment() {
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

        val deckName = arguments?.getString("DECK_TITLE");
        val fileName = arguments?.getString("DECK_FILENAME");
        val chosenMegami = arguments?.getStringArray("CHOSEN_MEGAMI");
        val data = arguments?.getStringArray("DECK_DATA");

        builder.setMessage("デッキ「$deckName」を編集します。よろしいですか？");

        builder.setPositiveButton("編集") {_, _ ->
            val intent = Intent(context, ChooseCardsActivity::class.java);

            // データの受け渡し
            intent.putExtra("DECK_FILENAME", fileName);
            intent.putExtra("CHOSEN_MEGAMI", chosenMegami);
            intent.putExtra("DECK_DATA", data);
            intent.putExtra("DECK_NAME", deckName);

            // 編集画面に画面遷移
            startActivity(intent);
        }
        builder.setNegativeButton("キャンセル") {_, _ ->
            listener?.cancel();
        }
        return builder.create();
    }
}

class ViewDeckActivity : AppCompatActivity(), CommentDialog.Listener {
    private lateinit var realm: Realm;
    val dialog = CommentDialog();

    override fun update() {
        // 画面のコメント部分を更新
        comment.setText(dialog.getInput());

        // DBを更新
        // 更新対象のデータを検索
        var deckCSV = intent.getStringExtra("DECK_FILENAME");
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
        var fileName = intent.getStringExtra("DECK_FILENAME");
        var editable = intent.getBooleanExtra("EDITABLE", true);

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
        val classifiedCardList0 = getClassifiedCsvData(res.getIdentifier(originMegamiName0, "raw", packageName), res, context, originMegamiName0);
        val classifiedCardList1 = getClassifiedCsvData(res.getIdentifier(originMegamiName1, "raw", packageName), res, context, originMegamiName1);
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
        if (fileName != null) {
            deckCardList = readInternalFile(fileName, applicationContext);
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
        var targetData = realm.where<Deck>().equalTo("fileName", fileName).findFirst();
        comment.setText(targetData?.comment);
        // コメント部分にコメント編集用のハンドラを設定
        if (editable) {
            setCommentHandler();
        }

        // editable = falseの時は鉛筆アイコンは非表示
        if (editable == false) {
            editButton.visibility = View.INVISIBLE;
        }

        // 編集ボタンのハンドラ設定
        editButton?.setOnClickListener {
            val dialog = EditConfirmDialog();
            // データ受け渡し
            val dialogArgs = Bundle();
            dialogArgs.putString("DECK_TITLE", deckTitle);
            dialogArgs.putString("DECK_FILENAME", fileName);
            dialogArgs.putStringArray("CHOSEN_MEGAMI", chosenMegami);
            val data = deckCardList.map{elem -> elem.joinToString(",")}.toTypedArray();
            dialogArgs.putStringArray("DECK_DATA", data);

            dialog.arguments = dialogArgs;
            // ダイアログ表示
            dialog.show(supportFragmentManager, "edit_dialog");
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