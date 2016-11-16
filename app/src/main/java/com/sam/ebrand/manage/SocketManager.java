package com.sam.ebrand.manage;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.meetingNetwork.socket.SocketApi;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ProgressHint;
import com.sam.ebrand.widget.ToastHint;

import java.util.Timer;
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
                        if (SocketManager.mcontext instanceof SetActivity && ((String)message.obj).equals("\u8bbe\u7f6e\u94ed\u724c\u6210\u529f")) {
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
                        ProgressHint.Show(SocketManager.mcontext, "\u5bfc\u5165\u6587\u4ef6", "\u6b63\u5728\u5bfc\u5165\u6587\u4ef6\uff0c\u8bf7\u7a0d\u5019\u2026\u2026");
                    }
                    case 63235: {
                        ProgressHint.Dismiss();
                        if (message.arg1 == 1) {
                            ToastHint.show(SocketManager.mcontext, "\u590d\u5236\u6587\u4ef6\u9519\u8bef\uff01");
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
        if (SocketManager.mVideoStreamTCPSocket.SocketOpen(MeetingParam.getSocketIP(), 4567, 10000) == 1) {
            Log.e("SocketManager", "\u89c6\u9891\u6d41\u901a\u8baf\u521b\u5efa\u6210\u529f");
        }
        initTCPRecvhread();
        (SocketManager.mTCPSendThread = new SendThread()).start();
        (this.mHeartbeatTimer = new Timer(4000, new Timer.OnTimerCallbackListener() {
            @Override
            public int callback() {
                SocketManager.this.heartBeart();
                return 0;
            }
        })).start();
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
        Log.e("SocketManager", " ####################### checkTCPSocket: SOCKET \u91cd\u8fde\u68c0\u6d4b #######################");
        if (b) {
            Log.e("SocketManager", " ####################### checkTCPSocket: SOCKET \u91cd\u65b0\u521b\u5efa#######################");
            SocketManager.mVideoStreamTCPSocket.recreateSocket();
        }
        else if (SocketManager.mVideoStreamTCPSocket.isConnected()) {
            return b2;
        }
        if (SocketManager.mVideoStreamTCPSocket.connect() != (b2 ? 1 : 0)) {
            Log.e("SocketManager", "checkTCPVideoStreamSocket: TCP\u89c6\u9891\u901a\u9053\u8fde\u63a5\u5931\u8d25\uff01");
            b2 = false;
        }
        else {
            Log.e("SocketManager", "checkTCPVideoStreamSocket: TCP\u89c6\u9891\u901a\u9053\u8fde\u63a5\u6210\u529f\uff01");
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
                //
                // This method could not be decompiled.
                //
                // Original Bytecode:
                //
                //     0: iconst_0
                //     1: invokestatic    com/meeting/manager/SocketManager.access$3:(Z)Z
                //     4: ifeq            49
                //     7: getstatic       com/meeting/manager/SocketManager.mRecvLock:[B
                //    10: astore          17
                //    12: aload           17
                //    14: monitorenter
                //    15: ldc             "recvLock"
                //    17: ldc             "recvLock notify"
                //    19: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //    22: pop
                //    23: getstatic       com/meeting/manager/SocketManager.mRecvLock:[B
                //    26: invokevirtual   java/lang/Object.notify:()V
                //    29: aload           17
                //    31: monitorexit
                //    32: getstatic       com/meeting/manager/SocketManager.mSendLock:[B
                //    35: astore          20
                //    37: aload           20
                //    39: monitorenter
                //    40: getstatic       com/meeting/manager/SocketManager.mSendLock:[B
                //    43: invokevirtual   java/lang/Object.notify:()V
                //    46: aload           20
                //    48: monitorexit
                //    49: iconst_0
                //    50: istore_1
                //    51: invokestatic    com/meeting/manager/SocketManager.access$0:()Z
                //    54: ifne            74
                //    57: return
                //    58: astore          18
                //    60: aload           17
                //    62: monitorexit
                //    63: aload           18
                //    65: athrow
                //    66: astore          21
                //    68: aload           20
                //    70: monitorexit
                //    71: aload           21
                //    73: athrow
                //    74: invokestatic    com/meeting/manager/SocketManager.access$1:()Lcom/Infree/MeetingNetwork/socket/SocketApi;
                //    77: ifnull          51
                //    80: invokestatic    com/meeting/manager/SocketManager.access$1:()Lcom/Infree/MeetingNetwork/socket/SocketApi;
                //    83: invokevirtual   com/Infree/MeetingNetwork/socket/SocketApi.isConnected:()Z
                //    86: ifeq            51
                //    89: bipush          12
                //    91: invokestatic    com/meeting/manager/SocketManager.access$4:(I)[B
                //    94: astore_2
                //    95: aload_2
                //    96: ifnull          375
                //    99: aload_2
                //   100: arraylength
                //   101: bipush          12
                //   103: if_icmpne       375
                //   106: aload_2
                //   107: iconst_3
                //   108: baload
                //   109: bipush          24
                //   111: ishl
                //   112: sipush          255
                //   115: aload_2
                //   116: iconst_2
                //   117: baload
                //   118: iand
                //   119: bipush          16
                //   121: ishl
                //   122: iadd
                //   123: sipush          255
                //   126: aload_2
                //   127: iconst_1
                //   128: baload
                //   129: iand
                //   130: bipush          8
                //   132: ishl
                //   133: iadd
                //   134: sipush          255
                //   137: aload_2
                //   138: iconst_0
                //   139: baload
                //   140: iand
                //   141: iadd
                //   142: istore          7
                //   144: aload_2
                //   145: bipush          7
                //   147: baload
                //   148: bipush          24
                //   150: ishl
                //   151: sipush          255
                //   154: aload_2
                //   155: bipush          6
                //   157: baload
                //   158: iand
                //   159: bipush          16
                //   161: ishl
                //   162: iadd
                //   163: sipush          255
                //   166: aload_2
                //   167: iconst_5
                //   168: baload
                //   169: iand
                //   170: bipush          8
                //   172: ishl
                //   173: iadd
                //   174: sipush          255
                //   177: aload_2
                //   178: iconst_4
                //   179: baload
                //   180: iand
                //   181: iadd
                //   182: istore          8
                //   184: aload_2
                //   185: bipush          11
                //   187: baload
                //   188: bipush          24
                //   190: ishl
                //   191: sipush          255
                //   194: aload_2
                //   195: bipush          10
                //   197: baload
                //   198: iand
                //   199: bipush          16
                //   201: ishl
                //   202: iadd
                //   203: sipush          255
                //   206: aload_2
                //   207: bipush          9
                //   209: baload
                //   210: iand
                //   211: bipush          8
                //   213: ishl
                //   214: iadd
                //   215: sipush          255
                //   218: aload_2
                //   219: bipush          8
                //   221: baload
                //   222: iand
                //   223: iadd
                //   224: istore          9
                //   226: ldc             "SocketManager"
                //   228: new             Ljava/lang/StringBuilder;
                //   231: dup
                //   232: ldc             "dataType:"
                //   234: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
                //   237: iload           8
                //   239: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
                //   242: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //   245: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //   248: pop
                //   249: new             Ljava/lang/StringBuilder;
                //   252: dup
                //   253: ldc             "count:"
                //   255: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
                //   258: astore          11
                //   260: iload_1
                //   261: iconst_1
                //   262: iadd
                //   263: istore          12
                //   265: ldc             "\u5171\u63a5\u53d7"
                //   267: aload           11
                //   269: iload_1
                //   270: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
                //   273: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //   276: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //   279: pop
                //   280: aconst_null
                //   281: astore          14
                //   283: iload           9
                //   285: ifle            356
                //   288: aconst_null
                //   289: astore          14
                //   291: iload           9
                //   293: ldc             51200
                //   295: if_icmpge       356
                //   298: iload           9
                //   300: newarray        B
                //   302: astore          14
                //   304: aload           14
                //   306: iload           9
                //   308: invokestatic    com/meeting/manager/SocketManager.access$5:([BI)Z
                //   311: ifne            328
                //   314: ldc             "SocketManager"
                //   316: ldc             "didReceiveSocketData: payload_data\u8bfb\u53d6\u5931\u8d25"
                //   318: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //   321: pop
                //   322: iload           12
                //   324: istore_1
                //   325: goto            51
                //   328: ldc             "SocketManager"
                //   330: new             Ljava/lang/StringBuilder;
                //   333: dup
                //   334: ldc             "didReceiveSocketData: \u8bfb\u53d6\u5230\u6570\u636e["
                //   336: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
                //   339: aload           14
                //   341: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
                //   344: ldc             "]"
                //   346: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
                //   349: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
                //   352: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //   355: pop
                //   356: invokestatic    com/meeting/manager/SocketManager.access$6:()Landroid/os/Handler;
                //   359: iload           7
                //   361: aload           14
                //   363: invokevirtual   android/os/Handler.obtainMessage:(ILjava/lang/Object;)Landroid/os/Message;
                //   366: invokevirtual   android/os/Message.sendToTarget:()V
                //   369: iload           12
                //   371: istore_1
                //   372: goto            51
                //   375: getstatic       com/meeting/manager/SocketManager.mRecvLock:[B
                //   378: astore_3
                //   379: aload_3
                //   380: monitorenter
                //   381: ldc             "recvLock"
                //   383: ldc             "recvLock wait"
                //   385: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                //   388: pop
                //   389: getstatic       com/meeting/manager/SocketManager.mRecvLock:[B
                //   392: invokevirtual   java/lang/Object.wait:()V
                //   395: aload_3
                //   396: monitorexit
                //   397: goto            51
                //   400: astore          5
                //   402: aload_3
                //   403: monitorexit
                //   404: aload           5
                //   406: athrow
                //   407: astore          4
                //   409: aload           4
                //   411: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
                //   414: goto            395
                //    Exceptions:
                //  Try           Handler
                //  Start  End    Start  End    Type
                //  -----  -----  -----  -----  --------------------------------
                //  15     32     58     66     Any
                //  40     49     66     74     Any
                //  60     63     58     66     Any
                //  68     71     66     74     Any
                //  381    395    407    417    Ljava/lang/InterruptedException;
                //  381    395    400    407    Any
                //  395    397    400    407    Any
                //  402    404    400    407    Any
                //  409    414    400    407    Any
                //
                throw new IllegalStateException("An error occurred while decompiling this method.");
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
        Log.e("SocketManager", "heartBeart\u5fc3\u8df3\u5305\u53d1\u9001: [" + this.mTimeoutTimer + "]");
        this.addMessage(1, null, 0, false);
        ++this.mTimeoutTimer;
        if (this.mTimeoutTimer >= 5) {
            this.mTimeoutTimer = 0;
            this.mOnline = false;
            new Thread() {
                @Override
                public void run() {
                    //
                    // This method could not be decompiled.
                    //
                    // Original Bytecode:
                    //
                    //     0: iconst_1
                    //     1: invokestatic    com/meeting/manager/SocketManager.access$3:(Z)Z
                    //     4: ifeq            46
                    //     7: getstatic       com/meeting/manager/SocketManager.mRecvLock:[B
                    //    10: astore_1
                    //    11: aload_1
                    //    12: monitorenter
                    //    13: ldc             "recvLock"
                    //    15: ldc             "recvLock notify"
                    //    17: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
                    //    20: pop
                    //    21: getstatic       com/meeting/manager/SocketManager.mRecvLock:[B
                    //    24: invokevirtual   java/lang/Object.notify:()V
                    //    27: aload_1
                    //    28: monitorexit
                    //    29: getstatic       com/meeting/manager/SocketManager.mSendLock:[B
                    //    32: astore          4
                    //    34: aload           4
                    //    36: monitorenter
                    //    37: getstatic       com/meeting/manager/SocketManager.mSendLock:[B
                    //    40: invokevirtual   java/lang/Object.notify:()V
                    //    43: aload           4
                    //    45: monitorexit
                    //    46: return
                    //    47: astore_2
                    //    48: aload_1
                    //    49: monitorexit
                    //    50: aload_2
                    //    51: athrow
                    //    52: astore          5
                    //    54: aload           4
                    //    56: monitorexit
                    //    57: aload           5
                    //    59: athrow
                    //    Exceptions:
                    //  Try           Handler
                    //  Start  End    Start  End    Type
                    //  -----  -----  -----  -----  ----
                    //  13     29     47     52     Any
                    //  37     46     52     60     Any
                    //  48     50     47     52     Any
                    //  54     57     52     60     Any
                    //
                    throw new IllegalStateException("An error occurred while decompiling this method.");
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
