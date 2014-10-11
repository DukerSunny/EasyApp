package tv.acfun.read.bases.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
import tv.acfun.read.bases.activities.ReplyActivity;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.beans.FullConversion;
import tv.acfun.read.holders.FullConversionHolder;
import tv.acfun.read.listeners.OnTotalPageChangedListener;
import tv.acfun.read.parsers.CommentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentFragment extends FragmentFramework {
    private IRequestCallback<String> mCallback;
    private int mContentId;
    private Helper mHelper;
    private View.OnClickListener mOptionsClickListener;
    private int mPageNo;
    private View.OnClickListener mQuoteExpandClickListener;
    private OnTagClickListener mTagClickListener;
    private OnTotalPageChangedListener mTotalPageChangedListener;
    private Task task = null;

    public static CommentFragment create(int contentId, int pageNo) {
        CommentFragment fragment = new CommentFragment();
        Bundle bundle = new Bundle();

        bundle.putInt("contentId", contentId);
        bundle.putInt("pageNo", pageNo);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void assignEvents() {
    }

    @Override
    public void initData(Bundle bundle) {
        mContentId = bundle.getInt("contentId");
        mPageNo = bundle.getInt("pageNo");
    }

    @Override
    public void newEvents() {
        mCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                setInfoVisibility(InfoView.INFO_ERROR);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                task = new Task();
                task.execute(result);
            }
        };
        mOptionsClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Conversion conversion = mHelper.getItem((Integer) v.getTag()).getContent();

                start(ReplyActivity.create(getActivity(), mContentId, conversion.getId(), conversion.getFloorindex(),
                        conversion.getUser().getUsername()));
            }
        };
        mTagClickListener = new OnTagClickListener() {
            @Override
            public void onTagClick(String tag, String link) {
                if ("img".equals(tag)) {
                    start(ComicActivity.create(getActivity(), link));
                }
            }
        };
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mTotalPageChangedListener = (OnTotalPageChangedListener) getActivity();
    }

    @Override
    public void onDestroyView() {
        if (task != null) {
            task.cancel(true);
            task = null;
        }
        super.onDestroyView();
    }

    @Override
    public void queryLayout() {
        mHelper = new Helper(this, R.id.comment_list);
        mHelper.bindAdapter();
        mHelper.setEnabled(false);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_comment);
    }

    @Override
    public void startAction() {
        executeRequest(API.getContentComment(mContentId, 50, mPageNo), mCallback);
    }

    private class Helper extends AbsListFramework<FullConversion, FullConversionHolder> {
        public Helper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public FullConversionHolder createHolder(View convertView) {
            FullConversionHolder holder = new FullConversionHolder(convertView);

            holder.setOnOptionsClickListener(mOptionsClickListener);
            holder.setOnQuoteExpandClickListener(mQuoteExpandClickListener);

            return holder;
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
            return fullConversion.getContent().getId();
        }
    }

    private class Task extends AsyncTask<String, Void, ArrayList<FullConversion>> {
        @Override
        protected ArrayList<FullConversion> doInBackground(String... params) {
            CommentListParser parser = CommentListParser.parse(getActivity(), params[0], 3, mTagClickListener);

            if (parser != null) {
                if (mTotalPageChangedListener != null) {
                    mTotalPageChangedListener.onTotalPageChanged(parser.getTotalPage());
                }

                return parser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }

        @Override
        protected void onPostExecute(ArrayList<FullConversion> result) {
            task = null;
            if (result != null) {
                setInfoVisibility(InfoView.INFO_HIDE);
                mHelper.from(result);
            } else {
                setInfoVisibility(InfoView.INFO_ERROR);
            }
        }

        @Override
        protected void onPreExecute() {
            setInfoVisibility(InfoView.INFO_LOADING);
        }
    }
}