package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.bases.IFramework;
import com.harreke.easyapp.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.listeners.OnTagClickListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.beans.Setting;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.holders.CommentFloorHolder;
import tv.acfun.read.holders.CommentQuoteHolder;
import tv.acfun.read.parsers.CommentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/14
 */
public class QuoteActivity extends ActivityFramework {
    private View comment_new;
    private ActionClickListener mActionClickListener;
    private int mCommentId;
    private ViewPropertyAnimator mCommentNewAnimator;
    private float mCommentNewPosition = -1;
    private int mContentId;
    private EventListener mEventListener;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private int mMaxQuoteCount;
    private View.OnClickListener mOnAvatarClickListener;
    private View.OnClickListener mOnClickListener;
    private OnTagClickListener mOnTagClickListener;
    private int mPageNo;
    private QuoteRecyclerHelper mQuoteRecyclerHelper;
    private Conversion mSelectedConversion = null;
    private Snackbar mSnackbar;
    private int mTextSize;

    public static Intent create(Context context, int contentId, int pageNo, int commentId) {
        Intent intent = new Intent(context, QuoteActivity.class);

        intent.putExtra("contentId", contentId);
        intent.putExtra("pageNo", pageNo);
        intent.putExtra("commentId", commentId);

        return intent;
    }

    @Override
    public void acquireArguments(Intent intent) {
        Setting setting = AcFunRead.getInstance().readSetting();

        mContentId = intent.getIntExtra("contentId", 0);
        mPageNo = intent.getIntExtra("pageNo", 0);
        mCommentId = intent.getIntExtra("commentId", 0);

        mMaxQuoteCount = setting.getMaxQuoteCount();
        mTextSize = setting.getDefaultTextSize();
    }

    @Override
    public void attachCallbacks() {
        comment_new.setOnClickListener(mOnClickListener);
        mLoginHelper.setLoginCallback(mLoginCallback);
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.comment_text);
        setToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        comment_new = findViewById(R.id.comment_new);
        mCommentNewAnimator = ViewPropertyAnimator.animate(comment_new);

        mLoginHelper = new LoginHelper(this);

        mQuoteRecyclerHelper = new QuoteRecyclerHelper(this);
        mQuoteRecyclerHelper.setCanLoad(false);
        mQuoteRecyclerHelper.setHasFixedSize(false);
        mQuoteRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginHelper.validateLogin()) {
                    start(ReplyActivity.create(getContext(), mContentId, 0, 0), 0);
                }
            }
        };
        mOnTagClickListener = new OnTagClickListener() {
            @Override
            public void onTagClick(String tag, String link) {
                switch (tag) {
                    case "img":
                        start(ComicActivity.create(getContext(), link));
                        break;
                    case "ac":
                        start(ContentActivity.create(getContext(), Integer.valueOf(link)));
                        break;
                    case "at":
                        start(ProfileActivity.create(getContext(), link), Anim.Enter_Right);
                }
            }
        };
        mOnAvatarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ProfileActivity.create(getContext(), (Integer) v.getTag()), Anim.Enter_Right);
            }
        };
        mLoginCallback = new LoginHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                start(ReplyActivity.create(getContext(), mContentId, 0, 0), 0);
            }
        };
        mActionClickListener = new ActionClickListener() {
            @Override
            public void onActionClicked(Snackbar snackbar) {
                start(ReplyActivity
                        .create(getContext(), mContentId, mSelectedConversion.getCid(), mSelectedConversion.getCount()));
            }
        };
        mEventListener = new EventListener() {
            @Override
            public void onDismiss(Snackbar snackbar) {
                mCommentNewAnimator.y(mCommentNewPosition);
            }

            @Override
            public void onDismissed(Snackbar snackbar) {
                mSelectedConversion = null;
            }

            @Override
            public void onShow(Snackbar snackbar) {
                if (mCommentNewPosition == -1) {
                    mCommentNewPosition = ViewHelper.getY(comment_new);
                }
                mCommentNewAnimator.y(mCommentNewPosition - comment_new.getMeasuredHeight());
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            showToast(R.string.comment_success);
            mQuoteRecyclerHelper.clear();
            startAction();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_quote;
    }

    @Override
    public void startAction() {
        mQuoteRecyclerHelper.from(API.getContentComment(mContentId, 50, mPageNo));
    }

    private class QuoteRecyclerHelper extends RecyclerFramework<Conversion> {
        private final static int TYPE_FLOOR = 1;
        private final static int TYPE_QUOTE = 0;

        public QuoteRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Conversion> createHolder(View convertView, int viewType) {
            CommentQuoteHolder quoteHolder;
            CommentFloorHolder floorHolder;

            if (viewType == TYPE_QUOTE) {
                quoteHolder = new CommentQuoteHolder(convertView);
                quoteHolder.setTextSize(mTextSize);

                return quoteHolder;
            } else {
                floorHolder = new CommentFloorHolder(convertView);
                floorHolder.setTextSize(mTextSize);
                floorHolder.setOnAvatarClickListener(mOnAvatarClickListener);

                return floorHolder;
            }
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            if (viewType == TYPE_QUOTE) {
                return inflater.inflate(R.layout.item_comment_quote, parent, false);
            } else {
                return inflater.inflate(R.layout.item_comment_floor, parent, false);
            }
        }

        @Override
        protected int getViewType(int position) {
            return position == getItemCount() - 1 ? TYPE_FLOOR : TYPE_QUOTE;
        }

        @Override
        public void onItemClick(int position, Conversion conversion) {
            if (mSnackbar != null && mSnackbar.isShowing()) {
                mSnackbar.dismiss();
            }
            mSelectedConversion = conversion;
            mSnackbar = Snackbar.with(getContext()).actionLabel(R.string.comment_reply).dismissOnActionClicked(true);
            mSnackbar.text(getString(R.string.comment_select, conversion.getCount()));
            mSnackbar.actionListener(mActionClickListener).eventListener(mEventListener);
            mSnackbar.show(getActivity());
        }

        @Override
        public List<Conversion> onParse(String json) {
            CommentListParser parser = CommentListParser.parse(json, mMaxQuoteCount, mCommentId, mOnTagClickListener);
            FullConversion fullConversion;
            List<Conversion> itemList;

            if (parser != null && parser.getItemList() != null && parser.getItemList().size() > 0) {
                fullConversion = parser.getItemList().get(0);
                itemList = fullConversion.getQuoteList();
                itemList.add(fullConversion.getConversion());

                return itemList;
            } else {
                return null;
            }
        }

        @Override
        protected void setItemDecoration() {
        }
    }
}