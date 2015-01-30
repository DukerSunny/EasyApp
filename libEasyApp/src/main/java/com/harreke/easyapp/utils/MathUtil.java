package com.harreke.easyapp.utils;

import android.graphics.PointF;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public class MathUtil {
    public static float getDistance(PointF p1, PointF p2) {
        return Math.abs((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }
}