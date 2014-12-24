package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.astuetz.PagerSlidingTabStrip;
import com.chiralcode.colorpicker.ColorPicker;
import com.harreke.easyapp.adapters.fragment.FragmentPageAdapter;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.StringUtil;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.umeng.analytics.MobclickAgent;

import java.util.regex.Matcher;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.bases.fragments.EmotFragment;
import tv.acfun.read.listeners.OnEmotClickListener;
import tv.acfun.read.widgets.UBBEditText;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/10
 */
public class ReplyActivity extends ActivityFramework implements OnEmotClickListener {
    private ColorPicker mColorPicker;
    private MaterialDialog.FullCallback mColorPickerCallback;
    private MaterialDialog mColorPickerDialog;
    private int mContentId;
    private int mDefaultColor;
    private int mDefaultSize;
    private int[] mEmotCounts;
    private String[] mEmotIds;
    private String[] mEmotNames;
    private EmotPageAdapter mEmotPageAdapter;
    private int mFloorIndex;
    private View.OnFocusChangeListener mFocusChangeListener;
    private View.OnClickListener mOnClickListener;
    private UBBEditText.OnSelectionChangeListener mOnSelectionChangeListener;
    private int mQuoteId;
    private IRequestCallback<String> mSendCallback;
    private MaterialDialog mSizePickerDialog;
    private MaterialDialog.FullCallback mSizePickerFullCallback;
    private MaterialDialog.ListCallback mSizePickerListCallback;
    //    private SizePickerHelper mSizePickerHelper;
    private int mTextColor;
    private int mTextSize;
    private View reply_emot_off;
    private View reply_emot_on;
    private UBBEditText reply_input;
    private ViewPager reply_pager;
    private PagerSlidingTabStrip reply_pager_strip;
    private View reply_selectall;
    private View reply_text_bold;
    private View reply_text_color;
    private TextView reply_text_color_indicator;
    private View reply_text_color_picker;
    //    private View reply_text_italic;
    private View reply_text_size;
    private TextView reply_text_size_indicator;
    private View reply_text_size_picker;
    private View reply_text_strikethrough;
    private View reply_text_underline;
    private View reply_unselect;

    public static Intent create(Context context, int contentId, int quoteId, int floorIndex) {
        Intent intent = new Intent(context, ReplyActivity.class);

        intent.putExtra("contentId", contentId);
        intent.putExtra("quoteId", quoteId);
        intent.putExtra("floorIndex", floorIndex);

        return intent;
    }

    @Override
    public void acquireArguments(Intent intent) {
        mContentId = intent.getIntExtra("contentId", 0);
        mQuoteId = intent.getIntExtra("quoteId", 0);
        mFloorIndex = intent.getIntExtra("floorIndex", 0);

        mEmotNames = getResources().getStringArray(R.array.emot_name);
        mEmotIds = getResources().getStringArray(R.array.emot_id);
        mEmotCounts = getResources().getIntArray(R.array.emot_count);
    }

    @Override
    public void attachCallbacks() {
        reply_text_bold.setOnClickListener(mOnClickListener);
        reply_text_underline.setOnClickListener(mOnClickListener);
        //        reply_text_italic.setOnClickListener(mOnClickListener);
        reply_text_strikethrough.setOnClickListener(mOnClickListener);
        reply_text_color.setOnClickListener(mOnClickListener);
        reply_text_color_picker.setOnClickListener(mOnClickListener);
        reply_text_size.setOnClickListener(mOnClickListener);
        reply_text_size_picker.setOnClickListener(mOnClickListener);
        reply_emot_on.setOnClickListener(mOnClickListener);
        reply_emot_off.setOnClickListener(mOnClickListener);
        reply_selectall.setOnClickListener(mOnClickListener);
        reply_unselect.setOnClickListener(mOnClickListener);

        reply_input.setOnSelectionChangeListener(mOnSelectionChangeListener);
        reply_input.setOnFocusChangeListener(mFocusChangeListener);
    }

    @Override
    public void createMenu() {
        if (mFloorIndex > 0) {
            setToolbarTitle(getString(R.string.reply_id_quote, "#" + mFloorIndex));
        } else {
            setToolbarTitle(R.string.reply_id);
        }
        setToolbarNavigation();
        addToolbarItem(0, R.string.comment_send, R.drawable.image_send_inverse);
    }

