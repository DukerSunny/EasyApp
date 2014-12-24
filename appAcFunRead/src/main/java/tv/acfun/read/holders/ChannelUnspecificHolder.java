package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.ChannelHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class ChannelUnspecificHolder extends RecyclerHolder<Content> {
    private View channel_unspecific_icon;
    private TextView channel_unspecific_name;
    private TextView channel_unspecific_title;

    public ChannelUnspecificHolder(View itemView) {
        super(itemView);

        channel_unspecific_icon = itemView.findViewById(R.id.channel_unspecific_icon);
        channel_unspecific_name = (TextView) itemView.findViewById(R.id.channel_unspecific_name);
        channel_unspecific_title = (TextView) itemView.findViewById(R.id.channel_unspecific_title);

        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(Content content) {
        channel_unspecific_icon.setBackgroundResource(ChannelHelper.getDrawableIdByChannelId(content.getChannelId()));
        channel_unspecific_name.setText(ChannelHelper.getTitleIdByChannelId(content.getChannelId()));
        channel_unspecific_title.setText(content.getTitle());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }
}