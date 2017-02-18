package com.sam.ebrand.application;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.application.board.boardSetting;
import com.sam.ebrand.application.call.CallActivity;
import com.sam.ebrand.application.meeting.MeetinginfoActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.application.set.SetActivity;
import com.sam.ebrand.application.sms.smsActivity;
import com.sam.ebrand.application.thread.CopyFileThread;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.SettingChangeListener;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;
import com.sam.ebrand.widget.ToastHint;

import java.io.File;

/**
 * Created by sam on 2016/11/17.
 */

public class mainActivity extends Activity implements View.OnClickListener, SocketManager.Observer, SettingChangeListener
{
    protected static final boolean SoundEnabled = false;
    public static final String TAG = "mainActivity";
    public static float x1;
    public static float x2;
    public static float y1;
    public static float y2;
    private ImageButton mBackBtn;
    private ImageButton mBtnAutoimport;
    private ImageButton mMainBtn;
    private ImageButton mMeetingInfoBtn;
    private TextView mMeetingTitle;
    private ImageButton mServiceCallBtn;
    private ImageButton mSignBtn;
    private ImageButton mSmsbtn;
    private ImageButton mSystemSettingBtn;
    private ImageButton mSystenNoticeBtn;
    private ImageButton mWelcomeBtn;

    static {
        mainActivity.x1 = 0.0f;
        mainActivity.x2 = 0.0f;
        mainActivity.y1 = 0.0f;
        mainActivity.y2 = 0.0f;
    }

    private void initview() {
        (this.mSystenNoticeBtn = (ImageButton)this.findViewById(R.id.btn_systemnotice)).setOnClickListener((View.OnClickListener)this);
        (this.mWelcomeBtn = (ImageButton)this.findViewById(R.id.btn_welcome)).setOnClickListener((View.OnClickListener)this);
        (this.mBackBtn = (ImageButton)this.findViewById(R.id.btn_goback)).setOnClickListener((View.OnClickListener)this);
        this.mBackBtn.setVisibility(View.INVISIBLE);
        (this.mMainBtn = (ImageButton)this.findViewById(R.id.btn_main)).setOnClickListener((View.OnClickListener)this);
        this.mMainBtn.setVisibility(View.VISIBLE);
    }

    public static boolean onTouchEvent(final Activity activity, final MotionEvent motionEvent) {
        return toolBar(activity, motionEvent);
    }

    public static void startActivity(final Context context) {
        if (context != null) {
            context.startActivity(new Intent(context, (Class)mainActivity.class));
            SocketManager.ifMain = true;
            ToastHint.show(context, "服务器连接断开！");
        }
    }

