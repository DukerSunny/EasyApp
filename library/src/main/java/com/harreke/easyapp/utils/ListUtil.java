package com.harreke.easyapp.utils;

import java.util.List;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/03/18
 */
public class ListUtil {
    public static <T> T getRandomItem(List<T> list) {
        if (list == null || list.size() == 0) {
            return null;
        } else {
            return list.get((int) (Math.random() * (list.size() - 1)));
        }
    }
}
