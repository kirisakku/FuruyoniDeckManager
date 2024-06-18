package com.example.furuyonideckmanager

import SetImageUtil.setImageToImageView
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_registration.akina
import kotlinx.android.synthetic.main.activity_registration.chikage
import kotlinx.android.synthetic.main.activity_registration.hagane
import kotlinx.android.synthetic.main.activity_registration.hatsumi
import kotlinx.android.synthetic.main.activity_registration.himika
import kotlinx.android.synthetic.main.activity_registration.honoka
import kotlinx.android.synthetic.main.activity_registration.kamui
import kotlinx.android.synthetic.main.activity_registration.kanae
import kotlinx.android.synthetic.main.activity_registration.korunu
import kotlinx.android.synthetic.main.activity_registration.kururu
import kotlinx.android.synthetic.main.activity_registration.megumi
import kotlinx.android.synthetic.main.activity_registration.misora
import kotlinx.android.synthetic.main.activity_registration.mizuki
import kotlinx.android.synthetic.main.activity_registration.oboro
import kotlinx.android.synthetic.main.activity_registration.raira
import kotlinx.android.synthetic.main.activity_registration.renri
import kotlinx.android.synthetic.main.activity_registration.saine
import kotlinx.android.synthetic.main.activity_registration.sariya
import kotlinx.android.synthetic.main.activity_registration.shinra
import kotlinx.android.synthetic.main.activity_registration.sisui
import kotlinx.android.synthetic.main.activity_registration.startCreationButton
import kotlinx.android.synthetic.main.activity_registration.tokoyo
import kotlinx.android.synthetic.main.activity_registration.utsuro
import kotlinx.android.synthetic.main.activity_registration.yatsuha
import kotlinx.android.synthetic.main.activity_registration.yukihi
import kotlinx.android.synthetic.main.activity_registration.yurina
import kotlinx.android.synthetic.main.activity_three_megami_deck_register.*
import kotlinx.android.synthetic.main.activity_three_megami_register.*

class ThreeMegamiDeckRegisterActivity : AppCompatActivity() {
    private lateinit var realm: Realm;

    /**
     * 登録ボタンを押された時のハンドラです。
     * @param megami0 対象メガミ0。
     * @param megami1 対象メガミ1。
     * @param uuid UUID。
     * @param target ターゲット。01, 12, 20のいずれか。
     * @param targetDeckId ターゲットに対して設定されているデッキのUUID。
     */
    fun onRegisterButtonTapped(megami0: String, megami1: String, uuid: String, target: String, targetDeckId: String) {
        // デッキ選択画面に遷移
        val intent = Intent(this, SelectDeckActivity::class.java);

        // メガミ名を渡す
        intent.putExtra("MEGAMI0", megami0);
        intent.putExtra("MEGAMI1", megami1);
        intent.putExtra("UUID", uuid);
        intent.putExtra("TARGET", target);
        intent.putExtra("TARGET_DECKID", targetDeckId);

        startActivity(intent);
    }

    /**
     * 参照ボタンを押された時のハンドラです。
     * @param megami0 対象メガミ0。
     * @param megami1 対象メガミ1。
     * @param deckId デッキのID。
     * @param deckName デッキの名称。
     */
    fun onViewButtonTapped(megami0: String, megami1: String, deckId: String, deckName: String) {
        // デッキ参照画面に遷移
        val intent = Intent(this, ViewDeckActivity::class.java);

        // データ受け渡し
        val selectedMegamiArray: Array<String> = arrayOf(megami0, megami1);
        intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray);
        intent.putExtra("DECK_FILENAME", deckId);
        intent.putExtra("DECK_TITLE", deckName);
        // この画面固有のフラグ
        intent.putExtra("EDITABLE", false);

        startActivity(intent);
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_megami_deck_register);
        realm = RealmManager.getRealm();

        // UUID
        val uuid = intent.getStringExtra("UUID");
        if (uuid == null) {
            throw Exception();
        }

        val targetData = realm.where<ThreeMegami>().equalTo("id", uuid).findFirst();

        if (targetData == null) {
            throw Exception();
        }

        val megami0 = targetData.megami0;
        val megami1 = targetData.megami1;
        val megami2 = targetData.megami2;

        // メガミ画像をセット
        setImageToImageView(megami0 + ".jpg", megamiImage0, assets);
        setImageToImageView(megami0 + ".jpg", megamiImage0_0, assets);
        setImageToImageView(megami0 + ".jpg", megamiImage2_0, assets);

        setImageToImageView(megami1 + ".jpg", megamiImage1, assets);
        setImageToImageView(megami1 + ".jpg", megamiImage0_1, assets);
        setImageToImageView(megami1 + ".jpg", megamiImage1_1, assets);

        setImageToImageView(megami2 + ".jpg", megamiImage2, assets);
        setImageToImageView(megami2 + ".jpg", megamiImage1_2, assets);
        setImageToImageView(megami2 + ".jpg", megamiImage2_2, assets);

        // デッキ情報セット
        val deck01 = targetData.deck01id;
        val deck12 = targetData.deck12id;
        val deck20 = targetData.deck20id;
        val deck01Name = targetData.deck01name;
        val deck12Name = targetData.deck12name;
        val deck20Name = targetData.deck20name;

        // 既にデッキが選択済み場合はデッキ名を書き換える
        if (deck01Name != "") {
            viewDeck0.text = deck01Name;
            viewDeck0.isEnabled = true;
            registerDeck_0.text = deck01Name;
            registerDeck_0.setBackgroundResource(R.color.gray);
        }
        if (deck12Name != "") {
            viewDeck1.text = deck12Name;
            viewDeck1.isEnabled = true;
            registerDeck_1.text = deck12Name;
            registerDeck_1.setBackgroundResource(R.color.gray);
        }
        if (deck20Name != "") {
            viewDeck2.text = deck20Name;
            viewDeck2.isEnabled = true;
            registerDeck_2.text = deck20Name;
            registerDeck_2.setBackgroundResource(R.color.gray);
        }

        // ハンドラ設定
        registerDeck_0.setOnClickListener{onRegisterButtonTapped(megami0, megami1, uuid, "01", deck01)};
        registerDeck_1.setOnClickListener{onRegisterButtonTapped(megami1, megami2, uuid, "12", deck12)};
        registerDeck_2.setOnClickListener{onRegisterButtonTapped(megami2, megami0, uuid, "20", deck20)};

        viewDeck0.setOnClickListener{onViewButtonTapped(megami0, megami1, deck01, deck01Name)}
        viewDeck1.setOnClickListener{onViewButtonTapped(megami1, megami2, deck12, deck12Name)}
        viewDeck2.setOnClickListener{onViewButtonTapped(megami2, megami0, deck20, deck20Name)}
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