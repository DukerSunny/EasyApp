package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class PopupListHoler implements IAbsListHolder<String> {
    private TextView popuplist;

    public PopupListHoler(View convertView) {
        popuplist = (TextView) convertView.findViewById(R.id.popuplist);
    }

    @Override
    public void setItem(int position, String item) {
        popuplist.setText(item);
    }
}
