package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.helpers.PopupAbsListHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;
import com.harreke.easyapp.widgets.ChildTabView;
import com.harreke.easyapp.widgets.GroupTabView;
import com.harreke.easyapp.widgets.SlidingTabLayout;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.ChannelFragment;
import tv.acfun.read.bases.fragments.RecommendFragment;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.helpers.ConnectionHelper;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.helpers.OfflineImageLoaderHelper;
import tv.acfun.read.holders.ChannelSelectHolder;
import tv.acfun.read.widgets.RepeatCheckableChildTabView;

public class MainActivity extends ActivityFramework {
    private String app_dropdown;
    private ViewPager content_pager;
    private Adapter mAdapter = null;
    private GroupTabView.OnCheckedChangeListener mCheckedChangeListener;
    private View.OnClickListener mClickListener;
    private DrawerLayout.DrawerListener mDrawerListener;
    private ActionBarDrawerToggle mDrawerToggle;
    private long mExitTime = 0;
    private FullUser mFullUser = null;
    private Helper mHelper;
    private AdapterView.OnItemClickListener mItemClickListener;
    private LoginHelper mLoginHelper;
    private LoginHelper.LoginCallback mLoginListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private int mPosition = 0;
    private RepeatCheckableChildTabView.OnRepeatCheckedListener mRepeatCheckedListener;
    private DrawerLayout main_drawer;
    private SlidingTabLayout main_slidingtab;
    private View menu_at;
    private TextView menu_at_text;
    private View menu_favourite;
    private View menu_history;
    private View menu_mail;
    private TextView menu_mail_text;
    private View menu_pattern;
    private View menu_setting;
    private ImageView menu_user_avatar;
    private View menu_user_description;
    private TextView menu_user_name;
    private TextView menu_user_signature;

    @Override
    public void acquireArguments(Intent intent) {
        AcFunRead acFunRead = AcFunRead.getInstance();

        mPosition = acFunRead.readInt("lastChannelPosition", 0);

        app_dropdown = getString(R.string.app_dropdown);
    }

    @Override
    public void attachCallbacks() {
        main_drawer.setDrawerListener(mDrawerToggle);

        menu_setting.setOnClickListener(mClickListener);

        content_pager.setOnPageChangeListener(mPageChangeListener);

        menu_user_description.setOnClickListener(mClickListener);
        menu_at.setOnClickListener(mClickListener);
        menu_mail.setOnClickListener(mClickListener);
        menu_favourite.setOnClickListener(mClickListener);
        menu_history.setOnClickListener(mClickListener);
        menu_pattern.setOnClickListener(mClickListener);

        mHelper.setOnItemClickListener(mItemClickListener);
    }

    private void checkUser() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        mFullUser = acFunRead.readFullUser();

