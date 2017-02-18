package com.sam.ebrand.application.thread;

import android.os.Handler;
import android.os.Message;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by sam on 2016/11/18.
 */

public class CopyFileThread extends Thread
{
    private Handler mHandler;
    private String mSrcPath;
    private String mTargetPath;

    public CopyFileThread(final String mSrcPath, final String mTargetPath, final Handler mHandler) {
        this.mSrcPath = mSrcPath;
        this.mTargetPath = mTargetPath;
        this.mHandler = mHandler;
    }

    public static boolean CopyFile(final String p0, final String p1) {
        try {
            InputStream in = new FileInputStream(p0);
            OutputStream out = new FileOutputStream(p1);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();;
        }

        return false;
        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     0: new             Ljava/io/File;
        //     3: dup
        //     4: aload_0
        //     5: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //     8: astore_2
        //     9: new             Ljava/io/File;
        //    12: dup
        //    13: aload_1
        //    14: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
        //    17: astore_3
        //    18: aload_2
        //    19: invokevirtual   java/io/File.exists:()Z
        //    22: ifne            27
        //    25: iconst_0
        //    26: ireturn
        //    27: aload_3
        //    28: invokevirtual   java/io/File.exists:()Z
        //    31: ifne            42
        //    34: aload_3
        //    35: invokevirtual   java/io/File.getParentFile:()Ljava/io/File;
        //    38: invokevirtual   java/io/File.mkdirs:()Z
        //    41: pop
        //    42: aload_2
        //    43: invokevirtual   java/io/File.isDirectory:()Z
        //    46: ifeq            154
        //    49: aload_3
        //    50: invokevirtual   java/io/File.mkdirs:()Z
        //    53: pop
        //    54: aload_2
        //    55: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //    58: astore          17
        //    60: aload           17
        //    62: ifnull          76
        //    65: iconst_0
        //    66: istore          18
        //    68: iload           18
        //    70: aload           17
        //    72: arraylength
        //    73: if_icmplt       78
        //    76: iconst_1
        //    77: ireturn
        //    78: new             Ljava/lang/StringBuilder;
        //    81: dup
        //    82: aload_0
        //    83: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //    86: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    89: getstatic       java/io/File.separator:Ljava/lang/String;
        //    92: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    95: aload           17
        //    97: iload           18
        //    99: aaload
        //   100: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   103: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   106: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   109: new             Ljava/lang/StringBuilder;
        //   112: dup
        //   113: aload_1
        //   114: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //   117: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   120: getstatic       java/io/File.separator:Ljava/lang/String;
        //   123: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   126: aload           17
        //   128: iload           18
        //   130: aaload
        //   131: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   134: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   137: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   140: invokestatic    com/meeting/application/Thread/CopyFileThread.CopyFile:(Ljava/lang/String;Ljava/lang/String;)Z
        //   143: ifne            148
        //   146: iconst_0
        //   147: ireturn
        //   148: iinc            18, 1
        //   151: goto            68
        //   154: aconst_null
        //   155: astore          4
        //   157: aconst_null
        //   158: astore          5
        //   160: new             Ljava/io/BufferedOutputStream;
        //   163: dup
        //   164: new             Ljava/io/FileOutputStream;
        //   167: dup
        //   168: aload_3
        //   169: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   172: sipush          8192
        //   175: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;I)V
        //   178: astore          6
        //   180: new             Ljava/io/BufferedInputStream;
        //   183: dup
        //   184: new             Ljava/io/FileInputStream;
        //   187: dup
        //   188: aload_2
        //   189: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
        //   192: sipush          8192
        //   195: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;I)V
        //   198: astore          7
        //   200: sipush          8192
        //   203: newarray        B
        //   205: astore          14
        //   207: aload           7
        //   209: aload           14
        //   211: iconst_0
        //   212: sipush          8192
        //   215: invokevirtual   java/io/BufferedInputStream.read:([BII)I
        //   218: istore          15
        //   220: iload           15
        //   222: iconst_m1
        //   223: if_icmpne       243
        //   226: aload           6
        //   228: invokevirtual   java/io/BufferedOutputStream.flush:()V
        //   231: aload           6
        //   233: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   236: aload           7
        //   238: invokevirtual   java/io/BufferedInputStream.close:()V
        //   241: iconst_1
        //   242: ireturn
        //   243: aload           6
        //   245: aload           14
        //   247: iconst_0
        //   248: iload           15
        //   250: invokevirtual   java/io/BufferedOutputStream.write:([BII)V
        //   253: goto            207
        //   256: astore          10
        //   258: aload           7
        //   260: astore          11
        //   262: aload           6
        //   264: astore          12
        //   266: aload           10
        //   268: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
        //   271: aload           12
        //   273: ifnull          281
        //   276: aload           12
        //   278: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   281: aload           11
        //   283: ifnull          291
        //   286: aload           11
        //   288: invokevirtual   java/io/BufferedInputStream.close:()V
        //   291: iconst_0
        //   292: ireturn
        //   293: astore          8
        //   295: aload           8
        //   297: invokevirtual   java/io/IOException.printStackTrace:()V
        //   300: aload           4
        //   302: ifnull          310
        //   305: aload           4
        //   307: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   310: aload           5
        //   312: ifnull          291
        //   315: aload           5
        //   317: invokevirtual   java/io/BufferedInputStream.close:()V
        //   320: goto            291
        //   323: astore          9
        //   325: goto            291
        //   328: astore          8
        //   330: aload           6
        //   332: astore          4
        //   334: aconst_null
        //   335: astore          5
        //   337: goto            295
        //   340: astore          8
        //   342: aload           7
        //   344: astore          5
        //   346: aload           6
        //   348: astore          4
        //   350: goto            295
        //   353: astore          13
        //   355: goto            291
        //   358: astore          10
        //   360: aconst_null
        //   361: astore          11
        //   363: aconst_null
        //   364: astore          12
        //   366: goto            266
        //   369: astore          10
        //   371: aload           6
        //   373: astore          12
        //   375: aconst_null
        //   376: astore          11
        //   378: goto            266
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  -------------------------------
        //  160    180    358    369    Ljava/io/FileNotFoundException;
        //  160    180    293    295    Ljava/io/IOException;
        //  180    200    369    381    Ljava/io/FileNotFoundException;
        //  180    200    328    340    Ljava/io/IOException;
        //  200    207    256    266    Ljava/io/FileNotFoundException;
        //  200    207    340    353    Ljava/io/IOException;
        //  207    220    256    266    Ljava/io/FileNotFoundException;
        //  207    220    340    353    Ljava/io/IOException;
        //  226    241    256    266    Ljava/io/FileNotFoundException;
        //  226    241    340    353    Ljava/io/IOException;
        //  243    253    256    266    Ljava/io/FileNotFoundException;
        //  243    253    340    353    Ljava/io/IOException;
        //  276    281    353    358    Ljava/io/IOException;
        //  286    291    353    358    Ljava/io/IOException;
        //  305    310    323    328    Ljava/io/IOException;
        //  315    320    323    328    Ljava/io/IOException;
        //
       // throw new IllegalStateException("An error occurred while decompiling this method.");
    }

    @Override
    public void run() {
        super.run();
        if (CopyFile(this.mSrcPath, this.mTargetPath)) {
            final Message obtain = Message.obtain();
            obtain.what = 63235;
            obtain.arg1 = 0;
            this.mHandler.sendMessageDelayed(obtain, 1000L);
            return;
        }
        final Message obtain2 = Message.obtain();
        obtain2.what = 63235;
        obtain2.arg1 = 1;
        this.mHandler.sendMessageDelayed(obtain2, 1000L);
    }
}
