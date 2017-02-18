package com.sam.ebrand.manage;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.board.boardSetting;
import com.sam.ebrand.application.meeting.MeetinginfoActivity;
import com.sam.ebrand.application.set.SetActivity;
import com.sam.ebrand.application.thread.CopyFileThread;
import com.sam.ebrand.meetingNetwork.beans.CmdDataBody;
import com.sam.ebrand.meetingNetwork.socket.SocketApi;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ProgressHint;
import com.sam.ebrand.widget.ToastHint;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by sam on 2016/11/15.
 */
public class SocketManager {

    private static final String TAG = "SocketManager";
    public static boolean ifMain;
    private static SocketManager mInstance;
    public static byte[] mRecvLock;
    private static int mRefCount;
    public static byte[] mSendLock;
    public static Handler mStaticHandler;
    private static Handler mTCPRecvHandler;
    private static Thread mTCPRecvThread;
    private static SendThread mTCPSendThread;
    private static SocketApi mVideoStreamTCPSocket;
    public static Context mcontext;
    public static Vector<Observer> observerActivitys;
    private static boolean running;
    public static boolean syncDemoControllerFlag;
    public static String syncDemoDocPath;
    public static boolean syncDemoLockFlag;
    private Timer mHeartbeatTimer;
    public boolean mOnline;
    public int mSyncTimeOut;
    public int mTimeoutTimer;

    public final static int TCPPort = 4567;

