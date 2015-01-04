package tv.acfun.read.tools.ubb;

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

import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.widgets.TagClickableSpan;

import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;

import tv.acfun.read.bases.application.AcFunRead;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/15
 */
public class UBBEncoder {
    private Queue<Integer> mAcStartQueue;
    private Queue<String> mAcValueQueue;
    private Queue<Integer> mAtStartQueue;
    private Queue<Integer> mBStartQueue;
    private SpannableStringBuilder mBuilder;
    private Queue<Integer> mColorStartQueue;
    private Queue<Integer> mColorValueQueue;
    private Queue<Integer> mImgStartQueue;
    private Queue<String> mImgValueQueue;
    private Queue<Integer> mSStartQueue;
    private Queue<Integer> mSizeStartQueue;
    private Queue<Integer> mSizeValueQueue;
    private OnTagClickListener mTagClickListener;
    private Queue<Integer> mUStartQueue;

    public Spanned encode(String input, OnTagClickListener tagClickListener) {
        Matcher matcher;
        String[] inputs;
        String temp;
        String tag;
        String value;
        int length;
        int i;

        //        Log.e(null, "encode request=" + input);

        input = input.replaceAll("\\[img=([\\S\\s]+?)\\]([\\S\\s]+?)\\[/img\\]", "[img=$2]$1[/img]");
        input = input.replaceAll("\\[ac=([0-9]+?)\\]([\\S\\s]+?)\\[/ac\\]", "[ac=$1]$2[/ac]");
        input = input.replaceAll("<br[\\s]*?/>", "\n");

        input = input.replaceAll("\\[[\\S\\s]+?\\]", "[break]$0[break]");
        input = input.replaceAll("\\[/[\\S\\s]+?\\]", "[break]$0[break]");

        inputs = input.split("\\[break\\]");

        mAcStartQueue = new LinkedList<Integer>();
        mAcValueQueue = new LinkedList<String>();
        mAtStartQueue = new LinkedList<Integer>();
        mBStartQueue = new LinkedList<Integer>();
        mColorStartQueue = new LinkedList<Integer>();
        mColorValueQueue = new LinkedList<Integer>();
        mImgStartQueue = new LinkedList<Integer>();
        mImgValueQueue = new LinkedList<String>();
        mSStartQueue = new LinkedList<Integer>();
        mSizeStartQueue = new LinkedList<Integer>();
        mSizeValueQueue = new LinkedList<Integer>();
        mUStartQueue = new LinkedList<Integer>();

        mTagClickListener = tagClickListener;

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
        //        Log.e(null, "encode result=" + mBuilder);

        return mBuilder;
    }

    private void endAc(int end) {
        String value = mAcValueQueue.poll();
        int start = mAcStartQueue.poll();

        mBuilder.setSpan(new TagClickableSpan("ac", value, mTagClickListener), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void endAt(int end) {
        int start = mAtStartQueue.poll();
        String value = StringUtil.getString(mBuilder, start, end);

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
        Matcher matcher = StringUtil.getMatcher("#[0-9a-fA-F]{3,6}", value);

        mColorStartQueue.offer(start);
        if (matcher.find()) {
            mColorValueQueue.offer(Color.parseColor(matcher.group(0)));
        } else {
            mColorValueQueue.offer(Color.BLACK);
        }
    }

    private void startImg(int start, String value) {
        mImgStartQueue.offer(start);
        mImgValueQueue.offer(value);
    }

    private void startS(int start) {
        mSStartQueue.offer(start);
    }

    private void startSize(int start, String value) {
        Matcher matcher = StringUtil.getMatcher("([0-9]+?)px", value);

        mSizeStartQueue.offer(start);
        if (matcher.find()) {
            mSizeValueQueue.offer((int) Double.parseDouble(matcher.group(1)));
        } else {
            mSizeValueQueue.offer(14);
        }
    }

    private void startU(int start) {
        mUStartQueue.offer(start);
    }
}
