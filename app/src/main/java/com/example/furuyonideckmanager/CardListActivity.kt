package com.example.furuyonideckmanager

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_card_list.*

class CardListActivity : AppCompatActivity() {
    /**
     * メガミのボタンタップ時の処理。
     * @param megamiName タップされたメガミの名前。
     */
    fun onButtonTapped(megamiName: String) {
        val intent = Intent(this, MegamiCardListActivity::class.java);
        // 選ばれたメガミの情報を渡す
        intent.putExtra("CHOSEN_MEGAMI", megamiName)
        startActivity(intent);
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    fun setClickListeners() {
        yurina.setOnClickListener {onButtonTapped("yurina")}
        himika.setOnClickListener {onButtonTapped("himika")}
        tokoyo.setOnClickListener {onButtonTapped("tokoyo")}
        oboro.setOnClickListener {onButtonTapped("oboro")}
        yukihi.setOnClickListener {onButtonTapped("yukihi")}
        shinra.setOnClickListener {onButtonTapped("shinra")}
        saine.setOnClickListener {onButtonTapped("saine")}
        hagane.setOnClickListener {onButtonTapped("hagane")}
        chikage.setOnClickListener {onButtonTapped("chikage")}
        kururu.setOnClickListener {onButtonTapped("kururu")}
        sariya.setOnClickListener {onButtonTapped("sariya")}
        utsuro.setOnClickListener {onButtonTapped("utsuro")}
        honoka.setOnClickListener {onButtonTapped("honoka")}
        raira.setOnClickListener {onButtonTapped("raira")}
        korunu.setOnClickListener {onButtonTapped("korunu")}
        yatsuha.setOnClickListener {onButtonTapped("yatsuha")}
        hatsumi.setOnClickListener {onButtonTapped("hatsumi")}
        mizuki.setOnClickListener {onButtonTapped("mizuki")}
        megumi.setOnClickListener {onButtonTapped("megumi")}
        kanae.setOnClickListener {onButtonTapped("kanae")}
        kamui.setOnClickListener {onButtonTapped("kamui")}
        renri.setOnClickListener {onButtonTapped("renri")}
        akina.setOnClickListener {onButtonTapped("akina")}
        sisui.setOnClickListener {onButtonTapped("sisui")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_list)

        // 全メガミボタンに押下時ハンドラ追加
        setClickListeners();
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