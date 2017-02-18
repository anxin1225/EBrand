package com.sam.ebrand.param;

import android.os.Environment;

/**
 * Created by sam on 2016/12/3.
 */
public class SampleParam {
    public static final int HTTP_DEFAULT_TIMEOUT = 30000;
    public static Boolean IFMUTE;
    public static final int LIMIT = 5;
    public static Boolean LOGINSUCCESS;
    public static String MeetingTitle;
    public static String MeetingUSERNAME;
    public static String NoticeStr;
    public static final int ORDER_BY_ACTIVITY = 1;
    public static final int ORDER_BY_FRIEND = 2;
    public static final int ORDER_BY_VISIT = 3;
    public static final String SDCARD_ROOT;
    public static final int SEARCH_RULE_CITY = 2;
    public static final int SEARCH_RULE_DEFAULT = 0;
    public static final int SEARCH_RULE_FAMILIARITY = 1;
    public static final int SEARCH_RULE_INDUSTRY = 3;
    public static String USERID;
    public static boolean authority;
    public static int blue;
    public static String fontColor;
    public static String fontName;
    public static int fontSize;
    public static int green;
    public static int red;

    static {
        SDCARD_ROOT = Environment.getExternalStorageDirectory() + "/sample";
        SampleParam.LOGINSUCCESS = false;
        SampleParam.IFMUTE = false;
        SampleParam.USERID = "";
        SampleParam.MeetingTitle = "";
        SampleParam.MeetingUSERNAME = "";
        SampleParam.NoticeStr = "";
        SampleParam.fontSize = 16;
        SampleParam.fontColor = "";
        SampleParam.fontName = "";
        SampleParam.red = 100;
        SampleParam.green = 100;
        SampleParam.blue = 100;
        SampleParam.authority = false;
    }
}
