package com.sam.ebrand.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.SDFolderManager;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.manage.SubLcdManager;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.meetingNetwork.beans.MessageManager;
import com.sam.ebrand.meetingNetwork.http.app.SetIP;
import com.sam.ebrand.meetingNetwork.socket.database.MeetingDBManager;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sam on 2016/11/23.
 */

public class MeetingApplication extends Application
{
    public static String VERSION;
    private static MeetingApplication instance;
    private List<Activity> mList;
    public PowerManager.WakeLock mWakeLock;

    static {
        MeetingApplication.VERSION = "1.0";
    }

    public MeetingApplication() {
        this.mList = new LinkedList<Activity>();
    }

    public static MeetingApplication getInstance() {
        synchronized (MeetingApplication.class) {
            if (MeetingApplication.instance == null) {
                MeetingApplication.instance = new MeetingApplication();
            }
            return MeetingApplication.instance;
        }
    }

    public void addActivity(final Activity activity) {
        this.mList.add(activity);
    }

    public void exit() {
        try {
            for (final Activity activity : this.mList) {
                if (activity != null) {
                    activity.finish();
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            System.exit(0);
        }
    }

    public void onCreate() {
        super.onCreate();
        MeetingApplication.instance = this;
        SDFolderManager.getInstance();
        CrashHandler.getInstance().Init((Context)this);
        SettingManager.getInstance().initialize(this.getBaseContext());
        SubLcdManager.getInstance().setContext(this.getBaseContext());
        if (!SubLcdManager.getInstance().mbStart) {
            SubLcdManager.getInstance().start();
        }
        MeetingDBManager.getInstance().initialize(this.getBaseContext());
        MessageManager.getInstance();
        final String string = SettingManager.getInstance().readSetting("mgID", "", "").toString();
        if (!string.equals("")) {
            new SetIP().Request(string);
        }
        NetWorkRes.NetWork();
        SocketManager.getInstance();
        this.startService(new Intent((Context)this, (Class)shortcutService.class));
        (this.mWakeLock = ((PowerManager)this.getSystemService("power")).newWakeLock(6, "meeting")).acquire();
        BackgroundManager.getInstance().UpdateBackground((Context)this, SettingManager.getInstance().readSetting("iil", "", "").toString());
        SettingManager.UserName = (String)SettingManager.getInstance().readSetting("USERNAME", "", "");
        new InitSublcdThread().start();
    }

    public void onExit() {
        this.mWakeLock.release();
    }

    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public class InitSublcdThread extends Thread
    {
        @Override
        public void run() {
            super.run();
        }
    }
}
