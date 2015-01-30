package com.harreke.easyapp.pages;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/06
 */
public interface IPageActionListener {
    /**
     * 当前页面改变的时候触发
     *
     * @param position
     *         当前页面位置
     */
    public void onPageChange(int position);

    /**
     * 销毁某一页面的时候触发
     *
     * @param position
     *         页面位置
     */
    public void onPageDestroy(int position);
}