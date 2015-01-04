package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.api.API;
import air.tv.douyu.android.beans.FullRoom;
import air.tv.douyu.android.parsers.FullRoomParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class RoomActivity extends ActivityFramework {
    private EmptyHelper mEmptyHelper;
    //    private ImageViewInfo mNewImageViewInfo = null;
    //    private ImageViewInfo mOldImageViewInfo = null;
    private View.OnClickListener mOnEmptyClickListener;
    private IRequestCallback<String> mRoomCallback;
    private int mRoomId;
    private ImageView room_avatar;
    private TextView room_follow;
    private View room_follow_root;
    private TextView room_game_name;
    private View room_info;
    private TextView room_name;
    private TextView room_nickname;
    private TextView room_online;
    private View room_online_root;
    private ImageView room_pic;
    private View room_play_root;
    private View room_root;
    private View room_share_root;
    private TextView room_show_details;

    //    public static Intent create(Context context, int roomId, ImageView oldView) {
    //        Intent intent = new Intent(context, RoomActivity.class);
    //
    //        intent.putExtra("roomId", roomId);
    //        intent.putExtra("oldViewInfo", GsonUtil.toString(new ImageViewInfo(oldView)));
    //
    //        return intent;
    //    }

    public static Intent create(Context context, int roomId) {
        Intent intent = new Intent(context, RoomActivity.class);

        intent.putExtra("roomId", roomId);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mRoomId = intent.getIntExtra("roomId", 0);

        //        mOldImageViewInfo = GsonUtil.toBean(intent.getStringExtra("oldViewInfo"), ImageViewInfo.class);
    }

    @Override
    public void attachCallbacks() {
        mEmptyHelper.setOnClickListener(mOnEmptyClickListener);
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_room);
        addToolbarItem(0, R.string.app_search, R.drawable.image_toolbar_search);
        setToolbarNavigation();
    }

    private void doUpdate(FullRoom fullRoom) {
        ImageLoaderHelper.loadImage(room_avatar, fullRoom.getOwner_avatar(), R.drawable.avatar, R.drawable.avatar);
        room_name.setText(fullRoom.getRoom_name());
        room_nickname.setText(fullRoom.getNickname());
        room_game_name.setText(fullRoom.getGame_name());
        ImageLoaderHelper.loadImage(room_pic, fullRoom.getRoom_src(), R.drawable.loading_16x9, R.drawable.retry_16x9);
        room_online.setText(getString(R.string.room_online, StringUtil.indentNumber(fullRoom.getOnline())));
        room_follow.setText(getString(R.string.room_follow, StringUtil.indentNumber(fullRoom.getFans())));
        room_show_details.setText(fullRoom.getShow_details());
    }

    @Override
    public void enquiryViews() {
        room_root = findViewById(R.id.room_root);
        room_info = findViewById(R.id.room_info);
        room_avatar = (ImageView) findViewById(R.id.room_avatar);
        room_name = (TextView) findViewById(R.id.room_name);
        room_nickname = (TextView) findViewById(R.id.room_nickname);
        room_game_name = (TextView) findViewById(R.id.room_game_name);
        room_pic = (ImageView) findViewById(R.id.room_pic);
        room_play_root = findViewById(R.id.room_play_root);
        room_online_root = findViewById(R.id.room_online_root);
        room_online = (TextView) findViewById(R.id.room_online);
        room_follow_root = findViewById(R.id.room_follow_root);
        room_follow = (TextView) findViewById(R.id.room_follow);
        room_share_root = findViewById(R.id.room_share_root);
        room_show_details = (TextView) findViewById(R.id.room_show_details);

        //        ImageLoaderHelper.loadImage(room_pic, mOldImageViewInfo.getImageUrl(), R.drawable.loading_16x9, R.drawable.retry_16x9);

        mEmptyHelper = new EmptyHelper(this);

        RippleDrawable.attach(room_info);
        RippleDrawable.attach(room_play_root, RippleDrawable.RIPPLE_STYLE_LIGHT);
        RippleDrawable.attach(room_online_root);
        RippleDrawable.attach(room_follow_root);
        RippleDrawable.attach(room_share_root);
    }

    @Override
    public void establishCallbacks() {
        mRoomCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                mEmptyHelper.showEmptyFailureIdle();
            }

            @Override
            public void onSuccess(String requestUrl, String json) {
                FullRoomParser parser = FullRoomParser.parse(json);

                if (parser != null) {
                    room_play_root.setVisibility(View.VISIBLE);
                    mEmptyHelper.hide();
                    doUpdate(parser.getData());
                } else {
                    mEmptyHelper.showEmptyFailureIdle();
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

    @Override
    protected void postCreate() {
    }

    @Override
    public void startAction() {
        mEmptyHelper.showLoading();
        executeRequest(API.getRoom(mRoomId), mRoomCallback);
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