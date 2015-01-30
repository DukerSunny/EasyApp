package tv.douyu.callback;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/23
 */
public interface OnCaptchaListener {
    public void onBindFailure(String message);

    public void onBindSuccess();

    public void onCaptchaClick();

    public void onObtainFailure(String message);

    public void onObtainSuccess();
}
