package tv.acfun.read.holders;

import android.content.res.Resources;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class CommentFloorHolder implements IAbsListHolder<Conversion> {
    private TextView comment_date;
    private String comment_date_text;
    private View comment_options;
    private TextView comment_text;
    private ImageView comment_userImg;
    private TextView comment_username;

    public CommentFloorHolder(View convertView) {
        Resources resources = convertView.getResources();

        comment_userImg = (ImageView) convertView.findViewById(R.id.comment_userImg);
        comment_username = (TextView) convertView.findViewById(R.id.comment_username);
        comment_date = (TextView) convertView.findViewById(R.id.comment_date);
        comment_options = convertView.findViewById(R.id.comment_options);
        comment_text = (TextView) convertView.findViewById(R.id.comment_text);

        comment_date_text = resources.getString(R.string.comment_date);

        comment_text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void setItem(int position, Conversion conversion) {
        comment_options.setTag(position);
        ImageLoaderHelper.loadImage(comment_userImg, conversion.getUserImg());
        comment_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        comment_date.setText(String.format(comment_date_text, conversion.getPostDate()));
        comment_text.setText(conversion.getSpanned());
    }

    public void setOnOptionsClickListener(View.OnClickListener clickListener) {
        comment_options.setOnClickListener(clickListener);
    }
}