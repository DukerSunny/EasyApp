package air.tv.douyu.android.bases.fragments;

import android.os.Bundle;

import com.harreke.easyapp.frameworks.bases.fragment.FragmentFramework;

import air.tv.douyu.android.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class GameFragment extends FragmentFramework {
    public static GameFragment create() {
        return new GameFragment();
    }

    @Override
    protected void acquireArguments(Bundle bundle) {

    }

    @Override
    public void attachCallbacks() {

    }

    @Override
    public void enquiryViews() {

    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public void setLayout() {
        setContentView(R.layout.fragment_game);
    }

    @Override
    public void startAction() {

    }
}
