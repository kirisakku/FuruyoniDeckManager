package PartsUtil

import android.content.Context
import android.view.View
import android.widget.Button
import com.example.furuyonideckmanager.R

/**
 * メインタイプ/サブタイプのボタンのスタイルを設定。
 * @param button スタイルを設定する対象のボタン。
 * @param type メインタイプ/サブタイプの属性。
 */
fun setButtonStyles(button: Button?, type: String) {
    when(type) {
        "攻撃" -> {
            button?.setBackgroundResource(R.drawable.circle_red);
            button?.setText("攻");
        };
        "行動" -> {
            button?.setBackgroundResource(R.drawable.circle_blue)
            button?.setText("行");
        };
        "付与" -> {
            button?.setBackgroundResource(R.drawable.circle_green)
            button?.setText("付");
        };
        "全力" -> {
            button?.setBackgroundResource(R.drawable.circle_yellow)
            button?.setText("全");
        };
        "対応" -> {
            button?.setBackgroundResource(R.drawable.circle_purple)
            button?.setText("対");
        };
        else -> {
            button?.setVisibility(View.INVISIBLE);
        }
    }
}