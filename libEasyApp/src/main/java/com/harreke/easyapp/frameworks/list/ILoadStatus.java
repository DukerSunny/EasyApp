package com.harreke.easyapp.frameworks.list;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/09
 */
public interface ILoadStatus {
    public void setLoadStatus(LoadStatus loadStatus);

    public void setOnLoadListener(OnLoadListener loadListener);

    public void setVisibility(int visibility);
}
