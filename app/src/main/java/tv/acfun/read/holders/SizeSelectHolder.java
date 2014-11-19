package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class SizeSelectHolder implements IAbsListHolder<Integer> {
    private TextView sizeselect;

    public SizeSelectHolder(View convertView) {
        sizeselect = (TextView) convertView.findViewById(R.id.sizeselect);
    }

    @Override
    public void setItem(int position, Integer item) {
        sizeselect.setText(String.valueOf(item));
    }
}
