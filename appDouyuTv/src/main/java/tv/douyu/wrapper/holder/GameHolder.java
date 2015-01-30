package tv.douyu.wrapper.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.douyu.R;
import tv.douyu.model.bean.Game;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class GameHolder extends RecyclerHolder<Game> {
    private TextView game_name;
    private View game_ripple;
    private ImageView game_src;

    public GameHolder(View itemView) {
        super(itemView);

        game_src = (ImageView) itemView.findViewById(R.id.game_src);
        game_name = (TextView) itemView.findViewById(R.id.game_name);
        game_ripple = itemView.findViewById(R.id.game_ripple);

        RippleDrawable.attach(game_ripple, RippleStyle.Light);
    }

    @Override
    public void setItem(Game game) {
        ImageLoaderHelper.loadImage(game_src, game.getGame_src(), R.drawable.loading_7x10, R.drawable.retry_7x10);
        game_ripple.setTag(game.hashCode());
        game_name.setText(game.getGame_name());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(game_ripple, onClickListener);
    }
}