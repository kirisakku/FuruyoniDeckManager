package com.example.furuyonideckmanager

import CsvUtil.readInternalFile
import CsvUtil.removeCsvFile
import PartsUtil.convertDpToPixel
import SetImageUtil.setImageToImageView
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.view.Gravity
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout.CENTER
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_deck_list.*
import kotlinx.android.synthetic.main.activity_deck_list.view.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class DeleteConfirmDialog: DialogFragment() {
    interface Listener {
        fun confirm();
        fun cancel();
        fun getDeckName(): String;
    }
    private var listener: Listener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        when (context) {
            is Listener -> listener = context;
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());
        // 表示内容設定
        val deckName = listener?.getDeckName();
        builder.setMessage("デッキ「$deckName」を削除します。よろしいですか？");

        builder.setPositiveButton("削除") {dialog, which ->
            listener?.confirm();
        }
        builder.setNegativeButton("キャンセル") {dialog, which ->
            listener?.cancel();
        }
        return builder.create();
    }
}

class DeckListActivity : AppCompatActivity(), DeleteConfirmDialog.Listener {
    // 削除候補のデッキ名
    var deleteCandidateDeckName = "";
    // csvから読み込んだデッキリスト
    var deckList = listOf<List<String>>();
    // 削除対象に選ばれたデッキのインデックス
    var deleteCandidateDeckIndex = -1;

    /**
     * 画面をリロード。
     */
    private fun reload() {
        val intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
    }

    /**
     * データ削除後のデッキリストの更新処理を実施。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun updateDeckListCsv() {
        if (deckList.size == 0) {
            // deckList.csvを削除
            removeCsvFile("deckList.csv", applicationContext);
            return;
        }

        val deckListCsv = File(applicationContext.filesDir, "deckList.csv");
        val fileWriter = FileWriter(deckListCsv, false);
        val bufferedWriter = BufferedWriter(fileWriter);

        bufferedWriter.append(deckList[0].joinToString(","))
        for (i in 1 until deckList.size) {
            bufferedWriter.newLine();
            bufferedWriter.append(deckList[i].joinToString(","));
        }
        bufferedWriter.close();
    }

    /**
     * ダイアログの「削除」ボタン押下時の処理。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun confirm() {
        // csvファイル名を保持
        val csvFileName = deckList[deleteCandidateDeckIndex][3];
        // デッキリストから対象行を削除
        deckList = deckList.filterIndexed { index, list -> index != deleteCandidateDeckIndex}
        // デッキリスト上書き
        updateDeckListCsv();
        // csvファイルを削除
        removeCsvFile(csvFileName, applicationContext);
        // 再描画
        reload();
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 何もしない
    }

    /**
     * 削除ボタンがタップされたデッキ名を取得。
     */
    override fun getDeckName(): String {
        return deleteCandidateDeckName;
    }

    /**
     * データが無い時の画面を生成。
     */
    fun createEmptyView() {
        noDeckError0.visibility = VISIBLE;
        noDeckError1.visibility = VISIBLE;
        registerDeckButton.visibility = VISIBLE;
        registerDeckButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java);
            startActivity(intent);
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deck_list);

        // CSVデータ読み込み
        deckList = readInternalFile("deckList.csv", applicationContext);
        val vLinearLayout = findViewById<LinearLayout>(R.id.vLinearLayout);

        //　データが無ければ専用の画面にする
        if (deckList.size == 0) {
            createEmptyView();
            return;
        }

        for(i in deckList.indices) {
            val elem = deckList[i];

            // 横整列レイアウト作成
            val hLinearLayout = LinearLayout(this);
            hLinearLayout.orientation = LinearLayout.HORIZONTAL;
            hLinearLayout.setPadding(0, convertDpToPixel(5, this), 0, convertDpToPixel(5, this));

            // 削除ボタン追加
            val deleteButton = Button(this);
            deleteButton.setBackgroundResource(R.drawable.circle_greentheme);
            deleteButton.setText("×");
            deleteButton.setTextColor(Color.parseColor("#FFFFFF"));
            deleteButton.textSize = 15.0f;
            deleteButton.setPadding(0, 0, 0, 0);
            val context = this;
            deleteButton.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT).apply {
                weight = 1.0F;
                gravity = Gravity.CENTER;
                width = convertDpToPixel(20, context);
                height = convertDpToPixel(20, context);
                marginEnd = convertDpToPixel(8, context);
            }
            deleteButton.setOnClickListener {
                // 削除ボタンがタップされたデッキ名の情報を更新
                deleteCandidateDeckName = elem[0];
                // 削除ボタンがタップされたデッキのインデックス情報を更新
                deleteCandidateDeckIndex = i;
                // ダイアログ表示
                val dialog = DeleteConfirmDialog();
                dialog.show(supportFragmentManager, "delete_dialog");
            }

            // 画像追加
            val imageView0 = ImageView(this);
            val imageView1 = ImageView(this);
            // TODO: 後で配列ではなくマップにしたいかも
            setImageToImageView(elem[1] + ".jpg", imageView0, assets);
            setImageToImageView(elem[2] + ".jpg", imageView1, assets);

            // TODO: 後でここのコード綺麗にしたい
            val layoutParams = ViewGroup.LayoutParams(convertDpToPixel(45, this), convertDpToPixel(45, this));
            imageView0.layoutParams = layoutParams;
            imageView1.layoutParams = layoutParams;
            val marginParams = ViewGroup.MarginLayoutParams(imageView1.layoutParams);
            marginParams.setMargins(convertDpToPixel(8, this), 0, convertDpToPixel(8, this), 0);
            imageView1.layoutParams = marginParams;

            // ボタン追加
            val button = Button(this);
            button.setText(elem[0]);
            button.width = convertDpToPixel(200, this);
            button.setOnClickListener {
                // 画面遷移
                val intent = Intent(this, ViewDeckActivity::class.java);
                // デッキ名を渡す
                intent.putExtra("DECK_TITLE", elem[0]);
                // デッキ情報のcsvを渡す
                intent.putExtra("DECK_CSV", elem[3]);
                // メガミ情報を渡す
                val selectedMegamiArray: Array<String> = arrayOf(elem[1], elem[2]);
                intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
                startActivity(intent);
            }
            button.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.FILL_PARENT).apply {
                gravity = Gravity.CENTER_VERTICAL;
            }


            // 作成した要素を全て横整列レイアウトに足す
            hLinearLayout.addView(deleteButton);
            hLinearLayout.addView(imageView0);
            hLinearLayout.addView(imageView1);
            hLinearLayout.addView(button);

            // 作成した横整列レイアウトを縦レイアウトに足す
            vLinearLayout.addView(hLinearLayout);
        }
    }
}