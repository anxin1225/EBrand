package com.sam.ebrand.param;

/**
 * Created by sam on 2016/11/10.
 */

import android.os.Environment;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.beans.PeopleBean;

import java.util.List;


public class MeetingParam {

    public static final String AUTOImportPath = "/mnt/usb_storage/\u5bfc\u5165\u8d44\u6599";
    public static final int BROWSE_MODE_SERVER = 1;
    public static final int BROWSE_MODE_UDISK = 2;
    public static final int CMD_BASIC_INFO_UPDTE = 12;
    public static final int CMD_CALL_SERVICE_IN_PROCESS = 6;
    public static final int CMD_COMMUNICATE_MESSAGE = 38;
    public static final int CMD_CONTROL_ALL = 19;
    public static final int CMD_DELETE_DATA = 16;
    public static final int CMD_DEMO = 11;
    public static final int CMD_DEMO_SCALE = 17;
    public static final int CMD_END_DEMO = 10;
    public static final int CMD_FILELIST_UPDATE = 40;
    public static final int CMD_HEADER_LEN = 12;
    public static final int CMD_HELLO = 1;
    public static final int CMD_INVALID = -1;
    public static final int CMD_LINK_OFFLINE = 8;
    public static final int CMD_LINK_ONLINE = 7;
    public static final int CMD_MEETING_CHANGE = 34;
    public static final int CMD_MEETING_TITLE_UPDTE = 13;
    public static final int CMD_MINGPAI_FAIL = 30;
    public static final int CMD_MINGPAI_LOGIN = 31;
    public static final int CMD_MINGPAI_OK = 29;
    public static final int CMD_MINPAI_LAYOUT = 32;
    public static final int CMD_MINPAI_UPDATA = 33;
    public static final int CMD_MPID_UPDATE = 15;
    public static final int CMD_NEW_MESSAGE = 3;
    public static final int CMD_NOTICE = 14;
    public static final int CMD_PAYLOAD_LEN = 51200;
    public static final int CMD_SET_BRIGHTNESS = 2;
    public static final int CMD_START_DEMO = 9;
    public static final int CMD_VGA_CLOSE = 37;
    public static final int CMD_VGA_IN = 36;
    public static final int CMD_VGA_IN_FORCE = 43;
    public static final int CMD_VGA_IN_UNFORCE = 44;
    public static final int CMD_VGA_OUT = 35;
    public static final int CMD_VGA_OUT_CLUE = 45;
    public static final int CMD_VIDEO_RECV = 4;
    public static final int CMD_VOTE_CLOSE = 42;
    public static final int CMD_VOTE_OPEN = 41;
    public static final int CMD_WHITEBOARD = 5;
    public static final String ExtsdPath = "/mnt/extsd";
    public static final int HEARTBEAT_TIMER_DELAY = 4000;
    public static final int HTTP_CONNECTION_TIMEOUNT = 10000;
    public static final int HTTP_DEFAULT_MSG_TIMEOUT = 60000;
    public static final int HTTP_DEFAULT_TIMEOUT = 30000;
    public static final String JAR_VERSION = "1.1";
    public static String MGID;
    public static String PeopleId;
    public static String PeopleName;
    public static final int RESP_PAYLOAD_LEN = 51200;
    public static final String SDCARD_BACKGROUND_PICTURE;
    public static final String SDCARD_BACKGROUND_PIC_FOLDER;
    public static final String SDCARD_COMMENT;
    public static final String SDCARD_DOC;
    public static final String SDCARD_DOWNLOAD = "/mnt/usb_storage/Meeting";
    public static final String SDCARD_ROOT;
    public static final String SDCARD_SPEECHDOC;
    public static final String SDCARD_SXPZ;
    public static final String SDCARD_UDISK_DOC;
    public static final String SDCARD_UDISK_RESTAURANT;
    public static final String SDCARD_UDISK_SCHEDULE;
    public static final String SDCARD_WELCOME_PICTURE;
    public static final String SDCARD_WHITEBOARD;
    public static final int SERVER = 2;
    public static final String SOCKET_IP = "192.168.1.103";
    public static final int SOCKET_PORT = 4567;
    public static final int SOCKET_TIMEOUT = 10000;
    public static final int SYNC_SOCKET_PORT = 4003;
    public static final int TIMEOUT_TIMER_DELAY = 5;
    public static final String UDiskPath = "/mnt/usb_storage";
    public static final String USERXLSPATH = "/mnt/usb_storage/导入资料/人员资料.xls";
    public static boolean bCameraExists = false;
    public static boolean bScaleSublcd = false;
    public static final int defaultPostTime = 5;
    public static List<PeopleBean> mdraftBean;
    public static String[] mstr;
    public static String voteId;

    static {
        SDCARD_ROOT = Environment.getExternalStorageDirectory() + "/Meeting/";
        SDCARD_WHITEBOARD = Environment.getExternalStorageDirectory() + "/Meeting/whiteboard/";
        SDCARD_UDISK_SCHEDULE = Environment.getExternalStorageDirectory() + "/Meeting/udiskschedule/";
        SDCARD_UDISK_RESTAURANT = Environment.getExternalStorageDirectory() + "/Meeting/udiskrestaurant/";
        SDCARD_SPEECHDOC = Environment.getExternalStorageDirectory() + "/Meeting/speechdoc/";
        SDCARD_DOC = Environment.getExternalStorageDirectory() + "/Meeting/doc/";
        SDCARD_UDISK_DOC = Environment.getExternalStorageDirectory() + "/Meeting/udiskdoc/";
        SDCARD_SXPZ = Environment.getExternalStorageDirectory() + "/Meeting/sxpz/";
        SDCARD_COMMENT = Environment.getExternalStorageDirectory() + "/Meeting/comment/";
        SDCARD_WELCOME_PICTURE = Environment.getExternalStorageDirectory() + "/Meeting/welcome.bmp";
        SDCARD_BACKGROUND_PICTURE = String.valueOf(MeetingParam.SDCARD_ROOT) + "/mpbackground/background.png";
        SDCARD_BACKGROUND_PIC_FOLDER = String.valueOf(MeetingParam.SDCARD_ROOT) + "/mpbackground/";
        MeetingParam.mstr = new String[0];
        MeetingParam.voteId = "";
        MeetingParam.MGID = "";
        MeetingParam.PeopleName = "";
        MeetingParam.PeopleId = "";
        MeetingParam.bScaleSublcd = false;
        MeetingParam.bCameraExists = true;
    }

    public static String getSocketIP() {
        return (String) SettingManager.getInstance().readSetting("serverip", "192.168.1.138", "192.168.1.103");
    }

    public static void setSocketIP(final String s) {
        SettingManager.getInstance().writeSetting("serverip", s);
    }
}
