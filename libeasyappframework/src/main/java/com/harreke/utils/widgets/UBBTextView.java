package com.harreke.utils.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import com.harreke.utils.listeners.OnTagClickListener;
import com.harreke.utils.tools.FileUtil;
import com.harreke.utils.tools.Regular;
import com.harreke.utils.tools.TagClickableSpan;

import java.util.regex.Matcher;

/**
 UBB文本视图

 设置的UBB文本会自动转换成对应的可操作标签

 比如：“[img="http://www.xxx.com/pic/pic1.png]图片[/img]”，会被替换为带有下划线的文本“图片”，点击可跳转打开图片”http://www.xxx.com/pic/pic1.png“
 */
public class UBBTextView extends TextView {
    private String mCacheDir;
    private int mEmotionSize;
    private OnTagClickListener mTagClickListener = null;

    public UBBTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mEmotionSize = (int) (context.getResources().getDisplayMetrics().density * 48f);
        mCacheDir = context.getCacheDir().getAbsolutePath() + "/Emotion/";
    }

    /**
     设置标签点击监听器

     @param mTagClickListener
     标签点击监听器
     */
    public final void setOnTagClickListener(OnTagClickListener mTagClickListener) {
        mTagClickListener = mTagClickListener;
    }

    /**
     设置UBB文本

     @param input
     UBB文本

     @return 经过转换的Spannable
     */
    public final Spannable setUBBText(String input) {
        SpannableStringBuilder builder = null;
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        String imageTag;
        String userTag;
        String acTag;
        String[] emotion;
        Matcher matcher;

        if (input != null && input.length() > 0) {
            builder = new SpannableStringBuilder(input);
            /**
             替换[b][/b]
             */
            matcher = Regular.getMatcher("\\[b\\]([\\S\\s]+?)\\[/b\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
                builder.setSpan(boldSpan, matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[i][/i]
             */
            matcher = Regular.getMatcher("\\[i\\]([\\S\\s]+?)\\[/i\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
                builder.setSpan(italicSpan, matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[u][/u]
             */
            matcher = Regular.getMatcher("\\[u\\]([\\S\\s]+?)\\[/u\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
                builder.setSpan(underlineSpan, matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[align][/align]
             */
            matcher = Regular.getMatcher("\\[(align=[\\S\\s]+?|/align)\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), "");
            }
            /**
             替换[email][/email]
             */
            matcher = Regular.getMatcher("\\[email[\\S\\s]+?/email\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), "email:");
            }
            /**
             替换[img][/img]
             */
            matcher = Regular.getMatcher("\\[img=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/img\\]", input);
            while (matcher.find()) {
                imageTag = matcher.group(1);
                builder = builder.replace(matcher.start(0), matcher.end(0), imageTag);
                builder.setSpan(new TagClickableSpan("image:" + matcher.group(2), mTagClickListener), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[at][/at]
             */
            matcher = Regular.getMatcher("\\[at\\]([\\S\\s]+?)\\[/at\\]", input);
            while (matcher.find()) {
                userTag = "@" + matcher.group(1);
                builder = builder.replace(matcher.start(0), matcher.end(0), userTag);
                builder.setSpan(new TagClickableSpan("at:" + matcher.group(1), mTagClickListener), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[ac][/ac]
             */
            matcher = Regular.getMatcher("\\[ac=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/ac\\]", input);
            while (matcher.find()) {
                acTag = matcher.group(1);
                builder = builder.replace(matcher.start(0), matcher.end(0), acTag);
                builder.setSpan(new TagClickableSpan("ac:" + matcher.group(2), mTagClickListener), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[font][/font]
             */
            matcher = Regular.getMatcher("\"\\[font=[\\S\\s]+?\\]([\\S\\s]+?)\\[/font\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
            }
            /**
             替换[back][/back]
             */
            matcher = Regular.getMatcher("\"\\[back=[\\S\\s]+?\\]([\\S\\s]+?)\\[/back\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
            }
            /**
             替换[emot][/emot]
             */
            matcher = Regular.getMatcher("\\[emot=([\\S\\s]+?)/\\]", input);
            while (matcher.find()) {
                emotion = matcher.group(1).split(",");
                builder.setSpan(new ImageSpan(FileUtil.readDrawable(mCacheDir + "/" + emotion[0] + "/" + emotion[1], mEmotionSize, mEmotionSize)),
                        matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[color][/color]
             */
            matcher = Regular.getMatcher("\\[color=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/color\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(2));
                builder.setSpan(new ForegroundColorSpan(Color.parseColor(matcher.group(1))), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[size][/size]
             */
            matcher = Regular.getMatcher("\\[size=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/size\\]", input);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(2));
                builder.setSpan(new AbsoluteSizeSpan(Integer.valueOf(matcher.group(1).replace("px", ""))), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
        }

        return builder;
    }
}