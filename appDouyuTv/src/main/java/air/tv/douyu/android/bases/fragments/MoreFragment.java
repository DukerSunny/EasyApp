package air.tv.douyu.android.bases.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import air.tv.douyu.android.R;
import air.tv.douyu.android.bases.activities.AdvertiseActivity;
import air.tv.douyu.android.bases.activities.FollowActivity;
import air.tv.douyu.android.bases.activities.HistoryActivity;
import air.tv.douyu.android.bases.activities.MissionActivity;
import air.tv.douyu.android.bases.activities.ProfileActivity;
import air.tv.douyu.android.bases.activities.SettingActivity;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.beans.User;
import air.tv.douyu.android.enums.AuthorizeStatus;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class MoreFragment extends FragmentFramework {
    private AuthorizeStatus mLastAuthorizeStatus = AuthorizeStatus.Unauthorized;
    private View.OnClickListener mOnClickListener;
    private View more_advertise;
    private View more_follow;
    private View more_history;
    private View more_mission;
    private View more_setting;
    private ImageView more_user_avatar;
    private View more_user_clicktologin;
    private TextView more_user_follow;
    private View more_user_info;
    private View more_user_login;
    private TextView more_user_weight;

    public static MoreFragment create() {
        return new MoreFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
    }

    @Override
    public void attachCallbacks() {
        RippleOnClickListener.attach(more_user_info, mOnClickListener);
        RippleOnClickListener.attach(more_follow, mOnClickListener);
        RippleOnClickListener.attach(more_history, mOnClickListener);
        RippleOnClickListener.attach(more_mission, mOnClickListener);
        RippleOnClickListener.attach(more_advertise, mOnClickListener);
        RippleOnClickListener.attach(more_setting, mOnClickListener);
    }

    @Override
    public void enquiryViews() {
        more_user_info = findViewById(R.id.more_user_info);
        more_user_avatar = (ImageView) findViewById(R.id.more_user_avatar);
        more_user_clicktologin = findViewById(R.id.more_user_clicktologin);
        more_user_login = findViewById(R.id.more_user_login);
        more_user_weight = (TextView) findViewById(R.id.more_user_weight);
        more_user_follow = (TextView) findViewById(R.id.more_user_follow);
        more_follow = findViewById(R.id.more_follow);
        more_history = findViewById(R.id.more_history);
        more_mission = findViewById(R.id.more_mission);
        more_advertise = findViewById(R.id.more_advertise);
        more_setting = findViewById(R.id.more_setting);

        RippleDrawable.attach(more_user_info);
        RippleDrawable.attach(more_follow);
        RippleDrawable.attach(more_history);
        RippleDrawable.attach(more_mission);
        RippleDrawable.attach(more_advertise);
        RippleDrawable.attach(more_setting);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.more_user_info:
                        onUserInfoClick();
                        break;
                    case R.id.more_follow:
                        onFollowClick();
                        break;
                    case R.id.more_history:
                        onHistoryClick();
                        break;
                    case R.id.more_mission:
                        onMissionClick();
                        break;
                    case R.id.more_advertise:
                        onAdvertiseClick();
                        break;
                    case R.id.more_setting:
                        onSettingClick();
                        break;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_more;
    }

    private void onAdvertiseClick() {
        if (DouyuTv.getInstance().validateAuthorize(getFramework())) {
            start(AdvertiseActivity.create(getContext()));
        }
    }

    private void onFollowClick() {
        if (DouyuTv.getInstance().validateAuthorize(getFramework())) {
            start(FollowActivity.create(getContext()));
        }
    }

    private void onHistoryClick() {
        if (DouyuTv.getInstance().validateAuthorize(getFramework())) {
            start(HistoryActivity.create(getContext()));
        }
    }

    private void onMissionClick() {
        if (DouyuTv.getInstance().validateAuthorize(getFramework())) {
            start(MissionActivity.create(getContext()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUser();
    }

    private void onSettingClick() {
        start(SettingActivity.create(getContext()));
    }

    private void onUserInfoClick() {
        if (DouyuTv.getInstance().validateAuthorize(getFramework())) {
            start(ProfileActivity.create(getContext()));
        }
    }

    @Override
    public void startAction() {
    }

    private void updateUser() {
        DouyuTv douyuTv = DouyuTv.getInstance();
        AuthorizeStatus authorizeStatus = douyuTv.validateAuthorize();
        User user;

        if (authorizeStatus == AuthorizeStatus.Authorized) {
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
        //        Log.e(null, "now authorize=" + authorizeStatus + " last authorize=" + mLastAuthorizeStatus);
        //        if (authorizeStatus != mLastAuthorizeStatus) {
        //            mLastAuthorizeStatus = authorizeStatus;
        //            if (authorizeStatus == AuthorizeStatus.Authorized) {
        //                user = douyuTv.getUser();
        //                ImageLoaderHelper
        //                        .loadImage(more_user_avatar, user.getAvatar().getBig(), R.drawable.loading_1x1, R.drawable.retry_1x1);
        //                more_user_clicktologin.setVisibility(View.GONE);
        //                more_user_login.setVisibility(View.VISIBLE);
        //                more_user_weight.setText(String.valueOf(user.getGold1()));
        //                more_user_follow.setText(String.valueOf(user.getFollow()));
        //            } else {
        //                more_user_avatar.setImageResource(R.drawable.avatar);
        //                more_user_clicktologin.setVisibility(View.VISIBLE);
        //                more_user_login.setVisibility(View.GONE);
        //                more_user_weight.setText("0");
        //                more_user_follow.setText("0");
        //            }
        //        }
    }
}