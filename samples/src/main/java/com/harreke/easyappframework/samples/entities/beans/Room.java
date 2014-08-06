package com.harreke.easyappframework.samples.entities.beans;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/08/05
 */
public class Room {
    private Data data;
    private int error;

    public Data getData() {
        return data;
    }

    public int getError() {
        return error;
    }

    public static class Data {
        private String rtmp_live;
        private String rtmp_url;

        public String getRtmp_live() {
            return rtmp_live;
        }

        public String getRtmp_url() {
            return rtmp_url;
        }
    }
}