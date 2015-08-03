package com.harreke.easyapp.listeners;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/07/24
 *
 * ClickableSpan的监听器
 *
 * @see android.text.style.ClickableSpan
 */
public interface OnTagClickListener {
    /**
     * Span被点击时触发
     *
     * @param tag
     *         Span的标签
     * @param link
     *         Span的链接
     */
    public void onTagClick(String tag, String link);
}