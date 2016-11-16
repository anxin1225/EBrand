/**
 * Created by sam on 2016/11/10.
 */
package com.sam.ebrand.engine;

import android.content.Intent;

import com.sam.ebrand.application.shortcutService;
import com.sam.ebrand.param.MeetingParam;

public class LCDEngine {
    public static boolean bIsVGAInput;
    public static boolean backlightStatus;

    static {
        while (true) {
            try {
                System.loadLibrary("SubLCD");
                LCDEngine.backlightStatus = true;
                LCDEngine.bIsVGAInput = false;
            }
            catch (Exception ex) {
                Log.e("JNI", new StringBuilder().append(ex.getMessage()).toString());
                continue;
            }
            break;
        }
    }

    public static int backlight(final int n) {
        if (n == 1) {
            BackLight.On();
        }
        else {
            BackLight.Off();
        }
        return 0;
    }

    public static void customBacklightStatus() {
        UserBitmapGenerate.getInstance().setNameFontAndColor("", "", "", "", "", "", 0, 0, 0, 0, 0, 0, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, false, false, false);
        SocketManager.mStaticHandler.obtainMessage(19).sendToTarget();
    }

    public static int refreshSubLcd(final String s) {
        final Intent intent = new Intent();
        intent.setAction("com.Meeting.NameShow.ACTION_SUBLCD_PIC_ADDR");
        intent.putExtra("pic_path", MeetingParam.SDCARD_WELCOME_PICTURE);
        if (SocketManager.mcontext != null) {
            SocketManager.mcontext.sendBroadcast(intent);
        }
        else if (shortcutService.getInstance() != null) {
            shortcutService.getInstance().sendBroadcast(intent);
        }
        return 0;
    }

    public static int saveRefreshSubLcd(final String s) {
        synchronized (LCDEngine.class) {
            return refreshSubLcd(s);
        }
    }

}