    public static boolean toolBar(final Activity activity, final MotionEvent motionEvent) {
        final int systemUiVisibility = activity.getWindow().getDecorView().getSystemUiVisibility();
        if (motionEvent.getAction() == 0) {
            mainActivity.x1 = motionEvent.getX();
            mainActivity.y1 = motionEvent.getY();
        }
        if (motionEvent.getAction() == 1) {
            mainActivity.x2 = motionEvent.getX();
            mainActivity.y2 = motionEvent.getY();
            if (mainActivity.y1 - mainActivity.y2 > 50.0f) {
                if ((systemUiVisibility & 0x8) != 0x0) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(0);
                }
            }
            else if (mainActivity.y2 - mainActivity.y1 > 50.0f && (systemUiVisibility & 0x8) == 0x0) {
                activity.getWindow().getDecorView().setSystemUiVisibility(8);
                return true;
            }
        }
        return true;
    }

    public int getFlags() {
        return 0;
    }

    public void getMeetingTitle() {
        this.mMeetingTitle.setText((CharSequence) SettingManager.getInstance().readSetting("meetingTitle", "", ""));
        HttpProtocalManager.getInstance().GetMeetingTitle(null);
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
        switch (n) {
            default: {}
            case 13: {
                this.getMeetingTitle();
                SettingManager.getInstance().writeSetting("issignsuccess", "false");
            }
            case 16: {
                this.getMeetingTitle();
                this.mMeetingTitle.setText((CharSequence)"");
            }
        }
    }

    public void onBackPressed() {
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            default: {}
            case R.id.btn_setting: {
                this.startActivity(new Intent((Context)this, (Class)SetActivity.class));
            }
            case R.id.btn_sign: {
                this.startActivity(new Intent((Context)this, (Class)boardSetting.class));
            }
            case R.id.btn_servicecall: {
                this.startActivity(new Intent((Context)this, (Class)CallActivity.class));
            }
            case R.id.btn_welcome: {
                final Intent intent = new Intent((Context)this, (Class)WelcomeActivity.class);
                intent.setFlags(67108864);
                this.startActivity(intent);
            }
            case R.id.btn_meetinginfo: {
                this.startActivity(new Intent((Context)this, (Class)MeetinginfoActivity.class));
            }
            case R.id.btn_systemnotice: {
                this.startActivity(new Intent((Context)this, (Class)NoticeActivity.class));
            }
            case R.id.btn_sms: {
                this.startActivity(new Intent((Context)this, (Class)smsActivity.class));
            }
            case R.id.btn_autoimport: {
                ThreadPool.getInstance().Execute(new Runnable() {
                    @Override
                    public void run() {
                        if (!new File("/mnt/usb_storage/导入资料").exists()) {
                            SocketManager.mStaticHandler.obtainMessage(1, "导入资料不存在！").sendToTarget();
                            return;
                        }
                        SocketManager.mStaticHandler.obtainMessage(3, 1, 0, (Object)new String[] { "导入文件", "导入会议资料" }).sendToTarget();
                        if (!CopyFileThread.CopyFile("/mnt/usb_storage/导入资料/会议资料", MeetingParam.SDCARD_UDISK_DOC)) {
                            final Message obtain = Message.obtain();
                            obtain.what = 63235;
                            obtain.arg1 = 1;
                            SocketManager.mStaticHandler.sendMessageDelayed(obtain, 1000L);
                        }
                        if (!CopyFileThread.CopyFile("/mnt/usb_storage/导入资料/铭牌背景", MeetingParam.SDCARD_BACKGROUND_PIC_FOLDER)) {
                            final Message obtain2 = Message.obtain();
                            obtain2.what = 63235;
                            obtain2.arg1 = 1;
                            SocketManager.mStaticHandler.sendMessageDelayed(obtain2, 1000L);
                        }
                        while (true) {
                            try {
                                Thread.sleep(1000L);
                                SocketManager.mStaticHandler.obtainMessage(3, 0, 0).sendToTarget();
                                final String[] excel = FileUtils.readExcel(new File("/mnt/usb_storage/导入资料/人员资料.xls"));
                                if (excel != null) {
                                    SocketManager.mStaticHandler.obtainMessage(18, (Object)excel).sendToTarget();
                                    return;
                                }
                            }
                            catch (InterruptedException ex) {
                                ex.printStackTrace();
                                continue;
                            }
                            break;
                        }
                        SocketManager.mStaticHandler.obtainMessage(1, (Object)"导入资料不存在！").sendToTarget();
                    }
                });
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().setFlags(1024, 1024);
        this.requestWindowFeature(1);
        this.setContentView(R.layout.main_modi);
        (this.mServiceCallBtn = (ImageButton)this.findViewById(R.id.btn_servicecall)).setOnClickListener((View.OnClickListener)this);
        (this.mSystemSettingBtn = (ImageButton)this.findViewById(R.id.btn_setting)).setOnClickListener((View.OnClickListener)this);
        (this.mSignBtn = (ImageButton)this.findViewById(R.id.btn_sign)).setOnClickListener(this);
        (this.mMeetingInfoBtn = (ImageButton)this.findViewById(R.id.btn_meetinginfo)).setOnClickListener((View.OnClickListener)this);
        (this.mSmsbtn = (ImageButton)this.findViewById(R.id.btn_sms)).setOnClickListener((View.OnClickListener)this);
        (this.mBtnAutoimport = (ImageButton)this.findViewById(R.id.btn_autoimport)).setOnClickListener((View.OnClickListener)this);
        this.initview();
        this.mMeetingTitle = (TextView)this.findViewById(R.id.MeetingTitle);
        NetWorkRes.NetWork();
        ((LinearLayout)this.findViewById(R.id.mainbackground)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBackground());
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        SettingManager.getInstance().setChangeListener(null);
        super.onPause();
    }

    protected void onResume() {
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        this.getMeetingTitle();
        SettingManager.getInstance().setChangeListener(this);
        super.onResume();
    }

    public void onSettingChange(final String s, final Object o) {
        if (s.equals("meetingTitle")) {
            this.mMeetingTitle.setText((CharSequence)o);
        }
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        onTouchEvent(this, motionEvent);
        return super.onTouchEvent(motionEvent);
    }
}
