package air.tv.douyu.android.parsers;

import com.harreke.easyapp.parsers.ListParser;

import java.util.List;

import air.tv.douyu.android.beans.Advertise;
import air.tv.douyu.android.beans.AdvertiseData;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/26
 */
public class AdvertiseListParser extends ListParser<Advertise> {
    private int mAvailableCount;

    public int getAvailableCount() {
        return mAvailableCount;
    }

    @Override
    public void parse(String json) {
        AdvertiseData data;
        List<Advertise> itemList;
        List<Integer> downList;
        int availableCount = 0;
        int i;
        int j;

        data = Parser.parseObject(json, AdvertiseData.class);
        if (data != null) {
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
            setList(itemList);
            mAvailableCount = availableCount;
        }
    }
}
