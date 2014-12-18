package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class ContributionHolder implements IAbsListHolder<Content> {
    private TextView contribution_channel;
    private TextView contribution_title;

    public ContributionHolder(View convertView) {
        contribution_title = (TextView) convertView.findViewById(R.id.contribution_title);
        contribution_channel = (TextView) convertView.findViewById(R.id.contribution_channel);
    }

    @Override
    public void setItem(int position, Content content) {
        switch (content.getChannelId()) {
            case 110:
                contribution_channel.setText(R.string.channel_misc);
                contribution_channel.setBackgroundResource(R.color.Channel_Misc);
                break;
            case 73:
                contribution_channel.setText(R.string.channel_work_emotion);
                contribution_channel.setBackgroundResource(R.color.Channel_Work_Emotion);
                break;
            case 74:
                contribution_channel.setText(R.string.channel_dramaculture);
                contribution_channel.setBackgroundResource(R.color.Channel_DramaCulture);
                break;
            case 75:
                contribution_channel.setText(R.string.channel_comic_novel);
                contribution_channel.setBackgroundResource(R.color.Channel_Comic_Novel);
        }
        contribution_title.setText(content.getTitle());
    }
}