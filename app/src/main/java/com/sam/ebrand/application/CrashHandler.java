package com.sam.ebrand.application;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sam on 2016/11/23.
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler
{
    public static final String TAG = "CrashHandler";
    private static CrashHandler instance;
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private DateFormat mFormatter;
    private Map<String, String> mInfos;

    static {
        CrashHandler.instance = new CrashHandler();
    }

    private CrashHandler() {
        this.mInfos = new HashMap<String, String>();
        this.mFormatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    }

    public static CrashHandler getInstance() {
        return CrashHandler.instance;
    }

    private boolean handleException(final Throwable t) {
        return false;
     /*   if (t == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(CrashHandler.this.mContext, (CharSequence)"很抱歉,程序出现异常,即将退出.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }.start();
        this.collectDeviceInfo(this.mContext);
        this.saveCrashInfo2File(t);
        return true;*/
    }

    private String saveCrashInfo2File(final Throwable t) {
        return null;
        /*
        final StringBuffer sb = new StringBuffer();
        final Iterator<Map.Entry<String, String>> iterator = this.mInfos.entrySet().iterator();
        Label_0064_Outer:
        while (true) {
            Label_0257: {
                if (iterator.hasNext()) {
                    break Label_0257;
                }
                final StringWriter stringWriter = new StringWriter();
                final PrintWriter printWriter = new PrintWriter(stringWriter);
                t.printStackTrace(printWriter);
                Throwable t2 = t.getCause();
                while (true) {
                    Label_0330: {
                        if (t2 != null) {
                            break Label_0330;
                        }
                        printWriter.close();
                        sb.append(stringWriter.toString());
                        try {
                            final String string = "crash-" + this.mFormatter.format(new Date()) + "-" + System.currentTimeMillis() + ".log";
                            if (Environment.getExternalStorageState().equals("mounted")) {
                                final String string2 = Environment.getExternalStorageDirectory() + "/crash/";
                                final File file = new File(string2);
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                final FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(string2) + string);
                                fileOutputStream.write(sb.toString().getBytes());
                                fileOutputStream.close();
                                Log.e("CrashHandler", sb.toString());
                            }
                            return string;
                            final Map.Entry<String, String> entry = iterator.next();
                            sb.append(String.valueOf(entry.getKey()) + "=" + entry.getValue() + "\n");
                            continue Label_0064_Outer;
                            t2.printStackTrace(printWriter);
                            t2 = t2.getCause();
                            continue;
                        }
                        catch (Exception ex) {
                            Log.e("CrashHandler", "an error occured while writing file...", (Throwable)ex);
                            return null;
                        }
                    }
                    break;
                }
            }
        }*/
    }

    public void Init(final Context mContext) {
        this.mContext = mContext;
        this.mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void collectDeviceInfo(final Context context) {
 /*       Label_0098_Outer:
        while (true) {
            Label_0206_Outer:
            while (true) {
                DateFormat.Field[] declaredFields;
                int n;
                while (true) {
                    try {
                        final PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 1);
                        if (packageInfo != null) {
                            String versionName;
                            if (packageInfo.versionName == null) {
                                versionName = "null";
                            }
                            else {
                                versionName = packageInfo.versionName;
                            }
                            final String string = new StringBuilder(String.valueOf(packageInfo.versionCode)).toString();
                            this.mInfos.put("versionName", versionName);
                            this.mInfos.put("versionCode", string);
                        }
                        declaredFields = Build.class.getDeclaredFields();
                        final int length = declaredFields.length;
                        n = 0;
                        if (n >= length) {
                            return;
                        }
                    }
                    catch (PackageManager$NameNotFoundException ex) {
                        Log.e("CrashHandler", "an error occured when collect package info", (Throwable)ex);
                        continue Label_0098_Outer;
                    }
                    break;
                }
                final DateFormat.Field field = declaredFields[n];
                while (true) {
                    try {
                        field.setAccessible(true);
                        this.mInfos.put(field.getName(), field.get(null).toString());
                        Log.d("CrashHandler", String.valueOf(field.getName()) + " : " + field.get(null));
                        ++n;
                        continue Label_0206_Outer;
                    }
                    catch (Exception ex2) {
                        Log.e("CrashHandler", "an error occured when collect crash info", (Throwable)ex2);
                        continue;
                    }
                    break;
                }
                break;
            }
        }
*/    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable t) {
        if (!this.handleException(t) && this.mDefaultHandler != null) {
            this.mDefaultHandler.uncaughtException(thread, t);
            return;
        }
            try {
                Thread.sleep(3000L);
               // Process.killProcess(Process.myPid());
                System.exit(1);
            }
            catch (InterruptedException ex) {
                Log.e("CrashHandler", "error : ", (Throwable)ex);
            }
    }
}
