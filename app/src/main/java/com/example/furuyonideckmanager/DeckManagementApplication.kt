package com.example.furuyonideckmanager

import android.app.Application
import io.realm.Realm

class DeckManagementApplication: Application() {
    override fun onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}