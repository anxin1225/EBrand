package com.sam.ebrand.meetingNetwork.socket.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sam on 2016/11/23.
 */

public class MeetingDBOpenHelper extends SQLiteOpenHelper
{
    public static final String COLUMN_AID = "aid";
    public static final String COLUMN_APP_NAME = "appName";
    public static final String COLUMN_AVATAR_URL = "avatarUrl";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_DB_ID = "dbID";
    public static final String COLUMN_DOWN_URL = "downloadUrl";
    public static final String COLUMN_FAVOURITE = "favourite";
    public static final String COLUMN_FRIEND_ACCOUNT = "account";
    public static final String COLUMN_FRIEND_AVATAR_URL = "avatarUrl";
    public static final String COLUMN_FRIEND_COIN = "coin";
    public static final String COLUMN_FRIEND_DB_ID = "id";
    public static final String COLUMN_FRIEND_DIRECTION = "direction";
    public static final String COLUMN_FRIEND_DISPLAY = "display";
    public static final String COLUMN_FRIEND_DISTANCE = "distance";
    public static final String COLUMN_FRIEND_FANS_COUNT = "fansCount";
    public static final String COLUMN_FRIEND_FOLLOW_COUNT = "followCount";
    public static final String COLUMN_FRIEND_IS_FOLLOW = "isFollow";
    public static final String COLUMN_FRIEND_LATITUDE = "latitude";
    public static final String COLUMN_FRIEND_LONGITUDE = "longitude";
    public static final String COLUMN_FRIEND_MESSAGE_COUNT = "messageCount";
    public static final String COLUMN_FRIEND_ONLINE = "online";
    public static final String COLUMN_FRIEND_POINT = "point";
    public static final String COLUMN_FRIEND_REMARK = "remark";
    public static final String COLUMN_FRIEND_SEX = "sex";
    public static final String COLUMN_FRIEND_TYPE = "friend_type";
    public static final String COLUMN_FRIEND_USER_ID = "userId";
    public static final String COLUMN_FRIEND_USER_NAME = "userName";
    public static final String COLUMN_FRIEND_VISITOR_COUNT = "visitorCount";
    public static final String COLUMN_FRIEND_WEIBO_ACCOUNT_NAME = "weibo_accountName";
    public static final String COLUMN_FRIEND_WEIBO_TOKEN = "weibo_token";
    public static final String COLUMN_FRIEND_WEIBO_TOKEN_SECRET = "weibo_tokenSecret";
    public static final String COLUMN_FRIEND_WEIBO_TYPE = "weibo_type";
    public static final String COLUMN_FROM_USER_ID = "fromUserId";
    public static final String COLUMN_ICON_URL = "iconUrl";
    public static final String COLUMN_ISREAD = "isRead";
    public static final String COLUMN_MESSAGE_DB_ID = "id";
    public static final String COLUMN_PACKAGE_NAME = "packageName";
    public static final String COLUMN_PICTURE_URL = "pictureUrl";
    public static final String COLUMN_SMS_ID = "smsId";
    public static final String COLUMN_STATUS = "appStatus";
    public static final String COLUMN_SUMNMARYURL = "summaryUrl";
    public static final String COLUMN_THEME_APK_SIZE = "apkSize";
    public static final String COLUMN_THEME_DB_ID = "id";
    public static final String COLUMN_THEME_ID = "themeID";
    public static final String COLUMN_THEME_IMAGE_PATH = "imagePath";
    public static final String COLUMN_THEME_INFO = "info";
    public static final String COLUMN_THEME_NAME = "themeName";
    public static final String COLUMN_THEME_PACKAGE_NAME = "packageName";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_TO_USER_ID = "toUserId";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_USER_NAME = "userName";
    public static final String COLUMN_VOICE_URL = "voiceUrl";
    private static String DB_NAME;
    private static int DB_VERSION = 0;
    public static final int FRIEND_TYPE_ALL = -1;
    public static final int FRIEND_TYPE_FANS = 1;
    public static final int FRIEND_TYPE_FOLLOWER = 2;
    public static final int FRIEND_TYPE_ME = 0;
    public static final int FRIEND_TYPE_NEARBY = 4;
    public static final int FRIEND_TYPE_VISITOR = 3;
    public static String TABLE_CLASS_APP;
    public static String TABLE_FRIEND;
    public static String TABLE_LOCAL_THEME;
    public static String TABLE_MESSAGE;

    static {
        MeetingDBOpenHelper.DB_VERSION = 10;
        MeetingDBOpenHelper.DB_NAME = "meetingDB";
        MeetingDBOpenHelper.TABLE_CLASS_APP = "classapplication";
        MeetingDBOpenHelper.TABLE_LOCAL_THEME = "local_theme";
        MeetingDBOpenHelper.TABLE_MESSAGE = "message";
        MeetingDBOpenHelper.TABLE_FRIEND = "myfriend";
    }

