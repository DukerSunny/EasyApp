package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.beans.User;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class ProfileActivity extends ActivityFramework {
    private MaterialDialog.SimpleCallback mLogoutCallback;
    private MaterialDialog mLogoutDialog;
    private View.OnClickListener mOnClickListener;
    private ImageView profile_avatar;
    private View profile_follow;
    private TextView profile_follow_text;
    private View profile_logout;
    private TextView profile_mail;
    private TextView profile_phone;
    private TextView profile_qq;
    private TextView profile_username;
    private TextView profile_weight_text;

    public static Intent create(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
        RippleOnClickListener.attach(profile_follow, mOnClickListener);
        RippleOnClickListener.attach(profile_logout, mOnClickListener);
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_profile);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        profile_avatar = (ImageView) findViewById(R.id.profile_avatar);
        profile_username = (TextView) findViewById(R.id.profile_username);
        profile_weight_text = (TextView) findViewById(R.id.profile_weight_text);
        profile_follow = findViewById(R.id.profile_follow);
        profile_follow_text = (TextView) findViewById(R.id.profile_follow_text);
        profile_mail = (TextView) findViewById(R.id.profile_mail);
        profile_phone = (TextView) findViewById(R.id.profile_phone);
        profile_qq = (TextView) findViewById(R.id.profile_qq);
        profile_logout = findViewById(R.id.profile_logout);

        mLogoutDialog = new MaterialDialog.Builder(this).title(R.string.logout_sure).positiveText(R.string.app_ok)
                .negativeText(R.string.app_cancel).callback(mLogoutCallback).build();

        RippleDrawable.attach(profile_follow, RippleStyle.Light);
        RippleDrawable.attach(profile_logout, RippleStyle.Light);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.profile_follow:
                        start(FollowActivity.create(getContext()));
                        break;
                    case R.id.profile_logout:
                        mLogoutDialog.show();
                        break;
                }
            }
        };
        mLogoutCallback = new MaterialDialog.SimpleCallback() {
            @Override
            public void onPositive(MaterialDialog materialDialog) {
                DouyuTv.getInstance().writeUser(null);
                onBackPressed();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_profile;
    }

    @Override
    public void startAction() {
        updateUser();
    }

    private void updateUser() {
        User user = DouyuTv.getInstance().readUser();

        ImageLoaderHelper.loadImage(profile_avatar, user.getAvatar().getBig(), R.drawable.loading_1x1, R.drawable.retry_1x1);
        profile_username.setText(user.getNickname());
        profile_weight_text.setText(String.valueOf(user.getGold1()));
        profile_follow_text.setText(String.valueOf(user.getFollow()));
        profile_mail.setText(user.getEmail());
        profile_phone.setText(user.getMobile_phone());
        profile_qq.setText(user.getQq());
    }
}