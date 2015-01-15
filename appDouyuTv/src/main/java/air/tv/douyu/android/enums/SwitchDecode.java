package air.tv.douyu.android.enums;

import air.tv.douyu.android.R;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/08
 */
public enum SwitchDecode {
    HW(R.string.setting_system_switch_decode_hw),
    SW(R.string.setting_system_switch_decode_sw);

    private int mTextId;

    private SwitchDecode(int textId) {
        mTextId = textId;
    }

    public static SwitchDecode get(int index) {
        if (index == 0) {
            return HW;
        } else {
            return SW;
        }
    }

    public static int indexOf(SwitchDecode switchDecode) {
        if (switchDecode == HW) {
            return 0;
        } else {
            return 1;
        }
    }

    public int getTextId() {
        return mTextId;
    }
}