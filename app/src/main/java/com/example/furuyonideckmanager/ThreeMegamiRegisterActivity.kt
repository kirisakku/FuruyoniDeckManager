package com.example.furuyonideckmanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject
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
import kotlinx.android.synthetic.main.activity_three_megami_register.*
import java.util.*

class ThreeMegamiRegisterActivity : AppCompatActivity(), GroupNameDialog.Listener {
    private lateinit var realm: Realm;

    // 選択済みのメガミを管理するリスト
    val selectedMegamiList: MutableList<String> = mutableListOf();
    // ダイアログ
    val dialog = GroupNameDialog();

    /**
     * 名前からImageButton取得
     * @param name パーツ名
     * @return 取得したImageButton。取得失敗時はnull。
     */
    fun findImageButtonByName(name: String): ImageButton? {
        val id = resources.getIdentifier(name, "id", packageName);
        if (id == 0) {
            return null;
        }

        return findViewById(id);
    }

    /**
     * メガミのボタンタップ時の処理。
     * @param imageButton タップされたボタン。
     * @param megamiName タップされたメガミの名前。
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun onButtonTapped(imageButton: ImageButton, megamiName: String) {
        val isPressed = imageButton.isSelected();
        val megami = megamiName.split('_')[0];
        val originImageButton = findImageButtonByName(megami);
        val a1ImageButton = findImageButtonByName(megami + "_a1");
        val a2ImageButton = findImageButtonByName(megami + "_a2");

        originImageButton?.alpha = 0.3f;
        a1ImageButton?.alpha = 0.3f;
        a2ImageButton?.alpha = 0.3f;

        if (!isPressed) {
            // 同種メガミすべてを半透明にする
            originImageButton?.alpha = 0.3f;
            a1ImageButton?.alpha = 0.3f;
            a2ImageButton?.alpha = 0.3f;

            // 枠線を除去する
            originImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a1ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a2ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));

            // 選択状態解除
            originImageButton?.setSelected(false);
            a1ImageButton?.setSelected(false);
            a2ImageButton?.setSelected(false);

            // 枠線を付ける
            imageButton.setBackgroundColor(Color.parseColor("#FFC0BC"));
            // 透明解除
            imageButton.alpha = 1.0f;
            // 同種メガミを間引く
            selectedMegamiList.removeIf { it.contains(megami) }
            // 選択済みリストに追加
            selectedMegamiList.add(megamiName);
        } else {
            // 同種メガミすべての半透明を解除
            originImageButton?.alpha = 1.0f;
            a1ImageButton?.alpha = 1.0f;
            a2ImageButton?.alpha = 1.0f;

            // 枠線を除去する
            originImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a1ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a2ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));

            // 選択状態解除
            originImageButton?.setSelected(false);
            a1ImageButton?.setSelected(false);
            a2ImageButton?.setSelected(false);

            // 選択済みリストから除外
            selectedMegamiList.remove(megamiName);
        }

        // 押下状態を反転させる
        imageButton.setSelected(!isPressed);
        // 作成ボタンの活性状態を変える
        if (selectedMegamiList.size == 3)  {
            startCreationButton.setEnabled(true);
        }
        else {
            startCreationButton.setEnabled(false);
        }
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun setClickListeners() {
        yurina.setOnClickListener {onButtonTapped(yurina, "yurina")}
        yurina_a1.setOnClickListener {onButtonTapped(yurina_a1, "yurina_a1")}
        yurina_a2.setOnClickListener {onButtonTapped(yurina_a2, "yurina_a2")}
        himika.setOnClickListener {onButtonTapped(himika, "himika")}
        himika_a1.setOnClickListener {onButtonTapped(himika_a1, "himika_a1")}
        tokoyo.setOnClickListener {onButtonTapped(tokoyo, "tokoyo")}
        tokoyo_a1.setOnClickListener {onButtonTapped(tokoyo_a1, "tokoyo_a1")}
        tokoyo_a2.setOnClickListener {onButtonTapped(tokoyo_a2, "tokoyo_a2")}
        oboro.setOnClickListener {onButtonTapped(oboro, "oboro")}
        oboro_a1.setOnClickListener {onButtonTapped(oboro_a1, "oboro_a1")}
        oboro_a2.setOnClickListener {onButtonTapped(oboro_a2, "oboro_a2")}
        yukihi.setOnClickListener {onButtonTapped(yukihi, "yukihi")}
        yukihi_a1.setOnClickListener {onButtonTapped(yukihi_a1, "yukihi_a1")}
        shinra.setOnClickListener {onButtonTapped(shinra, "shinra")}
        shinra_a1.setOnClickListener {onButtonTapped(shinra_a1, "shinra_a1")}
        saine.setOnClickListener {onButtonTapped(saine, "saine")}
        saine_a1.setOnClickListener {onButtonTapped(saine_a1, "saine_a1")}
        saine_a2.setOnClickListener {onButtonTapped(saine_a2, "saine_a2")}
        hagane.setOnClickListener {onButtonTapped(hagane, "hagane")}
        hagane_a1.setOnClickListener {onButtonTapped(hagane_a1, "hagane_a1")}
        chikage.setOnClickListener {onButtonTapped(chikage, "chikage")}
        chikage_a1.setOnClickListener {onButtonTapped(chikage_a1, "chikage_a1")}
        kururu.setOnClickListener {onButtonTapped(kururu, "kururu")}
        kururu_a1.setOnClickListener {onButtonTapped(kururu_a1, "kururu_a1")}
        kururu_a2.setOnClickListener {onButtonTapped(kururu_a2, "kururu_a2")}
        sariya.setOnClickListener {onButtonTapped(sariya, "sariya")}
        sariya_a1.setOnClickListener {onButtonTapped(sariya_a1, "sariya_a1")}
        utsuro.setOnClickListener {onButtonTapped(utsuro, "utsuro")}
        utsuro_a1.setOnClickListener {onButtonTapped(utsuro_a1, "utsuro_a1")}
        honoka.setOnClickListener {onButtonTapped(honoka, "honoka")}
        honoka_a1.setOnClickListener {onButtonTapped(honoka_a1, "honoka_a1")}
        raira.setOnClickListener {onButtonTapped(raira, "raira")}
        raira_a1.setOnClickListener {onButtonTapped(raira_a1, "raira_a1")}
        korunu.setOnClickListener {onButtonTapped(korunu, "korunu")}
        yatsuha.setOnClickListener {onButtonTapped(yatsuha, "yatsuha")}
        yatsuha_a1.setOnClickListener {onButtonTapped(yatsuha_a1, "yatsuha_a1")}
        yatsuha_a2.setOnClickListener {onButtonTapped(yatsuha_a2, "yatsuha_a2")}
        hatsumi.setOnClickListener {onButtonTapped(hatsumi, "hatsumi")}
        hatsumi_a1.setOnClickListener {onButtonTapped(hatsumi_a1, "hatsumi_a1")}
        mizuki.setOnClickListener {onButtonTapped(mizuki, "mizuki")}
        megumi.setOnClickListener {onButtonTapped(megumi, "megumi")}
        kanae.setOnClickListener {onButtonTapped(kanae, "kanae")}
        kamui.setOnClickListener {onButtonTapped(kamui, "kamui")}
        renri.setOnClickListener {onButtonTapped(renri, "renri")}
        renri_a1.setOnClickListener {onButtonTapped(renri_a1, "renri_a1")}
        akina.setOnClickListener {onButtonTapped(akina, "akina")}
        sisui.setOnClickListener {onButtonTapped(sisui, "sisui")}
        misora.setOnClickListener {onButtonTapped(misora, "misora")}
    }

    /**
     * convertMegamiNameの補助関数。
     * @param engMemiName 英語表記のメガミ名。
     * @return 日本語表記のメガミ名。
     */
    fun convertName(engMegamiName: String): String {
        when(engMegamiName) {
            "yurina" -> return "ユリナ"
            "himika" -> return "ヒミカ"
            "tokoyo" -> return "トコヨ"
            "oboro" -> return "オボロ"
            "yukihi" -> return "ユキヒ"
            "shinra" -> return "シンラ"
            "saine" -> return "サイネ"
            "hagane" -> return "ハガネ"
            "chikage" -> return "チカゲ"
            "kururu" -> return "クルル"
            "sariya" -> return "サリヤ"
            "utsuro" -> return "ウツロ"
            "honoka" -> return "ホノカ"
            "raira" -> return "ライラ"
            "korunu" -> return "コルヌ"
            "yatsuha" -> return "ヤツハ"
            "hatsumi" -> return "ハツミ"
            "mizuki" -> return "ミズキ"
            "megumi" -> return "メグミ"
            "kanae" -> return "カナヱ"
            "kamui" -> return "カムヰ"
            "renri" -> return "レンリ"
            "akina" -> return "アキナ"
            "sisui" -> return "シスイ"
            "misora" -> return "ミソラ"
            else -> return ""
        }
    }

