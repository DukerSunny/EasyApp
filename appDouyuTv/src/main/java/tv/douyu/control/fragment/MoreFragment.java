package tv.douyu.control.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.base.FragmentFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;
import tv.douyu.control.activity.AdvertiseActivity;
import tv.douyu.control.activity.FollowActivity;
import tv.douyu.control.activity.HistoryActivity;
import tv.douyu.control.activity.LoginActivity;
import tv.douyu.control.activity.MissionActivity;
import tv.douyu.control.activity.ProfileActivity;
import tv.douyu.control.activity.SettingActivity;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.User;
import tv.douyu.wrapper.helper.AuthorizeHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class MoreFragment extends FragmentFramework {
    private AuthorizeHelper mAuthorizeHelper;
    @InjectView(R.id.more_advertise)
    View more_advertise;
    @InjectView(R.id.more_follow)
    View more_follow;
    @InjectView(R.id.more_history)
    View more_history;
    @InjectView(R.id.more_mission)
    View more_mission;
    @InjectView(R.id.more_setting)
    View more_setting;
    @InjectView(R.id.more_user_avatar)
    ImageView more_user_avatar;
    @InjectView(R.id.more_user_clicktologin)
    View more_user_clicktologin;
    @InjectView(R.id.more_user_follow)
    TextView more_user_follow;
    @InjectView(R.id.more_user_info)
    View more_user_info;
    @InjectView(R.id.more_user_login)
    View more_user_login;
    @InjectView(R.id.more_user_weight)
    TextView more_user_weight;

    public static MoreFragment create() {
        return new MoreFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    public void enquiryViews() {
        mAuthorizeHelper = new AuthorizeHelper(this, 0);

        RippleDrawable.attach(more_user_info);
        RippleDrawable.attach(more_follow);
        RippleDrawable.attach(more_history);
        RippleDrawable.attach(more_mission);
        RippleDrawable.attach(more_advertise);
        RippleDrawable.attach(more_setting);
    }

    @Override
    public void establishCallbacks() {
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUser();
    }

    @OnClick(R.id.more_advertise)
    void onAdvertiseClick() {
        if (mAuthorizeHelper.validate(true)) {
            start(AdvertiseActivity.create(getContext()));
        }
    }

    @OnClick(R.id.more_follow)
    void onFollowClick() {
        if (mAuthorizeHelper.validate(true)) {
            start(FollowActivity.create(getContext()));
        }
    }

    @OnClick(R.id.more_history)
    void onHistoryClick() {
        if (mAuthorizeHelper.validate(true)) {
            start(HistoryActivity.create(getContext()));
        }
    }

    @OnClick(R.id.more_mission)
    void onMissionClick() {
        if (mAuthorizeHelper.validate(true)) {
            start(MissionActivity.create(getContext()));
        }
    }

    @OnClick(R.id.more_setting)
    void onSettingClick() {
        start(SettingActivity.create(getContext()));
    }

    @OnClick(R.id.more_user_info)
    void onUserInfoClick() {
        if (mAuthorizeHelper.validate(false)) {
            start(ProfileActivity.create(getContext()), 0);
        } else {
            start(LoginActivity.create(getContext()), 1);
        }
    }

    @Override
    public void startAction() {
        updateUser();
    }

    private void updateUser() {
        DouyuTv douyuTv = DouyuTv.getInstance();
        User user;

        if (douyuTv.isAuthorized()) {
            user = douyuTv.getUser();
            ImageLoaderHelper
                    .loadImage(more_user_avatar, user.getAvatar().getBig(), R.drawable.loading_1x1, R.drawable.retry_1x1);
            more_user_clicktologin.setVisibility(View.GONE);
            more_user_login.setVisibility(View.VISIBLE);
            more_user_weight.setText(String.valueOf(user.getGold1()));
            more_user_follow.setText(String.valueOf(user.getFollow()));
        } else {
            more_user_avatar.setImageResource(R.drawable.avatar);
            more_user_clicktologin.setVisibility(View.VISIBLE);
            more_user_login.setVisibility(View.GONE);
            more_user_weight.setText("0");
            more_user_follow.setText("0");
        }
    }
}