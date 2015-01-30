package tv.douyu.wrapper.helper;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.harreke.easyapp.helpers.CompoundButtonHelper;
import com.harreke.easyapp.listeners.OnButtonsCheckedChangeListener;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlDefinitionHelper implements OnButtonsCheckedChangeListener {
    private String mCdn = "ws";
    /**
     * 为真表示使用高清，否则表示使用标清
     */
    private CompoundButtonHelper mDefinitionHelper;
    private ToggleViewValueAnimator mMaskAnimator;
    private ToggleViewValueAnimator mOverlayAnimator;
    private boolean mShowing = false;
    @InjectView(R.id.player_definition_lx_hd)
    RadioButton player_definition_lx_hd;
    @InjectView(R.id.player_definition_lx_sd)
    RadioButton player_definition_lx_sd;
    @InjectView(R.id.player_definition_mask)
    View player_definition_mask;
    @InjectView(R.id.player_definition_overlay)
    View player_definition_overlay;
    @InjectView(R.id.player_definition_ws_hd)
    RadioButton player_definition_ws_hd;
    @InjectView(R.id.player_definition_ws_sd)
    RadioButton player_definition_ws_sd;

    public PlayerControlDefinitionHelper(View rootView) {
        ButterKnife.inject(this, rootView);

        mDefinitionHelper = new CompoundButtonHelper(player_definition_ws_sd, player_definition_ws_hd, player_definition_lx_sd,
                player_definition_lx_hd);
        mMaskAnimator = ToggleViewValueAnimator.animate(player_definition_mask);
        mOverlayAnimator = ToggleViewValueAnimator.animate(player_definition_overlay);

        mDefinitionHelper.check(0);
        mDefinitionHelper.setOnButtonCheckedChangeListener(this);
        rootView.post(new Runnable() {
            @Override
            public void run() {
                mMaskAnimator.alphaOff(0f).alphaOn(1f).visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
                mOverlayAnimator.alphaOff(0f).alphaOn(1f).yOff(-player_definition_overlay.getMeasuredHeight())
                        .yOn(ViewHelper.getY(player_definition_overlay)).visibilityOff(View.GONE).visibilityOn(View.VISIBLE);
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

    @Override
    public void onButtonCheck(CompoundButton compoundButton, int position) {
        switch (position) {
            case 0:
                onDefinitionChange("ws", false);
                break;
            case 1:
                onDefinitionChange("ws", true);
                break;
            case 2:
                onDefinitionChange("lx", false);
                break;
            case 3:
                onDefinitionChange("lx", true);
                break;
        }
    }

    protected abstract void onDefinitionChange(String cdn, boolean useHD);

    protected abstract void onDefinitionMaskClick();

    @OnClick(R.id.player_definition_mask)
    void onMaskClick() {
        hide(true);
        onDefinitionMaskClick();
    }

    public void setDefinition(String cdn, boolean useHD) {
        Log.e(null, "check " + cdn + " useHD=" + useHD);
        mCdn = cdn;
        if (cdn.equals("ws")) {
            if (useHD) {
                mDefinitionHelper.check(1);
            } else {
                mDefinitionHelper.check(0);
            }
        } else {
            if (useHD) {
                mDefinitionHelper.check(3);
            } else {
                mDefinitionHelper.check(2);
            }
        }
    }

    public void setHasSD(boolean hasSD) {
        if (hasSD) {
            player_definition_ws_sd.setVisibility(View.VISIBLE);
            player_definition_lx_sd.setVisibility(View.VISIBLE);
        } else {
            player_definition_ws_sd.setVisibility(View.GONE);
            player_definition_lx_sd.setVisibility(View.GONE);
        }
        setDefinition(mCdn, true);
    }

    public void show(boolean animate) {
        mShowing = true;
        mMaskAnimator.toggleOn(animate);
        mOverlayAnimator.toggleOn(animate);
    }

    public void toggle() {
        if (isShowing()) {
            hide(true);
        } else {
            show(true);
        }
    }
}