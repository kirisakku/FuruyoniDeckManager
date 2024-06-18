package com.example.furuyonideckmanager

import io.realm.Realm
import io.realm.RealmConfiguration

object RealmManager {
    fun getRealm(): Realm {
        return Realm.getInstance(getConfig())
    }

    private fun getConfig(): RealmConfiguration? {
        return RealmConfiguration.Builder()
            .schemaVersion(1)
            .migration((MyRealmMigration()))
            .allowWritesOnUiThread(true)
            .build();
    }
}