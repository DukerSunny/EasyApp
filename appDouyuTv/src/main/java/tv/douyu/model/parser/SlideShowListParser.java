package tv.douyu.model.parser;

import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.parsers.Parser;
import com.harreke.easyapp.utils.StringUtil;

import java.util.List;

import tv.douyu.model.bean.SlideShow;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/18
 */
public class SlideShowListParser implements IListParser<SlideShow> {
    @Override
    public ListResult<SlideShow> parse(String json) {
        ListResult<SlideShow> listResult;
        List<SlideShow> list;
        SlideShow slideShow;
        int i;

        if (json != null) {
            json = json.replace("\"rtmp_multi_bitrate\":[],", "");
        }
        listResult = Parser.parseList(json, SlideShow.class, "error", "data", "data");
        if (listResult != null && listResult.getList() != null) {
            list = listResult.getList();
            for (i = 0; i < list.size(); i++) {
                slideShow = list.get(i);
                slideShow.setTitle(StringUtil.escapeHtmlSymbols(slideShow.getTitle()));
            }
        }

        return listResult;
    }
}