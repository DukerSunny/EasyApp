package tv.acfun.read.holders;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.harreke.easyapp.helpers.ImageLoaderHelper;
import com.harreke.easyapp.holders.abslistview.IAbsListHolder;

import tv.acfun.read.R;
import tv.acfun.read.beans.ArticleContent;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ArticleContentHolder implements IAbsListHolder<ArticleContent> {
    private ImageView content_image;
    private TextView content_text;

    public ArticleContentHolder(View convertView) {
        content_image = (ImageView) convertView.findViewById(R.id.content_image);
        content_text = (TextView) convertView.findViewById(R.id.content_text);
    }

    @Override
    public void setItem(int position, ArticleContent articleContent) {
        if (articleContent.isImage()) {
            content_image.setVisibility(View.VISIBLE);
            content_text.setVisibility(View.GONE);
            ImageLoaderHelper.loadImage(content_image, articleContent.getContent());
        } else {
            content_text.setVisibility(View.VISIBLE);
            content_image.setVisibility(View.GONE);
            content_text.setText(Html.fromHtml(articleContent.getContent()));
        }
    }
}