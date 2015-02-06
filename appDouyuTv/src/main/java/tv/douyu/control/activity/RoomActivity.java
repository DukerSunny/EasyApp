package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.helpers.ConnectionHelper;
import com.harreke.easyapp.helpers.EmptyHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.helpers.ViewSwitchHelper;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.widgets.animators.ViewValueAnimator;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.FullRoom;
import tv.douyu.model.bean.Setting;
import tv.douyu.model.enumeration.AuthorizeStatus;
import tv.douyu.model.parser.FullRoomParser;
import tv.douyu.wrapper.helper.AuthorizeHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/29
 */
public class RoomActivity extends ActivityFramework {
    private AuthorizeHelper mAuthorizeHelper;
    private UMSocialService mController;
    private EmptyHelper mEmptyHelper;
    private IRequestCallback<String> mFollowAddCallback;
    private IRequestCallback<String> mFollowCheckCallback;
    private IRequestCallback<String> mFollowRemoveCallback;
    private ViewSwitchHelper mFollowSwitcher;
    private FullRoom mFullRoom = null;
    private FullRoomParser mFullRoomParser;
    private View.OnClickListener mOnClickListener;
    private View.OnClickListener mOnEmptyClickListener;
    private MaterialDialog.ButtonCallback mPlayCallback;
    private MaterialDialog mPlayDialog;
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
    private View room_share_root;
    private TextView room_show_details;
    @InjectView(R.id.room_play_root)
    View room_play_root;
    @InjectView(R.id.room_root)
    View room_root;

    public static Intent create(Context context, int roomId) {
        Intent intent = new Intent(context, RoomActivity.class);

        intent.putExtra("roomId", roomId);

        return intent;
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mRoomId = intent.getIntExtra("roomId", 0);

        mFullRoomParser = new FullRoomParser();

        initShare();
    }

    private void addFollow() {
        if (!isRequestExecuting()) {
            showToast(R.string.room_follow_adding, true);
            mFollowSwitcher.switchToView(room_follow_remove);
            executeRequest(API.getFollowAdd(mRoomId), mFollowAddCallback);
        }
    }

    @Override
    public void attachCallbacks() {
        //        mEmptyHelper.setOnClickListener(mOnEmptyClickListener);

        RippleOnClickListener.attach(room_follow_root, mOnClickListener);
        //        RippleOnClickListener.attach(room_share_root, mOnClickListener);
    }

    private void checkFollow() {
        mFollowSwitcher.hideAll(false);
        if (DouyuTv.getInstance().validateAuthorize() == AuthorizeStatus.Authorized) {
            executeRequest(API.getFollowCheck(mRoomId), mFollowCheckCallback);
        }
    }

    //    @Override
    //    protected void configActivity() {
    //        attachTransition(new SwipeToFinishLayout(this));
    //    }

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
        room_online = (TextView) findViewById(R.id.room_online);
        room_follow_root = findViewById(R.id.room_follow_root);
        room_follow_add = (ImageView) findViewById(R.id.room_follow_add);
        room_follow_remove = (ImageView) findViewById(R.id.room_follow_remove);
        room_follow_text = (TextView) findViewById(R.id.room_follow_text);
        room_share_root = findViewById(R.id.room_share_root);
        room_show_details = (TextView) findViewById(R.id.room_show_details);

        //        ImageLoaderHelper.loadImage(room_pic, mOldImageViewInfo.getImageUrl(), R.drawable.loading_16x9, R.drawable.retry_16x9);

        mPlayDialog = new MaterialDialog.Builder(this).title(R.string.room_play).content(R.string.room_play_sure)
                .positiveText(R.string.app_ok).negativeText(R.string.app_cancel).callback(mPlayCallback).build();

        mAuthorizeHelper = new AuthorizeHelper(this, 0);
        //        mEmptyHelper = new EmptyHelper(this);
        //        mEmptyHelper.showLoading(false);
        mFollowSwitcher = new ViewSwitchHelper(room_follow_add, room_follow_remove);

        RippleDrawable.attach(room_play_root, RippleStyle.Light);
        RippleDrawable.attach(room_follow_root, RippleStyle.Dark_Square);
        RippleDrawable.attach(room_share_root, RippleStyle.Dark_Square);

