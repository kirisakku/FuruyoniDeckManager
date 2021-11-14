package com.example.furuyonideckmanager

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_choose_cards.*
import java.io.*

class ChooseCardsActivity : AppCompatActivity() {
    // 通常札の選択されたカード
    val chosenNormalCards = mutableListOf<List<String>>();
    // 切札の選択されたカード
    val chosenSpecialCards = mutableListOf<List<String>>();

    fun readFile(fileId: Int): List<List<String>> {
        val res: Resources = this.getResources();
        var bufferReader: BufferedReader? = null;
        var separatedList = mutableListOf<List<String>>();
        try {
            try {
                val inputStream = res.openRawResource(fileId);
                bufferReader = BufferedReader(InputStreamReader(inputStream));
                var str = bufferReader.readLine();
                while(str != null) {
                    separatedList.add(str.split(','))
                    str = bufferReader.readLine();
                }
            } finally {
                if (bufferReader != null) {
                    bufferReader.close();
                }
            }
        } catch (e: IOException) {
            Toast.makeText(this, "読み込み失敗", Toast.LENGTH_SHORT).show();
        }

        return separatedList;
    }

    fun setMegamiImage(imageName: String, position: String) {
        var instream: InputStream? = null;
        var bitmap: Bitmap? = null;
        var target: ImageView? = null;

        // 画像を設定するターゲット取得
        // TODO: 後で関数化
        if (position == "left") {
            target = megamiImage1;
        } else if (position == "right") {
            target = megamiImage2;
        }

        try {
            instream = assets.open(imageName);
            if (instream != null && target != null) {
                bitmap = BitmapFactory.decodeStream(instream);
                target.setImageBitmap(bitmap);
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    fun getLeftMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to megami0_card0, "type0" to megami0_type00, "type1" to megami0_type01),
            mapOf("card" to megami0_card1, "type0" to megami0_type10, "type1" to megami0_type11),
            mapOf("card" to megami0_card2, "type0" to megami0_type20, "type1" to megami0_type21),
            mapOf("card" to megami0_card3, "type0" to megami0_type30, "type1" to megami0_type31),
            mapOf("card" to megami0_card4, "type0" to megami0_type40, "type1" to megami0_type41),
            mapOf("card" to megami0_card5, "type0" to megami0_type50, "type1" to megami0_type51),
            mapOf("card" to megami0_card6, "type0" to megami0_type60, "type1" to megami0_type61),
            mapOf("card" to megami0_s_card0, "type0" to megami0_s_type00, "type1" to megami0_s_type01),
            mapOf("card" to megami0_s_card1, "type0" to megami0_s_type10, "type1" to megami0_s_type11),
            mapOf("card" to megami0_s_card2, "type0" to megami0_s_type20, "type1" to megami0_s_type21),
            mapOf("card" to megami0_s_card3, "type0" to megami0_s_type30, "type1" to megami0_s_type31)
        );
    }

