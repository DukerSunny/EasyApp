package tv.acfun.read.holders;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class RecommendHolder implements IAbsListHolder<Content> {
    private TextView recommend_channel;
    private TextView recommend_description;
    private TextView recommend_number;
    private TextView recommend_title;

    public RecommendHolder(View convertView) {
        recommend_number = (TextView) convertView.findViewById(R.id.recommend_number);
        recommend_channel = (TextView) convertView.findViewById(R.id.recommend_channel);
        recommend_title = (TextView) convertView.findViewById(R.id.recommend_title);
        recommend_description = (TextView) convertView.findViewById(R.id.recommend_description);
    }

    @Override
    public void setItem(int position, Content content) {
        Resources resources = recommend_number.getResources();
        int color;

        switch (position) {
            case 0:
                recommend_number.setTextColor(Color.WHITE);
                recommend_number.setBackgroundResource(R.drawable.shape_circle_1st);
                recommend_channel.setTextColor(resources.getColor(R.color.Selection_1st));
                break;
            case 1:
                recommend_number.setTextColor(Color.WHITE);
                recommend_number.setBackgroundResource(R.drawable.shape_circle_2nd);
                recommend_channel.setTextColor(resources.getColor(R.color.Selection_2nd));
                break;
            case 2:
                recommend_number.setTextColor(Color.WHITE);
                recommend_number.setBackgroundResource(R.drawable.shape_circle_3rd);
                recommend_channel.setTextColor(resources.getColor(R.color.Selection_3rd));
                break;
            case 3:
                recommend_number.setTextColor(Color.WHITE);
                recommend_number.setBackgroundResource(R.drawable.shape_circle_4th);
                recommend_channel.setTextColor(resources.getColor(R.color.Selection_4th));
                break;
            case 4:
                recommend_number.setTextColor(Color.WHITE);
                recommend_number.setBackgroundResource(R.drawable.shape_circle_5th);
                recommend_channel.setTextColor(resources.getColor(R.color.Selection_5th));
                break;
            default:
                color = resources.getColor(R.color.Selection_Lesser);
                recommend_number.setTextColor(color);
                recommend_number.setBackgroundResource(R.drawable.shape_circle_lesser);
                recommend_channel.setTextColor(color);
        }
        switch (content.getChannelId()) {
            case 110:
                recommend_channel.setText(R.string.channel_misc);
                break;
            case 73:
                recommend_channel.setText(R.string.channel_work_emotion);
                break;
            case 74:
                recommend_channel.setText(R.string.channel_dramaculture);
                break;
            case 75:
                recommend_channel.setText(R.string.channel_comic_novel);
        }
        recommend_number.setText(String.valueOf(position + 1));
        recommend_title.setText(content.getTitle());
        recommend_description.setText(content.getSpanned());
    }
}
