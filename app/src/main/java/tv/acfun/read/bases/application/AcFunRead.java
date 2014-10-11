package tv.acfun.read.bases.application;

import android.graphics.drawable.Drawable;

import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.tools.FileUtil;
import com.harreke.easyapp.tools.GsonUtil;

import java.io.File;

import tv.acfun.read.R;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.beans.Token;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class AcFunRead extends ApplicationFramework {
    public static final String DIR_HISTORIES = "histories";
    public static AcFunRead mInstance = null;
    private boolean mHistoriesEnabled;

    public static AcFunRead getInstance() {
        return mInstance;
    }

    public static boolean isArticle(int channelId) {
        return channelId == 110 || channelId == 73 || channelId == 74 || channelId == 75;
    }

    public static Drawable readEmot(String emotName, String emotIndex) {
        if (emotName != null && emotName.length() > 0 && emotIndex != null && emotIndex.length() > 0) {
            return FileUtil.readDrawable(CacheDir + "/" + DIR_ASSETS + "/" + emotName + "/" + emotIndex, (int) (48 * Density),
                    (int) (48 * Density));
        } else {
            return null;
        }
    }

    public static Drawable readEmot(String filename) {
        if (filename != null && filename.length() > 0) {
            return FileUtil.readDrawable(filename, (int) (48 * Density), (int) (48 * Density));
        } else {
            return null;
        }
    }

    private void initFile() {
        File file;
        String[] emotionNames = getResources().getStringArray(R.array.emot_name);
        String filename;
        int[] emotionCounts = getResources().getIntArray(R.array.emot_count);
        int i;
        int j;

        mHistoriesEnabled = createDir(DIR_HISTORIES);
        if (isAssetsEnabled()) {
            copyAsset("web_loading", "");
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

    public boolean isHistoriesEnabled() {
        return mHistoriesEnabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initFile();

        mInstance = this;

    }

    public final FullUser readFullUser() {
        return GsonUtil.toBean(readString("fullUser", null), FullUser.class);
    }

    public final Token readToken() {
        return GsonUtil.toBean(readString("token", null), Token.class);
    }

    public final void writeFullUser(FullUser fullUser) {
        writeString("fullUser", GsonUtil.toString(fullUser));
    }

    public final void writeToken(Token token) {
        writeString("token", GsonUtil.toString(token));
    }
}
