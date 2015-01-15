package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.enums.ActivityAnimation;
import com.harreke.easyapp.enums.EnterTransition;
import com.harreke.easyapp.enums.ExitTransition;
import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.helpers.ImageSwitchHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;
import com.harreke.easyapp.widgets.transitions.TransitionLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.apis.API;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.beans.FullRoom;
import air.tv.douyu.android.enums.AuthorizeStatus;
import air.tv.douyu.android.parsers.FullRoomParser;
import air.tv.douyu.android.parsers.Parser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class RoomActivity extends ActivityFramework {
    private EmptyHelper mEmptyHelper;
    private IRequestCallback<String> mFollowAddCallback;
    private IRequestCallback<String> mFollowCheckCallback;
    private IRequestCallback<String> mFollowRemoveCallback;
    private ImageSwitchHelper mFollowSwitcher;
    private FullRoomParser mFullRoomParser;
    private View.OnClickListener mOnClickListener;
    //    private ImageViewInfo mNewImageViewInfo = null;
    //    private ImageViewInfo mOldImageViewInfo = null;
    private View.OnClickListener mOnEmptyClickListener;
    private IRequestCallback<String> mRoomCallback;
    private int mRoomId;
    private ImageView room_avatar;
    private ImageView room_follow_add;
    private ImageView room_follow_remove;
    private View room_follow_root;
    private TextView room_follow_text;
    private TextView room_game_name;
    private TextView room_name;
    private TextView room_nickname;
    private TextView room_online;
    private ImageView room_pic;
    private View room_play_root;
    private View room_share_root;

    //    public static Intent create(Context context, int roomId, ImageView oldView) {
    //        Intent intent = new Intent(context, RoomActivity.class);
    //
    //        intent.putExtra("roomId", roomId);
    //        intent.putExtra("oldViewInfo", GsonUtil.toString(new ImageViewInfo(oldView)));
    //
    //        return intent;
    //    }
    private TextView room_show_details;

    public static Intent create(Context context, int roomId) {
        Intent intent = new Intent(context, RoomActivity.class);

        intent.putExtra("roomId", roomId);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mRoomId = intent.getIntExtra("roomId", 0);

        mFullRoomParser = new FullRoomParser();
        //        mOldImageViewInfo = GsonUtil.toBean(intent.getStringExtra("oldViewInfo"), ImageViewInfo.class);
    }

    private void addFollow() {
        if (!isRequestExecuting()) {
            showToast(R.string.room_follow_adding, true);
            mFollowSwitcher.switchToImageView(room_follow_remove);
            executeRequest(API.getFollowAdd(mRoomId), mFollowAddCallback);
        }
    }

    @Override
    public void attachCallbacks() {
        mEmptyHelper.setOnClickListener(mOnEmptyClickListener);

        RippleOnClickListener.attach(room_play_root, mOnClickListener);
        RippleOnClickListener.attach(room_follow_root, mOnClickListener);
        RippleOnClickListener.attach(room_share_root, mOnClickListener);
    }

    private void checkFollow() {
        mFollowSwitcher.hideAll();
        executeRequest(API.getFollowCheck(mRoomId), mFollowCheckCallback);
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_room);
        addToolbarItem(0, R.string.app_search, R.drawable.image_toolbar_search);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        room_avatar = (ImageView) findViewById(R.id.room_avatar);
        room_name = (TextView) findViewById(R.id.room_name);
        room_nickname = (TextView) findViewById(R.id.room_nickname);
        room_game_name = (TextView) findViewById(R.id.room_game_name);
        room_pic = (ImageView) findViewById(R.id.room_pic);
        room_play_root = findViewById(R.id.room_play_root);
        room_online = (TextView) findViewById(R.id.room_online);
        room_follow_root = findViewById(R.id.room_follow_root);
        room_follow_add = (ImageView) findViewById(R.id.room_follow_add);
        room_follow_remove = (ImageView) findViewById(R.id.room_follow_remove);
        room_follow_text = (TextView) findViewById(R.id.room_follow_text);
        room_share_root = findViewById(R.id.room_share_root);
        room_show_details = (TextView) findViewById(R.id.room_show_details);

        //        ImageLoaderHelper.loadImage(room_pic, mOldImageViewInfo.getImageUrl(), R.drawable.loading_16x9, R.drawable.retry_16x9);

        mEmptyHelper = new EmptyHelper(this);
        mEmptyHelper.showLoading(false);
        mFollowSwitcher = new ImageSwitchHelper(room_follow_add, room_follow_remove);

        RippleDrawable.attach(room_play_root, RippleStyle.Light);
        RippleDrawable.attach(room_follow_root, RippleStyle.Dark_Square);
        RippleDrawable.attach(room_share_root, RippleStyle.Dark_Square);
    }

    @Override
    public void establishCallbacks() {
        mRoomCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mEmptyHelper.showEmptyFailureIdle();
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                mFullRoomParser.parse(result);
                if (mFullRoomParser.getObject() != null) {
                    room_play_root.setVisibility(View.VISIBLE);
                    mEmptyHelper.hide();
                    updateRoom(mFullRoomParser.getObject());
                    if (DouyuTv.getInstance().validateAuthorize() == AuthorizeStatus.Authorized) {
                        checkFollow();
                    }
                } else {
                    mEmptyHelper.showEmptyFailureIdle();
                }
            }
        };
        mFollowCheckCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                int check = Parser.parseInt(result);

                if (check == 1) {
                    mFollowSwitcher.switchToImageView(room_follow_remove);
                } else {
                    mFollowSwitcher.switchToImageView(room_follow_add);
                }
            }
        };
        mFollowAddCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.room_follow_add_failure);
                mFollowSwitcher.switchToImageView(room_follow_add);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                String add = Parser.parseString(result);

                if (add.equals("关注成功")) {
                    showToast(R.string.room_follow_add_success);
                } else {
                    showToast(R.string.room_follow_add_failure);
                    mFollowSwitcher.switchToImageView(room_follow_add);
                }
            }
        };
        mFollowRemoveCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.room_follow_remove_failure);
                mFollowSwitcher.switchToImageView(room_follow_remove);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                String remove = Parser.parseString(result);

                if (remove.equals("取消关注成功")) {
                    showToast(R.string.room_follow_remove_success);
                } else {
                    showToast(R.string.room_follow_remove_failure);
                    mFollowSwitcher.switchToImageView(room_follow_remove);
                }
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.room_play_root:
                        start(PlayerActivity.create(getContext(), mRoomId), ActivityAnimation.Default);
                        break;
                    case R.id.room_follow_root:
                        if (DouyuTv.getInstance().validateAuthorize(getFramework())) {
                            if (room_follow_add.getVisibility() == View.VISIBLE) {
                                addFollow();
                            } else if (room_follow_remove.getVisibility() == View.VISIBLE) {
                                removeFollow();
                            }
                        }
                        break;
                }
            }
        };
        mOnEmptyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAction();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room;
    }

    private void removeFollow() {
        if (!isRequestExecuting()) {
            showToast(R.string.room_follow_removing, true);
            mFollowSwitcher.switchToImageView(room_follow_add);
            executeRequest(API.getFollowRemove(mRoomId), mFollowRemoveCallback);
        }
    }

    @Override
    public void startAction() {
        mEmptyHelper.showLoading();
        executeRequest(API.getRoom(mRoomId), mRoomCallback);
    }

    @Override
    protected void startEnterTransition(TransitionLayout transitionLayout, Object... params) {
        transitionLayout.startEnterTransition(EnterTransition.Slide_In_Bottom);
    }

    @Override
    protected void startExitTransition(TransitionLayout transitionLayout, Object... params) {
        transitionLayout.startExitTransition(ExitTransition.Slide_Out_Bottom);
    }

    private void updateRoom(FullRoom fullRoom) {
        ImageLoaderHelper.loadImage(room_avatar, fullRoom.getOwner_avatar(), R.drawable.avatar, R.drawable.avatar);
        room_name.setText(fullRoom.getRoom_name());
        room_nickname.setText(fullRoom.getNickname());
        room_game_name.setText(fullRoom.getGame_name());
        ImageLoaderHelper.loadImage(room_pic, fullRoom.getRoom_src(), R.drawable.loading_16x9, R.drawable.retry_16x9);
        room_online.setText(getString(R.string.room_online, StringUtil.indentNumber(fullRoom.getOnline())));
        room_follow_text.setText(getString(R.string.room_follow, StringUtil.indentNumber(fullRoom.getFans())));
        room_show_details.setText(fullRoom.getShow_details());
    }

    //    @Override
    //    protected void startEnterTransition(TransitionLayout transitionLayout, Object... params) {
    //        mNewImageViewInfo = new ImageViewInfo(room_pic);
    //        transitionLayout.startEnterTransition(TransitionLayout.EnterTransition.Hero_In, mOldImageViewInfo, mNewImageViewInfo);
    //    }
    //
    //    @Override
    //    protected void startExitTransition(TransitionLayout transitionLayout, Object... params) {
    //        mNewImageViewInfo = new ImageViewInfo(room_pic);
    //        transitionLayout.startExitTransition(TransitionLayout.ExitTransition.Hero_Out, mNewImageViewInfo, mOldImageViewInfo);
    //    }
}