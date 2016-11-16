package com.sam.ebrand.application.set;

import android.graphics.Bitmap;

/**
 * Created by sam on 2016/11/16.
 */
public class BackgroundBitmapInfo
{
    public Bitmap mBitmap;
    public String mFileName;

    public BackgroundBitmapInfo(final Bitmap mBitmap, final String mFileName) {
        this.mBitmap = mBitmap;
        this.mFileName = mFileName;
    }
}
