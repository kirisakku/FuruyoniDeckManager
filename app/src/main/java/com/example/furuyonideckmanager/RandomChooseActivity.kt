package com.example.furuyonideckmanager

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_random_choose.*
import kotlinx.android.synthetic.main.activity_three_megami_register.*
import java.io.IOException
import java.io.InputStream
import kotlin.random.Random

class RandomChooseActivity : AppCompatActivity() {
    fun getCheckedMegami(): MutableList<String> {
        val checkedMegami: MutableList<String> = mutableListOf();
        // チェックがされていたら選択可能なメガミ
        if (yurinaCheckbox.isChecked() == true) {
            checkedMegami.add("ユリナ");
        }
        if (saineCheckbox.isChecked() == true) {
            checkedMegami.add("サイネ");
        }
        if (himikaCheckbox.isChecked() == true) {
            checkedMegami.add("ヒミカ");
        }
        if (tokoyoCheckbox.isChecked() == true) {
            checkedMegami.add("トコヨ");
        }
        if (oboroCheckbox.isChecked() == true) {
            checkedMegami.add("オボロ");
        }
        if (yukihiCheckbox.isChecked() == true) {
            checkedMegami.add("ユキヒ");
        }
        if (shinraCheckbox.isChecked() == true) {
            checkedMegami.add("シンラ");
        }
        if (haganeCheckbox.isChecked() == true) {
            checkedMegami.add("ハガネ");
        }
        if (chikageCheckbox.isChecked() == true) {
            checkedMegami.add("チカゲ");
        }
        if (kururuCheckbox.isChecked() == true) {
            checkedMegami.add("クルル");
        }
        if (sariyaCheckbox.isChecked() == true) {
            checkedMegami.add("サリヤ");
        }
        if (rairaCheckbox.isChecked() == true) {
            checkedMegami.add("ライラ");
        }
        if (utsuroCheckbox.isChecked() == true) {
            checkedMegami.add("ウツロ");
        }
        if (honokaCheckbox.isChecked() == true) {
            checkedMegami.add("ホノカ");
        }
        if (korunuCheckbox.isChecked() == true) {
            checkedMegami.add("コルヌ");
        }
        if (yatsuhaCheckbox.isChecked() == true) {
            checkedMegami.add("ヤツハ");
        }
        if (hatsumiCheckbox.isChecked() == true) {
            checkedMegami.add("ハツミ");
        }
        if (mizukiCheckbox.isChecked() == true) {
            checkedMegami.add("ミズキ");
        }
        if (megumiCheckbox.isChecked() == true) {
            checkedMegami.add("メグミ")
        }
        if (kanaeCheckbox.isChecked() == true) {
            checkedMegami.add("カナヱ")
        }
        if (kamuiCheckbox.isChecked() == true) {
            checkedMegami.add("カムヰ")
        }
        if (renriCheckbox.isChecked() == true) {
            checkedMegami.add("レンリ")
        }
        if (akinaCheckbox.isChecked() == true) {
            checkedMegami.add("アキナ")
        }
        if (sisuiCheckbox.isChecked() == true) {
            checkedMegami.add("シスイ")
        }
        if (misoraCheckbox.isChecked() == true) {
            checkedMegami.add("ミソラ")
        }

        return checkedMegami;
    }

    fun getNinzuu(selected: String): Int {
        if (selected == "2人") {
            return 2;
        }
        if (selected == "3人") {
            return 3;
        }
        if (selected == "1人") {
            return 1;
        }

        // 想定外
        return -1;
    }

    fun setText(list: MutableList<Map<String, String>>, selected: String) {
        if (selected == "2人") {
            megami1.text = list[0].get("megamiCategory");
            megami3.text = list[1].get("megamiCategory");
        } else if (selected == "3人") {
            megami1.text = list[0].get("megamiCategory");
            megami2.text = list[1].get("megamiCategory");
            megami3.text = list[2].get("megamiCategory");
        } else if (selected == "1人") {
            megami2.text = list[0].get("megamiCategory");
        }
    }

