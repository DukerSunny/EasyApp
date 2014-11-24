package tv.acfun.read.listeners;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public interface OnCommentListener {
    public void onTotalPageChanged(int totalPage);

    public void onRefresh();
}