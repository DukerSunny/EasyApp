package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class HistoryHolder extends RecyclerHolder<Content> {
    private TextView history_channel;
    private ImageView history_icon;
    private View history_remove;
    private SwipeLayout history_swipe;
    private TextView history_title;

    public HistoryHolder(View convertView) {
        super(convertView);
        history_swipe = (SwipeLayout) convertView.findViewById(R.id.history_swipe);
        history_icon = (ImageView) convertView.findViewById(R.id.history_icon);
        history_title = (TextView) convertView.findViewById(R.id.history_title);
        history_channel = (TextView) convertView.findViewById(R.id.history_channel);
        history_remove = convertView.findViewById(R.id.history_remove);

        history_swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        history_swipe.setDragEdge(SwipeLayout.DragEdge.Right);
    }

    public void addSwipeListener(SwipeLayout.SwipeListener swipleListener) {
        history_swipe.addSwipeListener(swipleListener);
    }

    @Override
    public void setItem(Content content) {
        switch (content.getChannelId()) {
            case 110:
                history_icon.setImageResource(R.drawable.shape_circle_misc);
                history_channel.setText(R.string.channel_misc);
                break;
            case 73:
                history_icon.setImageResource(R.drawable.shape_circle_work_emotion);
                history_channel.setText(R.string.channel_work_emotion);
                break;
            case 74:
                history_icon.setImageResource(R.drawable.shape_circle_dramaculture);
                history_channel.setText(R.string.channel_dramaculture);
                break;
            case 75:
                history_icon.setImageResource(R.drawable.shape_circle_comic_novel);
                history_channel.setText(R.string.channel_comic_novel);
        }
        history_title.setText(content.getTitle());
        history_remove.setTag(content.hashCode());
    }

    public void setOnRemoveClickListener(View.OnClickListener onRemoveClickListener) {
        history_remove.setOnClickListener(onRemoveClickListener);
    }
}