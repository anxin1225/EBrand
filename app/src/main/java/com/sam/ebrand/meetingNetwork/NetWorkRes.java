package com.sam.ebrand.meetingNetwork;

import android.os.StrictMode;

/**
 * Created by sam on 2016/11/15.
 */
public class NetWorkRes {
    public static void NetWork() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
    }
}
