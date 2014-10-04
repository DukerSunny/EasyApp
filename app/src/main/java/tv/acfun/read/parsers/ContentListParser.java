package tv.acfun.read.parsers;

import android.util.Log;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

import tv.acfun.read.beans.ArticleContent;
import tv.acfun.read.beans.Content;
import tv.acfun.read.beans.FullArticle;
import tv.acfun.read.tools.ArticleUtil;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentListParser {
    private HashMap<String, FullArticle> data;
    private Content mContent;
    private ArrayList<ArrayList<ArticleContent>> mPageList;
    private ArrayList<String> mPageTitleList;
    private int mTotalPage;
    private String msg;
    private int status;

    public static ContentListParser parse(String json) {
        ContentListParser parser = GsonUtil.toBean(json, ContentListParser.class);
        FullArticle fullArticle;

        if (parser != null) {
            if (NetUtil.isStatusOk(parser.status) && parser.data != null) {
                fullArticle = parser.data.get("fullArticle");
                if (fullArticle != null && fullArticle.getTxt() != null) {
                    parser.decode(fullArticle);

                    return parser;
                }
            }
        }

        return null;
    }

    private void decode(FullArticle fullArticle) {
        ArrayList<ArticleContent> page;
        ArticleContent articleContent;
        String input;
        String[] array;
        String string;
        int i;

        mTotalPage = 0;
        mPageList = new ArrayList<ArrayList<ArticleContent>>();
        mPageTitleList = new ArrayList<String>();
        mContent = fullArticle;

        input = ArticleUtil.convertUBBTags(fullArticle.getTxt());
        page = new ArrayList<ArticleContent>();
        array = input.split("\\[break\\]");
        if (array.length != 0) {
            for (i = 0; i < array.length; i++) {
                string = array[i].replaceAll("^[\\s]+", "");
                if (string.length() > 0) {
                    if (string.startsWith("[img]")) {
                        articleContent = new ArticleContent();
                        articleContent.setContent(string.substring(5));
                        articleContent.setImage(true);
                        page.add(articleContent);
                    } else if (string.startsWith("[page]")) {
                        mTotalPage++;
                        mPageList.add(page);
                        mPageTitleList.add(string.substring(6));
                        page = new ArrayList<ArticleContent>();
                    } else {
                        articleContent = new ArticleContent();
                        articleContent.setContent("　　" + string);
                        articleContent.setImage(false);
                        page.add(articleContent);
                    }
                }
            }
            mPageList.add(page);
            if (mTotalPage == 0) {
                mTotalPage = 1;
                mPageTitleList.add("");
            }
        }
    }

    public Content getContent() {
        return mContent;
    }

    public ArrayList<ArrayList<ArticleContent>> getPageList() {
        return mPageList;
    }

    public ArrayList<String> getPageTitleList() {
        return mPageTitleList;
    }

    public int getTotalPage() {
        return mTotalPage;
    }
}