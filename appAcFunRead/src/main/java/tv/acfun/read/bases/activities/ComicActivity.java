package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.configs.ImageExecutorConfig;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.utils.GsonUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tv.acfun.read.BuildConfig;
import tv.acfun.read.R;
import tv.acfun.read.bases.application.AcFunRead;
import tv.acfun.read.helpers.ImageConnectionHelper;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class ComicActivity extends ActivityFramework {
    private final static String TAG = "ComicActivity";

    private TextView comic_page;
    private ViewPager comic_pager;
    private EditText comic_save_input;
    private Adapter mAdapter;
    private int mContentId;
    private List<String> mImageList;
    private IRequestCallback<Bitmap> mLoadBitmapCallback;
    private Mode mMode;
    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private PhotoViewAttacher.OnPhotoTapListener mOnTapListener;
    private MaterialDialog.Callback mOverwriteCallback;
    private MaterialDialog mOverwriteDialog;
    private int mPagePosition;
    private int mPosition;
    private MaterialDialog.Callback mSaveCallback;
    private MaterialDialog mSaveDialog;
    private File mSaveFile = null;
    private int mSavePosition = -1;

    public static Intent create(Context context, int contentId, int pagePosition, List<String> imageList, int position) {
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
    public void acquireArguments(Intent intent) {
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
    public void attachCallbacks() {
        comic_pager.setOnPageChangeListener(mOnPageChangeListener);
    }

    private void checkBitmap() {
        if (mSaveFile.exists()) {
            mOverwriteDialog.show();
        } else {
            saveBitmap();
        }
    }

    @Override
    public void createMenu() {
        setToolbarTitle(R.string.app_imageview);
        setToolbarNavigation();
        addToolbarItem(0, R.string.comic_save, R.drawable.image_save_inverse);
    }

    @Override
    public void enquiryViews() {
        comic_pager = (ViewPager) findViewById(R.id.comic_pager);
        comic_page = (TextView) findViewById(R.id.comic_page);

        mSaveDialog = new MaterialDialog.Builder(this).title(R.string.comic_save_input).customView(R.layout.dialog_comic_save)
                .positiveText(R.string.app_ok).negativeText(R.string.app_cancel).callback(mSaveCallback).autoDismiss(false)
                .build();
        mSaveDialog.setCancelable(false);
        comic_save_input = (EditText) mSaveDialog.getCustomView().findViewById(R.id.comic_save_input);

        mOverwriteDialog = new MaterialDialog.Builder(this).title(R.string.comic_save_overwrite).positiveText(R.string.app_ok)
                .negativeText(R.string.app_cancel).callback(mOverwriteCallback).build();

        updatePage();

        mAdapter = new Adapter();
    }

    @Override
    public void establishCallbacks() {
        mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
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
        mOnTapListener = new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                int position = (Integer) view.getTag();

                if (imageCached(position)) {
                    toggleActionBar();
                } else {
                    ImageLoaderHelper.loadImage((ImageView) view, mImageList.get(position), R.drawable.image_loading,
                            R.drawable.image_idle);
                }
            }
        };
        mSaveCallback = new MaterialDialog.Callback() {
            @Override
            public void onNegative(MaterialDialog materialDialog) {
                mSaveDialog.hide();
                mSavePosition = -1;
                mSaveFile = null;
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                String input = comic_save_input.getText().toString();

                if (input.length() == 0) {
                    showToast(getString(R.string.comic_save_empty));
                } else {
                    mSaveFile = getFileByImageName(comic_save_input.getText().toString());
                    mSaveDialog.hide();
                    checkBitmap();
                }
            }
        };
        mOverwriteCallback = new MaterialDialog.Callback() {
            @Override
            public void onNegative(MaterialDialog materialDialog) {
                mSaveDialog.show();
            }

            @Override
            public void onPositive(MaterialDialog materialDialog) {
                saveBitmap();
            }
        };
        mLoadBitmapCallback = new IRequestCallback<Bitmap>() {
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
    public int getLayoutId() {
        return R.layout.activity_comic;
    }

    private boolean imageCached(int position) {
        return ImageExecutorConfig.isImageCacheAvailable(mImageList.get(position));
    }

    @Override
    protected void onDestroy() {
        mSaveDialog.dismiss();
        mOverwriteDialog.dismiss();
        super.onDestroy();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (mSavePosition != -1 || mSaveFile != null) {
            showToast(getString(R.string.comic_save_downloading));
        } else {
            mSavePosition = comic_pager.getCurrentItem();
            comic_save_input.setText(generateFilename());
            comic_save_input.setSelection(comic_save_input.getText().length());
            mSaveDialog.show();
        }

        return false;
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

    private void saveBitmap() {
        if (mSaveFile.exists()) {
            if (mSaveFile.delete()) {
                ImageLoaderHelper.loadBitmap(mImageList.get(mSavePosition), mLoadBitmapCallback);
            } else {
                saveResponse(R.string.comic_save_failure);
            }
        } else {
            ImageLoaderHelper.loadBitmap(mImageList.get(mSavePosition), mLoadBitmapCallback);
        }
    }

    private void saveResponse(int resId) {
        showToast(getString(resId, mSaveFile.getAbsolutePath()));
        mSavePosition = -1;
        mSaveFile = null;
    }

    @Override
    public void startAction() {
        comic_pager.setAdapter(mAdapter);
        comic_pager.setCurrentItem(mPosition);
    }

    private void toggleActionBar() {
        if (isToolbarShowing()) {
            hideToolbar();
        } else {
            showToolbar();
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
            PhotoView photoView =
                    (PhotoView) LayoutInflater.from(getContext()).inflate(R.layout.activity_comic_page, container, false);

            photoView.setTag(position);
            photoView.setMediumScale(2f);
            photoView.setMaximumScale(6f);
            photoView.setOnPhotoTapListener(mOnTapListener);
            if (ImageConnectionHelper.shouldLoadImage() || imageCached(position)) {
                ImageLoaderHelper
                        .loadImage(photoView, mImageList.get(position), R.drawable.image_loading, R.drawable.image_idle);
            } else {
                ImageLoaderHelper
                        .loadImage(photoView, "file://" + AcFunRead.CacheDir + "/" + AcFunRead.DIR_ASSETS + "/web_download",
                                R.drawable.image_loading, R.drawable.image_idle);
            }
            container.addView(photoView);

            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}