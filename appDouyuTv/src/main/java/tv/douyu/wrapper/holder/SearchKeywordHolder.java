package tv.douyu.wrapper.holder;

import android.view.View;
import android.widget.TextView;

import com.harreke.easyapp.frameworks.recyclerview.RecyclerHolder;
import com.harreke.easyapp.widgets.rippleeffects.RippleDrawable;
import com.harreke.easyapp.widgets.rippleeffects.RippleOnClickListener;

import tv.douyu.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/21
 */
public class SearchKeywordHolder extends RecyclerHolder<String> {
    private TextView searchkeyword_text;

    public SearchKeywordHolder(View itemView) {
        super(itemView);

        searchkeyword_text = (TextView) itemView.findViewById(R.id.searchkeyword_text);

        RippleDrawable.attach(itemView);
    }

    @Override
    public void setItem(String text) {
        searchkeyword_text.setText(text);
    }

    @Override
    public void setOnClickListener(View.OnClickListener onClickListener) {
        RippleOnClickListener.attach(itemView, onClickListener);
    }
}