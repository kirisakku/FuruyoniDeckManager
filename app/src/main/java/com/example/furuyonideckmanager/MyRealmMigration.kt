package com.example.furuyonideckmanager
import io.realm.DynamicRealm
import io.realm.FieldAttribute
import io.realm.RealmMigration
import io.realm.RealmSchema
import java.util.*

class MyRealmMigration: RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema: RealmSchema = realm.schema
        var _oldVersion = oldVersion;

        if (_oldVersion == 0L) {
            schema.create("ThreeMegami")
                ?.addField("id", String::class.java, FieldAttribute.PRIMARY_KEY, FieldAttribute.REQUIRED)
                ?.addField("title", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("megami0", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("megami1", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("megami2", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("deck01id", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("deck12id", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("deck20id", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("deck01name", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("deck12name", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("deck20name", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("comment", String::class.java, FieldAttribute.REQUIRED)
                ?.addField("date", Date::class.java, FieldAttribute.REQUIRED);

            _oldVersion++;
        }
    }

    override fun equals(other: Any?): Boolean {
        return true;
    }
}