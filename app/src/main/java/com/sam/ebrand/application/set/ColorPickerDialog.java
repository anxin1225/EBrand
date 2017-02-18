package com.sam.ebrand.application.set;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sam on 2016/11/16.
 */

public class ColorPickerDialog extends Dialog
{
    private int mInitialColor;
    private OnColorChangedListener mListener;

    public ColorPickerDialog(final Context context, final OnColorChangedListener mListener, final int mInitialColor) {
        super(context);
        this.mListener = mListener;
        this.mInitialColor = mInitialColor;
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView((View)new ColorPickerView(this.getContext(), new OnColorChangedListener() {
            @Override
            public void colorChanged(final int n) {
                ColorPickerDialog.this.mListener.colorChanged(n);
                ColorPickerDialog.this.dismiss();
            }
        }, this.mInitialColor));
        this.setTitle((CharSequence)"\u9009\u62e9\u989c\u8272\u2026\u2026");
    }

    private static class ColorPickerView extends View
    {
        private static final int CENTER_X = 160;
        private static final int CENTER_Y = 200;
        private Paint mCenterPaint;
        private Paint[] mColorPaint;
        private final int[] mColors;
        private OnColorChangedListener mListener;
        private Paint mPaint;
        private Rect[] mRects;

        ColorPickerView(final Context context, final OnColorChangedListener mListener, final int color) {
            super(context);
            this.mListener = mListener;
            this.mColors = new int[] { -65536, -48128, -30720, -13312, -16776961, -16759553, -16742145, -16724737, -16711936, -16711868, -16711800, -16711732, -256, -188, -120, -52, -16777216, -12303292, -7829368, -3355444 };
            this.mRects = new Rect[20];
            for (int i = 0; i < 20; ++i) {
                final int n = 80 * (i % 4);
                final int n2 = n + 80;
                final int n3 = 80 * (i / 4);
                this.mRects[i] = new Rect(n, n3, n2, n3 + 80);
            }
            this.mColorPaint = new Paint[20];
            (this.mColorPaint[0] = new Paint(1)).setColor(this.mColors[0]);
            this.mColorPaint[0].setStyle(Paint.Style.FILL);
            for (int j = 1; j < 20; ++j) {
                (this.mColorPaint[j] = new Paint(this.mColorPaint[0])).setColor(this.mColors[j]);
            }
            (this.mPaint = new Paint(1)).setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(32.0f);
            (this.mCenterPaint = new Paint(1)).setColor(color);
            this.mCenterPaint.setStrokeWidth(5.0f);
        }

        private boolean touchInRect(final float n, final float n2, final Rect rect) {
            return n >= rect.left && n <= rect.right && n2 >= rect.top && n2 <= rect.bottom;
        }

        protected void onDraw(final Canvas canvas) {
            for (int i = 0; i < 20; ++i) {
                canvas.drawRect(this.mRects[i], this.mColorPaint[i]);
            }
        }

        protected void onMeasure(final int n, final int n2) {
            this.setMeasuredDimension(320, 400);
        }

        public boolean onTouchEvent(final MotionEvent motionEvent) {
            final float x = motionEvent.getX();
            final float y = motionEvent.getY();
            switch (motionEvent.getAction()) {
                case 1: {
                    for (int i = 0; i < 20; ++i) {
                        if (this.touchInRect(x, y, this.mRects[i])) {
                            this.mListener.colorChanged(this.mColors[i]);
                        }
                    }
                    break;
                }
            }
            return true;
        }
    }

    public interface OnColorChangedListener
    {
        void colorChanged(final int p0);
    }
}
