package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.widgets.InfoView;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.holders.CommentFloorHolder;
import tv.acfun.read.holders.CommentQuoteHolder;
import tv.acfun.read.parsers.CommentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/14
 */
public class QuoteActivity extends ActivityFramework {
    private IRequestCallback<String> mCallback;
    private CommentFloorHolder mCommentFloorHolder;
    private int mCommentId;
    private int mContentId;
    private int mMaxQuoteCount;
    private View.OnClickListener mOnQuoteClickListener;
    private int mPageNo;
    private QuoteListHelper mQuoteListHelper;
    private QuoteParseTask mQuoteParseTask = null;
    private OnTagClickListener mTagClickListener;

    public static Intent create(Context context, int contentId, int pageNo, int commentId) {
        Intent intent = new Intent(context, QuoteActivity.class);

        intent.putExtra("contentId", contentId);
        intent.putExtra("pageNo", pageNo);
        intent.putExtra("commentId", commentId);

        return intent;
    }

    @Override
    public void acquireArguments(Intent intent) {
        mContentId = intent.getIntExtra("contentId", 0);
        mPageNo = intent.getIntExtra("pageNo", 0);
        mCommentId = intent.getIntExtra("commentId", 0);

        mMaxQuoteCount = AcFunRead.getInstance().readSetting().getMaxQuoteCount();
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        View commentFloor = View.inflate(this, R.layout.item_comment_floor, null);

        mCommentFloorHolder = new CommentFloorHolder(commentFloor);

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
        mTagClickListener = new OnTagClickListener() {
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
    }

    @Override
    public void onActionBarItemClick(int id, View item) {

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
            return new CommentQuoteHolder(convertView);
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
    }

    private class QuoteParseTask extends AsyncTask<String, Void, List<FullConversion>> {
        @Override
        protected List<FullConversion> doInBackground(String... params) {
            CommentListParser parser = CommentListParser.parse(params[0], mMaxQuoteCount, mCommentId, mTagClickListener);

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
                mCommentFloorHolder.setItem(0, fullConversion.getConversion());
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
