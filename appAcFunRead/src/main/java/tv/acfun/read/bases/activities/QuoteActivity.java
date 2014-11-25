package tv.acfun.read.bases.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.MenuItem;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.InfoView;
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
    private IRequestCallback<String> mCallback;
    private Conversion mCommentFloor;
    private CommentFloorHolder mCommentFloorHolder;
    private int mCommentId;
    private int mContentId;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private int mMaxQuoteCount;
    private View.OnClickListener mOnCloseClickListener;
    private View.OnClickListener mOnCopyClickListener;
    private View.OnClickListener mOnOpenClickListener;
    private View.OnClickListener mOnReplyClickListener;
    private OnTagClickListener mOnTagClickListener;
    private View.OnClickListener mOnUserClickListener;
    private int mPageNo;
    private QuoteListHelper mQuoteListHelper;
    private QuoteParseTask mQuoteParseTask = null;
    private boolean[] mSwipeStatus;
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

    }

    private void closeAllSwipe() {
        int i;

        for (i = 0; i < mSwipeStatus.length; i++) {
            closeSwipe(i);
        }
    }

    private void closeSwipe(int position) {
        mSwipeStatus[position] = false;
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.comment_text);
        setToolbarNavigation(R.drawable.image_back_inverse);
        addToolbarItem(0, R.string.comment_reply, R.drawable.image_reply);
    }

    @Override
    public void enquiryViews() {
        View commentFloor = View.inflate(this, R.layout.item_comment_floor, null);

        mCommentFloorHolder = new CommentFloorHolder(commentFloor);
        mCommentFloorHolder.setTextSize(mTextSize);
        mCommentFloorHolder.setOnOpenClickListener(mOnOpenClickListener);
        mCommentFloorHolder.setOnCloseClickListener(mOnCloseClickListener);
        mCommentFloorHolder.setOnUserClickListener(mOnUserClickListener);
        mCommentFloorHolder.setOnCopyClickListener(mOnCopyClickListener);
        mCommentFloorHolder.setOnReplyClickListener(mOnReplyClickListener);

        mLoginHelper = new LoginHelper(getActivity(), mLoginCallback);

        mQuoteListHelper = new QuoteListHelper(this, R.id.quote_list);
        mQuoteListHelper.addFooterView(commentFloor);
        mQuoteListHelper.bindAdapter();
    }

    @Override
    public void establishCallbacks() {
        mCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                setInfoVisibility(InfoView.INFO_ERROR);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                mQuoteParseTask = new QuoteParseTask();
                mQuoteParseTask.execute(result);
            }
        };
        mOnTagClickListener = new OnTagClickListener() {
            @Override
            public void onTagClick(String tag, String link) {
                if ("img".equals(tag)) {
                    start(ComicActivity.create(getActivity(), link));
                } else if ("ac".equals(tag)) {
                    start(ContentActivity.create(getActivity(), Integer.valueOf(link)));
                } else if ("at".equals(tag)) {
                    start(ProfileActivity.create(getActivity(), link));
                    getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
            }
        };
        mOnUserClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(ProfileActivity.create(getActivity(), (Integer) v.getTag()));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        };
        mOnCopyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversion conversion = getConversionFromViewTag(v);
                ClipboardManager manager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                manager.setPrimaryClip(ClipData.newPlainText(null, conversion.getContent()));
                showToast(R.string.comment_copied);
            }
        };
        mOnReplyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversion conversion = getConversionFromViewTag(v);

                start(ReplyActivity.create(getActivity(), mContentId, conversion.getCid(), conversion.getCount()), 0);
            }
        };
        mOnCloseClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                if (position == -1) {
                    mCommentFloorHolder.closeSwipe();
                } else {
                    closeSwipe(position);
                    mQuoteListHelper.refresh();
                }
            }
        };
        mOnOpenClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                int i;

                if (position == -1) {
                    mCommentFloorHolder.openSwipe();
                    closeAllSwipe();
                } else {
                    for (i = 0; i < mQuoteListHelper.getItemCount(); i++) {
                        if (position != i) {
                            closeSwipe(i);
                        }
                    }
                    openSwipe(position);
                }
                mQuoteListHelper.refresh();
            }
        };
        mLoginCallback = new LoginHelper.LoginCallback() {
            @Override
            public void onCancelRequest() {
                cancelRequest();
            }

            @Override
            public void onExecuteRequest(RequestBuilder builder, IRequestCallback<String> callback) {
                executeRequest(builder, callback);
            }

            @Override
            public void onSuccess() {
                if (mLoginHelper.isShowing()) {
                    start(ReplyActivity.create(getActivity(), mContentId, 0, 0), 0);
                }
                mLoginHelper.hide();
            }
        };
    }

    private Conversion getConversionFromViewTag(View v) {
        Conversion conversion;
        int position = (Integer) v.getTag(R.id.comment_quote_position);

        if (position == -1) {
            conversion = mCommentFloor;
        } else {
            conversion = mQuoteListHelper.getItem(position);
        }

        return conversion;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (AcFunRead.getInstance().readFullUser() == null) {
            mLoginHelper.show();
        } else {
            start(ReplyActivity.create(getActivity(), mContentId, 0, 0), 0);
        }

        return false;
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

    private void openSwipe(int position) {
        mSwipeStatus[position] = true;
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_quote);
    }

    @Override
    public void startAction() {
        setInfoVisibility(InfoView.INFO_LOADING);
        executeRequest(API.getContentComment(mContentId, 50, mPageNo), mCallback);
    }

    private class QuoteListHelper extends AbsListFramework<Conversion, CommentQuoteHolder> {
        public QuoteListHelper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public CommentQuoteHolder createHolder(View convertView) {
            CommentQuoteHolder holder = new CommentQuoteHolder(convertView);

            holder.setTextSize(mTextSize);
            holder.setOnOpenClickListener(mOnOpenClickListener);
            holder.setOnCloseClickListener(mOnCloseClickListener);
            holder.setOnUserClickListener(mOnUserClickListener);
            holder.setOnCopyClickListener(mOnCopyClickListener);
            holder.setOnReplyClickListener(mOnReplyClickListener);

            return holder;
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_comment_quote, null);
        }

        @Override
        public void onAction() {
        }

        @Override
        public void onItemClick(int position, Conversion conversion) {
        }

        @Override
        public List<Conversion> onParse(String json) {
            return null;
        }

        @Override
        public int parseItemId(Conversion conversion) {
            return conversion.getCid();
        }

        @Override
        public void setItem(int position, CommentQuoteHolder holder, Conversion conversion) {
            super.setItem(position, holder, conversion);
            if (mSwipeStatus[position]) {
                holder.openSwipe();
            } else {
                holder.closeSwipe();
            }
        }
    }

    private class QuoteParseTask extends AsyncTask<String, Void, List<FullConversion>> {
        @Override
        protected List<FullConversion> doInBackground(String... params) {
            CommentListParser parser = CommentListParser.parse(params[0], mMaxQuoteCount, mCommentId, mOnTagClickListener);

            if (parser != null) {
                return parser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            mQuoteParseTask = null;
        }

        @Override
        protected void onPostExecute(List<FullConversion> result) {
            FullConversion fullConversion;

            mQuoteParseTask = null;
            if (result != null) {
                setInfoVisibility(InfoView.INFO_HIDE);
                fullConversion = result.get(0);
                mCommentFloor = fullConversion.getConversion();
                mCommentFloorHolder.setItem(-1, mCommentFloor);
                mSwipeStatus = new boolean[fullConversion.getQuoteList().size()];
                mQuoteListHelper.from(fullConversion.getQuoteList());
            } else {
                setInfoVisibility(InfoView.INFO_ERROR);
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }
}
