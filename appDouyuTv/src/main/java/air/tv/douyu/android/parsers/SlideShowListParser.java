package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ListParser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import air.tv.douyu.android.beans.SlideShow;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShowListParser extends ListParser<SlideShow> {
    @Override
    public void parse(String json) {
        List<SlideShow> list;
        SlideShow slideShow;
        int i;

        if (json != null) {
            json = json.replace("\"rtmp_multi_bitrate\":[],", "");
        }
        list = Parser.parseList(json, SlideShow.class);
        if (list != null) {
            for (i = 0; i < list.size(); i++) {
                slideShow = list.get(i);
                slideShow.setTitle(StringUtil.escapeHtmlSymbols(slideShow.getTitle()));
            }
            setList(list);
        }
    }
}