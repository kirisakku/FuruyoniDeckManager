package com.example.furuyonideckmanager

import SetImageUtil.setImageToImageView
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_show_card.*

class ShowCardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_card)

        // 画像データの取得
        var image = intent.getStringExtra("IMAGE_FILE_NAME")
        if (image != null) {
            // 画像の設定
            setImageToImageView(image, cardImage, assets);
        }
    }
}