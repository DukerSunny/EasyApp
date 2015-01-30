package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.activity.ActivityFramework;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;
import tv.douyu.misc.api.API;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/29
 */
public class FeedBackActivity extends ActivityFramework {
    private String mDeviceInfo;
    private IRequestCallback<String> mFeedBackCallback;
    @InjectView(R.id.feedback_content_input)
    EditText feedback_content_input;
    @InjectView(R.id.feedback_submit)
    View feedback_submit;
    @InjectView(R.id.feedback_title_input)
    EditText feedback_title_input;

    public static Intent create(Context context) {
        return new Intent(context, FeedBackActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;
        int width = dm.widthPixels;

        String device_model = Build.MODEL;
        String version_sdk = Build.VERSION.SDK; // 设备SDK版本
        String version_release = Build.VERSION.RELEASE; // 设备的系统版本

        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);

        mDeviceInfo = "imei:" + tm.getDeviceId() + ",width:" + String.valueOf(width) + ",height:" + String.valueOf(height) +
                ",model:" + device_model + ",sdk:" + version_sdk + ",release:" + version_release;
    }

    @Override
    public void attachCallbacks() {
    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_feedback);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        RippleDrawable.attach(feedback_submit, RippleStyle.Light);
    }

    @Override
    public void establishCallbacks() {
        mFeedBackCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.feedback_submit_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String result) {
                Log.e(null, result);
                ObjectResult<Integer> objectResult = Parser.parseInt(result, "error", "data", "data");

                if (objectResult != null && objectResult.getObject() != null && objectResult.getObject() == 0) {
                    showToast(R.string.feedback_submit_success);
                    feedback_submit.setClickable(false);
                    onBackPressed(3000l);
                } else {
                    showToast(R.string.login_failure);
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @OnClick(R.id.feedback_submit)
    void onSubmitClick() {
        String title = feedback_title_input.getText().toString();
        String content = feedback_content_input.getText().toString();

        if (TextUtils.isEmpty(title)) {
            showToast(R.string.feedback_title_empty);

            return;
        }
        if (TextUtils.isEmpty(content)) {
            showToast(R.string.feedback_content_empty);

            return;
        }
        executeRequest(API.postFeedBack(title, content, mDeviceInfo), mFeedBackCallback);
    }

    @Override
    public void startAction() {

    }
}
