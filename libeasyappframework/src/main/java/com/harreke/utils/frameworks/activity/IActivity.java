package com.harreke.utils.frameworks.activity;

import android.content.Intent;

/**
 Activity框架的接口
 */
public interface IActivity {
    /**
     初始化Activity传参数据
     */
    public void initData(Intent intent);

    /**
     当Activity的菜单被创建时触发
     */
    public void onMenuCreate();
}