package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.PopupAbsListHelper;
import com.harreke.easyapp.widgets.ChildTabView;
import com.harreke.easyapp.widgets.GroupTabView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.ChannelFragment;
import tv.acfun.read.bases.fragments.RecommendFragment;
import tv.acfun.read.holders.ChannelSelectHolder;
import tv.acfun.read.widgets.RepeatCheckableChildTabView;

public class MainActivity extends ActivityFramework {
    private RepeatCheckableChildTabView content_channel;
    private GroupTabView content_group;
    private ViewPager content_pager;
    private View content_setting;
    private View content_user_avatar;
    private ImageView content_user_avatar_image;
    private Adapter mAdapter;
    private GroupTabView.OnCheckedChangeListener mCheckedChangeListener;
    private View.OnClickListener mClickListener;
    private Helper mHelper;
    private AdapterView.OnItemClickListener mItemClickListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private int mPosition = 0;
    private RepeatCheckableChildTabView.OnRepeatCheckedListener mRepeatCheckedListener;
    private SlidingMenu main_slidingmenu;
    private View menu_at;
    private TextView menu_at_text;
    private View menu_favourite;
    private View menu_history;
    private View menu_mail;
    private TextView menu_mail_text;
    //    private View menu_pattern;
    private View menu_user;
    private ImageView menu_user_avatar;
    private TextView menu_user_name;

    @Override
    public void assignEvents() {
        content_user_avatar.setOnClickListener(mClickListener);
        content_setting.setOnClickListener(mClickListener);
        content_group.setOnCheckedChangeListener(mCheckedChangeListener);
        content_channel.setOnRepeatCheckedListener(mRepeatCheckedListener);
        content_pager.setOnPageChangeListener(mPageChangeListener);
        menu_user.setOnClickListener(mClickListener);
        menu_at.setOnClickListener(mClickListener);
        menu_mail.setOnClickListener(mClickListener);
        menu_favourite.setOnClickListener(mClickListener);
        menu_history.setOnClickListener(mClickListener);
        //        menu_pattern.setOnClickListener(mClickListener);
        mHelper.setOnItemClickListener(mItemClickListener);
    }

    private int getChannelIdByPosition(int position) {
        int channelId;

        switch (position) {
            case 0:
                channelId = 110;
                break;
            case 1:
                channelId = 73;
                break;
            case 2:
                channelId = 74;
                break;
            case 3:
                channelId = 75;
                break;
            default:
                channelId = 110;
        }

        return channelId;
    }

    @Override
    public void initData(Intent intent) {
        AcFunRead acFunRead = (AcFunRead) AcFunRead.getInstance();

        mPosition = acFunRead.readInt("lastChannelPosition", 0);
    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.content_user_avatar:
                        if (main_slidingmenu.isMenuShowing()) {
                            main_slidingmenu.showContent(true);
                        } else {
                            main_slidingmenu.showMenu(true);
                        }
                        break;
                    case R.id.content_setting:
                        start(SettingActivity.create(getActivity()));
                }
            }
        };
        mCheckedChangeListener = new GroupTabView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(GroupTabView groupTabView, int position, ChildTabView childTabView) {
                content_pager.setCurrentItem(position, true);
            }
        };
        mPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int i) {
            }

            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                if (i == 0) {
                    main_slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                } else {
                    main_slidingmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                }
                content_group.check(i);
            }
        };
        mRepeatCheckedListener = new RepeatCheckableChildTabView.OnRepeatCheckedListener() {
            @Override
            public void onRepeatChecked(RepeatCheckableChildTabView childTabView) {
                mHelper.toggle();
            }
        };
        mItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mHelper.hide();
                content_channel.setText(mHelper.getItem(position));
                if (mPosition != position) {
                    mPosition = position;
                    mAdapter.remove(1);
                    mAdapter.refresh();
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
        exit(false);
    }

    @Override
    protected void onDestroy() {
        AcFunRead acFunRead = (AcFunRead) AcFunRead.getInstance();

        acFunRead.writeInt("lastChannelPosition", mPosition);

        mHelper.hide();

        super.onDestroy();
    }

    @Override
    public void queryLayout() {
        Resources resources = getResources();

        main_slidingmenu = (SlidingMenu) findContentView(R.id.main_slidingmenu);

        content_user_avatar = findContentView(R.id.content_user_avatar);
        content_user_avatar_image = (ImageView) findContentView(R.id.content_user_avatar_image);
        content_setting = findContentView(R.id.content_setting);

        content_group = (GroupTabView) findContentView(R.id.content_group);
        content_channel = (RepeatCheckableChildTabView) findContentView(R.id.content_channel);
        content_pager = (ViewPager) findContentView(R.id.content_pager);

        menu_user = findContentView(R.id.menu_user);
        menu_user_name = (TextView) findContentView(R.id.menu_user_name);
        menu_user_avatar = (ImageView) findContentView(R.id.menu_user_avatar);
        menu_at = findContentView(R.id.menu_at);
        menu_at_text = (TextView) findContentView(R.id.menu_at_text);
        menu_mail = findContentView(R.id.menu_mail);
        menu_mail_text = (TextView) findContentView(R.id.menu_mail_text);
        menu_favourite = findContentView(R.id.menu_favourite);
        menu_history = findContentView(R.id.menu_history);
        //        menu_pattern = findContentView(R.id.menu_pattern);

        mHelper = new Helper(getActivity(), findContentView(R.id.content_channel));
        mHelper.add(0, resources.getString(R.string.channel_misc));
        mHelper.add(1, resources.getString(R.string.channel_work_emotion));
        mHelper.add(2, resources.getString(R.string.channel_dramaculture));
        mHelper.add(3, resources.getString(R.string.channel_comic_novel));

        content_channel.setText(mHelper.getItem(mPosition));
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void startAction() {
        mAdapter = new Adapter(getSupportFragmentManager());
        content_pager.setAdapter(mAdapter);
    }

    private class Adapter extends FragmentPageAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new RecommendFragment();
                case 1:
                    return ChannelFragment.create(getChannelIdByPosition(mPosition));
            }

            return null;
        }
    }

    private class Helper extends PopupAbsListHelper<String, ChannelSelectHolder> {
        public Helper(Context context, View anchor) {
            super(context, anchor);
        }

        @Override
        public ChannelSelectHolder createHolder(View convertView) {
            return new ChannelSelectHolder(convertView);
        }

        @Override
        public View createView(String item) {
            return View.inflate(getActivity(), R.layout.item_channelselect, null);
        }
    }
}