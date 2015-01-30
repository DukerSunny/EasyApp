package tv.douyu.control.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
public class BindPhoneDomesticFragment extends FragmentFramework {
    private ImageView bind_captcha;
    private EditText bind_captcha_input;
    private TextView bind_phone_code_input;
    private View bind_phone_code_via_mail;
    private TextView bind_phone_code_via_mail_retry;
    private View bind_phone_code_via_voice;
    private TextView bind_phone_code_via_voice_retry;
    private View bind_phone_domestic_code_hint;
    private View bind_phone_domestic_code_sent;
    private EditText bind_phone_input;
    private View bind_phone_submit;
    private BindPhoneHelper mBindPhoneHelper;
    private int mCoolDown = -1;
    private Handler mCoolDownHandler = new Handler();
    private Runnable mCoolDownRunnable;
    private OnBindPhoneListener mOnBindPhoneListener = null;
    private OnCaptchaListener mOnCaptchaListener;
    private View.OnClickListener mOnClickListener;

    public static BindPhoneDomesticFragment create() {
        return new BindPhoneDomesticFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {
    }

    @Override
    public void attachCallbacks() {
        mBindPhoneHelper.setOnCaptchaListener(mOnCaptchaListener);

        RippleOnClickListener.attach(bind_phone_code_via_mail, mOnClickListener);
        RippleOnClickListener.attach(bind_phone_code_via_voice, mOnClickListener);
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

    private String checkValidateCode() {
        String validateCode = bind_phone_code_input.getText().toString().trim();

        if (validateCode.length() == 0) {
            showToast(R.string.bind_code_empty);

            return null;
        } else if (validateCode.length() < 6) {
            showToast(R.string.bind_code_wrong);

            return null;
        } else {
            return validateCode;
        }
    }

    private void disableObtain() {
        bind_phone_code_via_mail.setVisibility(View.GONE);
        bind_phone_code_via_mail_retry.setVisibility(View.VISIBLE);
        bind_phone_code_via_mail_retry.setText(getString(R.string.bind_code_via_mail, 60));
        bind_phone_code_via_voice.setVisibility(View.GONE);
        bind_phone_code_via_voice_retry.setVisibility(View.VISIBLE);
        bind_phone_code_via_voice_retry.setText(getString(R.string.bind_code_via_voice, 60));
    }

    private void enableValidate() {
        bind_phone_code_via_mail.setVisibility(View.VISIBLE);
        bind_phone_code_via_mail_retry.setVisibility(View.GONE);
        bind_phone_code_via_voice.setVisibility(View.VISIBLE);
        bind_phone_code_via_voice_retry.setVisibility(View.GONE);
    }

    @Override
    public void enquiryViews() {
        bind_phone_input = (EditText) findViewById(R.id.bind_phone_input);
        bind_captcha_input = (EditText) findViewById(R.id.bind_captcha_input);
        bind_captcha = (ImageView) findViewById(R.id.bind_captcha);
        bind_phone_domestic_code_hint = findViewById(R.id.bind_phone_domestic_code_hint);
        bind_phone_domestic_code_sent = findViewById(R.id.bind_phone_domestic_code_sent);
        bind_phone_code_input = (TextView) findViewById(R.id.bind_phone_code_input);
        bind_phone_code_via_mail = findViewById(R.id.bind_phone_code_via_mail);
        bind_phone_code_via_mail_retry = (TextView) findViewById(R.id.bind_phone_code_via_mail_retry);
        bind_phone_code_via_voice = findViewById(R.id.bind_phone_code_via_voice);
        bind_phone_code_via_voice_retry = (TextView) findViewById(R.id.bind_phone_code_via_voice_retry);
        bind_phone_submit = findViewById(R.id.bind_phone_submit);

        mBindPhoneHelper = new BindPhoneHelper(bind_captcha);

        RippleDrawable.attach(bind_phone_code_via_mail, RippleStyle.Light);
        RippleDrawable.attach(bind_phone_code_via_voice, RippleStyle.Light);
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
                mBindPhoneHelper.updateDomesticImage();
            }

            @Override
            public void onObtainFailure(String message) {
                showToast(message);
                enableValidate();
            }

            @Override
            public void onObtainSuccess() {
                bind_phone_domestic_code_hint.setVisibility(View.GONE);
                bind_phone_domestic_code_sent.setVisibility(View.VISIBLE);
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bind_phone_code_via_mail:
                        onObtainMailClick();
                        break;
                    case R.id.bind_phone_code_via_voice:
                        onObtainVoiceClick();
                        break;
                    case R.id.bind_phone_submit:
                        onSubmitClick();
                        break;
                }
            }
        };
        mCoolDownRunnable = new Runnable() {
            @Override
            public void run() {
                mCoolDown--;
                if (mCoolDown < 0) {
                    enableValidate();
                } else {
                    bind_phone_code_via_mail_retry.setText(getString(R.string.bind_retry, mCoolDown));
                    bind_phone_code_via_voice_retry.setText(getString(R.string.bind_retry, mCoolDown));
                    mCoolDownHandler.postDelayed(mCoolDownRunnable, 1000);
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bind_phone_domestic;
    }

    private void hideIme() {
        ViewUtil.hideInputMethod(getContext(), bind_phone_input);
        ViewUtil.hideInputMethod(getContext(), bind_captcha_input);
        ViewUtil.hideInputMethod(getContext(), bind_phone_code_input);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mOnBindPhoneListener = (OnBindPhoneListener) activity;
    }

    @Override
    public void onDestroyView() {
        mCoolDownHandler.removeCallbacks(mCoolDownRunnable);
        super.onDestroyView();
    }

    private void onObtainMailClick() {
        String phone;
        String captchaCode;

        hideIme();
        phone = checkPhone();
        if (phone != null) {
            captchaCode = checkCaptchaCode();
            if (captchaCode != null) {
                mCoolDown = 60;
                disableObtain();
                mCoolDownHandler.postDelayed(mCoolDownRunnable, 1000l);
                mBindPhoneHelper.obtainValidateCodeViaMail(phone, captchaCode);
            }
        }
    }

    private void onObtainVoiceClick() {
        String phone;
        String captchaCode;

        hideIme();
        phone = checkPhone();
        if (phone != null) {
            captchaCode = checkCaptchaCode();
            if (captchaCode != null) {
                mCoolDown = 60;
                disableObtain();
                mCoolDownHandler.postDelayed(mCoolDownRunnable, 1000l);
                mBindPhoneHelper.obtainValidateCodeViaVoice(phone, captchaCode);
            }
        }
    }

    private void onSubmitClick() {
        String captchaCode;
        String validateCode;

        hideIme();
        if (checkPhone() != null) {
            captchaCode = checkCaptchaCode();
            if (captchaCode != null) {
                validateCode = checkValidateCode();
                if (validateCode != null) {
                    Log.e(null, "bind domestic");
                    showToast(R.string.bind_phone_submitting, true);
                    mBindPhoneHelper.bindDomestic(captchaCode, validateCode);
                }
            }
        }
    }

    @Override
    public void startAction() {
        mBindPhoneHelper.updateDomesticImage();
    }
}