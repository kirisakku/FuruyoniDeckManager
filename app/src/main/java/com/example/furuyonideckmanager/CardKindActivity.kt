package com.example.furuyonideckmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_cardkind.*
import kotlinx.android.synthetic.main.activity_main.action
import kotlinx.android.synthetic.main.activity_main.attack

class CardKindActivity : AppCompatActivity() {
    /**
     * カード種類のボタンタップ時の処理。
     * @param megamiName タップされたカードの種類。
     */
    fun onButtonTapped(kardKind: String) {
        val intent = Intent(this, KindListActivity::class.java);
        // 選ばれたカードの種類の情報を渡す
        intent.putExtra("CHOSEN_KIND", kardKind);
        startActivity(intent);
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    fun setClickListeners() {
        attack.setOnClickListener {onButtonTapped("attack")}
        action.setOnClickListener {onButtonTapped("action")}
        assignment.setOnClickListener {onButtonTapped("assignment")}
        handling.setOnClickListener {onButtonTapped("handling")}
        fullpower.setOnClickListener {onButtonTapped("fullpower")}
        indefinite.setOnClickListener {onButtonTapped("indefinite")}
        nohandling.setOnClickListener {onButtonTapped("nohandling")}
        distance.setOnClickListener {onButtonTapped("distance")}
        arrow.setOnClickListener {onButtonTapped("arrow")}
        life.setOnClickListener {onButtonTapped("life")}
        hyphen.setOnClickListener {onButtonTapped("hyphen")}
        buff.setOnClickListener {onButtonTapped("buff")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardkind)

        // 全種類ボタンに押下時ハンドラ追加
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