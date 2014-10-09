package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.tools.GsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import tv.acfun.read.R;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class ComicActivity extends ActivityFramework {
    private final static String TAG = "ComicActivity";

    private int color_Title;
    private View comic_actionbar;
    private View comic_back;
    private TextView comic_page;
    private View comic_page_background;
    private ViewPager comic_pager;
    private View comic_save;
    private EditText comic_save_input;
    private Adapter mAdapter;
    private IRequestCallback<Bitmap> mCallback;
    private View.OnClickListener mClickListener;
    private int mContentId;
    private ArrayList<String> mImageList;
    private Mode mMode;
    private DialogHelper mOverwriteDialog;
    private DialogInterface.OnClickListener mOverwriteDialogListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private int mPagePosition;
    private int mPosition;
    private DialogHelper mSaveDialog;
    private DialogInterface.OnClickListener mSaveDialogListener;
    private File mSaveFile = null;
    private int mSavePosition = -1;
    private PhotoViewAttacher.OnPhotoTapListener mTapListener;

    public static Intent create(Context context, int contentId, int pagePosition, ArrayList<String> imageList, int position) {
        Intent intent = new Intent(context, ComicActivity.class);

        intent.putExtra("mode", Mode.List);
        intent.putExtra("contentId", contentId);
        intent.putExtra("pagePosition", pagePosition);
        intent.putExtra("imageList", GsonUtil.toString(imageList));
        intent.putExtra("position", position);

        return intent;
    }

    public static Intent create(Context context, String imageUrl) {
        Intent intent = new Intent(context, ComicActivity.class);

        intent.putExtra("mode", Mode.Single);
        intent.putExtra("imageUrl", imageUrl);

        return intent;
    }

    @Override
    public void assignEvents() {
        comic_back.setOnClickListener(mClickListener);
        comic_pager.setOnPageChangeListener(mPageChangeListener);

        comic_save.setOnClickListener(mClickListener);

        mSaveDialog =
                new DialogHelper(getActivity(), R.string.comic_save, R.string.app_ok, R.string.app_cancel, -1, comic_save_input,
                        mSaveDialogListener);
        mOverwriteDialog =
                new DialogHelper(getActivity(), R.string.comic_save_overwrite, R.string.app_ok, R.string.app_cancel, -1,
                        mOverwriteDialogListener);
    }

    private void checkBitmap() {
        if (mSaveFile.exists()) {
            mOverwriteDialog.show();
        } else {
            saveBitmap();
        }
    }

    private String generateFilename() {
        if (mMode == Mode.List) {
            return "ac" + mContentId + "_" + (mPagePosition + 1) + "_" + (mSavePosition + 1);
        } else {
            return String.valueOf(mImageList.get(0).hashCode());
        }
    }

    private File getFileByImageName(String input) {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + "/" + input +
                        ".jpg");
    }

    @Override
    public void initData(Intent intent) {
        mMode = (Mode) intent.getSerializableExtra("mode");

        if (mMode == Mode.List) {
            mContentId = intent.getIntExtra("contentId", 0);
            mPagePosition = intent.getIntExtra("pagePosition", 0);
            mImageList = GsonUtil.toBean(intent.getStringExtra("imageList"), new TypeToken<ArrayList<String>>() {
            }.getType());
            mPosition = intent.getIntExtra("position", 0);
        } else {
            mContentId = 0;
            mPagePosition = 0;
            mImageList = new ArrayList<String>(1);
            mImageList.add(intent.getStringExtra("imageUrl"));
            mPosition = 0;
        }
    }

    @Override
    public void newEvents() {
        mClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.comic_back:
                        onBackPressed();
                        break;
                    case R.id.comic_save:
                        if (mSavePosition != -1 || mSaveFile != null) {
                            showToast(getString(R.string.comic_save_downloading));
                        } else {
                            mSavePosition = comic_pager.getCurrentItem();
                            comic_save_input.setText(generateFilename());
                            comic_save_input.setSelection(comic_save_input.getText().length());
                            mSaveDialog.show();
                        }
                }
            }
        };
        mPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
                updatePage();
            }
        };
        mTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                toggleActionBar();
            }
        };
        mSaveDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input;

                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        input = comic_save_input.getText().toString();
                        if (input.length() == 0) {
                            showToast(getString(R.string.comic_save_empty));
                        } else {
                            mSaveFile = getFileByImageName(comic_save_input.getText().toString());
                            mSaveDialog.hide();
                            checkBitmap();
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mSaveDialog.hide();
                        mSavePosition = -1;
                        mSaveFile = null;
                }
            }
        };
        mOverwriteDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mOverwriteDialog.hide();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveBitmap();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mSaveDialog.show();
                }
            }
        };
        mCallback = new IRequestCallback<Bitmap>() {
            @Override
            public void onFailure(String requestUrl) {
                saveResponse(R.string.comic_save_failure);
            }

            @Override
            public void onSuccess(String requestUrl, Bitmap bitmap) {
                try {
                    if (mSaveFile.createNewFile()) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, new FileOutputStream(mSaveFile));
                        saveResponse(R.string.comic_save_success);
                    } else {
                        saveResponse(R.string.comic_save_failure);
                    }
                } catch (IOException e) {
                    saveResponse(R.string.comic_save_failure);
                }
            }
        };
    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {
    }

    @Override
    public void onActionBarMenuCreate() {

    }

    @Override
    protected void onDestroy() {
        mSaveDialog.hide();
        mOverwriteDialog.hide();
        super.onDestroy();
    }

    @Override
    public void queryLayout() {
        comic_pager = (ViewPager) findContentView(R.id.comic_pager);

        comic_actionbar = findContentView(R.id.comic_actionbar);
        comic_back = findContentView(R.id.comic_back);
        comic_page = (TextView) findContentView(R.id.comic_page);
        comic_page_background = findContentView(R.id.comic_page_background);
        comic_save = findContentView(R.id.comic_save);

        color_Title = getResources().getColor(R.color.Title);

        comic_save_input = (EditText) View.inflate(getActivity(), R.layout.activity_comic_save, null);

        updatePage();

        mAdapter = new Adapter();
    }

    private void saveBitmap() {
        if (mSaveFile.exists()) {
            if (mSaveFile.delete()) {
                ImageLoaderHelper.loadBitmap(mImageList.get(mSavePosition), mCallback);
            } else {
                saveResponse(R.string.comic_save_failure);
            }
        } else {
            ImageLoaderHelper.loadBitmap(mImageList.get(mSavePosition), mCallback);
        }
    }

    private void saveResponse(int resId) {
        showToast(getString(resId, mSaveFile.getAbsolutePath()));
        mSavePosition = -1;
        mSaveFile = null;
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_comic);
    }

    @Override
    public void startAction() {
        comic_pager.setAdapter(mAdapter);
        comic_pager.setCurrentItem(mPosition);
    }

    private void toggleActionBar() {
        if (comic_actionbar.getVisibility() == View.VISIBLE) {
            comic_actionbar.setVisibility(View.GONE);
            comic_page_background.setVisibility(View.VISIBLE);
            comic_page.setTextColor(Color.WHITE);
        } else {
            comic_actionbar.setVisibility(View.VISIBLE);
            comic_page_background.setVisibility(View.GONE);
            comic_page.setTextColor(color_Title);
        }
    }

    private void updatePage() {
        comic_page.setText((mPosition + 1) + " / " + mImageList.size());
    }

    private enum Mode {
        Single,
        List
    }

    private class Adapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((PhotoView) object);
        }

        @Override
        public int getCount() {
            return mImageList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = (PhotoView) View.inflate(getActivity(), R.layout.activity_comic_page, null);

            photoView.setMediumScale(2f);
            photoView.setMaximumScale(6f);
            photoView.setOnPhotoTapListener(mTapListener);
            ImageLoaderHelper.loadImage(photoView, mImageList.get(position));
            container.addView(photoView);

            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}