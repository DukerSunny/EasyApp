package tv.acfun.read.parsers;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;
import com.harreke.easyapp.tools.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import tv.acfun.read.beans.ArticlePage;
import tv.acfun.read.beans.Content;
import tv.acfun.read.beans.FullArticle;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/25
 */
public class ContentListParser {
    private static String TAG = "ContentListParser";

    private HashMap<String, FullArticle> data;
    private Content mContent;
    private ArrayList<ArticlePage> mPageList;
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
        Matcher matcher;
        ArticlePage articlePage;
        String input;
        mContent = fullArticle;

        mPageList = new ArrayList<ArticlePage>();

        input = fullArticle.getTxt();
        //        input = input.replace("<p>", "").replaceAll("<p[\\S\\s]+?>", "").replace("</p>", "");
        //        input = input.replace("<div>", "").replaceAll("<div[\\S\\s]+?>", "").replace("</div>", "");
        //        input = input.replace("<span>", "").replaceAll("<span[\\S\\s]+?>", "").replace("</span>", "");
        //        input = input.replaceAll(" style=[\\S\\s]+?", "");

        matcher = StringUtil.getMatcher("(\\S\\s)+?\\[NextPage\\]([\\S\\s]+?)\\[/NextPage\\]", input);
        while (matcher.find()) {
            articlePage = new ArticlePage(matcher.group(2), matcher.group(1));
            mPageList.add(articlePage);
        }
        if (mPageList.size() == 0) {
            articlePage = new ArticlePage(null, input);
            mPageList.add(articlePage);
        }
    }

    public Content getContent() {
        return mContent;
    }

    public ArrayList<ArticlePage> getPageList() {
        return mPageList;
    }
}