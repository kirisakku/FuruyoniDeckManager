package com.example.furuyonideckmanager

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import java.io.*

class DeckListActivity : AppCompatActivity() {
    fun readFile(): List<List<String>> {
        var separatedList = listOf<List<String>>();
        val file = File(applicationContext.filesDir, "deckList.csv");
        if (file.exists()) {
            file.bufferedReader().use {
                val content = it.readText();
                // 空欄行削除
                // TODO: ここは後でロジック見直したい
                val dataList = content.split('\n').dropLast(1);
                separatedList = dataList.map{it.split(',')}
            }
        }

        return separatedList;
    }

    fun setMegamiImage(target: ImageView, imageName: String) {
        var instream: InputStream? = null;
        var bitmap: Bitmap? = null;

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

    fun convertDpToPixel(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density;
        val result = ((dp * density) + 0.5).toInt();
        return result;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        // CSVデータ読み込み
        val deckList = readFile();
        val vLinearLayout = findViewById<LinearLayout>(R.id.vLinearLayout);
        deckList.forEach{elem ->
            // 横整列レイアウト作成
            val hLinearLayout = LinearLayout(this);
            hLinearLayout.orientation = LinearLayout.HORIZONTAL;

            // 画像追加
            val imageView0 = ImageView(this);
            val imageView1 = ImageView(this);
            // TODO: 後で配列ではなくマップにしたいかも
            setMegamiImage(imageView0, elem[1] + ".jpg");
            setMegamiImage(imageView1, elem[2] + ".jpg");

            // TODO: 後でここのコード綺麗にしたい
            val layoutParams = ViewGroup.LayoutParams(convertDpToPixel(this,45), convertDpToPixel(this,45));
            imageView0.layoutParams = layoutParams;
            imageView1.layoutParams = layoutParams;
            val marginParams = ViewGroup.MarginLayoutParams(imageView1.layoutParams);
            marginParams.setMargins(convertDpToPixel(this, 8), 0, convertDpToPixel(this, 8), 0);
            imageView1.layoutParams = marginParams;

            // ボタン追加
            val button = Button(this);
            button.setText(elem[0]);
            button.width = convertDpToPixel(this,225);
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