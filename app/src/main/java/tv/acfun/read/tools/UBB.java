package tv.acfun.read.tools;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;

import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.tools.StringUtil;
import com.harreke.easyapp.tools.TagClickableSpan;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;

import tv.acfun.read.bases.application.AcFunRead;

class Decoder {
    private SpannableStringBuilder mBuilder;
    private Spanned mSpanned;

    public Decoder(Spanned spanned) {
        mSpanned = spanned;
    }

    public String decode() {
        mBuilder = new SpannableStringBuilder(mSpanned);

        decodeB();
        decodeUnderline();
        decodeStrikethrough();
        decodeColor();
        decodeSize();
        decodeImg();
        decodeAt();
        decodeAc();
        decodeEmot();

        return mBuilder.toString();
    }

    private void decodeAc() {
        TagClickableSpan[] linkSpans = mBuilder.getSpans(0, mBuilder.length(), TagClickableSpan.class);
        TagClickableSpan linkSpan;
        String text;
        int start;
        int end;
        int i;

        for (i = 0; i < linkSpans.length; i++) {
            linkSpan = linkSpans[i];
            if ("ac".equals(linkSpan.getTag())) {
                start = mBuilder.getSpanStart(linkSpan);
                end = mBuilder.getSpanEnd(linkSpan);
                text = UBB.getString(mBuilder, start, end);
                mBuilder.removeSpan(linkSpan);
                mBuilder.replace(start, end, "[ac]" + text + "[/ac]");
            }
        }
    }

    private void decodeAt() {
        TagClickableSpan[] linkSpans = mBuilder.getSpans(0, mBuilder.length(), TagClickableSpan.class);
        TagClickableSpan linkSpan;
        String value;
        int start;
        int end;
        int i;

        for (i = 0; i < linkSpans.length; i++) {
            linkSpan = linkSpans[i];
            if ("at".equals(linkSpan.getTag())) {
                start = mBuilder.getSpanStart(linkSpan);
                end = mBuilder.getSpanEnd(linkSpan);
                value = linkSpan.getLink().substring(1);
                mBuilder.removeSpan(linkSpan);
                mBuilder.replace(start, end, "[at]" + value + "[/at]");
            }
        }
    }

    private void decodeB() {
        StyleSpan[] styleSpans = mBuilder.getSpans(0, mBuilder.length(), StyleSpan.class);
        StyleSpan styleSpan;
        String text;
        int start;
        int end;
        int i;

        for (i = 0; i < styleSpans.length; i++) {
            styleSpan = styleSpans[i];
            start = mBuilder.getSpanStart(styleSpan);
            end = mBuilder.getSpanEnd(styleSpan);
            text = UBB.getString(mBuilder, start, end);
            mBuilder.removeSpan(styleSpan);
            mBuilder.replace(start, end, "[b]" + text + "[/b]");
        }
    }

    private void decodeColor() {
        ForegroundColorSpan[] colorSpans = mBuilder.getSpans(0, mBuilder.length(), ForegroundColorSpan.class);
        ForegroundColorSpan colorSpan;
        String text;
        String value;
        int start;
        int end;
        int i;

        for (i = 0; i < colorSpans.length; i++) {
            colorSpan = colorSpans[i];
            start = mBuilder.getSpanStart(colorSpan);
            end = mBuilder.getSpanEnd(colorSpan);
            text = UBB.getString(mBuilder, start, end);
            value = Integer.toHexString(colorSpan.getForegroundColor());
            mBuilder.removeSpan(colorSpan);
            mBuilder.replace(start, end, "[color=#" + value + "]" + text + "[/color]");
        }
    }

    private void decodeEmot() {
        ImageSpan[] imageSpans = mBuilder.getSpans(0, mBuilder.length(), ImageSpan.class);
        ImageSpan imageSpan;
        int i;

        for (i = 0; i < imageSpans.length; i++) {
            imageSpan = imageSpans[i];
            mBuilder.removeSpan(imageSpan);
        }
    }

    private void decodeImg() {
        TagClickableSpan[] linkSpans = mBuilder.getSpans(0, mBuilder.length(), TagClickableSpan.class);
        TagClickableSpan linkSpan;
        String text;
        String value;
        int start;
        int end;
        int i;

        for (i = 0; i < linkSpans.length; i++) {
            linkSpan = linkSpans[i];
            if ("img".equals(linkSpan.getTag())) {
                start = mBuilder.getSpanStart(linkSpan);
                end = mBuilder.getSpanEnd(linkSpan);
                text = UBB.getString(mBuilder, start, end);
                value = linkSpan.getLink();
                mBuilder.removeSpan(linkSpan);
                mBuilder.replace(start, end, "[img=" + text + "]" + value + "[/img]");
            }
        }
    }

