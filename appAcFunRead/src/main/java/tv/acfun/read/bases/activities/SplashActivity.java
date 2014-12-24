package tv.acfun.read.bases.activities;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.harreke.easyapp.frameworks.bases.activity.ActivityFramework;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/23
 */
public class SplashActivity extends ActivityFramework {
    private ViewPropertyAnimator mNameAnimator;
    private int mRootBottom;
    private ViewPropertyAnimator mTextAnimator;
    private float mTextY;
    private View splash_name;
    private View splash_root;
    private View splash_text;

    @Override
    protected void acquireArguments(Intent intent) {

    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    public void enquiryViews() {
        splash_root = findViewById(R.id.splash_root);
        splash_name = findViewById(R.id.splash_name);
        splash_text = findViewById(R.id.splash_text);

        splash_root.post(new Runnable() {
            @Override
            public void run() {
                mRootBottom = splash_root.getBottom();
            }
        });
        splash_text.post(new Runnable() {
            @Override
            public void run() {
                mTextY = ViewHelper.getY(splash_text);
            }
        });

        mNameAnimator = ViewPropertyAnimator.animate(splash_name);
        mTextAnimator = ViewPropertyAnimator.animate(splash_text);
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void setLayout() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    public void startAction() {
        Handler handler = new Handler();

        ViewHelper.setAlpha(splash_name, 0f);
        ViewHelper.setAlpha(splash_text, 0f);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNameAnimator.alpha(1f).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(300).start();
            }
        }, 500);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ViewHelper.setY(splash_text, mRootBottom);
                mTextAnimator.alpha(1f).y(mTextY).setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(600)
                        .start();
            }
        }, 1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                start(MainActivity.create(getContext()), R.anim.zoom_in_enter, R.anim.zoom_out_exit);
                finish();
            }
        }, 3500);
    }
}
