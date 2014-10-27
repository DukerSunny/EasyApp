package tv.acfun.read.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.RequestBuilder;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Token;
import tv.acfun.read.parsers.FullUserParser;
import tv.acfun.read.parsers.TokenParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/17
 */
public class LoginHelper extends DialogHelper implements DialogInterface.OnClickListener {
    private int color_Assist;
    private int color_Red;
    private EditText login_account;
    private TextView login_error;
    private EditText login_password;
    private View login_progress;
    private CheckBox login_remember;
    private View login_status;
    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            AcFunRead.getInstance().writeBoolean("rememberAccount", isChecked);
        }
    };
    private OnLoginListener mLoginListener;
    private Token mToken;
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
                mLoginListener.onSuccess();
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
                    mLoginListener.onExecuteRequest(API.getFullUser(mToken.getUserId()), mFullUserCallback);
                }
            } else {
                showError(R.string.login_denied);
            }
        }
    };

    public LoginHelper(Context context) {
        super(context);
        View view = View.inflate(context, R.layout.dialog_login, null);

        login_account = (EditText) view.findViewById(R.id.login_account);
        login_password = (EditText) view.findViewById(R.id.login_password);
        login_remember = (CheckBox) view.findViewById(R.id.login_remember);
        login_status = view.findViewById(R.id.login_status);
        login_progress = view.findViewById(R.id.login_progress);
        login_error = (TextView) view.findViewById(R.id.login_message);

        login_remember.setOnCheckedChangeListener(mCheckedChangeListener);

        color_Assist = context.getResources().getColor(R.color.Assist);
        color_Red = context.getResources().getColor(R.color.Red);

        setTitle(R.string.login_required);
        setView(view);
        setPositiveButton(R.string.app_ok);
        setNegativeButton(R.string.app_cancel);
        setOnClickListener(this);

        reset();
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
            showError(R.string.login_account_tooshort);

            return;
        } else if (password.length() > 50) {
            showError(R.string.login_account_toolong);

            return;
        }
        acFunRead = AcFunRead.getInstance();
        if (login_remember.isChecked()) {
            acFunRead.writeString("account", account);
        } else {
            acFunRead.writeString("account", "");
        }
        showProgress();
        mLoginListener.onExecuteRequest(API.getToken(account, password), mTokenCallback);
    }

    @Override
    public void hide() {
        super.hide();
        login_status.setVisibility(View.GONE);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            access();
        } else {
            mLoginListener.onCancelRequest();
            hide();
        }
    }

    public void reset() {
        AcFunRead acFunRead = AcFunRead.getInstance();
        String account = acFunRead.readString("account", "");

        login_account.setText(account);
        login_account.setSelection(account.length());
        login_password.setText("");
        login_remember.setChecked(acFunRead.readBoolean("rememberAccount", false));
    }

    public void setOnLoginListener(OnLoginListener loginListener) {
        mLoginListener = loginListener;
    }

    public void show(Reason reason) {
        if (reason == Reason.Unauthorized) {
            setTitle(R.string.login_unauthorized);
        } else {
            setTitle(R.string.login_expired);
        }
        show();
    }

    private void showError(int errorId) {
        login_status.setVisibility(View.VISIBLE);
        login_progress.setVisibility(View.GONE);
        login_error.setVisibility(View.VISIBLE);

        login_error.setTextColor(color_Red);
        login_error.setText(errorId);
    }

    private void showProgress() {
        login_status.setVisibility(View.VISIBLE);
        login_progress.setVisibility(View.VISIBLE);
        login_error.setVisibility(View.VISIBLE);

        login_error.setTextColor(color_Assist);
        login_error.setText(R.string.login_progress);
    }

    public enum Reason {
        Unauthorized,
        Expired
    }

    public interface OnLoginListener {
        public void onCancelRequest();

        public void onExecuteRequest(RequestBuilder builder, IRequestCallback<String> callback);

        public void onSuccess();
    }
}