package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.helpers.DialogHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.tools.GsonUtil;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
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
    private View.OnClickListener mClickListener;
    private ArrayList<String> mImageList;
    private DialogHelper mOverwriteDialog;
    private DialogInterface.OnClickListener mOverwriteDialogListener;
    private ViewPager.OnPageChangeListener mPageChangeListener;
    private int mPosition;
    private WeakReference<PhotoView> mReference = null;
    private DialogHelper mSaveDialog;
    private DialogInterface.OnClickListener mSaveDialogListener;
    private PhotoViewAttacher.OnPhotoTapListener mTapListener;

    public static Intent create(Context context, ArrayList<String> imageList, int position) {
        Intent intent = new Intent(context, ComicActivity.class);

        intent.putExtra("imageList", GsonUtil.toString(imageList));
        intent.putExtra("position", position);

        return intent;
    }

    @Override
    public void assignEvents() {
        comic_back.setOnClickListener(mClickListener);
        comic_pager.setOnPageChangeListener(mPageChangeListener);

        comic_save.setOnClickListener(mClickListener);
    }

    private void checkBitmap(String input) {
        File file = getFileByImageName(input);

        if (file.exists()) {
            mOverwriteDialog.show();
        } else {
            saveBitmap(input);
        }
    }

    private File getFileByImageName(String input) {
        return new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + input +
                        ".png");
    }

    @Override
    public void initData(Intent intent) {
        mImageList = GsonUtil.toBean(intent.getStringExtra("imageList"), new TypeToken<ArrayList<String>>() {
        }.getType());
        mPosition = intent.getIntExtra("position", 0);
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
                            mSaveDialog.hide();
                            checkBitmap(input);
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mSaveDialog.hide();
                }
            }
        };
        mOverwriteDialogListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mOverwriteDialog.hide();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        saveBitmap(comic_save_input.getText().toString());
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        mSaveDialog.show();
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
        mSaveDialog = new DialogHelper(getActivity(), R.string.comic_save, R.string.app_ok, R.string.app_cancel, -1,
                comic_save_input);

        updatePage();

        mAdapter = new Adapter();
    }

    private void saveBitmap(String input) {
        ImageView imageView;
        File file = getFileByImageName(input);

        try {
            if (file.exists()) {
                file.delete();
            }
            if (file.createNewFile()) {
                if (mReference != null && mReference.get() != null) {
                    imageView = mReference.get();
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Save bitmap file " + input + "IO error!");
        }
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

            photoView.setOnPhotoTapListener(mTapListener);
            ImageLoaderHelper.loadImage(photoView, mImageList.get(position));
            container.addView(photoView);

            return photoView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            mReference = new WeakReference<PhotoView>((PhotoView) object);
        }
    }
}