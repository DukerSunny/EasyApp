package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.frameworks.base.ActivityFramework;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.ViewUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.User;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class BindQQActivity extends ActivityFramework {
    private EditText bind_qq_input;
    private View bind_qq_submit;
    private View.OnClickListener mOnClickListener;
    private IRequestCallback<String> mQQCallback;
    private IRequestCallback<String> mUserCallback;

    public static Intent create(Context context) {
        return new Intent(context, BindQQActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
        RippleOnClickListener.attach(bind_qq_submit, mOnClickListener);
    }

    private String checkQQ() {
        String qq = bind_qq_input.getText().toString().trim();

        if (qq.length() == 0) {
            showToast(R.string.bind_qq_empty);

            return null;
        } else {
            return qq;
        }
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.bind_qq);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        bind_qq_input = (EditText) findViewById(R.id.bind_qq_input);
        bind_qq_submit = findViewById(R.id.bind_qq_submit);

        RippleDrawable.attach(bind_qq_submit);
    }

    @Override
    public void establishCallbacks() {
        mQQCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.bind_qq_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Log.e(null, "bind success " + result);
                ObjectResult<String> objectResult = Parser.parseString(result, "error", "data", "data");
                if (objectResult != null) {
                    if (objectResult.getFlag() == 0) {
                        showToast(R.string.bind_qq_success);
                        executeRequest(API.getUser(), mUserCallback);
                    } else {
                        showToast(getString(R.string.bind_qq_failure) + objectResult.getMessage());
                    }
                } else {
                    showToast(R.string.bind_qq_failure);
                }
            }
        };
        mUserCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                onBackPressed();
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                ObjectResult<User> objectResult = Parser.parseObject(result, User.class, "error", "data", "data");
                User user;

                if (objectResult != null) {
                    user = objectResult.getObject();
                    if (user != null) {
                        DouyuTv.getInstance().updateUser(user);
                    }
                }
                setResult(RESULT_OK);
                onBackPressed();
            }
        };
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.bind_qq_submit:
                        onQQSubmitClick();
                        break;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_bind_qq;
    }

    private void hideIme() {
        ViewUtil.hideInputMethod(getContext(), bind_qq_input);
    }

    private void onQQSubmitClick() {
        String qq;

        hideIme();
        qq = checkQQ();
        if (qq != null) {
            showToast(R.string.bind_qq_submitting, true);
            executeRequest(API.getBindQQ(qq), mQQCallback);
        }
    }

    @Override
    public void startAction() {
    }
}