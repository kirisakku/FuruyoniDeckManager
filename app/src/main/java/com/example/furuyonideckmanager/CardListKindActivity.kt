package com.example.furuyonideckmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle

class CardListKindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cardlistkind)
        attack.setOnClickListener {
            val intent = Intent(this, CardListActivity::class.java);
            startActivity(intent);
        }

        action.setOnClickListener {
            val intent = Intent(this, CardKindActivity::class.java);
            startActivity(intent);
        }
    }
}