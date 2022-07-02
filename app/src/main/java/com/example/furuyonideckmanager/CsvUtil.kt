package CsvUtil

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.json.JSONObject
import java.io.*
import java.nio.file.Files
import java.nio.file.Paths

/**
 * RawからCSVを読み込む。
 * @param fileId リソース内におけるファイルID。
 * @param res リソース。
 * @param context 呼び出し元のcontext。エラー表示に使います。
 * @return CSVデータを文字列リストのリストにした結果を返します。
 */
fun readRawCsv(fileId: Int, res: Resources, context: Context): List<List<String>> {
    var separatedList = mutableListOf<List<String>>();
    val input = res.openRawResource(fileId);
    var bufferReader = BufferedReader(InputStreamReader(input));

    try {
        try {
            val inputStream = res.openRawResource(fileId);
            bufferReader = BufferedReader(InputStreamReader(inputStream));
            var str = bufferReader.readLine();
            while(str != null) {
                separatedList.add(str.split(','))
                str = bufferReader.readLine();
            }
        } finally {
            bufferReader.close();
        }
    } catch (e: IOException) {
        Toast.makeText(context, "読み込み失敗", Toast.LENGTH_SHORT).show();
    }

    return separatedList;
}

/**
 * 内部ストレージからCSVを読み込む。
 * @param fileName ファイル名。
 * @param context 呼び出し元のコンテキスト。ファイル取得時と読み込み失敗時のエラー表示に利用。
 * @return CSVデータを文字列リストのリストにした結果を返します。
 */
fun readInternalFile(fileName: String, context: Context): List<List<String>> {
    var separatedList = listOf<List<String>>();
    val file = File(context.filesDir, fileName);
    if (file.exists()) {
        file.bufferedReader().use {
            val content = it.readText();
            val dataList = content.split('\n');
            separatedList = dataList.map{it.split(',')}
        }
    } else {
        Toast.makeText(context, "ファイルが存在しません。", Toast.LENGTH_SHORT).show();
    }

    return separatedList;
}

/**
 * csvファイルの削除処理。
 * @param fileName ファイル名。
 * @param context 呼び出し元のコンテキスト。
 */
@RequiresApi(Build.VERSION_CODES.O)
fun removeCsvFile(fileName: String, context: Context) {
    val path = Paths.get(context.filesDir.path, fileName);

    try {
        Files.deleteIfExists(path);
    } catch (e: IOException) {
        e.printStackTrace();
    }
}

/**
 * csvデータをオリジン/A-1/A-2に分類。
 * @param csvData csvDataをparseした結果。
 * @return csvデータをオリジン/A-1/A-2に分類した結果。
 */
fun classifiedCsvData(csvData: List<List<String>>): Map<String, List<Map<String, String>>> {
    // まずMapのリストに変換
    val mapList = csvData.map{elem ->
        mapOf(
            "no" to elem[0], "actionName" to elem[1], "mainType" to elem[2], "subType" to elem[3], "fileName" to elem[4], "type" to elem[5]
        );
    }

    // 各種のリスト作成
    val origin = mapList.filter{ elem -> isOrigin(elem) }
    var A1List = mapList.filter{ elem -> isA1(elem) }.toMutableList();
    var A2List = mapList.filter{ elem -> isA2(elem) }.toMutableList();
    var extraOriginList = mapList.filter{ elem -> isExtraOrigin(elem) }
    var extraA1List = mapList.filter{ elem -> isExtraA1(elem) }
    var extraA2List = mapList.filter{ elem -> isExtraA2(elem) }

    // マージ用関数
    fun merge(origin: List<Map<String, String>>, another: List<Map<String, String>>): List<Map<String, String>> {
        if (another.count() == 0) {
            // 長さが0の場合はそのまま返す
            return another;
        }

        // オリジンをコピー
        val anotherList = origin.toMutableList();
        // 以下は長さが1以上のケース
        for (i in another.indices) {
            // 同じNoのカードを探す
            val targetIndex = origin.indexOfFirst{elem: Map<String, String> ->
                elem.get("no") == another[i].get("no")
            }
            // アナザーのカードで置き換える
            anotherList[targetIndex] = another[i];
        }

        return anotherList.toList();
    }

    return mapOf(
        "origin" to origin,
        "a1" to merge(origin, A1List),
        "a2" to merge(origin, A2List),
        "extra-origin" to extraOriginList,
        "extra-a1" to extraA1List,
        "extra-a2" to extraA2List
    )
}

