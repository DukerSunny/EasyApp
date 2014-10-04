package tv.acfun.read.bases.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.frameworks.list.abslistview.AbsListFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.widgets.InfoView;

import java.util.ArrayList;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.beans.ConvertedComment;
import tv.acfun.read.holders.CommentHolder;
import tv.acfun.read.listeners.OnTotalPageChangedListener;
import tv.acfun.read.parsers.CommentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentFragment extends FragmentFramework implements IRequestCallback<String> {
    private int mContentId;
    private Helper mHelper;
    private int mPageNo;
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
    public void onFailure() {
        setInfoVisibility(InfoView.INFO_ERROR);
    }

    @Override
    public void onSuccess(String result) {
        task = new Task();
        task.execute(result);
    }

    @Override
    public void queryLayout() {
        mHelper = new Helper(this, R.id.comment_list);
        FrameLayout blank = new FrameLayout(getActivity());
        blank.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, (int) (32 * getResources().getDisplayMetrics().density)));
        mHelper.addHeaderView(blank);
        mHelper.bindAdapter();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_comment);
    }

    @Override
    public void startAction() {
        executeRequest(API.getContentComment(mContentId, 50, mPageNo), this);
    }

    private class Helper extends AbsListFramework<ConvertedComment, CommentHolder> {
        public Helper(IFramework framework, int listId) {
            super(framework, listId);
        }

        @Override
        public CommentHolder createHolder(View convertView) {
            return new CommentHolder(convertView);
        }

        @Override
        public View createView(ConvertedComment convertedComment) {
            return View.inflate(getActivity(), R.layout.item_comment, null);
        }

        @Override
        public void onAction() {
        }

        @Override
        public void onItemClick(int position, ConvertedComment convertedComment) {
        }

        @Override
        public ArrayList<ConvertedComment> onParse(String json) {
            return null;
        }

        @Override
        public int parseItemId(ConvertedComment convertedComment) {
            return convertedComment.getCommentId();
        }
    }

    private class Task extends AsyncTask<String, Void, ArrayList<ConvertedComment>> {
        @Override
        protected ArrayList<ConvertedComment> doInBackground(String... params) {
            CommentListParser parser = CommentListParser.parse(getActivity(), params[0]);

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
        protected void onPostExecute(ArrayList<ConvertedComment> result) {
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
