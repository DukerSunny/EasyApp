package tv.douyu.control.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.activity.ActivityFramework;

import butterknife.InjectView;
import butterknife.OnClick;
import tv.douyu.R;
import tv.douyu.control.application.DouyuTv;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/29
 */
public class AboutUsActivity extends ActivityFramework {
    @InjectView(R.id.aboutus_sina)
    View aboutus_sina;
    @InjectView(R.id.aboutus_tencent)
    View aboutus_tencent;
    @InjectView(R.id.aboutus_version)
    TextView aboutus_version;
    @InjectView(R.id.aboutus_website)
    View aboutus_website;

    public static Intent create(Context context) {
        return new Intent(context, AboutUsActivity.class);
    }

    @Override
    protected void acquireArguments(Intent intent) {
    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    protected void createMenu() {
        setToolbarTitle(R.string.app_about);
        enableDefaultToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        aboutus_version.setText(DouyuTv.Version);
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @OnClick(R.id.aboutus_sina_url)
    void onSinaUrlClick() {
        start(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.aboutus_sina_url))));
    }

    @OnClick(R.id.aboutus_tencent_url)
    void onTencentUrlClick() {
        start(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.aboutus_tencent_url))));
    }

    @OnClick(R.id.aboutus_website_url)
    void onWebsiteUrlClick() {
        start(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.aboutus_website_url))));
    }

    @Override
    public void startAction() {

    }
}
