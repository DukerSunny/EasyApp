package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.tools.GsonUtil;

import tv.acfun.read.R;
import tv.acfun.read.beans.FullUser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public class ProfileActivity extends ActivityFramework {
    private View.OnClickListener mClickListener;
    private FullUser mFullUser;
    private boolean mSelfProfile;
    private int mUserId;
    private View profile_back;
    private ImageView profile_user_avatar;
    private View profile_user_chats;
    private TextView profile_user_chats_text;
    private View profile_user_contributes;
    private TextView profile_user_contributes_text;
    private TextView profile_user_gender;
    private TextView profile_user_id;
    private TextView profile_user_location;
    private TextView profile_user_name;
    private TextView profile_user_phone;
    private TextView profile_user_qq;
    private TextView profile_user_signature;

    public static Intent create(Context context, FullUser fullUser) {
        Intent intent = new Intent(context, ProfileActivity.class);

        intent.putExtra("fullUser", GsonUtil.toString(fullUser));

        return intent;
    }

    public static Intent create(Context context, int userId) {
        Intent intent = new Intent(context, ProfileActivity.class);

        intent.putExtra("userId", userId);

        return intent;
    }

    @Override
    public void assignEvents() {
        profile_back.setOnClickListener(mClickListener);
        profile_user_contributes.setOnClickListener(mClickListener);
        profile_user_chats.setOnClickListener(mClickListener);
    }

    private void checkUser() {
        String gender;
        String refer;

        if (mFullUser != null) {
            ImageLoaderHelper.loadImage(profile_user_avatar, mFullUser.getUserImg());
            profile_user_name.setText(mFullUser.getUsername());
            profile_user_id.setText(String.valueOf(mFullUser.getUserId()));
            profile_user_signature.setText(mFullUser.getSignature());
            switch (mFullUser.getGender()) {
                case 0:
                    gender = getString(R.string.user_refer_female);
                    refer = getString(R.string.user_gender_female_refer);
                    break;
                case 1:
                    gender = getString(R.string.user_gender_male);
                    refer = getString(R.string.user_refer_male);
                    break;
                default:
                    gender = getString(R.string.user_unknown);
                    refer = getString(R.string.user_refer_unknown);
            }
            if (mSelfProfile) {
                refer = getString(R.string.user_refer_mine);
            }
            profile_user_gender.setText(gender);
            if (mFullUser.getLocation() != null) {
                profile_user_location.setText(mFullUser.getLocation());
            } else {
                profile_user_location.setText(R.string.user_unknown);
            }
            if (mFullUser.getQq() != null) {
                profile_user_qq.setText(mFullUser.getQq());
            } else {
                profile_user_qq.setText(R.string.user_unknown);
            }
            if (mFullUser.getPhone() != null) {
                profile_user_phone.setText(mFullUser.getPhone());
            } else {
                profile_user_phone.setText(R.string.user_unknown);
            }
            profile_user_contributes_text.setText(getString(R.string.user_contributes, refer));
            if (mSelfProfile) {
                profile_user_chats.setVisibility(View.GONE);
            } else {
                profile_user_chats.setVisibility(View.VISIBLE);
                profile_user_chats_text.setText(getString(R.string.user_chats, refer));
            }
        } else {

        }
    }

    @Override
    public void initData(Intent intent) {
        mFullUser = GsonUtil.toBean(intent.getStringExtra("fullUser"), FullUser.class);
        mUserId = intent.getIntExtra("userId", 0);

        if (mFullUser == null) {
            mSelfProfile = false;
        } else {
            mSelfProfile = true;
            mUserId = mFullUser.getUserId();
        }
    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.profile_back:
                        onBackPressed();
                        break;
                    case R.id.profile_user_contributes:
                        //                        start(ContributeActivity.create(getActivity(), mUserId));
                        break;
                    case R.id.profile_user_chats:
                        //                        start(ChatActivity.create(getActivity(), AcFunRead.getInstance().readFullUser().getUserId(), mUserId));
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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void queryLayout() {
        profile_back = findViewById(R.id.profile_back);
        profile_user_avatar = (ImageView) findViewById(R.id.profile_user_avatar);
        profile_user_name = (TextView) findViewById(R.id.profile_user_name);
        profile_user_id = (TextView) findViewById(R.id.profile_user_id);
        profile_user_signature = (TextView) findViewById(R.id.profile_user_signature);

        profile_user_gender = (TextView) findViewById(R.id.profile_user_gender);
        profile_user_location = (TextView) findViewById(R.id.profile_user_location);
        profile_user_qq = (TextView) findViewById(R.id.profile_user_qq);
        profile_user_phone = (TextView) findViewById(R.id.profile_user_phone);

        profile_user_contributes = findViewById(R.id.profile_user_contributes);
        profile_user_contributes_text = (TextView) findViewById(R.id.profile_user_contributes_text);
        profile_user_chats = findViewById(R.id.profile_user_chats);
        profile_user_chats_text = (TextView) findViewById(R.id.profile_user_chats_text);
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_profile);
    }

    @Override
    public void startAction() {
        checkUser();
    }
}