package com.sam.ebrand.application;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Typeface;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.sam.ebrand.manage.SubLcdManager;
import com.sam.ebrand.manage.SubLcdMsg;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;

/**
 * Created by sam on 2016/11/10.
 */
public class UserBitmapGenerate {

    public static UserBitmapGenerate instance;
    private Bitmap mBackgroundBitmap;
    private int mBackgroundColor;
    private TextPaint mCenterPaint;
    private String mCompany;
    private int mCompanyColor;
    private float mCompanyPositionX;
    private float mCompanyPositionY;
    private Typeface mCompanyTf;
    private String mJob;
    private int mJobColor;
    private float mJobPositionX;
    private float mJobPositionY;
    private Typeface mJobTf;
    private float mJobWeight;
    private StaticLayout mLeftLayout;
    private TextPaint mLeftPaint;
    private String mName;
    private int mNameColor;
    private float mNameHeight;
    private StaticLayout mNameLayout;
    private float mNamePositionX;
    private float mNamePositionY;
    private Typeface mNameTf;
    private float mNameWeight;
    private StaticLayout mRightLayout;
    private TextPaint mRightPaint;
    private boolean mbUseBitmap;

    static {
        UserBitmapGenerate.instance = null;
    }

    private UserBitmapGenerate() {
        this.init();
    }

    static /* synthetic */ void access$10(final UserBitmapGenerate userBitmapGenerate, final float mNamePositionX) {
        userBitmapGenerate.mNamePositionX = mNamePositionX;
    }

    static /* synthetic */ void access$12(final UserBitmapGenerate userBitmapGenerate, final float mNamePositionY) {
        userBitmapGenerate.mNamePositionY = mNamePositionY;
    }

    static /* synthetic */ void access$13(final UserBitmapGenerate userBitmapGenerate, final String mName) {
        userBitmapGenerate.mName = mName;
    }

    static /* synthetic */ void access$15(final UserBitmapGenerate userBitmapGenerate, final String mCompany) {
        userBitmapGenerate.mCompany = mCompany;
    }

    static /* synthetic */ void access$17(final UserBitmapGenerate userBitmapGenerate, final StaticLayout mLeftLayout) {
        userBitmapGenerate.mLeftLayout = mLeftLayout;
    }

    static /* synthetic */ void access$18(final UserBitmapGenerate userBitmapGenerate, final String mJob) {
        userBitmapGenerate.mJob = mJob;
    }

    static /* synthetic */ void access$20(final UserBitmapGenerate userBitmapGenerate, final StaticLayout mRightLayout) {
        userBitmapGenerate.mRightLayout = mRightLayout;
    }

    static /* synthetic */ void access$23(final UserBitmapGenerate userBitmapGenerate, final Bitmap mBackgroundBitmap) {
        userBitmapGenerate.mBackgroundBitmap = mBackgroundBitmap;
    }

    static /* synthetic */ void access$9(final UserBitmapGenerate userBitmapGenerate, final StaticLayout mNameLayout) {
        userBitmapGenerate.mNameLayout = mNameLayout;
    }

    public static int calcTextSize(final String s) {
        final String[] split = s.split("\r\n");
        int length = 2;
        int length2 = 1;
        if (split != null) {
            length2 = split.length;
            if (length2 == 2) {
                length = 3;
            }
            else if (length2 == 3) {
                length = 5;
            }
            for (int i = 0; i < split.length; ++i) {
                if (length < split[i].length()) {
                    length = split[i].length();
                }
            }
        }
        Log.e("calc", "line:" + length2 + ",maxLen:" + length);
        if (length2 == 1) {
            switch (length) {
                case 2: {
                    return 310;
                }
                case 3: {
                    return 260;
                }
                case 4: {
                    return 200;
                }
                case 5: {
                    return 150;
                }
                case 6: {
                    return 130;
                }
                case 7: {
                    return 110;
                }
                case 8: {
                    return 90;
                }
                case 9: {
                    return 80;
                }
            }
        }
        else if (length2 == 2) {
            switch (length) {
                default: {
                    return 0;
                }
                case 3: {
                    return 220;
                }
                case 4: {
                    return 200;
                }
                case 5: {
                    return 150;
                }
                case 6: {
                    return 130;
                }
                case 7: {
                    return 110;
                }
                case 8: {
                    return 90;
                }
                case 9: {
                    return 80;
                }
            }
        }
        else if (length2 == 3) {
            switch (length) {
                default: {
                    return 0;
                }
                case 5: {
                    return 140;
                }
                case 6: {
                    return 130;
                }
                case 7: {
                    return 110;
                }
                case 8: {
                    return 90;
                }
                case 9: {
                    return 80;
                }
            }
        }
        return 0;
    }

