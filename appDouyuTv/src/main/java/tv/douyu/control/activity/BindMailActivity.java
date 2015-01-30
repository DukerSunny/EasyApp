package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.frameworks.activity.ActivityFramework;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import tv.douyu.R;
import tv.douyu.misc.api.API;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class BindMailActivity extends ActivityFramework {
    private EditText bind_mail_input;
    private View bind_mail_send;
    private IRequestCallback<String> mMailCallback;
    private View.OnClickListener mOnClickListener;

    public static Intent create(Context context) {
        return new Intent(context, BindMailActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
        RippleOnClickListener.attach(bind_mail_send, mOnClickListener);
    }

    private String checkMail() {
        String mail = bind_mail_input.getText().toString().trim();

        if (mail.length() == 0) {
            showToast(R.string.bind_mail_empty);

            return null;
        } else {
            if (!StringUtil.isValidMail(mail)) {
                showToast(R.string.bind_mail_wrong);

                return null;
            } else {
                return mail;
            }
        }
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.bind_mail);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        bind_mail_input = (EditText) findViewById(R.id.bind_mail_input);
        bind_mail_send = findViewById(R.id.bind_mail_send);

        RippleDrawable.attach(bind_mail_send);
    }

    @Override
    public void establishCallbacks() {
        mMailCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.bind_mail_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Log.e(null, "bind success " + result);
                ObjectResult<String> objectResult = Parser.parseString(result, "result", "data", "data");
                if (objectResult != null) {
                    if (objectResult.getFlag() == 0) {
                        showToast(R.string.bind_mail_success);
                    } else {
                        showToast(getString(R.string.bind_mail_failure) + objectResult.getMessage());
                    }
                } else {
                    showToast(R.string.bind_mail_failure);
                }
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bind_mail_send:
                        onMailSendClick();
                        break;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_mail;
    }

    private void hideIme() {
        ViewUtil.hideInputMethod(getContext(), bind_mail_input);
    }

    private void onMailSendClick() {
        String mail;

        hideIme();
        mail = checkMail();
        if (mail != null) {
            showToast(R.string.bind_mail_sending, true);
            executeRequest(API.postBindMail(mail), mMailCallback);
        }
    }

    @Override
    public void startAction() {
    }
}