package tv.acfun.read.holders;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class SelectionHolderBackup implements IAbsListHolder<Content> {
    private TextView selection_channel;
    private TextView selection_descripition;
    private ImageView selection_image;
    private View selection_mask;
    private View selection_mask_white;
    private TextView selection_number;
    private TextView selection_title;

    public SelectionHolderBackup(View convertView) {
        selection_image = (ImageView) convertView.findViewById(R.id.selection_image);
        selection_mask = convertView.findViewById(R.id.selection_mask);
        selection_mask_white = convertView.findViewById(R.id.selection_mask_white);
        selection_number = (TextView) convertView.findViewById(R.id.recommend_number);
        selection_channel = (TextView) convertView.findViewById(R.id.recommend_channel);
        selection_title = (TextView) convertView.findViewById(R.id.recommend_title);
        selection_descripition = (TextView) convertView.findViewById(R.id.recommend_description);
    }

    @Override
    public void setItem(int position, Content content) {
        int color;

        if (position == 0) {
            selection_image.setVisibility(View.VISIBLE);
            selection_mask.setVisibility(View.VISIBLE);
            selection_mask_white.setVisibility(View.GONE);
        } else {
            selection_image.setVisibility(View.GONE);
            selection_mask.setVisibility(View.GONE);
            selection_mask_white.setVisibility(View.VISIBLE);
        }
        switch (position) {
            case 0:
                selection_number.setTextColor(Color.WHITE);
                selection_number.setBackgroundResource(R.drawable.shape_circle_1st);
                selection_channel.setTextColor(selection_channel.getResources().getColor(R.color.Selection_1st));
                break;
            case 1:
                selection_number.setTextColor(Color.WHITE);
                selection_number.setBackgroundResource(R.drawable.shape_circle_2nd);
                selection_channel.setTextColor(selection_channel.getResources().getColor(R.color.Selection_2nd));
                break;
            case 2:
                selection_number.setTextColor(Color.WHITE);
                selection_number.setBackgroundResource(R.drawable.shape_circle_3rd);
                selection_channel.setTextColor(selection_channel.getResources().getColor(R.color.Selection_3rd));
                break;
            case 3:
                selection_number.setTextColor(Color.WHITE);
                selection_number.setBackgroundResource(R.drawable.shape_circle_4th);
                selection_channel.setTextColor(selection_channel.getResources().getColor(R.color.Selection_4th));
                break;
            case 4:
                selection_number.setTextColor(Color.WHITE);
                selection_number.setBackgroundResource(R.drawable.shape_circle_5th);
                selection_channel.setTextColor(selection_channel.getResources().getColor(R.color.Selection_5th));
                break;
            default:
                color = selection_number.getResources().getColor(R.color.Selection_Lesser);
                selection_number.setTextColor(color);
                selection_number.setBackgroundResource(R.drawable.shape_circle_lesser);
                selection_channel.setTextColor(color);
        }
        switch (content.getChannelId()) {
            case 110:
                selection_channel.setText(R.string.channel_misc);
                break;
            case 73:
                selection_channel.setText(R.string.channel_work_emotion);
                break;
            case 74:
                selection_channel.setText(R.string.channel_dramaculture);
                break;
            case 75:
                selection_channel.setText(R.string.channel_comic_novel);
        }
        selection_number.setText(String.valueOf(position + 1));
        selection_title.setText(content.getTitle());
        selection_descripition.setText(content.getSpanned());
    }
}
