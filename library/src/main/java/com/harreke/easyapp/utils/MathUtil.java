package com.harreke.easyapp.utils;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/26
 */
public class MathUtil {
    public static float getDistance(PointF p1, PointF p2) {
        return (float) Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }

    public static float getDistance(Point p1, Point p2) {
        return (float) Math.sqrt((p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y) * (p2.y - p1.y));
    }

    public static float getDistance(float dx, float dy) {
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public static float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }

    public static float getDistance(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
    }
}