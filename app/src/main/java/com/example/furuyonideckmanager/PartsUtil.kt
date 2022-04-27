package PartsUtil

import android.content.Context
import android.graphics.Color
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

/**
 * カード名ボタンに背景色を設定。
 * @param button ボタン。
 * @param cardData カードデータ。
 */
fun setButtonBackgroundColor(button: Button, cardData: Map<String, String>) {
    if (isOrigin(cardData)) {
        button.setBackgroundColor(Color.parseColor("#ffe4e1"));
    } else {
        val color = if (isSpecialCard(cardData)) "#eee8aa" else "#d6d7d7";
        button.setBackgroundColor((Color.parseColor(color)));
    }
}

/**
 * オリジンかどうかを判定。
 * @param cardData カードデータ。
 * @return　オリジンならtrue、アナザーならfalse。
 */
fun isOrigin(cardData: Map<String, String>): Boolean {
    return cardData.get("type") != "O";
}

/**
 * 切札かどうかを判定。
 * @param cardData カードデータ。
 * @return 切札ならtrue、通常札ならfalse。
 */
fun isSpecialCard(cardData: Map<String, String>): Boolean {
    return cardData.get("no")?.startsWith('S') == true
}

/**
 * アナザーが存在するかどうかを判定。
 * @param anotherCardList アナザーのカードリスト。
 * @return アナザーが存在するならtrue、存在しないならfalse。
 */
fun isAnotherExist(anotherCardList: List<Map<String, String>>?): Boolean {
    return anotherCardList != null && anotherCardList?.count() != 0;
}