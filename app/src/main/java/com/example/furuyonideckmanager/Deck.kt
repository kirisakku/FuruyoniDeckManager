package com.example.furuyonideckmanager

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Deck: RealmObject() {
    @PrimaryKey
    var fileName: String = ""
    var title: String = ""
    var date: Date = Date();
    var megami0: String = ""
    var megami1: String = ""
    var comment: String = ""
}