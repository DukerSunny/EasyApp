package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.ChannelFragment;
import tv.acfun.read.bases.fragments.RecommendFragment;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.helpers.ImageConnectionHelper;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.helpers.OfflineImageLoaderHelper;

public class MainActivity extends ActivityFramework {
    private int[] channel_id;
    private String[] channel_name;
    private ActionBarDrawerToggle mDrawerToggle;
    private long mExitTime = 0;
    private FullUser mFullUser = null;
    private LoginHelper mLoginHelper;
    private LoginHelper.LoginCallback mLoginListener;
    private View.OnClickListener mOnClickListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private int mPosition = 0;
    private ViewPager main_content_pager;
    private PagerSlidingTabStrip main_content_pager_strip;
    private DrawerLayout main_drawer;
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

    public static Intent create(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
        AcFunRead acFunRead = AcFunRead.getInstance();
        Resources resources = getResources();

        mPosition = acFunRead.readInt("lastChannelPosition", 0);

        channel_name = resources.getStringArray(R.array.channel_name);
        channel_id = resources.getIntArray(R.array.channel_id);
    }

    @Override
    public void attachCallbacks() {
        main_drawer.setDrawerListener(mDrawerToggle);

        menu_setting.setOnClickListener(mOnClickListener);

        main_content_pager_strip.setOnPageChangeListener(mPageChangeListener);

        menu_at.setOnClickListener(mOnClickListener);
        menu_mail.setOnClickListener(mOnClickListener);
        menu_pattern.setOnClickListener(mOnClickListener);

        mLoginHelper.setLoginCallback(mLoginListener);

        RippleOnClickListener.attach(menu_user_description, mOnClickListener);
        RippleOnClickListener.attach(menu_user_signature, mOnClickListener);
        RippleOnClickListener.attach(menu_favourite, mOnClickListener);
        RippleOnClickListener.attach(menu_history, mOnClickListener);
        RippleOnClickListener.attach(menu_setting, mOnClickListener);
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
        addToolbarItem(0, R.string.app_search, R.drawable.image_toolbar_search);
    }

    @Override
    public void enquiryViews() {
        Resources resources = getResources();

        main_drawer = (DrawerLayout) findViewById(R.id.main_drawer);

        mDrawerToggle = new ActionBarDrawerToggle(this, main_drawer, getToolBar(), R.string.empty, R.string.empty);
        mDrawerToggle.syncState();

        main_content_pager = (ViewPager) findViewById(R.id.main_content_pager);

        main_content_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.main_content_pager_strip);
        main_content_pager_strip.setTextColor(Color.WHITE);
        main_content_pager_strip.setTextSize((int) resources.getDimension(R.dimen.Subhead));

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

        RippleDrawable.attach(menu_user_description, RippleDrawable.RIPPLE_STYLE_LIGHT);
        RippleDrawable.attach(menu_user_signature, RippleDrawable.RIPPLE_STYLE_LIGHT);
        RippleDrawable.attach(menu_favourite);
        RippleDrawable.attach(menu_history);
        RippleDrawable.attach(menu_setting);

        mLoginHelper = new LoginHelper(this);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
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
                                start(ProfileActivity.create(getContext(), mFullUser), Anim.Enter_Left);
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
                                start(MailActivity.create(getContext()), Anim.Enter_Left);
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
                                start(FavouriteActivity.create(getContext()), Anim.Enter_Left);
                            }
                        } else {
                            mLoginHelper.show(LoginHelper.Reason.Unauthorized);
                        }
                        break;
                    case R.id.menu_history:
                        start(HistoryActivity.create(getContext()), Anim.Enter_Left);
                        break;
                    case R.id.menu_setting:
                        start(SettingActivity.create(getContext()), Anim.Enter_Left);
                }
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
                mPosition = i;
            }
        };
        mLoginListener = new LoginHelper.LoginCallback() {
            @Override
            public void onSuccess() {
                checkUser();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    private void loadAvatar() {
        if (mFullUser.getUserImg().equals("avatar") || !ImageConnectionHelper.shouldLoadImage()) {
            OfflineImageLoaderHelper.loadImage(menu_user_avatar, OfflineImageLoaderHelper.OfflineImage.Avatar);
        } else {
            ImageLoaderHelper
                    .loadImage(menu_user_avatar, mFullUser.getUserImg(), R.drawable.image_loading, R.drawable.image_idle);
        }
    }

    @Override
    public void onBackPressed() {
        long exitTime;

        if (main_drawer.isDrawerOpen(Gravity.LEFT)) {
            main_drawer.closeDrawer(Gravity.LEFT);
        } else {
            exitTime = System.currentTimeMillis();
            if (exitTime - mExitTime <= 3000) {
                exit();
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
        writeLastPosition();
        mLoginHelper.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(getContext()));

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
        checkUser();
    }

    @Override
    public void startAction() {
        main_content_pager.setAdapter(new Adapter(getSupportFragmentManager()));
        main_content_pager_strip.setViewPager(main_content_pager);
        main_content_pager.setCurrentItem(mPosition);
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
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new RecommendFragment();
                default:
                    return ChannelFragment.create(channel_id[position - 1]);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.main_recommend);
                default:
                    return channel_name[position - 1];
            }
        }
    }
}