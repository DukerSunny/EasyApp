package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.StringUtil;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;
import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.regex.Matcher;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.ContentFragment;
import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.listeners.OnContentListener;
import tv.acfun.read.parsers.ContentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentActivity extends ActivityFramework implements OnContentListener {
    private View content_comments;
    private ViewPager content_pager;
    private PagerSlidingTabStrip content_pager_strip;
    private Content mContent;
    private IRequestCallback<String> mContentCallback;
    private ViewPropertyAnimator mContentCommentsAnimator;
    private float mContentCommentsPosition = -1;
    private int mContentId;
    private ContentParseTask mContentParseTask;
    private EmptyHelper mEmptyHelper;
    private IRequestCallback<String> mFavouriteAddCallback;
    private IRequestCallback<String> mFavouriteCheckCallback;
    private IRequestCallback<String> mFavouriteRemoveCallback;
    private String mLink;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnEmptyClickListener;
    private List<ArticlePage> mPageList;
    private ActionClickListener mRedirectActionListener;
    private EventListener mRedirectEventListener;
    private Snackbar mSnackbar = null;

    public static Intent create(Context context, int contentId) {
        Intent intent = new Intent(context, ContentActivity.class);

        intent.putExtra("contentId", contentId);

        return intent;
    }

    @Override
    public void acquireArguments(Intent intent) {
        mContentId = intent.getIntExtra("contentId", 0);
    }

    @Override
    public void attachCallbacks() {
        content_comments.setOnClickListener(mOnClickListener);

        mEmptyHelper.setOnClickListener(mOnEmptyClickListener);

        mLoginHelper.setLoginCallback(mLoginCallback);
    }

    @Override
    public void createMenu() {
        setToolbarTitle("ac" + mContentId);
        setToolbarNavigation();
        addToolbarItem(0, R.string.favourite_add, R.drawable.image_favourite_add_inverse);
        addToolbarItem(1, R.string.favourite_remove, R.drawable.image_favourite_remove_inverse);
        addToolbarItem(2, R.string.share_title, R.drawable.image_share_inverse);

        hideToolbarItem(0);
        hideToolbarItem(1);
    }

    private void doFavouriteAdd() {
        showToast(R.string.favourite_operating, true);
        executeRequest(API.getFavouriteAdd(mLoginHelper.getToken(), mContentId), mFavouriteAddCallback);
    }

    private void doFavouriteCheck() {
        executeRequest(API.getFavouriteCheck(mLoginHelper.getToken(), mContentId), mFavouriteCheckCallback);
    }

    private void doFavouriteRemove() {
        showToast(R.string.favourite_operating, true);
        executeRequest(API.getFavouriteRemove(mLoginHelper.getToken(), mContentId), mFavouriteRemoveCallback);
    }

    private void doShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_title));
        intent.putExtra(Intent.EXTRA_TEXT,
                getString(R.string.share_content, Build.MODEL, mContent.getTitle(), mContent.getUser().getUsername(),
                        API.HOST + "/a/ac" + mContentId));
        startActivity(Intent.createChooser(intent, getTitle()));
    }

    @Override
    public void enquiryViews() {
        content_pager = (ViewPager) findViewById(R.id.content_pager);

        content_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.content_pager_strip);
        content_pager_strip.setTextColor(Color.WHITE);
        content_pager_strip.setTextSize((int) getResources().getDimension(R.dimen.Subhead));

        content_comments = findViewById(R.id.content_comments);

        mContentCommentsAnimator = ViewPropertyAnimator.animate(content_comments);

        mLoginHelper = new LoginHelper(this);
        mEmptyHelper = new EmptyHelper(findViewById(R.id.empty_root));
    }

    @Override
    public void establishCallbacks() {
        mContentCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mEmptyHelper.showEmptyIdle();
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                mContentParseTask = new ContentParseTask();
                mContentParseTask.execute(result);
            }
        };
        mFavouriteAddCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.favourite_add_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Log.e(null, "add result=" + result);
                if (result.contains("ok")) {
                    showToast(R.string.favourite_add_success);
                    showFavouriteRemove();
                } else {
                    showToast(R.string.favourite_add_failure);
                }
            }
        };
        mFavouriteRemoveCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.favourite_remove_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Log.e(null, "remove result=" + result);
                if (result.contains("ok")) {
                    showToast(R.string.favourite_remove_success);
                    showFavouriteAdd();
                } else {
                    showToast(R.string.favourite_remove_failure);
                }
            }
        };
        mFavouriteCheckCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                if (s.contains("true")) {
                    showFavouriteRemove();
                } else {
                    showFavouriteAdd();
                }
            }
        };
        mLoginCallback = new LoginHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                start(CommentActivity.create(getContext(), mContentId), Transition.Enter_Right);
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(CommentActivity.create(getContext(), mContent.getContentId()), Transition.Enter_Right);
            }
        };
        mOnEmptyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRequestExecuting()) {
                    startAction();
                }
            }
        };
        mRedirectActionListener = new ActionClickListener() {
            @Override
            public void onActionClicked(Snackbar snackbar) {
                parseLink();
            }
        };
        mRedirectEventListener = new EventListener() {
            @Override
            public void onDismiss(Snackbar snackbar) {
                mContentCommentsAnimator.y(mContentCommentsPosition);
            }

            @Override
            public void onDismissed(Snackbar snackbar) {
            }

            @Override
            public void onShow(Snackbar snackbar) {
                if (mContentCommentsPosition == -1) {
                    mContentCommentsPosition = ViewHelper.getY(content_comments);
                }
                mContentCommentsAnimator.y(mContentCommentsPosition - mSnackbar.getMeasuredHeight());
            }

            @Override
            public void onShown(Snackbar snackbar) {
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_content;
    }

    @Override
    protected void onDestroy() {
        if (isRequestExecuting()) {
            cancelRequest();
        }
        if (mContentParseTask != null && mContentParseTask.isCancelled()) {
            mContentParseTask.cancel(true);
        }
        super.onDestroy();
    }

    @Override
    public void onLinkClick(String link) {
        mLink = link;
        if (mSnackbar != null && mSnackbar.isShowing()) {
            mSnackbar.dismiss();
        }
        mSnackbar = Snackbar.with(getContext()).text(getString(R.string.content_redirect, link)).actionLabel(R.string.app_ok)
                .actionListener(mRedirectActionListener).eventListener(mRedirectEventListener);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 0:
                doFavouriteAdd();
                break;
            case 1:
                doFavouriteRemove();
                break;
            case 2:
                doShare();
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

    private void parseLink() {
        Matcher matcher;

        matcher = StringUtil.getMatcher("/a/ac([0-9]+)", mLink);
        if (matcher.find()) {
            start(ContentActivity.create(this, Integer.valueOf(matcher.group(1))), Transition.Enter_Right);

            return;
        }
        matcher = StringUtil.getMatcher("/a/aa([0-9]+)", mLink);
        if (matcher.find()) {
            showToast(getString(R.string.content_redirect_aa, matcher.group(1)));

            return;
        }
        matcher = StringUtil.getMatcher("http://[\\S\\s]+?", mLink);
        if (matcher.find()) {
            start(new Intent(Intent.ACTION_VIEW, Uri.parse(mLink)), Transition.Enter_Right);
        }
    }

    private void showFavouriteAdd() {
        showToolbarItem(0);
        hideToolbarItem(1);
    }

    private void showFavouriteRemove() {
        hideToolbarItem(0);
        showToolbarItem(1);
    }

    @Override
    public void startAction() {
        mEmptyHelper.showLoading();
        executeRequest(API.getArticleContent(mContentId), mContentCallback);
    }

    private class Adapter extends FragmentPagerAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mPageList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return ContentFragment.create(mContent, position, mPageList.get(position));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mPageList.get(position).getTitle();
        }
    }

    private class ContentParseTask extends AsyncTask<String, Void, ContentListParser> {
        @Override
        protected ContentListParser doInBackground(String... params) {
            return ContentListParser.parse(params[0]);
        }

        @Override
        protected void onCancelled() {
            mContentParseTask = null;
            mEmptyHelper.showEmptyIdle();
        }

        @Override
        protected void onPostExecute(ContentListParser result) {
            mContentParseTask = null;
            if (result != null) {
                mContent = result.getContent();
                if (AcFunRead.isArticle(mContent.getChannelId())) {
                    mPageList = result.getPageList();
                    if (mPageList.size() == 0) {
                        mEmptyHelper.showEmptyIdle();
                    } else {
                        mEmptyHelper.hide();
                        if (mPageList.size() == 1) {
                            content_pager_strip.setVisibility(View.GONE);
                        } else {
                            content_pager_strip.setVisibility(View.VISIBLE);
                        }
                        content_pager.setAdapter(new Adapter(getSupportFragmentManager()));
                        content_pager_strip.setViewPager(content_pager);
                        AcFunRead.getInstance().addHistory(mContent);
                        if (mLoginHelper.isLogin()) {
                            doFavouriteCheck();
                        }
                    }
                } else {
                    mEmptyHelper.showEmptyIdle();
                    showToast("该投稿为视频，请使用视频客户端浏览！");
                }
            } else {
                mEmptyHelper.showEmptyIdle();
            }
        }
    }
}