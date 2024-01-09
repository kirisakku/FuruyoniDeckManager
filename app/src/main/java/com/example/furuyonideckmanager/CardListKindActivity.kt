package com.example.furuyonideckmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

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