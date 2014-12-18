package tv.acfun.read.listeners;

import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public interface OnCommentListener {
    public void onCommentClick(Conversion conversion);

    public void onTotalPageChanged(int totalPage);
}