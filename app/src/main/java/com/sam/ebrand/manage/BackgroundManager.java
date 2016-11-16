package com.sam.ebrand.manage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

import com.sam.ebrand.param.MeetingParam;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by sam on 2016/11/15.
 */
public class BackgroundManager {

    private static BackgroundManager instance;
    private BitmapDrawable mBackgroundDrawable;
    private HashMap<String, WeakReference<BitmapDrawable>> mBackgroundList;
    private BitmapDrawable mSublcdDrawable;

    private BackgroundManager() {
        this.mBackgroundDrawable = null;
        this.mBackgroundList = new HashMap<String, WeakReference<BitmapDrawable>>();
        this.mSublcdDrawable = null;
    }

    public static BackgroundManager getInstance() {
        if (BackgroundManager.instance == null) {
            BackgroundManager.instance = new BackgroundManager();
        }
        return BackgroundManager.instance;
    }

    public void UpdateBackground(final Context context, final String s) {
        if (s.equals("")) {
            this.UpdateBackground(context, "2130837741");
        }
        else if (SocketNoticeManager.isNum(s)) {
            if (!this.mBackgroundList.containsKey(s)) {
                this.mBackgroundDrawable = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(s)));
                this.mBackgroundList.put(s, new WeakReference<BitmapDrawable>(this.mBackgroundDrawable));
                return;
            }
            this.mBackgroundDrawable = this.mBackgroundList.get(s).get();
            if (this.mBackgroundDrawable == null) {
                this.mBackgroundDrawable = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), Integer.parseInt(s)));
                this.mBackgroundList.put(s, new WeakReference<BitmapDrawable>(this.mBackgroundDrawable));
            }
        }
        else if (this.mBackgroundList.containsKey(s)) {
            this.mBackgroundDrawable = this.mBackgroundList.get(s).get();
            if (this.mBackgroundDrawable == null) {
                final Bitmap decodeFile = BitmapFactory.decodeFile(s);
                if (decodeFile != null) {
                    this.mBackgroundDrawable = new BitmapDrawable(decodeFile);
                    this.mBackgroundList.put(s, new WeakReference<BitmapDrawable>(this.mBackgroundDrawable));
                    return;
                }
                this.UpdateBackground(context, "2130837741");
            }
        }
        else {
            final Bitmap decodeFile2 = BitmapFactory.decodeFile(s);
            if (decodeFile2 != null) {
                this.mBackgroundDrawable = new BitmapDrawable(decodeFile2);
                this.mBackgroundList.put(s, new WeakReference<BitmapDrawable>(this.mBackgroundDrawable));
                return;
            }
            this.UpdateBackground(context, "2130837741");
        }
    }

    public void UpdateSublcdDrawable() {
        final Bitmap decodeFile = BitmapFactory.decodeFile(MeetingParam.SDCARD_WELCOME_PICTURE);
        if (decodeFile != null) {
            final BitmapDrawable mSublcdDrawable = new BitmapDrawable(decodeFile);
            new WeakReference(mSublcdDrawable);
            this.mSublcdDrawable = mSublcdDrawable;
        }
    }

    public BitmapDrawable getBackground() {
        return this.mBackgroundDrawable;
    }

    public BitmapDrawable getBitmapDrawable(final Context context, final int n) {
        if (this.mBackgroundList.containsKey(new StringBuilder().append(n).toString())) {
            BitmapDrawable bitmapDrawable = this.mBackgroundList.get(new StringBuilder().append(n).toString()).get();
            if (bitmapDrawable == null) {
                bitmapDrawable = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), n));
                this.mBackgroundList.put(new StringBuilder().append(n).toString(), new WeakReference<BitmapDrawable>(bitmapDrawable));
            }
            return bitmapDrawable;
        }
        final BitmapDrawable bitmapDrawable2 = new BitmapDrawable(BitmapFactory.decodeResource(context.getResources(), n));
        this.mBackgroundList.put(new StringBuilder().append(n).toString(), new WeakReference<BitmapDrawable>(bitmapDrawable2));
        return bitmapDrawable2;
    }

    public BitmapDrawable getSublcdPicture() {
        return this.mSublcdDrawable;
    }
}
