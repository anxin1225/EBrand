package com.sam.ebrand.manage;

import android.os.Environment;
import android.util.Log;

import com.sam.ebrand.application.ThreadPool;
import com.sam.ebrand.meetingNetwork.beans.DraftBean;
import com.sam.ebrand.meetingNetwork.manage.HttpDownloader;
import com.sam.ebrand.param.MeetingParam;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by sam on 2016/11/21.
 */

public class MeetingFileManager
{
    public static void getAccommodation(final String s) {
        if (!s.equals("")) {
            final String[] split = s.split("/");
            ThreadPool.getInstance().Execute(new Runnable() {
                private final /* synthetic */ String val$path = split[-1 + split.length];

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
                    final File file = new File(String.valueOf(MeetingParam.SDCARD_DOC) + this.val$path);
                    final int n = (int)file.length();
                    if ((file.exists() && n != 0) || ThreadPool.getInstance().isDownloading(s)) {
                        return;
                    }
                    int n2 = 0;
                    final HttpDownloader httpDownloader = new HttpDownloader();
                    boolean b;
                    do {
                        b = true;
                        httpDownloader.chanceStatus(1);
                        ThreadPool.getInstance().insert(s, this.val$path);
                        final int downloadFile = httpDownloader.downloadFile(s, String.valueOf(MeetingParam.SDCARD_DOC) + this.val$path);
                        if (downloadFile != 1 && downloadFile != 2) {
                            b = false;
                        }
                        ThreadPool.getInstance().remove(s);
                    } while (++n2 <= 3 && !b);
                    httpDownloader.release();
                }
            });
        }
    }

    public static void getDraft(final List<DraftBean> list) {
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                final String docUrl = list.get(i).getDocUrl();
                final int n = i;
                final String[] split = docUrl.split("/");
                final String s = split[-1 + split.length];
                while (true) {
                    try {
                        final String decode = URLDecoder.decode(s, "utf-8");
                        final File file = new File(String.valueOf(MeetingParam.SDCARD_DOC) + decode);
                        final int n2 = (int)file.length();
                        System.out.println(n2);
                        if (!decode.contains("%") && (!file.exists() || n2 == 0)) {
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
                                    Log.e("newThread", "----------" + n + "----------");
                                    if (ThreadPool.getInstance().isDownloading(docUrl)) {
                                        return;
                                    }
                                    int n = 0;
                                    final HttpDownloader httpDownloader = new HttpDownloader();
                                    boolean b;
                                    do {
                                        b = true;
                                        httpDownloader.chanceStatus(1);
                                        ThreadPool.getInstance().insert(docUrl, decode);
                                        final int downloadFile = httpDownloader.downloadFile(docUrl, String.valueOf(MeetingParam.SDCARD_DOC) + decode);
                                        if (downloadFile != 1 && downloadFile != 2) {
                                            b = false;
                                        }
                                        ThreadPool.getInstance().remove(docUrl);
                                    } while (++n <= 3 && !b);
                                    httpDownloader.release();
                                }
                            });
                        }
                        ++i;
                    }
                    catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    break;
                }
            }
        }
    }

    public static void getMeetingSchedule(final String s) {
        if (!s.equals("")) {
            final String[] split = s.split("/");
            ThreadPool.getInstance().Execute(new Runnable() {
                private final /* synthetic */ String val$path = split[-1 + split.length];

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
                    final File file = new File(String.valueOf(MeetingParam.SDCARD_DOC) + this.val$path);
                    final int n = (int)file.length();
                    if ((file.exists() && n != 0) || ThreadPool.getInstance().isDownloading(s)) {
                        return;
                    }
                    int n2 = 0;
                    final HttpDownloader httpDownloader = new HttpDownloader();
                    boolean b;
                    do {
                        b = true;
                        httpDownloader.chanceStatus(1);
                        ThreadPool.getInstance().insert(s, this.val$path);
                        final int downloadFile = httpDownloader.downloadFile(s, String.valueOf(MeetingParam.SDCARD_DOC) + this.val$path);
                        if (downloadFile != 1 && downloadFile != 2) {
                            b = false;
                        }
                        ThreadPool.getInstance().remove(s);
                    } while (++n2 <= 3 && !b);
                    httpDownloader.release();
                }
            });
        }
    }
}
