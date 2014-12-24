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
public class ChannelSpecificHolder extends RecyclerHolder<Content> {
    private TextView channel_specific_comments;
    private TextView channel_specific_description;
    private View channel_specific_icon;
    private TextView channel_specific_title;

    public ChannelSpecificHolder(View itemView) {
        super(itemView);

        channel_specific_icon = itemView.findViewById(R.id.channel_specific_icon);
        channel_specific_title = (TextView) itemView.findViewById(R.id.channel_specific_title);
        channel_specific_comments = (TextView) itemView.findViewById(R.id.channel_specific_comments);
        channel_specific_description = (TextView) itemView.findViewById(R.id.channel_specific_description);

        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(Content content) {
        channel_specific_icon.setBackgroundResource(ChannelHelper.getDrawableIdByChannelId(content.getChannelId()));
        channel_specific_title.setText(content.getTitle());
        channel_specific_comments.setText(String.valueOf(content.getComments()));
        channel_specific_description.setText(content.getSpanned());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }
}