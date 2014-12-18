package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.lists.recyclerview.RecyclerFramework;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

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
    private FavouriteRecyclerHelper mFavouriteRecyclerHelper;
    private LoginHelper.LoginCallback mLoginCallback;
    private LoginHelper mLoginHelper;
    private View.OnClickListener mOnRemoveClickListener;
    private SwipeLayout mOpenSwipeLayout = null;
    private IRequestCallback<String> mRemoveCallback;
    private int mRemovingHashCode = -1;
    private SwipeLayout.SwipeListener mSwipeListener;

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
        setToolbarNavigation();
        addToolbarItem(0, R.string.app_clear, R.drawable.image_clear_inverse);
    }

    @Override
    public void enquiryViews() {
        mFavouriteRecyclerHelper = new FavouriteRecyclerHelper(this);
        mFavouriteRecyclerHelper.setHasFixedSize(true);
        mFavouriteRecyclerHelper.attachAdapter();

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
        mOnRemoveClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Content content;
                int hashCode = (Integer) v.getTag();
                int position = mFavouriteRecyclerHelper.findItem(hashCode);

                if (mOpenSwipeLayout != null) {
                    mOpenSwipeLayout.close();
                    mOpenSwipeLayout = null;
                }
                showToast(R.string.favourite_operating, true);
                position = (Integer) v.getTag();
                content = mFavouriteRecyclerHelper.getItem(position);
                mRemovingHashCode = hashCode;
                executeRequest(API.getFavouriteRemove(AcFunRead.getInstance().readToken(), content.getContentId()),
                        mRemoveCallback);
            }
        };
        mRemoveCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mRemovingHashCode = -1;
                if (mOpenSwipeLayout != null) {
                    mOpenSwipeLayout.close();
                    mOpenSwipeLayout = null;
                }
                showToast(R.string.favourite_remove_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                int position;

                if (s.contains("ok")) {
                    if (mOpenSwipeLayout != null) {
                        mOpenSwipeLayout.close();
                        mOpenSwipeLayout = null;
                    }
                    position = mFavouriteRecyclerHelper.findItem(mRemovingHashCode);
                    mFavouriteRecyclerHelper.removeItem(position);
                    showToast(R.string.favourite_remove_success);
                } else {
                    showToast(R.string.favourite_remove_failure);
                }
                mRemovingHashCode = -1;
            }
        };
        mSwipeListener = new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout swipeLayout) {
            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v2) {

            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                mOpenSwipeLayout = swipeLayout;
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {

            }

            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                if (mOpenSwipeLayout != null && mOpenSwipeLayout != swipeLayout) {
                    mOpenSwipeLayout.close();
                }
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {

            }
        };
    }

    @Override
    public void onBackPressed() {
        exit(R.anim.zoom_in_enter, R.anim.slide_out_left);
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

        mFavouriteRecyclerHelper.clear();
        readList();
    }

    private void readList() {
        if (mLoginHelper.validateLogin()) {
            mFavouriteRecyclerHelper.from(API
                    .getFavourite(mLoginHelper.getToken(), "110,73,74,75", 20, mFavouriteRecyclerHelper.getCurrentPage()));
        }
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_favourite);
    }

    @Override
    public void startAction() {
    }

    private class FavouriteRecyclerHelper extends RecyclerFramework<Content> {
        public FavouriteRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        public RecyclerHolder<Content> createHolder(View convertView, int viewType) {
            FavouriteHolder holder = new FavouriteHolder(convertView);

            holder.setOnRemoveClickListener(mOnRemoveClickListener);
            holder.addSwipeListener(mSwipeListener);

            return holder;
        }

        @Override
        public View createView(ViewGroup parent, int viewType) {
            return LayoutInflater.from(getActivity()).inflate(R.layout.item_favourite, parent, false);
        }

        @Override
        public void onRequestAction() {
            readList();
        }

        @Override
        public void onItemClick(int position, Content content) {
            if (mOpenSwipeLayout != null) {
                mOpenSwipeLayout.close();
                mOpenSwipeLayout = null;
            } else {
                start(ContentActivity.create(getActivity(), content.getContentId()));
            }
        }

        @Override
        public List<Content> onParse(String json) {
            ChannelListParser listParser = ChannelListParser.parse(json);

            if (listParser != null) {
                return listParser.getItemList();
            } else {
                return null;
            }
        }
    }
}