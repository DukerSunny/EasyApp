package tv.douyu.control.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.harreke.easyapp.frameworks.base.IFramework;
import com.harreke.easyapp.frameworks.base.FragmentFramework;
import com.harreke.easyapp.frameworks.recyclerview.GridItemDecoration;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerFramework;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import tv.douyu.R;
import tv.douyu.misc.api.API;
import tv.douyu.control.activity.LiveActivity;
import tv.douyu.control.application.DouyuTv;
import tv.douyu.model.bean.Game;
import tv.douyu.wrapper.holder.GameHolder;
import tv.douyu.model.parser.GameListParser;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class GameFragment extends FragmentFramework {
    private GameRecyclerHelper mGameRecyclerHelper;

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
        mGameRecyclerHelper = new GameRecyclerHelper(this);
        mGameRecyclerHelper.setHasFixedSize(true);
        mGameRecyclerHelper.setCanLoad(false);
        mGameRecyclerHelper.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mGameRecyclerHelper.setItemDecoration(new GridItemDecoration(3));
        mGameRecyclerHelper.setListParser(new GameListParser(DouyuTv.getInstance().getSetting().isShowEmptyCategory()));
        mGameRecyclerHelper.attachAdapter();
    }

    @Override
    public void establishCallbacks() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_game;
    }

    @Override
    public void startAction() {
        mGameRecyclerHelper.from(API.getGame());
    }

    private class GameRecyclerHelper extends RecyclerFramework<Game> {
        public GameRecyclerHelper(IFramework framework) {
            super(framework);
        }

        @Override
        protected RecyclerHolder<Game> createHolder(View itemView, int viewType) {
            return new GameHolder(itemView);
        }

        @Override
        protected View createView(LayoutInflater inflater, ViewGroup parent, int viewType) {
            return inflater.inflate(R.layout.item_game, parent, false);
        }

        @Override
        public void onItemClick(int position, Game game) {
            start(LiveActivity.create(getContext(), game.getGame_name(), game.getCate_id()));
        }
    }
}