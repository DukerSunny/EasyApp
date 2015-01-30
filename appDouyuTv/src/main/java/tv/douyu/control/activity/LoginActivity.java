package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.misc.api.API;
import tv.douyu.model.bean.User;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class LoginActivity extends ActivityFramework {
    private final static int LOGIN_FORGOT = 2;
    private final static int LOGIN_QQ = 3;
    private final static int LOGIN_REGISTER = 1;
    private IRequestCallback<String> mAuthorizeCallback;
    private String mPassword;
    private IRequestCallback<String> mQQLoginCallback;
    private Tencent mTencent;
    private IUiListener mTentcentListener;
    @InjectView(R.id.login_password)
    EditText login_password;
    @InjectView(R.id.login_submit)
    View login_submit;
    @InjectView(R.id.login_username)
    EditText login_username;

    public static Intent create(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mTencent = Tencent.createInstance("1102007514", this);
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    protected void configActivity() {
        attachTransition(new SwipeToFinishLayout(this));
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_login);
        enableDefaultToolbarNavigation();
    }

    private void doAccess(String username, String password) {
        mPassword = password;
        showToast(R.string.login_submitting, true);
        executeRequest(API.getLogin(username, mPassword), mAuthorizeCallback);
    }

    @Override
    public void enquiryViews() {
        RippleDrawable.attach(login_submit, RippleStyle.Light);
    }

    @Override
    public void establishCallbacks() {
        mAuthorizeCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.connection_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<User> objectResult = Parser.parseObject(result, User.class, "error", "data", "data");
                User user;

                if (objectResult != null) {
                    user = objectResult.getObject();
                    if (user != null) {
                        user.setPassword(StringUtil.toMD5(mPassword));
                        DouyuTv.getInstance().setUser(user);
                        showToast(R.string.login_success);
                        login_submit.setClickable(false);
                        setResult(RESULT_OK);
//                        onBackPressed(3000l);
                        onBackPressed();
                    } else {
                        showToast(getString(R.string.login_failure) + objectResult.getMessage());
                    }
                }
            }
        };
        mTentcentListener = new IUiListener() {
            @Override
            public void onCancel() {
            }

            @Override
            public void onComplete(Object o) {
                showToast(R.string.login_validating, true);
                executeRequest(API.getOauthQQ(mTencent.getAccessToken()), mQQLoginCallback);
            }

            @Override
            public void onError(UiError uiError) {
                showToast(R.string.login_failure);
            }
        };
        mQQLoginCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.login_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Log.e(null, result);
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case LOGIN_REGISTER:
                if (resultCode == RESULT_OK) {
                    showToast(R.string.register_success);
                }
                break;
            case LOGIN_FORGOT:
                if (resultCode == RESULT_OK) {
                    showToast(R.string.forgot_submitted);
                }
                break;
            case LOGIN_QQ:
                setResult(resultCode);
                break;
        }
    }

    @OnClick(R.id.login_forgot)
    void onForgotClick() {
        start(ForgotActivity.create(getContext()), LOGIN_FORGOT);
    }

    @OnClick(R.id.login_qq)
    void onLoginQQClick() {
        if (!isRequestExecuting()) {
            mTencent.login(this, "all", mTentcentListener);
        }
    }

    @OnClick(R.id.login_register)
    void onRegisterClick() {
        start(RegisterActivity.create(getContext()), LOGIN_REGISTER);
    }

    @OnClick(R.id.login_submit)
    void onSubmitClick() {
        String username;
        String password;

        if (!isRequestExecuting()) {
            username = login_username.getText().toString();
            password = login_password.getText().toString();
            if (TextUtils.isEmpty(username)) {
                showToast(R.string.login_username_empty);

                return;
            }
            if (TextUtils.isEmpty(password)) {
                showToast(R.string.login_password_empty);

                return;
            }
            ViewUtil.hideInputMethod(this, login_username);
            ViewUtil.hideInputMethod(this, login_password);
            doAccess(username, password);
        }
    }

    @Override
    public void startAction() {
    }
}