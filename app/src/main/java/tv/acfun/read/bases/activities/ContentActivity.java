package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.InfoView;

import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.ContentFragment;
import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.parsers.ContentListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentActivity extends ActivityFramework {
    private ViewPager content_pager;
    private PagerTabStrip content_pager_indicator;
    private Content mContent;
    private IRequestCallback<String> mContentCallback;
    private int mContentId;
    private ContentParseTask mContentParseTask;
    private IRequestCallback<String> mFavouriteAddCallback;
    private IRequestCallback<String> mFavouriteCheckCallback;
    private IRequestCallback<String> mFavouriteRemoveCallback;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private List<ArticlePage> mPageList;

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
        setActionBarTitle("ac" + mContentId);
        addActionBarImageItem(0, R.drawable.image_favourite_add);
        addActionBarImageItem(1, R.drawable.image_favourite_remove);
        addActionBarImageItem(2, R.drawable.image_share);

        content_pager = (ViewPager) findViewById(R.id.content_pager);
        content_pager_indicator = (PagerTabStrip) findViewById(R.id.content_pager_indicator);
        content_pager_indicator.setTabIndicatorColorResource(R.color.Theme);
        content_pager_indicator.setTextColor(getResources().getColor(R.color.Title));

        mLoginHelper = new LoginHelper(this, mLoginCallback);
    }

    @Override
    public void establishCallbacks() {
        mContentCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                setInfoVisibility(InfoView.INFO_ERROR);
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
            public void onSuccess(String requestUrl, String s) {
                if (s.contains("ok")) {
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
            public void onSuccess(String requestUrl, String s) {
                if (s.contains("ok")) {
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
            public void onCancelRequest() {
                cancelRequest();
            }

            @Override
            public void onExecuteRequest(RequestBuilder builder, IRequestCallback<String> callback) {
                executeRequest(builder, callback);
            }

            @Override
            public void onSuccess() {
                mLoginHelper.hide();
            }
        };
    }

    @Override
    public void onActionBarItemClick(int id, View item) {
        switch (id) {
            case 0:
                doFavouriteAdd();
                break;
            case 1:
                doFavouriteRemove();
                break;
            case 2:
                doShare();
        }
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
    public void setLayout() {
        setContentView(R.layout.activity_content);
    }

    private void showFavouriteAdd() {
        showActionBarItem(0);
        hideActionbarItem(1);
    }

    private void showFavouriteRemove() {
        hideActionbarItem(0);
        showActionBarItem(1);
    }

    @Override
    public void startAction() {
        setInfoVisibility(InfoView.INFO_LOADING);
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
            setInfoVisibility(InfoView.INFO_ERROR);
        }

        @Override
        protected void onPostExecute(ContentListParser result) {
            mContentParseTask = null;
            if (result != null) {
                mContent = result.getContent();
                if (AcFunRead.isArticle(mContent.getChannelId())) {
                    mPageList = result.getPageList();
                    if (mPageList.size() == 0) {
                        setInfoVisibility(InfoView.INFO_ERROR);
                    } else {
                        setInfoVisibility(InfoView.INFO_HIDE);
                        if (mPageList.size() == 1) {
                            content_pager_indicator.setVisibility(View.GONE);
                        } else {
                            content_pager_indicator.setVisibility(View.VISIBLE);
                        }
                        content_pager.setAdapter(new Adapter(getSupportFragmentManager()));
                        AcFunRead.getInstance().addHistory(mContent);
                        if (mLoginHelper.isLogin()) {
                            doFavouriteCheck();
                        }
                    }
                } else {
                    setInfoVisibility(InfoView.INFO_ERROR);
                    showToast("该投稿为视频，请使用视频客户端浏览！");
                }
            } else {
                setInfoVisibility(InfoView.INFO_ERROR);
            }
        }
    }
}