    private void decodeSize() {
        AbsoluteSizeSpan[] sizeSpans = mBuilder.getSpans(0, mBuilder.length(), AbsoluteSizeSpan.class);
        AbsoluteSizeSpan sizeSpan;
        String text;
        int value;
        int start;
        int end;
        int i;

        for (i = 0; i < sizeSpans.length; i++) {
            sizeSpan = sizeSpans[i];
            start = mBuilder.getSpanStart(sizeSpan);
            end = mBuilder.getSpanEnd(sizeSpan);
            text = UBB.getString(mBuilder, start, end);
            value = sizeSpan.getSize();
            mBuilder.removeSpan(sizeSpan);
            mBuilder.replace(start, end, "[size=" + value + "px]" + text + "[/size]");
        }
    }

    private void decodeStrikethrough() {
        StrikethroughSpan[] strikethroughSpans = mBuilder.getSpans(0, mBuilder.length(), StrikethroughSpan.class);
        StrikethroughSpan strikethroughSpan;
        String text;
        int start;
        int end;
        int i;

        for (i = 0; i < strikethroughSpans.length; i++) {
            strikethroughSpan = strikethroughSpans[i];
            start = mBuilder.getSpanStart(strikethroughSpan);
            end = mBuilder.getSpanEnd(strikethroughSpan);
            text = UBB.getString(mBuilder, start, end);
            mBuilder.removeSpan(strikethroughSpan);
            mBuilder.replace(start, end, "[s]" + text + "[/s]");
        }
    }

    private void decodeUnderline() {
        UnderlineSpan[] underlineSpans = mBuilder.getSpans(0, mBuilder.length(), UnderlineSpan.class);
        UnderlineSpan underlineSpan;
        String text;
        int start;
        int end;
        int i;

        for (i = 0; i < underlineSpans.length; i++) {
            underlineSpan = underlineSpans[i];
            start = mBuilder.getSpanStart(underlineSpan);
            end = mBuilder.getSpanEnd(underlineSpan);
            text = UBB.getString(mBuilder, start, end);
            mBuilder.removeSpan(underlineSpan);
            mBuilder.replace(start, end, "[u]" + text + "[/u]");
        }
    }
}

class Encoder {
    private Queue<Integer> mAcStartQueue = new LinkedList<Integer>();
    private Queue<String> mAcValueQueue = new LinkedList<String>();
    private Queue<Integer> mAtStartQueue = new LinkedList<Integer>();
    private Queue<Integer> mBStartQueue = new LinkedList<Integer>();
    private SpannableStringBuilder mBuilder;
    private Queue<Integer> mColorStartQueue = new LinkedList<Integer>();
    private Queue<Integer> mColorValueQueue = new LinkedList<Integer>();
    private Queue<Integer> mImgStartQueue = new LinkedList<Integer>();
    private Queue<String> mImgValueQueue = new LinkedList<String>();
    private String mInput;
    private Queue<Integer> mSStartQueue = new LinkedList<Integer>();
    private Queue<Integer> mSizeStartQueue = new LinkedList<Integer>();
    private Queue<Integer> mSizeValueQueue = new LinkedList<Integer>();
    private OnTagClickListener mTagClickListener;
    private Queue<Integer> mUStartQueue = new LinkedList<Integer>();

    public Encoder(String input, OnTagClickListener tagClickListener) {
        mInput = input;
        mTagClickListener = tagClickListener;
    }

