package DBUtil;

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.furuyonideckmanager.ThreeMegami
import io.realm.Realm
import io.realm.kotlin.where
import org.json.JSONObject
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths

/**
 * 選択されたデッキ情報を登録。
 * @param realm realm
 * @param id 三柱管理ID
 * @param fileName デッキのUUID
 * @param title デッキタイトル
 * @param target 対象のペア。01、12、20のいずれか。
 */
fun registerSelectedDeck(realm: Realm, id: String, fileName: String, title: String, target: String) {
    var targetData = realm.where<ThreeMegami>().equalTo("id", id).findFirst();
    if (target == "01") {
        realm.executeTransaction {
            targetData?.deck01id = fileName;
            targetData?.deck01name = title;
        }
    } else if (target == "12") {
        realm.executeTransaction {
            targetData?.deck12id = fileName;
            targetData?.deck12name = title;
        }
    } else if (target == "20") {
        realm.executeTransaction {
            targetData?.deck20id = fileName;
            targetData?.deck20name = title;
        }
    }
}