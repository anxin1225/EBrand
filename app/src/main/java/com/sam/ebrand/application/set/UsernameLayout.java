package com.sam.ebrand.application.set;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.sam.ebrand.application.UserBitmapGenerate;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SubLcdManager;
import com.sam.ebrand.manage.SubLcdMsg;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;

/**
 * Created by sam on 2016/11/16.
 */

public class UsernameLayout extends View
{
    private static final int CMPRECT = 2;
    public static final String COMPANYPOSX = "COMPANYPOSX";
    public static final String COMPANYPOSY = "COMPANYPOSY";
    public static final String JOBPOSX = "JOBPOSX";
    public static final String JOBPOSY = "JOBPOSY";
    private static final int JOBRECT = 3;
    public static final String NAMEPOSX = "NAMEPOSX";
    public static final String NAMEPOSY = "NAMEPOSY";
    private static final int NAMERECT = 1;
    private static final int NONE = 0;
    StaticLayout layout;
    private Bitmap mBackgroundBitmap;
    private int mBackgroundColor;
    private String mCompany;
    int mCompanyColor;
    private float mCompanyPositionX;
    private float mCompanyPositionY;
    private Rect mCompanyRect;
    private String mJob;
    int mJobColor;
    private float mJobPositionX;
    private float mJobPositionY;
    private Rect mJobRect;
    private Paint mLeftPaint;
    private String mName;
    int mNameColor;
    private float mNamePositionX;
    private float mNamePositionY;
    private Paint mPaint;
    private Paint mRectPaint;
    private Paint mRightPaint;
    private int mTouchSelect;
    private float mTouchX;
    private float mTouchY;
    private Rect mUserNameRect;
    private boolean mbOnlynameMode;
    boolean mbReInit;
    boolean mbRefresh;
    private boolean mbUseBitmap;

    public UsernameLayout(final Context context) {
        super(context);
        this.mbOnlynameMode = false;
        this.init();
    }

    public UsernameLayout(final Context context, final AttributeSet set) {
        super(context, set);
        this.mbOnlynameMode = false;
        this.init();
    }

    private void init() {
        this.mbRefresh = false;
        (this.mPaint = new Paint()).setAntiAlias(true);
        this.mPaint.setFakeBoldText(true);
        this.mLeftPaint = new Paint(this.mPaint);
        this.mRightPaint = new Paint(this.mLeftPaint);
        (this.mRectPaint = new Paint(this.mPaint)).setStyle(Paint.Style.STROKE);
        this.mRectPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mRectPaint.setStrokeCap(Paint.Cap.ROUND);
      //  this.mRectPaint.setColor(-256);
        this.mPaint.setTextAlign(Paint.Align.CENTER);
        this.mPaint.setTextSize(200.0f);
        this.mBackgroundColor = -65536;
        this.mLeftPaint.setTextAlign(Paint.Align.LEFT);
        this.mRightPaint.setTextAlign(Paint.Align.RIGHT);
        this.mLeftPaint.setTextSize(60.0f);
        this.mRightPaint.setTextSize(60.0f);
        this.mUserNameRect = new Rect();
        this.mJobRect = new Rect();
        this.mCompanyRect = new Rect();
        this.mName = "";
        this.mJob = "";
        this.mCompany = "";
    }

    private boolean touchInRect(final float n, final float n2, final Rect rect) {
        return n >= rect.left && n <= rect.right && n2 >= rect.top && n2 <= rect.bottom;
    }

    public void InitPos() {
        final Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        if (!this.mbOnlynameMode) {
            final float mNamePositionY = 480.0f - (480.0f - (fontMetrics.bottom - fontMetrics.top)) / 2.0f - fontMetrics.bottom;
            this.mNamePositionX = 400.0f;
            this.mNamePositionY = mNamePositionY;
        }
        else {
            this.mNamePositionY = 240 - this.layout.getHeight() / 2;
            this.mNamePositionX = 400.0f;
        }
        this.mCompanyPositionX = 0.0f;
        this.mCompanyPositionY = 50.0f;
        this.mJobPositionX = 800.0f;
        this.mJobPositionY = 470.0f;
        this.invalidate();
    }

