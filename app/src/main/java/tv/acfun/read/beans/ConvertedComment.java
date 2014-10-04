package tv.acfun.read.beans;

import android.content.Context;
import android.util.Log;

import com.harreke.easyapp.tools.StringUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class ConvertedComment {
    private int mCommentId;
    private String mDate;
    private int mFloorindex;
    private boolean mHasRepeatQuote;
    private boolean mInQuoteMode;
    private String mText;
    private String mUserImg;
    private String mUsername;

    public int getCommentId() {
        return mCommentId;
    }

    public String getDate() {
        return mDate;
    }

    public int getFloorindex() {
        return mFloorindex;
    }

    public String getText() {
        return mText;
    }

    public String getUserImg() {
        return mUserImg;
    }

    public String getUsername() {
        return mUsername;
    }

    public boolean hasRepeatQuote() {
        return mHasRepeatQuote;
    }

    public boolean isInQuoteMode() {
        return mInQuoteMode;
    }

    public void parse(Context context, Comment comment) {
        mCommentId = comment.getId();
        mUserImg = comment.getUser().getUserImg();
        mFloorindex = comment.getFloorindex();
        mUsername = comment.getUser().getUsername();
        mDate = new SimpleDateFormat(context.getString(R.string.comment_date)).format(new Date(comment.getTime()));
        mText = StringUtil.UBBDecode(comment.getContent());
        Log.e(null, mText);
    }

    public void setHasRepeatQuote(boolean hasRepeatQuote) {
        mHasRepeatQuote = hasRepeatQuote;
    }

    public void setInQuoteMode(boolean inQuoteMode) {
        mInQuoteMode = inQuoteMode;
    }
}