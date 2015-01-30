package tv.douyu.wrapper.helper;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.frameworks.IFramework;

import tv.douyu.R;
import tv.douyu.control.activity.LoginActivity;
import tv.douyu.control.application.DouyuTv;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/29
 */
public class AuthorizeHelper extends MaterialDialog.ButtonCallback {
    private MaterialDialog mDialog;
    private IFramework mFramework;
    private int mRequestCode = -1;

    public AuthorizeHelper(IFramework framework, int requestCode) {
        mFramework = framework;
        mRequestCode = requestCode;
        mDialog = new MaterialDialog.Builder(framework.getContext()).title(R.string.app_login).positiveText(R.string.app_ok)
                .negativeText(R.string.app_cancel).callback(this).build();
    }

    public void destroy() {
        mDialog.dismiss();
        mDialog = null;
        mFramework = null;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        mFramework.start(LoginActivity.create(mFramework.getContext()), mRequestCode);
    }

    public boolean validate(boolean showDialog) {
        switch (DouyuTv.getInstance().validateAuthorize()) {
            case Unauthorized:
                if (showDialog) {
                    mDialog.setContent(mFramework.getContext().getString(R.string.login_unauthorized));
                    mDialog.show();
                }

                return false;
            case AuthorizeExpired:
                if (showDialog) {
                    mDialog.setContent(mFramework.getContext().getString(R.string.login_authorize_expired));
                    mDialog.show();
                }

                return false;
            default:
                return true;
        }
    }
}