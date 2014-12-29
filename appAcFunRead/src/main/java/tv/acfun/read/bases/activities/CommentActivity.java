package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.umeng.analytics.MobclickAgent;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.bases.fragments.CommentFragment;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.listeners.OnCommentListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentActivity extends ActivityFramework implements OnCommentListener {
    private View comment_new;
    private ViewPager comment_pager;
    private PagerSlidingTabStrip comment_pager_strip;
    private ActionClickListener mActionClickListener;
    private Adapter mAdapter;
    private ViewPropertyAnimator mCommentNewAnimator;
    private float mCommentNewPosition = -1;
    private int mContentId;
    private EventListener mEventListener;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private View.OnClickListener mOnClickListener;
    private Conversion mSelectedConversion = null;
    private Snackbar mSnackbar;
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
        comment_new.setOnClickListener(mOnClickListener);
        mLoginHelper.setLoginCallback(mLoginCallback);
    }

    private void checkTotalPage() {
        if (mTotalPage > 1) {
            comment_pager_strip.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void createMenu() {
        setToolbarTitle("ac" + mContentId);
        setToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        comment_pager = (ViewPager) findViewById(R.id.comment_pager);
        comment_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.comment_pager_strip);
        comment_pager_strip.setTextColor(Color.WHITE);
        comment_pager_strip.setTextSize((int) getResources().getDimension(R.dimen.Subhead));

        comment_new = findViewById(R.id.comment_new);

        mCommentNewAnimator = ViewPropertyAnimator.animate(comment_new);

        mLoginHelper = new LoginHelper(this);

        mAdapter = new Adapter(getSupportFragmentManager());
        checkTotalPage();
    }

    @Override
    public void establishCallbacks() {
        mLoginCallback = new LoginHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                start(ReplyActivity.create(getContext(), mContentId, 0, 0), Transition.Enter_Right);
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLoginHelper.validateLogin()) {
                    start(ReplyActivity.create(getContext(), mContentId, 0, 0), Transition.Enter_Right);
                }
            }
        };
        mActionClickListener = new ActionClickListener() {
            @Override
            public void onActionClicked(Snackbar snackbar) {
                if (mLoginHelper.validateLogin()) {
                    start(ReplyActivity
                            .create(getContext(), mContentId, mSelectedConversion.getCid(), mSelectedConversion.getCount()),
                            Transition.Enter_Right);
                }
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
                mCommentNewAnimator.y(mCommentNewPosition - mSnackbar.getMeasuredHeight());
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_comment;
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
    public void onCommentClick(Conversion conversion) {
        if (mSnackbar != null && mSnackbar.isShowing()) {
            mSnackbar.dismiss();
        }
        mSelectedConversion = conversion;
        mSnackbar = Snackbar.with(this).actionLabel(R.string.comment_reply).dismissOnActionClicked(true);
        mSnackbar.text(getString(R.string.comment_select, conversion.getCount()));
        mSnackbar.actionListener(mActionClickListener).eventListener(mEventListener);
        mSnackbar.show(this);
    }

    @Override
    protected void onDestroy() {
        mLoginHelper.destroy();
        super.onDestroy();
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
    public void onTotalPageChanged(int totalPage) {
        if (mTotalPage < totalPage) {
            mTotalPage = totalPage;
            checkTotalPage();
            mAdapter.refresh();
            comment_pager_strip.setViewPager(comment_pager);
        }
    }

    public void refreshComment() {
        mAdapter.clear();
        mAdapter.refresh();
    }

    @Override
    public void startAction() {
        comment_pager.setAdapter(mAdapter);
        comment_pager_strip.setViewPager(comment_pager);
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
