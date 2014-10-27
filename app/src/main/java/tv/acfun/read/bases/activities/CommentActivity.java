package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.tools.GsonUtil;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.CommentFragment;
import tv.acfun.read.beans.Content;
import tv.acfun.read.beans.User;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.listeners.OnTotalPageChangedListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentActivity extends ActivityFramework implements OnTotalPageChangedListener {
    private View comment_back;
    private ViewPager comment_pager;
    private PagerTabStrip comment_pager_indicator;
    private View comment_refresh;
    private View comment_reply;
    private Adapter mAdapter;
    private View.OnClickListener mClickListener;
    private int mContentId;
    private Handler mHandler;
    private LoginHelper mLoginHelper;
    private LoginHelper.OnLoginListener mLoginListener;
    private int mTotalPage;
    private User mUser;

    public static Intent create(Context context, Content content) {
        Intent intent = new Intent(context, CommentActivity.class);

        intent.putExtra("contentId", content.getContentId());
        intent.putExtra("user", GsonUtil.toString(content.getUser()));

        return intent;
    }

    @Override
    public void assignEvents() {
        comment_back.setOnClickListener(mClickListener);
        comment_refresh.setOnClickListener(mClickListener);
        comment_reply.setOnClickListener(mClickListener);

        mLoginHelper.setOnLoginListener(mLoginListener);
    }

    private void checkTotalPage() {
        if (mTotalPage > 1) {
            comment_pager_indicator.setVisibility(View.VISIBLE);
        } else {
            comment_pager_indicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void initData(Intent intent) {
        mContentId = intent.getIntExtra("contentId", 0);
        mUser = GsonUtil.toBean(intent.getStringExtra("user"), User.class);

        mTotalPage = 1;
    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.comment_back:
                        onBackPressed();
                        break;
                    case R.id.comment_refresh:
                        refreshComments();
                        break;
                    case R.id.comment_reply:
                        if (AcFunRead.getInstance().readFullUser() == null) {
                            mLoginHelper.show();
                        } else {
                            start(ReplyActivity.create(getActivity(), mContentId, 0, 0), 0);
                        }
                }
            }
        };
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mAdapter.refresh();
                checkTotalPage();

                return false;
            }
        });
        mLoginListener = new LoginHelper.OnLoginListener() {
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

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onActionBarMenuCreate() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Log.e(null, "result success");
            showToast(R.string.comment_success);
            refreshComments();
        }
    }

    @Override
    protected void onDestroy() {
        mLoginHelper.hide();
        super.onDestroy();
    }

    @Override
    public void onTotalPageChanged(int totalPage) {
        Log.e(null, "totalPage changed from " + mTotalPage + " to " + totalPage);
        if (mTotalPage < totalPage) {
            mTotalPage = totalPage;
            mHandler.sendEmptyMessage(0);
        }
    }

    @Override
    public void queryLayout() {
        TextView comment_id = (TextView) findViewById(R.id.comment_id);

        comment_back = findViewById(R.id.comment_back);
        comment_refresh = findViewById(R.id.comment_refresh);
        comment_reply = findViewById(R.id.comment_reply);

        comment_pager = (ViewPager) findViewById(R.id.comment_pager);
        comment_pager_indicator = (PagerTabStrip) findViewById(R.id.comment_pager_indicator);

        comment_pager_indicator.setTabIndicatorColorResource(R.color.Theme);
        comment_pager_indicator.setTextColor(getResources().getColor(R.color.Title));

        comment_id.setText("ac" + mContentId);

        mLoginHelper = new LoginHelper(getActivity());
        mLoginHelper.setOnLoginListener(mLoginListener);

        mAdapter = new Adapter(getSupportFragmentManager());
        checkTotalPage();
    }

    private void refreshComments() {
        mAdapter.clear();
        mAdapter.refresh();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_comment);
    }

    @Override
    public void startAction() {
        comment_pager.setAdapter(mAdapter);
    }

    private class Adapter extends FragmentPageAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTotalPage;
        }

        @Override
        public Fragment getItem(int position) {
            return CommentFragment.create(mContentId, position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(R.string.comment_pager_title, position + 1);
        }
    }
}
