package tv.acfun.read.bases.application;

import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;

import com.google.gson.reflect.TypeToken;
import com.harreke.easyapp.frameworks.bases.application.ApplicationFramework;
import com.harreke.easyapp.tools.FileUtil;
import com.harreke.easyapp.tools.GsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import tv.acfun.read.R;
import tv.acfun.read.bases.receivers.ConnectionReceiver;
import tv.acfun.read.beans.Content;
import tv.acfun.read.beans.FullUser;
import tv.acfun.read.beans.Setting;
import tv.acfun.read.beans.Token;
import tv.acfun.read.helpers.ConnectionHelper;
import tv.acfun.read.tools.FontFilter;

/**
 * 由 Harreke（harreke@live.cn） 创建于 2014/09/24
 */
public class AcFunRead extends ApplicationFramework {
    public static final String DIR_FONTS = "fonts";
    public static AcFunRead mInstance = null;
    private ConnectionReceiver mConnectionReceiver = null;
    private boolean mFontsEnabled = false;

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

        mFontsEnabled = createStorageDir("AcFun") && createStorageDir("AcFun/" + DIR_FONTS);
        if (isAssetsEnabled()) {
            copyAsset("web_loading", "");
            copyAsset("web_download", "");
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

    public boolean isFontsEnabled() {
        return mFontsEnabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initFile();

        mInstance = this;
    }

    public List<String> readFontsPath() {
        List<String> fontsPathList = new ArrayList<String>();
        File dir;
        File[] files;
        int i;

        if (mFontsEnabled) {
            dir = new File(StorageDir + "/AcFun/" + DIR_FONTS);
            if (dir.exists() && dir.isDirectory()) {
                files = dir.listFiles(new FontFilter());
                for (i = 0; i < files.length; i++) {
                    fontsPathList.add(files[i].getAbsolutePath());
                }
            }
        }

        return fontsPathList;
    }

    public final FullUser readFullUser() {
        return GsonUtil.toBean(readString("fullUser", null), FullUser.class);
    }

    public final List<Content> readHistory() {
        List<Content> historyList;

        if (isTempsEnabled()) {
            historyList = GsonUtil.toBean(FileUtil.readTxt(new File(CacheDir + "/" + DIR_TEMPS + "/history.cache")),
                    new TypeToken<ArrayList<Content>>() {
                    }.getType());
            if (historyList == null) {
                historyList = new ArrayList<Content>();
            }
        } else {
            historyList = null;
        }

        return historyList;
    }

    public final Setting readSetting() {
        Setting setting = GsonUtil.toBean(readString("setting", null), Setting.class);

        if (setting == null) {
            setting = new Setting();
        }

        return setting;
    }

    public final Token readToken() {
        return GsonUtil.toBean(readString("token", null), Token.class);
    }

    public final void registerConnectionReceiver() {
        if (mConnectionReceiver == null) {
            mConnectionReceiver = new ConnectionReceiver();
        }
        ConnectionHelper.checkConnection(this);
        registerReceiver(mConnectionReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public final void unregisterConnectionReceiver() {
        if (mConnectionReceiver != null) {
            unregisterReceiver(mConnectionReceiver);
            mConnectionReceiver = null;
        }
    }

    public final void writeFullUser(FullUser fullUser) {
        writeString("fullUser", GsonUtil.toString(fullUser));
    }

    public void writeHistory(List<Content> list) {
        String json;

        if (isTempsEnabled()) {
            if (list != null) {
                json = GsonUtil.toString(list);
            } else {
                json = "";
            }
            FileUtil.writeTxt(new File(CacheDir + "/" + DIR_TEMPS + "/history.cache"), json);
        }
    }

    public final void writeSetting(Setting setting) {
        writeString("setting", GsonUtil.toString(setting));
    }

    public final void writeToken(Token token) {
        writeString("token", GsonUtil.toString(token));
    }
}