        if (mFullUser == null) {
            menu_user_avatar.setImageResource(R.drawable.image_avatar);
            menu_user_name.setText(R.string.login_required);
            menu_at_text.setVisibility(View.GONE);
            menu_mail_text.setVisibility(View.GONE);
        } else {
            loadAvatar();
            menu_user_name.setText(mFullUser.getUsername());
            menu_user_signature.setText(mFullUser.getSignature());
        }
    }

    @Override
    public void configActivity() {
        AcFunRead.getInstance().registerConnectionReceiver();
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.app_name);
        addToolbarItem(0, R.string.app_search, R.drawable.image_search_inverse);
    }

    @Override
    public void enquiryViews() {
        Resources resources = getResources();

        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, main_drawer, getToolBar(), R.string.empty, R.string.empty);
        mDrawerToggle.syncState();

        content_pager = (ViewPager) findViewById(R.id.content_pager);

        main_slidingtab = (SlidingTabLayout) findViewById(R.id.main_slidingtab);
        main_slidingtab.setCustomTabView(R.layout.item_slidingtab_fixed, R.id.slidingtab_text);

        menu_user_description = findViewById(R.id.menu_user_description);
        menu_user_name = (TextView) findViewById(R.id.menu_user_name);
        menu_user_avatar = (ImageView) findViewById(R.id.menu_user_avatar);
        menu_user_signature = (TextView) findViewById(R.id.menu_user_signature);
        menu_at = findViewById(R.id.menu_at);
        menu_at_text = (TextView) findViewById(R.id.menu_at_text);
        menu_mail = findViewById(R.id.menu_mail);
        menu_mail_text = (TextView) findViewById(R.id.menu_mail_text);
        menu_favourite = findViewById(R.id.menu_favourite);
        menu_history = findViewById(R.id.menu_history);
        menu_setting = findViewById(R.id.menu_setting);
        menu_pattern = findViewById(R.id.menu_pattern);

        mLoginHelper = new LoginHelper(this, mLoginListener);

        mHelper = new Helper(getActivity(), main_slidingtab);
        mHelper.add(0, resources.getString(R.string.channel_misc));
        mHelper.add(1, resources.getString(R.string.channel_work_emotion));
        mHelper.add(2, resources.getString(R.string.channel_dramaculture));
        mHelper.add(3, resources.getString(R.string.channel_comic_novel));

        //        content_channel.setText(mHelper.getItem(mPosition) + " " + app_dropdown);
    }

    @Override
    public void establishCallbacks() {
        mDrawerListener = new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerClosed(View view) {
                hideSoftInputMethod();
            }

            @Override
            public void onDrawerOpened(View view) {
            }

            @Override
            public void onDrawerSlide(View view, float v) {
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        };
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcFunRead acFunRead = AcFunRead.getInstance();

                switch (v.getId()) {
                    case R.id.menu_user_description:
                        if (mFullUser != null) {
                            if (acFunRead.isExpired()) {
                                acFunRead.clearLogin();
                                mLoginHelper.show(LoginHelper.Reason.Expired);
                            } else {
                                start(ProfileActivity.create(getActivity(), mFullUser), false);
                            }
                        } else {
                            mLoginHelper.show(LoginHelper.Reason.Unauthorized);
                        }
                        break;
                    case R.id.menu_mail:
                        if (mFullUser != null) {
                            if (acFunRead.isExpired()) {
                                acFunRead.clearLogin();
                                mLoginHelper.show(LoginHelper.Reason.Expired);
                            } else {
                                start(MailActivity.create(getActivity()), false);
                            }
                        } else {
                            mLoginHelper.show(LoginHelper.Reason.Unauthorized);
                        }
                        break;
                    case R.id.menu_favourite:
                        if (mFullUser != null) {
                            if (acFunRead.isExpired()) {
                                acFunRead.clearLogin();
                                mLoginHelper.show(LoginHelper.Reason.Expired);
                            } else {
                                start(FavouriteActivity.create(getActivity()), false);
                            }
                        } else {
                            mLoginHelper.show(LoginHelper.Reason.Unauthorized);
                        }
                        break;
                    case R.id.menu_history:
                        start(HistoryActivity.create(getActivity()));
                        break;
                    case R.id.menu_setting:
                        start(SettingActivity.create(getActivity()), false);
                }
            }
        };
        mCheckedChangeListener = new GroupTabView.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(GroupTabView groupTabView, int position, ChildTabView childTabView) {
                content_pager.setCurrentItem(position, true);
            }
        };
        //        mPageChangeListener = new ViewPager.OnPageChangeListener() {
        //            @Override
        //            public void onPageScrollStateChanged(int i) {
        //            }
        //
        //            @Override
        //            public void onPageScrolled(int i, float v, int i2) {
        //            }
        //
        //            @Override
        //            public void onPageSelected(int i) {
        //                content_group.check(i);
        //            }
        //        };
        //        mRepeatCheckedListener = new RepeatCheckableChildTabView.OnRepeatCheckedListener() {
        //            @Override
        //            public void onRepeatChecked(RepeatCheckableChildTabView childTabView) {
        //                mHelper.toggle();
        //            }
        //        };
        //        mItemClickListener = new AdapterView.OnItemClickListener() {
        //            @Override
        //            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //                mHelper.hide();
        //                content_channel.setText(mHelper.getItem(position) + " " + app_dropdown);
        //                if (mPosition != position) {
        //                    mPosition = position;
        //                    mAdapter.remove(1);
        //                    mAdapter.refresh();
        //                    writeLastPosition();
        //                }
        //            }
        //        };
        mLoginListener = new LoginHelper.LoginCallback() {
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
                checkUser();
            }
        };
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

    private void hideSoftInputMethod() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(main_drawer.getWindowToken(), 0);
    }

    private void loadAvatar() {
        if (mFullUser.getUserImg().equals("avatar") || !ConnectionHelper.shouldLoadImage()) {
            OfflineImageLoaderHelper.loadImage(menu_user_avatar, OfflineImageLoaderHelper.OfflineImage.Avatar);
        } else {
            ImageLoaderHelper.loadImage(menu_user_avatar, mFullUser.getUserImg());
        }
    }

    @Override
    public void onBackPressed() {
        long exitTime;

        if (main_drawer.isDrawerOpen(Gravity.START)) {
            main_drawer.closeDrawer(Gravity.START);
        } else {
            exitTime = System.currentTimeMillis();
            if (exitTime - mExitTime <= 3000) {
                exit(false);
            } else {
                mExitTime = exitTime;
                showToast(getString(R.string.app_exit));
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UmengUpdateAgent.update(this);
    }

    @Override
    protected void onDestroy() {
        AcFunRead.getInstance().unregisterConnectionReceiver();

        mHelper.hide();

        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(getActivity()));

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

        if (mAdapter == null) {
            mAdapter = new Adapter(getSupportFragmentManager());
            content_pager.setAdapter(mAdapter);
            main_slidingtab.setViewPager(content_pager);
        }
        checkUser();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void startAction() {
    }

    private void writeLastPosition() {
        AcFunRead.getInstance().writeInt("lastChannelPosition", mPosition);
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
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RecommendFragment();
                case 1:
                    return ChannelFragment.create(getChannelIdByPosition(mPosition));
            }

            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.main_recommend);
                case 1:
                    return mHelper.getItem(mPosition) + " " + app_dropdown;
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
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_channelselect, null);
        }
    }
}