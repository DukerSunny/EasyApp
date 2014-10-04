package tv.acfun.read.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.tools.StringUtil;
import com.harreke.easyapp.tools.TagClickableSpan;

import java.util.regex.Matcher;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * UBB文本视图
 *
 * 设置的UBB文本会自动转换成对应的可操作标签
 *
 * 比如：“[img="http://www.xxx.com/pic/pic1.png]图片[/img]”，会被替换为带有下划线的文本“图片”，点击可跳转打开图片”http://www.xxx.com/pic/pic1.png“
 */
public class UBBTextView extends TextView {
    private boolean linkHit;
    private String mCacheDir;
    private int mEmotionSize;
    private OnTagClickListener mTagClickListener = null;

    public UBBTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (!isInEditMode()) {
            mEmotionSize = (int) (context.getResources().getDisplayMetrics().density * 48f);
            mCacheDir = AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS;
            setMovementMethod(LocalLinkMovementMethod.getInstance());
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        super.onTouchEvent(event);

        return linkHit;
    }

    /**
     * 设置标签点击监听器
     *
     * @param tagClickListener
     *         标签点击监听器
     */
    public final void setOnTagClickListener(OnTagClickListener tagClickListener) {
        mTagClickListener = tagClickListener;
    }

    /**
     * 设置UBB文本
     *
     * @param input
     *         UBB文本
     *
     * @return 经过转换的Spannable
     */
    public final Spannable setUBBText(String input) {
        SpannableStringBuilder builder = null;
        StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        UnderlineSpan underlineSpan = new UnderlineSpan();
        String imageTag;
        String userTag;
        String acTag;
        Matcher matcher;
        Drawable drawable;

        if (input != null && input.length() > 0) {
            input = StringUtil.cleanHtmlSymbols(input);
            input = input.replace("<br/>", "\r\n");
            input = input.replace("<br />", "\r\n");
            builder = new SpannableStringBuilder(input);
            /**
             替换[b][/b]
             */
            matcher = StringUtil.getMatcher("\\[b\\]([\\S\\s]+?)\\[/b\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
                builder.setSpan(boldSpan, matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[i][/i]
             */
            matcher = StringUtil.getMatcher("\\[i\\]([\\S\\s]+?)\\[/i\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
                builder.setSpan(italicSpan, matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[u][/u]
             */
            matcher = StringUtil.getMatcher("\\[u\\]([\\S\\s]+?)\\[/u\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
                builder.setSpan(underlineSpan, matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[align][/align]
             */
            matcher = StringUtil.getMatcher("\\[(align=[\\S\\s]+?|/align)\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), "");
            }
            /**
             替换[email][/email]
             */
            matcher = StringUtil.getMatcher("\\[email[\\S\\s]+?/email\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), "email:");
            }
            /**
             替换[img][/img]
             */
            matcher = StringUtil.getMatcher("\\[img=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/img\\]", builder);
            while (matcher.find()) {
                imageTag = matcher.group(1);
                builder = builder.replace(matcher.start(0), matcher.end(0), imageTag);
                builder.setSpan(new TagClickableSpan("image:" + matcher.group(2), mTagClickListener), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[at][/at]
             */
            matcher = StringUtil.getMatcher("\\[at\\]([\\S\\s]+?)\\[/at\\]", builder);
            while (matcher.find()) {
                userTag = "@" + matcher.group(1);
                builder = builder.replace(matcher.start(0), matcher.end(0), userTag);
                builder.setSpan(new TagClickableSpan("at:" + matcher.group(1), mTagClickListener), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[ac][/ac]
             */
            matcher = StringUtil.getMatcher("\\[ac=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/ac\\]", builder);
            while (matcher.find()) {
                acTag = matcher.group(1);
                builder = builder.replace(matcher.start(0), matcher.end(0), acTag);
                builder.setSpan(new TagClickableSpan("ac:" + matcher.group(2), mTagClickListener), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[font][/font]
             */
            matcher = StringUtil.getMatcher("\"\\[font=[\\S\\s]+?\\]([\\S\\s]+?)\\[/font\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
            }
            /**
             替换[back][/back]
             */
            matcher = StringUtil.getMatcher("\"\\[back=[\\S\\s]+?\\]([\\S\\s]+?)\\[/back\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(1));
            }
            /**
             替换[emot][/emot]
             */
            matcher = StringUtil.getMatcher("\\[emot=([a-z]+),([0-9]+)/\\]", builder);
            while (matcher.find()) {
                drawable = AcFunRead.readEmot(matcher.group(1), matcher.group(2));
                if (drawable != null) {
                    builder.setSpan(new ImageSpan(drawable), matcher.start(0), matcher.end(0), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    builder.replace(matcher.start(0), matcher.end(0), "");
                }
            }
            /**
             替换[color][/color]
             */
            matcher = StringUtil.getMatcher("\\[color=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/color\\]", builder);
            while (matcher.find()) {
                Log.e(null, matcher.group(0));
                Log.e(null, matcher.group(1));
                Log.e(null, matcher.group(2) + "\n");
//                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(2));
//                builder.setSpan(new ForegroundColorSpan(Color.parseColor(matcher.group(1))), matcher.start(0), matcher.end(0),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            /**
             替换[size][/size]
             */
            matcher = StringUtil.getMatcher("\\[size=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/size\\]", builder);
            while (matcher.find()) {
                builder = builder.replace(matcher.start(0), matcher.end(0), matcher.group(2));
                builder.setSpan(new AbsoluteSizeSpan(Integer.valueOf(matcher.group(1).replace("px", ""))), matcher.start(0), matcher.end(0),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
        }

        return builder;
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;


        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null) {
                sInstance = new LocalLinkMovementMethod();
            }

            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        link[0].onClick(widget);
                    } else {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
                    }

                    if (widget instanceof UBBTextView) {
                        ((UBBTextView) widget).linkHit = true;
                    }
                    return true;
                } else {
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }
}