        room_root.setVisibility(View.INVISIBLE);
        ViewHelper.setAlpha(room_root, 0f);
    }

    @Override
    public void establishCallbacks() {
        mRoomCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                //                mEmptyHelper.showEmptyFailureIdle();
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<FullRoom> objectResult = mFullRoomParser.parse(result);

                if (mFullRoomParser != null) {
                    mFullRoom = objectResult.getObject();
                    if (mFullRoom != null) {
                        room_play_root.setVisibility(View.VISIBLE);
                        //                        mEmptyHelper.hide();
                        updateRoom(mFullRoom);
                        checkFollow();
                    } else {
                        //                        mEmptyHelper.showEmptyFailureIdle();
                    }
                } else {
                    //                    mEmptyHelper.showEmptyFailureIdle();
                }
            }
        };
        mFollowCheckCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<Integer> objectResult = Parser.parseInt(result, "error", "data", "data");

                if (objectResult != null && objectResult.getObject() != null) {
                    if (objectResult.getObject() == 1) {
                        mFollowSwitcher.switchToView(room_follow_remove);
                    } else {
                        mFollowSwitcher.switchToView(room_follow_add);
                    }
                }
            }
        };
        mFollowAddCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.room_follow_add_failure);
                mFollowSwitcher.switchToView(room_follow_add);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<String> objectResult = Parser.parseString(result, "error", "data", "data");

                if (objectResult != null && objectResult.getObject() != null && objectResult.getObject().equals("关注成功")) {
                    showToast(R.string.room_follow_add_success);
                } else {
                    showToast(R.string.room_follow_add_failure);
                    mFollowSwitcher.switchToView(room_follow_add);
                }
            }
        };
        mFollowRemoveCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.room_follow_remove_failure);
                mFollowSwitcher.switchToView(room_follow_remove);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<String> objectResult = Parser.parseString(result, "error", "data", "data");

                if (objectResult != null && objectResult.getObject() != null && objectResult.getObject().equals("取消关注成功")) {
                    showToast(R.string.room_follow_remove_success);
                } else {
                    showToast(R.string.room_follow_remove_failure);
                    mFollowSwitcher.switchToView(room_follow_remove);
                }
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.room_play_root:

                        break;
                    case R.id.room_follow_root:
                        if (mAuthorizeHelper.validate(true)) {
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
        mPlayCallback = new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                openVideo();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_room;
    }

    private void initShare() {
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        mController.getConfig().removePlatform(SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
        String appID = "wx6be84d532f192698";
        String appSecret = "50a9c65160dcc4d22c0de3c88ae888a7";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(this, appID, appSecret);
        wxHandler.addToSocialSDK();
        wxHandler.showCompressToast(false);
        // 添加微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(this, appID, appSecret);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.showCompressToast(false);
        wxCircleHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1102007514", "2nfTkdPuJEv2EnYm");
        qqSsoHandler.addToSocialSDK();

        //参数1为当前Activity，参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "1102007514", "2nfTkdPuJEv2EnYm");
        qZoneSsoHandler.addToSocialSDK();

        mController.getConfig().setSsoHandler(new SinaSsoHandler());

        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        checkFollow();
    }

    @Override
    protected void onDestroy() {
        mAuthorizeHelper.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(this));

        return false;
    }

    @OnClick(R.id.room_play_root)
    void onPlayRootClick() {
        Setting setting = DouyuTv.getInstance().getSetting();

        if (!setting.isPlayVideoUnderMobileNetwork() && ConnectionHelper.mobileConnected && !ConnectionHelper.wifiConnected) {
            mPlayDialog.show();
        } else {
            openVideo();
        }
    }

    @OnClick(R.id.room_share_root)
    void onShareClick() {
        String shareUrl;

        if (mFullRoom != null) {
            shareUrl = "http://www.douyu.tv" + mFullRoom.getUrl();
            mController.setShareContent(
                    shareUrl + "   我正在  " + mFullRoom.getRoom_name() + "  的房间观看直播  / 主播" + mFullRoom.getNickname() +
                            ", 欢迎大家前来围观 / 来自#斗鱼#游戏直播!");
            UMVideo umVideo = new UMVideo(shareUrl);
            //设置视频缩略图
            umVideo.setThumb(mFullRoom.getRoom_src().replace("&size=small", ""));
            umVideo.setTitle(mFullRoom.getRoom_name());
            mController.setShareMedia(umVideo);

            mController.openShare(this, false);
        }
    }

    private void openVideo() {
        start(PlayerActivity.create(getContext(), mRoomId));
    }

    private void removeFollow() {
        if (!isRequestExecuting()) {
            showToast(R.string.room_follow_removing, true);
            mFollowSwitcher.switchToView(room_follow_add);
            executeRequest(API.getFollowRemove(mRoomId), mFollowRemoveCallback);
        }
    }

    @Override
    public void startAction() {
        room_root.setVisibility(View.VISIBLE);
        ViewValueAnimator.animate(room_root).alpha(1f).start(true);
        //        mEmptyHelper.showLoading();
        executeRequest(API.getRoom(mRoomId), mRoomCallback);
    }

    //    @Override
    //    protected void startEnterTransition(TransitionLayout transitionLayout, Object... params) {
    //        transitionLayout.startEnterTransition(EnterTransition.Slide_In_Bottom);
    //    }
    //
    //    @Override
    //    protected void startExitTransition(TransitionLayout transitionLayout, Object... params) {
    //        transitionLayout.startExitTransition(ExitTransition.Slide_Out_Bottom);
    //    }

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