    public MeetingDBOpenHelper(final Context context) {
        super(context, MeetingDBOpenHelper.DB_NAME, (SQLiteDatabase.CursorFactory)null, MeetingDBOpenHelper.DB_VERSION);
    }

    public void checkAndInitialize(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_CLASS_APP + "(" + "dbID" + " integer primary key autoincrement, " + "aid" + " integer , " + "appName" + " varchar, " + "packageName" + " varchar, " + "appStatus" + " varchar, " + "downloadUrl" + " varchar, " + "iconUrl" + " varchar, " + "category" + " integer, " + "favourite" + " integer, " + "summaryUrl" + " varchar" + ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_LOCAL_THEME + "(" + "id" + " integer primary key autoincrement, " + "themeID" + " integer, " + "themeName" + " varchar(20), " + "packageName" + " varchar(128), " + "apkSize" + " varchar(10), " + "info" + " varchar(50), " + "imagePath" + " varchar(128)" + ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_MESSAGE + "(" + "id" + " integer primary key autoincrement, " + "fromUserId" + " integer, " + "toUserId" + " integer, " + "userName" + " varchar, " + "avatarUrl" + " varchar, " + "content" + " varchar, " + "pictureUrl" + " varchar, " + "smsId" + " integer, " + "voiceUrl" + " varchar, " + "time" + " integer, " + "isRead" + " integer, " + "type" + " varchar" + ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_FRIEND + "(" + "id" + " integer primary key autoincrement, " + "userId" + " integer, " + "sex" + " integer, " + "account" + " varchar, " + "userName" + " varchar, " + "remark" + " varchar, " + "avatarUrl" + " varchar, " + "point" + " integer, " + "coin" + " integer, " + "latitude" + " varchar, " + "longitude" + " varchar, " + "direction" + " varchar, " + "distance" + " integer, " + "online" + " integer, " + "weibo_type" + " integer, " + "weibo_token" + " varchar, " + "weibo_tokenSecret" + " varchar, " + "weibo_accountName" + " varchar, " + "fansCount" + " integer, " + "visitorCount" + " integer, " + "followCount" + " integer, " + "messageCount" + " integer, " + "isFollow" + " integer, " + "display" + " integer, " + "friend_type" + " integer" + ")");
    }

    public void close() {
        synchronized (this) {
            super.close();
        }
    }

    public void onCreate(final SQLiteDatabase sqLiteDatabase) {
        this.checkAndInitialize(sqLiteDatabase);
    }

    public void onOpen(final SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_CLASS_APP + "(" + "dbID" + " integer primary key autoincrement, " + "aid" + " integer , " + "appName" + " varchar, " + "packageName" + " varchar, " + "appStatus" + " varchar, " + "downloadUrl" + " varchar, " + "iconUrl" + " varchar, " + "category" + " integer, " + "favourite" + " integer" + "summaryUrl" + " varchar" + ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_LOCAL_THEME + "(" + "id" + " integer primary key autoincrement, " + "themeID" + " integer, " + "themeName" + " varchar(20), " + "packageName" + " varchar(128), " + "apkSize" + " varchar(10), " + "info" + " varchar(50), " + "imagePath" + " varchar(128)" + ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_MESSAGE + "(" + "id" + " integer primary key autoincrement, " + "fromUserId" + " integer, " + "toUserId" + " integer, " + "userName" + " varchar, " + "avatarUrl" + " varchar, " + "content" + " varchar, " + "pictureUrl" + " varchar, " + "voiceUrl" + " varchar, " + "time" + " integer, " + "type" + " varchar" + ")");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + MeetingDBOpenHelper.TABLE_FRIEND + "(" + "id" + " integer primary key autoincrement, " + "userId" + " integer, " + "sex" + " integer, " + "account" + " varchar, " + "userName" + " varchar, " + "remark" + " varchar, " + "avatarUrl" + " varchar, " + "point" + " integer, " + "coin" + " integer, " + "latitude" + " varchar, " + "longitude" + " varchar, " + "direction" + " varchar, " + "distance" + " integer, " + "online" + " integer, " + "weibo_type" + " integer, " + "weibo_token" + " varchar, " + "weibo_tokenSecret" + " varchar, " + "weibo_accountName" + " varchar, " + "fansCount" + " integer, " + "visitorCount" + " integer, " + "followCount" + " integer, " + "messageCount" + " integer, " + "isFollow" + " integer, " + "display" + " integer, " + "friend_type" + " integer" + ")");
    }

    public void onUpgrade(final SQLiteDatabase sqLiteDatabase, final int n, final int n2) {
    }
}
