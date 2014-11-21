package tv.acfun.read.tools;

import java.io.File;
import java.io.FileFilter;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/11/21
 */
public class FontFilter implements FileFilter {
    @Override
    public boolean accept(File pathname) {
        return pathname.isFile() && pathname.length() > 0 && pathname.getAbsolutePath().toLowerCase().endsWith(".ttf");
    }
}
