package tv.douyu.model.bean;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/07
 */
public class AdvertiseData {
    private List<Advertise> data;
    private List<Integer> down_list;

    public List<Advertise> getData() {
        return data;
    }

    public List<Integer> getDown_list() {
        return down_list;
    }
}
