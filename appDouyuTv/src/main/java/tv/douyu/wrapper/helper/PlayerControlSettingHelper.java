package tv.douyu.wrapper.helper;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.harreke.easyapp.frameworks.IFramework;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import tv.douyu.R;
import tv.douyu.model.bean.Setting;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlSettingHelper
        implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private ToggleViewValueAnimator mMaskAnimator;
    private ToggleViewValueAnimator mOverlayAnimator;
    private boolean mShowing = false;
    private View player_control_setting;
    private View player_control_setting_mask;
    private View player_control_setting_overlay;
    private RadioGroup player_danmaku_density;
    private SeekBar player_danmaku_opacity;
    private RadioGroup player_danmaku_size;
    private SeekBar player_screen_brightness;
    private RadioGroup player_screen_ratio;

    public PlayerControlSettingHelper(IFramework framework, Setting setting) {
        player_control_setting = framework.findViewById(R.id.player_control_setting);
        player_control_setting_mask = framework.findViewById(R.id.player_control_setting_mask);
        player_control_setting_overlay = framework.findViewById(R.id.player_control_setting_overlay);
        player_screen_brightness = (SeekBar) framework.findViewById(R.id.player_screen_brightness);
        player_danmaku_opacity = (SeekBar) framework.findViewById(R.id.player_danmaku_opacity);
        player_danmaku_density = (RadioGroup) framework.findViewById(R.id.player_danmaku_density);
        player_danmaku_size = (RadioGroup) framework.findViewById(R.id.player_danmaku_size);
        player_screen_ratio = (RadioGroup) framework.findViewById(R.id.player_screen_ratio);

        mMaskAnimator = ToggleViewValueAnimator.animate(player_control_setting_mask);
        mOverlayAnimator = ToggleViewValueAnimator.animate(player_control_setting_overlay);

        player_danmaku_density.setOnCheckedChangeListener(this);
        player_danmaku_size.setOnCheckedChangeListener(this);
        player_screen_ratio.setOnCheckedChangeListener(this);
        player_control_setting_mask.setOnClickListener(this);
        player_screen_brightness.setOnSeekBarChangeListener(this);
        player_danmaku_opacity.setOnSeekBarChangeListener(this);

        player_screen_brightness.setProgress(217);
        player_danmaku_opacity.setProgress(setting.getDanmakuOpacity() - 38);

        player_control_setting.post(new Runnable() {
            @Override
            public void run() {
                mMaskAnimator.alphaOff(0f).alphaOn(1f).visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                mOverlayAnimator.yOff(-player_control_setting_overlay.getMeasuredHeight())
                        .yOn(ViewHelper.getY(player_control_setting_overlay)).alphaOff(0f).alphaOn(1f).visibilityOff(View.GONE)
                        .visibilityOn(View.VISIBLE);
                hide(false);
            }
        });
    }

    public void hide(boolean animate) {
        mShowing = false;
        mMaskAnimator.toggleOff(animate);
        mOverlayAnimator.toggleOff(animate);
    }

    public boolean isShowing() {
        return mShowing;
    }

    protected abstract void onBrightnessChange(int value);

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.player_danmaku_density:
                onDanmakuDensityChange(ViewUtil.findChild(group, checkedId));
                break;
            case R.id.player_danmaku_size:
                onDanmakuSizeChange(ViewUtil.findChild(group, checkedId));
                break;
            case R.id.player_screen_ratio:
                onScreenRatioChange(ViewUtil.findChild(group, checkedId));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_control_setting_mask:
                hide(true);
                onMaskClick();
        }
    }

    protected abstract void onDanmakuDensityChange(int index);

    protected abstract void onDanmakuOpacityChange(int value);

    protected abstract void onDanmakuSizeChange(int index);

    protected abstract void onMaskClick();

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    protected abstract void onScreenRatioChange(int index);

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        switch (seekBar.getId()) {
            case R.id.player_screen_brightness:
                onBrightnessChange(seekBar.getProgress());
                break;
            case R.id.player_danmaku_opacity:
                onDanmakuOpacityChange(seekBar.getProgress());
                break;
        }
    }

    public void show(boolean animate) {
        mShowing = true;
        mMaskAnimator.toggleOn(animate);
        mOverlayAnimator.toggleOn(animate);
    }
}