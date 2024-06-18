package com.example.furuyonideckmanager

import CsvUtil.convertCsvToStringArray
import CsvUtil.getChosenMegamiCsvList
import CsvUtil.getClassifiedCsvData
import CsvUtil.isAnotherExist
import PartsUtil.*
import SetImageUtil.setImageToImageView
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmObject.getRealm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_choose_cards.*
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card0_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card1_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card2_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card3_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card4_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card5_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_card6_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_card0_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_card1_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_card2_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_card3_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type00_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type01_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type10_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type11_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type20_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type21_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type30_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_s_type31_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type00_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type01_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type10_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type11_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type20_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type21_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type30_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type31_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type40_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type41_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type50_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type51_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type60_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami0_type61_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card0_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card1_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card2_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card3_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card4_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card5_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_card6_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_card0_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_card1_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_card2_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_card3_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type00_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type01_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type10_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type11_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type20_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type21_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type30_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_s_type31_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type00_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type01_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type10_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type11_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type20_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type21_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type30_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type31_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type40_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type41_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type50_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type51_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type60_view
import kotlinx.android.synthetic.main.activity_choose_cards.megami1_type61_view
import kotlinx.android.synthetic.main.activity_view_deck.*
import kotlinx.android.synthetic.main.deck_item.*
import java.io.*
import java.util.*

class ChooseCardsActivity : AppCompatActivity(), DeckNameDialog.Listener {
    private lateinit var realm: Realm;

    // 通常札の選択されたカード
    val chosenNormalCards = mutableListOf<Map<String, String>>();
    // 切札の選択されたカード
    val chosenSpecialCards = mutableListOf<Map<String, String>>();
    // 選ばれたメガミその1
    var megami0Name: String? = null;
    // 選ばれたメガミその2
    var megami1Name: String? = null;
    // メガミの種類その1
    var megamiKind0: String = "origin";
    // メガミの種類その2
    var megamiKind1: String = "origin";
    // ダイアログ
    val dialog = DeckNameDialog();
    // 追加札表示ダイアログ
    val additionalCardDialog = AdditionalCardDialogs();

    // 画面上のチェックボックスを管理するマップ
    var checkBoxMap: Map<String, List<CheckBox>>? = null;