    public void SetName(final String s, final int mNameColor, final Typeface typeface) {
        this.mbOnlynameMode = true;
        this.mNameColor = mNameColor;
        final TextPaint textPaint = new TextPaint();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(this.mNameColor);
        if (typeface != null) {
            textPaint.setTypeface(typeface);
        }
        this.mName = s.replace("\n", "\r\n").trim();
        this.mName = this.mName.replace("-", "\r\n");
        textPaint.setTextSize((float) UserBitmapGenerate.calcTextSize(this.mName));
        textPaint.setAntiAlias(true);
        this.layout = new StaticLayout((CharSequence)this.mName, textPaint, 800, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        this.mNamePositionY = 240 - this.layout.getHeight() / 2;
        this.mNamePositionX = 400.0f;
        this.invalidate();
    }

    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        this.mbRefresh = true;
        if (this.mbOnlynameMode) {
            if (this.mbUseBitmap) {
                canvas.drawBitmap(this.mBackgroundBitmap, 0.0f, 0.0f, (Paint)null);
            }
            else {
                canvas.drawColor(this.mBackgroundColor);
            }
            canvas.save();
            canvas.translate(this.mNamePositionX, this.mNamePositionY);
            this.layout.draw(canvas);
            canvas.restore();
        }
        else {
            if (this.mbUseBitmap) {
                canvas.drawBitmap(this.mBackgroundBitmap, 0.0f, 0.0f, (Paint)null);
            }
            else {
                canvas.drawColor(this.mBackgroundColor);
            }
            canvas.drawText(this.mName, this.mNamePositionX, this.mNamePositionY, this.mPaint);
            if (!this.mName.equals("")) {
                this.mPaint.getTextBounds(this.mName, 0, this.mName.length(), this.mUserNameRect);
                final int width = this.mUserNameRect.width();
                final int height = this.mUserNameRect.height();
                this.mUserNameRect.left = (int)(this.mNamePositionX - width / 2);
                this.mUserNameRect.right = width + this.mUserNameRect.left;
                this.mUserNameRect.top = (int)(this.mNamePositionY - height);
                this.mUserNameRect.bottom = height + this.mUserNameRect.top;
            }
            canvas.drawText(this.mCompany, this.mCompanyPositionX, this.mCompanyPositionY, this.mLeftPaint);
            if (!this.mCompany.equals("")) {
                this.mLeftPaint.getTextBounds(this.mCompany, 0, this.mCompany.length(), this.mCompanyRect);
                final int width2 = this.mCompanyRect.width();
                final int height2 = this.mCompanyRect.height();
                this.mCompanyRect.left = (int)this.mCompanyPositionX;
                this.mCompanyRect.right = width2 + this.mCompanyRect.left;
                this.mCompanyRect.top = (int)(this.mCompanyPositionY - height2);
                this.mCompanyRect.bottom = height2 + this.mCompanyRect.top;
            }
            canvas.drawText(this.mJob, this.mJobPositionX, this.mJobPositionY, this.mRightPaint);
            if (!this.mJob.equals("")) {
                this.mLeftPaint.getTextBounds(this.mJob, 0, this.mJob.length(), this.mJobRect);
                final int width3 = this.mJobRect.width();
                final int height3 = this.mJobRect.height();
                this.mJobRect.left = (int)(this.mJobPositionX - width3);
                this.mJobRect.right = width3 + this.mJobRect.left;
                this.mJobRect.top = (int)(this.mJobPositionY - height3);
                this.mJobRect.bottom = height3 + this.mJobRect.top;
            }
        }
    }

    protected void onMeasure(final int n, final int n2) {
        super.onMeasure(n, n2);
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        final int action = motionEvent.getAction();
        final float x = motionEvent.getX();
        final float y = motionEvent.getY();
        switch (action) {
            default: {
                return true;
            }
            case 0: {
                if (this.touchInRect(x, y, this.mCompanyRect)) {
                    this.mTouchSelect = 2;
                }
                else if (this.touchInRect(x, y, this.mJobRect)) {
                    this.mTouchSelect = 3;
                }
                else if (this.touchInRect(x, y, this.mUserNameRect)) {
                    this.mTouchSelect = 1;
                }
                else {
                    this.mTouchSelect = 0;
                }
                this.mTouchX = x;
                this.mTouchY = y;
                this.invalidate();
                return true;
            }
            case 2: {
                final int n = (int)(x - this.mTouchX);
                final int n2 = (int)(y - this.mTouchY);
                if (this.mTouchSelect == 2) {
                    this.mCompanyPositionX += n;
                    this.mCompanyPositionY += n2;
                }
                else if (this.mTouchSelect == 3) {
                    this.mJobPositionX += n;
                    this.mJobPositionY += n2;
                }
                else if (this.mTouchSelect == 1) {
                    this.mNamePositionX += n;
                    this.mNamePositionY += n2;
                }
                this.mTouchX = x;
                this.mTouchY = y;
                this.invalidate();
                return true;
            }
            case 1: {
                this.mTouchSelect = 0;
                this.invalidate();
                return true;
            }
        }
    }

    public void saveCurrentLayout() {
        if (this.mbOnlynameMode) {
            return;
        }
        final SharedPreferences.Editor editor = SettingManager.getInstance().getEditor();
        editor.putFloat("NAMEPOSX", this.mNamePositionX);
        editor.putFloat("NAMEPOSY", this.mNamePositionY);
        editor.putFloat("COMPANYPOSX", this.mCompanyPositionX);
        editor.putFloat("COMPANYPOSY", this.mCompanyPositionY);
        editor.putFloat("JOBPOSX", this.mJobPositionX);
        editor.putFloat("JOBPOSY", this.mJobPositionY);
        editor.commit();
    }