    static {
        SocketManager.mRecvLock = new byte[0];
        SocketManager.mSendLock = new byte[0];
        SocketManager.mInstance = null;
        SocketManager.mRefCount = 0;
        SocketManager.mStaticHandler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    case 1: {
                        if (SocketManager.mcontext == null) {
                            break;
                        }
                        ToastHint.show(SocketManager.mcontext, (String)message.obj);
                        if (SocketManager.mcontext instanceof SetActivity && ((String)message.obj).equals("设置铭牌成功")) {
                            ((SetActivity)SocketManager.mcontext).EnableConfirmBtn();
                            return;
                        }
                        break;
                    }
                    case 2: {
                        if (SocketManager.mcontext instanceof WelcomeActivity) {
                            ((WelcomeActivity)SocketManager.mcontext).showUserName();
                            return;
                        }
                        final Intent intent = new Intent(SocketManager.mcontext, (Class)WelcomeActivity.class);
                        intent.setFlags(67108864);
                        SocketManager.mcontext.startActivity(intent);
                    }
                    case 3: {
                        if (message.arg1 == 1) {
                            final String[] array = (String[])message.obj;
                            ProgressDialogHint.Show(SocketManager.mcontext, array[0], array[1]);
                            return;
                        }
                        ProgressDialogHint.Dismiss();
                    }
                    case 4: {
                        SocketNoticeManager.getMingpaiMassage();
                    }
                    case 11: {
                        final String[] array2 = (String[])message.obj;
                        new CopyFileThread(array2[0], array2[1], this).start();
                        ProgressHint.Show(SocketManager.mcontext, "导入文件", "正在导入文件，请稍候……");
                    }
                    case 63235: {
                        ProgressHint.Dismiss();
                        if (message.arg1 == 1) {
                            ToastHint.show(SocketManager.mcontext, "复制文件错误！");
                            return;
                        }
                        if (SocketManager.mcontext instanceof MeetinginfoActivity) {
                            ((MeetinginfoActivity)SocketManager.mcontext).RefreshFileList();
                            return;
                        }
                        break;
                    }
                    case 18: {
                        final String[] array3 = (String[])message.obj;
                        final Intent intent2 = new Intent(SocketManager.mcontext, (Class)boardSetting.class);
                        final Bundle bundle = new Bundle();
                        bundle.putString("Name", array3[0]);
                        bundle.putString("Company", array3[1]);
                        bundle.putString("Job", array3[2]);
                        intent2.putExtras(bundle);
                        SocketManager.mcontext.startActivity(intent2);
                    }
                    case 19: {
                        if (SocketManager.mcontext instanceof WelcomeActivity) {
                            ((WelcomeActivity)SocketManager.mcontext).showUserName();
                            return;
                        }
                        break;
                    }
                    case 17: {
                        HttpProtocalManager.getInstance().GetUserInfo();
                    }
                    case 20: {
                        Log.e("Start", "Start NameShow Activity");
                        if (SocketManager.mcontext != null) {
                            final ComponentName component = new ComponentName("com.Meeting.NameShow", "com.Meeting.NameShow.NameShowActivity");
                            final Intent intent3 = new Intent();
                            intent3.setComponent(component);
                            intent3.setFlags(270532608);
                            SocketManager.mcontext.startActivity(intent3);
                            return;
                        }
                        break;
                    }
                }
            }
        };
        SocketManager.syncDemoLockFlag = false;
        SocketManager.syncDemoControllerFlag = false;
        SocketManager.syncDemoDocPath = "";
        SocketManager.mcontext = null;
        SocketManager.ifMain = false;
        SocketManager.running = true;
    }

    private SocketManager() {
        this.mTimeoutTimer = 0;
        this.mSyncTimeOut = 0;
        this.mOnline = false;
        SocketManager.observerActivitys = new Vector<Observer>();
        SocketManager.mVideoStreamTCPSocket = new SocketApi();
        if (SocketManager.mVideoStreamTCPSocket.SocketOpen(MeetingParam.getSocketIP(), TCPPort, 10000) == 1) {
            Log.e("SocketManager", "视频流通讯创建成功");
        }
        initTCPRecvhread();
        (SocketManager.mTCPSendThread = new SendThread()).start();
        mHeartbeatTimer = new Timer(true);
        mHeartbeatTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SocketManager.this.heartBeart();
            }
        },4000,4000);
    }

    private int addMessage(final int cmd, final byte[] array, final int payload_len, final boolean b) {
        final CmdDataBody cmdDataBody = new CmdDataBody(payload_len + 12);
        cmdDataBody.cmd = cmd;
        cmdDataBody.dataType = 0;
        cmdDataBody.payload_len = payload_len;
        if (array != null && payload_len > 0) {
            System.arraycopy(array, 0, cmdDataBody.payload_data, 0, payload_len);
        }
        final byte[] array2 = new byte[12 + cmdDataBody.payload_len];
        CmdDataBody.marshall(cmdDataBody, array2);
        SocketManager.mTCPSendThread.Send(array2, b);
        return 0;
    }

    private static boolean checkTCPSocket(final boolean b) {
        boolean b2 = true;
        Log.e("SocketManager", " ####################### checkTCPSocket: SOCKET 重连检测 #######################");
        if (b) {
            Log.e("SocketManager", " ####################### checkTCPSocket: SOCKET 重新创建#######################");
            SocketManager.mVideoStreamTCPSocket.recreateSocket();
        }
        else if (SocketManager.mVideoStreamTCPSocket.isConnected()) {
            return b2;
        }
        if (SocketManager.mVideoStreamTCPSocket.connect() != (b2 ? 1 : 0)) {
            Log.e("SocketManager", "checkTCPVideoStreamSocket: TCP视频通道连接失败！");
            b2 = false;
        }
        else {
            Log.e("SocketManager", "checkTCPVideoStreamSocket: TCP视频通道连接成功！");
            if (SocketManager.mTCPSendThread != null) {
                SocketManager.mTCPSendThread.ClearQueue();
                final String string = SettingManager.getInstance().readSetting("mgID", "", "").toString();
                if (string != null && !string.equals("")) {
                    getInstance().LoginMgId(string);
                    return b2;
                }
                getInstance().RegisterMgID(WelcomeActivity.MGID);
                return b2;
            }
        }
        return b2;
    }

    public static SocketManager getInstance() {
        synchronized (SocketManager.class) {
            if (SocketManager.mInstance == null) {
                Log.e("SocketManager", "new instance");
                SocketManager.mInstance = new SocketManager();
            }
            ++SocketManager.mRefCount;
            return SocketManager.mInstance;
        }
    }

    private static void initTCPRecvhread() {
        SocketManager.mTCPRecvHandler = new Handler() {
            public void handleMessage(final Message message) {
                notice(message.what, message.obj);
            }
        };
        (SocketManager.mTCPRecvThread = new Thread(new Runnable() {
            @Override
            public void run() {

                int i = 0;
                while(SocketManager.running){

                    if (!SocketManager.checkTCPSocket(false)) {
                        continue;
                    }

                    if ((SocketManager.mVideoStreamTCPSocket != null) && (SocketManager.mVideoStreamTCPSocket.isConnected()))
                    {
                        byte[] arrayOfByte1 = SocketManager.socket_read(12);
                        if ((arrayOfByte1 == null) || (arrayOfByte1.length != 12)) {
                            continue;
                        }
                        int j = (arrayOfByte1[3] << 24) + ((0xFF & arrayOfByte1[2]) << 16) + ((0xFF & arrayOfByte1[1]) << 8) + (0xFF & arrayOfByte1[0]);
                        int k = (arrayOfByte1[7] << 24) + ((0xFF & arrayOfByte1[6]) << 16) + ((0xFF & arrayOfByte1[5]) << 8) + (0xFF & arrayOfByte1[4]);
                        int m = (arrayOfByte1[11] << 24) + ((0xFF & arrayOfByte1[10]) << 16) + ((0xFF & arrayOfByte1[9]) << 8) + (0xFF & arrayOfByte1[8]);
                        Log.e("SocketManager", "dataType:" + k);

                        int n = i + 1;
                        // Log.e("共接受%d", i);
                        byte[] arrayOfByte3 = null;
                        if (m > 0)
                        {
                            arrayOfByte3 = null;
                            if (m < 51200)
                            {
                                arrayOfByte3 = new byte[m];
                                if (!SocketManager.socket_read_block(arrayOfByte3, m))
                                {
                                    Log.e("SocketManager", "didReceiveSocketData: payload_data读取失败");
                                    i = n;
                                    continue;
                                }
                                Log.e("SocketManager", "didReceiveSocketData: 读取到数据[" + arrayOfByte3 + "]");
                            }
                        }
                        SocketManager.mTCPRecvHandler.obtainMessage(j, arrayOfByte3).sendToTarget();
                        i = n;
                    }
                }
            }
        })).start();
        SocketManager.mTCPRecvThread.setPriority(10);
    }

    private static void notice(final int n, final Object o) {
        SocketNoticeManager.getNotice(n, o, SocketManager.mcontext, null);
        for (int size = SocketManager.observerActivitys.size(), i = 0; i < size; ++i) {
            SocketManager.observerActivitys.get(i).notice(n, o);
        }
    }

    public static void release() {
        --SocketManager.mRefCount;
        if (SocketManager.mRefCount == 0) {
            SocketManager.mInstance = null;
        }
    }

    private void setCurrentContext(final Context mcontext) {
        SocketManager.mcontext = mcontext;
    }

    private static byte[] socket_read(final int n) {
        final byte[] array = new byte[n];
        final int socketReadByBuffered = SocketManager.mVideoStreamTCPSocket.SocketReadByBuffered(array, n);
        Log.e("SocketRead", "recLen:" + socketReadByBuffered + ",needLen:" + n);
        if (socketReadByBuffered > 0) {
            return array;
        }
        return null;
    }

    private static boolean socket_read_block(final byte[] array, final int n) {
        if (n > 0) {
            final int socketReadByBuffered = SocketManager.mVideoStreamTCPSocket.SocketReadByBuffered(array, n);
            Log.e("SocketRead", "recLen:" + socketReadByBuffered + ",needLen:" + n);
            if (socketReadByBuffered > 0) {
                return true;
            }
        }
        return false;
    }

    public void LoginMgId(final String s) {
        if (s != null && !s.equals("")) {
            this.addMessage(31, s.getBytes(), s.length(), false);
        }
    }

    public void ReConnection(final String s) {
        SocketManager.mVideoStreamTCPSocket.close();
        SocketManager.mVideoStreamTCPSocket.SocketOpen(s, 4567, 5000);
        SocketManager.mVideoStreamTCPSocket.connect();
    }

    public void RegisterMgID(final String s) {
        if (s != null && !s.equals("")) {
            final byte[] bytes = s.getBytes();
            this.addMessage(7, bytes, bytes.length, false);
        }
    }

    public void ResMgId(final String s) {
        Log.e("Login", "mgId:" + s);
        if (s != null && !s.equals("")) {
            this.addMessage(7, s.getBytes(), s.length(), false);
        }
    }

    public void attach(final Observer observer, final Context currentContext) {
        SocketNoticeManager.myActivity = (Activity)observer;
        this.setCurrentContext(currentContext);
        SocketManager.observerActivitys.add(observer);
    }

    public void detach(final Observer observer) {
        SocketManager.observerActivitys.remove(observer);
    }

    public void heartBeart() {
        Log.e("SocketManager", "heartBeart心跳包发送: [" + this.mTimeoutTimer + "]");
        this.addMessage(1, null, 0, false);
        ++this.mTimeoutTimer;
        if (this.mTimeoutTimer >= 5) {
            this.mTimeoutTimer = 0;
            this.mOnline = false;
            new Thread() {
                @Override
                public void run() {
                    if(SocketManager.checkTCPSocket(true)){
                        synchronized (SocketManager.mSendLock){
                            SocketManager.mSendLock.notify();
                        }
                    }
                }
            }.start();
        }
    }

    public void sendMessage(final byte[] array) {
        SocketManager.mTCPSendThread.Send(array, false);
    }

    public interface Observer
    {
        int getFlags();

        void handleCommand(final int p0, final byte[] p1);

        void notice(final int p0, final Object p1);
    }

    private class SendThread extends Thread
    {
        private LinkedBlockingQueue<byte[]> mSendList;

        public SendThread() {
            this.mSendList = new LinkedBlockingQueue<byte[]>(5000);
        }

        public void ClearQueue() {
            this.mSendList.clear();
        }

        public void Send(final byte[] array, final boolean b) {
            this.mSendList.offer(array);
        }

        @Override
        public void run() {
            super.run();

            while (SocketManager.running) {
                try {
                    final byte[] array = mSendList.take();
                    if(array != null)
                    {
                        if(SocketManager.mVideoStreamTCPSocket.SocketWriteByBuffered(array)<0)
                        {
                            try {
                                SocketManager.this.mTimeoutTimer = 5;
                                synchronized (SocketManager.mSendLock) {
                                    SocketManager.mSendLock.wait();
                                }
                            }
                            catch (InterruptedException e)
                            {

                            }
                        }
                    }
                }
                catch (InterruptedException e)
                {

                }
            }
        }
    }
}
