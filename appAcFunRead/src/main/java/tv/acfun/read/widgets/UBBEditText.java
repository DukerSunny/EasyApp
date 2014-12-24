package tv.acfun.read.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.EditText;

import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.tools.ubb.UBBDecoder;
import tv.acfun.read.tools.ubb.UBBEncoder;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/11
 */
public class UBBEditText extends EditText {
    public final static int CONVERT_AT = 4;
    public final static int CONVERT_BOLD = 1;
    public final static int CONVERT_COLOR = 5;
    public final static int CONVERT_EMOT = 7;
    public final static int CONVERT_SIZE = 6;
    public final static int CONVERT_STRIKETHROUGH = 3;
    public final static int CONVERT_UNDERLINE = 2;
    private final static String TAG = "UBBEditText";
    private UBBDecoder mDecoder;
    private UBBEncoder mEncoder;
    private OnSelectionChangeListener mOnSelectionChangeListener = null;

    public UBBEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mEncoder = new UBBEncoder();
        mDecoder = new UBBDecoder();
    }

    private void clearSpans(SpannableStringBuilder builder, CharacterStyle[] spans) {
        int i;

        for (i = 0; i < spans.length; i++) {
            builder.removeSpan(spans[i]);
        }
    }

    private void convert(String ubbText) {
        setText(mEncoder.encode(ubbText, null));
    }

    public String getUBBText() {
        return mDecoder.decode(getText());
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

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);

        if (mOnSelectionChangeListener != null) {
            mOnSelectionChangeListener.onSelectionChanged(selStart, selEnd);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void selectAllWithAction() {
        Bundle bundle;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bundle = new Bundle();
            bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, 0);
            bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, length());
            performAccessibilityAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, bundle);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void selectWithAction(int start, int end) {
        Bundle bundle;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            bundle = new Bundle();
            bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, start);
            bundle.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, end);
            performAccessibilityAction(AccessibilityNodeInfo.ACTION_SET_SELECTION, bundle);
        }
    }

    public void setColor(int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        ForegroundColorSpan[] sizeSpans;
        ForegroundColorSpan sizeSpan;
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int spanStart;
        int spanEnd;
        int spanColor;

        if (builder.length() > 0) {
            if (start == end) {
                selectAllWithAction();
            } else {
                sizeSpans = builder.getSpans(start, end, ForegroundColorSpan.class);
                if (sizeSpans.length == 0) {
                    builder.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    sizeSpan = sizeSpans[sizeSpans.length - 1];
                    spanStart = builder.getSpanStart(sizeSpan);
                    spanEnd = builder.getSpanEnd(sizeSpan);
                    spanColor = sizeSpan.getForegroundColor();
                    clearSpans(builder, sizeSpans);
                    if (spanStart < start && spanEnd == end) {
                        builder.setSpan(new ForegroundColorSpan(spanColor), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart == start && spanEnd > end) {
                        builder.setSpan(new ForegroundColorSpan(spanColor), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart < start && spanEnd > end) {
                        builder.setSpan(new ForegroundColorSpan(spanColor), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new ForegroundColorSpan(spanColor), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart > start || spanEnd < end) {
                        builder.setSpan(new ForegroundColorSpan(color), Math.min(start, spanStart), Math.max(end, spanEnd),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                setText(builder);
                selectWithAction(start, end);
            }
        }
    }

    public void setOnSelectionChangeListener(OnSelectionChangeListener onSelectionChangeListener) {
        mOnSelectionChangeListener = onSelectionChangeListener;
    }

    public void setSize(int size) {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        AbsoluteSizeSpan[] sizeSpans;
        AbsoluteSizeSpan sizeSpan;
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int spanStart;
        int spanEnd;
        int spanSize;

        if (builder.length() > 0) {
            if (start == end) {
                selectAllWithAction();
            } else {
                sizeSpans = builder.getSpans(start, end, AbsoluteSizeSpan.class);
                if (sizeSpans.length == 0) {
                    builder.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    sizeSpan = sizeSpans[sizeSpans.length - 1];
                    spanStart = builder.getSpanStart(sizeSpan);
                    spanEnd = builder.getSpanEnd(sizeSpan);
                    spanSize = sizeSpan.getSize();
                    clearSpans(builder, sizeSpans);
                    if (spanStart < start && spanEnd == end) {
                        builder.setSpan(new AbsoluteSizeSpan(spanSize, true), spanStart, start,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart == start && spanEnd > end) {
                        builder.setSpan(new AbsoluteSizeSpan(spanSize, true), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart < start && spanEnd > end) {
                        builder.setSpan(new AbsoluteSizeSpan(spanSize, true), spanStart, start,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new AbsoluteSizeSpan(spanSize, true), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart > start || spanEnd < end) {
                        builder.setSpan(new AbsoluteSizeSpan(size, true), Math.min(start, spanStart), Math.max(end, spanEnd),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                setText(builder);
                setSelection(start, end);
            }
        }
    }

    public void setUBBText(String ubbText) {
        convert(ubbText);
        setSelection(getText().length());
    }

    public void toggleBold() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        StyleSpan[] styleSpans;
        StyleSpan styleSpan;
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int spanStart;
        int spanEnd;

        if (builder.length() > 0) {
            if (start == end) {
                selectAllWithAction();
            } else {
                styleSpans = builder.getSpans(start, end, StyleSpan.class);
                if (styleSpans.length == 0) {
                    builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    styleSpan = styleSpans[styleSpans.length - 1];
                    spanStart = builder.getSpanStart(styleSpan);
                    spanEnd = builder.getSpanEnd(styleSpan);
                    clearSpans(builder, styleSpans);
                    if (spanStart < start && spanEnd == end) {
                        builder.setSpan(new StyleSpan(Typeface.BOLD), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart == start && spanEnd > end) {
                        builder.setSpan(new StyleSpan(Typeface.BOLD), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart < start && spanEnd > end) {
                        builder.setSpan(new StyleSpan(Typeface.BOLD), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new StyleSpan(Typeface.BOLD), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart > start || spanEnd < end) {
                        builder.setSpan(new StyleSpan(Typeface.BOLD), Math.min(start, spanStart), Math.max(end, spanEnd),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                setText(builder);
                selectWithAction(start, end);
            }
        }
    }

    public void toggleItalic() {
    }

    public void toggleStrikethrough() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        StrikethroughSpan[] styleSpans;
        StrikethroughSpan styleSpan;
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int spanStart;
        int spanEnd;

        if (builder.length() > 0) {
            if (start == end) {
                selectAllWithAction();
            } else {
                styleSpans = builder.getSpans(start, end, StrikethroughSpan.class);
                if (styleSpans.length == 0) {
                    builder.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    styleSpan = styleSpans[styleSpans.length - 1];
                    spanStart = builder.getSpanStart(styleSpan);
                    spanEnd = builder.getSpanEnd(styleSpan);
                    clearSpans(builder, styleSpans);
                    if (spanStart < start && spanEnd == end) {
                        builder.setSpan(new StrikethroughSpan(), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart == start && spanEnd > end) {
                        builder.setSpan(new StrikethroughSpan(), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart < start && spanEnd > end) {
                        builder.setSpan(new StrikethroughSpan(), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new StrikethroughSpan(), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart > start || spanEnd < end) {
                        builder.setSpan(new StrikethroughSpan(), Math.min(start, spanStart), Math.max(end, spanEnd),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                setText(builder);
                selectWithAction(start, end);
            }
        }
    }

    public void toggleUnderline() {
        SpannableStringBuilder builder = new SpannableStringBuilder(getText());
        UnderlineSpan[] styleSpans;
        UnderlineSpan styleSpan;
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int spanStart;
        int spanEnd;

        if (builder.length() > 0) {
            if (start == end) {
                selectAllWithAction();
            } else {
                styleSpans = builder.getSpans(start, end, UnderlineSpan.class);
                if (styleSpans.length == 0) {
                    builder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    styleSpan = styleSpans[styleSpans.length - 1];
                    spanStart = builder.getSpanStart(styleSpan);
                    spanEnd = builder.getSpanEnd(styleSpan);
                    clearSpans(builder, styleSpans);
                    if (spanStart < start && spanEnd == end) {
                        builder.setSpan(new UnderlineSpan(), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart == start && spanEnd > end) {
                        builder.setSpan(new UnderlineSpan(), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart < start && spanEnd > end) {
                        builder.setSpan(new UnderlineSpan(), spanStart, start, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        builder.setSpan(new UnderlineSpan(), end, spanEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (spanStart > start || spanEnd < end) {
                        builder.setSpan(new UnderlineSpan(), Math.min(start, spanStart), Math.max(end, spanEnd),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                setText(builder);
                selectWithAction(start, end);
            }
        }
    }

    public void unselect() {
        setSelection(getSelectionEnd());
    }

    public interface OnSelectionChangeListener {
        public void onSelectionChanged(int start, int end);
    }
}