package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.RippleDrawable;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class ChannelHolder extends RecyclerHolder<Content> {
    private TextView channel_comments;
    private TextView channel_description;
    private TextView channel_title;

    public ChannelHolder(View convertView) {
        super(convertView);
        RippleDrawable.attach(convertView);
        channel_title = (TextView) convertView.findViewById(R.id.channel_title);
        channel_comments = (TextView) convertView.findViewById(R.id.channel_comments);
        channel_description = (TextView) convertView.findViewById(R.id.channel_description);
    }

    @Override
    public void setItem(Content content) {
        channel_title.setText(content.getTitle());
        channel_comments.setText(String.valueOf(content.getComments()));
        channel_description.setText(content.getSpanned());
    }
}