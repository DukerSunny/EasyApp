package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.RippleDrawable;

import tv.acfun.read.R;
import tv.acfun.read.beans.Search;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/29
 */
public class SearchHolder extends RecyclerHolder<Search> {
    private TextView search_channel;
    private TextView search_description;
    private ImageView search_icon;
    private TextView search_title;

    public SearchHolder(View convertView) {
        super(convertView);
        search_icon = (ImageView) convertView.findViewById(R.id.search_icon);
        search_channel = (TextView) convertView.findViewById(R.id.search_channel);
        search_title = (TextView) convertView.findViewById(R.id.search_title);
        search_description = (TextView) convertView.findViewById(R.id.search_description);

        RippleDrawable.attach(convertView);
    }

    @Override
    public void setItem(Search search) {
        switch (search.getChannelId()) {
            case 110:
                search_icon.setImageResource(R.drawable.shape_circle_misc);
                search_channel.setText(R.string.channel_misc);
                break;
            case 73:
                search_icon.setImageResource(R.drawable.shape_circle_work_emotion);
                search_channel.setText(R.string.channel_work_emotion);
                break;
            case 74:
                search_icon.setImageResource(R.drawable.shape_circle_dramaculture);
                search_channel.setText(R.string.channel_dramaculture);
                break;
            case 75:
                search_icon.setImageResource(R.drawable.shape_circle_comic_novel);
                search_channel.setText(R.string.channel_comic_novel);
        }
        search_title.setText(search.getTitle());
        search_description.setText(search.getSpanned());
    }
}