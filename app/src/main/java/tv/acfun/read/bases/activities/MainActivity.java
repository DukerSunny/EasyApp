package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.ChannelFragment;
import tv.acfun.read.bases.fragments.RecommendFragment;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.helpers.LoginHelper;
import tv.acfun.read.holders.ChannelSelectHolder;
import tv.acfun.read.widgets.RepeatCheckableChildTabView;

public class MainActivity extends ActivityFramework {
    private String app_dropdown;
    private RepeatCheckableChildTabView content_channel;
    private GroupTabView content_group;
    private ViewPager content_pager;
    private View content_search;
    private View content_user_avatar;
    private ImageView content_user_avatar_image;
    private View content_user_notification;
    private Adapter mAdapter = null;
    private GroupTabView.OnCheckedChangeListener mCheckedChangeListener;
    private View.OnClickListener mClickListener;
    private SlidingMenu.OnCloseListener mCloseListener;
    private SlidingMenu.OnClosedListener mClosedListener;
    private long mExitTime = 0;
    private AlphaAnimation mFadeIn;
    private Animation.AnimationListener mFadeInListener;
    private AlphaAnimation mFadeOut;
    private Animation.AnimationListener mFadeOutListener;
    private FullUser mFullUser = null;
    private Helper mHelper;
    private AdapterView.OnItemClickListener mItemClickListener;
    private LoginHelper mLoginHelper;
    private LoginHelper.LoginCallback mLoginListener;
    private SlidingMenu.OnOpenListener mOpenListener;
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
        main_slidingmenu.setOnOpenListener(mOpenListener);
        main_slidingmenu.setOnCloseListener(mCloseListener);
        main_slidingmenu.setOnClosedListener(mClosedListener);
        content_user_avatar.setOnClickListener(mClickListener);
        content_search.setOnClickListener(mClickListener);
        menu_setting.setOnClickListener(mClickListener);
        content_group.setOnCheckedChangeListener(mCheckedChangeListener);
        content_channel.setOnRepeatCheckedListener(mRepeatCheckedListener);
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
            content_user_avatar_image.setImageResource(R.drawable.image_avatar);
            content_user_notification.setVisibility(View.GONE);
            menu_user_avatar.setImageResource(R.drawable.image_avatar);
            menu_user_name.setText(R.string.login_required);
            menu_at_text.setVisibility(View.GONE);
            menu_mail_text.setVisibility(View.GONE);
        } else {
            if (mFullUser.getUserImg().equals("avatar")) {
                content_user_avatar_image.setImageResource(R.drawable.image_avatar);
                menu_user_avatar.setImageResource(R.drawable.image_avatar);
            } else {
                ImageLoaderHelper.loadImage(content_user_avatar_image, mFullUser.getUserImg());
                ImageLoaderHelper.loadImage(menu_user_avatar, mFullUser.getUserImg());
            }
            menu_user_name.setText(mFullUser.getUsername());
            menu_user_signature.setText(mFullUser.getSignature());
        }
    }

    @Override
    public void enquiryViews() {
        Resources resources = getResources();

        addActionBarViewItem(0, R.layout.activity_main_content_header, false);
        showActionBarHome(false);

        main_slidingmenu = (SlidingMenu) findViewById(R.id.main_slidingmenu);

        content_user_avatar = findViewById(R.id.content_user_avatar);
        content_user_avatar_image = (ImageView) findViewById(R.id.content_user_avatar_image);
        content_user_notification = findViewById(R.id.content_user_notification);
        content_search = findViewById(R.id.content_search);

        content_group = (GroupTabView) findViewById(R.id.content_group);
        content_channel = (RepeatCheckableChildTabView) findViewById(R.id.content_channel);
        content_pager = (ViewPager) findViewById(R.id.content_pager);

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

        mHelper = new Helper(getActivity(), findViewById(R.id.content_channel));
        mHelper.add(0, resources.getString(R.string.channel_misc));
        mHelper.add(1, resources.getString(R.string.channel_work_emotion));
        mHelper.add(2, resources.getString(R.string.channel_dramaculture));
        mHelper.add(3, resources.getString(R.string.channel_comic_novel));

        content_channel.setText(mHelper.getItem(mPosition) + " " + app_dropdown);

        mFadeIn = new AlphaAnimation(0f, 1f);
        mFadeIn.setDuration(400);
        mFadeIn.setAnimationListener(mFadeInListener);
        mFadeOut = new AlphaAnimation(1f, 0f);
        mFadeOut.setDuration(400);
        mFadeOut.setAnimationListener(mFadeOutListener);
    }

    @Override
    public void establishCallbacks() {
        mOpenListener = new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                hideAvatar();
            }
        };
        mCloseListener = new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                showAvatar();
            }
        };
        mClosedListener = new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                hideSoftInputMethod();
            }
        };
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcFunRead acFunRead = AcFunRead.getInstance();

                switch (v.getId()) {
                    case R.id.content_user_avatar:
                        if (main_slidingmenu.isMenuShowing()) {
                            main_slidingmenu.showContent(true);
                        } else {
                            main_slidingmenu.showMenu(true);
                        }
                        break;
                    case R.id.content_search:
                        start(SearchActivity.create(getActivity()));
                        break;
                    case R.id.menu_user_description:
                        if (mFullUser != null) {
                            if (acFunRead.isExpired()) {
                                acFunRead.clearLogin();
                                mLoginHelper.show(LoginHelper.Reason.Expired);
                            } else {
                                start(ProfileActivity.create(getActivity(), mFullUser), false);
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                            }
                        } else {
                            mLoginHelper.show(LoginHelper.Reason.Unauthorized);
                        }
                        break;
                    case R.id.menu_history:
                        start(HistoryActivity.create(getActivity()));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        break;
                    case R.id.menu_setting:
                        start(SettingActivity.create(getActivity()), false);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
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
                content_channel.setText(mHelper.getItem(position) + " " + app_dropdown);
                if (mPosition != position) {
                    mPosition = position;
                    mAdapter.remove(1);
                    mAdapter.refresh();
                }
            }
        };
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
        mFadeInListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {
                content_user_avatar_image.setVisibility(View.VISIBLE);
            }
        };
        mFadeOutListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                content_user_avatar_image.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationStart(Animation animation) {

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

    private void hideAvatar() {
        content_user_avatar_image.clearAnimation();
        content_user_avatar_image.startAnimation(mFadeOut);
    }

    private void hideSoftInputMethod() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(main_slidingmenu.getWindowToken(), 0);
    }

    @Override
    public void onActionBarItemClick(int id, View item) {

    }

    @Override
    public void onBackPressed() {
        long exitTime;

        if (main_slidingmenu.isMenuShowing()) {
            main_slidingmenu.showContent(true);
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
    protected void onDestroy() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        acFunRead.writeInt("lastChannelPosition", mPosition);

        mHelper.hide();

        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter == null) {
            mAdapter = new Adapter(getSupportFragmentManager());
            content_pager.setAdapter(mAdapter);
        }
        checkUser();
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_main);
    }

    private void showAvatar() {
        content_user_avatar_image.clearAnimation();
        content_user_avatar_image.startAnimation(mFadeIn);
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
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_channelselect, null);
        }
    }
}