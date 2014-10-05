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
import android.widget.TextView;

import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.tools.GsonUtil;

import tv.acfun.read.R;
import tv.acfun.read.bases.fragments.CommentFragment;
import tv.acfun.read.beans.Content;
import tv.acfun.read.listeners.OnTotalPageChangedListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentActivity extends ActivityFramework implements OnTotalPageChangedListener {
    private Adapter adapter;
    private View comment_back;
    private TextView comment_id;
    private PagerTabStrip comment_pager_indicator;
    private View.OnClickListener mClickListener;
    private Content mContent;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            adapter.refresh();
            checkTotalPage();

            return false;
        }
    });
    private int mTotalPage;

    public static Intent create(Context context, Content comment) {
        Intent intent = new Intent(context, CommentActivity.class);

        intent.putExtra("comment", GsonUtil.toString(comment));

        return intent;
    }

    @Override
    public void assignEvents() {
        comment_back.setOnClickListener(mClickListener);
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
        mContent = GsonUtil.toBean(intent.getStringExtra("comment"), Content.class);
        mTotalPage = (mContent.getComments() - 1) / 50 + 1;
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
                }
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
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.slide_out_up);
    }

    @Override
    public void onTotalPageChanged(int totalPage) {
        mTotalPage = totalPage;
    }

    @Override
    public void queryLayout() {
        ViewPager comment_pager = (ViewPager) findContentView(R.id.comment_pager);
        TextView comment_id = (TextView) findContentView(R.id.comment_id);

        comment_back = findContentView(R.id.comment_back);

        comment_pager_indicator = (PagerTabStrip) findContentView(R.id.comment_pager_indicator);

        comment_pager_indicator.setTabIndicatorColorResource(R.color.Theme);
        comment_pager_indicator.setTextColor(getResources().getColor(R.color.Title));

        comment_id.setText("ac" + mContent.getContentId());

        adapter = new Adapter(getSupportFragmentManager());
        comment_pager.setAdapter(adapter);
        checkTotalPage();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_comment);
    }

    @Override
    public void startAction() {
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
            return CommentFragment.create(mContent.getContentId(), position + 1);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getString(R.string.comment_pager_title, position + 1);
        }
    }
}
