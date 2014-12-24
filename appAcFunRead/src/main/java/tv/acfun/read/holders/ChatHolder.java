package tv.acfun.read.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.Mail;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/27
 */
public class ChatHolder extends RecyclerHolder<Mail> {
    private ImageView chat_avatar;
    private TextView chat_receive;
    private TextView chat_send;

    public ChatHolder(View itemView) {
        super(itemView);

        chat_send = (TextView) itemView.findViewById(R.id.chat_send);
        chat_avatar = (ImageView) itemView.findViewById(R.id.chat_avatar);
        chat_receive = (TextView) itemView.findViewById(R.id.chat_receive);
    }

    @Override
    public void setItem(Mail mail) {

    }
}