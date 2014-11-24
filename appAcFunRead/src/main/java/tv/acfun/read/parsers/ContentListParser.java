package tv.acfun.read.parsers;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.NetUtil;

import java.util.ArrayList;
import java.util.HashMap;

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
        ArticlePage articlePage;
        String input;
        String[] inputs;
        String content = null;
        int i;

        mContent = fullArticle;

        mPageList = new ArrayList<ArticlePage>();

        input = fullArticle.getTxt();
        input = input.replace("<p>[NextPage]", "[break][title]").replace("[/NextPage]<p>", "[break]");
        input = input.replace("[NextPage]", "[break][title]").replace("[/NextPage]", "[break]");
        inputs = input.split("\\[break\\]");
        if (inputs.length > 1) {
            for (i = 0; i < inputs.length; i++) {
                if (inputs[i].startsWith("[title]")) {
                    articlePage = new ArticlePage(inputs[i].substring(7), content);
                    mPageList.add(articlePage);
                    content = null;
                } else {
                    content = inputs[i];
                }
            }
        } else {
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