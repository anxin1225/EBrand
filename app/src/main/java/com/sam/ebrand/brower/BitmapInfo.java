package com.sam.ebrand.brower;

import android.graphics.Bitmap;

/**
 * Created by sam on 2016/11/10.
 */
public class BitmapInfo
{
    public Bitmap mBitmap;
    public String mPath;

    public BitmapInfo(final Bitmap mBitmap, final String mPath) {
        this.mBitmap = mBitmap;
        this.mPath = mPath;
    }
}
