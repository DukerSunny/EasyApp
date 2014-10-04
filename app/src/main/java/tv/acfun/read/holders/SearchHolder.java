package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Search;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/29
 */
public class SearchHolder implements IAbsListHolder<Search> {
    private TextView search_channel;
    private TextView search_description;
    private TextView search_title;

    public SearchHolder(View convertView) {
        search_channel = (TextView) convertView.findViewById(R.id.search_channel);
        search_title = (TextView) convertView.findViewById(R.id.search_title);
        search_description = (TextView) convertView.findViewById(R.id.search_description);
    }

    @Override
    public void setItem(int position, Search search) {
        switch (search.getChannelId()) {
            case 110:
                search_channel.setText(R.string.channel_misc);
                search_channel.setBackgroundResource(R.color.Search_Misc);
                break;
            case 73:
                search_channel.setText(R.string.channel_work_emotion);
                search_channel.setBackgroundResource(R.color.Search_Work_Emotion);
                break;
            case 74:
                search_channel.setText(R.string.channel_dramaculture);
                search_channel.setBackgroundResource(R.color.Search_DramaCulture);
                break;
            case 75:
                search_channel.setText(R.string.channel_comic_novel);
                search_channel.setBackgroundResource(R.color.Search_Comic_Novel);
        }
        search_title.setText("               " + search.getTitle());
        search_description.setText(search.getSpanned());
    }
}