    /**
     * メガミ名を英字から日本語表記へ変換します。
     * 例：yurina_a1 → A1ユリナ
     * @param engMegamiName 英語表記のメガミ名
     * @return 日本語表記のメガミ名を返します。
     */
    fun convertMegamiName(engMemiName: String): String {
        val splitInfo = engMemiName.split('_');
        val megami = splitInfo[0];
        var kind = "";
        if (splitInfo.count() == 2) {
            kind = splitInfo[1];
        }

        // 特殊ケース
        if (megami == "yatsuha" && kind == "a2") {
            return "AAヤツハ";
        }

        // 通常ケース
        var jpMegami = convertName(megami);
        var prefix = "";
        if (kind == "a1") {
            prefix = "A1";
        } else if (kind == "a2") {
            prefix = "A2";
        }

        return prefix + jpMegami;
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_three_megami_register);

//        var config = RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
//        Realm.setDefaultConfiguration(config);
        realm = Realm.getDefaultInstance();

        // 全メガミボタンに押下時ハンドラ追加
        setClickListeners();

        // 登録画面に遷移するためのボタンにハンドラ追加
        startCreationButton.setOnClickListener {
            /*
            val intent = Intent(this, ThreeMegamiDeckRegisterActivity::class.java);
            // 選ばれたメガミの情報を渡す
            val selectedMegamiArray: Array<String> = selectedMegamiList.toTypedArray()
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
            startActivity(intent);
             */
            val dialogArgs = Bundle();
            val jpMegamiName0 = convertMegamiName(selectedMegamiList[0]);
            val jpMegamiName1 = convertMegamiName(selectedMegamiList[1]);
            val jpMegamiName2 = convertMegamiName(selectedMegamiList[2]);
            dialogArgs.putString("defaultName", jpMegamiName0 + "/" + jpMegamiName1 + "/" + jpMegamiName2);
            dialog.arguments = dialogArgs;
            dialog.show(supportFragmentManager, "register_megamis");
        }
    }

    /**
     * ダイアログの「登録」ボタン押下時の処理。
     */
    override fun confirm()
    {
        // DBに情報を書き込む
        val id = UUID.randomUUID().toString();
        realm.beginTransaction();
        realm.createObject<ThreeMegami>(id).apply {
            title = dialog.getInput().toString();
            megami0 = selectedMegamiList[0];
            megami1 = selectedMegamiList[1];
            megami2= selectedMegamiList[2];
        }
        realm.commitTransaction();
        Toast.makeText(applicationContext, "三柱を登録しました", Toast.LENGTH_SHORT).show();

        // 画面遷移
        val intent = Intent(this, ThreeMegamiListActivity::class.java);
        startActivity(intent);
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 何もしない
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