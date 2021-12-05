package com.example.furuyonideckmanager

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_deck.*
import java.io.*

class ViewDeckActivity : AppCompatActivity() {
    fun readOrigFile(fileId: Int): List<List<String>> {
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

    // TODO: 後で共通関数化
    fun readFile(fileName: String): List<List<String>> {
        var separatedList = listOf<List<String>>();
        val file = File(applicationContext.filesDir, fileName);
        if (file.exists()) {
            file.bufferedReader().use {
                val content = it.readText();
                // 空欄行削除
                // TODO: ここは後でロジック見直したい
                val dataList = content.split('\n');
                separatedList = dataList.map{it.split(',')}
            }
        }

        return separatedList;
    }

    // TODO: 後で共通関数化
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
                target.setImageBitmap(bitmap);            }
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

    fun setButtonsView(buttons: Array<Map<String, Button?>>, csvData: List<List<String>>, chosenCardList: List<List<String>>) {
        for (i in csvData.indices) {
            val targetData = csvData[i];
            val targetButtons = buttons[i];
            // カード名設定
            targetButtons.get("card")?.setText(targetData[1]);
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData[2]);
            setButtonStyles(targetButtons.get("type1"), targetData[3]);
            // 活性か非活性か（リストにあれば活性）
            // TODO: ここの処理は別関数に分けたほうが綺麗な気がする
            val isEnable = chosenCardList.any{it[1] == targetData[1]}
            targetButtons.get("card")?.isEnabled = isEnable;
            if (isEnable == false) {
                targetButtons.get("card")?.alpha = 0.75F;
                targetButtons.get("type0")?.alpha = 0.25F;
                targetButtons.get("type1")?.alpha = 0.25F;
            }
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_deck);

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
        setMegamiImage(megami0 + ".jpg", "left");
        setMegamiImage(megami1 + ".jpg", "right");

        // カード情報の取得
        val megamiCardList0 = readOrigFile(getResources().getIdentifier(megami0, "raw", getPackageName()));
        val megamiCardList1 = readOrigFile(getResources().getIdentifier(megami1, "raw", getPackageName()));
        var deckCardList: List<List<String>> = listOf();
        // csvファイルからデッキ情報を読み込む
        if (deckCSV != null) {
            deckCardList = readFile(deckCSV);
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