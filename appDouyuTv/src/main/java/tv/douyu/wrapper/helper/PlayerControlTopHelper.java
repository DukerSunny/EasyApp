package tv.douyu.wrapper.helper;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.helpers.ViewSwitchHelper;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlTopHelper implements View.OnClickListener {
    private ViewSwitchHelper mDefinitionSwitchHelper;
    private View mRootView;
    private ToggleViewValueAnimator mTopAnimator;
    private View player_back;
    private View player_control_top;
    private View player_definition_hd;
    private View player_definition_sd;
    private View player_live;
    private View player_setting;
    private TextView player_title;

    public PlayerControlTopHelper(View rootView) {
        mRootView = rootView;
        player_control_top = mRootView.findViewById(R.id.player_control_top);
        player_back = mRootView.findViewById(R.id.player_back);
        player_title = (TextView) mRootView.findViewById(R.id.player_title);
        player_definition_sd = mRootView.findViewById(R.id.player_definition_sd);
        player_definition_hd = mRootView.findViewById(R.id.player_definition_hd);
        player_live = mRootView.findViewById(R.id.player_live);
        player_setting = mRootView.findViewById(R.id.player_setting);

        mTopAnimator = ToggleViewValueAnimator.animate(player_control_top);
        mDefinitionSwitchHelper = new ViewSwitchHelper(player_definition_sd, player_definition_hd);

        RippleDrawable.attach(player_back, RippleStyle.Light_Square);
        RippleDrawable.attach(player_definition_sd, RippleStyle.Light);
        RippleDrawable.attach(player_definition_hd, RippleStyle.Light);
        RippleDrawable.attach(player_live, RippleStyle.Light);
        RippleDrawable.attach(player_setting, RippleStyle.Light_Square);

        RippleOnClickListener.attach(player_back, this);
        RippleOnClickListener.attach(player_definition_sd, this);
        RippleOnClickListener.attach(player_definition_hd, this);
        RippleOnClickListener.attach(player_live, this);
        RippleOnClickListener.attach(player_setting, this);

        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mTopAnimator.yOff(-player_control_top.getMeasuredHeight()).yOn(0f).alphaOff(0f).alphaOn(1f)
                        .visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                hide(false);
            }
        });
    }

    public void hide(boolean animate) {
        mTopAnimator.toggleOff(animate);
    }

    public boolean isShowing() {
        return mTopAnimator.isToggledOn();
    }

    protected abstract void onBackClick();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_back:
                onBackClick();
                break;
            case R.id.player_definition_sd:
                onDefinitionClick();
                break;
            case R.id.player_definition_hd:
                onDefinitionClick();
                break;
            case R.id.player_live:
                onLiveClick();
                break;
            case R.id.player_setting:
                onSettingClick();
                break;
        }
    }

    protected abstract void onDefinitionClick();

    protected abstract void onLiveClick();

    protected abstract void onSettingClick();

    public void setTitle(String title) {
        player_title.setText(title);
    }

    public void show(boolean animate) {
        mTopAnimator.toggleOn(animate);
    }

    public void setUseHD(boolean useHD) {
        if (useHD) {
            mDefinitionSwitchHelper.switchToView(player_definition_hd);
        } else {
            mDefinitionSwitchHelper.switchToView(player_definition_sd);
        }
    }

    public void toggle() {
        if (isShowing()) {
            hide(true);
        } else {
            show(true);
        }
    }
}