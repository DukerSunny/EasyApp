package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.umeng.analytics.MobclickAgent;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.CommentFragment;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.listeners.OnCommentListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentActivity extends ActivityFramework implements OnCommentListener {
    private ViewPager comment_pager;
    private PagerTabStrip comment_pager_indicator;
    private Adapter mAdapter;
    private int mContentId;
    private Handler mHandler;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private int mTotalPage;

    public static Intent create(Context context, int contentId) {
        Intent intent = new Intent(context, CommentActivity.class);

        intent.putExtra("contentId", contentId);

        return intent;
    }

    @Override
    public void acquireArguments(Intent intent) {
        mContentId = intent.getIntExtra("contentId", 0);

        mTotalPage = 1;
    }

    @Override
    public void attachCallbacks() {
    }

    private void checkTotalPage() {
        if (mTotalPage > 1) {
            comment_pager_indicator.setVisibility(View.VISIBLE);
        } else {
            comment_pager_indicator.setVisibility(View.GONE);
        }
    }

    @Override
    public void enquiryViews() {
        setActionBarTitle("ac" + mContentId);
        addActionBarImageItem(0, R.drawable.image_refresh);

        comment_pager = (ViewPager) findViewById(R.id.comment_pager);
        comment_pager_indicator = (PagerTabStrip) findViewById(R.id.comment_pager_indicator);

        comment_pager_indicator.setTabIndicatorColorResource(R.color.Theme);
        comment_pager_indicator.setTextColor(getResources().getColor(R.color.Title));

        mLoginHelper = new LoginHelper(getActivity(), mLoginCallback);

        mAdapter = new Adapter(getSupportFragmentManager());
        checkTotalPage();
    }

    @Override
    public void establishCallbacks() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mAdapter.refresh();
                checkTotalPage();

                return false;
            }
        });
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

    @Override
    public void onActionBarItemClick(int id, View item) {
        refreshComment();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            showToast(R.string.comment_success);
            refreshComment();
        }
    }

    @Override
    protected void onDestroy() {
        mLoginHelper.hide();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onTotalPageChanged(int totalPage) {
        if (mTotalPage < totalPage) {
            mTotalPage = totalPage;
            mHandler.sendEmptyMessage(0);
        }
    }

    public void refreshComment() {
        mAdapter.clear();
        mAdapter.refresh();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_comment);
    }

    @Override
    public void showLogin() {
        if (AcFunRead.getInstance().readFullUser() == null) {
            mLoginHelper.show();
        } else {
            start(ReplyActivity.create(getActivity(), mContentId, 0, 0), 0);
        }
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
