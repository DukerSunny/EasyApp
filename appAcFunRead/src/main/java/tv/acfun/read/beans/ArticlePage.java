package tv.acfun.read.beans;

import com.harreke.easyapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.regex.Matcher;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/10/04
 */
public class ArticlePage {
    private String mArticle;
    private ArrayList<String> mImageList;
    private String mTitle;

    public ArticlePage(String title, String article) {
        Matcher tagMatcher;
        Matcher attrMatcher;
        String tag;
        String attr;
        int position;

        mTitle = title == null ? "" : title;
        mArticle = article;
        mImageList = new ArrayList<String>();
        tagMatcher = StringUtil.getMatcher("<img[\\S\\s]+?/>", mArticle);
        position = 0;
        while (tagMatcher.find()) {
            tag = tagMatcher.group();
            attrMatcher = StringUtil.getMatcher("src=\"([\\S\\s]+?)\"", tag);
            if (attrMatcher.find()) {
                attr = attrMatcher.group(1);
                mArticle =
                        mArticle.replace(tag, "<img src=\"" + attr + "\" onClick=\"content.onImgClick(" + position + ")\"/>");
                mImageList.add(attrMatcher.group(1));
            }
            position++;
        }
        tagMatcher = StringUtil.getMatcher("<a[\\S\\s]+?>([\\S\\s]+?)</a>", mArticle);
        while (tagMatcher.find()) {
            tag = tagMatcher.group();
            attrMatcher = StringUtil.getMatcher("href=\"([\\S\\s]+?)\"", tag);
            if (attrMatcher.find()) {
                attr = attrMatcher.group(1);
                mArticle = mArticle.replace(tag, "<a onClick=\"content.onAClick('" + attr + "')\">" + tagMatcher.group(1) +
                        "</a>");
            }
        }
    }

    public String getArticle() {
        return mArticle;
    }

    public ArrayList<String> getImageList() {
        return mImageList;
    }

    public String getTitle() {
        return mTitle;
    }
}