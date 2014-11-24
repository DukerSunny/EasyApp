package tv.acfun.read.tools.ubb;

import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.SparseArray;

import com.harreke.easyapp.tools.TagClickableSpan;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/15
 */
public class UBBDecoder {
    public String decode(Spanned spanned) {
        StringBuilder builder = new StringBuilder(spanned);
        int length = spanned.length();
        SparseArray<String> ubbMap = new SparseArray<String>(length);
        CharacterStyle[] characterStyles = spanned.getSpans(0, length, CharacterStyle.class);
        CharacterStyle characterStyle;
        int start;
        int end;
        int i;
        String startUBB;
        String endUBB;
        TagClickableSpan tagClickableSpan;
        String ubb;
        int position;
        int offset = 0;

        //        Log.e(null, "decode request=" + builder);

        for (i = 0; i < characterStyles.length; i++) {
            characterStyle = characterStyles[i];
            start = spanned.getSpanStart(characterStyle);
            end = spanned.getSpanEnd(characterStyle);
            if (start > -1 && end > -1 && start != end) {
                startUBB = ubbMap.get(start);
                if (startUBB == null) {
                    startUBB = "";
                }
                endUBB = ubbMap.get(end);
                if (endUBB == null) {
                    endUBB = "";
                }
                if (characterStyle instanceof StyleSpan) {
                    startUBB += "[b]";
                    endUBB += "[/b]";
                } else if (characterStyle instanceof UnderlineSpan) {
                    startUBB += "[u]";
                    endUBB += "[/u]";
                } else if (characterStyle instanceof StrikethroughSpan) {
                    startUBB += "[s]";
                    endUBB += "[/s]";
                } else if (characterStyle instanceof ForegroundColorSpan) {
                    startUBB +=
                            "[color=#" + Integer.toHexString(((ForegroundColorSpan) characterStyle).getForegroundColor()) + "]";
                    endUBB += "[/color]";
                } else if (characterStyle instanceof AbsoluteSizeSpan) {
                    startUBB += "[size=" + ((AbsoluteSizeSpan) characterStyle).getSize() + "px]";
                    endUBB += "[/size]";
                } else if (characterStyle instanceof TagClickableSpan) {
                    tagClickableSpan = (TagClickableSpan) characterStyle;
                    if ("at".equals(tagClickableSpan.getTag())) {
                        startUBB += "[at]";
                        endUBB += "[/at]";
                    } else if ("ac".equals(tagClickableSpan.getTag())) {
                        startUBB += "[ac]";
                        endUBB += "[/ac]";
                    }
                }
                ubbMap.put(start, startUBB);
                ubbMap.put(end, endUBB);
            }
        }
        for (i = 0; i < ubbMap.size(); i++) {
            position = ubbMap.keyAt(i);
            ubb = ubbMap.valueAt(i);
            builder.insert(position + offset, ubb);
            offset += ubb.length();
        }

        //        Log.e(null, "decode result=" + builder.toString());

        return builder.toString();
    }
}