    fun getRightMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to megami1_card0, "type0" to megami1_type00, "type1" to megami1_type01),
            mapOf("card" to megami1_card1, "type0" to megami1_type10, "type1" to megami1_type11),
            mapOf("card" to megami1_card2, "type0" to megami1_type20, "type1" to megami1_type21),
            mapOf("card" to megami1_card3, "type0" to megami1_type30, "type1" to megami1_type31),
            mapOf("card" to megami1_card4, "type0" to megami1_type40, "type1" to megami1_type41),
            mapOf("card" to megami1_card5, "type0" to megami1_type50, "type1" to megami1_type51),
            mapOf("card" to megami1_card6, "type0" to megami1_type60, "type1" to megami1_type61),
            mapOf("card" to megami1_s_card0, "type0" to megami1_s_type00, "type1" to megami1_s_type01),
            mapOf("card" to megami1_s_card1, "type0" to megami1_s_type10, "type1" to megami1_s_type11),
            mapOf("card" to megami1_s_card2, "type0" to megami1_s_type20, "type1" to megami1_s_type21),
            mapOf("card" to megami1_s_card3, "type0" to megami1_s_type30, "type1" to megami1_s_type31)
        );
    }

    fun setButtonStyles(button: Button?, type: String) {
        when(type) {
            "攻撃" -> {
                button?.setBackgroundResource(R.drawable.circle_red);
                button?.setText("攻");
            };
            "行動" -> {
                button?.setBackgroundResource(R.drawable.circle_blue)
                button?.setText("行");
            };
            "付与" -> {
                button?.setBackgroundResource(R.drawable.circle_green)
                button?.setText("付");
            };
            "全力" -> {
                button?.setBackgroundResource(R.drawable.circle_yellow)
                button?.setText("全");
            };
            "対応" -> {
                button?.setBackgroundResource(R.drawable.circle_purple)
                button?.setText("対");
            };
            else -> {
                button?.setVisibility(View.INVISIBLE);
            }
        }
    }

    fun setButtonsView(buttons: Array<Map<String, Button?>>, csvData: List<List<String>>) {
        for (i in csvData.indices) {
            val targetData = csvData[i];
            val targetButtons = buttons[i];
            // カード名設定
            targetButtons.get("card")?.setText(targetData[1]);
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData[2]);
            setButtonStyles(targetButtons.get("type1"), targetData[3]);
        }
    }

    fun setButtonsHandler(buttons: Array<Map<String, Button?>>, csvData: List<List<String>>) {
        for (i in csvData.indices) {
            val targetData = csvData[i];
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

    fun setRegisterButtonEnable() {
        // 作成ボタンの活性状態を変える
        if (chosenNormalCards.size == 7 && chosenSpecialCards.size == 3) {
            registerDeck.setEnabled(true);
        } else {
            registerDeck.setEnabled(false);
        }
    }

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

    fun setCheckBoxHandlers(csvData: List<List<String>>, isRight: Boolean) {
        var checkBoxes = if (isRight == true) getRightMegamiCheckBoxes() else getLeftMegamiCheckBoxes();

        // 通常カードのチェックボックスにハンドラ設定
        for (i in 0..6) {
            val checkBox = checkBoxes[i];
            checkBox.setOnCheckedChangeListener {_, isChecked ->
                val targetData = csvData[i];
                if (isChecked) {
                    chosenNormalCards.add(targetData);
                } else {
                    chosenNormalCards.remove(targetData);
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
                    chosenSpecialCards.add(csvData[i]);
                } else {
                    chosenSpecialCards.remove(csvData[i]);
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_cards)

        var chosenMegami = intent.getStringArrayExtra("CHOSEN_MEGAMI")
        var megami0 = chosenMegami?.get(0);
        var megami1 = chosenMegami?.get(1);
        if (megami0 == null || megami1 ==null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // メガミ画像の設定
        setMegamiImage(megami0 + ".jpg", "left");
        setMegamiImage(megami1 + ".jpg", "right");

        // カード情報の取得
        val megamiCardList0 = readFile(getResources().getIdentifier(megami0, "raw", getPackageName()));
        val megamiCardList1 = readFile(getResources().getIdentifier(megami1, "raw", getPackageName()));

        // 画面の初期化
        // ボタン一覧を取得する
        val megamiButtonList0 = getLeftMegamiCardButtons();
        val megamiButtonList1 = getRightMegamiCardButtons();
        // ボタンの見た目設定
        setButtonsView(megamiButtonList0, megamiCardList0);
        setButtonsView(megamiButtonList1, megamiCardList1);

        // カード表示画面に遷移するためのハンドラ設定
        setButtonsHandler(megamiButtonList0, megamiCardList0);
        setButtonsHandler(megamiButtonList1, megamiCardList1);

        // チェックボックスにハンドラ設定
        setCheckBoxHandlers(megamiCardList0, true);
        setCheckBoxHandlers(megamiCardList0, false);

        // 登録
        registerDeck.setOnClickListener {
            val dialog = DeckNameDialog();
            dialog.show(supportFragmentManager, "register_dialog")
        }
    }
}