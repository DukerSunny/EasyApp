package tv.douyu.wrapper.holder;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.enums.RippleStyle;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;

import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/25
 */
public class HotWordHolder extends RecyclerHolder<String> {
    private TextView hotword_text;

    public HotWordHolder(View itemView) {
        super(itemView);

        hotword_text = (TextView) itemView.findViewById(R.id.hotword_text);

        RippleDrawable.attach(itemView, RippleStyle.Light);
    }

    @Override
    public void setItem(String hotword) {
        hotword_text.setText(hotword);
    }
}
