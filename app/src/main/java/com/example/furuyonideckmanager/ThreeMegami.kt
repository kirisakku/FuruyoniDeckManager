package com.example.furuyonideckmanager

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class ThreeMegami: RealmObject() {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var date: Date = Date();
    var megami0: String = ""
    var megami1: String = ""
    var megami2: String = ""
    var comment: String = ""
    var deck01id: String = ""
    var deck12id: String = ""
    var deck20id: String = ""
    var deck01name: String = "";
    var deck12name: String = "";
    var deck20name: String = "";
}