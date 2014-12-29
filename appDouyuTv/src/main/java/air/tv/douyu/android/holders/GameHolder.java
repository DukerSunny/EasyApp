package air.tv.douyu.android.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import air.tv.douyu.android.R;
import air.tv.douyu.android.beans.Game;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class GameHolder extends RecyclerHolder<Game> {
    private TextView game_name;
    private ImageView game_src;

    public GameHolder(View itemView) {
        super(itemView);

        game_src = (ImageView) itemView.findViewById(R.id.game_src);
        game_name = (TextView) itemView.findViewById(R.id.game_name);

        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(Game game) {
        ImageLoaderHelper.loadImage(game_src, game.getGame_src(), R.drawable.loading_2x3, R.drawable.retry_2x3);
        game_name.setText(game.getGame_name());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }
}