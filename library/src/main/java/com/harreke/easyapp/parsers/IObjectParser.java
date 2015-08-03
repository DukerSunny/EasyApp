package com.harreke.easyapp.parsers;

import android.support.annotation.NonNull;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/23
 */
public interface IObjectParser<ITEM> {
    @NonNull
    ObjectResult<ITEM> parse(String json);
}
