package com.sam.ebrand.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.sam.ebrand.brower.ImageBrowser;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SubLcdManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sam on 2016/11/10.
 */
public class FileUtils {
        public static String CurrentOpenPath;
        public static int OPEN_MODE = 0;
        public static final int OPEN_NORMAL = 0;
        public static final int OPEN_WITH_THIRD = 1;
        public String SDPATH;

        static {
            FileUtils.OPEN_MODE = 0;
            FileUtils.CurrentOpenPath = "";
        }

        public FileUtils() {
            this.SDPATH = Environment.getExternalStorageDirectory() + "/";
        }

        public static byte[] Bitmap2BytesJPG(final Bitmap bitmap) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, (OutputStream)byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }

        public static byte[] Bitmap2BytesPNG(final Bitmap bitmap) {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, (OutputStream)byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }

        public static void BrowserPicture(final Context context, final String s) {
            if (s != null && s != "") {
                File file;
                try {
                    file = new File(s);
                    if (!file.exists()) {
                        return;
                    }
                    if (file.isDirectory()) {
                        final Bundle bundle = new Bundle();
                        bundle.putString("FileName", s);
                        final Intent intent = new Intent(context, ImageBrowser.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                        return;
                    }
                }
                catch (Exception ex) {
                    ex.fillInStackTrace();
                    return;
                }
                final String lowerCase = file.getName().substring(1 + file.getName().lastIndexOf("."), file.getName().length()).toLowerCase();
                if (lowerCase.equals("jpg") || lowerCase.equals("gif") || lowerCase.equals("png") || lowerCase.equals("jpeg") || lowerCase.equals("bmp")) {
                    final Bundle bundle2 = new Bundle();
                    bundle2.putString("FileName", s);
                    final Intent intent2 = new Intent(context, (Class)ImageBrowser.class);
                    intent2.putExtras(bundle2);
                    context.startActivity(intent2);
                }
            }
        }

        public static Bitmap Bytes2Bimap(final byte[] array) {
            if (array.length != 0) {
                return BitmapFactory.decodeByteArray(array, 0, array.length);
            }
            return null;
        }

        public static boolean CopyFile(final String s, final String s2) {
            final File file = new File(s2);
            final File file2 = new File(s);
            if (file2.isDirectory()) {
                return false;
            }
            boolean b;
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                final FileInputStream fileInputStream = new FileInputStream(file2);
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = fileInputStream.read(array);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(array, 0, read);
                }
                fileOutputStream.flush();
                fileInputStream.close();
                fileOutputStream.close();
                b = true;
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                Log.e("FileCopy", "error:" + ex.getMessage());
                b = false;
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
                Log.e("FileCopy", "error:" + ex2.getMessage());
                b = false;
            }
            return b;
        }

        public static void DeleteRecursive(final File file) {
            if (file.isDirectory()) {
                final File[] listFiles = file.listFiles();
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    DeleteRecursive(listFiles[i]);
                }
            }
            file.delete();
        }

        public static List<String> GetImageFiles(final String s) {
            final File[] listFiles = new File(s).listFiles();
            List<String> list;
            if (listFiles == null || listFiles.length == 0) {
                list = null;
            }
            else {
                list = new ArrayList<String>();
                for (int i = 0; i < listFiles.length; ++i) {
                    final File file = listFiles[i];
                    if (file.isFile()) {
                        final String name = file.getName();
                        final int lastIndex = name.lastIndexOf(".");
                        if (lastIndex >= 0) {
                            final String lowerCase = name.substring(lastIndex, name.length()).toLowerCase();
                            if (lowerCase != "" && (lowerCase.equals(".jpg") || lowerCase.equals(".jpeg") || lowerCase.equals(".bmp") || lowerCase.equals(".png"))) {
                                list.add(file.getPath());
                            }
                        }
                    }
                }
            }
            return list;
        }

        public static List<FileItem> GetImageFiles(final List<FileItem> list) {
            List<FileItem> list2;
            if (list == null || list.size() == 0) {
                list2 = null;
            }
            else {
                list2 = new ArrayList<FileItem>();
                list2.clear();
                for (int size = list.size(), i = 0; i < size; ++i) {
                    final FileItem fileItem = list.get(i);
                    if (fileItem.getFiletype() == 2) {
                        list2.add(fileItem);
                    }
                }
            }
            return list2;
        }

        public static Drawable GetNewDrawable(final Drawable drawable, final Drawable drawable2) {
            if (drawable == null) {
                return drawable2;
            }
            final Drawable[] array = { drawable2, drawable };
            array[0].setAlpha(40);
            final LayerDrawable layerDrawable = new LayerDrawable(array);
            layerDrawable.setAlpha(255);
            return (Drawable)layerDrawable;
        }

        public static boolean MakeFolder(final String s) {
            if (!isFolderExist(s)) {
                creatDir(s);
            }
            return true;
        }

        public static boolean PngToBmp(final Context context, final int n, final String s) {
            synchronized (SubLcdManager.moperatorLock) {
                final Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), n);
                final int width = decodeResource.getWidth();
                final int height = decodeResource.getHeight();
                final int[] array = new int[width * height];
                decodeResource.getPixels(array, 0, width, 0, 0, width, height);
                final byte[] fillBmpRGB = fillBmpRGB(array, width, height);
                final byte[] fillBmpHeader = fillBmpHeader(fillBmpRGB.length);
                final byte[] fillBmpInfosHeader = fillBmpInfosHeader(width, height);
                final byte[] array2 = new byte[54 + fillBmpRGB.length];
                System.arraycopy(fillBmpHeader, 0, array2, 0, fillBmpHeader.length);
                System.arraycopy(fillBmpInfosHeader, 0, array2, 14, fillBmpInfosHeader.length);
                System.arraycopy(fillBmpRGB, 0, array2, 54, fillBmpRGB.length);
                try {
                    final File file = new File(s);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    bufferedOutputStream.write(array2);
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    return true;
                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    Log.e("PngToBmp", new StringBuilder().append(ex.getMessage()).toString());
                    SDFolderManager.createFolders();
                    return false;
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                    Log.e("PngToBmp", new StringBuilder().append(ex2.getMessage()).toString());
                    SDFolderManager.createFolders();
                    return false;
                }
            }
        }

        public static boolean PngToBmp(final Bitmap bitmap, final String s) {
            synchronized (SubLcdManager.moperatorLock) {
                final int width = bitmap.getWidth();
                final int height = bitmap.getHeight();
                final int[] array = new int[width * height];
                bitmap.getPixels(array, 0, width, 0, 0, width, height);
                final byte[] fillBmpRGB = fillBmpRGB(array, width, height);
                final byte[] fillBmpHeader = fillBmpHeader(fillBmpRGB.length);
                final byte[] fillBmpInfosHeader = fillBmpInfosHeader(width, height);
                final byte[] array2 = new byte[54 + fillBmpRGB.length];
                System.arraycopy(fillBmpHeader, 0, array2, 0, fillBmpHeader.length);
                System.arraycopy(fillBmpInfosHeader, 0, array2, 14, fillBmpInfosHeader.length);
                System.arraycopy(fillBmpRGB, 0, array2, 54, fillBmpRGB.length);
                try {
                    final File file = new File(s);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    final BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    bufferedOutputStream.write(array2);
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    if (bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    return true;
                }
                catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                    Log.e("PngToBmp", new StringBuilder().append(ex.getMessage()).toString());
                    SDFolderManager.createFolders();
                    return false;
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                    Log.e("PngToBmp", new StringBuilder().append(ex2.getMessage()).toString());
                    SDFolderManager.createFolders();
                    return false;
                }
            }
        }

        public static boolean SalfCreateDir(final String p0) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     0: new             Ljava/io/File;
            //     3: dup
            //     4: aload_0
            //     5: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
            //     8: astore_1
            //     9: aload_1
            //    10: invokevirtual   java/io/File.exists:()Z
            //    13: ifne            25
            //    16: aload_1
            //    17: invokevirtual   java/io/File.mkdirs:()Z
            //    20: istore          4
            //    22: iload           4
            //    24: ireturn
            //    25: iconst_1
            //    26: ireturn
            //    27: astore_2
            //    28: ldc_w           "CreateDir"
            //    31: new             Ljava/lang/StringBuilder;
            //    34: dup
            //    35: invokespecial   java/lang/StringBuilder.<init>:()V
            //    38: aload_2
            //    39: invokevirtual   java/lang/Exception.getMessage:()Ljava/lang/String;
            //    42: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    45: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    48: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
            //    51: pop
            //    52: iconst_0
            //    53: ireturn
            //    54: astore_2
            //    55: goto            28
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ---------------------
            //  0      9      27     28     Ljava/lang/Exception;
            //  9      22     54     58     Ljava/lang/Exception;
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        private static byte[] addBMPImageHeader(final int n) {
            return new byte[] { 66, 77, (byte)(n >> 0), (byte)(n >> 8), (byte)(n >> 16), (byte)(n >> 24), 0, 0, 0, 0, 54, 0, 0, 0 };
        }

        private static byte[] addBMPImageInfosHeader(final int n, final int n2) {
            return new byte[] { 40, 0, 0, 0, (byte)(n >> 0), (byte)(n >> 8), (byte)(n >> 16), (byte)(n >> 24), (byte)(n2 >> 0), (byte)(n2 >> 8), (byte)(n2 >> 16), (byte)(n2 >> 24), 1, 0, 24, 0, 0, 0, 0, 0, 0, 0, 0, 0, -32, 1, 0, 0, 2, 3, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        }

        private static byte[] addBMP_RGB_888(final int[] array, final int n, final int n2) {
            final int length = array.length;
            System.out.println(array.length);
            final byte[] array2 = new byte[3 * (n * n2)];
            int n3 = 0;
            for (int i = length - 1; i >= 0; i -= n) {
                for (int n4 = i, j = 1 + (i - n); j <= n4; ++j) {
                    array2[n3] = (byte)(array[j] >> 0);
                    array2[n3 + 1] = (byte)(array[j] >> 8);
                    array2[n3 + 2] = (byte)(array[j] >> 16);
                    n3 += 3;
                }
            }
            return array2;
        }

        private static int computeInitialSampleSize(final BitmapFactory.Options bitmapFactory$Options, final int n, final int n2) {
            final double n3 = bitmapFactory$Options.outWidth;
            final double n4 = bitmapFactory$Options.outHeight;
            int n5;
            if (n2 == -1) {
                n5 = 1;
            }
            else {
                n5 = (int)Math.ceil(Math.sqrt(n3 * n4 / n2));
            }
            int n6;
            if (n == -1) {
                n6 = 128;
            }
            else {
                n6 = (int)Math.min(Math.floor(n3 / n), Math.floor(n4 / n));
            }
            if (n6 >= n5) {
                if (n2 == -1 && n == -1) {
                    return 1;
                }
                if (n != -1) {
                    return n6;
                }
            }
            return n5;
        }

        private static int computeSampleSize(final BitmapFactory.Options bitmapFactory$Options, final int n, final int n2) {
            final int computeInitialSampleSize = computeInitialSampleSize(bitmapFactory$Options, n, n2);
            if (computeInitialSampleSize <= 8) {
                int i;
                for (i = 1; i < computeInitialSampleSize; i <<= 1) {}
                return i;
            }
            return 8 * ((computeInitialSampleSize + 7) / 8);
        }

        public static boolean copyAssetToSDCard(final Context context, final String s, final String s2) {
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(s);
                final InputStream open = context.getAssets().open(s2);
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = open.read(array);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(array, 0, read);
                }
                fileOutputStream.flush();
                open.close();
                fileOutputStream.close();
                return true;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                Log.e("Copy file", "exception:" + ex.getMessage());
                return false;
            }
        }

        public static File creatDir(final String s) {
            File file = null;
            File file2 = null;
            try {
                final File file3;
                file2 = (file3 = new File(s));
                final boolean b = file3.exists();
                if (!b) {
                    final File file4 = file2;
                    file4.mkdirs();
                    final String s2 = "FileUtils";
                    final String s3 = "\u521b\u5efaSD\u5361\u6587\u4ef6\u5939\u6210\u529f: ";
                    final StringBuilder sb = new StringBuilder(s3);
                    final String s4 = s;
                    final StringBuilder sb2 = sb.append(s4);
                    final String s5 = sb2.toString();
                    Log.e(s2, s5);
                    return file2;
                }
                return file2;
            }
            catch (Exception ex2) {}
            while (true) {
                try {
                    final File file3 = file2;
                    final boolean b = file3.exists();
                    if (!b) {
                        final File file4 = file2;
                        file4.mkdirs();
                        final String s2 = "FileUtils";
                        final String s3 = "\u521b\u5efaSD\u5361\u6587\u4ef6\u5939\u6210\u529f: ";
                        final StringBuilder sb = new StringBuilder(s3);
                        final String s4 = s;
                        final StringBuilder sb2 = sb.append(s4);
                        final String s5 = sb2.toString();
                        Log.e(s2, s5);
                        return file2;
                    }
                    return file2;
                    final Exception ex;
                    Log.e("FileUtils", "\u521b\u5efaSD\u5361\u6587\u4ef6\u5939\u5931\u8d25: " + ex.getMessage());
                    ex.printStackTrace();
                    return file;
                }
                catch (Exception ex) {
                    file = file2;
                    continue;
                }
                break;
            }
            return file2;
        }

        public static Bitmap decodeFile(final String p0) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     0: new             Landroid/graphics/BitmapFactory$Options;
            //     3: dup
            //     4: invokespecial   android/graphics/BitmapFactory$Options.<init>:()V
            //     7: astore_1
            //     8: aload_1
            //     9: iconst_0
            //    10: putfield        android/graphics/BitmapFactory$Options.inDither:Z
            //    13: aload_1
            //    14: iconst_1
            //    15: putfield        android/graphics/BitmapFactory$Options.inPurgeable:Z
            //    18: aload_1
            //    19: sipush          12288
            //    22: newarray        B
            //    24: putfield        android/graphics/BitmapFactory$Options.inTempStorage:[B
            //    27: new             Ljava/io/File;
            //    30: dup
            //    31: aload_0
            //    32: invokespecial   java/io/File.<init>:(Ljava/lang/String;)V
            //    35: astore_2
            //    36: new             Ljava/io/FileInputStream;
            //    39: dup
            //    40: aload_2
            //    41: invokespecial   java/io/FileInputStream.<init>:(Ljava/io/File;)V
            //    44: astore_3
            //    45: aload_3
            //    46: astore          4
            //    48: aconst_null
            //    49: astore          5
            //    51: aload           4
            //    53: ifnull          82
            //    56: aload           4
            //    58: invokevirtual   java/io/FileInputStream.getFD:()Ljava/io/FileDescriptor;
            //    61: aconst_null
            //    62: aload_1
            //    63: invokestatic    android/graphics/BitmapFactory.decodeFileDescriptor:(Ljava/io/FileDescriptor;Landroid/graphics/Rect;Landroid/graphics/BitmapFactory$Options;)Landroid/graphics/Bitmap;
            //    66: astore          10
            //    68: aload           10
            //    70: astore          5
            //    72: aload           4
            //    74: ifnull          82
            //    77: aload           4
            //    79: invokevirtual   java/io/FileInputStream.close:()V
            //    82: aload           5
            //    84: areturn
            //    85: astore          12
            //    87: aload           12
            //    89: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
            //    92: aconst_null
            //    93: astore          4
            //    95: goto            48
            //    98: astore          8
            //   100: aload           8
            //   102: invokevirtual   java/io/IOException.printStackTrace:()V
            //   105: aconst_null
            //   106: astore          5
            //   108: aload           4
            //   110: ifnull          82
            //   113: aload           4
            //   115: invokevirtual   java/io/FileInputStream.close:()V
            //   118: aconst_null
            //   119: areturn
            //   120: astore          9
            //   122: aload           9
            //   124: invokevirtual   java/io/IOException.printStackTrace:()V
            //   127: aconst_null
            //   128: areturn
            //   129: astore          6
            //   131: aload           4
            //   133: ifnull          141
            //   136: aload           4
            //   138: invokevirtual   java/io/FileInputStream.close:()V
            //   141: aload           6
            //   143: athrow
            //   144: astore          7
            //   146: aload           7
            //   148: invokevirtual   java/io/IOException.printStackTrace:()V
            //   151: goto            141
            //   154: astore          11
            //   156: aload           11
            //   158: invokevirtual   java/io/IOException.printStackTrace:()V
            //   161: aload           5
            //   163: areturn
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  -------------------------------
            //  36     45     85     98     Ljava/io/FileNotFoundException;
            //  56     68     98     129    Ljava/io/IOException;
            //  56     68     129    154    Any
            //  77     82     154    164    Ljava/io/IOException;
            //  100    105    129    154    Any
            //  113    118    120    129    Ljava/io/IOException;
            //  136    141    144    154    Ljava/io/IOException;
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        public static Bitmap decodeFileZoom(final String s, final int n, final int n2) {
            final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(s, bitmapFactory$Options);
            bitmapFactory$Options.inSampleSize = computeSampleSize(bitmapFactory$Options, -1, n * n2);
            bitmapFactory$Options.inJustDecodeBounds = false;
            try {
                return BitmapFactory.decodeFile(s, bitmapFactory$Options);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                return null;
            }
        }

        public static void delAllFile(final String s) {
            final File file = new File(s);
            if (file.exists() && file.isDirectory()) {
                final String[] list = file.list();
                for (int i = 0; i < list.length; ++i) {
                    File file2;
                    if (s.endsWith(File.separator)) {
                        file2 = new File(String.valueOf(s) + list[i]);
                    }
                    else {
                        file2 = new File(String.valueOf(s) + File.separator + list[i]);
                    }
                    if (file2.isFile()) {
                        file2.delete();
                    }
                    if (file2.isDirectory()) {
                        delAllFile(String.valueOf(s) + "/" + list[i]);
                    }
                }
            }
        }

        public static void deletApk(final Context context, final String s) {
            context.startActivity(new Intent("android.intent.action.DELETE", Uri.parse("package:" + s)));
        }

        public static File deleteSDFileFull(final String s) throws IOException {
            final File file = new File(s);
            file.delete();
            return file;
        }

        public static Bitmap drawableToBitmap(final Drawable drawable) {
            final int intrinsicWidth = drawable.getIntrinsicWidth();
            final int intrinsicHeight = drawable.getIntrinsicHeight();
            Bitmap.Config bitmap$Config;
            if (drawable.getOpacity() != -1) {
                bitmap$Config = Bitmap.Config.ARGB_8888;
            }
            else {
                bitmap$Config = Bitmap.Config.RGB_565;
            }
            final Bitmap bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, bitmap$Config);
            final Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            drawable.draw(canvas);
            return bitmap;
        }

        public static String fetchStr(final String s, final String s2) {
            Object string;
            if (s == null) {
                string = null;
            }
            else {
                int i = 0;
                string = "";
                boolean b = false;
                while (i < s.length()) {
                    final char char1 = s.charAt(i);
                    if (new StringBuilder().append(char1).toString().equals(s2)) {
                        b = true;
                    }
                    if (b) {
                        string = String.valueOf(string) + char1;
                    }
                    ++i;
                }
            }
            return (String)string;
        }

        private static byte[] fillBmpHeader(final int n) {
            return new byte[] { 66, 77, (byte)(n & 0xFF), (byte)(0xFF & n >> 8), (byte)(0xFF & n >> 16), (byte)(0xFF & n >> 24), 0, 0, 0, 0, 54, 0, 0, 0 };
        }

        private static byte[] fillBmpInfosHeader(final int n, final int n2) {
            final byte[] array = new byte[40];
            Arrays.fill(array, (byte)0);
            array[0] = 40;
            array[4] = (byte)(n & 0xFF);
            array[5] = (byte)(0xFF & n >> 8);
            array[6] = (byte)(0xFF & n >> 16);
            array[7] = (byte)(0xFF & n >> 24);
            array[8] = (byte)(n2 & 0xFF);
            array[9] = (byte)(0xFF & n2 >> 8);
            array[10] = (byte)(0xFF & n2 >> 16);
            array[11] = (byte)(0xFF & n2 >> 24);
            array[12] = 1;
            array[14] = 24;
            array[24] = -32;
            array[25] = 1;
            array[28] = 2;
            array[29] = 3;
            return array;
        }

        private static byte[] fillBmpRGB(final int[] array, final int n, final int n2) {
            final int length = array.length;
            final byte[] array2 = new byte[3 * (n * n2)];
            int n3 = 0;
            for (int i = length - 1; i >= 0; i -= n) {
                for (int n4 = i, j = 1 + (i - n); j <= n4; ++j) {
                    array2[n3] = (byte)(0xFF & array[j]);
                    array2[n3 + 1] = (byte)(0xFF & array[j] >> 8);
                    array2[n3 + 2] = (byte)(0xFF & array[j] >> 16);
                    n3 += 3;
                }
            }
            return array2;
        }

        public static Intent getAllIntent(final String s) {
            final Intent intent = new Intent();
            intent.addFlags(268435456);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(s)), "*/*");
            return intent;
        }

        public static Intent getApkFileIntent(final String s) {
            final Intent intent = new Intent();
            intent.addFlags(268435456);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(s)), "application/vnd.android.package-archive");
            return intent;
        }

        public static Intent getAudioFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(67108864);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            intent.setDataAndType(Uri.fromFile(new File(s)), "audio/*");
            return intent;
        }

        public static Bitmap getBitmapBySDPath(final String s, final int inSampleSize) {
            if (inSampleSize <= 1) {
                return BitmapFactory.decodeFile(s);
            }
            final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
            bitmapFactory$Options.inSampleSize = inSampleSize;
            return BitmapFactory.decodeFile(s, bitmapFactory$Options);
        }

        public static Intent getChmFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(270532608);
            intent.setDataAndType(Uri.fromFile(new File(s)), "application/x-chm");
            return intent;
        }

        public static Intent getExcelFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(270532608);
            intent.setDataAndType(Uri.fromFile(new File(s)), "application/vnd.ms-excel");
            return intent;
        }

        private static Intent getFileIntent(final Context context, final String currentOpenPath) {
            final File file = new File(currentOpenPath);
            if (!file.exists()) {
                return null;
            }
            FileUtils.OPEN_MODE = 1;
            FileUtils.CurrentOpenPath = currentOpenPath;
            if (file.isDirectory()) {
                final Bundle bundle = new Bundle();
                bundle.putString("FileName", currentOpenPath);
                final Intent intent = new Intent(context, (Class)ImageBrowser.class);
                intent.putExtras(bundle);
                FileUtils.OPEN_MODE = 0;
                FileUtils.CurrentOpenPath = "";
                return intent;
            }
            final String lowerCase = file.getName().substring(1 + file.getName().lastIndexOf("."), file.getName().length()).toLowerCase();
            if (lowerCase.equals("m4a") || lowerCase.equals("mp3") || lowerCase.equals("mid") || lowerCase.equals("xmf") || lowerCase.equals("ogg") || lowerCase.equals("wav")) {
                return getAudioFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("3gp") || lowerCase.equals("mp4")) {
                return getVideoFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("jpg") || lowerCase.equals("gif") || lowerCase.equals("png") || lowerCase.equals("jpeg") || lowerCase.equals("bmp")) {
                return getImageFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("apk")) {
                return getApkFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("ppt") || lowerCase.equals("pptx")) {
                return getPptFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("xls") || lowerCase.equals("xlsx")) {
                return getExcelFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("doc") || lowerCase.equals("docx")) {
                return getWordFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("pdf")) {
                return getPdfFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("chm")) {
                return getChmFileIntent(currentOpenPath);
            }
            if (lowerCase.equals("txt")) {
                return getTextFileIntent(currentOpenPath, false);
            }
            return getAllIntent(currentOpenPath);
        }

        public static String getFileNameFromFullPath(final String s) {
            return s.substring(1 + s.lastIndexOf("/"));
        }

        public static Intent getHtmlFileIntent(final String s) {
            final Uri build = Uri.parse(s).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(s).build();
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(build, "text/html");
            return intent;
        }

        public static Intent getImageFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(270532608);
            intent.setDataAndType(Uri.fromFile(new File(s)), "image/*");
            return intent;
        }

        public static String getMIMEType(final File file) {
            final String lowerCase = file.getName().substring(1 + file.getName().lastIndexOf("."), file.getName().length()).toLowerCase();
            String s;
            if (lowerCase.equals("mp3") || lowerCase.equals("aac") || lowerCase.equals("aac") || lowerCase.equals("amr") || lowerCase.equals("mpeg") || lowerCase.equals("mp4")) {
                s = "audio";
            }
            else if (lowerCase.equals("jpg") || lowerCase.equals("gif") || lowerCase.equals("png") || lowerCase.equals("jpeg")) {
                s = "image";
            }
            else {
                s = "*";
            }
            return String.valueOf(s) + "/*";
        }

        public static Intent getPdfFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(270532608);
            intent.setDataAndType(Uri.fromFile(new File(s)), "application/pdf");
            return intent;
        }

        public static Intent getPptFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(270532608);
            intent.setDataAndType(Uri.fromFile(new File(s)), "application/vnd.ms-powerpoint");
            return intent;
        }

        public static Intent getTextFileIntent(final String s, final boolean b) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(270532608);
            if (b) {
                intent.setDataAndType(Uri.parse(s), "text/plain");
                return intent;
            }
            intent.setDataAndType(Uri.fromFile(new File(s)), "text/plain");
            return intent;
        }

        public static Intent getVideoFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addFlags(67108864);
            intent.putExtra("oneshot", 0);
            intent.putExtra("configchange", 0);
            intent.setDataAndType(Uri.fromFile(new File(s)), "video/*");
            return intent;
        }

        public static Intent getWordFileIntent(final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(268435456);
            intent.setDataAndType(Uri.fromFile(new File(s)), "application/msword");
            return intent;
        }

        public static void insetApk(final Context context, final String s) {
            final Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.parse("file://" + s), "application/vnd.android.package-archive");
            context.startActivity(intent);
        }

        public static boolean isFileExistFullPath(final String s) {
            return new File(s).exists();
        }

        public static boolean isFolderExist(final String s) {
            return new File(s).exists();
        }

        public static void openFile(final Context context, final String s) {
            if (s == null || s == "") {
                return;
            }
            try {
                context.startActivity(getFileIntent(context, s));
            }
            catch (Exception ex) {
                ex.fillInStackTrace();
            }
        }

        public static byte[] readBytesFromFile(final String s) {
            try {
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(s));
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                System.out.println("Available bytes:" + bufferedInputStream.available());
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = bufferedInputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(array, 0, read);
                }
                bufferedInputStream.close();
                final byte[] byteArray = byteArrayOutputStream.toByteArray();
                System.out.println("Readed bytes count:" + byteArray.length);
                return byteArray;
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
                goto Label_0131;
            }
        }

        public static String[] readExcel(final File file) {
            Workbook workbook = null;
            try {
                final String s = (String) SettingManager.getInstance().readSetting("mgID", "", "");
                workbook = Workbook.getWorkbook(file);
                workbook.getNumberOfSheets();
                final Sheet sheet = workbook.getSheet(0);
                final int rows = sheet.getRows();
                final int columns = sheet.getColumns();
                Log.e("Excel", "\u603b\u884c\u6570\uff1a" + rows);
                Log.e("Excel", "\u603b\u5217\u6570\uff1a" + columns);
                for (int i = 1; i < rows; ++i) {
                    final String contents = sheet.getCell(0, i).getContents();
                    final String contents2 = sheet.getCell(1, i).getContents();
                    final String contents3 = sheet.getCell(2, i).getContents();
                    final String contents4 = sheet.getCell(3, i).getContents();
                    Log.e("Excel", "rows:" + i + ",ID:" + contents + ",Name:" + contents2 + ",Company:" + contents3 + ",Jobs:" + contents4);
                    if (contents.equals(s)) {
                        final String[] array = { contents2, contents3, contents4 };
                        workbook.close();
                        return array;
                    }
                }
                if (workbook != null) {
                    workbook.close();
                }
                return null;
            }
            catch (BiffException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
            finally {
                if (workbook != null) {
                    workbook.close();
                }
            }
        }

        public static String readFileContent(final String p0, final String p1) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     0: ldc             ""
            //     2: astore_2
            //     3: new             Ljava/io/BufferedReader;
            //     6: dup
            //     7: new             Ljava/io/InputStreamReader;
            //    10: dup
            //    11: new             Ljava/io/FileInputStream;
            //    14: dup
            //    15: aload_0
            //    16: invokespecial   java/io/FileInputStream.<init>:(Ljava/lang/String;)V
            //    19: aload_1
            //    20: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/lang/String;)V
            //    23: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
            //    26: astore_3
            //    27: aload_3
            //    28: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
            //    31: astore          8
            //    33: aload           8
            //    35: ifnonnull       44
            //    38: aload_3
            //    39: invokevirtual   java/io/BufferedReader.close:()V
            //    42: aload_2
            //    43: areturn
            //    44: new             Ljava/lang/StringBuilder;
            //    47: dup
            //    48: aload_2
            //    49: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
            //    52: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
            //    55: aload           8
            //    57: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    60: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    63: astore          9
            //    65: aload           9
            //    67: astore_2
            //    68: goto            27
            //    71: astore          4
            //    73: aload           4
            //    75: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
            //    78: ldc_w           "ych"
            //    81: ldc_w           "\u8bfb\u53d6\u6570\u636e\u62a5\u9519"
            //    84: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //    87: pop
            //    88: aconst_null
            //    89: areturn
            //    90: astore          6
            //    92: aload           6
            //    94: invokevirtual   java/io/IOException.printStackTrace:()V
            //    97: ldc_w           "ych"
            //   100: ldc_w           "\u8bfb\u53d6\u6570\u636e\u62a5\u9519"
            //   103: invokestatic    android/util/Log.d:(Ljava/lang/String;Ljava/lang/String;)I
            //   106: pop
            //   107: aconst_null
            //   108: areturn
            //   109: astore          6
            //   111: goto            92
            //   114: astore          4
            //   116: goto            73
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  -------------------------------
            //  3      27     71     73     Ljava/io/FileNotFoundException;
            //  3      27     90     92     Ljava/io/IOException;
            //  27     33     114    119    Ljava/io/FileNotFoundException;
            //  27     33     109    114    Ljava/io/IOException;
            //  38     42     114    119    Ljava/io/FileNotFoundException;
            //  38     42     109    114    Ljava/io/IOException;
            //  44     65     114    119    Ljava/io/FileNotFoundException;
            //  44     65     109    114    Ljava/io/IOException;
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        public static String readPrivateFile(final Context p0, final String p1, final String p2) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     0: new             Ljava/io/BufferedReader;
            //     3: dup
            //     4: new             Ljava/io/InputStreamReader;
            //     7: dup
            //     8: aload_0
            //     9: aload_1
            //    10: invokevirtual   android/content/Context.openFileInput:(Ljava/lang/String;)Ljava/io/FileInputStream;
            //    13: aload_2
            //    14: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;Ljava/lang/String;)V
            //    17: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
            //    20: astore_3
            //    21: ldc             ""
            //    23: astore          4
            //    25: aload_3
            //    26: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
            //    29: astore          7
            //    31: aload           7
            //    33: ifnonnull       43
            //    36: aload_3
            //    37: invokevirtual   java/io/BufferedReader.close:()V
            //    40: aload           4
            //    42: areturn
            //    43: new             Ljava/lang/StringBuilder;
            //    46: dup
            //    47: aload           4
            //    49: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
            //    52: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
            //    55: aload           7
            //    57: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    60: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //    63: astore          8
            //    65: aload           8
            //    67: astore          4
            //    69: goto            25
            //    72: astore          5
            //    74: aload           5
            //    76: invokevirtual   java/io/FileNotFoundException.printStackTrace:()V
            //    79: aconst_null
            //    80: areturn
            //    81: astore          6
            //    83: aload           6
            //    85: invokevirtual   java/io/IOException.printStackTrace:()V
            //    88: goto            79
            //    91: astore          6
            //    93: goto            83
            //    96: astore          5
            //    98: goto            74
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  -------------------------------
            //  0      21     72     74     Ljava/io/FileNotFoundException;
            //  0      21     81     83     Ljava/io/IOException;
            //  25     31     96     101    Ljava/io/FileNotFoundException;
            //  25     31     91     96     Ljava/io/IOException;
            //  36     40     96     101    Ljava/io/FileNotFoundException;
            //  36     40     91     96     Ljava/io/IOException;
            //  43     65     96     101    Ljava/io/FileNotFoundException;
            //  43     65     91     96     Ljava/io/IOException;
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        public static void removeFile(final String s) {
            if (!new File(s).exists()) {
                return;
            }
            final String string = "rm -r " + s;
            final Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(string);
            }
            catch (IOException ex) {}
        }

        public static void savePureBitmapToSDCard(final Bitmap bitmap, final String s) {
            if (bitmap == null) {
                return;
            }
            final int width = bitmap.getWidth();
            final int height = bitmap.getHeight();
            final int[] array = new int[width * height];
            bitmap.getPixels(array, 0, width, 0, 0, width, height);
            final byte[] addBMP_RGB_888 = addBMP_RGB_888(array, width, height);
            final byte[] addBMPImageHeader = addBMPImageHeader(addBMP_RGB_888.length);
            final byte[] addBMPImageInfosHeader = addBMPImageInfosHeader(width, height);
            final byte[] array2 = new byte[54 + addBMP_RGB_888.length];
            System.arraycopy(addBMPImageHeader, 0, array2, 0, addBMPImageHeader.length);
            System.arraycopy(addBMPImageInfosHeader, 0, array2, 14, addBMPImageInfosHeader.length);
            System.arraycopy(addBMP_RGB_888, 0, array2, 54, addBMP_RGB_888.length);
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(s);
                fileOutputStream.write(array2);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }

        public static void transferFile(final String s, final String s2) throws IOException {
            final FileInputStream fileInputStream = new FileInputStream(s);
            final FileOutputStream fileOutputStream = new FileOutputStream(s2);
            final FileChannel channel = fileInputStream.getChannel();
            final FileChannel channel2 = fileOutputStream.getChannel();
            final ByteBuffer allocate = ByteBuffer.allocate(1024);
            while (true) {
                allocate.clear();
                if (channel.read(allocate) == -1) {
                    break;
                }
                allocate.flip();
                channel2.write(allocate);
            }
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

        public static void transferFile(final String s, final String s2, final long n) throws IOException {
            final FileInputStream fileInputStream = new FileInputStream(s);
            final FileOutputStream fileOutputStream = new FileOutputStream(s2);
            final FileChannel channel = fileInputStream.getChannel();
            channel.transferTo(n, channel.size() - n, fileOutputStream.getChannel());
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }

        public static void transferFile2(final String p0, final String p1) {
            //
            // This method could not be decompiled.
            //
            // Original Bytecode:
            //
            //     0: aconst_null
            //     1: astore_2
            //     2: aconst_null
            //     3: astore_3
            //     4: aconst_null
            //     5: astore          4
            //     7: aconst_null
            //     8: astore          5
            //    10: new             Ljava/io/FileInputStream;
            //    13: dup
            //    14: aload_0
            //    15: invokespecial   java/io/FileInputStream.<init>:(Ljava/lang/String;)V
            //    18: astore          6
            //    20: new             Ljava/io/FileOutputStream;
            //    23: dup
            //    24: aload_1
            //    25: invokespecial   java/io/FileOutputStream.<init>:(Ljava/lang/String;)V
            //    28: astore          7
            //    30: aload           6
            //    32: invokevirtual   java/io/FileInputStream.getChannel:()Ljava/nio/channels/FileChannel;
            //    35: astore_2
            //    36: aload           7
            //    38: invokevirtual   java/io/FileOutputStream.getChannel:()Ljava/nio/channels/FileChannel;
            //    41: astore_3
            //    42: aload_2
            //    43: invokevirtual   java/nio/channels/FileChannel.position:()J
            //    46: lstore          12
            //    48: aload_2
            //    49: invokevirtual   java/nio/channels/FileChannel.size:()J
            //    52: lstore          14
            //    54: lload           12
            //    56: lload           14
            //    58: lcmp
            //    59: iflt            113
            //    62: aload_2
            //    63: ifnull          77
            //    66: aload_2
            //    67: invokevirtual   java/nio/channels/FileChannel.isOpen:()Z
            //    70: ifeq            77
            //    73: aload_2
            //    74: invokevirtual   java/nio/channels/FileChannel.close:()V
            //    77: aload_3
            //    78: ifnull          92
            //    81: aload_3
            //    82: invokevirtual   java/nio/channels/FileChannel.isOpen:()Z
            //    85: ifeq            92
            //    88: aload_3
            //    89: invokevirtual   java/nio/channels/FileChannel.close:()V
            //    92: aload           6
            //    94: ifnull          102
            //    97: aload           6
            //    99: invokevirtual   java/io/FileInputStream.close:()V
            //   102: aload           7
            //   104: ifnull          331
            //   107: aload           7
            //   109: invokevirtual   java/io/FileOutputStream.close:()V
            //   112: return
            //   113: aload_2
            //   114: invokevirtual   java/nio/channels/FileChannel.size:()J
            //   117: aload_2
            //   118: invokevirtual   java/nio/channels/FileChannel.position:()J
            //   121: lsub
            //   122: sipush          5120
            //   125: i2l
            //   126: lcmp
            //   127: ifge            251
            //   130: aload_2
            //   131: invokevirtual   java/nio/channels/FileChannel.size:()J
            //   134: aload_2
            //   135: invokevirtual   java/nio/channels/FileChannel.position:()J
            //   138: lsub
            //   139: l2i
            //   140: istore          16
            //   142: iload           16
            //   144: invokestatic    java/nio/ByteBuffer.allocateDirect:(I)Ljava/nio/ByteBuffer;
            //   147: astore          17
            //   149: aload_2
            //   150: aload           17
            //   152: invokevirtual   java/nio/channels/FileChannel.read:(Ljava/nio/ByteBuffer;)I
            //   155: pop
            //   156: aload           17
            //   158: invokevirtual   java/nio/ByteBuffer.flip:()Ljava/nio/Buffer;
            //   161: pop
            //   162: aload_3
            //   163: aload           17
            //   165: invokevirtual   java/nio/channels/FileChannel.write:(Ljava/nio/ByteBuffer;)I
            //   168: pop
            //   169: aload_3
            //   170: iconst_0
            //   171: invokevirtual   java/nio/channels/FileChannel.force:(Z)V
            //   174: goto            42
            //   177: astore          10
            //   179: aload           7
            //   181: astore          5
            //   183: aload           6
            //   185: astore          4
            //   187: aload           10
            //   189: invokevirtual   java/io/IOException.printStackTrace:()V
            //   192: aload_2
            //   193: ifnull          207
            //   196: aload_2
            //   197: invokevirtual   java/nio/channels/FileChannel.isOpen:()Z
            //   200: ifeq            207
            //   203: aload_2
            //   204: invokevirtual   java/nio/channels/FileChannel.close:()V
            //   207: aload_3
            //   208: ifnull          222
            //   211: aload_3
            //   212: invokevirtual   java/nio/channels/FileChannel.isOpen:()Z
            //   215: ifeq            222
            //   218: aload_3
            //   219: invokevirtual   java/nio/channels/FileChannel.close:()V
            //   222: aload           4
            //   224: ifnull          232
            //   227: aload           4
            //   229: invokevirtual   java/io/FileInputStream.close:()V
            //   232: aload           5
            //   234: ifnull          112
            //   237: aload           5
            //   239: invokevirtual   java/io/FileOutputStream.close:()V
            //   242: return
            //   243: astore          11
            //   245: aload           11
            //   247: invokevirtual   java/io/IOException.printStackTrace:()V
            //   250: return
            //   251: sipush          5120
            //   254: istore          16
            //   256: goto            142
            //   259: astore          8
            //   261: aload_2
            //   262: ifnull          276
            //   265: aload_2
            //   266: invokevirtual   java/nio/channels/FileChannel.isOpen:()Z
            //   269: ifeq            276
            //   272: aload_2
            //   273: invokevirtual   java/nio/channels/FileChannel.close:()V
            //   276: aload_3
            //   277: ifnull          291
            //   280: aload_3
            //   281: invokevirtual   java/nio/channels/FileChannel.isOpen:()Z
            //   284: ifeq            291
            //   287: aload_3
            //   288: invokevirtual   java/nio/channels/FileChannel.close:()V
            //   291: aload           4
            //   293: ifnull          301
            //   296: aload           4
            //   298: invokevirtual   java/io/FileInputStream.close:()V
            //   301: aload           5
            //   303: ifnull          311
            //   306: aload           5
            //   308: invokevirtual   java/io/FileOutputStream.close:()V
            //   311: aload           8
            //   313: athrow
            //   314: astore          9
            //   316: aload           9
            //   318: invokevirtual   java/io/IOException.printStackTrace:()V
            //   321: goto            311
            //   324: astore          21
            //   326: aload           21
            //   328: invokevirtual   java/io/IOException.printStackTrace:()V
            //   331: return
            //   332: astore          8
            //   334: aload           6
            //   336: astore          4
            //   338: aconst_null
            //   339: astore_2
            //   340: aconst_null
            //   341: astore          5
            //   343: aconst_null
            //   344: astore_3
            //   345: goto            261
            //   348: astore          8
            //   350: aload           7
            //   352: astore          5
            //   354: aload           6
            //   356: astore          4
            //   358: goto            261
            //   361: astore          10
            //   363: aconst_null
            //   364: astore          4
            //   366: aconst_null
            //   367: astore_2
            //   368: aconst_null
            //   369: astore          5
            //   371: aconst_null
            //   372: astore_3
            //   373: goto            187
            //   376: astore          10
            //   378: aload           6
            //   380: astore          4
            //   382: aconst_null
            //   383: astore_2
            //   384: aconst_null
            //   385: astore          5
            //   387: aconst_null
            //   388: astore_3
            //   389: goto            187
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type
            //  -----  -----  -----  -----  ---------------------
            //  10     20     361    376    Ljava/io/IOException;
            //  10     20     259    261    Any
            //  20     30     376    392    Ljava/io/IOException;
            //  20     30     332    348    Any
            //  30     42     177    187    Ljava/io/IOException;
            //  30     42     348    361    Any
            //  42     54     177    187    Ljava/io/IOException;
            //  42     54     348    361    Any
            //  66     77     324    331    Ljava/io/IOException;
            //  81     92     324    331    Ljava/io/IOException;
            //  97     102    324    331    Ljava/io/IOException;
            //  107    112    324    331    Ljava/io/IOException;
            //  113    142    177    187    Ljava/io/IOException;
            //  113    142    348    361    Any
            //  142    174    177    187    Ljava/io/IOException;
            //  142    174    348    361    Any
            //  187    192    259    261    Any
            //  196    207    243    251    Ljava/io/IOException;
            //  211    222    243    251    Ljava/io/IOException;
            //  227    232    243    251    Ljava/io/IOException;
            //  237    242    243    251    Ljava/io/IOException;
            //  265    276    314    324    Ljava/io/IOException;
            //  280    291    314    324    Ljava/io/IOException;
            //  296    301    314    324    Ljava/io/IOException;
            //  306    311    314    324    Ljava/io/IOException;
            //
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }

        public static boolean writeBitmapByPath(final String s, final Bitmap bitmap) {
            if (bitmap == null) {
                return false;
            }
            try {
                final File file = new File(s);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(Bitmap2BytesJPG(bitmap));
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return false;
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
                return false;
            }
        }

        public static boolean writeBytesByPath(final String s, final byte[] array) {
            if (array.length <= 0) {
                return false;
            }
            try {
                final File file = new File(s);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(array);
                fileOutputStream.flush();
                fileOutputStream.close();
                return true;
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return false;
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
                return false;
            }
        }

        public static void writeFileContent(final String s, final String s2, final String s3) {
            try {
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(s), s3);
                outputStreamWriter.write(s2);
                outputStreamWriter.close();
                outputStreamWriter.close();
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }

        public static void writePNGBitmapByPath(final String s, final Bitmap bitmap) {
            try {
                final File file = new File(s);
                if (file.exists()) {
                    file.delete();
                }
                file.createNewFile();
                final FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(Bitmap2BytesPNG(bitmap));
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }

        public static void writePrivateFile(final Context context, final String s, final String s2) {
            try {
                final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(s, 0));
                outputStreamWriter.write(s2);
                outputStreamWriter.close();
                outputStreamWriter.close();
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex2) {
                ex2.printStackTrace();
            }
        }

        public File creatSDDir(final String s) {
            File file = null;
            File file2 = null;
            try {
                final File file3;
                file2 = (file3 = new File(String.valueOf(this.SDPATH) + s));
                file3.mkdir();
                return file2;
            }
            catch (Exception ex2) {
            }

            return null;
        }

        public File creatSDFile(final String s) throws IOException {
            final File file = new File(String.valueOf(this.SDPATH) + s);
            file.createNewFile();
            return file;
        }

        public File deleteSDFile(final String s) throws IOException {
            final File file = new File(String.valueOf(this.SDPATH) + s);
            file.delete();
            return file;
        }

        public String getSDPATH() {
            return this.SDPATH;
        }

        public boolean isFileExist(final String s) {
            return new File(String.valueOf(this.SDPATH) + s).exists();
        }

        public void zipFile(final InputStream inputStream, final int n) {
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory() + "/mymovies/" + n + ".mp4"));
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(array, 0, read);
                }
                fileOutputStream.close();
                inputStream.close();
            }
            catch (IOException ex) {
                System.out.println("sjsjs" + ex);
            }
        }

        public void zipFile(final InputStream inputStream, final String s) {
            try {
                final FileOutputStream fileOutputStream = new FileOutputStream(new File(s));
                final byte[] array = new byte[1024];
                while (true) {
                    final int read = inputStream.read(array);
                    if (read == -1) {
                        break;
                    }
                    fileOutputStream.write(array, 0, read);
                }
                fileOutputStream.close();
                inputStream.close();
            }
            catch (IOException ex) {
                System.out.println("sjsjs" + ex);
            }
        }
    }

}
