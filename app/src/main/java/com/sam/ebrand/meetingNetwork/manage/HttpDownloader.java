package com.sam.ebrand.meetingNetwork.manage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sam on 2016/11/17.
 */
public class HttpDownloader {

    public static final int CANCELED = 2;
    public static final int COMPLETE = 3;
    public static final int DEFAULT_BUFFER_READER_SIZE = 8192;
    public static final String DEFAULT_CHARSET_NAME = "GBK";
    public static final int DEFAULT_DOWNLOAD_BUFFER_SIZE = 65536;
    public static final int DOWNLOADING = 1;
    public static final int DOWNLOAD_RESULT_CODE_CANCEL = 2;
    public static final int DOWNLOAD_RESULT_CODE_FAIL = 0;
    public static final int DOWNLOAD_RESULT_CODE_SUCCESS = 1;
    public static final int DOWNLOAD_RESULT_CODE_URL_ILLEAGL = -1;
    public static final String TAG = "HttpDownloader";
    public int mStatus;

    public HttpDownloader() {
        this.mStatus = 1;
        DownloaderManager.getInstance().addDownloader(this.hashCode(), this);
    }

    public static Bitmap downloadBitmapFromUrl(final String s) {
        Bitmap decodeStream = null;
        if (s == null) {
            return decodeStream;
        }
        final boolean equals = s.trim().equals("");
        decodeStream = null;
        if (equals) {
            return decodeStream;
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)new URL(s).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            final int responseCode = httpURLConnection.getResponseCode();
            decodeStream = null;
            if (responseCode == 200) {
                Log.d("HttpDownloader", "Start download image form : " + s);
                decodeStream = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                Log.d("HttpDownloader", "Finish download image form : " + s);
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            return decodeStream;
        }
        catch (IOException ex) {
            Log.e("HttpDownloader", "downloadDrawableFormUrl error! URL : " + s, (Throwable)ex);
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            return decodeStream;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }

            return null;
        }
    }

    public static Drawable downloadDrawableFormUrl(final String s) {
        Object o = null;
        if (s == null) {
            return (Drawable)o;
        }
        final boolean equals = s.trim().equals("");
        o = null;
        if (equals) {
            return (Drawable)o;
        }
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection)new URL(s).openConnection();
            httpURLConnection.setConnectTimeout(10000);
            final int responseCode = httpURLConnection.getResponseCode();
            o = null;
            if (responseCode == 200) {
                Log.d("HttpDownloader", "Start download image form : " + s);
                new BitmapFactory.Options().inJustDecodeBounds = true;
                final Bitmap decodeStream = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                Log.d("HttpDownloader", "bitmap w h :" + decodeStream.getWidth() + " * " + decodeStream.getHeight());
                final Bitmap bitmap = Bitmap.createBitmap(decodeStream.getWidth(), decodeStream.getHeight(), Bitmap.Config.ARGB_4444);
                final Canvas canvas = new Canvas(bitmap);
                final Paint paint = new Paint();
                paint.setDither(true);
                paint.setFilterBitmap(true);
                paint.setColor(-7829368);
                paint.setAntiAlias(true);
                canvas.drawARGB(0, 0, 0, 0);
                final Rect rect = new Rect(0, 0, decodeStream.getWidth(), decodeStream.getHeight());
                canvas.drawBitmap(decodeStream, rect, rect, paint);
                o = new BitmapDrawable(bitmap);
                ((Drawable)o).setBounds(0, 0, decodeStream.getWidth(), decodeStream.getHeight());
                decodeStream.recycle();
                Log.d("HttpDownloader", "Finish download image form : " + s);
            }
            return (Drawable)o;
        }
        catch (IOException ex) {
            Log.e("HttpDownloader", "downloadDrawableFormUrl error! URL : " + s, (Throwable)ex);
            return (Drawable)o;
        }
        finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }

    public void chanceStatus(final int mStatus) {
        this.mStatus = mStatus;
    }

    public int downloadFile(final String s, final File file) {
        return this.downloadFile(s, file, 65536);
    }

    public int downloadFile(final String p0, final File p1, final int p2) {
        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     0: iconst_0
        //     1: istore          4
        //     3: aload_1
        //     4: ifnull          200
        //     7: aload_1
        //     8: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    11: ldc             ""
        //    13: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    16: istore          5
        //    18: iconst_0
        //    19: istore          4
        //    21: iload           5
        //    23: ifne            200
        //    26: aconst_null
        //    27: astore          6
        //    29: aconst_null
        //    30: astore          7
        //    32: aconst_null
        //    33: astore          8
        //    35: new             Ljava/net/URL;
        //    38: dup
        //    39: aload_1
        //    40: invokespecial   java/net/URL.<init>:(Ljava/lang/String;)V
        //    43: invokevirtual   java/net/URL.openConnection:()Ljava/net/URLConnection;
        //    46: checkcast       Ljava/net/HttpURLConnection;
        //    49: astore          6
        //    51: aload           6
        //    53: sipush          10000
        //    56: invokevirtual   java/net/HttpURLConnection.setConnectTimeout:(I)V
        //    59: aload           6
        //    61: invokevirtual   java/net/HttpURLConnection.getResponseCode:()I
        //    64: istore          17
        //    66: aconst_null
        //    67: astore          7
        //    69: aconst_null
        //    70: astore          8
        //    72: iconst_0
        //    73: istore          4
        //    75: iload           17
        //    77: sipush          200
        //    80: if_icmpne       170
        //    83: iload_3
        //    84: newarray        B
        //    86: astore          18
        //    88: new             Ljava/io/BufferedInputStream;
        //    91: dup
        //    92: aload           6
        //    94: invokevirtual   java/net/HttpURLConnection.getInputStream:()Ljava/io/InputStream;
        //    97: iload_3
        //    98: invokespecial   java/io/BufferedInputStream.<init>:(Ljava/io/InputStream;I)V
        //   101: astore          19
        //   103: new             Ljava/io/BufferedOutputStream;
        //   106: dup
        //   107: new             Ljava/io/FileOutputStream;
        //   110: dup
        //   111: aload_2
        //   112: invokespecial   java/io/FileOutputStream.<init>:(Ljava/io/File;)V
        //   115: iload_3
        //   116: invokespecial   java/io/BufferedOutputStream.<init>:(Ljava/io/OutputStream;I)V
        //   119: astore          20
        //   121: aload_0
        //   122: getfield        com/Infree/MeetingNetwork/win/winutil/HttpDownloader.mStatus:I
        //   125: iconst_1
        //   126: if_icmpeq       203
        //   129: aload_0
        //   130: getfield        com/Infree/MeetingNetwork/win/winutil/HttpDownloader.mStatus:I
        //   133: iconst_3
        //   134: if_icmpne       428
        //   137: iconst_1
        //   138: istore          4
        //   140: ldc             "HttpDownloader"
        //   142: new             Ljava/lang/StringBuilder;
        //   145: dup
        //   146: ldc             "URL:"
        //   148: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   151: aload_1
        //   152: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   155: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   158: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   161: pop
        //   162: aload           20
        //   164: astore          8
        //   166: aload           19
        //   168: astore          7
        //   170: aload           8
        //   172: ifnull          180
        //   175: aload           8
        //   177: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   180: aload           7
        //   182: ifnull          190
        //   185: aload           7
        //   187: invokevirtual   java/io/BufferedInputStream.close:()V
        //   190: aload           6
        //   192: ifnull          200
        //   195: aload           6
        //   197: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   200: iload           4
        //   202: ireturn
        //   203: aload           19
        //   205: aload           18
        //   207: invokevirtual   java/io/BufferedInputStream.read:([B)I
        //   210: istore          21
        //   212: iload           21
        //   214: iconst_m1
        //   215: if_icmpeq       329
        //   218: aload           20
        //   220: aload           18
        //   222: iconst_0
        //   223: iload           21
        //   225: invokevirtual   java/io/BufferedOutputStream.write:([BII)V
        //   228: aload           20
        //   230: invokevirtual   java/io/BufferedOutputStream.flush:()V
        //   233: goto            121
        //   236: astore          9
        //   238: aload           20
        //   240: astore          8
        //   242: aload           19
        //   244: astore          7
        //   246: ldc             "HttpDownloader"
        //   248: new             Ljava/lang/StringBuilder;
        //   251: dup
        //   252: ldc             "downloadFile error - url illeagal! URL : "
        //   254: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   257: aload_1
        //   258: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   261: ldc             "-error:"
        //   263: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   266: aload           9
        //   268: invokevirtual   java/net/MalformedURLException.getMessage:()Ljava/lang/String;
        //   271: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   274: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   277: aload           9
        //   279: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   282: pop
        //   283: iconst_m1
        //   284: istore          4
        //   286: aload           8
        //   288: ifnull          296
        //   291: aload           8
        //   293: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   296: aload           7
        //   298: ifnull          306
        //   301: aload           7
        //   303: invokevirtual   java/io/BufferedInputStream.close:()V
        //   306: aload           6
        //   308: ifnull          200
        //   311: aload           6
        //   313: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   316: iload           4
        //   318: ireturn
        //   319: astore          13
        //   321: aload           13
        //   323: invokevirtual   java/io/IOException.printStackTrace:()V
        //   326: iload           4
        //   328: ireturn
        //   329: aload_0
        //   330: iconst_3
        //   331: putfield        com/Infree/MeetingNetwork/win/winutil/HttpDownloader.mStatus:I
        //   334: goto            121
        //   337: astore          14
        //   339: aload           20
        //   341: astore          8
        //   343: aload           19
        //   345: astore          7
        //   347: ldc             "HttpDownloader"
        //   349: new             Ljava/lang/StringBuilder;
        //   352: dup
        //   353: ldc             "downloadFile error - connect failed! URL : "
        //   355: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   358: aload_1
        //   359: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   362: ldc             "-error:"
        //   364: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   367: aload           14
        //   369: invokevirtual   java/io/IOException.getMessage:()Ljava/lang/String;
        //   372: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   375: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   378: aload           14
        //   380: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Throwable;)I
        //   383: pop
        //   384: aload           8
        //   386: ifnull          394
        //   389: aload           8
        //   391: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   394: aload           7
        //   396: ifnull          404
        //   399: aload           7
        //   401: invokevirtual   java/io/BufferedInputStream.close:()V
        //   404: iconst_0
        //   405: istore          4
        //   407: aload           6
        //   409: ifnull          200
        //   412: aload           6
        //   414: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   417: iconst_0
        //   418: ireturn
        //   419: astore          16
        //   421: aload           16
        //   423: invokevirtual   java/io/IOException.printStackTrace:()V
        //   426: iconst_0
        //   427: ireturn
        //   428: iconst_2
        //   429: istore          4
        //   431: goto            140
        //   434: astore          10
        //   436: aload           8
        //   438: ifnull          446
        //   441: aload           8
        //   443: invokevirtual   java/io/BufferedOutputStream.close:()V
        //   446: aload           7
        //   448: ifnull          456
        //   451: aload           7
        //   453: invokevirtual   java/io/BufferedInputStream.close:()V
        //   456: aload           6
        //   458: ifnull          466
        //   461: aload           6
        //   463: invokevirtual   java/net/HttpURLConnection.disconnect:()V
        //   466: aload           10
        //   468: athrow
        //   469: astore          11
        //   471: aload           11
        //   473: invokevirtual   java/io/IOException.printStackTrace:()V
        //   476: goto            466
        //   479: astore          23
        //   481: aload           23
        //   483: invokevirtual   java/io/IOException.printStackTrace:()V
        //   486: iload           4
        //   488: ireturn
        //   489: astore          10
        //   491: aload           19
        //   493: astore          7
        //   495: aconst_null
        //   496: astore          8
        //   498: goto            436
        //   501: astore          10
        //   503: aload           20
        //   505: astore          8
        //   507: aload           19
        //   509: astore          7
        //   511: goto            436
        //   514: astore          14
        //   516: aconst_null
        //   517: astore          7
        //   519: aconst_null
        //   520: astore          8
        //   522: goto            347
        //   525: astore          14
        //   527: aload           19
        //   529: astore          7
        //   531: aconst_null
        //   532: astore          8
        //   534: goto            347
        //   537: astore          9
        //   539: aconst_null
        //   540: astore          7
        //   542: aconst_null
        //   543: astore          8
        //   545: goto            246
        //   548: astore          9
        //   550: aload           19
        //   552: astore          7
        //   554: aconst_null
        //   555: astore          8
        //   557: goto            246
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  --------------------------------
        //  35     66     537    548    Ljava/net/MalformedURLException;
        //  35     66     514    525    Ljava/io/IOException;
        //  35     66     434    436    Any
        //  83     103    537    548    Ljava/net/MalformedURLException;
        //  83     103    514    525    Ljava/io/IOException;
        //  83     103    434    436    Any
        //  103    121    548    560    Ljava/net/MalformedURLException;
        //  103    121    525    537    Ljava/io/IOException;
        //  103    121    489    501    Any
        //  121    137    236    246    Ljava/net/MalformedURLException;
        //  121    137    337    347    Ljava/io/IOException;
        //  121    137    501    514    Any
        //  140    162    236    246    Ljava/net/MalformedURLException;
        //  140    162    337    347    Ljava/io/IOException;
        //  140    162    501    514    Any
        //  175    180    479    489    Ljava/io/IOException;
        //  185    190    479    489    Ljava/io/IOException;
        //  195    200    479    489    Ljava/io/IOException;
        //  203    212    236    246    Ljava/net/MalformedURLException;
        //  203    212    337    347    Ljava/io/IOException;
        //  203    212    501    514    Any
        //  218    233    236    246    Ljava/net/MalformedURLException;
        //  218    233    337    347    Ljava/io/IOException;
        //  218    233    501    514    Any
        //  246    283    434    436    Any
        //  291    296    319    329    Ljava/io/IOException;
        //  301    306    319    329    Ljava/io/IOException;
        //  311    316    319    329    Ljava/io/IOException;
        //  329    334    236    246    Ljava/net/MalformedURLException;
        //  329    334    337    347    Ljava/io/IOException;
        //  329    334    501    514    Any
        //  347    384    434    436    Any
        //  389    394    419    428    Ljava/io/IOException;
        //  399    404    419    428    Ljava/io/IOException;
        //  412    417    419    428    Ljava/io/IOException;
        //  441    446    469    479    Ljava/io/IOException;
        //  451    456    469    479    Ljava/io/IOException;
        //  461    466    469    479    Ljava/io/IOException;
        //
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }

    public int downloadFile(final String s, final String s2) {
        final File file = new File(s2);
        Label_0029: {
            if (file.exists()) {
                break Label_0029;
            }
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return this.downloadFile(s, file, 65536);
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return this.downloadFile(s, file, 65536);
            }
        }
    }

    public void release() {
        DownloaderManager.getInstance().cancelDownloader(this.hashCode());
    }
}