    public void saveToBitmapAndRefresh() {
        synchronized (this) {
            this.mbRefresh = false;
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    final SharedPreferences.Editor editor = SettingManager.getInstance().getEditor();
                    editor.putBoolean("bCustomSetName", true);
                    editor.putString("CustomUserName", UsernameLayout.this.mName);
                    editor.commit();
                    final Bitmap bitmap = Bitmap.createBitmap(800, 480, Bitmap.Config.ARGB_8888);
                    final Canvas canvas = new Canvas(bitmap);
                    canvas.setDrawFilter((DrawFilter)new PaintFlagsDrawFilter(0, 3));
                    if (UsernameLayout.this.mbUseBitmap) {
                        canvas.drawBitmap(UsernameLayout.this.mBackgroundBitmap, 0.0f, 0.0f, (Paint)null);
                    }
                    else {
                        canvas.drawColor(UsernameLayout.this.mBackgroundColor);
                    }
                    if (UsernameLayout.this.mbOnlynameMode) {
                        canvas.translate(400.0f, (float)(240 - UsernameLayout.this.layout.getHeight() / 2));
                        UsernameLayout.this.layout.draw(canvas);
                    }
                    else {
                        UsernameLayout.this.saveCurrentLayout();
                        canvas.drawText(UsernameLayout.this.mName, UsernameLayout.this.mNamePositionX, UsernameLayout.this.mNamePositionY, UsernameLayout.this.mPaint);
                        canvas.drawText(UsernameLayout.this.mCompany, UsernameLayout.this.mCompanyPositionX, UsernameLayout.this.mCompanyPositionY, UsernameLayout.this.mLeftPaint);
                        canvas.drawText(UsernameLayout.this.mJob, UsernameLayout.this.mJobPositionX, UsernameLayout.this.mJobPositionY, UsernameLayout.this.mRightPaint);
                    }
                    FileUtils.PngToBmp(bitmap, MeetingParam.SDCARD_WELCOME_PICTURE);
                    SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 0, true));
                }
            }.start();
        }
    }

    public void setBackgroundBitmap(final Bitmap mBackgroundBitmap) {
        this.mBackgroundBitmap = mBackgroundBitmap;
        this.mbUseBitmap = true;
        this.invalidate();
    }

    public void setBackgroundColor(final int mBackgroundColor) {
        this.mBackgroundColor = mBackgroundColor;
        this.mbUseBitmap = false;
        this.invalidate();
    }

    public void setNameFontAndColor(final String s, final String s2, final String s3, final Typeface typeface, final Typeface typeface2, final Typeface typeface3, final int n, final int n2, final int n3, final int n4, final int n5, final int n6, final boolean fakeBoldText, final boolean fakeBoldText2, final boolean fakeBoldText3) {
        this.mbOnlynameMode = false;
        this.mName = s.replace("-", "");
        this.mJob = s2.replace("-", "");
        this.mCompany = s3.replace("-", "");
        if (typeface != null) {
            this.mPaint.setTypeface(typeface);
        }
        if (typeface2 != null) {
            this.mRightPaint.setTypeface(typeface2);
        }
        if (typeface3 != null) {
            this.mLeftPaint.setTypeface(typeface3);
        }
        this.mNameColor = n;
        this.mJobColor = n2;
        this.mCompanyColor = n3;
        this.mPaint.setColor(n);
        this.mRightPaint.setColor(n2);
        this.mLeftPaint.setColor(n3);
        final Paint.FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        final float n7 = 480.0f - (480.0f - (fontMetrics.bottom - fontMetrics.top)) / 2.0f - fontMetrics.bottom;
        final SharedPreferences sharedPrefrences = SettingManager.getInstance().getSharedPrefrences();
        this.mNamePositionX = sharedPrefrences.getFloat("NAMEPOSX", 400.0f);
        this.mNamePositionY = sharedPrefrences.getFloat("NAMEPOSY", n7);
        this.mCompanyPositionX = sharedPrefrences.getFloat("COMPANYPOSX", 0.0f);
        this.mCompanyPositionY = sharedPrefrences.getFloat("COMPANYPOSY", 50.0f);
        this.mJobPositionX = sharedPrefrences.getFloat("JOBPOSX", 800.0f);
        this.mJobPositionY = sharedPrefrences.getFloat("JOBPOSY", 470.0f);
        this.mPaint.setTextSize(10.0f * n4);
        this.mLeftPaint.setTextSize(10.0f * n6);
        this.mRightPaint.setTextSize(10.0f * n5);
        this.mPaint.setFakeBoldText(fakeBoldText);
        this.mLeftPaint.setFakeBoldText(fakeBoldText3);
        this.mRightPaint.setFakeBoldText(fakeBoldText2);
        this.invalidate();
    }
}
