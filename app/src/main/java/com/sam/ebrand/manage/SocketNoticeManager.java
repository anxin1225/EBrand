package com.sam.ebrand.manage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sam.ebrand.application.shortcutService;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by sam on 2016/11/15.
 */
public class SocketNoticeManager {

    public static int SocketId = 0;
    private static final String TAG = "SocketNoticeManager";
    static String demoContent;
    public static boolean ifDeleteAll;
    public static Context mContext;
    static int mdemoIndex;
    public static Activity myActivity;

    static {
        SocketNoticeManager.ifDeleteAll = false;
    }

    private static Bitmap downloadWelcomePictureFromUrl(final String s) {
        Bitmap decodeByteArray = null;
        Log.e("background", "downloading background:" + s);
        try {
            final URL url = new URL(s);
            try {
                final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                final InputStream inputStream = httpURLConnection.getInputStream();
                final int contentLength = httpURLConnection.getContentLength();
                decodeByteArray = null;
                if (contentLength != -1) {
                    final byte[] array = new byte[contentLength];
                    final byte[] array2 = new byte[512];
                    int n = 0;
                    while (true) {
                        final int read = inputStream.read(array2);
                        if (read <= 0) {
                            break;
                        }
                        System.arraycopy(array2, 0, array, n, read);
                        n += read;
                    }
                    decodeByteArray = BitmapFactory.decodeByteArray(array, 0, array.length);
                    final File file = new File(MeetingParam.SDCARD_BACKGROUND_PICTURE);
                    if (file.exists()) {
                        file.delete();
                    }
                    else {
                        file.getParentFile().mkdirs();
                    }
                    FileUtils.writeBytesByPath(MeetingParam.SDCARD_BACKGROUND_PICTURE, array);
                    return decodeByteArray;
                }
                return null;
            }
            catch (MalformedURLException ex) {
                return decodeByteArray;
            }
            catch (IOException ex2) {}
        }
        catch (IOException ex3) {}
        catch (MalformedURLException ex4) {
            return null;
        }
        return null;
    }

