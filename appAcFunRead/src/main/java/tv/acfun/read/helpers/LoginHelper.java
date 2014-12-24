package tv.acfun.read.helpers;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.frameworks.bases.IFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.widgets.CircularProgressDrawable;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.beans.Token;
import tv.acfun.read.parsers.FullUserParser;
import tv.acfun.read.parsers.TokenParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/17
 */
public class LoginHelper implements MaterialDialog.Callback {
    private EditText login_account;
    private TextView login_error;
    private TextView login_message;
    private EditText login_password;
    private ImageView login_progress;
    private CheckBox login_remember;
    private View login_status;
    private CheckBox.OnCheckedChangeListener mCheckedChangeListener = new CheckBox.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AcFunRead.getInstance().writeBoolean("rememberAccount", isChecked);
        }
    };
    private IFramework mFramework;
    private FullUser mFullUser = null;
    private LoginCallback mLoginCallback;
    private MaterialDialog mLoginDialog;
    private CircularProgressDrawable mProgressDrawable;
    private Token mToken = null;
    private IRequestCallback<String> mFullUserCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(String requestUrl) {
            showError(R.string.login_timeout);
        }

        @Override
        public void onSuccess(String requestUrl, String result) {
            AcFunRead acFunRead;
            FullUserParser parser = FullUserParser.parse(result);

            if (parser != null) {
                login_account.clearFocus();
                login_password.clearFocus();
                login_status.setVisibility(View.GONE);
                acFunRead = AcFunRead.getInstance();
                acFunRead.writeFullUser(parser.getFullUser());
                acFunRead.writeToken(mToken);
                if (mLoginCallback != null) {
                    hide();
                    mLoginCallback.onSuccess();
                }
            } else {
                showError(R.string.login_timeout);
                mToken = null;
            }
        }
    };
    private IRequestCallback<String> mTokenCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(String requestUrl) {
            showError(R.string.login_timeout);
        }

        @Override
        public void onSuccess(String requestUrl, String result) {
            TokenParser parser = TokenParser.parser(result);

            if (parser != null && parser.getData() != null) {
                if (parser.isSuccess()) {
                    mToken = parser.getData();
                    mFramework.executeRequest(API.getFullUser(mToken.getUserId()), mFullUserCallback);
                }
            } else {
                showError(R.string.login_denied);
            }
        }
    };

    public LoginHelper(IFramework framework) {
        View view;

        mFramework = framework;
        mLoginDialog = new MaterialDialog.Builder(mFramework.getContext()).callback(this).title(R.string.login_required)
                .positiveText(R.string.app_ok).negativeText(R.string.app_cancel).customView(R.layout.dialog_login)
                .autoDismiss(false).build();
        view = mLoginDialog.getCustomView();
        mLoginDialog.setCancelable(false);
        login_account = (EditText) view.findViewById(R.id.login_account);
        login_password = (EditText) view.findViewById(R.id.login_password);
        login_remember = (CheckBox) view.findViewById(R.id.login_remember);
        login_status = view.findViewById(R.id.login_status);
        login_progress = (ImageView) view.findViewById(R.id.login_progress);
        login_message = (TextView) view.findViewById(R.id.login_message);
        login_error = (TextView) view.findViewById(R.id.login_error);

        mProgressDrawable = new CircularProgressDrawable();
        login_progress.setImageDrawable(mProgressDrawable);
        login_remember.setOnCheckedChangeListener(mCheckedChangeListener);

        doUpdate();
    }

    private void access() {
        AcFunRead acFunRead;
        String account = login_account.getText().toString();
        String password = login_password.getText().toString();

        if (account.length() < 2) {
            showError(R.string.login_account_tooshort);

            return;
        } else if (account.length() > 50) {
            showError(R.string.login_account_toolong);

            return;
        }
        if (password.length() < 2) {
            showError(R.string.login_password_tooshort);

            return;
        } else if (password.length() > 50) {
            showError(R.string.login_password_toolong);

            return;
        }
        acFunRead = AcFunRead.getInstance();
        if (login_remember.isChecked()) {
            acFunRead.writeString("account", account);
        } else {
            acFunRead.writeString("account", null);
        }
        showProgress();
        mFramework.executeRequest(API.getToken(account, password), mTokenCallback);
    }

    public void destroy() {
        mLoginDialog.dismiss();
        mFramework = null;
    }

    public void doUpdate() {
        AcFunRead acFunRead = AcFunRead.getInstance();
        String account = acFunRead.readString("account", "");

        login_account.setText(account);
        login_account.setSelection(account.length());
        login_password.setText("");
        login_remember.setChecked(acFunRead.readBoolean("rememberAccount", false));
    }

    /**
     * 获取缓存的用户信息
     *
     * @return 用户信息
     */
    public final FullUser getFullUser() {
        return mFullUser;
    }

    /**
     * 获取缓存的身份令牌
     *
     * @return 身份令牌
     */
    public final Token getToken() {
        return mToken;
    }

    public void hide() {
        mLoginDialog.hide();
        mProgressDrawable.setProgress(0);
        login_status.setVisibility(View.GONE);
    }

    /**
     * 检查用户身份
     *
     * @return 是否通过身份验证
     */
    public boolean isLogin() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        mFullUser = acFunRead.readFullUser();
        mToken = acFunRead.readToken();
        return !(mFullUser == null || mToken == null) && !mToken.isExpired();
    }

    public boolean isShowing() {
        return mLoginDialog.isShowing();
    }

    @Override
    public void onNegative(MaterialDialog materialDialog) {
        mFramework.cancelRequest();
        hide();
    }

    @Override
    public void onPositive(MaterialDialog materialDialog) {
        access();
    }

    public void setLoginCallback(LoginCallback loginCallback) {
        mLoginCallback = loginCallback;
    }

    public void show(Reason reason) {
        if (reason == Reason.Unauthorized) {
            mLoginDialog.setTitle(R.string.login_unauthorized);
        } else {
            mLoginDialog.setTitle(R.string.login_expired);
        }
        mLoginDialog.show();
    }

    private void showError(int errorId) {
        login_status.setVisibility(View.VISIBLE);
        login_progress.setVisibility(View.GONE);
        login_message.setVisibility(View.GONE);
        login_error.setVisibility(View.VISIBLE);

        mProgressDrawable.setProgress(0);
        login_error.setText(errorId);
    }

    private void showProgress() {
        login_status.setVisibility(View.VISIBLE);
        login_progress.setVisibility(View.VISIBLE);
        login_message.setVisibility(View.VISIBLE);
        login_error.setVisibility(View.GONE);

        mProgressDrawable.setProgress(-1);
        login_message.setText(R.string.login_progress);
    }

    /**
     * 验证用户身份
     *
     * 如果未登录，或者身份验证已过期，则会清空身份记录，并弹出登录对话框
     *
     * @return 是否通过身份验证
     */
    public boolean validateLogin() {
        AcFunRead acFunRead = AcFunRead.getInstance();

        mFullUser = acFunRead.readFullUser();
        mToken = acFunRead.readToken();
        if (mFullUser == null || mToken == null) {
            acFunRead.writeFullUser(null);
            acFunRead.writeToken(null);
            show(Reason.Unauthorized);

            return false;
        } else if (mToken.isExpired()) {
            acFunRead.writeFullUser(null);
            acFunRead.writeToken(null);
            show(Reason.Expired);

            return false;
        } else {
            return true;
        }
    }

    public enum Reason {
        Unauthorized,
        Expired
    }

    public interface LoginCallback {
        public void onSuccess();
    }
}