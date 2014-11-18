package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class FavouriteHolder implements IAbsListHolder<Content> {
    private TextView favourite_channel;
    private View favourite_remove;
    private TextView favourite_title;

    public FavouriteHolder(View convertView, View.OnClickListener clickListener) {
        favourite_title = (TextView) convertView.findViewById(R.id.favourite_title);
        favourite_channel = (TextView) convertView.findViewById(R.id.favourite_channel);
        favourite_remove = convertView.findViewById(R.id.favourite_remove);
        favourite_remove.setOnClickListener(clickListener);
    }

    @Override
    public void setItem(int position, Content content) {
        switch (content.getChannelId()) {
            case 110:
                favourite_channel.setText(R.string.channel_misc);
                favourite_channel.setBackgroundResource(R.color.Search_Misc);
                break;
            case 73:
                favourite_channel.setText(R.string.channel_work_emotion);
                favourite_channel.setBackgroundResource(R.color.Search_Work_Emotion);
                break;
            case 74:
                favourite_channel.setText(R.string.channel_dramaculture);
                favourite_channel.setBackgroundResource(R.color.Search_DramaCulture);
                break;
            case 75:
                favourite_channel.setText(R.string.channel_comic_novel);
                favourite_channel.setBackgroundResource(R.color.Search_Comic_Novel);
        }
        favourite_title.setText(content.getTitle());
        favourite_remove.setTag(position);
    }
}