    // 編集状態かどうか
    var isEdit = false;
    // ファイル名（編集時のみ値あり）
    var fileName: String? = null;
    // デッキ名（編集時のみ値あり）
    var deckName: String? = null;
    // 強制的にメガミボタンを非活性にするかどうか
    var forceDisableButtons = false;

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
                    // noとメガミ情報だけ保持しておく
                    chosenNormalCards.add(mapOf("no" to targetData.get("no")!!, "megamiName" to targetData.get("megamiName")!!));
                } else {
                    chosenNormalCards.removeIf{it.get("no") == targetData.get("no") && it.get("megamiName") == targetData.get("megamiName")}
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
                    chosenSpecialCards.add(mapOf("no" to cardCsv[i].get("no")!!, "megamiName" to cardCsv[i].get("megamiName")!!));
                } else {
                    chosenSpecialCards.removeIf{it.get("no") == cardCsv[i].get("no") && it.get("megamiName") == cardCsv[i].get("megamiName")}
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
            megami0 = (if (megamiKind0 != "origin") megami0Name + "_" + megamiKind0 else megami0Name) ?: "";
            megami1 = (if (megamiKind1 != "origin") megami1Name + "_" + megamiKind1 else megami1Name) ?: "";
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
                if (!isEdit) {
                    addDeckToList(deckFileName);
                    Toast.makeText(applicationContext, "デッキを登録しました", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(applicationContext, "デッキを更新しました", Toast.LENGTH_SHORT).show();
                }
            }
            catch (e: IOException) {
                Toast.makeText(applicationContext, "デッキ登録 / 更新に失敗しました。もう1度試してみて下さい", Toast.LENGTH_LONG).show();
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
     * @param megamiKind メガミの種類（O, A1, A2)。
     * @param isLeft 左側のメガミかどうか。
     */
    @SuppressLint("ResourceAsColor")
    @RequiresApi(Build.VERSION_CODES.N)
    fun setMegamiButton(
        imageView: ImageView,
        megamiName: String,
        megamiButtonList: Array<Map<String, Button?>>,
        cardCsvList: List<Map<String, String>>,
        extraCardCsvList: List<Map<String, String>>,
        megamiKind: String,
        isLeft: Boolean
    ) {
        // 画像設定
        val imageName = if (megamiKind == "origin") "$megamiName.jpg" else megamiName + "_" + megamiKind + ".jpg";
        setImageToImageView(imageName, imageView, assets);

        // 一律非選択の見た目にするための関数を選択
        var setAlphaFunc = { -> setUnselectedAlpha(megamiImage0_edit, megamiImage0_A1_edit, megamiImage0_A2_edit)};

        if (isLeft == false) {
            setAlphaFunc = { -> setUnselectedAlpha(megamiImage1_edit, megamiImage1_A1_edit, megamiImage1_A2_edit)};
        }
        var additionalButton = if (isLeft)  additionalCardButton0 else additionalCardButton1;

        // ハンドラ設定
        imageView.setOnClickListener {
            if (!forceDisableButtons) {
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
                    megamiKind0 = megamiKind;
                } else {
                    megami1Name = megamiName;
                    megamiKind1 = megamiKind;
                }
            } else {
                // タップ時に警告を出す
                val layout = findViewById<View>(android.R.id.content);
                val snackBar = Snackbar.make(layout, "編集時はメガミタイプを変更できません", Snackbar.LENGTH_SHORT);
                snackBar.view.setBackgroundColor(Color.rgb(1, 135, 134));
                snackBar.show();
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
        val megami0 = if (megamiKind0 != "origin") megami0Name + "_" + megamiKind0 else megami0Name;
        val megami1 = if (megamiKind1 != "origin") megami1Name + "_" + megamiKind1 else megami1Name;

        val normalCsvDataList = getChosenMegamiCsvList(megami0!!, megami1!!, chosenNormalCards, resources, applicationContext);
        val specialCsvDataList = getChosenMegamiCsvList(megami0!!, megami1!!, chosenSpecialCards, resources, applicationContext);
        val normalCsvData = normalCsvDataList.joinToString("\n");
        val specialCsvData = specialCsvDataList.joinToString("\n");
        // 連結
        val resultCsvData = normalCsvData + "\n" + specialCsvData;
        // ファイル名
        fileName = if (fileName != null) fileName else UUID.randomUUID().toString() + ".csv";
        // データ保存
        saveFile(fileName!!, resultCsvData);

        if (isEdit) {
            // 編集時は元のデッキ参照画面に遷移
            val intent = Intent(this, ViewDeckActivity::class.java);

            // デッキ名を渡す
            intent.putExtra("DECK_TITLE", deckName);
            // ファイル名を渡す
            intent.putExtra("DECK_FILENAME", fileName);
            // メガミ情報を渡す
            val selectedMegamiArray: Array<String> = arrayOf(megami0!!, megami1!!);
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)

            startActivity(intent);
        } else {
            // 新規作成時
            val uuid = intent.getStringExtra("UUID");
            val target = intent.getStringExtra("TARGET");

            if (uuid != null && target != null) {
                // もしUUIDとTARGETがあれば三柱管理画面から遷移したのでデータを登録後、三柱管理画面に戻る
                val title = dialog.getInput().toString()
                DBUtil.registerSelectedDeck(realm, uuid, fileName!!, title, target);

                val intent0 = Intent(this, ThreeMegamiDeckRegisterActivity::class.java);
                intent0.putExtra("UUID", uuid);
                startActivity(intent0);
            } else {
                // 通常のデッキ登録の場合は、デッキ参照画面に遷移
                val intent1 = Intent(this, DeckListActivity::class.java);
                startActivity(intent1);
            }
        }
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

    /**
     * 初期状態のチェック
     * @param data すでに登録されているカードのデータ一覧
     */
    fun setCheck(data: Array<String>) {
        for (i in data.indices) {
            // csvデータを分解
            val targetData = data[i].split(',');
            // map形式に変換
            val dataMap = CsvUtil.convertCsvToMap(targetData);
            // 対象のチェックボックスを探す
            val index = if (dataMap.get("megamiName") == megami0Name) 0 else 1;
            val targetCheckBox = checkBoxMap?.get(dataMap.get("no"))?.get(index);
            // 対象のチェックボックスにチェックを入れる
            targetCheckBox?.isChecked = true;
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_cards)

        val res = resources;
        val context = applicationContext;

        realm = RealmManager.getRealm();

        var chosenMegami = intent.getStringArrayExtra("CHOSEN_MEGAMI");

        if (chosenMegami?.get(0) == null || chosenMegami?.get(1) == null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // カード情報の取得
        val splitedName0 = chosenMegami?.get(0)!!.split('_');
        val splitedName1 = chosenMegami?.get(1)!!.split('_');
        // オリジナルのメガミ名を取得
        megami0Name = splitedName0[0];
        megami1Name = splitedName1[0];
        // メガミの種類取得
        megamiKind0 = if (splitedName0.count() == 1) "origin" else if (splitedName0[1] == "a1") "a1" else "a2";
        megamiKind1 = if (splitedName1.count() == 1) "origin" else if (splitedName1[1] == "a1") "a1" else "a2";

        // 以下は編集時だけあるデータ
        fileName = intent.getStringExtra("DECK_FILENAME");
        val data = intent.getStringArrayExtra("DECK_DATA");
        deckName = intent.getStringExtra("DECK_NAME");
        // ファイル名の取得に成功した場合は編集状態とする
        if (fileName != null) {
            isEdit = true;
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
        setMegamiButton(megamiImage0_edit, megami0Name!!, megamiButtonList0, originCardList0, originExtraCardList0!!, "origin", true);
        setMegamiButton(megamiImage1_edit, megami1Name!!, megamiButtonList1, originCardList1, originExtraCardList1!!, "origin", false);

        // A1
        if (isAnotherExist(a1CardList0)) {
            setMegamiButton(megamiImage0_A1_edit, megami0Name!!, megamiButtonList0, a1CardList0!!, a1ExtraCardList0!!, "a1", true);
        }
        if (isAnotherExist(a1CardList1)) {
            setMegamiButton(megamiImage1_A1_edit, megami1Name!!, megamiButtonList1, a1CardList1!!, a1ExtraCardList1!!, "a1", false);
        }

        // A2
        if (isAnotherExist(a2CardList0)) {
            setMegamiButton(megamiImage0_A2_edit, megami0Name!!, megamiButtonList0, a2CardList0!!, a2ExtraCardList0!!, "a2", true);
        }
        if (isAnotherExist(a2CardList1)) {
            setMegamiButton(megamiImage1_A2_edit, megami1Name!!, megamiButtonList1, a2CardList1!!, a2ExtraCardList1!!, "a2", false);
        }

        // 初期状態を設定するためにonClickを実行
        val targetButton0 = if (splitedName0.count() == 1) megamiImage0_edit else if (splitedName0[1] == "a1") megamiImage0_A1_edit else megamiImage0_A2_edit;
        val targetButton1 = if (splitedName1.count() == 1) megamiImage1_edit else if (splitedName1[1] == "a1") megamiImage1_A1_edit else megamiImage1_A2_edit;
        if (targetButton0 != null && targetButton1 != null) {
            targetButton0.performClick();
            targetButton1.performClick();
            // 編集の場合は以降ボタンが動作しないように抑止する
            if (isEdit) {
                forceDisableButtons = true;
            }
        }

        // 三柱管理画面から遷移した場合は種別切り替えを無効にする
        val uuid = intent.getStringExtra("UUID");
        if (uuid != null) {
            if (megamiKind0 != "origin") {
                megamiImage0_edit?.isEnabled = false;
            }
            if (megamiKind0 != "a1") {
                megamiImage0_A1_edit?.isEnabled = false;
            }
            if (megamiKind0 != "a2") {
                megamiImage0_A2_edit?.isEnabled = false;
            }
            if (megamiKind1 != "origin") {
                megamiImage1_edit?.isEnabled = false;
            }
            if (megamiKind1 != "a1") {
                megamiImage1_A1_edit?.isEnabled = false;
            }
            if (megamiKind1 != "a2") {
                megamiImage1_A2_edit?.isEnabled = false;
            }
        }

        // データがある場合はチェック状態をセット
        if (data != null) {
            // チェックボックスとカードのmapping
            checkBoxMap = mapOf (
                "N-1" to listOf(megami0_check0, megami1_check0),
                "N-2" to listOf(megami0_check1, megami1_check1),
                "N-3" to listOf(megami0_check2, megami1_check2),
                "N-4" to listOf(megami0_check3, megami1_check3),
                "N-5" to listOf(megami0_check4, megami1_check4),
                "N-6" to listOf(megami0_check5, megami1_check5),
                "N-7" to listOf(megami0_check6, megami1_check6),
                "S-1" to listOf(megami0_s_check0, megami1_s_check0),
                "S-2" to listOf(megami0_s_check1, megami1_s_check1),
                "S-3" to listOf(megami0_s_check2, megami1_s_check2),
                "S-4" to listOf(megami0_s_check3, megami1_s_check3)
            );

            setCheck(data);
        }

        // 編集時はボタンのテキスト変更
        if (isEdit) {
            registerDeck.setText("デッキを更新");
        }

        // 登録
        registerDeck.setOnClickListener {
            if (!isEdit) {
                // 新規登録時
                dialog.show(supportFragmentManager, "register_dialog");
            } else {
                // 編集時
                register();
            }
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