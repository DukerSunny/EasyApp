package com.harreke.easyapp.requests.executors;

import java.io.File;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2015/04/30
 */
public class UploadPair {
    public String contentType;
    public File file;
    public String name;

    public UploadPair(String name, String contentType, File file) {
        this.name = name;
        this.contentType = contentType;
        this.file = file;
    }
}