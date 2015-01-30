package tv.douyu.model.parser;

import com.harreke.easyapp.parsers.IListParser;
import com.harreke.easyapp.parsers.ListResult;
import com.harreke.easyapp.parsers.ObjectResult;
import com.harreke.easyapp.parsers.Parser;

import java.util.List;

import tv.douyu.model.bean.Advertise;
import tv.douyu.model.bean.AdvertiseData;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class AdvertiseListParser implements IListParser<Advertise> {
    private int mAvailableCount;

    public int getAvailableCount() {
        return mAvailableCount;
    }

    @Override
    public ListResult<Advertise> parse(String json) {
        ListResult<Advertise> advertiseList = new ListResult<Advertise>();
        ObjectResult<AdvertiseData> advertiseData;
        AdvertiseData data;
        List<Advertise> itemList;
        List<Integer> downList;
        int availableCount = 0;
        int i;
        int j;

        advertiseData = Parser.parseObject(json, AdvertiseData.class, "error", "data", "data");
        if (advertiseData != null && advertiseData.getObject() != null) {
            data = advertiseData.getObject();
            itemList = data.getData();
            downList = data.getDown_list();
            if (itemList != null && itemList.size() > 0 && downList != null && downList.size() > 0) {
                availableCount = itemList.size();
                for (i = 0; i < itemList.size(); i++) {
                    for (j = 0; j < downList.size(); i++) {
                        if (itemList.get(i).hashCode() == downList.get(i)) {
                            itemList.get(i).setDownloaded(true);
                            availableCount--;
                        }
                    }
                }
            }
            advertiseList.setFlag(advertiseData.getFlag());
            advertiseList.setList(itemList);
            advertiseList.setMessage(advertiseData.getMessage());
            mAvailableCount = availableCount;
        }

        return advertiseList;
    }
}