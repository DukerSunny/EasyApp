package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.harreke.easyapp.frameworks.activity.ActivityFramework;
import com.harreke.easyapp.widgets.transitions.SwipeToFinishLayout;

import butterknife.InjectView;
import tv.douyu.R;
import tv.douyu.misc.api.API;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/21
 */
public class RegisterActivity extends ActivityFramework {
    private WebViewClient mWebViewClient;
    @InjectView(R.id.register_web)
    WebView register_web;

    public static Intent create(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {
        register_web.setWebViewClient(mWebViewClient);
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
        register_web.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void establishCallbacks() {
        mWebViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.equals(API.HOST + "/")) {
                    setResult(RESULT_OK);
                    onBackPressed();

                    return true;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    public void startAction() {
        register_web.loadUrl("http://www.douyutv.com/member/register?mobile=true&hide=login&client_sys=android");
    }
}