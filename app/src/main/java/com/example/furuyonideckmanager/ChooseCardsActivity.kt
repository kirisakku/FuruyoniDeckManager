package com.example.furuyonideckmanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_choose_cards.*
import java.io.IOException
import java.io.InputStream

class ChooseCardsActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_cards)

        // メガミ画像の設定
        // データの取得
        var chosenMegami = intent.getStringArrayExtra("CHOSEN_MEGAMI")
        setMegamiImage(chosenMegami?.get(0) + ".jpg", "left");
        setMegamiImage(chosenMegami?.get(1) + ".jpg", "right");
    }
}