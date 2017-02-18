package com.sam.ebrand.manage;

import android.content.Context;
import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.sam.ebrand.R;
import com.sam.ebrand.engine.LCDEngine;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;

import java.security.MessageDigest;
import java.util.LinkedList;

/**
 * Created by sam on 2016/11/10.
 */
public class SubLcdManager extends Thread{

    private static SubLcdManager instnce;
    public static byte[] moperatorLock;
    private Context mContext;
    private byte[] mLock;
    private LinkedList<SubLcdMsg> mMsgQueue;
    public boolean mbStart;

    static {
        SubLcdManager.moperatorLock = new byte[0];
        SubLcdManager.instnce = new SubLcdManager();
    }

    private SubLcdManager() {
        this.mLock = new byte[0];
        (this.mMsgQueue = new LinkedList<SubLcdMsg>()).clear();
    }

    public static SubLcdManager getInstance() {
        return SubLcdManager.instnce;
    }

    public void OperateSubLcd(final SubLcdMsg subLcdMsg) {
        synchronized (this) {
           Log.i("SubLcd", "刷副屏请求！");
            this.mMsgQueue.add(subLcdMsg);
            synchronized (this.mLock) {
                this.mLock.notify();
            }
        }
    }
    @Override
    public void run() {

        if(Environment.getExternalStorageState() == "mounted") {
            SocketManager.mStaticHandler.obtainMessage(20).sendToTarget();
            FileUtils.creatDir(MeetingParam.SDCARD_ROOT);
            FileUtils.creatDir(MeetingParam.SDCARD_WHITEBOARD);
            FileUtils.creatDir(MeetingParam.SDCARD_DOC);
            FileUtils.creatDir(MeetingParam.SDCARD_COMMENT);
            FileUtils.creatDir(MeetingParam.SDCARD_UDISK_DOC);
            FileUtils.creatDir(MeetingParam.SDCARD_UDISK_RESTAURANT);
            FileUtils.creatDir(MeetingParam.SDCARD_UDISK_SCHEDULE);
            FileUtils.creatDir(MeetingParam.SDCARD_SXPZ);
            FileUtils.creatDir(MeetingParam.SDCARD_BACKGROUND_PIC_FOLDER);
        }

        while(mbStart){

            try{
                Thread.sleep(1000);

//                MessageDigest md = MessageDigest.getInstance("MD5");

                synchronized (mLock){
                    if(mMsgQueue.size()>0)
                    {
                        SubLcdMsg msg = mMsgQueue.poll();
                        if(msg == null)
                            continue;
                        if(LCDEngine.backlightStatus != (msg.bBacklightOn == 1))
                        {
                            LCDEngine.backlight(msg.bBacklightOn);
                            LCDEngine.backlightStatus = msg.bBacklightOn == 1;

                            if(msg.bBacklightOn == 1){
                                Message message = Message.obtain();
                                message.what = 19;
                                SocketManager.mStaticHandler.sendMessage(message);
                            }
                        }
                        if(msg.bDefault == 1){
                            FileUtils.PngToBmp(SubLcdManager.getInstance().mContext, R.drawable.defalt_lcd,MeetingParam.SDCARD_WELCOME_PICTURE);
                        }
                        else{
                        }

                        if(msg.bNeedToToast){
                            SocketManager.mStaticHandler.obtainMessage(1,"设置铭牌成功");
                        }
                    }
                    else
                        mLock.wait();
                }

            }
            catch (Exception e){

            }
        }
    }

    public void setContext(final Context mContext) {
        this.mContext = mContext;
    }
}
