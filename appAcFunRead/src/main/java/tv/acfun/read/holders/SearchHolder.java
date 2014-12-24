package tv.acfun.read.holders;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.acfun.read.R;
import tv.acfun.read.beans.Search;
import tv.acfun.read.helpers.ChannelHelper;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class SearchHolder extends RecyclerHolder<Search> {
    private View search_icon;
    private TextView search_name;
    private TextView search_title;

    public SearchHolder(View itemView) {
        super(itemView);

        search_icon = itemView.findViewById(R.id.search_icon);
        search_name = (TextView) itemView.findViewById(R.id.search_name);
        search_title = (TextView) itemView.findViewById(R.id.search_title);

        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(Search search) {
        search_icon.setBackgroundResource(ChannelHelper.getDrawableIdByChannelId(search.getChannelId()));
        search_name.setText(ChannelHelper.getTitleIdByChannelId(search.getChannelId()));
        search_title.setText(search.getTitle());
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }
}