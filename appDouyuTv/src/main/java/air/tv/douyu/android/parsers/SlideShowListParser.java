package air.tv.douyu.android.parsers;

import com.harreke.easyapp.tools.GsonUtil;
import com.harreke.easyapp.tools.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.SlideShow;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShowListParser {
    private List<SlideShow> data;
    private int error;

    public static SlideShowListParser parse(String json) {
        SlideShowListParser parser = GsonUtil.toBean(json, SlideShowListParser.class);
        SlideShow slideShow;
        int i;

        if (parser != null && parser.error == 0 && parser.data != null && parser.getData().size() > 0) {
            for (i = 0; i < parser.data.size(); i++) {
                slideShow = parser.data.get(i);
                slideShow.setTitle(StringUtil.escapeHtmlSymbols(slideShow.getTitle()));
            }

            return parser;
        } else {
            return null;
        }
    }

    public List<SlideShow> getData() {
        return data;
    }
}
