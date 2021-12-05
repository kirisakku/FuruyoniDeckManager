package CsvUtil

import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

/**
 * RawからCSVを読み込む。
 * @param fileId リソース内におけるファイルID。
 * @param res リソース。
 * @param context 呼び出し元のcontext。エラー表示に使います。
 * @return CSVデータを文字列リストのリストにした結果を返します。
 */
fun readRawCsv(fileId: Int, res: Resources, context: Context): List<List<String>> {
    var bufferReader: BufferedReader? = null;
    var separatedList = mutableListOf<List<String>>();
    val inputStream = res.openRawResource(fileId);
    bufferReader = BufferedReader(InputStreamReader(inputStream));

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
            if (bufferReader != null) {
                bufferReader.close();
            }
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