package com.example.furuyonideckmanager

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    // 起動モード
    var mode = "";

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
        // デッキ登録の場合
        if (selectedMegamiList.size == 2 && mode == "registerDeck") {
            startCreationButton.setEnabled(true);
        } else if (selectedMegamiList.size == 3 && mode == "threeMegami")  {
            startCreationButton.setEnabled(true);
        }
        else {
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
        kamui.setOnClickListener {onButtonTapped(kamui, "kamui")}
        renri.setOnClickListener {onButtonTapped(renri, "renri")}
        akina.setOnClickListener {onButtonTapped(akina, "akina")}
        sisui.setOnClickListener {onButtonTapped(sisui, "sisui")}
        misora.setOnClickListener {onButtonTapped(misora, "misora")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // モード情報の取得
        val modeData = intent.getStringExtra("MODE");
        mode = if (modeData != null) modeData else "registerDeck";

        // モードが三柱選択の場合はボタンのテキストを変える
        if (mode == "threeMegami") {
            startCreationButton.setText("三柱登録");
        }

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