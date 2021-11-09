package com.example.furuyonideckmanager

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_show_card.*
import java.io.IOException
import java.io.InputStream

class ShowCardActivity : AppCompatActivity() {
    // TODO: 後で共通関数化
    fun setCardImage(imageName: String) {
        var instream: InputStream? = null;
        var bitmap: Bitmap? = null;

        try {
            instream = assets.open(imageName);
            if (instream != null) {
                bitmap = BitmapFactory.decodeStream(instream);
                cardImage.setImageBitmap(bitmap);
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_card)

        // 画像データの取得
        var image = intent.getStringExtra("IMAGE_FILE_NAME")
        if (image != null) {
            // 画像の設定
            setCardImage(image);
        }
    }
}