/**
 * RawからCSVを読み込み、オリジン/A-1/A-2に分類。
 * @param fileId リソース内におけるファイルID。
 * @param res リソース。
 * @param context 呼び出し元のcontext。エラー表示に使います。
 * @return CSVデータを文字列リストのリストにした結果を返します。
 */
fun getClassifiedCsvData(fileId: Int, res: Resources, context: Context): Map<String, List<Map<String, String>>> {
    val csvData = readRawCsv(fileId, res, context);
    return classifiedCsvData(csvData);
}

/**
 * CSVデータを文字列配列に変換
 * @param extraCardCsvList 追加札一覧
 * @return CSVデータを文字列配列に変換した結果を返します。
 */
fun convertCsvToStringArray(extraCardList: List<Map<String, String>>): Array<String> {
    var extraCardArray: Array<String> = arrayOf();
    for (i in extraCardList.indices) {
        extraCardArray += JSONObject(extraCardList[i]).toString();
    }

    return extraCardArray;
}

/**
 * JSON配列をマップのListに変換
 * @param jsonList JSON配列
 * @return 変換結果のマップのListを返します。
 */
fun convertJsonArrayToMapList(jsonList: Array<String>?): List<Map<String, String>> {
    var cardList: MutableList<Map<String, String>> = mutableListOf();
    if (jsonList == null) {
        return cardList;
    }

    for (i in jsonList.indices) {
        var json = JSONObject(jsonList[i]);
        var map: Map<String, String> = mapOf (
            "no" to json.getString("no"),
            "actionName" to json.getString("actionName"),
            "mainType" to json.getString("mainType"),
            "subType" to json.getString("subType"),
            "fileName" to json.getString("fileName"),
            "type" to json.getString("type")
        )

        cardList.add(map);
    }

    return cardList.toList();
}

/**
 * オリジンのカードかどうかを判定
 * @param cardData カードデータ
 * @return オリジンのカードの場合はtrue、それ以外の場合はfalse
 */
fun isOrigin(cardData: Map<String, String>): Boolean {
    return cardData.get("type") == "O" && !cardData.get("no")!!.startsWith('A');
}

/**
 * A1のカードかどうかを判定
 * @param cardData カードデータ
 * @return A1のカードの場合はtrue、それ以外の場合はfalse
 */
fun isA1(cardData: Map<String, String>): Boolean {
    return cardData.get("type") == "a1" && !cardData.get("no")!!.startsWith('A');
}

/**
 * A2のカードかどうかを判定
 * @param cardData カードデータ
 * @return A2のカードの場合はtrue、それ以外の場合はfalse
 */
fun isA2(cardData: Map<String, String>): Boolean {
    return cardData.get("type") == "a2" && !cardData.get("no")!!.startsWith('A');
}

/**
 * オリジンの追加札かどうかを判定
 * @param cardData カードデータ
 * @return オリジンの追加札の場合はtrue、それ以外の場合はfalse
 */
fun isExtraOrigin(cardData: Map<String, String>): Boolean {
    return cardData.get("type") == "O" && cardData.get("no")!!.startsWith('A');
}

/**
 * A1の追加札かどうかを判定
 * @param cardData カードデータ
 * @return A1の追加札の場合はtrue、それ以外の場合はfalse
 */
fun isExtraA1(cardData: Map<String, String>): Boolean {
    return cardData.get("type") == "a1" && cardData.get("no")!!.startsWith('A');
}

/**
 * A2の追加札かどうかを判定
 * @param cardData カードデータ
 * @return A2の追加札の場合はtrue、それ以外の場合はfalse
 */
fun isExtraA2(cardData: Map<String, String>): Boolean {
    return cardData.get("type") == "a2" && cardData.get("no")!!.startsWith('A');
}

/**
 * アナザーかどうかを判定。
 * @param cardData カードデータ。
 * @return　オリジンならtrue、アナザーならfalse。
 */
fun isAnother(cardData: Map<String, String>): Boolean {
    return cardData.get("type") != "O";
}

/**
 * 切札かどうかを判定。
 * @param cardData カードデータ。
 * @return 切札ならtrue、通常札ならfalse。
 */
fun isSpecialCard(cardData: Map<String, String>): Boolean {
    return cardData.get("no")?.startsWith('S') == true || cardData.get("no")?.endsWith('S') == true;
}

/**
 * アナザーが存在するかどうかを判定。
 * @param anotherCardList アナザーのカードリスト。
 * @return アナザーが存在するならtrue、存在しないならfalse。
 */
fun isAnotherExist(anotherCardList: List<Map<String, String>>?): Boolean {
    return anotherCardList != null && anotherCardList?.count() != 0;
}