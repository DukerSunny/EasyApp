package tv.acfun.read.widgets;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.harreke.easyapp.listeners.OnTagClickListener;

import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.tools.CommentUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/11
 */
public class UBBEditText extends EditText {
    private final static String TAG = "UBBEditText";

    private boolean mBlock = false;
    private SpannableStringBuilder mBuilder;
    private OnTagClickListener mTagClickListener = null;

    public UBBEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.e(TAG, "after change " + s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e(TAG, "before change " + s + " start=" + start + " count=" + count + " after=" + after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e(TAG, "text change " + s + " start=" + start + " before=" + before + " count=" + count);
            }
        });
        mBuilder = new SpannableStringBuilder();
    }

    private void convert(StringBuilder builder) {
        Log.e(TAG, "rawText=" + builder.toString());
        mBlock = true;
        setText(CommentUtil.convertUBB(builder.toString(), mTagClickListener));
        //        setText(builder);
        mBlock = false;
    }

    public String getRawText() {
        return getText().toString();
    }

    public void insertEmot(String which, int position) {
        String emotIndex = (position < 9 ? "0" : "") + (position + 1);
        String emot = "[emot=" + which + "," + emotIndex + "/]";
        int start = getSelectionStart();
        int end = getSelectionEnd();

        if (start != end) {
            Log.e(TAG, "emot replace " + start + "-" + end + " to " + emot);
            mBuilder.replace(start, end, emot);
        } else {
            Log.e(TAG, "emot insert " + emot + " at " + start);
            mBuilder.insert(start, emot);
        }
        mBuilder.setSpan(new ImageSpan(AcFunRead.readEmot(which, emotIndex)), start, start + emot.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setText(mBuilder);
        setSelection(start + emot.length());
    }

    //    @Override
    //    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
    //        if (!mBlock && mBuilder != null) {
    //            mBuilder.replace(start, lengthBefore, text.toString());
    //            Log.e(TAG, "on text chaged " + text + " start=" + start + " lengthbefor=" + lengthBefore + " lengthafter=" +
    //                    lengthAfter);
    //        }
    //    }

    public void setColor(int color) {

    }

    public void setOnTagClickListener(OnTagClickListener tagClickListener) {
        mTagClickListener = tagClickListener;
    }

    public void setRawText(String rawText) {
        mBuilder.clear();
        mBuilder.append(rawText);
        setText(mBuilder);
        setSelection(mBuilder.length());
    }

    public void setSize(int size) {

    }

    public void toggleBold() {

    }

    public void toggleItalic() {

    }

    public void toggleStrikethrough() {

    }

    public void toggleUnderline() {

    }
}