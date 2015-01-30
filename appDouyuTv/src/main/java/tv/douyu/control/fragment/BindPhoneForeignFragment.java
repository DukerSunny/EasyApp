package tv.douyu.control.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.base.FragmentFramework;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.douyu.R;
import tv.douyu.wrapper.helper.BindPhoneHelper;
import tv.douyu.callback.OnBindPhoneListener;
import tv.douyu.callback.OnCaptchaListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class BindPhoneForeignFragment extends FragmentFramework {
    private ImageView bind_captcha;
    private EditText bind_captcha_input;
    private EditText bind_phone_input;
    private View bind_phone_submit;
    private BindPhoneHelper mBindPhoneHelper;
    private OnBindPhoneListener mOnBindPhoneListener = null;
    private OnCaptchaListener mOnCaptchaListener;
    private View.OnClickListener mOnClickListener;

    public static BindPhoneForeignFragment create() {
        return new BindPhoneForeignFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
    }

    @Override
    public void attachCallbacks() {
        mBindPhoneHelper.setOnCaptchaListener(mOnCaptchaListener);

        RippleOnClickListener.attach(bind_phone_submit, mOnClickListener);
    }

    private String checkCaptchaCode() {
        String captchaCode = bind_captcha_input.getText().toString().trim();

        if (captchaCode.length() == 0) {
            showToast(R.string.bind_captcha_empty);

            return null;
        } else if (captchaCode.length() < 4) {
            showToast(R.string.bind_captcha_wrong);

            return null;
        } else {
            return captchaCode;
        }
    }

    private String checkPhone() {
        String phone = bind_phone_input.getText().toString().trim();

        if (phone.length() == 0) {
            showToast(R.string.bind_phone_empty);

            return null;
        } else if (phone.length() < 11) {
            showToast(R.string.bind_phone_wrong);

            return null;
        } else {
            return phone;
        }
    }

    @Override
    public void enquiryViews() {
        bind_phone_input = (EditText) findViewById(R.id.bind_phone_input);
        bind_captcha_input = (EditText) findViewById(R.id.bind_captcha_input);
        bind_captcha = (ImageView) findViewById(R.id.bind_captcha);
        bind_phone_submit = findViewById(R.id.bind_phone_submit);

        mBindPhoneHelper = new BindPhoneHelper(bind_captcha);

        RippleDrawable.attach(bind_phone_submit, RippleStyle.Light);
    }

    @Override
    public void establishCallbacks() {
        mOnCaptchaListener = new OnCaptchaListener() {
            @Override
            public void onBindFailure(String message) {
                showToast(message);
            }

            @Override
            public void onBindSuccess() {
                showToast(R.string.bind_success);
                if (mOnBindPhoneListener != null) {
                    mOnBindPhoneListener.onBindSuccess();
                }
            }

            @Override
            public void onCaptchaClick() {
                mBindPhoneHelper.updateForeignImage();
            }

            @Override
            public void onObtainFailure(String message) {
            }

            @Override
            public void onObtainSuccess() {
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bind_phone_submit:
                        onSubmitClick();
                        break;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bind_phone_foreign;
    }

    private void hideIme() {
        ViewUtil.hideInputMethod(getContext(), bind_phone_input);
        ViewUtil.hideInputMethod(getContext(), bind_captcha_input);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnBindPhoneListener = (OnBindPhoneListener) activity;
    }

    private void onSubmitClick() {
        String phone;
        String captchaCode;

        hideIme();
        phone = checkPhone();
        if (phone != null) {
            captchaCode = checkCaptchaCode();
            if (captchaCode != null) {
                Log.e(null, "bind foreign");
                showToast(R.string.bind_phone_submitting, true);
                mBindPhoneHelper.bindForeign(phone, captchaCode);
            }
        }
    }

    @Override
    public void startAction() {
        mBindPhoneHelper.updateForeignImage();
    }
}