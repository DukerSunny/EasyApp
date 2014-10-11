package tv.acfun.read.tools;

import android.text.Spanned;

import com.harreke.easyapp.listeners.OnTagClickListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/08
 */
public class CommentUtil {
    private final static String TAG = "CommentUtil";

    public static Spanned convertUBB(String input, OnTagClickListener tagClickListener) {
        Spanned spanned;

        input = input.replaceAll("\\[emot=([a-z]+?),([0-9]+?)/\\]", "<img src=\"$1/$2\"/>");
        input = input.replaceAll("\\[color=#([a-z0-9]+?)\\]", "<font color=\"#$1\">").replace("[/color]", "</font>");
        input = input.replaceAll("\\[font=[\\S\\s]+?\\]", "").replace("[/font]", "");
        input = input.replaceAll("\\[size=([0-9]+?)px\\]", "<font size=\"$1px;\">").replace("[/size]", "</font>");
        input = input.replace("[b]", "<b>").replace("[/b]", "</b>");

        input = input.replaceAll("\\[img=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/img\\]", "<a href=\"$2\">$1</a>&nbsp;");
        input = input.replaceAll("\\[at\\]([\\S\\s]+?)\\[/at\\]", "<at>$1</at>");

        spanned = Html.fromHtml(input, tagClickListener);

        return spanned;
    }
}
