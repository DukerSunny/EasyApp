package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.helpers.CompoundButtonHelper;
import com.harreke.easyapp.listeners.OnButtonsCheckedChangeListener;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.beans.Setting;
import air.tv.douyu.android.enums.DanmakuLocation;
import air.tv.douyu.android.enums.DanmakuSize;
import air.tv.douyu.android.enums.SwitchDecode;
import air.tv.douyu.android.helpers.ExpandableSettingLayoutHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SettingActivity extends ActivityFramework {
    private ExpandableSettingLayoutHelper mDanmakuLocationExpandHelper;
    private CompoundButtonHelper mDanmakuLocationHelper;
    private ExpandableSettingLayoutHelper mDanmakuOpacityExpandHelper;
    private ExpandableSettingLayoutHelper mDanmakuSizeExpandHelper;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private View.OnClickListener mOnClickListener;
    private OnButtonsCheckedChangeListener mOnButtonsCheckedChangeListener;
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener;
    private Setting mSetting;
    private ExpandableSettingLayoutHelper mSwitchDecodeExpandHelper;
    private CompoundButtonHelper mSwitchDecoderHelper;
    private View setting_live_danmaku_location;
    private RadioButton setting_live_danmaku_location_bottom;
    private RadioButton setting_live_danmaku_location_fullscreen;
    private View setting_live_danmaku_location_layout;
    private View setting_live_danmaku_location_switch;
    private TextView setting_live_danmaku_location_text;
    private RadioButton setting_live_danmaku_location_top;
    private View setting_live_danmaku_opacity;
    private View setting_live_danmaku_opacity_layout;
    private SeekBar setting_live_danmaku_opacity_seekbar;
    private View setting_live_danmaku_opacity_switch;
    private TextView setting_live_danmaku_opacity_text;
    private View setting_live_danmaku_size;
    private View setting_live_danmaku_size_layout;
    private SeekBar setting_live_danmaku_size_seekbar;
    private View setting_live_danmaku_size_switch;
    private TextView setting_live_danmaku_size_text;
    private SwitchCompat setting_live_player_gesture;
    private View setting_support_about_us;
    private View setting_support_check_update;
    private View setting_support_feedback;
    private SwitchCompat setting_system_auto_load_more;
    private SwitchCompat setting_system_play_video_under_mobile_network;
    private SwitchCompat setting_system_receive_push;
    private SwitchCompat setting_system_show_empty_category;
    private View setting_system_switch_decode;
    private RadioButton setting_system_switch_decode_hw;
    private View setting_system_switch_decode_layout;
    private RadioButton setting_system_switch_decode_sw;
    private View setting_system_switch_decode_switch;
    private TextView setting_system_switch_decode_text;

    public static Intent create(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mSetting = DouyuTv.getInstance().readSetting();
    }

    @Override
    public void attachCallbacks() {
        setting_system_auto_load_more.setOnCheckedChangeListener(mOnCheckedChangeListener);
        setting_system_show_empty_category.setOnCheckedChangeListener(mOnCheckedChangeListener);
        setting_system_receive_push.setOnCheckedChangeListener(mOnCheckedChangeListener);
        setting_system_play_video_under_mobile_network.setOnCheckedChangeListener(mOnCheckedChangeListener);
        setting_system_switch_decode.setOnClickListener(mOnClickListener);
        mSwitchDecoderHelper.setOnButtonsCheckedChangeListener(mOnButtonsCheckedChangeListener);
        setting_live_player_gesture.setOnCheckedChangeListener(mOnCheckedChangeListener);
        setting_live_danmaku_opacity.setOnClickListener(mOnClickListener);
        setting_live_danmaku_opacity_seekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        setting_live_danmaku_size.setOnClickListener(mOnClickListener);
        setting_live_danmaku_size_seekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        setting_live_danmaku_location.setOnClickListener(mOnClickListener);
        mDanmakuLocationHelper.setOnButtonsCheckedChangeListener(mOnButtonsCheckedChangeListener);
        setting_support_check_update.setOnClickListener(mOnClickListener);
        setting_support_feedback.setOnClickListener(mOnClickListener);
        setting_support_about_us.setOnClickListener(mOnClickListener);
    }

    private void checkUpdate() {
        // TODO 检查更新
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_setting);
        enableDefaultToolbarNavigation();
    }

    private void doFeedBack() {
        // TODO 反馈
    }

    @Override
    public void enquiryViews() {
        setting_system_auto_load_more = (SwitchCompat) findViewById(R.id.setting_system_auto_load_more);
        setting_system_show_empty_category = (SwitchCompat) findViewById(R.id.setting_system_show_empty_category);
        setting_system_receive_push = (SwitchCompat) findViewById(R.id.setting_system_receive_push);
        setting_system_play_video_under_mobile_network =
                (SwitchCompat) findViewById(R.id.setting_system_play_video_under_mobile_network);
        setting_system_switch_decode = findViewById(R.id.setting_system_switch_decode);
        setting_system_switch_decode_text = (TextView) findViewById(R.id.setting_system_switch_decode_text);
        setting_system_switch_decode_switch = findViewById(R.id.setting_system_switch_decode_switch);
        setting_system_switch_decode_layout = findViewById(R.id.setting_system_switch_decode_layout);
        setting_system_switch_decode_hw = (RadioButton) findViewById(R.id.setting_system_switch_decode_hw);
        setting_system_switch_decode_sw = (RadioButton) findViewById(R.id.setting_system_switch_decode_sw);
        setting_live_player_gesture = (SwitchCompat) findViewById(R.id.setting_live_player_gesture);
        setting_live_danmaku_opacity = findViewById(R.id.setting_live_danmaku_opacity);
        setting_live_danmaku_opacity_text = (TextView) findViewById(R.id.setting_live_danmaku_opacity_text);
        setting_live_danmaku_opacity_switch = findViewById(R.id.setting_live_danmaku_opacity_switch);
        setting_live_danmaku_opacity_layout = findViewById(R.id.setting_live_danmaku_opacity_layout);
        setting_live_danmaku_opacity_seekbar = (SeekBar) findViewById(R.id.setting_live_danmaku_opacity_seekbar);
        setting_live_danmaku_size = findViewById(R.id.setting_live_danmaku_size);
        setting_live_danmaku_size_text = (TextView) findViewById(R.id.setting_live_danmaku_size_text);
        setting_live_danmaku_size_switch = findViewById(R.id.setting_live_danmaku_size_switch);
        setting_live_danmaku_size_layout = findViewById(R.id.setting_live_danmaku_size_layout);
        setting_live_danmaku_size_seekbar = (SeekBar) findViewById(R.id.setting_live_danmaku_size_seekbar);
        setting_live_danmaku_location = findViewById(R.id.setting_live_danmaku_location);
        setting_live_danmaku_location_text = (TextView) findViewById(R.id.setting_live_danmaku_location_text);
        setting_live_danmaku_location_switch = findViewById(R.id.setting_live_danmaku_location_switch);
        setting_live_danmaku_location_layout = findViewById(R.id.setting_live_danmaku_location_layout);
        setting_live_danmaku_location_top = (RadioButton) findViewById(R.id.setting_live_danmaku_location_top);
        setting_live_danmaku_location_bottom = (RadioButton) findViewById(R.id.setting_live_danmaku_location_bottom);
        setting_live_danmaku_location_fullscreen = (RadioButton) findViewById(R.id.setting_live_danmaku_location_fullscreen);
        setting_support_check_update = findViewById(R.id.setting_support_check_update);
        setting_support_feedback = findViewById(R.id.setting_support_feedback);
        setting_support_about_us = findViewById(R.id.setting_support_about_us);

        mSwitchDecoderHelper = new CompoundButtonHelper(setting_system_switch_decode_hw, setting_system_switch_decode_sw);
        mDanmakuLocationHelper = new CompoundButtonHelper(setting_live_danmaku_location_top, setting_live_danmaku_location_bottom,
                setting_live_danmaku_location_fullscreen);

        mSwitchDecodeExpandHelper =
                new ExpandableSettingLayoutHelper(setting_system_switch_decode_switch, setting_system_switch_decode_layout,
                        (int) (ApplicationFramework.Density * 56));
        mDanmakuOpacityExpandHelper =
                new ExpandableSettingLayoutHelper(setting_live_danmaku_opacity_switch, setting_live_danmaku_opacity_layout,
                        (int) (ApplicationFramework.Density * 48));
        mDanmakuSizeExpandHelper =
                new ExpandableSettingLayoutHelper(setting_live_danmaku_size_switch, setting_live_danmaku_size_layout,
                        (int) (ApplicationFramework.Density * 48));
        mDanmakuLocationExpandHelper =
                new ExpandableSettingLayoutHelper(setting_live_danmaku_location_switch, setting_live_danmaku_location_layout,
                        (int) (ApplicationFramework.Density * 56));

        RippleDrawable.attach(setting_system_auto_load_more);
        RippleDrawable.attach(setting_system_show_empty_category);
        RippleDrawable.attach(setting_system_receive_push);
        RippleDrawable.attach(setting_system_play_video_under_mobile_network);
        RippleDrawable.attach(setting_system_switch_decode);
        RippleDrawable.attach(setting_live_player_gesture);
        RippleDrawable.attach(setting_live_danmaku_opacity);
        RippleDrawable.attach(setting_live_danmaku_size);
        RippleDrawable.attach(setting_live_danmaku_location);
        RippleDrawable.attach(setting_support_check_update);
        RippleDrawable.attach(setting_support_feedback);
        RippleDrawable.attach(setting_support_about_us);

        setting_system_auto_load_more.setChecked(mSetting.isAutoLoadMore());
        setting_system_show_empty_category.setChecked(mSetting.isShowEmptyCategory());
        setting_system_receive_push.setChecked(mSetting.isReceivePush());
        setting_system_play_video_under_mobile_network.setChecked(mSetting.isPlayVideoUnderMobileNetwork());
        setting_system_switch_decode_text.setText(mSetting.getSwitchDecode().getTextId());
        mSwitchDecoderHelper.check(SwitchDecode.indexOf(mSetting.getSwitchDecode()));
        setting_live_danmaku_opacity_text.setText(mSetting.getDanmakuOpacity() * 100 / 255 + "%");
        setting_live_danmaku_opacity_seekbar.setProgress(mSetting.getDanmakuOpacity() - 38);
        setting_live_danmaku_size_text.setText(String.valueOf(mSetting.getDanmakuSize().getSize()));
        setting_live_danmaku_size_seekbar.setProgress(DanmakuSize.indexOf(mSetting.getDanmakuSize()));
        setting_live_danmaku_location_text.setText(mSetting.getDanmakuLocation().getTextId());
        mDanmakuLocationHelper.check(DanmakuLocation.indexOf(mSetting.getDanmakuLocation()));
        mSwitchDecodeExpandHelper.hide(false);
        mDanmakuOpacityExpandHelper.hide(false);
        mDanmakuSizeExpandHelper.hide(false);
        mDanmakuLocationExpandHelper.hide(false);
    }

    @Override
    public void establishCallbacks() {
        mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switch (buttonView.getId()) {
                    case R.id.setting_system_auto_load_more:
                        mSetting.setAutoLoadMore(isChecked);
                        break;
                    case R.id.setting_system_show_empty_category:
                        mSetting.setShowEmptyCategory(isChecked);
                        break;
                    case R.id.setting_system_receive_push:
                        mSetting.setReceivePush(isChecked);
                        break;
                    case R.id.setting_system_play_video_under_mobile_network:
                        mSetting.setPlayVideoUnderMobileNetwork(isChecked);
                        break;
                    case R.id.setting_live_player_gesture:
                        mSetting.setPlayVideoUnderMobileNetwork(isChecked);
                }
                writeSetting();
            }
        };
        mOnButtonsCheckedChangeListener = new OnButtonsCheckedChangeListener() {
            @Override
            public void onButtonCheck(CompoundButton compoundButton, int position) {
                switch (compoundButton.getId()) {
                    case R.id.setting_system_switch_decode_hw:
                        mSetting.setSwitchDecode(SwitchDecode.HW);
                        setting_system_switch_decode_text.setText(SwitchDecode.HW.getTextId());
                        break;
                    case R.id.setting_system_switch_decode_sw:
                        mSetting.setSwitchDecode(SwitchDecode.SW);
                        setting_system_switch_decode_text.setText(SwitchDecode.SW.getTextId());
                        break;
                    case R.id.setting_live_danmaku_location_top:
                        mSetting.setDanmakuLocation(DanmakuLocation.Top);
                        setting_live_danmaku_location_text.setText(DanmakuLocation.Top.getTextId());
                        break;
                    case R.id.setting_live_danmaku_location_bottom:
                        mSetting.setDanmakuLocation(DanmakuLocation.Bottom);
                        setting_live_danmaku_location_text.setText(DanmakuLocation.Bottom.getTextId());
                        break;
                    case R.id.setting_live_danmaku_location_fullscreen:
                        mSetting.setDanmakuLocation(DanmakuLocation.FullScreen);
                        setting_live_danmaku_location_text.setText(DanmakuLocation.FullScreen.getTextId());
                        break;
                }
                writeSetting();
            }
        };
        mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DanmakuSize danmakuSize;
                int value;

                switch (seekBar.getId()) {
                    case R.id.setting_live_danmaku_opacity_seekbar:
                        value = progress + 38;
                        mSetting.setDanmakuOpacity(value);
                        setting_live_danmaku_opacity_text.setText((value) * 100 / 255 + "%");
                        break;
                    case R.id.setting_live_danmaku_size_seekbar:
                        danmakuSize = DanmakuSize.get(progress);
                        mSetting.setDanmakuSize(danmakuSize);
                        setting_live_danmaku_size_text.setText(String.valueOf(danmakuSize.getSize()));
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                writeSetting();
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.setting_system_switch_decode:
                        toggleSwitchDecodeLayout();
                        break;
                    case R.id.setting_live_danmaku_opacity:
                        toggleDanmakuOpacityLayout();
                        break;
                    case R.id.setting_live_danmaku_size:
                        toggleDanmakuSizeLayout();
                        break;
                    case R.id.setting_live_danmaku_location:
                        toggleDanmakuLocationLayout();
                        break;
                    case R.id.setting_support_check_update:
                        checkUpdate();
                        break;
                    case R.id.setting_support_feedback:
                        doFeedBack();
                        break;
                    case R.id.setting_support_about_us:
                        showAboutUs();
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        start(SearchActivity.create(this));

        return false;
    }

    private void showAboutUs() {
        // TODO 关于我们
    }

    @Override
    public void startAction() {
    }

    private void toggleDanmakuLocationLayout() {
        mDanmakuLocationExpandHelper.toggle();
    }

    private void toggleDanmakuOpacityLayout() {
        mDanmakuOpacityExpandHelper.toggle();
    }

    private void toggleDanmakuSizeLayout() {
        mDanmakuSizeExpandHelper.toggle();
    }

    private void toggleSwitchDecodeLayout() {
        mSwitchDecodeExpandHelper.toggle();
    }

    private void writeSetting() {
        DouyuTv.getInstance().writeSetting(mSetting);
    }
}