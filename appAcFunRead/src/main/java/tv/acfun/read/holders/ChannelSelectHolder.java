package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ChannelSelectHolder implements IAbsListHolder<String> {
    private TextView channelselect;

    public ChannelSelectHolder(View convertView) {
        channelselect = (TextView) convertView.findViewById(R.id.channelselect);
    }

    @Override
    public void setItem(int position, String item) {
        channelselect.setText(item);
    }
}
