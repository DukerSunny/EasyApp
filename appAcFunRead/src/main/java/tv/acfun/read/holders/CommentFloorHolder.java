package tv.acfun.read.holders;

import android.content.res.Resources;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;
import tv.acfun.read.helpers.ConnectionHelper;
import tv.acfun.read.helpers.OfflineImageLoaderHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class CommentFloorHolder implements IAbsListHolder<Conversion> {
    private TextView comment_date;
    private String comment_date_text;
    private View comment_floor_close;
    private View comment_floor_open;
    private SwipeLayout comment_floor_swipe;
    private View comment_floor_swipe_copy;
    private View comment_floor_swipe_reply;
    private View comment_floor_swipe_user;
    private TextView comment_text;
    private ImageView comment_userImg;
    private TextView comment_username;

    public CommentFloorHolder(View convertView) {
        Resources resources = convertView.getResources();

        comment_floor_swipe = (SwipeLayout) convertView.findViewById(R.id.comment_floor_swipe);
        comment_floor_open = convertView.findViewById(R.id.comment_floor_open);
        comment_floor_close = convertView.findViewById(R.id.comment_floor_close);
        comment_floor_swipe_user = convertView.findViewById(R.id.comment_floor_swipe_user);
        comment_floor_swipe_copy = convertView.findViewById(R.id.comment_floor_swipe_copy);
        comment_floor_swipe_reply = convertView.findViewById(R.id.comment_floor_swipe_reply);
        comment_userImg = (ImageView) convertView.findViewById(R.id.comment_userImg);
        comment_username = (TextView) convertView.findViewById(R.id.comment_username);
        comment_date = (TextView) convertView.findViewById(R.id.comment_date);
        comment_text = (TextView) convertView.findViewById(R.id.comment_text);

        comment_date_text = resources.getString(R.string.comment_date);

        comment_floor_swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        comment_floor_swipe.setDragEdge(SwipeLayout.DragEdge.Bottom);
        comment_floor_swipe.setSwipeEnabled(false);

        comment_floor_open.setTag(-1);
        comment_floor_close.setTag(-1);

        comment_text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void closeSwipe() {
        comment_floor_swipe.close();
        comment_floor_open.setVisibility(View.VISIBLE);
        comment_floor_close.setVisibility(View.GONE);
    }

    public void openSwipe() {
        comment_floor_swipe.open(true);
        comment_floor_open.setVisibility(View.GONE);
        comment_floor_close.setVisibility(View.VISIBLE);
    }

    @Override
    public void setItem(int position, Conversion conversion) {
        comment_floor_swipe_user.setTag(conversion.getUserID());
        comment_floor_swipe_copy.setTag(R.id.comment_floor_position, position);
        comment_floor_swipe_copy.setTag(R.id.comment_quote_position, -1);
        comment_floor_swipe_reply.setTag(R.id.comment_floor_position, position);
        comment_floor_swipe_reply.setTag(R.id.comment_quote_position, -1);
        if (ConnectionHelper.shouldLoadImage()) {
            ImageLoaderHelper.loadImage(comment_userImg, conversion.getUserImg());
        } else {
            OfflineImageLoaderHelper.loadImage(comment_userImg, OfflineImageLoaderHelper.OfflineImage.Avatar);
        }
        comment_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        comment_date.setText(String.format(comment_date_text, conversion.getPostDate()));
        comment_text.setText(conversion.getSpanned());
    }

    public final void setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        comment_floor_close.setOnClickListener(onCloseClickListener);
    }

    public final void setOnCopyClickListener(View.OnClickListener onCopyClickListener) {
        comment_floor_swipe_copy.setOnClickListener(onCopyClickListener);
    }

    public final void setOnOpenClickListener(View.OnClickListener onOpenClickListener) {
        comment_floor_open.setOnClickListener(onOpenClickListener);
    }

    public final void setOnReplyClickListener(View.OnClickListener onReplyClickListener) {
        comment_floor_swipe_reply.setOnClickListener(onReplyClickListener);
    }

    public final void setOnUserClickListener(View.OnClickListener onUserClickListener) {
        comment_floor_swipe_user.setOnClickListener(onUserClickListener);
    }

    public final void setTextSize(int textSize) {
        comment_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }
}