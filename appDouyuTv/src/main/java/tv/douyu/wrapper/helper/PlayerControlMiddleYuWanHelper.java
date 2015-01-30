package tv.douyu.wrapper.helper;

import android.view.View;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.helpers.ViewSwitchHelper;
import com.harreke.easyapp.widgets.animators.ToggleViewValueAnimator;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public abstract class PlayerControlMiddleYuWanHelper implements View.OnClickListener {
    private View mRootView;
    private ViewSwitchHelper mSwitchHelper;
    private ToggleViewValueAnimator mYuWanAlphaAnimator;
    private ToggleViewValueAnimator mYuWanSlideAnimator;
    private View player_yuwan_send;
    private View player_yuwan_send_icon;
    private View player_yuwan_send_root;
    private View player_yuwan_sending;

    public PlayerControlMiddleYuWanHelper(View rootView) {
        mRootView = rootView;
        player_yuwan_send_root = mRootView.findViewById(R.id.player_yuwan_send_root);
        player_yuwan_send = mRootView.findViewById(R.id.player_yuwan_send);
        player_yuwan_send_icon = mRootView.findViewById(R.id.player_yuwan_send_icon);
        player_yuwan_sending = mRootView.findViewById(R.id.player_yuwan_sending);

        mYuWanSlideAnimator = ToggleViewValueAnimator.animate(player_yuwan_send_root);
        mYuWanAlphaAnimator = ToggleViewValueAnimator.animate(player_yuwan_send_root);
        mSwitchHelper = new ViewSwitchHelper(player_yuwan_send, player_yuwan_sending);

        RippleDrawable.attach(player_yuwan_send_root, RippleStyle.Light);
        RippleDrawable.attach(player_yuwan_send, RippleStyle.Light);

        RippleOnClickListener.attach(player_yuwan_send_root, this);
        RippleOnClickListener.attach(player_yuwan_send, this);

        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mYuWanSlideAnimator.xOff(mRootView.getMeasuredWidth() - player_yuwan_send_icon.getMeasuredWidth())
                        .xOn(mRootView.getMeasuredWidth() - player_yuwan_send_root.getMeasuredWidth());
                mYuWanAlphaAnimator.xOff(mRootView.getMeasuredWidth())
                        .xOn(mRootView.getMeasuredWidth() - player_yuwan_send_icon.getMeasuredWidth()).alphaOff(0f).alphaOn(1f);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_yuwan_send_root:
                toggleOpen();
                break;
            case R.id.player_yuwan_send:
                onSendClick();
                break;
        }
    }

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