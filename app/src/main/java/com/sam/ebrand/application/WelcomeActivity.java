package com.sam.ebrand.application;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.sam.ebrand.R;
import com.sam.ebrand.engine.LCDEngine;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.manage.SocketNoticeManager;
import com.sam.ebrand.manage.SubLcdManager;
import com.sam.ebrand.manage.SubLcdMsg;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

public class WelcomeActivity extends Activity {

    public static String MGID;
    private Button cancelButton;
    private Dialog dialog;
    int fileLen;
    private boolean first_run;
    String fontname;
    private LayoutInflater inflater;
    private ImageView mUsernamebmpIV;
    private EditText messageEditText;
    private Button sendMessage;
    private View sendMessageView;

    public WelcomeActivity() {
        this.first_run = true;
    }

    private void dialog() {
        if (this.dialog == null) {
            (this.dialog = new Dialog(this)).setCancelable(false);
            this.dialog.requestWindowFeature(1);
            this.initDialog();
            if (this.sendMessageView != null) {
                this.dialog.setContentView(this.sendMessageView);
            }
        }
        this.dialog.show();
    }

    public void dialogDismiss() {
        if (this.dialog != null) {
            this.dialog.dismiss();
            this.dialog.hide();
        }
    }

    public int getFlags() {
        return 0;
    }

    public void getMeetingTitle() {
        HttpProtocalManager.getInstance().GetMeetingTitle(null);
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void initDialog() {
        this.inflater = this.getLayoutInflater();
        this.sendMessageView = this.inflater.inflate(R.layout.send_message_dialog, (ViewGroup) null);
        this.messageEditText = (EditText) this.sendMessageView.findViewById(R.id.myComment);
        (this.sendMessage = (Button) this.sendMessageView.findViewById(R.id.sendBtn)).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    final String trim = WelcomeActivity.this.messageEditText.getText().toString().trim();
                    if (trim != null && !trim.equals("")) {
                        WelcomeActivity.MGID = trim;
                        ProgressDialogHint.Show((Context) WelcomeActivity.this, "提示", "正在上传桌牌ID...");
                        SocketManager.getInstance().mTimeoutTimer = 3;
                        SocketManager.getInstance().ResMgId(trim);
                    } else {
                        ToastHint.show((Context) WelcomeActivity.this, "桌牌ID不能为空");
                    }
                }
                WelcomeActivity.this.dialogDismiss();
                return false;
            }
        });
        (this.cancelButton = (Button) this.sendMessageView.findViewById(R.id.cancelBtn)).setOnTouchListener( new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    WelcomeActivity.this.dialogDismiss();
                }
                return false;
            }
        });
    }

    public void notice(final int n, final Object o) {
        switch (n) {
            default: {
            }
            case 32:
            case 33:
            case 34: {
              //  final SharedPreferences$Editor editor = SettingManager.getInstance().getEditor();
              //  editor.putBoolean("bCustomSetName", false);
               // editor.apply();
            }
        }
    }

    public void onBackPressed() {
        this.startActivity(new Intent((Context) this, (Class) mainActivity.class));
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(R.layout.activity_welcome);
        Log.e("networkJar", "version1.1");
        NetWorkRes.NetWork();
        this.mUsernamebmpIV = (ImageView) this.findViewById(R.id.view_usernamebmp);
        if (BackgroundManager.getInstance().getSublcdPicture() == null) {
            BackgroundManager.getInstance().UpdateSublcdDrawable();
        }
        if (!LCDEngine.backlightStatus) {
            this.mUsernamebmpIV.setBackgroundResource(0);
        } else {
            this.mUsernamebmpIV.setBackgroundDrawable( BackgroundManager.getInstance().getSublcdPicture());
        }
        this.first_run = (boolean)(Boolean) SettingManager.getInstance().readSetting("first_run", this.first_run, true);
        MeetingParam.MGID = SettingManager.getInstance().readSetting("mgID", "", "").toString();
        if (MeetingParam.MGID.equals("")) {
            this.dialog();
            return;
        }
        this.getMeetingTitle();
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer) this);
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer) this, (Context) this);
        final boolean boolean1 = SettingManager.getInstance().getSharedPrefrences().getBoolean("bCustomSetName", false);
        MeetingParam.MGID = (String) SettingManager.getInstance().readSetting("mgID", "", "");
        if (MeetingParam.MGID.equals("")) {
            this.dialog();
            if (!boolean1) {
                SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 1));
            } else {
                SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 0));
            }
        } else {
            if (!boolean1) {
                HttpProtocalManager.getInstance().GetUserInfo();
                SocketNoticeManager.getBackgroundpicture();
                HttpProtocalManager.getInstance().GetMeetingTitle(null);
            }
            SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 0));
        }
        super.onResume();
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        mainActivity.onTouchEvent(this, motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void showUserName() {
        if (!LCDEngine.backlightStatus) {
            this.mUsernamebmpIV.setBackgroundResource(0);
            return;
        }
        this.mUsernamebmpIV.setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getSublcdPicture());
    }
}
