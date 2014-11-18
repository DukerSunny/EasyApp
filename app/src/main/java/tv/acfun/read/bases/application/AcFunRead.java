package tv.acfun.read.bases.application;

import android.graphics.drawable.Drawable;

import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.tools.FileUtil;
import com.harreke.easyapp.tools.GsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.beans.Content;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.beans.Token;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class AcFunRead extends ApplicationFramework {
    public static AcFunRead mInstance = null;

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

    public void addHistory(Content content) {
        List<Content> list;
        Content history;
        int i;

        if (content != null) {
            list = readHistory();
            if (list != null) {
                if (list.size() > 30) {
                    list = list.subList(0, 30);
                }
            } else {
                list = new ArrayList<Content>();
            }
            for (i = 0; i < list.size(); i++) {
                history = list.get(i);
                if (history.getContentId() == content.getContentId()) {
                    break;
                }
            }
            if (i < list.size()) {
                list.remove(i);
            }
            list.add(0, content);
            writeHistory(list);
        }
    }

    public final void clearLogin() {
        writeFullUser(null);
        writeToken(null);
    }

    private void initFile() {
        File file;
        String[] emotionNames = getResources().getStringArray(R.array.emot_name);
        String filename;
        int[] emotionCounts = getResources().getIntArray(R.array.emot_count);
        int i;
        int j;

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

    public final boolean isExpired() {
        Token token = readToken();

        return token == null || token.isExpired();
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

    public List<Content> readHistory() {
        if (isCachesEnabled()) {
            return GsonUtil.toBean(FileUtil.readTxt(new File(CacheDir + "/" + DIR_CACHES + "/history.cache")),
                    new TypeToken<ArrayList<Content>>() {
                    }.getType());
        } else {
            return null;
        }
    }

    public final Token readToken() {
        return GsonUtil.toBean(readString("token", null), Token.class);
    }

    public final void writeFullUser(FullUser fullUser) {
        writeString("fullUser", GsonUtil.toString(fullUser));
    }

    public void writeHistory(List<Content> list) {
        String json;

        if (isCachesEnabled()) {
            if (list != null) {
                json = GsonUtil.toString(list);
            } else {
                json = "";
            }
            FileUtil.writeTxt(new File(CacheDir + "/" + DIR_CACHES + "/history.cache"), json);
        }
    }

    public final void writeToken(Token token) {
        writeString("token", GsonUtil.toString(token));
    }
}
