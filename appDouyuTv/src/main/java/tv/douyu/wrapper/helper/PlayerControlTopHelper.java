package tv.douyu.wrapper.helper;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.helpers.ViewSwitchHelper;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.nineoldandroids.view.ViewHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlTopHelper {
    private ViewSwitchHelper mDefinitionSwitchHelper;
    private ToggleViewValueAnimator mTopAnimator;
    @InjectView(R.id.player_back)
    View player_back;
    @InjectView(R.id.player_control_top)
    View player_control_top;
    @InjectView(R.id.player_definition_hd)
    View player_definition_hd;
    @InjectView(R.id.player_definition_sd)
    View player_definition_sd;
    @InjectView(R.id.player_live)
    View player_live;
    @InjectView(R.id.player_setting)
    View player_setting;
    @InjectView(R.id.player_title)
    TextView player_title;

    public PlayerControlTopHelper(View rootView) {
        ButterKnife.inject(this, rootView);

        mTopAnimator = ToggleViewValueAnimator.animate(player_control_top);
        mDefinitionSwitchHelper = new ViewSwitchHelper(player_definition_sd, player_definition_hd);

        RippleDrawable.attach(player_back, RippleStyle.Light_Square);
        RippleDrawable.attach(player_definition_sd, RippleStyle.Light);
        RippleDrawable.attach(player_definition_hd, RippleStyle.Light);
        RippleDrawable.attach(player_live, RippleStyle.Light);
        RippleDrawable.attach(player_setting, RippleStyle.Light_Square);

        player_control_top.post(new Runnable() {
            @Override
            public void run() {
                mTopAnimator.yOff(-player_control_top.getMeasuredHeight()).yOn(0f).alphaOff(0f).alphaOn(1f)
                        .visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                hide(false);
            }
        });
    }

    public void hide(boolean animate) {
        Log.e(null, "hide top from " + ViewHelper.getY(player_control_top) + " to " + mTopAnimator.getYOff());
        mTopAnimator.toggleOff(animate);
        //        ViewPropertyAnimator.animate(player_control_top).y(-120f).duration(300l).start();
    }

    public boolean isShowing() {
        return mTopAnimator.isToggledOn();
    }

    @OnClick(R.id.player_back)
    protected abstract void onBackClick();

    @OnClick(R.id.player_definition_sd)
    protected abstract void onDefinitionClick();

    @OnClick(R.id.player_live)
    protected abstract void onLiveClick();

    @OnClick(R.id.player_setting)
    protected abstract void onSettingClick();

    public void setTitle(String title) {
        player_title.setText(title);
    }

    public void setUseHD(boolean useHD) {
        if (useHD) {
            mDefinitionSwitchHelper.switchToView(player_definition_hd);
        } else {
            mDefinitionSwitchHelper.switchToView(player_definition_sd);
        }
    }

    public void show(boolean animate) {
        Log.e(null, "show top from " + ViewHelper.getY(player_control_top) + " to " + mTopAnimator.getYOn());
        //        ViewPropertyAnimator.animate(player_control_top).y(0f).duration(300l).start();
        mTopAnimator.toggleOn(animate);
    }

    public void toggle() {
        if (isShowing()) {
            hide(true);
        } else {
            show(true);
        }
    }
}