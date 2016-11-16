package com.sam.ebrand.brower;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.ViewFlipper;

import com.sam.ebrand.R;
import com.sam.ebrand.widget.ProgressHint;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by sam on 2016/11/10.
 */

public class ImageBrowser extends Activity implements AdapterView.OnItemSelectedListener, Observer
{
    private long mBackpressTime;
    private Matrix mBitmapMatrix;
    private BitmapDrawable mCurrentImage;
    private Gallery mGallery;
    private ImageAdapter mGalleryAdapter;
    private RelativeLayout mGalleryView;
    private GestureDetector mGestureDetector;
    private Handler mHandler;
    private int mImagePosition;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private ImageLoader mLoader;
    private PointF mStartPos;
    private ViewFlipper mViewFlipper;
    private boolean mbNextAnimation;

    @Override
    public void update(Observable observable, Object data) {

    }

    public ImageBrowser() {
        this.mHandler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {}
                    case 1: {
                        ImageBrowser.this.mGalleryAdapter.notifyDataSetChanged();
                        if (ImageBrowser.this.mGalleryAdapter.getCount() > 0) {
                            if (ImageBrowser.this.mLoader != null && ImageBrowser.this.mLoader.getStatus() != AsyncTask.Status.FINISHED) {
                                ImageBrowser.this.mLoader.cancel();
                            }
                            ImageBrowser.access$2(ImageBrowser.this, (ImageLoader)new ImageLoader().execute((Object[])new String[] { ImageBrowser.this.mGalleryAdapter.mImageList.get(0).mPath }));
                            ImageBrowser.access$3(ImageBrowser.this, 0);
                        }
                        ProgressHint.Dismiss();
                    }
                }
            }
        };
        this.mBackpressTime = 0L;
    }

    static /* synthetic */ void access$11(final ImageBrowser imageBrowser, final boolean mbNextAnimation) {
        imageBrowser.mbNextAnimation = mbNextAnimation;
    }

    static /* synthetic */ void access$2(final ImageBrowser imageBrowser, final ImageLoader mLoader) {
        imageBrowser.mLoader = mLoader;
    }

    static /* synthetic */ void access$3(final ImageBrowser imageBrowser, final int mImagePosition) {
        imageBrowser.mImagePosition = mImagePosition;
    }

    static /* synthetic */ void access$4(final ImageBrowser imageBrowser, final BitmapDrawable mCurrentImage) {
        imageBrowser.mCurrentImage = mCurrentImage;
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
    }

    public void onBackPressed() {
        final long currentThreadTimeMillis = SystemClock.currentThreadTimeMillis();
        if (currentThreadTimeMillis - this.mBackpressTime > 500L) {
            this.mBackpressTime = currentThreadTimeMillis;
            this.finish();
        }
    }

    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        final String string = this.getIntent().getExtras().getString("FileName");
        Log.e("OpenF", new StringBuilder().append(string).toString());
        this.setContentView(R.layout.activity_image_browser);
        this.mGallery = (Gallery)this.findViewById(R.id.gallery);
        ProgressHint.Show((Context)this, "\u6253\u5f00", "\u6b63\u5728\u6253\u5f00\uff0c\u8bf7\u7a0d\u5019\u2026\u2026");
        (this.mImageView1 = (ImageView)this.findViewById(R.id.imageView1)).setScaleType(ImageView.ScaleType.MATRIX);
        (this.mImageView2 = (ImageView)this.findViewById(R.id.imageView2)).setScaleType(ImageView.ScaleType.MATRIX);
        this.mGalleryAdapter = new ImageAdapter();
        this.mGallery.setAdapter((SpinnerAdapter)this.mGalleryAdapter);
        this.mGallery.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)this);
        this.mImagePosition = -1;
        this.mStartPos = new PointF();
        this.mGestureDetector = new GestureDetector((GestureDetector.OnGestureListener)new FlipGesture());
        this.mBitmapMatrix = new Matrix();
        this.mViewFlipper = (ViewFlipper)this.findViewById(R.id.view_flipper);
        this.mGalleryView = (RelativeLayout)this.findViewById(R.id.gallery_layout);
        new Thread(new LoadImages(string)).start();
    }

    protected void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        Log.e("ImageBrowser", "ondestory");
        this.mGalleryAdapter.Release();
        if (this.mLoader != null && this.mLoader.getStatus() != AsyncTask.Status.FINISHED) {
            this.mLoader.cancel();
        }
        if (this.mCurrentImage != null) {
            this.mCurrentImage.setCallback(null);
            if (this.mCurrentImage.getBitmap() != null && !this.mCurrentImage.getBitmap().isRecycled()) {
                this.mCurrentImage.getBitmap().recycle();
            }
        }
        for (int i = 0; i < this.mViewFlipper.getChildCount(); ++i) {
            final ImageView imageView = (ImageView)this.mViewFlipper.getChildAt(i);
            if (imageView != null) {
                final BitmapDrawable bitmapDrawable = (BitmapDrawable)imageView.getDrawable();
                imageView.setImageDrawable((Drawable)null);
                if (bitmapDrawable != null && bitmapDrawable.getBitmap() != null && !bitmapDrawable.getBitmap().isRecycled()) {
                    bitmapDrawable.getBitmap().recycle();
                }
            }
        }
        System.gc();
    }

    public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
        Log.e("position", this.mImagePosition + "," + n);
        if (this.mImagePosition == n) {
            return;
        }
        this.mGalleryAdapter.setSelectItem(n);
        if (this.mImagePosition > n) {
            this.mbNextAnimation = false;
        }
        else {
            this.mbNextAnimation = true;
        }
        if (this.mLoader != null && this.mLoader.getStatus() != AsyncTask.Status.FINISHED) {
            this.mLoader.cancel();
        }
        this.mLoader = (ImageLoader)new ImageLoader().execute((Object[])new String[] { this.mGalleryAdapter.mImageList.get(n).mPath });
        this.mImagePosition = n;
    }

    public void onNothingSelected(final AdapterView<?> adapterView) {
    }

    protected void onResume() {
        super.onResume();
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return this.mGestureDetector.onTouchEvent(motionEvent);
    }

    private class FlipGesture extends GestureDetector.SimpleOnGestureListener
    {
        PointF mScrollPoint;

        private FlipGesture() {
            this.mScrollPoint = new PointF();
        }

        public boolean onDoubleTap(final MotionEvent motionEvent) {
            return true;
        }

        public boolean onDown(final MotionEvent motionEvent) {
            this.mScrollPoint.x = motionEvent.getX();
            this.mScrollPoint.y = motionEvent.getY();
            return true;
        }

        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            if (motionEvent == null || motionEvent2 == null) {
                return false;
            }
            if (Math.abs(motionEvent.getY() - motionEvent2.getY()) > 200.0f) {
                return false;
            }
            if (ImageBrowser.this.mGalleryAdapter.getCount() <= 0) {
                return true;
            }
            if (motionEvent.getX() - motionEvent2.getX() > 200.0f && Math.abs(n) > 200.0f) {
                Log.e("Flip", "next");
                final ImageBrowser this$0 = ImageBrowser.this;
                ImageBrowser.access$3(this$0, 1 + this$0.mImagePosition);
                ImageBrowser.access$11(ImageBrowser.this, true);
                if (ImageBrowser.this.mImagePosition < ImageBrowser.this.mGalleryAdapter.getCount()) {
                    if (ImageBrowser.this.mLoader != null && ImageBrowser.this.mLoader.getStatus() != AsyncTask.Status.FINISHED) {
                        ImageBrowser.this.mLoader.cancel();
                    }
                    ImageBrowser.access$2(ImageBrowser.this, (ImageLoader)new ImageLoader().execute((Object[])new String[] { ImageBrowser.this.mGalleryAdapter.mImageList.get(ImageBrowser.this.mImagePosition).mPath }));
                    ImageBrowser.this.mGallery.setSelection(ImageBrowser.this.mImagePosition, true);
                    ImageBrowser.this.mGalleryAdapter.setSelectItem(ImageBrowser.this.mImagePosition);
                }
                else {
                    ImageBrowser.access$3(ImageBrowser.this, -1 + ImageBrowser.this.mGalleryAdapter.getCount());
                    if (ImageBrowser.this.mImagePosition < 0) {
                        ImageBrowser.access$3(ImageBrowser.this, 0);
                    }
                }
            }
            else if (motionEvent2.getX() - motionEvent.getX() > 200.0f && Math.abs(n) > 200.0f) {
                Log.e("Flip", "prev");
                final ImageBrowser this$2 = ImageBrowser.this;
                ImageBrowser.access$3(this$2, -1 + this$2.mImagePosition);
                ImageBrowser.access$11(ImageBrowser.this, false);
                if (ImageBrowser.this.mImagePosition >= 0 && ImageBrowser.this.mImagePosition < ImageBrowser.this.mGalleryAdapter.getCount()) {
                    if (ImageBrowser.this.mLoader != null && ImageBrowser.this.mLoader.getStatus() != AsyncTask.Status.FINISHED) {
                        ImageBrowser.this.mLoader.cancel();
                    }
                    ImageBrowser.access$2(ImageBrowser.this, (ImageLoader)new ImageLoader().execute((Object[])new String[] { ImageBrowser.this.mGalleryAdapter.mImageList.get(ImageBrowser.this.mImagePosition).mPath }));
                    ImageBrowser.this.mGallery.setSelection(ImageBrowser.this.mImagePosition, true);
                    ImageBrowser.this.mGalleryAdapter.setSelectItem(ImageBrowser.this.mImagePosition);
                }
                else {
                    ImageBrowser.access$3(ImageBrowser.this, 0);
                }
            }
            return true;
        }

        public boolean onScroll(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float n, final float n2) {
            return true;
        }

        public boolean onSingleTapUp(final MotionEvent motionEvent) {
            if (ImageBrowser.this.mGalleryView.isShown()) {
                ImageBrowser.this.mGalleryView.setVisibility(4);
            }
            else {
                ImageBrowser.this.mGalleryView.setVisibility(0);
            }
            return true;
        }
    }

    private class ImageAdapter extends BaseAdapter
    {
        ArrayList<BitmapInfo> mImageList;
        int mSelectItem;

        public ImageAdapter() {
            this.mImageList = new ArrayList<BitmapInfo>();
        }

        public void Add(final BitmapInfo bitmapInfo) {
            this.mImageList.add(bitmapInfo);
        }

        public void Release() {
            for (int i = 0; i < this.mImageList.size(); ++i) {
                final BitmapInfo bitmapInfo = this.mImageList.get(i);
                if (bitmapInfo.mBitmap != null && bitmapInfo.mBitmap.isRecycled()) {
                    bitmapInfo.mBitmap.recycle();
                }
            }
            this.mImageList.clear();
        }

        public int getCount() {
            return this.mImageList.size();
        }

        public Object getItem(final int n) {
            return n;
        }

        public long getItemId(final int n) {
            return n;
        }

        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            final ImageView imageView = new ImageView((Context)ImageBrowser.this);
            imageView.setImageBitmap(this.mImageList.get(n).mBitmap);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams((ViewGroup.LayoutParams)new Gallery.LayoutParams(-2, -2));
            if (this.mSelectItem == n) {
                imageView.setBackgroundColor(-65536);
                imageView.setPadding(1, 1, 1, 1);
                return (View)imageView;
            }
            imageView.setBackgroundColor(16777215);
            imageView.setPadding(10, 10, 10, 10);
            return (View)imageView;
        }

        public void setSelectItem(final int mSelectItem) {
            if (this.mSelectItem != mSelectItem) {
                this.mSelectItem = mSelectItem;
                this.notifyDataSetChanged();
            }
        }
    }

    private class ImageLoader extends AsyncTask<String, Void, Bitmap>
    {
        BitmapFactory.Options mOptions;

        public ImageLoader() {
            this.mOptions = new BitmapFactory.Options();
            this.mOptions.inDither = false;
            this.mOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        }

        void cancel() {
            this.mOptions.requestCancelDecode();
            super.cancel(true);
        }

        protected Bitmap doInBackground(final String... array) {
            if (this.isCancelled()) {
                return null;
            }
            try {
                return BitmapFactory.decodeFile(array[0], this.mOptions);
            }
            catch (OutOfMemoryError outOfMemoryError) {
                return null;
            }
        }

        protected void onPostExecute(final Bitmap bitmap) {
            if (bitmap == null) {
                return;
            }
            if (!this.isCancelled() && !this.mOptions.mCancel) {
                ImageBrowser.access$4(ImageBrowser.this, new BitmapDrawable(bitmap));
                if (!ImageBrowser.this.mbNextAnimation) {
                    ImageBrowser.this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation((Context)ImageBrowser.this, 2130968579));
                    ImageBrowser.this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation((Context)ImageBrowser.this, 2130968580));
                }
                else {
                    ImageBrowser.this.mViewFlipper.setInAnimation(AnimationUtils.loadAnimation((Context)ImageBrowser.this, 2130968577));
                    ImageBrowser.this.mViewFlipper.setOutAnimation(AnimationUtils.loadAnimation((Context)ImageBrowser.this, 2130968578));
                }
                ImageBrowser.this.mViewFlipper.showNext();
                ImageBrowser.this.mViewFlipper.invalidate();
                final ImageView imageView = (ImageView)ImageBrowser.this.mViewFlipper.getCurrentView();
                ImageBrowser.this.mBitmapMatrix.reset();
                imageView.setImageDrawable((Drawable)ImageBrowser.this.mCurrentImage);
                imageView.setImageMatrix(ImageBrowser.this.mBitmapMatrix);
                imageView.invalidate();
                return;
            }
            bitmap.recycle();
        }
    }

    private class LoadImages implements Runnable
    {
        private String mPath;

        public LoadImages(final String mPath) {
            this.mPath = mPath;
        }

        @Override
        public void run() {
            final File file = new File(this.mPath);
            if (file.exists()) {
                if (!file.isDirectory()) {
                    final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
                    bitmapFactory$Options.inDither = false;
                    bitmapFactory$Options.inSampleSize = 4;
                    final Bitmap decodeFile = BitmapFactory.decodeFile(this.mPath, bitmapFactory$Options);
                    if (decodeFile != null) {
                        ImageBrowser.this.mGalleryAdapter.Add(new BitmapInfo(decodeFile, this.mPath));
                    }
                    ImageBrowser.this.mHandler.obtainMessage(1).sendToTarget();
                    return;
                }
                final String[] list = file.list();
                final BitmapFactory.Options bitmapFactory$Options2 = new BitmapFactory.Options();
                bitmapFactory$Options2.inDither = false;
                bitmapFactory$Options2.inSampleSize = 4;
                if (list != null) {
                    for (int i = 0; i < list.length; ++i) {
                        if (!new File(String.valueOf(this.mPath) + "/" + list[i]).isDirectory() && list[i].length() > 4) {
                            final String substring = list[i].substring(-4 + list[i].length(), list[i].length());
                            if (substring.equalsIgnoreCase(".png") || substring.equalsIgnoreCase(".jpg") || substring.equalsIgnoreCase(".bmp")) {
                                final Bitmap decodeFile2 = BitmapFactory.decodeFile(String.valueOf(this.mPath) + "/" + list[i], bitmapFactory$Options2);
                                if (decodeFile2 != null) {
                                    ImageBrowser.this.mGalleryAdapter.Add(new BitmapInfo(decodeFile2, String.valueOf(this.mPath) + "/" + list[i]));
                                }
                            }
                        }
                    }
                }
            }
            ImageBrowser.this.mHandler.obtainMessage(1).sendToTarget();
        }
    }
}
