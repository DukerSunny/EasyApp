package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.utils.StringUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/21
 */
public class RegisterActivityBack extends ActivityFramework {
    @InjectView(R.id.register_mail)
    public EditText register_mail;
    @InjectView(R.id.register_password)
    public EditText register_password;
    @InjectView(R.id.register_password_again)
    public EditText register_password_again;
    @InjectView(R.id.register_submit)
    public View register_submit;
    @InjectView(R.id.register_username)
    public EditText register_username;

    public static Intent create(Context context) {
        return new Intent(context, RegisterActivityBack.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
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
        setToolbarTitle(R.string.app_register);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        RippleDrawable.attach(register_submit, RippleStyle.Light);
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.register_submit)
    public void onSubmitClick() {
        String username = register_username.getText().toString();
        String mail = register_mail.getText().toString().trim();
        String password;
        String passwordAgain;

        if (mail.length() == 0) {
            showToast(R.string.bind_mail_empty);

            return;
        }
        if (!StringUtil.isValidMail(mail)) {
            showToast(R.string.bind_mail_wrong);

            return;
        }
    }

    @Override
    public void startAction() {
    }
}