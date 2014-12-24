package tv.acfun.read.helpers;

import tv.acfun.read.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/12/22
 */
public class ChannelHelper {
    public static int getDrawableIdByChannelId(int channelId) {
        switch (channelId) {
            default:
            case 110:
                return R.drawable.shape_circle_channel_misc;
            case 73:
                return R.drawable.shape_circle_channel_work_emotion;
            case 74:
                return R.drawable.shape_circle_channel_dramaculture;
            case 75:
                return R.drawable.shape_circle_channel_comic_novel;
        }
    }

    public static int getTitleIdByChannelId(int channelId) {
        switch (channelId) {
            default:
            case 110:
                return R.string.channel_misc;
            case 73:
                return R.string.channel_work_emotion;
            case 74:
                return R.string.channel_dramaculture;
            case 75:
                return R.string.channel_comic_novel;
        }
    }
}