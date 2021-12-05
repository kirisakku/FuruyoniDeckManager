package com.example.furuyonideckmanager

import CsvUtil.readInternalFile
import PartsUtil.convertDpToPixel
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_deck_list.*

class DeckListActivity : AppCompatActivity() {
    /**
     * データが無い時の画面を生成。
     */
    fun createEmptyView() {
        noDeckError0.visibility = VISIBLE;
        noDeckError1.visibility = VISIBLE;
        registerDeckButton.visibility = VISIBLE;
        registerDeckButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java);
            startActivity(intent);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        // CSVデータ読み込み
        val deckList = readInternalFile("deckList.csv", applicationContext);
        val vLinearLayout = findViewById<LinearLayout>(R.id.vLinearLayout);

        //　データが無ければ専用の画面にする
        if (deckList.size == 0) {
            createEmptyView();
            return;
        }

        deckList.forEach{elem ->
            // 横整列レイアウト作成
            val hLinearLayout = LinearLayout(this);
            hLinearLayout.orientation = LinearLayout.HORIZONTAL;

            // 画像追加
            val imageView0 = ImageView(this);
            val imageView1 = ImageView(this);
            // TODO: 後で配列ではなくマップにしたいかも
            setImageToImageView(elem[1] + ".jpg", imageView0, assets);
            setImageToImageView(elem[2] + ".jpg", imageView1, assets);

            // TODO: 後でここのコード綺麗にしたい
            val layoutParams = ViewGroup.LayoutParams(convertDpToPixel(45, this), convertDpToPixel(45, this));
            imageView0.layoutParams = layoutParams;
            imageView1.layoutParams = layoutParams;
            val marginParams = ViewGroup.MarginLayoutParams(imageView1.layoutParams);
            marginParams.setMargins(convertDpToPixel(8, this), 0, convertDpToPixel(8, this), 0);
            imageView1.layoutParams = marginParams;

            // ボタン追加
            val button = Button(this);
            button.setText(elem[0]);
            button.width = convertDpToPixel(225, this);
            button.setOnClickListener {
                // 画面遷移
                val intent = Intent(this, ViewDeckActivity::class.java);
                // デッキ名を渡す
                intent.putExtra("DECK_TITLE", elem[0]);
                // デッキ情報のcsvを渡す
                intent.putExtra("DECK_CSV", elem[3]);
                // メガミ情報を渡す
                val selectedMegamiArray: Array<String> = arrayOf(elem[1], elem[2]);
                intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
                startActivity(intent);
            }

            // 作成した要素を全て横整列レイアウトに足す
            hLinearLayout.addView(imageView0);
            hLinearLayout.addView(imageView1);
            hLinearLayout.addView(button);

            // 作成した横整列レイアウトを縦レイアウトに足す
            vLinearLayout.addView(hLinearLayout);
        }
    }
}