    fun setImage(list: MutableList<Map<String, String>>, selected: String) {
        val assets = resources.assets;

        var instream1: InputStream? = null;
        var instream2: InputStream? = null;
        var instream3: InputStream? = null;
        var bitmap1: Bitmap? = null;
        var bitmap2: Bitmap? = null;
        var bitmap3: Bitmap? = null;
        // try-with-resources
        try {
            if (selected == "2人") {
                instream1 = assets.open(list[0].get("image").toString());
                instream3 = assets.open(list[1].get("image").toString());
            } else if (selected == "3人") {
                instream1 = assets.open(list[0].get("image").toString());
                instream2 = assets.open(list[1].get("image").toString());
                instream3 = assets.open(list[2].get("image").toString());
            } else if (selected == "1人") {
                instream2 = assets.open(list[0].get("image").toString());
            }

            if (instream1 != null) {
                bitmap1 = BitmapFactory.decodeStream(instream1);
                megamiImage1.setImageBitmap(bitmap1);
            }
            if (instream2 != null) {
                bitmap2 = BitmapFactory.decodeStream(instream2);
                megamiImage2.setImageBitmap(bitmap2);
            }
            if (instream3 != null) {
                bitmap3 = BitmapFactory.decodeStream(instream3);
                megamiImage3.setImageBitmap(bitmap3);
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_random_choose)

        // 全チェック状態を管理するグローバル変数
        var checked = false;

        chooseButton.setOnClickListener {
            val selected = ninzuu.selectedItem as String;
            val num = this.getNinzuu(selected);
            val checkedMegami = this.getCheckedMegami();

            // 初期化
            megami1.text = "";
            megami2.text = "";
            megami3.text = "";
            megamiImage1.setImageBitmap(null);
            megamiImage2.setImageBitmap(null);
            megamiImage3.setImageBitmap(null);

            // メガミが人数分選べないケース
            if (checkedMegami.size < num) {
                errorMessage.text = "エラー：メガミを" + selected + "選べません";
            } else {
                // 以下はメガミが人数分選べるケース
                val choosenMegamiList: MutableList<Map<String, String>> = mutableListOf();
                val megamiList = getList(this, checkedMegami);
                var nonDuplicatedMegamiList = megamiList.toList();
                errorMessage.text = "";

                for (i in 1..num) {
                    val randomNumber = Random.nextInt(nonDuplicatedMegamiList.size);
                    val targetData = nonDuplicatedMegamiList[randomNumber];
                    choosenMegamiList.add(targetData)

                    // 同じ種類のメガミを除外する
                    nonDuplicatedMegamiList =
                        nonDuplicatedMegamiList.filter { elem ->
                            elem.get("megamiName") != targetData.get(
                                "megamiName"
                            )
                        }
                }

                // テキスト設定
                this.setText(choosenMegamiList, selected);

                // 画像設定
                this.setImage(choosenMegamiList, selected);
            }
        }

        allButton.setOnClickListener {
            // 全メガミの選択状態を設定する
            yurinaCheckbox.setChecked(checked);
            saineCheckbox.setChecked(checked);
            himikaCheckbox.setChecked(checked);
            tokoyoCheckbox.setChecked(checked);
            oboroCheckbox.setChecked(checked);
            yukihiCheckbox.setChecked(checked);
            shinraCheckbox.setChecked(checked);
            haganeCheckbox.setChecked(checked);
            chikageCheckbox.setChecked(checked);
            kururuCheckbox.setChecked(checked);
            sariyaCheckbox.setChecked(checked);
            rairaCheckbox.setChecked(checked);
            utsuroCheckbox.setChecked(checked);
            honokaCheckbox.setChecked(checked);
            korunuCheckbox.setChecked(checked);
            yatsuhaCheckbox.setChecked(checked);
            hatsumiCheckbox.setChecked(checked);
            mizukiCheckbox.setChecked(checked);
            megumiCheckbox.setChecked(checked);
            kanaeCheckbox.setChecked(checked);
            kamuiCheckbox.setChecked(checked);
            renriCheckbox.setChecked(checked);
            akinaCheckbox.setChecked(checked);
            sisuiCheckbox.setChecked(checked);
            misoraCheckbox.setChecked(checked);

            // フラグを反転
            checked = !checked;

            // テキストを変更
            val text = if (checked) "全選択" else "全選択解除";
            allButton.setText(text)
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