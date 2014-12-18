package tv.acfun.read.holders;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.recycerview.RecyclerHolder;
import com.harreke.easyapp.widgets.RippleDrawable;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.helpers.ImageConnectionHelper;
import tv.acfun.read.helpers.OfflineImageLoaderHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class CommentFloorHolder extends RecyclerHolder<Conversion> {
    private TextView comment_date;
    private String comment_date_text;
    private TextView comment_text;
    private ImageView comment_userImg;
    private TextView comment_username;

    public CommentFloorHolder(View convertView) {
        super(convertView);
        Resources resources = convertView.getResources();

        comment_userImg = (ImageView) convertView.findViewById(R.id.comment_userImg);
        comment_username = (TextView) convertView.findViewById(R.id.comment_username);
        comment_date = (TextView) convertView.findViewById(R.id.comment_date);
        comment_text = (TextView) convertView.findViewById(R.id.comment_text);

        comment_date_text = resources.getString(R.string.comment_date);

        RippleDrawable.attach(convertView.findViewById(R.id.comment_floor));
    }

    @Override
    public void setItem(Conversion conversion) {
        if (ImageConnectionHelper.shouldLoadImage()) {
            ImageLoaderHelper.loadImage(comment_userImg, conversion.getUserImg());
        } else {
            OfflineImageLoaderHelper.loadImage(comment_userImg, OfflineImageLoaderHelper.OfflineImage.Avatar);
        }
        comment_userImg.setTag(conversion.getUserID());
        comment_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        comment_date.setText(String.format(comment_date_text, conversion.getPostDate()));
        comment_text.setText(conversion.getSpanned());
    }

    public final void setOnAvatarClickListener(View.OnClickListener onAvatarClickListener) {
        comment_userImg.setOnClickListener(onAvatarClickListener);
    }

    public final void setTextSize(int textSize) {
        comment_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }
}