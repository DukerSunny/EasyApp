package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class HistoryHolder implements IAbsListHolder<Content> {
    private TextView history_channel;
    private View history_remove;
    private TextView history_title;

    public HistoryHolder(View convertView, View.OnClickListener clickListener) {
        history_title = (TextView) convertView.findViewById(R.id.history_title);
        history_channel = (TextView) convertView.findViewById(R.id.history_channel);
        history_remove = convertView.findViewById(R.id.history_remove);
        history_remove.setOnClickListener(clickListener);
    }

    @Override
    public void setItem(int position, Content content) {
        switch (content.getChannelId()) {
            case 110:
                history_channel.setText(R.string.channel_misc);
                history_channel.setBackgroundResource(R.color.Search_Misc);
                break;
            case 73:
                history_channel.setText(R.string.channel_work_emotion);
                history_channel.setBackgroundResource(R.color.Search_Work_Emotion);
                break;
            case 74:
                history_channel.setText(R.string.channel_dramaculture);
                history_channel.setBackgroundResource(R.color.Search_DramaCulture);
                break;
            case 75:
                history_channel.setText(R.string.channel_comic_novel);
                history_channel.setBackgroundResource(R.color.Search_Comic_Novel);
        }
        history_title.setText(content.getTitle());
        history_remove.setTag(position);
    }
}