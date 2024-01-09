package com.example.furuyonideckmanager

import CsvUtil.convertJsonArrayToMapList
import PartsUtil.*
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.LinearLayout.*
import android.widget.ScrollView
import android.widget.Space
import androidx.core.view.marginRight
import androidx.core.view.marginTop
import androidx.fragment.app.DialogFragment

class AdditionalCardDialogs: DialogFragment() {
    interface Listener {
        fun update();
        fun cancel();
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
        // inflate = レイアウトXMLからビューを生成するもの
        val inflater = requireActivity().layoutInflater;
        val dialogLayout = inflater.inflate(R.layout.dialog_additionalcards, null);

        // データ設定
        val extraCardListData = arguments?.getStringArray("extraCardList");
        // Mapの配列に戻す
        val extraCardList = convertJsonArrayToMapList(extraCardListData);

        val vLinearLayout = dialogLayout.findViewById<LinearLayout>(R.id.dialogVLinearLayout);
        for(i in extraCardList.indices) {
            var elem = extraCardList[i];

            // 横整列レイアウト作成
            val hLinearLayout = LinearLayout(context);

            // 名称ボタン追加
            val button = Button(context);
            setButtonBackgroundColor(button, elem);
            button.setText(elem.get("actionName"));
            // ボタンのレイアウト設定
            setLayoutParamsToButton(button, context!!);

            // ハンドラ設定
            button?.setOnClickListener {
                // 画面遷移
                val intent = Intent(context, ShowCardActivity::class.java);
                // 画像データ
                val image = elem.get("fileName");
                // 画像データを渡す
                intent.putExtra("IMAGE_FILE_NAME", image);
                startActivity(intent);
            }

            // 属性ボタン追加
            val size = convertDpToPixel(20, context!!);
            val typeButton0 = createTypeButtonForAdditionalCard(elem.get("mainType").orEmpty(), context!!);
//            typeButton0.layoutParams = LayoutParams(size, size).apply { gravity = Gravity.CENTER }

            val typeButton1 = createTypeButtonForAdditionalCard(elem.get("subType").orEmpty(), context!!)
//            typeButton1.layoutParams = LayoutParams(size, size).apply { gravity = Gravity.CENTER }

            // 作成した要素を横整列
            hLinearLayout.addView(button);
            hLinearLayout.addView(typeButton0);
            hLinearLayout.addView(typeButton1);

            // 作成した横整列レイアウトを縦整列レイアウトに足す
            vLinearLayout.addView(hLinearLayout);
        }

        builder.setView(dialogLayout);

        return builder.create();
    }
}
