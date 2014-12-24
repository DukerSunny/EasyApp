package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;
import tv.acfun.read.helpers.ChannelHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class ChannelUnspecificRemovableHolder extends RecyclerHolder<Content> {
    private View channel_unspecific_removable_icon;
    private TextView channel_unspecific_removable_name;
    private View channel_unspecific_removable_remove;
    private SwipeLayout channel_unspecific_removable_swipe;
    private TextView channel_unspecific_removable_title;

    public ChannelUnspecificRemovableHolder(View itemView) {
        super(itemView);

        channel_unspecific_removable_swipe = (SwipeLayout) itemView.findViewById(R.id.channel_unspecific_removable_swipe);
        channel_unspecific_removable_remove = itemView.findViewById(R.id.channel_unspecific_removable_remove);
        channel_unspecific_removable_icon = itemView.findViewById(R.id.channel_unspecific_removable_icon);
        channel_unspecific_removable_name = (TextView) itemView.findViewById(R.id.channel_unspecific_removable_name);
        channel_unspecific_removable_title = (TextView) itemView.findViewById(R.id.channel_unspecific_removable_title);

        RippleDrawable.attach(itemView);
    }

    public void addSwipeListener(SwipeLayout.SwipeListener swipeListener) {
        channel_unspecific_removable_swipe.addSwipeListener(swipeListener);
    }

    @Override
    public void setItem(Content content) {
        channel_unspecific_removable_remove.setTag(content.hashCode());
        channel_unspecific_removable_icon.setBackgroundResource(ChannelHelper.getDrawableIdByChannelId(content.getChannelId()));
        channel_unspecific_removable_name.setText(ChannelHelper.getTitleIdByChannelId(content.getChannelId()));
        channel_unspecific_removable_title.setText(content.getTitle());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }

    public void setOnRemoveClickListener(View.OnClickListener onRemoveClickListener) {
        channel_unspecific_removable_remove.setOnClickListener(onRemoveClickListener);
    }
}