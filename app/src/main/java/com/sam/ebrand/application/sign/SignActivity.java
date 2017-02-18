package com.sam.ebrand.application.sign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.SettingChangeListener;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.http.app.UploadSignPictureTask;
import com.sam.ebrand.param.SampleParam;
import com.sam.ebrand.util.ExitApplication;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sam on 2016/12/5.
 */

public class SignActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback, SocketManager.Observer, SettingChangeListener
{
    public static final int MEDIA_TYPE_IMAGE = 1;
    private boolean isMain;
    private Camera mCamera;
    private ImageView mCameraPicture;
    private SurfaceView mCameraView;
    private ImageButton mGoBackBtn;
    private ImageButton mMainBtn;
    private final Camera.PictureCallback mPictureCallback;
    private TextView mSignClueTxt;
    private ImageButton mSignCofirmBtn;
    private SurfaceHolder mSurfaceHolder;
    private ImageButton mSystemNoticeBtn;
    private TextView mUserNameTxt;
    private ImageButton mWelcomeBtn;

    public SignActivity() {
        this.isMain = false;
        this.mPictureCallback = (Camera.PictureCallback)new Camera.PictureCallback() {
            public void onPictureTaken(final byte[] array, final Camera camera) {
                try {
                    ProgressDialogHint.Show((Context)SignActivity.this, "提示", "正在上传图片...");
                    HttpProtocalManager.getInstance().UploadSignPicture(array, new UploadSignPictureTask.OnResultListener() {
                        @Override
                        public void CancelListener() {
                            ProgressDialogHint.Dismiss();
                        }

                        @Override
                        public void ResultListener(final int n, final String s, final String userid) {
                            ProgressDialogHint.Dismiss();
                            if (n == 0 && userid != "") {
                                SampleParam.USERID = userid;
                                ToastHint.show((Context)SignActivity.this, "签到成功，系统返回主界面");
                                SignActivity.this.finish();
                                return;
                            }
                            ToastHint.show((Context)SignActivity.this, s);
                            SignActivity.this.finish();
                        }
                    });
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    private int FindBackCamera() {
        final Camera.CameraInfo camera$CameraInfo = new Camera.CameraInfo();
        for (int numberOfCameras = Camera.getNumberOfCameras(), i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, camera$CameraInfo);
            if (camera$CameraInfo.facing == 0) {
                return i;
            }
        }
        return -1;
    }

    private int FindFrontCamera() {
        final Camera.CameraInfo camera$CameraInfo = new Camera.CameraInfo();
        for (int numberOfCameras = Camera.getNumberOfCameras(), i = 0; i < numberOfCameras; ++i) {
            Camera.getCameraInfo(i, camera$CameraInfo);
            if (camera$CameraInfo.facing == 1) {
                return i;
            }
        }
        return -1;
    }

    static /* synthetic */ void access$1(final SignActivity signActivity, final Camera mCamera) {
        signActivity.mCamera = mCamera;
    }

    private void getCamera() {
        this.mCameraView = (SurfaceView)this.findViewById(R.id.cameraView);
        (this.mSurfaceHolder = this.mCameraView.getHolder()).addCallback((SurfaceHolder.Callback)this);
        this.mSurfaceHolder.setType(3);
    }

    private void getCamera1() {
        (this.mCameraView = (SurfaceView)this.findViewById(R.id.cameraView)).setVisibility(View.VISIBLE);
        (this.mSurfaceHolder = this.mCameraView.getHolder()).addCallback((SurfaceHolder.Callback)this);
        this.mSurfaceHolder.setType(3);
    }

    public static Camera getCameraInstance() {
        try {
            return Camera.open();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static File getOutputMediaFile(final int n) {
        final File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraUseApp");
        if (!file.exists() && !file.mkdirs()) {
            Log.d("CameraUse", "failed to create directory");
        }
        else {
            final String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            if (n == 1) {
                return new File(String.valueOf(file.getPath()) + File.separator + "IMG_" + format + ".jpg");
            }
        }
        return null;
    }

    private void initview() {
        this.mUserNameTxt = (TextView)this.findViewById(R.id.UserName);
        this.mSignClueTxt = (TextView)this.findViewById(R.id.SignClueText);
        (this.mMainBtn = (ImageButton)this.findViewById(R.id.btn_main)).setOnClickListener(this);
        (this.mWelcomeBtn = (ImageButton)this.findViewById(R.id.btn_welcome)).setOnClickListener(this);
        this.mSystemNoticeBtn = (ImageButton)this.findViewById(R.id.btn_systemnotice);
        this.mGoBackBtn = (ImageButton)this.findViewById(R.id.btn_goback);
        this.mSystemNoticeBtn.setOnClickListener(this);
        this.mGoBackBtn.setOnClickListener(this);
        (this.mSignCofirmBtn = (ImageButton)this.findViewById(R.id.btn_sign_comfirm)).setOnClickListener(this);
        this.getCamera();
    }

    private void releaseCamera() {
        if (this.mCamera != null) {
            this.mCamera.release();
            this.mCamera = null;
            this.getCamera();
        }
    }

    private boolean setCustomUserName() {
        final boolean boolean1 = SettingManager.getInstance().getSharedPrefrences().getBoolean("bCustomSetName", false);
        if (boolean1) {
            this.mUserNameTxt.setText((CharSequence)SettingManager.getInstance().getSharedPrefrences().getString("CustomUserName", ""));
        }
        return boolean1;
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
        switch (n) {
            default: {}
            case 13: {
                SettingManager.getInstance().writeSetting("issignsuccess", "false");
                this.mCameraView.setVisibility(View.GONE);
                this.mSignCofirmBtn.setVisibility(View.VISIBLE);
                this.getCamera1();
                this.mSignClueTxt.setVisibility(View.VISIBLE);
                this.mSignClueTxt.setText(R.string.signconfirmclue);
            }
            case 16: {
                this.mUserNameTxt.setText((CharSequence)"");
            }
        }
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case 2131427552: {
                this.mCameraView.setVisibility(View.VISIBLE);
                this.mCameraPicture.setVisibility(View.GONE);
                try {
                    this.mCamera.takePicture((Camera.ShutterCallback)null, null, this.mPictureCallback);
                }
                catch (RuntimeException ex) {
                    ex.fillInStackTrace();
                }
            }
            case R.id.btn_main: {
                if (!this.isMain) {
                    final Intent intent = new Intent((Context)this, (Class)mainActivity.class);
                    intent.setFlags(67108864);
                    this.startActivity(intent);
                    return;
                }
                break;
            }
            case R.id.btn_welcome: {
                this.startActivity(new Intent((Context)this, (Class)WelcomeActivity.class));
            }
            case R.id.btn_systemnotice: {
                this.startActivity(new Intent((Context)this, (Class)NoticeActivity.class));
            }
            case R.id.btn_goback: {
                this.finish();
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.sign_modi);
        ExitApplication.getInstance().addActivity(this);
        this.mCameraPicture = (ImageView)this.findViewById(R.id.cameraPicture);
        this.initview();
        this.mCameraView = (SurfaceView)this.findViewById(R.id.cameraView);
        this.getCamera();
        ((LinearLayout)this.findViewById(R.id.mainbackground)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBackground());
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        this.releaseCamera();
        SettingManager.getInstance().setChangeListener(null);
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        SettingManager.getInstance().setChangeListener(this);
        this.getCamera();
        this.mUserNameTxt.setText((CharSequence)SettingManager.getInstance().readSetting("USERNAME", "", ""));
        this.setCustomUserName();
        super.onResume();
    }

    public void onSettingChange(final String s, final Object o) {
        if (s.equals("USERNAME")) {
            this.mUserNameTxt.setText((CharSequence)o);
        }
    }

    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
    }

    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        new LoadCameraTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        if (this.mCamera != null) {
            this.mCamera.stopPreview();
            this.mCamera.release();
            this.mCamera = null;
        }
    }

    private class LoadCameraTask extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(final Void... array) {
            final int access$0 = SignActivity.this.FindFrontCamera();
            if (access$0 == 1) {
                SignActivity.access$1(SignActivity.this, Camera.open(access$0));
            }
            if (access$0 == -1) {
                SignActivity.access$1(SignActivity.this, Camera.open(SignActivity.this.FindBackCamera()));
            }
            return null;
        }

        protected void onPostExecute(final Void void1) {
            super.onPostExecute(void1);
            if (SignActivity.this.mCamera == null) {
                return;
            }
            while (true) {
                try {
                    SignActivity.this.mCamera.setPreviewDisplay(SignActivity.this.mSurfaceHolder);
                    final Camera.Parameters parameters = SignActivity.this.mCamera.getParameters();
                    parameters.setPictureFormat(256);
                    SignActivity.this.mCamera.setParameters(parameters);
                    SignActivity.this.mCamera.startPreview();
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
    }
}
