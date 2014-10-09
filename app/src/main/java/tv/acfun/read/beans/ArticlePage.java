package tv.acfun.read.beans;

import com.harreke.easyapp.tools.StringUtil;

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
        Matcher imgMatcher;
        Matcher srcMatcher;
        String img;
        String src;
        int position;

        mTitle = title;
        mArticle = article;
        mImageList = new ArrayList<String>();
        imgMatcher = StringUtil.getMatcher("<img[\\S\\s]+?/>", article);
        position = 0;
        while (imgMatcher.find()) {
            img = imgMatcher.group();
            srcMatcher = StringUtil.getMatcher("src=\"([\\S\\s]+?)\"", img);
            if (srcMatcher.find()) {
                src = srcMatcher.group(1);
                mArticle = mArticle.replace(img,
                        "<img src=\"" + src + "\" onClick=\"content.onSingleClicked(" + position + ")\"/>");
                mImageList.add(srcMatcher.group(1));
            }
            position++;
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