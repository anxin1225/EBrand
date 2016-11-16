package com.sam.ebrand.engine;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by sam on 2016/11/10.
 */
public class BackLight {
    private static final String[] CMD_OFF;
    private static final String[] CMD_ON;
    private static final String[] POWER_OFF;

    static {
        CMD_OFF = new String[] { "echo 1 > /sys/class/backlight/rk28_bl/bl_power_extra_lcd", "exit" };
        CMD_ON = new String[] { "echo 0 > /sys/class/backlight/rk28_bl/bl_power_extra_lcd", "exit" };
        POWER_OFF = new String[] { "reboot -p", "exit" };
    }

    public static void Off() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(final Void... array) {
                Run(BackLight.CMD_OFF);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void On() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(final Void... array) {
                Run(BackLight.CMD_ON);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public static void PowerOff() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(final Void... array) {
                Run(BackLight.POWER_OFF);
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private static void Run(final String[] array) {
        final String[] array2 = { "su" };
        final StringBuffer sb = new StringBuffer();
        while (true) {
            try {
                final ProcessBuilder processBuilder = new ProcessBuilder(array2);
                processBuilder.redirectErrorStream(true);
                final Process start = processBuilder.start();
                final DataOutputStream dataOutputStream = new DataOutputStream(start.getOutputStream());
                int i = 0;
                try {
                    while (i < array.length) {
                        dataOutputStream.writeBytes(String.valueOf(array[i]) + "\n");
                        ++i;
                    }
                    final InputStream inputStream = start.getInputStream();
                    final byte[] array3 = new byte[1024];
                    Arrays.fill(array3, (byte)0);
                    while (inputStream.read(array3) > 0) {
                        sb.append(new String(array3, "GBK"));
                        Arrays.fill(array3, (byte)0);
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    start.destroy();
                    dataOutputStream.close();
                    return;
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                    Log.e("exception", ex2.toString());
                }
            }
            catch (IOException ex) {
                continue;
            }
            break;
        }
    }
}
