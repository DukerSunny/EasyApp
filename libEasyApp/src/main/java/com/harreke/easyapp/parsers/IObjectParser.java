package com.harreke.easyapp.parsers;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/01/23
 */
public interface IObjectParser<ITEM> {
    public ObjectResult<ITEM> parse(String json);
}
