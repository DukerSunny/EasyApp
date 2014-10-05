package tv.acfun.read.tools;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.tools.StringUtil;
import com.harreke.easyapp.tools.TagClickableSpan;

import java.util.regex.Matcher;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class ArticleUtil {
    public static String convertUBBTags(String input) {
        Matcher imgMatcher;
        Matcher srcMatcher;
        String img;
        String src;

        input = input.replaceAll(" style=\"[\\S\\s]+?\"", "");
        input = input.replace("[NextPage]", "[break]").replace("[/NextPage]", "");
        imgMatcher = StringUtil.getMatcher("<img [\\S\\s]+?/>", input);
        while (imgMatcher.find()) {
            img = imgMatcher.group();
            srcMatcher = StringUtil.getMatcher("src=\"([\\S\\s]+?)\"", img);
            if (srcMatcher.find()) {
                src = "[break][img]" + srcMatcher.group(1) + "[break]";
                input = input.replace(img, src);
            }
        }

        return input;
    }

    //    public static String clearHtmlTags(String input) {
    //        Matcher imgMatcher;
    //        Matcher srcMatcher;
    //        String img;
    //        String src;
    //
    ////        input = StringUtil.cleanHtmlSymbols(input);
    //        input = input.replaceAll(" style=\"[\\S\\s]+?\"","");
    //        input = input.replaceAll(" class=\"[\\S\\s]+?\"","");
    //        input = input.replaceAll(" align=\"[\\S\\s]+?\"","");
    //        input = input.replaceAll("<div[\\S\\s]+?>", "").replace("</div>", "");
    //        input = input.replaceAll("<a[\\S\\s]+?>", "").replace("</a>", "");
    //        input = input.replaceAll("<h1[\\S\\s]+?>", "").replace("</h1>", "");
    //        input = input.replaceAll("<br[\\S\\s]+?/>", "[break]");
    ////        input = input.replace("\r\n", "[break]");
    //        input = input.replaceAll("<span[\\S\\s]+?>", "").replace("</span>", "");
    //        input = input.replaceAll("<p[\\S\\s]+?>", "[break]").replace("</p>", "[break]");
    //        input = input.replaceAll("<strong[\\S\\s]+?>", "").replace("</strong>", "");
    //        input = input.replace("<hr/>", "");
    //        input = input.replace("[NextPage]", "[break][page]").replace("[/NextPage]", "");
    //        imgMatcher = StringUtil.getMatcher("<img [\\S\\s]+?/>", input);
    //        while (imgMatcher.find()) {
    //            img = imgMatcher.group();
    //            srcMatcher = StringUtil.getMatcher("src=\"([\\S\\s]+?)\"", img);
    //            if (srcMatcher.find()) {
    //                src = "[break][img]" + srcMatcher.group(1) + "[break]";
    //                input = input.replace(img, src);
    //            }
    //        }
    //
    //        return input;
    //    }

    public static Spanned convertUBB(String input, OnTagClickListener tagClickListener) {
        SpannableStringBuilder builder;
        Matcher matcher;

        if (input != null && input.length() > 0) {
            builder = new SpannableStringBuilder(input);

            matcher = StringUtil.getMatcher("<br(/>| [\\S\\s]+?>)", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), "\r\n");
            }

            matcher = StringUtil.getMatcher("\\[emot=([a-z]+?),([0-9]+?)/\\]", input);
            while (matcher.find()) {
                builder.setSpan(new ImageSpan(AcFunRead.readEmot(matcher.group(1), matcher.group(2))), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            matcher = StringUtil.getMatcher("\\[img\\]([\\S\\s]+?)\\[/img\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), "图像");
                builder.setSpan(new TagClickableSpan("[img]" + matcher.group(1), tagClickListener), matcher.start(0), matcher.start(0) + 2,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }


            return builder;
        } else {
            return null;
        }
    }
}
