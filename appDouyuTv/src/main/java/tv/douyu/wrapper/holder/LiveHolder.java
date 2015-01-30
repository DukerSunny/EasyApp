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
import tv.douyu.model.bean.Room;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class LiveHolder extends RecyclerHolder<Room> {
    private TextView live_name;
    private View live_ripple;
    private ImageView live_src;

    public LiveHolder(View itemView) {
        super(itemView);

        live_src = (ImageView) itemView.findViewById(R.id.live_src);
        live_name = (TextView) itemView.findViewById(R.id.live_name);
        live_ripple = itemView.findViewById(R.id.live_ripple);

        RippleDrawable.attach(live_ripple, RippleStyle.Light);
    }

    @Override
    public void setItem(Room live) {
        ImageLoaderHelper.loadImage(live_src, live.getRoom_src(), R.drawable.loading_16x9, R.drawable.retry_16x9);
        live_ripple.setTag(live.hashCode());
        live_name.setText(live.getRoom_name());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(live_ripple, onClickListener);
    }
}