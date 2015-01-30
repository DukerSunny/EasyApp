package tv.douyu.wrapper.helper;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.frameworks.base.IFramework;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tv.douyu.R;
import tv.douyu.model.bean.Update;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/29
 */
public class UpdateHelper extends MaterialDialog.ButtonCallback {
    private MaterialDialog mDialog;
    private String mDownUrl;
    private boolean mForceUpdate;
    private IFramework mFramework;
    @InjectView(R.id.update_content)
    TextView update_content;
    @InjectView(R.id.update_size)
    TextView update_size;
    @InjectView(R.id.update_version)
    TextView update_version;

    public UpdateHelper(IFramework framework) {
        View rootView;
        MaterialDialog.Builder builder;

        mFramework = framework;
        rootView = LayoutInflater.from(framework.getContext()).inflate(R.layout.dialog_update, null, false);
        builder = new MaterialDialog.Builder(framework.getContext()).title(R.string.setting_support_check_update_available)
                .customView(rootView, false).positiveText(R.string.app_ok);
        if (!mForceUpdate) {
            builder.negativeText(R.string.app_cancel);
        }
        mDialog = builder.build();
        ButterKnife.inject(this, rootView);
    }

    public void destroy() {
        mDialog.dismiss();
        mDialog = null;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        mFramework.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(mDownUrl)));
    }

    public void show(Update update) {
        mDownUrl = update.getDown_url();
        mForceUpdate = update.getForce_update() == 1;

        update_version.setText(update.getVersion());
        update_content.setText(update.getUpdate_content());
        update_size.setText(update.getFilesize() + "mb");

        if (mForceUpdate) {
            mDialog.getActionButton(DialogAction.NEGATIVE).setVisibility(View.INVISIBLE);
        } else {
            mDialog.getActionButton(DialogAction.NEGATIVE).setVisibility(View.VISIBLE);
        }
        mDialog.setCancelable(!mForceUpdate);
        mDialog.show();
    }
}