package com.example.furuyonideckmanager

import android.app.Application
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmMigration

class DeckManagementApplication: Application() {
    override fun onCreate() {
        super.onCreate();

        Realm.init(this);

        val config = RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration((MyRealmMigration()))
            .allowWritesOnUiThread(true)
            .build();

        Realm.setDefaultConfiguration(config);

        // Realmインスタンスの取得
        val realm = Realm.getDefaultInstance();
        val currentVersion = realm.version
        Log.d("RealmVersion", "Current Realm schema version: $currentVersion")
    }
}