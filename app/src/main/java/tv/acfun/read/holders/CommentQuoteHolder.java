package tv.acfun.read.holders;

import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Conversion;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/05
 */
public class CommentQuoteHolder implements IAbsListHolder<Conversion> {
    private View comment_quote_close;
    private View comment_quote_open;
    private TextView comment_quote_reference;
    private SwipeLayout comment_quote_swipe;
    private View comment_quote_swipe_copy;
    private View comment_quote_swipe_reply;
    private View comment_quote_swipe_user;
    private TextView comment_quote_text;
    private TextView comment_quote_username;
    private View mConvertView;

    public CommentQuoteHolder(View convertView) {
        mConvertView = convertView;
        comment_quote_swipe = (SwipeLayout) convertView.findViewById(R.id.comment_quote_swipe);
        comment_quote_open = convertView.findViewById(R.id.comment_quote_open);
        comment_quote_close = convertView.findViewById(R.id.comment_quote_close);
        comment_quote_swipe_user = convertView.findViewById(R.id.comment_quote_swipe_user);
        comment_quote_swipe_copy = convertView.findViewById(R.id.comment_quote_swipe_copy);
        comment_quote_swipe_reply = convertView.findViewById(R.id.comment_quote_swipe_reply);
        comment_quote_text = (TextView) convertView.findViewById(R.id.comment_quote_text);
        comment_quote_username = (TextView) convertView.findViewById(R.id.comment_quote_username);
        comment_quote_reference = (TextView) convertView.findViewById(R.id.comment_quote_reference);

        comment_quote_swipe.setShowMode(SwipeLayout.ShowMode.LayDown);
        comment_quote_swipe.setDragEdge(SwipeLayout.DragEdge.Bottom);
        comment_quote_swipe.setSwipeEnabled(false);

        comment_quote_text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void closeSwipe() {
        comment_quote_swipe.close();
        comment_quote_open.setVisibility(View.VISIBLE);
        comment_quote_close.setVisibility(View.GONE);
    }

    public void hide() {
        mConvertView.setVisibility(View.GONE);
    }

    public void openSwipe() {
        comment_quote_swipe.open(true);
        comment_quote_open.setVisibility(View.GONE);
        comment_quote_close.setVisibility(View.VISIBLE);
    }

    public void setFloorPosition(int position) {
        comment_quote_swipe_copy.setTag(R.id.comment_floor_position, position);
        comment_quote_swipe_reply.setTag(R.id.comment_floor_position, position);
    }

    @Override
    public void setItem(int position, Conversion conversion) {
        comment_quote_open.setTag(position);
        comment_quote_close.setTag(position);
        comment_quote_swipe_user.setTag(conversion.getUserID());
        comment_quote_swipe_copy.setTag(R.id.comment_quote_position, position);
        comment_quote_swipe_reply.setTag(R.id.comment_quote_position, position);
        comment_quote_text.setText(conversion.getSpanned());
        comment_quote_username.setText("#" + conversion.getCount() + " " + conversion.getUserName());
        comment_quote_reference.setText(String.valueOf(conversion.getDeep() + 1));
    }

    public final void setOnCloseClickListener(View.OnClickListener onCloseClickListener) {
        comment_quote_close.setOnClickListener(onCloseClickListener);
    }

    public final void setOnCopyClickListener(View.OnClickListener onCopyClickListener) {
        comment_quote_swipe_copy.setOnClickListener(onCopyClickListener);
    }

    public final void setOnOpenClickListener(View.OnClickListener onOpenClickListener) {
        comment_quote_open.setOnClickListener(onOpenClickListener);
    }

    public final void setOnReplyClickListener(View.OnClickListener onReplyClickListener) {
        comment_quote_swipe_reply.setOnClickListener(onReplyClickListener);
    }

    public final void setOnUserClickListener(View.OnClickListener onUserClickListener) {
        comment_quote_swipe_user.setOnClickListener(onUserClickListener);
    }

    public void show() {
        mConvertView.setVisibility(View.VISIBLE);
    }
}