    public static UserBitmapGenerate getInstance() {
        if (UserBitmapGenerate.instance == null) {
            UserBitmapGenerate.instance = new UserBitmapGenerate();
        }
        return UserBitmapGenerate.instance;
    }

    private void init() {
        (this.mCenterPaint = new TextPaint()).setAntiAlias(true);
        this.mCenterPaint.setFakeBoldText(true);
        this.mLeftPaint = new TextPaint((Paint)this.mCenterPaint);
        this.mRightPaint = new TextPaint((Paint)this.mLeftPaint);
        this.mBackgroundColor = -65536;
        this.mName = "";
        this.mJob = "";
        this.mCompany = "";
    }

    private void saveToBitmapAndRefresh() {
        synchronized (this) {
            new Thread() {
                @Override
                public void run() {
                    final Bitmap bitmap = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
                    final Canvas canvas = new Canvas(bitmap);
                    canvas.setDrawFilter((DrawFilter)new PaintFlagsDrawFilter(0, 3));
                    UserBitmapGenerate.this.mCenterPaint.setTypeface(UserBitmapGenerate.this.mNameTf);
                    UserBitmapGenerate.this.mRightPaint.setTypeface(UserBitmapGenerate.this.mJobTf);
                    UserBitmapGenerate.this.mLeftPaint.setTypeface(UserBitmapGenerate.this.mCompanyTf);
                    if (UserBitmapGenerate.this.mName.trim().equals("") && UserBitmapGenerate.this.mCompany.trim().equals("") && UserBitmapGenerate.this.mJob.trim().equals("")) {
                        canvas.drawColor(-16777216);
                    }
                    else {
                        if (!UserBitmapGenerate.this.mName.equals("") && UserBitmapGenerate.this.mCompany.equals("") && UserBitmapGenerate.this.mJob.equals("")) {
                            UserBitmapGenerate.this.mCenterPaint.setTextAlign(Paint.Align.CENTER);
                            UserBitmapGenerate.this.mCenterPaint.setTextSize((float)UserBitmapGenerate.calcTextSize(UserBitmapGenerate.this.mName));
                            UserBitmapGenerate.access$9(UserBitmapGenerate.this, new StaticLayout((CharSequence)UserBitmapGenerate.this.mName, UserBitmapGenerate.this.mCenterPaint, 800, Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true));
                            UserBitmapGenerate.access$10(UserBitmapGenerate.this, 400.0f);
                            UserBitmapGenerate.access$12(UserBitmapGenerate.this, 240 - UserBitmapGenerate.this.mNameLayout.getHeight() / 2);
                        }
                        else {
                            UserBitmapGenerate.access$13(UserBitmapGenerate.this, UserBitmapGenerate.this.mName.replace("\r\n", ""));
                            UserBitmapGenerate.this.mCenterPaint.setTextAlign(Paint.Align.LEFT);
                            UserBitmapGenerate.access$9(UserBitmapGenerate.this, new StaticLayout((CharSequence)UserBitmapGenerate.this.mName, UserBitmapGenerate.this.mCenterPaint, (int)(800.0f - UserBitmapGenerate.this.mNamePositionX), Layout$Alignment.ALIGN_CENTER, 1.0f, 0.0f, true));
                            UserBitmapGenerate.access$15(UserBitmapGenerate.this, UserBitmapGenerate.this.mCompany.replace("\r\n", ""));
                            UserBitmapGenerate.access$17(UserBitmapGenerate.this, new StaticLayout((CharSequence)UserBitmapGenerate.this.mCompany, UserBitmapGenerate.this.mLeftPaint, (int)(800.0f - UserBitmapGenerate.this.mCompanyPositionX), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true));
                            UserBitmapGenerate.access$18(UserBitmapGenerate.this, UserBitmapGenerate.this.mJob.replace("\r\n", ""));
                            UserBitmapGenerate.access$20(UserBitmapGenerate.this, new StaticLayout((CharSequence)UserBitmapGenerate.this.mJob, UserBitmapGenerate.this.mRightPaint, (int)(800.0f - UserBitmapGenerate.this.mJobPositionX), Layout$Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true));
                        }
                        canvas.drawColor(UserBitmapGenerate.this.mBackgroundColor);
                        if (UserBitmapGenerate.this.mbUseBitmap) {
                            UserBitmapGenerate.access$23(UserBitmapGenerate.this, FileUtils.getBitmapBySDPath(MeetingParam.SDCARD_BACKGROUND_PICTURE, 1));
                            if (UserBitmapGenerate.this.mBackgroundBitmap != null) {
                                canvas.drawBitmap(UserBitmapGenerate.this.mBackgroundBitmap, 0.0f, 0.0f, (Paint)null);
                            }
                        }
                        else {
                            canvas.drawColor(UserBitmapGenerate.this.mBackgroundColor);
                        }
                        canvas.save();
                        canvas.translate(UserBitmapGenerate.this.mNamePositionX, UserBitmapGenerate.this.mNamePositionY);
                        UserBitmapGenerate.this.mNameLayout.draw(canvas);
                        canvas.restore();
                        if (!UserBitmapGenerate.this.mCompany.equals("")) {
                            canvas.save();
                            canvas.translate(UserBitmapGenerate.this.mCompanyPositionX, UserBitmapGenerate.this.mCompanyPositionY);
                            if (UserBitmapGenerate.this.mLeftLayout != null) {
                                UserBitmapGenerate.this.mLeftLayout.draw(canvas);
                            }
                            canvas.restore();
                        }
                        if (!UserBitmapGenerate.this.mJob.equals("")) {
                            canvas.save();
                            canvas.translate(UserBitmapGenerate.this.mJobPositionX, UserBitmapGenerate.this.mJobPositionY);
                            if (UserBitmapGenerate.this.mRightLayout != null) {
                                UserBitmapGenerate.this.mRightLayout.draw(canvas);
                            }
                            canvas.restore();
                        }
                    }
                    FileUtils.PngToBmp(bitmap, MeetingParam.SDCARD_WELCOME_PICTURE);
                    SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 0));
                }
            }.start();
        }
    }

    public void setBackgroundBitmap() {
        this.mbUseBitmap = true;
    }

    public void setBackgroundColor(final int n) {
        this.mBackgroundColor = (0xFF000000 | n);
        this.mbUseBitmap = false;
    }

    public void setNameFontAndColor(final String s, final String s2, final String s3, final String s4, final String s5, final String s6, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final float mNameHeight, final float n7, final float n8, final float mNameWeight, final float mJobWeight, final float n9, final float mNamePositionX, final float mNamePositionY, final float mJobPositionX, final float mJobPositionY, final float mCompanyPositionX, final float mCompanyPositionY, final boolean fakeBoldText, final boolean fakeBoldText2, final boolean fakeBoldText3) {
        Log.e("MPINFO", "name:" + s + ",job:" + s2 + ",company:" + s3);
        this.mCenterPaint.reset();
        this.mLeftPaint.reset();
        this.mRightPaint.reset();
        this.mCenterPaint.setAntiAlias(true);
        this.mRightPaint.setAntiAlias(true);
        this.mLeftPaint.setAntiAlias(true);
        this.mName = s.replace("-", "\r\n").trim();
        this.mJob = s2.replace("-", "\r\n").trim();
        this.mCompany = s3.replace("-", "\r\n").trim();
        this.mNameHeight = mNameHeight;
        this.mNameWeight = mNameWeight;
        this.mJobWeight = mJobWeight;
        this.mNamePositionX = mNamePositionX;
        this.mNamePositionY = mNamePositionY;
        this.mJobPositionX = mJobPositionX;
        this.mJobPositionY = mJobPositionY;
        this.mCompanyPositionX = mCompanyPositionX;
        this.mCompanyPositionY = mCompanyPositionY;
        this.mNameTf = ServerFontsManager.getInstance().getTypeface(s4);
        this.mJobTf = ServerFontsManager.getInstance().getTypeface(s5);
        this.mCompanyTf = ServerFontsManager.getInstance().getTypeface(s6);
        this.mNameColor = (0xFF000000 | n);
        this.mJobColor = (0xFF000000 | n2);
        this.mCompanyColor = (0xFF000000 | n3);
        this.mCenterPaint.setColor(this.mNameColor);
        this.mRightPaint.setColor(this.mJobColor);
        this.mLeftPaint.setColor(this.mCompanyColor);
        this.mCenterPaint.setTextSize(1.0f * n4);
        this.mLeftPaint.setTextSize(1.0f * n6);
        this.mRightPaint.setTextSize(1.0f * n5);
        this.mCenterPaint.setFakeBoldText(fakeBoldText);
        this.mLeftPaint.setFakeBoldText(fakeBoldText3);
        this.mRightPaint.setFakeBoldText(fakeBoldText2);
        this.mCenterPaint.setTextAlign(Paint.Align.LEFT);
        this.mRightPaint.setTextAlign(Paint.Align.LEFT);
        this.mLeftPaint.setTextAlign(Paint.Align.LEFT);
        this.saveToBitmapAndRefresh();
    }
}
