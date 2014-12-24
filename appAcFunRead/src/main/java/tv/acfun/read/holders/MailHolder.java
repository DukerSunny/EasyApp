package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.acfun.read.R;
import tv.acfun.read.beans.Mail;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class MailHolder extends RecyclerHolder<Mail> {
    private ImageView mail_avatar;
    private TextView mail_last;
    private View mail_new;
    private TextView mail_user;

    public MailHolder(View itemView) {
        super(itemView);

        mail_avatar = (ImageView) itemView.findViewById(R.id.mail_avatar);
        mail_user = (TextView) itemView.findViewById(R.id.mail_user);
        mail_last = (TextView) itemView.findViewById(R.id.mail_last);
        mail_new = itemView.findViewById(R.id.mail_new);

        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(Mail mail) {
        ImageLoaderHelper.loadImage(mail_avatar, mail.getUser_img(), R.drawable.image_loading, R.drawable.image_idle);
        mail_user.setText(mail.getFromusername());
        mail_last.setText(mail.getSpanned());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }
}