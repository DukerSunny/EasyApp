package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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

    private TextView comic_page;
    private ViewPager comic_pager;
    private EditText comic_save_input;
    private Adapter mAdapter;
    private IRequestCallback<Bitmap> mCallback;
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
        comic_pager.setOnPageChangeListener(mPageChangeListener);

        mSaveDialog = new DialogHelper(getActivity());
        mSaveDialog.setTitle(R.string.comic_save_input);
        mSaveDialog.setView(R.layout.dialog_comic_save);
        mSaveDialog.setPositiveButton(R.string.app_ok);
        mSaveDialog.setPositiveButton(R.string.app_cancel);
        mSaveDialog.setOnClickListener(mSaveDialogListener);

        mOverwriteDialog = new DialogHelper(getActivity());
        mOverwriteDialog.setTitle(R.string.comic_save_overwrite);
        mOverwriteDialog.setPositiveButton(R.string.app_ok);
        mOverwriteDialog.setNegativeButton(R.string.app_cancel);
        mOverwriteDialog.setOnClickListener(mOverwriteDialogListener);
    }

    private void checkBitmap() {
        if (mSaveFile.exists()) {
            mOverwriteDialog.show();
        } else {
            saveBitmap();
        }
    }

    @Override
    public void configActivity() {
        setActionBarMode(ActionBarMode.Overlay);
    }

    @Override
    public void enquiryViews() {
        addActionBarImageItem(0, R.drawable.image_save);

        comic_pager = (ViewPager) findViewById(R.id.comic_pager);
        comic_page = (TextView) findViewById(R.id.comic_page);

        comic_save_input = (EditText) View.inflate(getActivity(), R.layout.dialog_comic_save, null);

        updatePage();

        mAdapter = new Adapter();
    }

    @Override
    public void establishCallbacks() {
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
    public void onActionBarItemClick(int id, View item) {
        if (mSavePosition != -1 || mSaveFile != null) {
            showToast(getString(R.string.comic_save_downloading));
        } else {
            mSavePosition = comic_pager.getCurrentItem();
            comic_save_input.setText(generateFilename());
            comic_save_input.setSelection(comic_save_input.getText().length());
            mSaveDialog.show();
        }
    }

    @Override
    protected void onDestroy() {
        mSaveDialog.hide();
        mOverwriteDialog.hide();
        super.onDestroy();
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
        if (isActionBarShowing()) {
            hideActionBar();
        } else {
            showActionBar();
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