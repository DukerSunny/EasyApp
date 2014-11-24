package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Mail;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class ChatHolder implements IAbsListHolder<Mail> {
    private ImageView chat_avatar;
    private TextView chat_receive;
    private TextView chat_send;

    public ChatHolder(View convertView) {
        chat_send = (TextView) convertView.findViewById(R.id.chat_send);
        chat_avatar = (ImageView) convertView.findViewById(R.id.chat_avatar);
        chat_receive = (TextView) convertView.findViewById(R.id.chat_receive);
    }

    @Override
    public void setItem(int position, Mail mail) {

    }
}