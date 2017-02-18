package com.sam.ebrand.manage;

import android.graphics.Typeface;
import android.os.Message;
import android.util.Log;

import java.io.File;

/**
 * Created by sam on 2016/11/16.
 */
public class ServerFontsManager {

    private static ServerFontsManager instance;
    private static final String[] mFontFileName;
    public static final String[] mFontName;
    private static Typeface[] mTypeFace;

    static {
        ServerFontsManager.instance = null;
        mFontName = new String[] { "\u9ed8\u8ba4\u5b57\u4f53", "\u534e\u6587\u884c\u6977", "\u65b9\u6b63\u786c\u7b14\u884c\u4e66", "\u6977\u4f53", "\u534e\u6587\u65b0\u5b8b", "\u65b9\u6b63\u9b4f\u7891", "\u65b9\u6b63\u7c97\u5706", "\u96b6\u4e66", "\u534e\u6587\u5f69\u4e91", "\u5fae\u8f6f\u96c5\u9ed1" };
        mFontFileName = new String[] { "default", "hwhk.ttf", "fzybhs.ttf", "kaiti.ttf", "hwxs.ttf", "fzwb.ttf", "fzcy.ttf", "ls.ttf", "hwcy.ttf", "wryh.ttf" };
    }

    private ServerFontsManager() {
        this.Reload();
    }

    public static ServerFontsManager getInstance() {
        synchronized (ServerFontsManager.class) {
            if (ServerFontsManager.instance == null) {
                ServerFontsManager.instance = new ServerFontsManager();
            }
            return ServerFontsManager.instance;
        }
    }

    public void Reload() {
        (ServerFontsManager.mTypeFace = new Typeface[10])[0] = Typeface.DEFAULT;
        int i = 1;
        while (i < 10) {
            while (true) {
                try {
                    ServerFontsManager.mTypeFace[i] = Typeface.createFromFile("/system/fonts/" + ServerFontsManager.mFontFileName[i]);
                    ++i;
                }
                catch (Exception ex) {
                    Log.e("Fonts", "create font failed!");
                    ServerFontsManager.mTypeFace[i] = Typeface.DEFAULT;
                    if (new File("/system/fonts/" + ServerFontsManager.mFontFileName[i]).exists()) {
                        final Message obtain = Message.obtain();
                        obtain.what = 13;
                        SocketManager.mStaticHandler.sendMessageDelayed(obtain, 1000L);
                        return;
                    }
                    continue;
                }
                break;
            }
        }
    }

    public Typeface getTypeface(final int n) {
        return this.getTypeface(ServerFontsManager.mFontName[n]);
    }

    public Typeface getTypeface(final String s) {
        int n = 0;
        while (true) {
            Block_3: {
                int n2;
                while (true) {
                    final int length = ServerFontsManager.mFontName.length;
                    n2 = 0;
                    if (n >= length) {
                        break;
                    }
                    if (s.equals(ServerFontsManager.mFontName[n])) {
                        break Block_3;
                    }
                    ++n;
                }
                if (s.equals("\u534e\u6587\u9b4f\u7891")) {
                    n2 = 5;
                }
                if (n2 < 0 || n2 >= ServerFontsManager.mTypeFace.length) {
                    return Typeface.DEFAULT;
                }
                return ServerFontsManager.mTypeFace[n2];
            }
            int n2 = n;
            continue;
        }
    }
}

