package air.tv.douyu.android.parsers;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.Recommend;
import air.tv.douyu.android.beans.SlideShowRecommend;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class RecommendListParser {
    private List<Recommend> data;
    private int error;

    public static RecommendListParser parse(String json) {
        RecommendListParser parser = GsonUtil.toBean(json, RecommendListParser.class);
        Recommend recommend;
        int i;

        if (parser != null && parser.error == 0 && parser.data != null) {
            for (i = 0; i < parser.data.size(); i++) {
                recommend = parser.data.get(i);
                recommend.setTitle(StringUtil.escapeHtmlSymbols(recommend.getTitle()));
            }

            return parser;
        } else {
            return null;
        }
    }

    public List<Recommend> getData() {
        return data;
    }
}
