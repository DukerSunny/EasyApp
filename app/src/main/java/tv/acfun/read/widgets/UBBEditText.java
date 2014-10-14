package tv.acfun.read.widgets;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.harreke.easyapp.tools.TagClickableSpan;

import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.tools.UBB;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/11
 */
public class UBBEditText extends EditText {
    private final static String TAG = "UBBEditText";

    public UBBEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void convert(String rawText) {
        setText(UBB.fromUBB(rawText, null));
    }

    public String getRawText() {
        Log.e(TAG, "getRawText" + UBB.toUBB(getText()));

        return UBB.toUBB(getText());
    }

    private String getString(SpannableStringBuilder builder, int start, int end) {
        char[] chars = new char[end - start];
        builder.getChars(start, end, chars, 0);

        return new String(chars);
    }

    public void insertEmot(String which, int position) {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        String emotIndex = (position < 9 ? "0" : "") + (position + 1);
        String emotUBB = "[emot=" + which + "," + emotIndex + "/]";
        ImageSpan emotSpan = new ImageSpan(AcFunRead.readEmot(which, emotIndex));

        if (start != end) {
            builder.replace(start, end, emotUBB);
        } else {
            builder.insert(start, emotUBB);
        }
        builder.setSpan(emotSpan, start, start + emotUBB.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(builder);
        setSelection(start + emotUBB.length());
    }

    public void setColor(int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        ForegroundColorSpan[] colorSpans;
        ForegroundColorSpan colorSpan;
        int i;

        if (start != end) {
            colorSpans = builder.getSpans(start, end, ForegroundColorSpan.class);
            if (colorSpans.length > 0) {
                for (i = 0; i < colorSpans.length; i++) {
                    builder.removeSpan(colorSpans[i]);
                }
            } else {
                colorSpan = new ForegroundColorSpan(color);
                builder.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
            setSelection(end);
        }
    }

    public void setRawText(String rawText) {
        Log.e(TAG, "setRawText " + rawText);
        convert(rawText);
        setSelection(getText().length());
    }

    public void setSize(int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        AbsoluteSizeSpan[] sizeSpans;
        AbsoluteSizeSpan sizeSpan;
        int i;

        if (start != end) {
            sizeSpans = builder.getSpans(start, end, AbsoluteSizeSpan.class);
            if (sizeSpans.length > 0) {
                for (i = 0; i < sizeSpans.length; i++) {
                    builder.removeSpan(sizeSpans[i]);
                }
            } else {
                sizeSpan = new AbsoluteSizeSpan(size, true);
                builder.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
            setSelection(end);
        }
    }

    public void toggleAt() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        String atText;
        TagClickableSpan[] tagClickableSpans;
        TagClickableSpan tagClickableSpan;
        int i;

        if (start != end) {
            tagClickableSpans = builder.getSpans(start, end, TagClickableSpan.class);
            if (tagClickableSpans.length > 0) {
                for (i = 0; i < tagClickableSpans.length; i++) {
                    tagClickableSpan = tagClickableSpans[i];
                    if ("at".equals(tagClickableSpan.getTag())) {
                        builder.removeSpan(tagClickableSpan);
                    }
                }
                setText(builder);
                setSelection(end);
            } else {
                atText = "@" + getString(builder, start, end);
                if (!atText.contains("[emot")) {
                    builder.replace(start, end, atText);
                    tagClickableSpan = new TagClickableSpan("at", atText, null);
                    builder.setSpan(tagClickableSpan, start, start + atText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    setText(builder);
                    setSelection(start + atText.length());
                }
            }
        }
    }

    public void toggleBold() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        StyleSpan[] styleSpans;
        StyleSpan styleSpan;
        int i;

        if (start != end) {
            styleSpans = builder.getSpans(start, end, StyleSpan.class);
            if (styleSpans.length > 0) {
                for (i = 0; i < styleSpans.length; i++) {
                    builder.removeSpan(styleSpans[i]);
                }
            } else {
                styleSpan = new StyleSpan(Typeface.BOLD);
                builder.setSpan(styleSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
            setSelection(end);
        }
    }

    public void toggleItalic() {
    }

    public void toggleStrikethrough() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        StrikethroughSpan[] strikethroughSpans;
        StrikethroughSpan strikethroughSpan;
        int i;

        if (start != end) {
            strikethroughSpans = builder.getSpans(start, end, StrikethroughSpan.class);
            if (strikethroughSpans.length > 0) {
                for (i = 0; i < strikethroughSpans.length; i++) {
                    builder.removeSpan(strikethroughSpans[i]);
                }
            } else {
                strikethroughSpan = new StrikethroughSpan();
                builder.setSpan(strikethroughSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
            setSelection(end);
        }
    }

    public void toggleUnderline() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        int start = getSelectionStart();
        int end = getSelectionEnd();
        UnderlineSpan[] underlineSpans;
        UnderlineSpan underlineSpan;
        int i;

        if (start != end) {
            underlineSpans = builder.getSpans(start, end, UnderlineSpan.class);
            if (underlineSpans.length > 0) {
                for (i = 0; i < underlineSpans.length; i++) {
                    builder.removeSpan(underlineSpans[i]);
                }
            } else {
                underlineSpan = new UnderlineSpan();
                builder.setSpan(underlineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            setText(builder);
            setSelection(end);
        }
    }
}