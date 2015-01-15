package air.tv.douyu.android.helpers;

import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.listeners.OnButtonsCheckedChangeListener;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import air.tv.douyu.android.R;
import air.tv.douyu.android.beans.Setting;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/14
 */
public abstract class PlayerControlHelper {
    private Handler mHandler = new Handler();
    private Runnable mHidePlayerControlRunnable = new Runnable() {
        @Override
        public void run() {
            startControlOut();
        }
    };
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.player_back:
                    onPlayerBackClick();
                    break;
                case R.id.player_control:
                    onPlayControlClick();
                    break;
                case R.id.player_setting_overlay_mask:
                    onSettingOverlayMaskClick();
                    break;
                case R.id.player_setting:
                    onPlayerSettingClick();
                    break;
            }
            if (isPlayerControlShowing()) {
                mHandler.removeCallbacks(mHidePlayerControlRunnable);
                mHandler.postDelayed(mHidePlayerControlRunnable, 6000l);
            }
        }
    };
    private RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
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
    };
    private OnButtonsCheckedChangeListener mOnDanmakuDensityButtonsCheckedChangeListener =
            new OnButtonsCheckedChangeListener() {
                @Override
                public void onButtonCheck(CompoundButton compoundButton, int position) {
                    onDanmakuDensityChange(position);
                }
            };
    private OnButtonsCheckedChangeListener mOnDanmakuSizeButtonsCheckedChangeListener = new OnButtonsCheckedChangeListener() {
        @Override
        public void onButtonCheck(CompoundButton compoundButton, int position) {
            onDanmakuSizeChange(position);
        }
    };
    private OnButtonsCheckedChangeListener mOnScreenRatioButtonsCheckedChangeListener = new OnButtonsCheckedChangeListener() {
        @Override
        public void onButtonCheck(CompoundButton compoundButton, int position) {
            onScreenRatioChange(position);
        }
    };
    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

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
            }
        }
    };
    private ViewPropertyAnimator mPlayerControlBottomAnimator;
    private Animator.AnimatorListener mPlayerControlBottomInListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            player_control_bottom.setVisibility(View.VISIBLE);
        }
    };
    private Animator.AnimatorListener mPlayerControlBottomOutListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            player_control_bottom.setVisibility(View.GONE);
        }
    };
    private boolean mPlayerControlShowing = false;
    private ViewPropertyAnimator mPlayerControlTopAnimator;
    private Animator.AnimatorListener mPlayerControlTopInListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            player_control_top.setVisibility(View.VISIBLE);
        }
    };
    private Animator.AnimatorListener mPlayerControlTopOutListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            player_control_top.setVisibility(View.GONE);
        }
    };
    private ViewPropertyAnimator mPlayerSettingOverlayAnimator;
    private Animator.AnimatorListener mPlayerSettingOverlayInListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            player_setting_overlay.setVisibility(View.VISIBLE);
        }
    };
    private ViewPropertyAnimator mPlayerSettingOverlayMaskAnimator;
    private Animator.AnimatorListener mPlayerSettingOverlayMaskInListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationStart(Animator animation) {
            player_setting_overlay_mask.setVisibility(View.VISIBLE);
        }
    };
    private Animator.AnimatorListener mPlayerSettingOverlayMaskOutListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            player_setting_overlay_mask.setVisibility(View.GONE);
        }
    };
    private Animator.AnimatorListener mPlayerSettingOverlayOutListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            player_setting_overlay.setVisibility(View.GONE);
        }
    };
    private boolean mPlayerSettingShowing = false;
    private View player_back;
    private View player_control;
    private View player_control_bottom;
    private View player_control_top;
    private RadioGroup player_danmaku_density;
    //    private CompoundButton player_danmaku_density_high;
    //    private CompoundButton player_danmaku_density_low;
    //    private CompoundButton player_danmaku_density_middle;
    //    private CompoundButton player_danmaku_density_unlimited;
    private SeekBar player_danmaku_opacity;
    private RadioGroup player_danmaku_size;
    //    private CompoundButton player_danmaku_size_huge;
    //    private CompoundButton player_danmaku_size_large;
    //    private CompoundButton player_danmaku_size_normal;
    //    private CompoundButton player_danmaku_size_small;
    private SeekBar player_screen_brightness;
    private RadioGroup player_screen_ratio;
    //    private CompoundButton player_screen_ratio_16x9;
    //    private CompoundButton player_screen_ratio_4x3;
    //    private CompoundButton player_screen_ratio_auto;
    //    private CompoundButton player_screen_ratio_fullscreen;
    private View player_setting;
    private View player_setting_overlay;
    private View player_setting_overlay_mask;
    private TextView player_title;

    public PlayerControlHelper(IFramework framework, Setting setting) {
        player_back = framework.findViewById(R.id.player_back);
        player_title = (TextView) framework.findViewById(R.id.player_title);
        player_setting = framework.findViewById(R.id.player_setting);
        player_control = framework.findViewById(R.id.player_control);
        player_control_top = framework.findViewById(R.id.player_control_top);
        player_control_bottom = framework.findViewById(R.id.player_control_bottom);
        player_setting_overlay_mask = framework.findViewById(R.id.player_setting_overlay_mask);
        player_setting_overlay = framework.findViewById(R.id.player_setting_overlay);
        player_screen_brightness = (SeekBar) framework.findViewById(R.id.player_screen_brightness);
        player_danmaku_opacity = (SeekBar) framework.findViewById(R.id.player_danmaku_opacity);
        player_danmaku_density = (RadioGroup) framework.findViewById(R.id.player_danmaku_density);
        player_danmaku_size = (RadioGroup) framework.findViewById(R.id.player_danmaku_size);
        player_screen_ratio = (RadioGroup) framework.findViewById(R.id.player_screen_ratio);

        //        player_danmaku_density_low = (CompoundButton) framework.findViewById(R.id.player_danmaku_density_low);
        //        player_danmaku_density_middle = (CompoundButton) framework.findViewById(R.id.player_danmaku_density_middle);
        //        player_danmaku_density_high = (CompoundButton) framework.findViewById(R.id.player_danmaku_density_high);
        //        player_danmaku_density_unlimited = (CompoundButton) framework.findViewById(R.id.player_danmaku_density_unlimited);
        //        player_danmaku_size_small = (CompoundButton) framework.findViewById(R.id.player_danmaku_size_small);
        //        player_danmaku_size_normal = (CompoundButton) framework.findViewById(R.id.player_danmaku_size_normal);
        //        player_danmaku_size_large = (CompoundButton) framework.findViewById(R.id.player_danmaku_size_large);
        //        player_danmaku_size_huge = (CompoundButton) framework.findViewById(R.id.player_danmaku_size_huge);
        //        player_screen_ratio_16x9 = (CompoundButton) framework.findViewById(R.id.player_screen_ratio_16x9);
        //        player_screen_ratio_4x3 = (CompoundButton) framework.findViewById(R.id.player_screen_ratio_4x3);
        //        player_screen_ratio_auto = (CompoundButton) framework.findViewById(R.id.player_screen_ratio_auto);
        //        player_screen_ratio_fullscreen = (CompoundButton) framework.findViewById(R.id.player_screen_ratio_fullscreen);

        mPlayerControlTopAnimator = ViewPropertyAnimator.animate(player_control_top);
        mPlayerControlBottomAnimator = ViewPropertyAnimator.animate(player_control_bottom);
        mPlayerSettingOverlayMaskAnimator = ViewPropertyAnimator.animate(player_setting_overlay_mask);
        mPlayerSettingOverlayAnimator = ViewPropertyAnimator.animate(player_setting_overlay);

        player_danmaku_density.setOnCheckedChangeListener(mOnCheckedChangeListener);
        player_danmaku_size.setOnCheckedChangeListener(mOnCheckedChangeListener);
        player_screen_ratio.setOnCheckedChangeListener(mOnCheckedChangeListener);

        //        mDanmakuDensityHelper =
        //                new CompoundButtonHelper(player_danmaku_density_low, player_danmaku_density_middle, player_danmaku_density_high,
        //                        player_danmaku_density_unlimited);
        //        mDanmakuSizeHelper =
        //                new CompoundButtonHelper(player_danmaku_size_small, player_danmaku_size_normal, player_danmaku_size_large,
        //                        player_danmaku_size_huge);
        //        mScreenRatioHelper =
        //                new CompoundButtonHelper(player_screen_ratio_16x9, player_screen_ratio_4x3, player_screen_ratio_auto,
        //                        player_screen_ratio_fullscreen);

        RippleDrawable.attach(player_back, RippleStyle.Light_Square);
        RippleDrawable.attach(player_setting, RippleStyle.Light_Square);

        player_screen_brightness.setProgress(217);
        player_danmaku_opacity.setProgress(setting.getDanmakuOpacity() - 38);
        //        mDanmakuDensityHelper.check(DanmakuDensity.indexOf(setting.getDanmakuDensity()));
        //        mDanmakuSizeHelper.check(DanmakuSize.indexOf(setting.getDanmakuSize()));
        //        mScreenRatioHelper.check(2);

        player_control.setOnClickListener(mOnClickListener);
        player_setting_overlay_mask.setOnClickListener(mOnClickListener);
        player_setting_overlay.setOnClickListener(mOnClickListener);
        player_screen_brightness.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        player_danmaku_opacity.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
        //        mDanmakuDensityHelper.setOnButtonsCheckedChangeListener(mOnDanmakuDensityButtonsCheckedChangeListener);
        //        mDanmakuSizeHelper.setOnButtonsCheckedChangeListener(mOnDanmakuSizeButtonsCheckedChangeListener);
        //        mScreenRatioHelper.setOnButtonsCheckedChangeListener(mOnScreenRatioButtonsCheckedChangeListener);

        RippleOnClickListener.attach(player_back, mOnClickListener);
        RippleOnClickListener.attach(player_setting, mOnClickListener);

        player_control_top.setVisibility(View.INVISIBLE);
        player_control_bottom.setVisibility(View.INVISIBLE);
        player_setting_overlay_mask.setVisibility(View.GONE);
        player_setting_overlay.setVisibility(View.INVISIBLE);
    }

    public boolean isPlayerControlShowing() {
        return mPlayerControlShowing;
    }

    public boolean isPlayerSettingShowing() {
        return mPlayerSettingShowing;
    }

    protected abstract void onBrightnessChange(int progress);

    protected abstract void onDanmakuDensityChange(int index);

    protected abstract void onDanmakuOpacityChange(int value);

    protected abstract void onDanmakuSizeChange(int index);

    private void onPlayControlClick() {
        if (isPlayerControlShowing()) {
            startControlOut();
        } else {
            startControlIn();
        }
    }

    protected abstract void onPlayerBackClick();

    private void onPlayerSettingClick() {
        startSettingIn();
    }

    protected abstract void onScreenRatioChange(int index);

    private void onSettingOverlayMaskClick() {
        startSettingOut();
    }

    public void setTitle(String title) {
        player_title.setText(title);
    }

    public void startControlIn() {
        mPlayerControlShowing = true;
        mPlayerControlTopAnimator.cancel();
        mPlayerControlBottomAnimator.cancel();
        ViewHelper.setY(player_control_top, -player_control_top.getMeasuredHeight());
        ViewHelper.setAlpha(player_control_top, 0f);
        mPlayerControlTopAnimator.y(0f).alpha(1f).setDuration(300l).setListener(mPlayerControlTopInListener).start();
        ViewHelper.setY(player_control_bottom, player_control.getMeasuredHeight());
        ViewHelper.setAlpha(player_control_bottom, 0f);
        mPlayerControlBottomAnimator.y(player_control.getMeasuredHeight() - player_control_bottom.getMeasuredHeight()).alpha(1f)
                .setDuration(300l).setListener(mPlayerControlBottomInListener).start();
    }

    public void startControlOut() {
        mPlayerControlShowing = false;
        mPlayerControlTopAnimator.cancel();
        mPlayerControlBottomAnimator.cancel();
        ViewHelper.setY(player_control_top, 0f);
        ViewHelper.setAlpha(player_control_top, 1f);
        mPlayerControlTopAnimator.y(-player_control_top.getMeasuredHeight()).alpha(0f).setDuration(300l)
                .setListener(mPlayerControlTopOutListener).start();
        ViewHelper.setY(player_control_bottom, player_control.getMeasuredHeight() - player_control_bottom.getMeasuredHeight());
        ViewHelper.setAlpha(player_control_bottom, 1f);
        mPlayerControlBottomAnimator.y(player_control.getMeasuredHeight()).alpha(0f).setDuration(300l)
                .setListener(mPlayerControlBottomOutListener).start();
    }

    public void startSettingIn() {
        mHandler.removeCallbacks(mHidePlayerControlRunnable);
        mPlayerSettingShowing = true;
        mPlayerSettingOverlayMaskAnimator.cancel();
        mPlayerSettingOverlayAnimator.cancel();
        ViewHelper.setAlpha(player_setting_overlay_mask, 0f);
        mPlayerSettingOverlayMaskAnimator.alpha(1f).setDuration(300l).setListener(mPlayerSettingOverlayMaskInListener).start();
        ViewHelper.setY(player_setting_overlay, -player_setting_overlay.getMeasuredHeight());
        ViewHelper.setAlpha(player_setting_overlay, 0f);
        mPlayerSettingOverlayAnimator.y(0f).alpha(1f).setDuration(300l).setListener(mPlayerSettingOverlayInListener).start();
    }

    public void startSettingOut() {
        mPlayerSettingShowing = false;
        mPlayerSettingOverlayMaskAnimator.cancel();
        mPlayerSettingOverlayAnimator.cancel();
        ViewHelper.setAlpha(player_setting_overlay_mask, 1f);
        mPlayerSettingOverlayMaskAnimator.alpha(0f).setDuration(300l).setListener(mPlayerSettingOverlayMaskOutListener).start();
        ViewHelper.setY(player_setting_overlay, 0f);
        ViewHelper.setAlpha(player_setting_overlay, 1f);
        mPlayerSettingOverlayAnimator.y(-player_setting_overlay.getMeasuredHeight()).alpha(0f).setDuration(300l)
                .setListener(mPlayerSettingOverlayOutListener).start();
        mHandler.postDelayed(mHidePlayerControlRunnable, 6000l);
    }
}