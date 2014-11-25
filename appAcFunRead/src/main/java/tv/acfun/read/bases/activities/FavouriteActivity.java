package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.list.abslistview.FooterLoadStatus;
import com.harreke.easyapp.frameworks.list.swipelayout.AbsListSwipeFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.holders.FavouriteHolder;
import tv.acfun.read.parsers.ChannelListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/23
 */
public class FavouriteActivity extends ActivityFramework {
    private Helper mFavouriteListHelper;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private SwipeLayout mOpenSwipeLayout = null;
    private IRequestCallback<String> mRemoveCallback;
    private View.OnClickListener mRemoveClickListener;
    private int mRemovingPosition = -1;

    public static Intent create(Context context) {
        return new Intent(context, FavouriteActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.menu_favourite);
        setToolbarNavigation(R.drawable.image_back_inverse);
    }

    @Override
    public void enquiryViews() {
        View footer_loadmore = View.inflate(getActivity(), R.layout.footer_loadmore, null);

        mFavouriteListHelper = new Helper(this, R.id.favourite_list, R.id.favourite_swipe);
        mFavouriteListHelper.setRefresh(findViewById(R.id.favourite_refresh));
        mFavouriteListHelper.addFooterView(footer_loadmore);
        mFavouriteListHelper.setLoadMore(new FooterLoadStatus(footer_loadmore));
        mFavouriteListHelper.bindAdapter();

        mLoginHelper = new LoginHelper(getActivity(), mLoginCallback);
    }

    @Override
    public void establishCallbacks() {
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
                readList();
            }
        };
        mRemoveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position;
                Content content;

                if (mRemovingPosition == -1) {
                    showToast(R.string.favourite_operating, true);
                    position = (Integer) v.getTag();
                    content = mFavouriteListHelper.getItem(position);
                    mRemovingPosition = position;
                    executeRequest(API.getFavouriteRemove(AcFunRead.getInstance().readToken(), content.getContentId()),
                            mRemoveCallback);
                }
            }
        };
        mRemoveCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mRemovingPosition = -1;
                if (mOpenSwipeLayout != null) {
                    mOpenSwipeLayout.close();
                    mOpenSwipeLayout = null;
                }
                showToast(R.string.favourite_remove_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                if (s.contains("ok")) {
                    showToast(R.string.favourite_remove_success);
                    if (mOpenSwipeLayout != null) {
                        mOpenSwipeLayout.close();
                        mOpenSwipeLayout = null;
                    }
                    mFavouriteListHelper.removeItem(mRemovingPosition);
                    mFavouriteListHelper.refresh();
                } else {
                    showToast(R.string.favourite_remove_failure);
                }
                mRemovingPosition = -1;
            }
        };
    }

    @Override
    public void onBackPressed() {
        exit(false);
    }

    @Override
    protected void onDestroy() {
        mLoginHelper.hide();
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

        mFavouriteListHelper.clear();
        readList();
    }

    private void readList() {
        if (mLoginHelper.validateLogin()) {
            mFavouriteListHelper
                    .from(API.getFavourite(mLoginHelper.getToken(), "110,73,74,75", 20, mFavouriteListHelper.getCurrentPage()));
        }
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_favourite);
    }

    @Override
    public void startAction() {
    }

    private class Helper extends AbsListSwipeFramework<Content, FavouriteHolder> {
        public Helper(IFramework framework, int listId, int swipeLayoutId) {
            super(framework, listId, swipeLayoutId);
        }

        @Override
        public FavouriteHolder createHolder(View convertView) {
            return new FavouriteHolder(convertView, mRemoveClickListener);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_favourite, null);
        }

        @Override
        public void onAction() {
            readList();
        }

        @Override
        public void onClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onHandRelease(SwipeLayout swipeLayout, float v, float v2) {
        }

        @Override
        public void onItemClick(int position, Content content) {
            if (mOpenSwipeLayout != null) {
                mOpenSwipeLayout.close();
                mOpenSwipeLayout = null;
            } else {
                if (content != null) {
                    start(ContentActivity.create(getActivity(), content.getContentId()));
                } else {
                    start(SearchActivity.create(getActivity()));
                }
            }
        }

        @Override
        public void onOpen(SwipeLayout swipeLayout) {
            mOpenSwipeLayout = swipeLayout;
        }

        @Override
        public ArrayList<Content> onParse(String json) {
            ChannelListParser listParser = ChannelListParser.parse(json);

            if (listParser != null) {
                setTotalPage(listParser.getTotalPage());

                return listParser.getItemList();
            } else {
                return null;
            }
        }

        @Override
        public void onStartClose(SwipeLayout swipeLayout) {
        }

        @Override
        public void onStartOpen(SwipeLayout swipeLayout) {
        }

        @Override
        public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {
        }

        @Override
        public int parseItemId(Content content) {
            return content.getContentId();
        }
    }
}