package tv.acfun.read.bases.fragments;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.listeners.OnTagClickListener;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.widgets.InfoView;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.activities.ComicActivity;
import tv.acfun.read.bases.activities.ContentActivity;
import tv.acfun.read.bases.activities.ProfileActivity;
import tv.acfun.read.bases.activities.QuoteActivity;
import tv.acfun.read.bases.activities.ReplyActivity;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.beans.Setting;
import tv.acfun.read.holders.FullConversionHolder;
import tv.acfun.read.listeners.OnCommentListener;
import tv.acfun.read.parsers.CommentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentFragment extends FragmentFramework {
    private View comment_reply;
    private IRequestCallback<String> mCallback;
    private CommentListHelper mCommentListHelper;
    private OnCommentListener mCommentListener;
    private CommentParseTask mCommentParseTask = null;
    private int mContentId;
    private int mMaxQuoteCount;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnCopyClickListener;
    private View.OnClickListener mOnQuoteExpandClickListener;
    private View.OnClickListener mOnReplyClickListener;
    private OnTagClickListener mOnTagClickListener;
    private View.OnClickListener mOnUserClickListener;
    private int mPageNo;
    private int mTextSize;

    public static CommentFragment create(int contentId, int pageNo) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("contentId", contentId);
        bundle.putInt("pageNo", pageNo);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void acquireArguments(Bundle bundle) {
        Setting setting = AcFunRead.getInstance().readSetting();

        mContentId = bundle.getInt("contentId");
        mPageNo = bundle.getInt("pageNo");

        mMaxQuoteCount = setting.getMaxQuoteCount();
        mTextSize = setting.getDefaultTextSize();
    }

    @Override
    public void attachCallbacks() {
        comment_reply.setOnClickListener(mOnClickListener);
    }

    @Override
    public void enquiryViews() {
        comment_reply = findViewById(R.id.comment_reply);

        mCommentListHelper = new CommentListHelper(this, R.id.comment_list);
        mCommentListHelper.setRefresh(comment_reply);
        mCommentListHelper.bindAdapter();
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentListener != null) {
                    mCommentListener.showLogin();
                }
            }
        };
        mCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                setInfoVisibility(InfoView.INFO_ERROR);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                mCommentParseTask = new CommentParseTask();
                mCommentParseTask.execute(result);
            }
        };
        mOnQuoteExpandClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullConversion fullConversion = mCommentListHelper.getItem((Integer) v.getTag());

                start(QuoteActivity.create(getActivity(), mContentId, mCommentListHelper.getCurrentPage(),
                        fullConversion.getConversion().getCid()));
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
                FragmentActivity activity = getActivity();

                start(ProfileActivity.create(activity, (Integer) v.getTag()));
                activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
    }

    private Conversion getConversionFromViewTag(View v) {
        int floorPosition = (Integer) v.getTag(R.id.comment_floor_position);
        int quotePosition = (Integer) v.getTag(R.id.comment_quote_position);
        FullConversion fullConversion = mCommentListHelper.getItem(floorPosition);

        if (quotePosition == -1) {
            return fullConversion.getConversion();
        } else {
            return fullConversion.getQuoteList().get(quotePosition);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCommentListener = (OnCommentListener) getActivity();
    }

    @Override
    public void onDestroyView() {
        if (mCommentParseTask != null) {
            mCommentParseTask.cancel(true);
            mCommentParseTask = null;
        }
        super.onDestroyView();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_comment);
    }

    @Override
    public void startAction() {
        setInfoVisibility(InfoView.INFO_LOADING);
        executeRequest(API.getContentComment(mContentId, 50, mPageNo), mCallback);
    }

    private class CommentListHelper extends AbsListFramework<FullConversion, FullConversionHolder> {
        public CommentListHelper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public FullConversionHolder createHolder(View convertView) {
            return new FullConversionHolder(convertView, mTextSize, mMaxQuoteCount, mOnUserClickListener, mOnCopyClickListener,
                    mOnReplyClickListener, mOnQuoteExpandClickListener);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_comment, null);
        }

        @Override
        public void onAction() {
        }

        @Override
        public void onItemClick(int position, FullConversion fullConversion) {

        }

        @Override
        public ArrayList<FullConversion> onParse(String json) {
            return null;
        }

        @Override
        public int parseItemId(FullConversion fullConversion) {
            return fullConversion.getConversion().getCid();
        }
    }

    private class CommentParseTask extends AsyncTask<String, Void, ArrayList<FullConversion>> {
        @Override
        protected ArrayList<FullConversion> doInBackground(String... params) {
            CommentListParser parser = CommentListParser.parse(params[0], mMaxQuoteCount, 0, mOnTagClickListener);

            if (parser != null) {
                if (mCommentListener != null) {
                    mCommentListener.onTotalPageChanged(parser.getTotalPage());
                }

                return parser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            mCommentParseTask = null;
        }

        @Override
        protected void onPostExecute(ArrayList<FullConversion> result) {
            mCommentParseTask = null;
            if (result != null) {
                setInfoVisibility(InfoView.INFO_HIDE);
                mCommentListHelper.from(result);
            } else {
                setInfoVisibility(InfoView.INFO_ERROR);
            }
        }

        @Override
        protected void onPreExecute() {
        }
    }
}