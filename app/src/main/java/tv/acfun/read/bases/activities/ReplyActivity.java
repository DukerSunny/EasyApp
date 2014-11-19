package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.chiralcode.colorpicker.ColorPicker;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.helpers.PopupAbsListHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.StringUtil;

import java.util.regex.Matcher;

import tv.acfun.read.R;
import tv.acfun.read.api.API;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.holders.EmotHolder;
import tv.acfun.read.holders.SizeSelectHolder;
import tv.acfun.read.widgets.UBBEditText;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/10
 */
public class ReplyActivity extends ActivityFramework {
    private Adapter mAdapter;
    private DialogInterface.OnClickListener mColorPickerClickListener;
    private DialogHelper mColorPickerHelper;
    private int mContentId;
    private int mDefaultColor;
    private int mDefaultSize;
    private int[] mEmotCounts;
    private AdapterView.OnItemClickListener mEmotItemClickListener;
    private String[] mEmotNames;
    private int mFloorIndex;
    private View.OnFocusChangeListener mFocusChangeListener;
    private View.OnClickListener mOnClickListener;
    private int mQuoteId;
    private IRequestCallback<String> mSendCallback;
    private SizePickerHelper mSizePickerHelper;
    private AdapterView.OnItemClickListener mSizePickerItemListener;
    private int mStyleColor;
    private int mStyleSize;
    private ColorPicker picker;
    private UBBEditText reply_input;
    private ViewPager reply_pager;
    private View reply_selectall;
    private View reply_style_bold;
    private View reply_style_color;
    private View reply_style_color_indicator;
    private View reply_style_color_picker;
    private View reply_style_emot_off;
    private View reply_style_emot_on;
    private View reply_style_italic;
    private View reply_style_size;
    private TextView reply_style_size_indicator;
    private View reply_style_size_picker;
    private View reply_style_strikethrough;
    private View reply_style_underline;
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
        mEmotCounts = getResources().getIntArray(R.array.emot_count);
    }

    @Override
    public void attachCallbacks() {
        reply_style_bold.setOnClickListener(mOnClickListener);
        reply_style_italic.setOnClickListener(mOnClickListener);
        reply_style_underline.setOnClickListener(mOnClickListener);
        reply_style_strikethrough.setOnClickListener(mOnClickListener);
        reply_style_color.setOnClickListener(mOnClickListener);
        reply_style_color_picker.setOnClickListener(mOnClickListener);
        reply_style_size.setOnClickListener(mOnClickListener);
        reply_style_size_picker.setOnClickListener(mOnClickListener);
        reply_style_emot_on.setOnClickListener(mOnClickListener);
        reply_style_emot_off.setOnClickListener(mOnClickListener);
        reply_selectall.setOnClickListener(mOnClickListener);
        reply_unselect.setOnClickListener(mOnClickListener);

        reply_input.setOnFocusChangeListener(mFocusChangeListener);

        mColorPickerHelper.setPositiveButton(R.string.app_ok);
        mColorPickerHelper.setNegativeButton(R.string.app_cancel);
        mColorPickerHelper.setNeutralButton(R.string.app_reset);
        mColorPickerHelper.setOnClickListener(mColorPickerClickListener);

        mSizePickerHelper.setOnItemClickListener(mSizePickerItemListener);
    }

    private void doSelectAll() {
        if (reply_input.getText().length() > 0) {
            reply_input.selectAllWithAction();
            reply_selectall.setVisibility(View.GONE);
            reply_unselect.setVisibility(View.VISIBLE);
        }
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
        reply_selectall.setVisibility(View.VISIBLE);
        reply_unselect.setVisibility(View.GONE);
    }

    @Override
    public void enquiryViews() {
        PagerTabStrip reply_pager_indicator;
        View dialog_colorpicker;

        if (mFloorIndex > 0) {
            setActionBarTitle(getString(R.string.reply_id_quote, "#" + mFloorIndex));
        } else {
            setActionBarTitle(R.string.reply_id);
        }
        addActionBarImageItem(0, R.drawable.image_send);

        reply_style_bold = findViewById(R.id.reply_style_bold);
        reply_style_italic = findViewById(R.id.reply_style_italic);
        reply_style_underline = findViewById(R.id.reply_style_underline);
        reply_style_strikethrough = findViewById(R.id.reply_style_strikethrough);
        reply_style_color = findViewById(R.id.reply_style_color);
        reply_style_color_indicator = findViewById(R.id.reply_style_color_indicator);
        reply_style_color_picker = findViewById(R.id.reply_style_color_picker);
        reply_style_size = findViewById(R.id.reply_style_size);
        reply_style_size_indicator = (TextView) findViewById(R.id.reply_style_size_indicator);
        reply_style_size_picker = findViewById(R.id.reply_style_size_picker);
        reply_style_emot_on = findViewById(R.id.reply_style_emot_on);
        reply_style_emot_off = findViewById(R.id.reply_style_emot_off);

        reply_input = (UBBEditText) findViewById(R.id.reply_input);
        reply_pager = (ViewPager) findViewById(R.id.reply_pager);
        reply_pager_indicator = (PagerTabStrip) findViewById(R.id.reply_pager_indicator);
        reply_pager_indicator.setTabIndicatorColorResource(R.color.Theme);
        reply_pager_indicator.setTextColor(getResources().getColor(R.color.Title));
        reply_selectall = findViewById(R.id.reply_selectall);
        reply_unselect = findViewById(R.id.reply_unselect);

        mAdapter = new Adapter();

        mDefaultColor = getResources().getColor(R.color.Content);
        mStyleColor = mDefaultColor;
        dialog_colorpicker = View.inflate(getActivity(), R.layout.dialog_colorpicker, null);
        picker = (ColorPicker) dialog_colorpicker.findViewById(R.id.picker);
        picker.setColor(mDefaultColor);
        mColorPickerHelper = new DialogHelper(getActivity());
        mColorPickerHelper.setTitle(R.string.reply_pickcolor);
        mColorPickerHelper.setView(dialog_colorpicker);

        mDefaultSize = 16;
        mStyleSize = mDefaultSize;
        mSizePickerHelper = new SizePickerHelper(getActivity(), reply_style_size_picker);
        mSizePickerHelper.add(0, 10);
        mSizePickerHelper.add(1, 12);
        mSizePickerHelper.add(2, 16);
        mSizePickerHelper.add(3, 18);
        mSizePickerHelper.add(4, 24);
        mSizePickerHelper.add(5, 32);
        mSizePickerHelper.add(6, 48);
    }

    @Override
    public void establishCallbacks() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.reply_style_bold:
                        reply_input.toggleBold();
                        break;
                    case R.id.reply_style_italic:
                        reply_input.toggleItalic();
                        break;
                    case R.id.reply_style_underline:
                        reply_input.toggleUnderline();
                        break;
                    case R.id.reply_style_strikethrough:
                        reply_input.toggleStrikethrough();
                        break;
                    case R.id.reply_style_color:
                        reply_input.setColor(mStyleColor);
                        break;
                    case R.id.reply_style_color_picker:
                        mColorPickerHelper.show();
                        break;
                    case R.id.reply_style_size:
                        reply_input.setSize(mStyleSize);
                        break;
                    case R.id.reply_style_size_picker:
                        mSizePickerHelper.show();
                        break;
                    case R.id.reply_style_emot_on:
                        hideEmot();
                        showSoftInputMethod();
                        break;
                    case R.id.reply_style_emot_off:
                        showEmot();
                        hideSoftInputMethod();
                        break;
                    case R.id.reply_selectall:
                        doSelectAll();
                        break;
                    case R.id.reply_unselect:
                        doUnselect();
                }
            }
        };
        mColorPickerClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mColorPickerHelper.hide();
                        mStyleColor = picker.getColor();
                        reply_style_color_indicator.setBackgroundColor(mStyleColor);
                        break;
                    case DialogInterface.BUTTON_NEUTRAL:
                        picker.setColor(mDefaultColor);
                        reply_style_color_indicator.setBackgroundColor(mDefaultColor);
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mColorPickerHelper.hide();
                }
            }
        };
        mSizePickerItemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSizePickerHelper.hide();
                mStyleSize = mSizePickerHelper.getItem(position);
                reply_style_size_indicator.setText(String.valueOf(mStyleSize));
            }
        };
        mEmotItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                reply_input.insertEmot(mEmotNames[(Integer) parent.getTag()], position);
            }
        };
        mFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showSoftInputMethod();
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
    }

    private void hideEmot() {
        reply_pager.setVisibility(View.GONE);
        reply_style_emot_on.setVisibility(View.GONE);
        reply_style_emot_off.setVisibility(View.VISIBLE);
    }

    private void hideSoftInputMethod() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(reply_input.getWindowToken(), 0);
    }

    @Override
    public void onActionBarItemClick(int id, View item) {
        doSend();
    }

    @Override
    protected void onDestroy() {
        mColorPickerHelper.hide();
        mSizePickerHelper.hide();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        AcFunRead.getInstance().writeString("replyText", reply_input.getUBBText());
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reply_input.setUBBText(AcFunRead.getInstance().readString("replyText", ""));
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_reply);
    }

    private void showEmot() {
        reply_pager.setVisibility(View.VISIBLE);
        reply_style_emot_on.setVisibility(View.VISIBLE);
        reply_style_emot_off.setVisibility(View.GONE);
    }

    private void showSoftInputMethod() {
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).showSoftInput(reply_input, 0);
    }

    @Override
    public void startAction() {
        reply_pager.setAdapter(mAdapter);
    }

    private class Adapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((GridView) object);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mEmotNames[position];
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = (GridView) View.inflate(getActivity(), R.layout.activity_reply_emot, null);

            gridView.setTag(position);
            gridView.setOnItemClickListener(mEmotItemClickListener);
            gridView.setAdapter(new EmotListAdapter(position));
            container.addView(gridView);

            return gridView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    private class EmotListAdapter extends BaseAdapter {
        private int mIndex;

        private EmotListAdapter(int index) {
            mIndex = index;
        }

        @Override
        public int getCount() {
            return mEmotCounts[mIndex];
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            EmotHolder holder;

            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_emot, null);
                holder = new EmotHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (EmotHolder) convertView.getTag();
            }
            holder.setItem(position, mEmotNames[mIndex]);

            return convertView;
        }
    }

    private class SizePickerHelper extends PopupAbsListHelper<Integer, SizeSelectHolder> {
        public SizePickerHelper(Context context, View anchor) {
            super(context, anchor);
        }

        @Override
        public SizeSelectHolder createHolder(View convertView) {
            return new SizeSelectHolder(convertView);
        }

        @Override
        public View createView() {
            return View.inflate(getActivity(), R.layout.item_sizeselect, null);
        }
    }
}