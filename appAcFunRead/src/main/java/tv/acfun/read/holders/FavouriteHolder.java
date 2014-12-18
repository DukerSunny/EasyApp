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
public class FavouriteHolder extends RecyclerHolder<Content> {
    private TextView favourite_channel;
    private ImageView favourite_icon;
    private View favourite_remove;
    private SwipeLayout favourite_swipe;
    private TextView favourite_title;

    public FavouriteHolder(View convertView) {
        super(convertView);
        favourite_swipe = (SwipeLayout) convertView.findViewById(R.id.favourite_swipe);
        favourite_icon = (ImageView) convertView.findViewById(R.id.favourite_icon);
        favourite_title = (TextView) convertView.findViewById(R.id.favourite_title);
        favourite_channel = (TextView) convertView.findViewById(R.id.favourite_channel);
        favourite_remove = convertView.findViewById(R.id.favourite_remove);


        favourite_swipe.setShowMode(SwipeLayout.ShowMode.PullOut);
        favourite_swipe.setDragEdge(SwipeLayout.DragEdge.Right);
    }

    public void addSwipeListener(SwipeLayout.SwipeListener swipleListener) {
        favourite_swipe.addSwipeListener(swipleListener);
    }

    @Override
    public void setItem(Content content) {
        switch (content.getChannelId()) {
            case 110:
                favourite_icon.setImageResource(R.drawable.shape_circle_misc);
                favourite_channel.setText(R.string.channel_misc);
                break;
            case 73:
                favourite_icon.setImageResource(R.drawable.shape_circle_work_emotion);
                favourite_channel.setText(R.string.channel_work_emotion);
                break;
            case 74:
                favourite_icon.setImageResource(R.drawable.shape_circle_dramaculture);
                favourite_channel.setText(R.string.channel_dramaculture);
                break;
            case 75:
                favourite_icon.setImageResource(R.drawable.shape_circle_comic_novel);
                favourite_channel.setText(R.string.channel_comic_novel);
        }
        favourite_title.setText(content.getTitle());
        favourite_remove.setTag(content.hashCode());
    }

    public void setOnRemoveClickListener(View.OnClickListener onRemoveClickListener) {
        favourite_remove.setOnClickListener(onRemoveClickListener);
    }
}