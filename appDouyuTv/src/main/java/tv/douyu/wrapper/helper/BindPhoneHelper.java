package tv.douyu.wrapper.helper;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.harreke.easyapp.helpers.HttpLoaderHelper;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.requests.IRequestCallback;
import com.harreke.easyapp.requests.IRequestExecutor;
import com.harreke.easyapp.utils.ResourceUtil;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.callback.OnCaptchaListener;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/22
 */
public class BindPhoneHelper implements View.OnClickListener {
    private String bind_failure;
    private String connection_failure;
    private ImageView mImageView;
    private OnCaptchaListener mOnCaptchaListener = null;
    private IRequestExecutor mRequestExecutor = null;
    private IRequestCallback<String> mBindCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(String requestUrl) {
            Log.e(null, "bind failure");
            mRequestExecutor = null;
            if (mOnCaptchaListener != null) {
                mOnCaptchaListener.onBindFailure(connection_failure);
            }
        }

        @Override
        public void onSuccess(String requestUrl, String result) {
            Log.e(null, "bind success " + result);
            ObjectResult<String> objectResult = Parser.parseString(result, "result", "data", "data");

            mRequestExecutor = null;
            if (mOnCaptchaListener != null) {
                if (objectResult != null) {
                    if (objectResult.getFlag() == 0) {
                        mOnCaptchaListener.onBindSuccess();
                    } else {
                        mOnCaptchaListener.onBindFailure(bind_failure + objectResult.getMessage());
                    }
                } else {
                    mOnCaptchaListener.onBindFailure(bind_failure);
                }
            }
        }
    };
    private IRequestCallback<ImageView> mCaptchaImageCallback = new IRequestCallback<ImageView>() {
        @Override
        public void onFailure(String requestUrl) {
            mRequestExecutor = null;
            mImageView.setImageResource(R.drawable.retry_16x9);
        }

        @Override
        public void onSuccess(String requestUrl, ImageView imageView) {
            mRequestExecutor = null;
        }
    };
    private String obtain_failure;
    private IRequestCallback<String> mObtainCallback = new IRequestCallback<String>() {
        @Override
        public void onFailure(String requestUrl) {
            Log.e(null, "obtain failure");
            mRequestExecutor = null;
            if (mOnCaptchaListener != null) {
                mOnCaptchaListener.onObtainFailure(connection_failure);
            }
        }

        @Override
        public void onSuccess(String requestUrl, String result) {
            Log.e(null, "obtain result=" + result);
            ObjectResult<String> objectResult = Parser.parseString(result, "result", "data", "data");

            mRequestExecutor = null;
            if (mOnCaptchaListener != null) {
                if (objectResult != null) {
                    if (objectResult.getFlag() == 0) {
                        mOnCaptchaListener.onObtainSuccess();
                    } else {
                        mOnCaptchaListener.onObtainFailure(obtain_failure + objectResult.getMessage());
                    }
                } else {
                    mOnCaptchaListener.onObtainFailure(obtain_failure);
                }
            }
        }
    };

    public BindPhoneHelper(ImageView imageView) {
        Context context = mImageView.getContext();

        mImageView = imageView;
        mImageView.setOnClickListener(this);
        bind_failure = ResourceUtil.getString(context, R.string.bind_failure);
        obtain_failure = ResourceUtil.getString(context, R.string.bind_phone_failure);
        connection_failure = ResourceUtil.getString(context, R.string.connection_failure);
    }

    public void bindDomestic(String captchaCode, String validateCode) {
        if (mRequestExecutor == null) {
            mRequestExecutor = HttpLoaderHelper
                    .loadString(mImageView.getContext(), API.postBindDomestic(captchaCode, validateCode), mBindCallback);
        }
    }

    public void bindForeign(String phone, String captchaCode) {
        if (mRequestExecutor == null) {
            mRequestExecutor = HttpLoaderHelper
                    .loadString(mImageView.getContext(), API.postBindForeign(phone, captchaCode), mBindCallback);
        }
    }

    public void obtainValidateCodeViaMail(String phone, String captchaCode) {
        if (mRequestExecutor == null) {
            mRequestExecutor = HttpLoaderHelper
                    .loadString(mImageView.getContext(), API.postValidateCodeViaMail(phone, captchaCode), mObtainCallback);
        }
    }

    public void obtainValidateCodeViaVoice(String phone, String captchaCode) {
        if (mRequestExecutor == null) {
            mRequestExecutor = HttpLoaderHelper
                    .loadString(mImageView.getContext(), API.postValidateCodeViaVoice(phone, captchaCode), mObtainCallback);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnCaptchaListener != null) {
            mOnCaptchaListener.onCaptchaClick();
        }
    }

    public void setOnCaptchaListener(OnCaptchaListener onCaptchaListener) {
        mOnCaptchaListener = onCaptchaListener;
    }

    public void updateDomesticImage() {
        if (mRequestExecutor == null) {
            mImageView.setImageResource(R.drawable.loading_16x9);
            mRequestExecutor = ImageLoaderHelper.loadImage(mImageView, API.postDomesticCaptchaImage(), mCaptchaImageCallback);
        }
    }

    public void updateForeignImage() {
        if (mRequestExecutor == null) {
            mImageView.setImageResource(R.drawable.loading_16x9);
            mRequestExecutor = ImageLoaderHelper.loadImage(mImageView, API.postForeignCaptchaImage(), mCaptchaImageCallback);
        }
    }
}