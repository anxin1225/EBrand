package com.sam.ebrand.manage;

import android.util.Log;

import com.sam.ebrand.param.MeetingParam;

import java.io.File;

/**
 * Created by sam on 2016/11/23.
 */

public class SDFolderManager
{
    private static SDFolderManager mInstance;
    private static int mRefCount;

    static {
        SDFolderManager.mInstance = null;
        SDFolderManager.mRefCount = 0;
    }

    private SDFolderManager() {
        createFolders();
    }

    public static File creatDir(final String s) {
        try {
            File file = new File(s);
            if (!file.exists()) {
                file.mkdirs();
            }

            if(file.exists())
                return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }

    public static void createFolders() {
        creatDir(MeetingParam.SDCARD_ROOT);
        creatDir(MeetingParam.SDCARD_WHITEBOARD);
        creatDir("/mnt/usb_storage/Meeting");
        creatDir(MeetingParam.SDCARD_DOC);
        creatDir(MeetingParam.SDCARD_COMMENT);
        creatDir(String.valueOf(MeetingParam.SDCARD_ROOT) + "/mpbackground");
    }

    public static SDFolderManager getInstance() {
        synchronized (SDFolderManager.class) {
            if (SDFolderManager.mInstance == null) {
                SDFolderManager.mInstance = new SDFolderManager();
            }
            ++SDFolderManager.mRefCount;
            return SDFolderManager.mInstance;
        }
    }

    public static void release() {
        --SDFolderManager.mRefCount;
        if (SDFolderManager.mRefCount == 0) {
            SDFolderManager.mInstance = null;
        }
    }
}
