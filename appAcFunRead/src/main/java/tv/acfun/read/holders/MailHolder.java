package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Mail;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class MailHolder implements IAbsListHolder<Mail> {
    private ImageView mail_avatar;
    private TextView mail_last;
    private View mail_new;
    private TextView mail_user;

    public MailHolder(View convertView) {
        mail_avatar = (ImageView) convertView.findViewById(R.id.mail_avatar);
        mail_user = (TextView) convertView.findViewById(R.id.mail_user);
        mail_last = (TextView) convertView.findViewById(R.id.mail_last);
        mail_new = convertView.findViewById(R.id.mail_new);
    }

    @Override
    public void setItem(int position, Mail mail) {
        ImageLoaderHelper.loadImage(mail_avatar, mail.getUser_img());
        mail_user.setText(mail.getFromusername());
        mail_last.setText(mail.getSpanned());
    }
}