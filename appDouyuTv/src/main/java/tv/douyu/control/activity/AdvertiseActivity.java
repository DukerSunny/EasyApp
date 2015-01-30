package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.IFramework;
import com.harreke.easyapp.frameworks.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.Advertise;
import tv.douyu.wrapper.holder.AdvertiseHolder;
import tv.douyu.model.parser.AdvertiseListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class AdvertiseActivity extends ActivityFramework {
    private Advertise mAdvertise;
    private AdvertiseRecyclerHelper mAdvertiseRecyclerHelper;
    private View.OnClickListener mOnDownloadClickListener;
    private int mPosition = 0;
    private IRequestCallback<String> mSendCallback;

    public static Intent create(Context context) {
        return new Intent(context, AdvertiseActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_advertise);
        enableDefaultToolbarNavigation();
    }

    private void doSend(Advertise advertise) {
        showToast(R.string.advertise_sending, true);
        executeRequest(API.getAdvertiseAdd(advertise.getId(), advertise.getAndroid_id()), mSendCallback);
    }

    @Override
    public void enquiryViews() {
        mAdvertiseRecyclerHelper = new AdvertiseRecyclerHelper(this);
        mAdvertiseRecyclerHelper.setHasFixedSize(true);
        mAdvertiseRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mAdvertiseRecyclerHelper.setItemDecoration();
        mAdvertiseRecyclerHelper.setListParser(new AdvertiseListParser());
        mAdvertiseRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {
        mOnDownloadClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPosition = ViewUtil.getIntTag(v);
                mAdvertise = mAdvertiseRecyclerHelper.getItem(mPosition);
                doSend(mAdvertise);
            }
        };
        mSendCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                hideToast();
                showToast(R.string.advertise_send_failure);
                mAdvertise = null;
                mPosition = 0;
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                hideToast();
                mAdvertise.setDownloaded(true);
                mAdvertiseRecyclerHelper.replaceItem(mPosition, mAdvertise);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mAdvertise.getDown_an_url())));
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_advertise;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(this));

        return false;
    }

    @Override
    public void startAction() {
        mAdvertiseRecyclerHelper.from(API.getAdvertise());
    }

    private class AdvertiseRecyclerHelper extends RecyclerFramework<Advertise> {
        public AdvertiseRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Advertise> createHolder(View itemView, int viewType) {
            AdvertiseHolder holder = new AdvertiseHolder(itemView);

            holder.setOnDownloadListener(mOnDownloadClickListener);

            return holder;
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_room, parent, false);
        }

        @Override
        public void onItemClick(int position, Advertise room) {
        }
    }
}