package CsvUtil

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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