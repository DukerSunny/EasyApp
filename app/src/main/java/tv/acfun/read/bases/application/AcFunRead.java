package tv.acfun.read.bases.application;

import android.graphics.drawable.Drawable;
import android.text.Html;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.tools.FileUtil;

import java.io.File;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class AcFunRead extends ApplicationFramework {
    public static final String DIR_HISTORIES = "histories";
    public Html.ImageGetter emotGetter = new Html.ImageGetter() {
        @Override
        public Drawable getDrawable(String source) {
            return FileUtil.readDrawable(CacheDir + "/" + DIR_ASSETS + "/" + source, (int) (48 * Density), (int) (48 * Density));
        }
    };
    private boolean mHistoriesEnabled;

    public static Drawable readEmot(String emotName, String emotIndex) {
        if (emotName != null && emotName.length() > 0 && emotIndex != null && emotIndex.length() > 0) {
            return FileUtil.readDrawable(CacheDir + "/" + DIR_ASSETS + "/" + emotName + "/" + emotIndex, (int) (32 * Density), (int) (32 * Density));
        } else {
            return null;
        }
    }

    private void initFile() {
        File file;
        //        String[] assetNames = {"thumbnail", "special", "image", "avatar", "article", "anonymous"};
        String[] emotionNames = {"ac", "ais", "brd", "td", "tsj"};
        String filename;
        int[] emotionCounts = {54, 40, 40, 40, 40};
        int i;
        int j;

        mHistoriesEnabled = createDir(DIR_HISTORIES);
        if (isAssetsEnabled()) {
            //            for (i = 0; i < assetNames.length; i++) {
            //                copyAsset(assetNames[i], )
            //                file = new File(CacheDir + "/" + DIR_EMOTS + "/" + assetNames[i]);
            //                try {
            //                    if (!file.exists() && file.createNewFile()) {
            //                        is = getAssets().open(assetNames[i]);
            //                        os = new FileOutputStream(file);
            //                        FileUtil.copyFile(is, os);
            //                        is.close();
            //                        os.close();
            //                    }
            //                } catch (Exception ignored) {
            //                }
            //            }
            for (i = 0; i < emotionNames.length; i++) {
                file = new File(CacheDir + "/" + DIR_ASSETS + "/" + emotionNames[i]);
                if (file.exists() || file.mkdir()) {
                    for (j = 1; j < emotionCounts[i] + 1; j++) {
                        filename = emotionNames[i] + "/" + (j < 10 ? "0" : "") + j;
                        copyAsset(filename, "");
                    }
                }
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initFile();
    }
}