    public Spanned encode() {
        Matcher matcher;
        String[] inputs;
        String temp;
        String tag;
        String value;
        int length;
        int i;

        Log.e(null, "encode raw=" + mInput);

        mInput = mInput.replaceAll("\\[img=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/img\\]", "[img=$2]$1[/img]");
        mInput = mInput.replaceAll("\\[at\\]([\\S\\s]+?)\\[/at\\]", "[at]@$1[/at]");
        mInput = mInput.replaceAll("\\[ac=([0-9]+?)\\]([\\S\\s]+?)\\[/at\\]", "[ac=$1]$2[/ac]");
        mInput = mInput.replaceAll("<br[\\s]*?/>", "\n");

        Log.e(null, "encode replaced=" + mInput);

        mInput = mInput.replaceAll("\\[[\\S\\s]+?\\]", "[break]$0[break]");
        mInput = mInput.replaceAll("\\[/[\\S\\s]+?\\]", "[break]$0[break]");

        inputs = mInput.split("\\[break\\]");

        mBuilder = new SpannableStringBuilder();
        for (i = 0; i < inputs.length; i++) {
            temp = inputs[i];
            if (temp.length() > 0) {
                if (temp.charAt(0) != '[') {
                    mBuilder.append(temp);
                } else {
                    length = mBuilder.length();
                    matcher = StringUtil.getMatcher("\\[(/?[a-z]+?)(=([\\S\\s]+?))?\\]", temp);
                    if (matcher.find()) {
                        tag = matcher.group(1);
                        value = matcher.group(3);
                        if (tag.equals("b")) {
                            startB(length);
                        } else if (tag.equals("/b")) {
                            endB(length);
                        } else if (tag.equals("u")) {
                            startU(length);
                        } else if (tag.equals("/u")) {
                            endU(length);
                        } else if (tag.equals("s")) {
                            startS(length);
                        } else if (tag.equals("/s")) {
                            endS(length);
                        } else if (tag.equals("color")) {
                            startColor(length, value);
                        } else if (tag.equals("/color")) {
                            endColor(length);
                        } else if (tag.equals("size")) {
                            startSize(length, value);
                        } else if (tag.equals("/size")) {
                            endSize(length);
                        } else if (tag.equals("img")) {
                            startImg(length, value);
                        } else if (tag.equals("/img")) {
                            endImg(length);
                        } else if (tag.equals("at")) {
                            startAt(length);
                        } else if (tag.equals("/at")) {
                            endAt(length);
                        } else if (tag.equals("ac")) {
                            startAc(length, value);
                        } else if (tag.equals("/ac")) {
                            endAc(length);
                        } else if (tag.equals("emot")) {
                            endEmot(length, value);
                        }
                    }
                }
            }
        }
        Log.e(null, "encode result=" + mBuilder);

        return mBuilder;
    }

    private void endAc(int end) {
        String value = mAcValueQueue.poll();
        int start = mAcStartQueue.poll();

        mBuilder.setSpan(new TagClickableSpan("ac", value, mTagClickListener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endAt(int end) {
        int start = mAtStartQueue.poll();
        String value = UBB.getString(mBuilder, start, end).substring(1);

        mBuilder.setSpan(new TagClickableSpan("at", value, mTagClickListener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endB(int end) {
        int start = mBStartQueue.poll();

        mBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endColor(int end) {
        int value = mColorValueQueue.poll();
        int start = mColorStartQueue.poll();

        mBuilder.setSpan(new ForegroundColorSpan(value), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endEmot(int start, String value) {
        String emotText = "[emot=" + value + "]";
        Matcher matcher = StringUtil.getMatcher("([a-z]+?),([0-9]+?)/", value);

        if (matcher.find()) {
            mBuilder.append(emotText);
            mBuilder.setSpan(new ImageSpan(AcFunRead.readEmot(matcher.group(1), matcher.group(2))), start,
                    start + emotText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void endImg(int end) {
        String value = mImgValueQueue.poll();
        int start = mImgStartQueue.poll();

        mBuilder.setSpan(new TagClickableSpan("img", value, mTagClickListener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endS(int end) {
        int start = mSStartQueue.poll();

        mBuilder.setSpan(new StrikethroughSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endSize(int end) {
        int value = mSizeValueQueue.poll();
        int start = mSizeStartQueue.poll();

        mBuilder.setSpan(new AbsoluteSizeSpan(value, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endU(int end) {
        int start = mUStartQueue.poll();

        mBuilder.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void startAc(int start, String value) {
        mAcStartQueue.offer(start);
        mAcValueQueue.offer(value);
    }

    private void startAt(int start) {
        mAtStartQueue.offer(start);
    }

    private void startB(int start) {
        mBStartQueue.offer(start);
    }

    private void startColor(int start, String value) {
        mColorStartQueue.offer(start);
        mColorValueQueue.offer(Color.parseColor(value));
    }

    private void startImg(int start, String value) {
        mImgStartQueue.offer(start);
        mImgValueQueue.offer(value);
    }

    private void startS(int start) {
        mSStartQueue.offer(start);
    }

    private void startSize(int start, String value) {
        mSizeStartQueue.offer(start);
        mSizeValueQueue.offer(Integer.valueOf(value.replace("px", "")));
    }

    private void startU(int start) {
        mUStartQueue.offer(start);
    }
}

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/08
 */
public class UBB {
    private final static String TAG = "UBB";

    public static Spanned fromUBB(String input, OnTagClickListener tagClickListener) {
        return new Encoder(input, tagClickListener).encode();
    }

    protected static String getString(SpannableStringBuilder builder, int start, int end) {
        char[] chars = new char[end - start];
        builder.getChars(start, end, chars, 0);

        return new String(chars);
    }

    public static String toUBB(Spanned input) {
        return new Decoder(input).decode();
    }
}