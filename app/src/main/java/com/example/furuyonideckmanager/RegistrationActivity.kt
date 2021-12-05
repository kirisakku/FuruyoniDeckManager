package com.example.furuyonideckmanager

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    // 選択済みのメガミを管理するリスト
    val selectedMegamiList: MutableList<String> = mutableListOf();

    /**
     * メガミのボタンタップ時の処理。
     * @param imageButton タップされたボタン。
     * @param megamiName タップされたメガミの名前。
     */
    fun onButtonTapped(imageButton: ImageButton, megamiName: String) {
        val isPressed = imageButton.isSelected();
        if (!isPressed) {
            // 枠線を付ける
            imageButton.setBackgroundColor(Color.parseColor("#FFC0BC"));
            // 選択済みリストに追加
            selectedMegamiList.add(megamiName);
        } else {
            // 枠線を除去する
            imageButton.setBackgroundColor(Color.parseColor("#00000000"));
            // 選択済みリストから除外
            selectedMegamiList.remove(megamiName);
        }

        // 押下状態を反転させる
        imageButton.setSelected(!isPressed);
        // 作成ボタンの活性状態を変える
        if (selectedMegamiList.size == 2) {
            startCreationButton.setEnabled(true);
        } else {
            startCreationButton.setEnabled(false);
        }
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    fun setClickListeners() {
        yurina.setOnClickListener {onButtonTapped(yurina, "yurina")}
        himika.setOnClickListener {onButtonTapped(himika, "himika")}
        tokoyo.setOnClickListener {onButtonTapped(tokoyo, "tokoyo")}
        oboro.setOnClickListener {onButtonTapped(oboro, "oboro")}
        yukihi.setOnClickListener {onButtonTapped(yukihi, "yukihi")}
        shinra.setOnClickListener {onButtonTapped(shinra, "shinra")}
        saine.setOnClickListener {onButtonTapped(saine, "saine")}
        hagane.setOnClickListener {onButtonTapped(hagane, "hagane")}
        chikage.setOnClickListener {onButtonTapped(chikage, "chikage")}
        kururu.setOnClickListener {onButtonTapped(kururu, "kururu")}
        sariya.setOnClickListener {onButtonTapped(sariya, "sariya")}
        utsuro.setOnClickListener {onButtonTapped(utsuro, "utsuro")}
        honoka.setOnClickListener {onButtonTapped(honoka, "honoka")}
        raira.setOnClickListener {onButtonTapped(raira, "raira")}
        korunu.setOnClickListener {onButtonTapped(korunu, "korunu")}
        yatsuha.setOnClickListener {onButtonTapped(yatsuha, "yatsuha")}
        hatsumi.setOnClickListener {onButtonTapped(hatsumi, "hatsumi")}
        mizuki.setOnClickListener {onButtonTapped(mizuki, "mizuki")}
        megumi.setOnClickListener {onButtonTapped(megumi, "megumi")}
        kanae.setOnClickListener {onButtonTapped(kanae, "kanae")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // 全メガミボタンに押下時ハンドラ追加
        setClickListeners();

        // 登録画面に遷移するためのボタンにハンドラ追加
        startCreationButton.setOnClickListener {
            val intent = Intent(this, ChooseCardsActivity::class.java);
            // 選ばれたメガミの情報を渡す
            val selectedMegamiArray: Array<String> = selectedMegamiList.toTypedArray()
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
            startActivity(intent);
        }
    }
}