package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.widgets.ChildTabView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

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
    private DialogHelper mMaxQuoteDialog;
    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private View.OnClickListener mOnClickListener;
    private DialogInterface.OnClickListener mOnMaxQuoteClickListener;
    private AdapterView.OnItemSelectedListener mOnTextFontItemSelectListener;
    private AdapterView.OnItemSelectedListener mOnTextSizeItemSelectListener;
    private View.OnTouchListener mOnTouchListener;
    private Setting mSetting;
    private ChildTabView setting_autoloadimage;
    private EditText setting_maxquotecount;
    private View setting_maxquotecount_ok;
    private View setting_restoredefault;
    private Spinner setting_textfont;
    private Spinner setting_textsize;

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
        setting_maxquotecount.setOnTouchListener(mOnTouchListener);
        setting_maxquotecount_ok.setOnClickListener(mOnClickListener);
        setting_textsize.setOnItemSelectedListener(mOnTextSizeItemSelectListener);
        setting_textfont.setOnItemSelectedListener(mOnTextFontItemSelectListener);
        setting_restoredefault.setOnClickListener(mOnClickListener);
    }

    @Override
    public void enquiryViews() {
        setActionBarTitle(R.string.menu_setting);

        setting_autoloadimage = (ChildTabView) findViewById(R.id.setting_autoloadimage);
        setting_maxquotecount = (EditText) findViewById(R.id.setting_maxquotecount);
        setting_maxquotecount_ok = findViewById(R.id.setting_maxquotecount_ok);
        setting_textsize = (Spinner) findViewById(R.id.setting_textsize);
        setting_textfont = (Spinner) findViewById(R.id.setting_textfont);
        setting_restoredefault = findViewById(R.id.setting_restoredefault);

        mMaxQuoteDialog = new DialogHelper(this);
        mMaxQuoteDialog.setTitle(R.string.setting_maxquotecount_toobig);
        mMaxQuoteDialog.setPositiveButton(R.string.app_ok);
        mMaxQuoteDialog.setNegativeButton(R.string.app_cancel);
        mMaxQuoteDialog.setOnClickListener(mOnMaxQuoteClickListener);
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
                        restoreDefault();
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
        mOnMaxQuoteClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMaxQuoteDialog.hide();
                hideMaxQuoteOk();
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    mSetting.setMaxQuoteCount(Integer.valueOf(setting_maxquotecount.getText().toString()));
                    writeSetting();
                }
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
                    mSetting.setDefaultTextSize(Integer.valueOf((String) setting_textsize.getSelectedItem()));
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
    }

    private void hideMaxQuoteOk() {
        setting_maxquotecount_ok.setVisibility(View.GONE);
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(setting_maxquotecount.getWindowToken(), 0);
    }

    @Override
    public void onActionBarItemClick(int id, View item) {
    }

    @Override
    public void onBackPressed() {
        exit(false);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
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

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_setting);
    }

    private void setMaxQuoteCount() {
        int maxQuoteCount = Integer.valueOf(setting_maxquotecount.getText().toString());

        if (maxQuoteCount < 0) {
            showToast(R.string.setting_maxquotecount_toosmall);
        } else if (maxQuoteCount > 9) {
            mMaxQuoteDialog.show();
        } else {
            hideMaxQuoteOk();
            mSetting.setMaxQuoteCount(Integer.valueOf(setting_maxquotecount.getText().toString()));
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
        setting_maxquotecount.setText(String.valueOf(mSetting.getMaxQuoteCount()));
        setting_textsize.setSelection((mSetting.getDefaultTextSize() - 12) / 2);
        setting_textfont.setAdapter(mFontAdapter);
        setting_textfont.setSelection(0);
        mBlock = false;
    }

    private void writeSetting() {
        AcFunRead.getInstance().writeSetting(mSetting);
    }
}