package com.sam.ebrand.manage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by sam on 2016/11/10.
 */
public class SettingManager {
    public static final String ACCOUNT = "account";
    public static final String ADDRESS = "address";
    public static final String AUTO_HINT_NEW_GAME = "auto_hint_new_game";
    public static final String AUTO_UPDATE_GAME_WHEN_WIFI_OPEN = "auto_update_game_when_wifi_open";
    public static final String BACKGROUND_COLOR = "BACKGROUNDCOLOR";
    public static final String BCUSTOMSET = "bCustomSetName";
    public static final String BOARDBACKGROUND = "boardbackground";
    public static final String COMMENT_LINE_SIZE = "COMMENT_LINE_SIZE";
    public static final String CUSTOMCOMPANYCOLOR = "customCompanyColor";
    public static final String CUSTOMCOMPANYFONT = "customCompanyFont";
    public static final String CUSTOMCOMPANYNAME = "customCompanyName";
    public static final String CUSTOMCOMPANYSIZE = "customCompanySize";
    public static final String CUSTOMJOBCOLOR = "customJobColor";
    public static final String CUSTOMJOBFONT = "customJobFont";
    public static final String CUSTOMJOBNAME = "customJobName";
    public static final String CUSTOMJOBSIZE = "customJobSize";
    public static final String CUSTOMNAME = "CustomUserName";
    public static final String CUSTOMNAMECOLOR = "CustomUserNameColor";
    public static final String CUSTOMNAMEFONT = "CustomUserNameFont";
    public static final String CUSTOMNAMESIZE = "CustomUserNameSize";
    public static final String DISPLAY_POSITION = "display_position";
    public static final String FILEXPORTPERMISSION = "FILEXPORTPERMISSION";
    public static final String FISRT_RUN = "first_run";
    public static String FONTNAME;
    public static String FONTSIZE;
    public static final String IIL = "iil";
    public static final String ISSIGNSUCCESS = "issignsuccess";
    public static final String LASTUSERNAME = "lastusername";
    public static final String MEETING_TITLE = "meetingTitle";
    public static final String MGID = "mgID";
    public static final String MINGPAIBACKGROUND_NEWVERSION = "mingpai_newversion";
    public static final String NOCUSTOMNAME = "NOCustomUserName";
    public static final String NOCUSTOMNAMECOLOR = "NOCustomUserNameColor";
    public static final String NOCUSTOMNAMEFONT = "NOCustomUserNameFont";
    public static final String NOCUSTOMNAMESIZE = "NOCustomUserNameSize";
    public static final String PASSWORD = "password";
    public static String SOCKETID;
    public static final String THEME_PACKAGE_NAME = "theme_package_name";
    public static final String TIMEMARK = "timemark";
    public static final String USERID = "userid";
    public static final String USERJOBS = "UserJobs";
    public static final String USERNAME = "USERNAME";
    public static final String USERNAMECOLOR = "usernamecolor";
    public static String UserName;
    public static final String VGA_INPORT = "VGA_INPORT";
    public static final String VGA_OUTPORT = "VGA_OUTPORT";
    public static final String WELCOMEWORD = "WelcomeWrod";
    public static final String bPURECOLOR = "bPURECOLOR";
    private static SettingManager instance;
    private SharedPreferences.Editor mEditor;
    private SettingChangeListener mSettingChangeListener;
    private SharedPreferences settings;

    static {
        SettingManager.FONTSIZE = "fontsize";
        SettingManager.FONTNAME = "fontname";
        SettingManager.SOCKETID = "socketid";
        SettingManager.UserName = "";
    }

    public SettingManager() {
        this.settings = null;
        this.mSettingChangeListener = null;
    }

    public static SettingManager getInstance() {
        if (SettingManager.instance == null) {
            SettingManager.instance = new SettingManager();
        }
        return SettingManager.instance;
    }

    public void beginWrite() {
        this.mEditor = this.settings.edit();
    }

    public void deleteSetting(final String s) {
        final SharedPreferences.Editor edit = this.settings.edit();
        edit.remove(s);
        edit.commit();
    }

    public void endWrite() {
        this.mEditor.apply();
        this.mEditor = null;
    }

    public SharedPreferences.Editor getEditor() {
        return this.settings.edit();
    }

    public SharedPreferences getSharedPrefrences() {
        return this.settings;
    }

    public void initialize(final Context context) {
        this.settings = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isInitialized() {
        return this.settings != null;
    }

    public Object readSetting(final String s, Object value, final Object o) {
        if (value instanceof Boolean) {
            value = this.settings.getBoolean(s, (boolean)(Boolean)o);
        }
        else {
            if (value instanceof Integer) {
                return this.settings.getInt(s, (int)(Integer)o);
            }
            if (value instanceof Float) {
                return this.settings.getFloat(s, (float)(Float)o);
            }
            if (value instanceof Long) {
                return this.settings.getLong(s, (long)(Long)o);
            }
            if (value instanceof String) {
                return this.settings.getString(s, (String)o);
            }
        }
        return value;
    }

    public void setChangeListener(final SettingChangeListener mSettingChangeListener) {
        this.mSettingChangeListener = mSettingChangeListener;
    }

    public void write(final String s, final Object o) {
        if (this.mEditor == null) {
            this.beginWrite();
        }
        if (o instanceof Boolean) {
            this.mEditor.putBoolean(s, (Boolean)o);
        }
        else if (o instanceof Integer) {
            this.mEditor.putInt(s, (Integer) o);
        }
        else if (o instanceof Float) {
            this.mEditor.putFloat(s, (Float) o);
        }
        else if (o instanceof Long) {
            this.mEditor.putLong(s, (Long)o);
        }
        else if (o instanceof String) {
            this.mEditor.putString(s, (String)o);
        }
        if (this.mSettingChangeListener != null) {
            this.mSettingChangeListener.onSettingChange(s, o);
        }
        if (s == "USERNAME") {
            SettingManager.UserName = (String)o;
        }
    }

    public void writeSetting(final String s, final Object o) {
        final SharedPreferences.Editor edit = this.settings.edit();
        if (o instanceof Boolean) {
            edit.putBoolean(s, (Boolean)o);
        }
        else if (o instanceof Integer) {
            edit.putInt(s, (Integer) o);
        }
        else if (o instanceof Float) {
            edit.putFloat(s, (Float)o);
        }
        else if (o instanceof Long) {
            edit.putLong(s, (Long)o);
        }
        else if (o instanceof String) {
            edit.putString(s, (String)o);
        }
        if (this.mSettingChangeListener != null) {
            this.mSettingChangeListener.onSettingChange(s, o);
        }
        if (s == "USERNAME") {
            SettingManager.UserName = (String)o;
        }
        edit.apply();
    }
}
