package com.harreke.easyapp.frameworks.list.recyclerview;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/31
 */
@Deprecated
public interface IRecyclerItemClickListener {
    /**
     * 当列表某一条目被点击时触发
     *
     * @param position
     *         条目位置
     */
    public void onItemClick(int position);
}