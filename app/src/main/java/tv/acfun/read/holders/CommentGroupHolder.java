package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IExListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/26
 */
public class CommentGroupHolder implements IExListHolder.Group<Conversion> {
    private TextView comment_date;
    private TextView comment_text;
    private ImageView comment_user_avatar;
    private TextView comment_user_name;

    public CommentGroupHolder(View convertView) {
        comment_user_avatar = (ImageView) convertView.findViewById(R.id.comment_userImg);
        comment_user_name = (TextView) convertView.findViewById(R.id.comment_username);
        comment_date = (TextView) convertView.findViewById(R.id.comment_date);
        comment_text = (TextView) convertView.findViewById(R.id.comment_text);
    }

    @Override
    public void setItem(int groupPosition, Conversion conversion) {
        ImageLoaderHelper.loadImage(comment_user_avatar, conversion.getUser().getUserImg());
        comment_user_name.setText("# " + conversion.getFloorindex() + " " + conversion.getUser().getUsername());
        comment_date.setText(conversion.getDate());
        comment_text.setText(conversion.getSpanned());
    }
}