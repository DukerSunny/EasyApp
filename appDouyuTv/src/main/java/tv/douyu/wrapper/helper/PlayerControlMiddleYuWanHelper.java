package tv.douyu.wrapper.helper;

import android.view.View;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.helpers.ViewSwitchHelper;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlMiddleYuWanHelper {
    private ViewSwitchHelper mSwitchHelper;
    private ToggleViewValueAnimator mYuWanAlphaAnimator;
    private ToggleViewValueAnimator mYuWanSlideAnimator;
    @InjectView(R.id.player_yuwan_icon)
    View player_yuwan_icon;
    @InjectView(R.id.player_yuwan_root)
    View player_yuwan_root;
    @InjectView(R.id.player_yuwan_send)
    View player_yuwan_send;
    @InjectView(R.id.player_yuwan_sending)
    View player_yuwan_sending;

    public PlayerControlMiddleYuWanHelper(View rootView) {
        ButterKnife.inject(this, rootView);
        mYuWanSlideAnimator = ToggleViewValueAnimator.animate(player_yuwan_root);
        mYuWanAlphaAnimator = ToggleViewValueAnimator.animate(player_yuwan_root);
        mSwitchHelper = new ViewSwitchHelper(player_yuwan_send, player_yuwan_sending);

        RippleDrawable.attach(player_yuwan_root, RippleStyle.Light);
        RippleDrawable.attach(player_yuwan_send, RippleStyle.Light);

        player_yuwan_root.post(new Runnable() {
            @Override
            public void run() {
                mYuWanSlideAnimator.xOff(player_yuwan_root.getRight() - player_yuwan_icon.getMeasuredWidth())
                        .xOn(player_yuwan_root.getRight() - player_yuwan_root.getMeasuredWidth());
                mYuWanAlphaAnimator.xOff(player_yuwan_root.getRight())
                        .xOn(player_yuwan_root.getRight() - player_yuwan_icon.getMeasuredWidth()).alphaOff(0f).alphaOn(1f);
                close(false);
            }
        });
    }

    public void close(boolean animate) {
        mYuWanSlideAnimator.toggleOff(animate);
    }

    public void hide(boolean animate) {
        mYuWanAlphaAnimator.toggleOff(animate);
    }

    public boolean isShowing() {
        return mYuWanAlphaAnimator.isToggledOn();
    }

    @OnClick(R.id.player_yuwan_send)
    protected abstract void onSendClick();

    public void open(boolean animate) {
        mYuWanSlideAnimator.toggleOn(animate);
    }

    public void show(boolean animate) {
        mYuWanAlphaAnimator.toggleOn(animate);
    }

    public void showSend() {
        mSwitchHelper.switchToView(player_yuwan_send);
    }

    public void showSending() {
        mSwitchHelper.switchToView(player_yuwan_sending);
    }

    @OnClick(R.id.player_yuwan_root)
    public void toggleOpen() {
        mYuWanSlideAnimator.toggle(true);
    }

    public void toggleShow() {
        if (isShowing()) {
            hide(true);
        } else {
            show(true);
        }
    }
}