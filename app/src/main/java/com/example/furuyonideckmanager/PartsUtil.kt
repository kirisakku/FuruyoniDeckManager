package PartsUtil

import CsvUtil.isAnother
import CsvUtil.isSpecialCard
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
    button?.setVisibility(View.VISIBLE);

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
        "不定" -> {
            button?.setBackgroundResource(R.drawable.circle_gray)
            button?.setText("不")
        }
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
    // アナザーのカードかつ追加札でない場合は桃色を設定
    if (isAnother(cardData) && cardData.get("actionName") != "" && cardData.get("no")?.startsWith('A') == false) {
        button.setBackgroundColor(Color.parseColor("#ffe4e1"));
    } else if(cardData.get("no") == "A-E")  {
        // 追加札の場合は薄紫色
        button.setBackgroundColor((Color.parseColor("#e6e6fa")))
    } else {
        // それ以外の場合は通常札であれば灰色、切札の場合は金色
        val color = if (isSpecialCard(cardData)) "#eee8aa" else "#d6d7d7";
        button.setBackgroundColor((Color.parseColor(color)));
    }
}

/**
 * 追加札が存在するかどうかを判定。
 * @param extraCardList 追加札のカードリスト。
 * @return 追加札が存在するならtrue、存在しないならfalse。
 */
fun isExtraExist(extraCardList: List<Map<String, String>>?): Boolean {
    return extraCardList != null && extraCardList?.count() != 0;
}

/**
 * dpをpixelに変換。
 * @param dp 変換対象のdp。
 * @param context 呼び出し元のcontext。
 */
fun convertDpToPixel(dp: Int, context: Context): Int {
    val density = context.resources.displayMetrics.density;
    val result = ((dp * density) + 0.5).toInt();
    return result;
}