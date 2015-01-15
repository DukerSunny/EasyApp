package air.tv.douyu.android.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import air.tv.douyu.android.R;
import air.tv.douyu.android.apis.API;
import air.tv.douyu.android.bases.application.DouyuTv;
import air.tv.douyu.android.parsers.LoginParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/04
 */
public class LoginActivity extends ActivityFramework {
    private View login_access;
    private View login_forgot;
    private EditText login_password;
    private EditText login_username;
    private IRequestCallback<String> mAuthorizeCallback;
    private LoginParser mLoginParser;
    private View.OnClickListener onClickListener;

    public static Intent create(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
        mLoginParser = new LoginParser();
    }

    @Override
    public void attachCallbacks() {
        RippleOnClickListener.attach(login_access, onClickListener);
    }

    private void checkIfCanAccess() {
        String username = login_username.getText().toString();
        String password = login_password.getText().toString();

        if (TextUtils.isEmpty(username)) {
            showToast(R.string.login_username_empty);

            return;
        }
        if (TextUtils.isEmpty(password)) {
            showToast(R.string.login_password_empty);

            return;
        }
        doAccess(username, password);
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
        showToast(R.string.login_access_acting, true);
        executeRequest(API.getLogin(username, password), mAuthorizeCallback);
    }

    @Override
    public void enquiryViews() {
        login_username = (EditText) findViewById(R.id.login_username);
        login_password = (EditText) findViewById(R.id.login_password);
        login_access = findViewById(R.id.login_access);

        RippleDrawable.attach(login_access, RippleStyle.Light);
    }

    @Override
    public void establishCallbacks() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.login_access:
                        checkIfCanAccess();
                        break;
                }
            }
        };
        mAuthorizeCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.connection_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                mLoginParser.parse(result);
                if (mLoginParser.getObject() != null) {
                    DouyuTv.getInstance().writeUser(mLoginParser.getObject());
                    onBackPressed();
                } else {
                    showToast(R.string.login_failure);
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void startAction() {
    }
}