package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.SwitchCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.harreke.easyapp.bases.activity.ActivityFramework;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.beans.Setting;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/30
 */
public class SettingActivity extends ActivityFramework {
    private boolean mBlock = false;
    private ArrayAdapter<String> mFontAdapter;
    private List<String> mFontPathList;
    private MaterialDialog.SimpleCallback mMaxQuoteCallback;
    private MaterialDialog mMaxQuoteDialog;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private View.OnClickListener mOnClickListener;
    private DialogInterface.OnClickListener mOnMaxQuoteClickListener;
    private AdapterView.OnItemSelectedListener mOnTextFontItemSelectListener;
    private AdapterView.OnItemSelectedListener mOnTextSizeItemSelectListener;
    private View.OnTouchListener mOnTouchListener;
    private MaterialDialog.SimpleCallback mRestoreDefaultCallback;
    private MaterialDialog mRestoreDefaultDialog;
    private Setting mSetting;
    private View setting_about;
    private SwitchCompat setting_autoloadimage;
    private View setting_maxquotecount;
    private EditText setting_maxquotecount_edittext;
    private View setting_maxquotecount_ok;
    private View setting_restoredefault;
    private Spinner setting_textfont;
    private View setting_textsize;
    private Spinner setting_textsize_spinner;

    public static Intent create(Context context) {
        return new Intent(context, SettingActivity.class);
    }

    @Override
    public void acquireArguments(Intent intent) {
        mSetting = AcFunRead.getInstance().readSetting();

        readFonts();
    }

