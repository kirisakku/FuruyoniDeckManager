package com.example.furuyonideckmanager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.EditText
import kotlinx.android.synthetic.main.dialog_register.*

class DeckNameDialog: DialogFragment() {
    interface Listener {
        fun register();
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
        val dialogLayout = inflater.inflate(R.layout.dialog_register, null);
        // 左側に「登録」ボタンを出したいので敢えてPositiveとNegativeを入れ替える
        builder.setView(dialogLayout)
            .setPositiveButton(R.string.cancel) {dialog, which ->
                // 画面閉じる処理
                getDialog().cancel();
            }
            .setNegativeButton(R.string.register) { dialog, which ->
                // 登録処理
            }

        // バリデーション設定
        val editText = dialogLayout.findViewById<AppCompatEditText>(R.id.deckNameField);
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length == 0) {
                    editText.setError("必須項目です。デッキ名を入力してください");
                } else if (s.length > 15) {
                    editText.setError("デッキ名は15文字以内で入力してください");
                }
            }
        })

        return builder.create();
    }
}