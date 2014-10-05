package tv.acfun.read.bases.activities;

import android.content.Context;
import android.content.Intent;

import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.beans.ActionBarItem;
import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.harreke.easyapp.tools.GsonUtil;

import java.util.ArrayList;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class ComicActivity extends ActivityFramework {
    private ArrayList<String> mImageList;
    private int mPosition;

    public static Intent create(Context context, ArrayList<String> imageList, int position) {
        Intent intent = new Intent(context, ComicActivity.class);

        intent.putExtra("imageList", GsonUtil.toString(imageList));
        intent.putExtra("position", position);

        return intent;
    }

    @Override
    public void assignEvents() {

    }

    @Override
    public void initData(Intent intent) {
        mImageList = GsonUtil.toBean(intent.getStringExtra("imageList"), new TypeToken<ArrayList<String>>() {
        }.getType());
        mPosition = intent.getIntExtra("position", 0);
    }

    @Override
    public void newEvents() {

    }

    @Override
    public void onActionBarItemClick(int position, ActionBarItem item) {

    }

    @Override
    public void onActionBarMenuCreate() {

    }

    @Override
    public void queryLayout() {

    }

    @Override
    public void setLayout() {

    }

    @Override
    public void startAction() {

    }
}