    public static void getBackgroundpicture() {
        HttpProtocalManager.getInstance().getMingpaiBackground(new GetMingpaiBackgroundTask.OnResultListener() {
            @Override
            public void CancelListener() {
            }

            @Override
            public void ResultListener(final int n, final String s, final String s2, final String s3) {
                final String string = SettingManager.getInstance().readSetting("mingpai_newversion", "", "").toString();
                while (true) {
                    Label_0138: {
                        if (s2.equals("")) {
                            break Label_0138;
                        }
                        Block_5: {
                            Block_3: {
                                String s4;
                                while (true) {
                                    Label_0163: {
                                        while (true) {
                                            try {
                                                final String substring = s2.substring(1 + s2.lastIndexOf("/"), s2.lastIndexOf("."));
                                                s4 = substring;
                                                if (s2.equals("") || string.equals(s4)) {
                                                    break Label_0163;
                                                }
                                                Log.e("MP", "\u53d1\u73b0\u65b0\u56fe\u7247\uff1a" + s4 + "," + string + ",url:" + s2);
                                                if (s4.equals("")) {
                                                    SocketNoticeManager.getMingpaiMassage();
                                                    return;
                                                }
                                                break;
                                            }
                                            catch (Exception ex) {
                                                final String substring = "";
                                                continue;
                                            }
                                            break;
                                        }
                                        break Label_0138;
                                    }
                                    final File file = new File(MeetingParam.SDCARD_WELCOME_PICTURE);
                                    Log.e("MP", "same version");
                                    if (file.exists() && file.length() > 0L) {
                                        break Block_3;
                                    }
                                    while (true) {
                                        try {
                                            final String substring2 = s2.substring(1 + s2.lastIndexOf("/"), s2.lastIndexOf("."));
                                            if (substring2.equals("")) {
                                                SocketNoticeManager.getMingpaiMassage();
                                                return;
                                            }
                                        }
                                        catch (Exception ex2) {
                                            final String substring2 = "";
                                            continue;
                                        }
                                        break;
                                    }
                                    if (!s2.equals("")) {
                                        break Block_5;
                                    }
                                    return;
                                }
                                ThreadPool.getInstance().Execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        while (!Environment.getExternalStorageState().equals("mounted")) {
                                            try {
                                                Thread.sleep(1000L);
                                            }
                                            catch (InterruptedException ex) {
                                                ex.printStackTrace();
                                            }
                                        }
                                        if (ThreadPool.getInstance().isDownloading(s2)) {
                                            return;
                                        }
                                        ThreadPool.getInstance().insert(s2, s2);
                                        if (downloadWelcomePictureFromUrl(s2) != null) {
                                            SettingManager.getInstance().writeSetting("mingpai_newversion", s4);
                                        }
                                        SocketManager.mStaticHandler.obtainMessage(4).sendToTarget();
                                        ThreadPool.getInstance().remove(s2);
                                    }
                                });
                                return;
                            }
                            SocketNoticeManager.getMingpaiMassage();
                            return;
                        }
                        ThreadPool.getInstance().Execute(new Runnable() {
                            @Override
                            public void run() {
                                while (!Environment.getExternalStorageState().equals("mounted")) {
                                    try {
                                        Thread.sleep(1000L);
                                    }
                                    catch (InterruptedException ex) {
                                        ex.printStackTrace();
                                    }
                                }
                                if (ThreadPool.getInstance().isDownloading(s2)) {
                                    return;
                                }
                                ThreadPool.getInstance().insert(s2, s2);
                                downloadWelcomePictureFromUrl(s2);
                                SocketManager.mStaticHandler.obtainMessage(4).sendToTarget();
                                ThreadPool.getInstance().remove(s2);
                            }
                        });
                        return;
                    }
                    String s4 = "";
                    continue;
                }
            }
        });
    }

    public static void getMingpaiMassage() {
        HttpProtocalManager.getInstance().GetMingpaiInfo(new GetMingpaiInfoTask.OnResultListener() {
            @Override
            public void CancelListener() {
            }

            @Override
            public void ResultListener(final int n, final String s, final List<MingpaiBackgroundBean> list) {
                if (n == 0 && list != null) {
                    final String username = list.get(0).getUsername();
                    SettingManager.getInstance().writeSetting("USERNAME", username);
                    final String rank = list.get(0).getRank();
                    final String company = list.get(0).getCompany();
                    final String usernamefont = list.get(0).getUsernamefont();
                    final String rankfont = list.get(0).getRankfont();
                    final String companyfont = list.get(0).getCompanyfont();
                    final int color = Color.parseColor(list.get(0).getUsernamecolor());
                    final int color2 = Color.parseColor(list.get(0).getRankcolor());
                    final int color3 = Color.parseColor(list.get(0).getCompanycolor());
                    final int int1 = Integer.parseInt(list.get(0).getUsernamefontsize());
                    final int int2 = Integer.parseInt(list.get(0).getRankfontsize());
                    final int int3 = Integer.parseInt(list.get(0).getCompanyfontsize());
                    final float usernameleft = list.get(0).getUsernameleft();
                    final float usernametop = list.get(0).getUsernametop();
                    final float rankleft = list.get(0).getRankleft();
                    final float ranktop = list.get(0).getRanktop();
                    final float companyleft = list.get(0).getCompanyleft();
                    final float companytop = list.get(0).getCompanytop();
                    final float usernameheight = list.get(0).getUsernameheight();
                    final float rankheight = list.get(0).getRankheight();
                    final float companyheight = list.get(0).getCompanyheight();
                    final float usernamewidth = list.get(0).getUsernamewidth();
                    final float rankwidth = list.get(0).getRankwidth();
                    final float companywidth = list.get(0).getCompanywidth();
                    if (list.get(0).isPureColor()) {
                        UserBitmapGenerate.getInstance().setBackgroundColor(Color.parseColor(list.get(0).getBackcolor()));
                    }
                    else {
                        UserBitmapGenerate.getInstance().setBackgroundBitmap();
                    }
                    UserBitmapGenerate.getInstance().setNameFontAndColor(username, rank, company, usernamefont, rankfont, companyfont, color, color2, color3, int1, int2, int3, usernameheight, rankheight, companyheight, usernamewidth, rankwidth, companywidth, usernameleft, usernametop, rankleft, ranktop, companyleft, companytop, false, false, false);
                }
            }
        });
    }

    public static void getNotice(final int n, final Object o, final Context mContext, final ImageButton imageButton) {
        final byte[] array = (byte[])o;
        SocketNoticeManager.mContext = mContext;
        SocketManager.getInstance().mTimeoutTimer = 0;
        SocketManager.getInstance().mOnline = true;
        if (n != 1) {
            Log.e("RealTimeMsg", "cmd:" + n);
        }
        switch (n) {
            case 19: {
                final String s = new String(array);
                if (s == null || s.equals("")) {
                    break;
                }
                final int int1 = Integer.parseInt(s);
                if (SocketNoticeManager.mContext == null) {
                    break;
                }
                if (int1 == 1) {
                    Backlight.PowerOff();
                    return;
                }
                if (int1 == 2) {
                    final Intent intent = new Intent(SocketNoticeManager.mContext, (Class)mainActivity.class);
                    intent.setFlags(67108864);
                    SocketNoticeManager.mContext.startActivity(intent);
                    return;
                }
                if (int1 == 3) {
                    final Intent intent2 = new Intent(SocketNoticeManager.mContext, (Class)WelcomeActivity.class);
                    intent2.setFlags(67108864);
                    SocketNoticeManager.mContext.startActivity(intent2);
                    return;
                }
                if (int1 == 4) {
                    SocketNoticeManager.mContext.startActivity(new Intent(SocketNoticeManager.mContext, (Class)SignActivity.class));
                    return;
                }
                if (int1 == 5) {
                    SocketNoticeManager.mContext.startActivity(new Intent(SocketNoticeManager.mContext, (Class)VoteActivity.class));
                    return;
                }
                if (int1 == 6) {
                    SocketNoticeManager.mContext.startActivity(new Intent(SocketNoticeManager.mContext, (Class)MeetinginfoActivity.class));
                    return;
                }
                if (int1 == 7) {
                    final Intent intent3 = new Intent(SocketNoticeManager.mContext, (Class)WelcomeActivity.class);
                    intent3.setFlags(67108864);
                    SocketNoticeManager.mContext.startActivity(intent3);
                    return;
                }
                break;
            }
            case 16: {
                DownloaderManager.getInstance().calcelAllDownloader();
                FileUtils.removeFile(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.removeFile(MeetingParam.SDCARD_DOC);
                FileUtils.removeFile(MeetingParam.SDCARD_COMMENT);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_DOC);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_RESTAURANT);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_SCHEDULE);
                FileUtils.creatDir(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.creatDir(MeetingParam.SDCARD_DOC);
                FileUtils.creatDir(MeetingParam.SDCARD_COMMENT);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_DOC);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_RESTAURANT);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_SCHEDULE);
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
                HttpProtocalManager.getInstance().GetUserInfo();
                getBackgroundpicture();
            }
            case 13: {
                SettingManager.getInstance().writeSetting("issignsuccess", "false");
                DownloaderManager.getInstance().calcelAllDownloader();
                FileUtils.removeFile(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.removeFile(MeetingParam.SDCARD_DOC);
                FileUtils.removeFile(MeetingParam.SDCARD_COMMENT);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_DOC);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_RESTAURANT);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_SCHEDULE);
                FileUtils.creatDir(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.creatDir(MeetingParam.SDCARD_DOC);
                FileUtils.creatDir(MeetingParam.SDCARD_COMMENT);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_DOC);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_RESTAURANT);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_SCHEDULE);
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
                if (!(SocketNoticeManager.mContext instanceof WelcomeActivity)) {
                    final Intent intent4 = new Intent(SocketNoticeManager.mContext, (Class)WelcomeActivity.class);
                    if (SocketNoticeManager.mContext instanceof Service) {
                        intent4.setFlags(268468224);
                    }
                    else {
                        intent4.setFlags(67108864);
                    }
                    SocketNoticeManager.mContext.startActivity(intent4);
                    return;
                }
                break;
            }
            case 29: {
                final String string = SettingManager.getInstance().readSetting("mgID", "", "").toString();
                ProgressDialogHint.Dismiss();
                if (string.equals("")) {
                    if (SocketNoticeManager.mContext != null) {
                        ToastHint.show(SocketNoticeManager.mContext, "\u94ed\u724cID\u6ce8\u518c\u6210\u529f!");
                    }
                }
                else if (SocketNoticeManager.mContext != null) {
                    ToastHint.show(SocketNoticeManager.mContext, "\u94ed\u724cID\u4e0a\u4f20\u6210\u529f!");
                }
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
                HttpProtocalManager.getInstance().GetUserInfo();
                getBackgroundpicture();
                if (string == null || string.equals("")) {
                    SettingManager.getInstance().writeSetting("mgID", WelcomeActivity.MGID);
                    MeetingParam.MGID = WelcomeActivity.MGID;
                    new SetIP().Request(WelcomeActivity.MGID);
                }
                else {
                    new SetIP().Request(string);
                }
                SocketNoticeManager.ifDeleteAll = false;
            }
            case 30: {
                final String string2 = SettingManager.getInstance().readSetting("mgID", "", "").toString();
                if (string2 != null && !string2.equals("")) {
                    new SetIP().Request(string2);
                }
                else if (SocketNoticeManager.mContext != null) {
                    ToastHint.show(SocketNoticeManager.mContext, "\u6ce8\u518c\u5931\u8d25\uff01");
                }
                WelcomeActivity.MGID = "";
                MeetingParam.MGID = "";
                ProgressDialogHint.Dismiss();
            }
            case 31: {
                final String s2 = new String(array);
                SettingManager.getInstance().writeSetting(SettingManager.SOCKETID, s2);
                if (s2 != null && !s2.equals("") && s2.length() > 0) {
                    new SetIP().Request(SettingManager.getInstance().readSetting("mgID", "", "").toString());
                    return;
                }
                break;
            }
            case 7: {
                final String s3 = new String(array);
                if (s3 != null && !s3.equals("") && s3.length() > 0) {
                    SettingManager.getInstance().writeSetting(SettingManager.SOCKETID, s3);
                }
                final String string3 = SettingManager.getInstance().readSetting("mgID", "", "").toString();
                if (string3 != null && !string3.equals("")) {
                    SocketManager.getInstance().LoginMgId(string3);
                    return;
                }
                break;
            }
            case 17: {
                if (array == null) {
                    return;
                }
                break;
            }
            case 12: {
                Log.e("SocketNoticeManager", "\u57fa\u672c\u8d44\u6599\u4fee\u6539");
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
                getBackgroundpicture();
                HttpProtocalManager.getInstance().GetUserInfo();
            }
            case 14: {
                final String s4 = SampleParam.NoticeStr = new String(array);
                final Intent intent5 = new Intent(mContext, (Class)NoticeActivity.class);
                if (SocketNoticeManager.mContext instanceof Service) {
                    intent5.setFlags(268468224);
                }
                intent5.putExtra("notice", s4);
                mContext.startActivity(intent5);
            }
            case 15: {
                final String mgid = new String(array);
                Log.e("SocketNoticeManager", "----------\u540e\u53f0\u4fee\u6539\u4e86\u94ed\u724cID\uff0c\u65b0ID\u4e3a\u3010 " + mgid + "\u3011----------");
                SettingManager.getInstance().writeSetting("mgID", mgid);
                MeetingParam.MGID = mgid;
            }
            case 32: {
                Log.e("SocketNoticeManager", "\u5e03\u5c40\u6539\u53d8");
                if (new String(array).startsWith("#")) {
                    getMingpaiMassage();
                    return;
                }
                getBackgroundpicture();
            }
            case 33: {
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
                getBackgroundpicture();
                final String s5 = new String(array);
                Log.e("SocketNoticeManager", "\u8d44\u6599\u66f4\u65b0:" + s5);
                SettingManager.getInstance().writeSetting("bCustomSetName", false);
                if (!isNum(s5)) {
                    HttpProtocalManager.getInstance().GetUserInfo();
                    return;
                }
                final Message obtain = Message.obtain();
                obtain.what = 17;
                SocketManager.mStaticHandler.sendMessageDelayed(obtain, 2000L);
            }
            case 34: {
                Log.e("SocketNoticeManager", "\u4f1a\u8bae\u5207\u6362");
                SettingManager.getInstance().writeSetting("meetingTitle", "");
                LCDEngine.customBacklightStatus();
                final boolean b = mContext instanceof mainActivity;
                LCDEngine.customBacklightStatus();
                getBackgroundpicture();
                DownloaderManager.getInstance().calcelAllDownloader();
                FileUtils.delAllFile(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.delAllFile(MeetingParam.SDCARD_DOC);
                FileUtils.delAllFile(MeetingParam.SDCARD_COMMENT);
                FileUtils.removeFile(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.removeFile(MeetingParam.SDCARD_DOC);
                FileUtils.removeFile(MeetingParam.SDCARD_COMMENT);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_DOC);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_RESTAURANT);
                FileUtils.removeFile(MeetingParam.SDCARD_UDISK_SCHEDULE);
                FileUtils.removeFile(MeetingParam.SDCARD_SXPZ);
                FileUtils.creatDir(MeetingParam.SDCARD_ROOT);
                FileUtils.creatDir(MeetingParam.SDCARD_WHITEBOARD);
                FileUtils.creatDir(MeetingParam.SDCARD_DOC);
                FileUtils.creatDir(MeetingParam.SDCARD_COMMENT);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_DOC);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_RESTAURANT);
                FileUtils.creatDir(MeetingParam.SDCARD_UDISK_SCHEDULE);
                FileUtils.creatDir(MeetingParam.SDCARD_SXPZ);
                SettingManager.getInstance().writeSetting("issignsuccess", "false");
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
                HttpProtocalManager.getInstance().GetUserInfo();
                if (!(SocketNoticeManager.mContext instanceof WelcomeActivity)) {
                    final Intent intent6 = new Intent(SocketNoticeManager.mContext, (Class)WelcomeActivity.class);
                    if (SocketNoticeManager.mContext instanceof Service) {
                        intent6.setFlags(268468224);
                    }
                    else {
                        intent6.setFlags(67108864);
                    }
                    SocketNoticeManager.mContext.startActivity(intent6);
                    return;
                }
                break;
            }
            case 41: {
                final String s6 = new String(array);
                Log.e("CMD", "cmd:41.." + s6);
                if (s6.contains("close")) {
                    if (SocketNoticeManager.mContext instanceof VoteActivity) {
                        ((VoteActivity)SocketNoticeManager.mContext).UpdateVote();
                        return;
                    }
                    break;
                }
                else {
                    if (SocketNoticeManager.mContext instanceof VoteActivity) {
                        ((VoteActivity)SocketNoticeManager.mContext).UpdateVote();
                        return;
                    }
                    final Intent intent7 = new Intent(SocketNoticeManager.mContext, (Class)VoteActivity.class);
                    if (SocketNoticeManager.mContext instanceof Service) {
                        intent7.setFlags(268468224);
                    }
                    SocketNoticeManager.mContext.startActivity(intent7);
                    return;
                }
                break;
            }
            case 42: {
                if (SocketNoticeManager.mContext instanceof VoteActivity) {
                    ((VoteActivity)SocketNoticeManager.mContext).UpdateVote();
                    return;
                }
                break;
            }
            case 3: {
                final UnReadMessageQueue.ShortMessage shortMessage = new UnReadMessageQueue.ShortMessage(true);
                shortMessage.mSender = "\u7ba1\u7406\u5458";
                shortMessage.mMsgContent = new String(array);
                if (ReceiveMessageDlg.mDialog != null) {
                    UnReadMessageQueue.getInstance().add(shortMessage);
                    return;
                }
                new ReceiveMessageDlg((Context)shortcutService.getInstance(), shortMessage).show();
            }
            case 38: {
                final String[] split = new String(array).split(",");
                final int length = split.length;
                final UnReadMessageQueue.ShortMessage shortMessage2 = new UnReadMessageQueue.ShortMessage();
                shortMessage2.mSender = split[0];
                for (int i = 1; i < length; ++i) {
                    if (!true) {
                        shortMessage2.mMsgContent = String.valueOf(shortMessage2.mMsgContent) + ",";
                    }
                    shortMessage2.mMsgContent = String.valueOf(shortMessage2.mMsgContent) + split[i];
                }
                if (ReceiveMessageDlg.mDialog != null) {
                    UnReadMessageQueue.getInstance().add(shortMessage2);
                    return;
                }
                new ReceiveMessageDlg((Context) shortcutService.getInstance(), shortMessage2).show();
            }
        }
    }

    public static boolean isNum(final String s) {
        return s.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }
}
