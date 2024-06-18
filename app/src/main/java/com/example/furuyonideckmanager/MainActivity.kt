package com.example.furuyonideckmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import io.realm.Realm
import io.realm.RealmConfiguration

class MainActivity : AppCompatActivity() {
    var instance: MainActivity? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        attack.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java);
            startActivity(intent);
        }

        action.setOnClickListener {
            val intent = Intent(this, DeckListActivity::class.java);
            startActivity(intent);
        }

        three_megami_register.setOnClickListener {
            val intent = Intent(this, ThreeMegamiRegisterActivity::class.java);
            startActivity(intent);
        }

        three_megami_view.setOnClickListener {
            val intent = Intent(this, ThreeMegamiListActivity::class.java);
            startActivity(intent);
        }

        cardList.setOnClickListener {
            val intent = Intent(this, CardListKindActivity::class.java);
            startActivity(intent);
        }

        random_choose.setOnClickListener {
            val intent = Intent(this, RandomChooseActivity::class.java);
            startActivity(intent);
        }

        instance = this;
    }
}