    @Override
    public void attachCallbacks() {
        setting_autoloadimage.setOnCheckedChangeListener(mOnCheckedChangeListener);
        setting_maxquotecount_edittext.setOnTouchListener(mOnTouchListener);
        RippleOnClickListener.attach(setting_maxquotecount_ok, mOnClickListener);
        setting_textsize_spinner.setOnItemSelectedListener(mOnTextSizeItemSelectListener);
        setting_textfont.setOnItemSelectedListener(mOnTextFontItemSelectListener);
        RippleOnClickListener.attach(setting_restoredefault, mOnClickListener);
        RippleOnClickListener.attach(setting_about, mOnClickListener);
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.menu_setting);
        setToolbarNavigation();
    }

    @Override
    public void enquiryViews() {
        setting_autoloadimage = (SwitchCompat) findViewById(R.id.setting_autoloadimage);
        setting_maxquotecount = findViewById(R.id.setting_maxquotecount);
        setting_maxquotecount_edittext = (EditText) findViewById(R.id.setting_maxquotecount_edittext);
        setting_maxquotecount_ok = findViewById(R.id.setting_maxquotecount_ok);
        setting_textsize = findViewById(R.id.setting_textsize);
        setting_textsize_spinner = (Spinner) findViewById(R.id.setting_textsize_spinner);
        setting_textfont = (Spinner) findViewById(R.id.setting_textfont);
        setting_restoredefault = findViewById(R.id.setting_restoredefault);
        setting_about = findViewById(R.id.setting_about);

        mMaxQuoteDialog =
                new MaterialDialog.Builder(this).title(R.string.setting_maxquotecount_toobig).positiveText(R.string.app_ok)
                        .negativeText(R.string.app_cancel).callback(mMaxQuoteCallback).build();
        mRestoreDefaultDialog =
                new MaterialDialog.Builder(this).title(R.string.setting_restoredefault_ask).positiveText(R.string.app_ok)
                        .negativeText(R.string.app_cancel).callback(mRestoreDefaultCallback).build();

        RippleDrawable.attach(setting_autoloadimage);
        RippleDrawable.attach(setting_maxquotecount);
        RippleDrawable.attach(setting_maxquotecount_ok, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(setting_textsize);
        RippleDrawable.attach(setting_restoredefault);
        RippleDrawable.attach(setting_about);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.setting_maxquotecount_ok:
                        setMaxQuoteCount();
                        break;
                    case R.id.setting_restoredefault:
                        mRestoreDefaultDialog.show();
                        break;
                    case R.id.setting_about:
                        start(AboutActivity.create(getContext()), Anim.Enter_Left);
                }
            }
        };
        mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!mBlock) {
                    switch (buttonView.getId()) {
                        case R.id.setting_autoloadimage:
                            mSetting.setAutoLoadImage(isChecked);
                            writeSetting();
                    }
                }
            }
        };
        mMaxQuoteCallback = new MaterialDialog.SimpleCallback() {
            @Override
            public void onPositive(MaterialDialog materialDialog) {
                hideMaxQuoteOk();
                mSetting.setMaxQuoteCount(Integer.valueOf(setting_maxquotecount_edittext.getText().toString()));
                writeSetting();
            }
        };
        mOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    showMaxQuoteOk();
                }

                return false;
            }
        };
        mOnTextSizeItemSelectListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mBlock) {
                    mSetting.setDefaultTextSize(Integer.valueOf((String) setting_textsize_spinner.getSelectedItem()));
                    writeSetting();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mOnTextFontItemSelectListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!mBlock) {
                    mSetting.setDefaultTextFont((String) setting_textfont.getSelectedItem());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        };
        mRestoreDefaultCallback = new MaterialDialog.SimpleCallback() {
            @Override
            public void onPositive(MaterialDialog materialDialog) {
                restoreDefault();
            }
        };
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    private void hideMaxQuoteOk() {
        setting_maxquotecount_ok.setVisibility(View.GONE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(setting_maxquotecount_edittext.getWindowToken(), 0);
    }

    @Override
    public void onBackPressed() {
        exit(Anim.Exit_Left);
    }

    @Override
    protected void onDestroy() {
        mRestoreDefaultDialog.dismiss();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onPause(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG) {
            MobclickAgent.onResume(this);
        }
    }

    private void readFonts() {
        String path;

        mFontPathList = AcFunRead.getInstance().readFontsPath();
        int size = mFontPathList.size();
        List<String> fontNameList = new ArrayList<String>(size);
        int i;

        mFontPathList.add(null);
        fontNameList.add(getString(R.string.setting_textfont_default));
        if (size > 0) {
            path = AcFunRead.StorageDir + "/AcFun/" + AcFunRead.DIR_FONTS + "/";
            for (i = 0; i < size; i++) {
                fontNameList.add(mFontPathList.get(i).replace(path, ""));
            }
        }
        mFontAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fontNameList);
        mFontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    private void restoreDefault() {
        mSetting = new Setting();
        AcFunRead.getInstance().writeSetting(mSetting);
        startAction();
    }

    private void setMaxQuoteCount() {
        int maxQuoteCount = Integer.valueOf(setting_maxquotecount_edittext.getText().toString());

        if (maxQuoteCount < 0) {
            showToast(R.string.setting_maxquotecount_toosmall);
        } else if (maxQuoteCount > 9) {
            mMaxQuoteDialog.show();
        } else {
            hideMaxQuoteOk();
            mSetting.setMaxQuoteCount(Integer.valueOf(setting_maxquotecount_edittext.getText().toString()));
            writeSetting();
        }
    }

    private void showMaxQuoteOk() {
        setting_maxquotecount_ok.setVisibility(View.VISIBLE);
    }

    @Override
    public void startAction() {
        mBlock = true;
        setting_autoloadimage.setChecked(mSetting.isAutoLoadImage());
        setting_maxquotecount_edittext.setText(String.valueOf(mSetting.getMaxQuoteCount()));
        setting_textsize_spinner.setSelection((mSetting.getDefaultTextSize() - 12) / 2);
        setting_textfont.setAdapter(mFontAdapter);
        setting_textfont.setSelection(0);
        mBlock = false;
    }

    private void writeSetting() {
        AcFunRead.getInstance().writeSetting(mSetting);
    }
}