    private void doSend() {
        AcFunRead acFunRead;
        String originalContent = reply_input.getUBBText();
        Matcher matcher;
        String temp;
        String result;

        hideEmot();
        hideSoftInputMethod();

        if (originalContent.length() < 5) {
            showToast(R.string.comment_tooshort);
        } else {
            result = "";
            temp = originalContent.replaceAll("\\[[/a-z](.?|[\\S\\s]+?)\\]", "");
            matcher = StringUtil.getMatcher("([\\u4e00-\\u9fa5]+)|([0-9a-zA-Z]+)", temp);
            while (matcher.find()) {
                result += matcher.group(0);
            }
            if (result.length() == 0) {
                showToast(R.string.comment_onlyemot);
            } else if (result.length() < 5) {
                showToast(R.string.comment_tooshort);
            } else {
                acFunRead = AcFunRead.getInstance();
                showToast(R.string.comment_sending, true);
                executeRequest(API.getCommentAdd(mContentId, acFunRead.readFullUser().getUserId(), originalContent, mQuoteId,
                        acFunRead.readToken()), mSendCallback);
            }
        }
    }

    private void doUnselect() {
        reply_input.unselect();
        showSelectAll();
    }

    @Override
    public void enquiryViews() {
        reply_text_bold = findViewById(R.id.reply_text_bold);
        //        reply_text_italic = findViewById(R.id.reply_text_italic);
        reply_text_underline = findViewById(R.id.reply_text_underline);
        reply_text_strikethrough = findViewById(R.id.reply_text_strikethrough);
        reply_text_color = findViewById(R.id.reply_text_color);
        reply_text_color_indicator = (TextView) findViewById(R.id.reply_text_color_indicator);
        reply_text_color_picker = findViewById(R.id.reply_text_color_picker);
        reply_text_size = findViewById(R.id.reply_text_size);
        reply_text_size_indicator = (TextView) findViewById(R.id.reply_text_size_indicator);
        reply_text_size_picker = findViewById(R.id.reply_text_size_picker);
        reply_emot_on = findViewById(R.id.reply_emot_on);
        reply_emot_off = findViewById(R.id.reply_emot_off);

        reply_input = (UBBEditText) findViewById(R.id.reply_input);
        reply_pager = (ViewPager) findViewById(R.id.reply_pager);
        reply_pager_strip = (PagerSlidingTabStrip) findViewById(R.id.reply_pager_strip);
        reply_selectall = findViewById(R.id.reply_selectall);
        reply_unselect = findViewById(R.id.reply_unselect);

        reply_pager_strip.setTextColor(Color.DKGRAY);
        reply_pager_strip.setTextSize((int) getResources().getDimension(R.dimen.Subhead));

        mEmotPageAdapter = new EmotPageAdapter(getSupportFragmentManager());

        mDefaultColor = Color.BLACK;
        mTextColor = mDefaultColor;

        mColorPickerDialog =
                new MaterialDialog.Builder(this).title(R.string.reply_pickcolor).customView(R.layout.dialog_colorpicker)
                        .positiveText(R.string.app_ok).negativeText(R.string.app_cancel).neutralText(R.string.app_reset)
                        .callback(mColorPickerCallback).build();
        mColorPicker = (ColorPicker) mColorPickerDialog.getCustomView().findViewById(R.id.colorpicker);
        mColorPicker.setColor(mDefaultColor);

        mDefaultSize = 16;
        mTextSize = mDefaultSize;

        mSizePickerDialog = new MaterialDialog.Builder(this).title(R.string.reply_picksize).items(R.array.reply_text_size).
                neutralText(R.string.app_reset).negativeText(R.string.app_cancel).callback(mSizePickerFullCallback)
                .itemsCallback(mSizePickerListCallback).build();

        RippleDrawable.attach(reply_text_bold, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(reply_text_underline, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(reply_text_strikethrough, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(reply_text_color, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(reply_text_size, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(reply_emot_on, RippleDrawable.RIPPLE_STYLE_DARK_SQUARE);
        RippleDrawable.attach(reply_emot_off, RippleDrawable.RIPPLE_STYLE_LIGHT_SQUARE);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.reply_text_bold:
                        reply_input.toggleBold();
                        break;
                    //                    case R.id.reply_text_italic:
                    //                        reply_input.toggleItalic();
                    //                        break;
                    case R.id.reply_text_underline:
                        reply_input.toggleUnderline();
                        break;
                    case R.id.reply_text_strikethrough:
                        reply_input.toggleStrikethrough();
                        break;
                    case R.id.reply_text_color:
                        reply_input.setColor(mTextColor);
                        break;
                    case R.id.reply_text_color_picker:
                        mColorPickerDialog.show();
                        break;
                    case R.id.reply_text_size:
                        reply_input.setSize(mTextSize);
                        break;
                    case R.id.reply_text_size_picker:
                        mSizePickerDialog.show();
                        break;
                    case R.id.reply_emot_on:
                        showEmot();
                        hideSoftInputMethod();
                        break;
                    case R.id.reply_emot_off:
                        hideEmot();
                        showSoftInputMethod();
                        break;
                    case R.id.reply_selectall:
                        selectAll();
                        break;
                    case R.id.reply_unselect:
                        doUnselect();
                }
            }
        };
        mColorPickerCallback = new MaterialDialog.FullCallback() {
            @Override
            public void onNegative(MaterialDialog materialDialog) {
            }

            @Override
            public void onNeutral(MaterialDialog materialDialog) {
                resetTextColor();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                setTextColor(mColorPicker.getColor());
            }
        };
        mSizePickerFullCallback = new MaterialDialog.FullCallback() {
            @Override
            public void onNegative(MaterialDialog materialDialog) {
            }

            @Override
            public void onNeutral(MaterialDialog materialDialog) {
                resetTextSize();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
            }
        };
        mSizePickerListCallback = new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence item) {
                setTextSize(Integer.valueOf(item.toString()));
            }
        };
        mFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSoftInputMethod();
                    reply_pager_strip.setVisibility(View.GONE);
                    reply_pager.setVisibility(View.GONE);
                } else {
                    hideSoftInputMethod();
                }
            }
        };
        mSendCallback = new IRequestCallback<String>() {
            @Override
            public void onFailure(String requestUrl) {
                showToast(R.string.comment_failure);
            }

            @Override
            public void onSuccess(String requestUrl, String s) {
                reply_input.setText("");
                setResult(RESULT_OK);
                onBackPressed();
            }
        };
        mOnSelectionChangeListener = new UBBEditText.OnSelectionChangeListener() {
            @Override
            public void onSelectionChanged(int start, int end) {
                if (start != end) {
                    showUnselect();
                } else {
                    showSelectAll();
                }
            }
        };
    }

    private void hideEmot() {
        reply_pager_strip.setVisibility(View.GONE);
        reply_pager.setVisibility(View.GONE);
        reply_emot_on.setVisibility(View.VISIBLE);
        reply_emot_off.setVisibility(View.GONE);
    }

    private void hideSoftInputMethod() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(reply_input.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        mColorPickerDialog.dismiss();
        mSizePickerDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public void onEmotClick(String emotName, String emotId, int emotPosition) {
        reply_input.insertEmot(emotId, emotPosition);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        doSend();

        return false;
    }

    @Override
    protected void onPause() {
        AcFunRead.getInstance().writeString("replyText", reply_input.getUBBText());
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

        reply_input.setUBBText(AcFunRead.getInstance().readString("replyText", ""));
    }

    private void resetTextColor() {
        setTextColor(mDefaultColor);
    }

    private void resetTextSize() {
        setTextSize(mDefaultSize);
    }

    private void selectAll() {
        if (reply_input.getText().length() > 0) {
            reply_input.selectAllWithAction();
            showUnselect();
        }
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_reply);
    }

    private void setTextColor(int textColor) {
        mTextColor = textColor;
        mColorPicker.setColor(mTextColor);
        reply_text_color_indicator.setTextColor(mTextColor);
    }

    private void setTextSize(int textSize) {
        mTextSize = textSize;
        reply_text_size_indicator.setText(String.valueOf(mTextSize));
    }

    private void showEmot() {
        reply_pager_strip.setVisibility(View.VISIBLE);
        reply_pager.setVisibility(View.VISIBLE);
        reply_emot_off.setVisibility(View.VISIBLE);
        reply_emot_on.setVisibility(View.GONE);
    }

    private void showSelectAll() {
        reply_selectall.setVisibility(View.VISIBLE);
        reply_unselect.setVisibility(View.GONE);
    }

    private void showSoftInputMethod() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(reply_input, 0);
    }

    private void showUnselect() {
        reply_selectall.setVisibility(View.GONE);
        reply_unselect.setVisibility(View.VISIBLE);
    }

    @Override
    public void startAction() {
        reply_pager.setAdapter(mEmotPageAdapter);
        reply_pager_strip.setViewPager(reply_pager);
    }

    private class EmotPageAdapter extends FragmentPageAdapter {
        public EmotPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Fragment getItem(int position) {
            return EmotFragment.create(mEmotNames[position], mEmotIds[position], mEmotCounts[position]);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mEmotNames[position];
        }
    }

    //    private class SizePickerHelper extends PopupAbsListHelper<Integer, SizeSelectHolder> {
    //        public SizePickerHelper(Context context, View anchor) {
    //            super(context, anchor);
    //        }
    //
    //        @Override
    //        public SizeSelectHolder createHolder(View convertView) {
    //            return new SizeSelectHolder(convertView);
    //        }
    //
    //        @Override
    //        public View createView() {
    //            return View.inflate(getActivity(), R.layout.item_sizeselect, null);
    //        }
    //    }
}