package com.sam.ebrand.manage;

import android.content.Context;

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
           // Log.i("SubLcd", "\u5237\u526f\u5c4f\u8bf7\u6c42\uff01");
            this.mMsgQueue.add(subLcdMsg);
            synchronized (this.mLock) {
                this.mLock.notify();
            }
        }
    }
    @Override
    public void run() {
        while(mbStart){
            try{
                Thread.sleep(5000);
            }
            catch (Exception e){

            }


        }
        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     0: aload_0
        //     1: invokespecial   java/lang/Thread.run:()V
        //     4: aload_0
        //     5: iconst_1
        //     6: putfield        com/meeting/manager/SubLcdManager.mbStart:Z
        //     9: ldc2_w          5000
        //    12: invokestatic    java/lang/Thread.sleep:(J)V
        //    15: invokestatic    android/os/Environment.getExternalStorageState:()Ljava/lang/String;
        //    18: ldc             "mounted"
        //    20: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    23: ifeq            370
        //    26: getstatic       com/meeting/manager/SocketManager.mStaticHandler:Landroid/os/Handler;
        //    29: bipush          20
        //    31: invokevirtual   android/os/Handler.obtainMessage:(I)Landroid/os/Message;
        //    34: invokevirtual   android/os/Message.sendToTarget:()V
        //    37: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_ROOT:Ljava/lang/String;
        //    40: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    43: pop
        //    44: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_WHITEBOARD:Ljava/lang/String;
        //    47: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    50: pop
        //    51: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_DOC:Ljava/lang/String;
        //    54: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    57: pop
        //    58: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_COMMENT:Ljava/lang/String;
        //    61: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    64: pop
        //    65: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_UDISK_DOC:Ljava/lang/String;
        //    68: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    71: pop
        //    72: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_UDISK_RESTAURANT:Ljava/lang/String;
        //    75: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    78: pop
        //    79: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_UDISK_SCHEDULE:Ljava/lang/String;
        //    82: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    85: pop
        //    86: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_SXPZ:Ljava/lang/String;
        //    89: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    92: pop
        //    93: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_BACKGROUND_PIC_FOLDER:Ljava/lang/String;
        //    96: invokestatic    com/meeting/win/winutils/FileUtils.creatDir:(Ljava/lang/String;)Ljava/io/File;
        //    99: pop
        //   100: ldc             ""
        //   102: astore          12
        //   104: ldc             "MD5"
        //   106: invokestatic    java/security/MessageDigest.getInstance:(Ljava/lang/String;)Ljava/security/MessageDigest;
        //   109: astore          47
        //   111: aload           47
        //   113: astore          14
        //   115: ldc2_w          1000
        //   118: invokestatic    com/meeting/manager/SubLcdManager.sleep:(J)V
        //   121: aload_0
        //   122: getfield        com/meeting/manager/SubLcdManager.mLock:[B
        //   125: astore          16
        //   127: aload           16
        //   129: monitorenter
        //   130: aload_0
        //   131: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   134: invokevirtual   java/util/LinkedList.size:()I
        //   137: istore          18
        //   139: iload           18
        //   141: ifgt            151
        //   144: aload_0
        //   145: getfield        com/meeting/manager/SubLcdManager.mLock:[B
        //   148: invokevirtual   java/lang/Object.wait:()V
        //   151: aload_0
        //   152: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   155: invokevirtual   java/util/LinkedList.poll:()Ljava/lang/Object;
        //   158: checkcast       Lcom/meeting/manager/SubLcdMsg;
        //   161: astore          19
        //   163: getstatic       com/meeting/engine/LCDEngine.backlightStatus:Z
        //   166: ifne            202
        //   169: iconst_1
        //   170: invokestatic    com/meeting/engine/LCDEngine.backlight:(I)I
        //   173: pop
        //   174: iconst_1
        //   175: putstatic       com/meeting/engine/LCDEngine.backlightStatus:Z
        //   178: invokestatic    android/os/Message.obtain:()Landroid/os/Message;
        //   181: astore          44
        //   183: aload           44
        //   185: bipush          19
        //   187: putfield        android/os/Message.what:I
        //   190: getstatic       com/meeting/manager/SocketManager.mStaticHandler:Landroid/os/Handler;
        //   193: aload           44
        //   195: ldc2_w          1000
        //   198: invokevirtual   android/os/Handler.sendMessageDelayed:(Landroid/os/Message;J)Z
        //   201: pop
        //   202: aload           19
        //   204: ifnull          630
        //   207: aload           19
        //   209: getfield        com/meeting/manager/SubLcdMsg.bDefault:I
        //   212: iconst_1
        //   213: if_icmpne       420
        //   216: aload_0
        //   217: getfield        com/meeting/manager/SubLcdManager.mContext:Landroid/content/Context;
        //   220: ldc             2130837691
        //   222: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_WELCOME_PICTURE:Ljava/lang/String;
        //   225: invokestatic    com/meeting/win/winutils/FileUtils.PngToBmp:(Landroid/content/Context;ILjava/lang/String;)Z
        //   228: pop
        //   229: new             Ljava/io/File;
        //   232: dup
        //   233: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_WELCOME_PICTURE:Ljava/lang/String;
        //   236: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //   239: astore          22
        //   241: aload           22
        //   243: invokevirtual   java/io/File.exists:()Z
        //   246: ifeq            761
        //   249: ldc             ""
        //   251: astore          23
        //   253: sipush          8192
        //   256: newarray        B
        //   258: astore          24
        //   260: new             Ljava/io/FileInputStream;
        //   263: dup
        //   264: aload           22
        //   266: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   269: astore          25
        //   271: aload           14
        //   273: invokevirtual   java/security/MessageDigest.reset:()V
        //   276: aload           25
        //   278: aload           24
        //   280: invokevirtual   java/io/FileInputStream.read:([B)I
        //   283: istore          38
        //   285: iload           38
        //   287: iconst_m1
        //   288: if_icmpne       504
        //   291: aload           25
        //   293: invokevirtual   java/io/FileInputStream.close:()V
        //   296: new             Ljava/lang/String;
        //   299: dup
        //   300: aload           14
        //   302: invokevirtual   java/security/MessageDigest.digest:()[B
        //   305: invokespecial   java/lang/String.<init>:([B)V
        //   308: astore          39
        //   310: aload           39
        //   312: aload           12
        //   314: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   317: ifeq            636
        //   320: ldc             "MD5"
        //   322: ldc             "same md5,abort!"
        //   324: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   327: pop
        //   328: aload           19
        //   330: getfield        com/meeting/manager/SubLcdMsg.bNeedToToast:Z
        //   333: ifeq            348
        //   336: getstatic       com/meeting/manager/SocketManager.mStaticHandler:Landroid/os/Handler;
        //   339: iconst_1
        //   340: ldc             "\u8bbe\u7f6e\u94ed\u724c\u6210\u529f"
        //   342: invokevirtual   android/os/Handler.obtainMessage:(ILjava/lang/Object;)Landroid/os/Message;
        //   345: invokevirtual   android/os/Message.sendToTarget:()V
        //   348: aload           16
        //   350: monitorexit
        //   351: goto            115
        //   354: astore          17
        //   356: aload           16
        //   358: monitorexit
        //   359: aload           17
        //   361: athrow
        //   362: astore_1
        //   363: aload_1
        //   364: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   367: goto            15
        //   370: ldc2_w          2000
        //   373: invokestatic    com/meeting/manager/SubLcdManager.sleep:(J)V
        //   376: goto            15
        //   379: astore_2
        //   380: aload_2
        //   381: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   384: goto            15
        //   387: astore          13
        //   389: aload           13
        //   391: invokevirtual   java/security/NoSuchAlgorithmException.printStackTrace:()V
        //   394: aconst_null
        //   395: astore          14
        //   397: goto            115
        //   400: astore          15
        //   402: aload           15
        //   404: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   407: goto            121
        //   410: astore          46
        //   412: aload           46
        //   414: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   417: goto            151
        //   420: aload           19
        //   422: getfield        com/meeting/manager/SubLcdMsg.bDefault:I
        //   425: ifeq            229
        //   428: aload           19
        //   430: getfield        com/meeting/manager/SubLcdMsg.bBacklightOn:I
        //   433: iconst_1
        //   434: if_icmpne       468
        //   437: iconst_1
        //   438: invokestatic    com/meeting/engine/LCDEngine.backlight:(I)I
        //   441: ifeq            462
        //   444: ldc             "SubLcd"
        //   446: ldc_w           "\u5f00\u542f\u80cc\u5149\u5931\u8d25\uff01"
        //   449: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   452: pop
        //   453: aload_0
        //   454: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   457: aload           19
        //   459: invokevirtual   java/util/LinkedList.addFirst:(Ljava/lang/Object;)V
        //   462: aload           16
        //   464: monitorexit
        //   465: goto            115
        //   468: aload           19
        //   470: getfield        com/meeting/manager/SubLcdMsg.bBacklightOn:I
        //   473: ifne            462
        //   476: iconst_0
        //   477: invokestatic    com/meeting/engine/LCDEngine.backlight:(I)I
        //   480: ifeq            462
        //   483: ldc             "SubLcd"
        //   485: ldc_w           "\u5f00\u542f\u80cc\u5149\u5931\u8d25\uff01"
        //   488: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   491: pop
        //   492: aload_0
        //   493: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   496: aload           19
        //   498: invokevirtual   java/util/LinkedList.addFirst:(Ljava/lang/Object;)V
        //   501: goto            462
        //   504: aload           14
        //   506: aload           24
        //   508: iconst_0
        //   509: iload           38
        //   511: invokevirtual   java/security/MessageDigest.update:([BII)V
        //   514: goto            276
        //   517: astore          37
        //   519: aload           37
        //   521: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
        //   524: aload           19
        //   526: getfield        com/meeting/manager/SubLcdMsg.bBacklightOn:I
        //   529: iconst_1
        //   530: if_icmpne       662
        //   533: iconst_1
        //   534: invokestatic    com/meeting/engine/LCDEngine.backlight:(I)I
        //   537: ifeq            558
        //   540: ldc             "SubLcd"
        //   542: ldc_w           "\u5f00\u542f\u80cc\u5149\u5931\u8d25\uff01"
        //   545: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   548: pop
        //   549: aload_0
        //   550: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   553: aload           19
        //   555: invokevirtual   java/util/LinkedList.addFirst:(Ljava/lang/Object;)V
        //   558: getstatic       com/meeting/manager/SubLcdManager.moperatorLock:[B
        //   561: astore          28
        //   563: aload           28
        //   565: monitorenter
        //   566: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_WELCOME_PICTURE:Ljava/lang/String;
        //   569: invokestatic    com/meeting/engine/LCDEngine.saveRefreshSubLcd:(Ljava/lang/String;)I
        //   572: pop
        //   573: aload           28
        //   575: monitorexit
        //   576: ldc2_w          1000
        //   579: invokestatic    com/meeting/manager/SubLcdManager.sleep:(J)V
        //   582: getstatic       com/meeting/manager/SubLcdManager.moperatorLock:[B
        //   585: astore          32
        //   587: aload           32
        //   589: monitorenter
        //   590: getstatic       com/Infree/Meeting/param/MeetingParam.SDCARD_WELCOME_PICTURE:Ljava/lang/String;
        //   593: invokestatic    com/meeting/engine/LCDEngine.saveRefreshSubLcd:(Ljava/lang/String;)I
        //   596: istore          34
        //   598: aload           32
        //   600: monitorexit
        //   601: invokestatic    com/meeting/manager/BackgroundManager.getInstance:()Lcom/meeting/manager/BackgroundManager;
        //   604: invokevirtual   com/meeting/manager/BackgroundManager.UpdateSublcdDrawable:()V
        //   607: iload           34
        //   609: ifeq            724
        //   612: ldc             "SubLcd"
        //   614: ldc_w           "\u5237\u526f\u5c4f\u5931\u8d25"
        //   617: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   620: pop
        //   621: aload_0
        //   622: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   625: aload           19
        //   627: invokevirtual   java/util/LinkedList.addFirst:(Ljava/lang/Object;)V
        //   630: aload           16
        //   632: monitorexit
        //   633: goto            115
        //   636: ldc             "MD5"
        //   638: ldc_w           "different md5,refresh"
        //   641: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   644: pop
        //   645: aload           39
        //   647: astore          23
        //   649: goto            524
        //   652: astore          26
        //   654: aload           26
        //   656: invokevirtual   java/io/IOException.printStackTrace:()V
        //   659: goto            524
        //   662: aload           19
        //   664: getfield        com/meeting/manager/SubLcdMsg.bBacklightOn:I
        //   667: ifne            558
        //   670: iconst_0
        //   671: invokestatic    com/meeting/engine/LCDEngine.backlight:(I)I
        //   674: ifeq            558
        //   677: ldc             "SubLcd"
        //   679: ldc_w           "\u5f00\u542f\u80cc\u5149\u5931\u8d25\uff01"
        //   682: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   685: pop
        //   686: aload_0
        //   687: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   690: aload           19
        //   692: invokevirtual   java/util/LinkedList.addFirst:(Ljava/lang/Object;)V
        //   695: goto            558
        //   698: astore          29
        //   700: aload           28
        //   702: monitorexit
        //   703: aload           29
        //   705: athrow
        //   706: astore          31
        //   708: aload           31
        //   710: invokevirtual   java/lang/InterruptedException.printStackTrace:()V
        //   713: goto            582
        //   716: astore          33
        //   718: aload           32
        //   720: monitorexit
        //   721: aload           33
        //   723: athrow
        //   724: aload           23
        //   726: astore          12
        //   728: aload           19
        //   730: getfield        com/meeting/manager/SubLcdMsg.bNeedToToast:Z
        //   733: ifeq            748
        //   736: getstatic       com/meeting/manager/SocketManager.mStaticHandler:Landroid/os/Handler;
        //   739: iconst_1
        //   740: ldc             "\u8bbe\u7f6e\u94ed\u724c\u6210\u529f"
        //   742: invokevirtual   android/os/Handler.obtainMessage:(ILjava/lang/Object;)Landroid/os/Message;
        //   745: invokevirtual   android/os/Message.sendToTarget:()V
        //   748: getstatic       com/meeting/manager/SocketManager.mStaticHandler:Landroid/os/Handler;
        //   751: iconst_2
        //   752: invokevirtual   android/os/Handler.obtainMessage:(I)Landroid/os/Message;
        //   755: invokevirtual   android/os/Message.sendToTarget:()V
        //   758: goto            630
        //   761: aload_0
        //   762: getfield        com/meeting/manager/SubLcdManager.mMsgQueue:Ljava/util/LinkedList;
        //   765: aload           19
        //   767: invokevirtual   java/util/LinkedList.addFirst:(Ljava/lang/Object;)V
        //   770: goto            630
        //   773: astore          26
        //   775: aload           39
        //   777: astore          23
        //   779: goto            654
        //   782: astore          37
        //   784: aload           39
        //   786: astore          23
        //   788: goto            519
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----------------------------------------
        //  9      15     362    370    Ljava/lang/InterruptedException;
        //  104    111    387    400    Ljava/security/NoSuchAlgorithmException;
        //  115    121    400    410    Ljava/lang/InterruptedException;
        //  130    139    354    362    Any
        //  144    151    410    420    Ljava/lang/InterruptedException;
        //  144    151    354    362    Any
        //  151    202    354    362    Any
        //  207    229    354    362    Any
        //  229    249    354    362    Any
        //  253    260    354    362    Any
        //  260    276    517    519    Ljava/io/FileNotFoundException;
        //  260    276    652    654    Ljava/io/IOException;
        //  260    276    354    362    Any
        //  276    285    517    519    Ljava/io/FileNotFoundException;
        //  276    285    652    654    Ljava/io/IOException;
        //  276    285    354    362    Any
        //  291    310    517    519    Ljava/io/FileNotFoundException;
        //  291    310    652    654    Ljava/io/IOException;
        //  291    310    354    362    Any
        //  310    348    782    791    Ljava/io/FileNotFoundException;
        //  310    348    773    782    Ljava/io/IOException;
        //  310    348    354    362    Any
        //  348    351    354    362    Any
        //  356    359    354    362    Any
        //  370    376    379    387    Ljava/lang/InterruptedException;
        //  412    417    354    362    Any
        //  420    462    354    362    Any
        //  462    465    354    362    Any
        //  468    501    354    362    Any
        //  504    514    517    519    Ljava/io/FileNotFoundException;
        //  504    514    652    654    Ljava/io/IOException;
        //  504    514    354    362    Any
        //  519    524    354    362    Any
        //  524    558    354    362    Any
        //  558    566    354    362    Any
        //  566    576    698    706    Any
        //  576    582    706    716    Ljava/lang/InterruptedException;
        //  576    582    354    362    Any
        //  582    590    354    362    Any
        //  590    601    716    724    Any
        //  601    607    354    362    Any
        //  612    630    354    362    Any
        //  630    633    354    362    Any
        //  636    645    782    791    Ljava/io/FileNotFoundException;
        //  636    645    773    782    Ljava/io/IOException;
        //  636    645    354    362    Any
        //  654    659    354    362    Any
        //  662    695    354    362    Any
        //  700    703    698    706    Any
        //  703    706    354    362    Any
        //  708    713    354    362    Any
        //  718    721    716    724    Any
        //  721    724    354    362    Any
        //  728    748    354    362    Any
        //  748    758    354    362    Any
        //  761    770    354    362    Any
        //
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }

    public void setContext(final Context mContext) {
        this.mContext = mContext;
    }
}
