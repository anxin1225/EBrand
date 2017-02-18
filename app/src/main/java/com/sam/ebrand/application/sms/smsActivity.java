package com.sam.ebrand.application.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.sam.ebrand.R;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.application.thread.GetPeopleListTask;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.beans.DataPackage;
import com.sam.ebrand.meetingNetwork.beans.PeopleBean;
import com.sam.ebrand.meetingNetwork.beans.SmsBean;
import com.sam.ebrand.meetingNetwork.http.app.Sentsms;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.ExitApplication;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by sam on 2016/11/19.
 */

public class smsActivity extends Activity implements View.OnClickListener, SocketManager.Observer
{
    private Button administratorbtn;
    String fromName;
    private AlertDialog mDialog;
    ImageButton mGoBackBtn;
    String mId;
    String mId1;
    ImageButton mMainBtn;
    private ArrayList<Integer> mReceiverList;
    int mSelectedItem;
    ImageButton mSystemNoticeBtn;
    ImageButton mWelcomeBtn;
    List<PeopleBean> mdraftBean;
    String[] mstr;
    private Button packet;
    private Button sent;
    private EditText sentTople;
    String smsId;
    String smsTxt;
    private EditText smsbigTxt;
    float x1;
    float x2;
    float y1;
    float y2;

    public smsActivity() {
        this.x1 = 0.0f;
        this.x2 = 0.0f;
        this.y1 = 0.0f;
        this.y2 = 0.0f;
        this.mId = "";
        this.mId1 = "";
        this.mstr = new String[0];
    }

    static /* synthetic */ void access$3(final smsActivity smsActivity, final AlertDialog mDialog) {
        smsActivity.mDialog = mDialog;
    }

    private void initview() {
        this.mMainBtn = (ImageButton)this.findViewById(R.id.btn_main);
        this.mWelcomeBtn = (ImageButton)this.findViewById(R.id.btn_welcome);
        this.mSystemNoticeBtn = (ImageButton)this.findViewById(R.id.btn_systemnotice);
        this.mGoBackBtn = (ImageButton)this.findViewById(R.id.btn_goback);
        this.mMainBtn.setOnClickListener((View.OnClickListener)this);
        this.mWelcomeBtn.setOnClickListener((View.OnClickListener)this);
        this.mSystemNoticeBtn.setOnClickListener((View.OnClickListener)this);
        this.mGoBackBtn.setOnClickListener((View.OnClickListener)this);
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
        switch (n) {
            default: {}
        }
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_main: {
                final Intent intent = new Intent((Context)this, (Class)mainActivity.class);
                intent.setFlags(67108864);
                this.startActivity(intent);
            }
            case R.id.btn_welcome: {
                final Intent intent2 = new Intent((Context)this, (Class)WelcomeActivity.class);
                intent2.setFlags(67108864);
                this.startActivity(intent2);
            }
            case R.id.btn_systemnotice: {
                this.startActivity(new Intent((Context)this, (Class)NoticeActivity.class));
            }
            case R.id.btn_goback: {
                this.finish();
            }
            default: {}
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(R.layout.sms);
        ExitApplication.getInstance().addActivity(this);
        this.sentTople = (EditText)this.findViewById(R.id.txt_receiver);
        this.smsbigTxt = (EditText)this.findViewById(R.id.txt_smscontext);
        ((LinearLayout)this.findViewById(R.id.mainbackground)).setBackgroundDrawable(BackgroundManager.getInstance().getBackground());
        this.mdraftBean = new ArrayList<PeopleBean>();
        this.mReceiverList = new ArrayList<Integer>();
        if (!MeetingParam.PeopleName.equals("")) {
            this.sentTople.setText((CharSequence)MeetingParam.PeopleName);
            this.mId1 = String.valueOf(MeetingParam.PeopleId) + ",";
        }
        this.initview();
        (this.sent = (Button)this.findViewById(R.id.btn_send)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                smsActivity.this.smsTxt = smsActivity.this.smsbigTxt.getText().toString();
                if (smsActivity.this.smsTxt.equals("")) {
                    ToastHint.show((Context)smsActivity.this, "没填写内容");
                    return;
                }
                if (smsActivity.this.mReceiverList.size() == 0) {
                    ToastHint.show((Context)smsActivity.this, "没有选择收件人");
                    return;
                }
                final StringBuffer sb = new StringBuffer();
                for (int i = 0; i < smsActivity.this.mReceiverList.size(); ++i) {
                    sb.append(smsActivity.this.mReceiverList.get(i));
                    if (-1 + smsActivity.this.mReceiverList.size() != i) {
                        sb.append(",");
                    }
                }
                sb.append("|");
                sb.append(SettingManager.UserName);
                sb.append(",");
                sb.append(smsActivity.this.smsTxt);
                final byte[] bytes = sb.toString().getBytes();
                final byte[] array = new byte[12 + bytes.length];
                DataPackage.intToBytes(38, array, 0);
                DataPackage.intToBytes(1, array, 4);
                DataPackage.intToBytes(bytes.length, array, 8);
                System.arraycopy(bytes, 0, array, 12, bytes.length);
                SocketManager.getInstance().sendMessage(array);
                smsActivity.this.smsbigTxt.setText((CharSequence)"");
                smsActivity.this.mReceiverList.clear();
                smsActivity.this.sentTople.setText((CharSequence)"");
                ToastHint.show((Context)smsActivity.this, "消息已发送");
            }
        });
        (this.administratorbtn = (Button)this.findViewById(R.id.btn_sendadmin)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                final String format = new SimpleDateFormat("yyyy-MM-dd   HH:mm:ss     ").format(new Date(System.currentTimeMillis()));
                smsActivity.this.smsTxt = smsActivity.this.smsbigTxt.getText().toString();
                if (!smsActivity.this.smsTxt.equals("")) {
                    final SmsBean smsBean = new SmsBean();
                    smsBean.setSendername("系统");
                    smsBean.setAdd_time(format);
                    smsBean.setContent(smsActivity.this.smsTxt);
                    smsBean.setType("\u6211\u7684");
                    new ArrayList<SmsBean>().add(smsBean);
                    ProgressDialogHint.Show((Context)smsActivity.this, "提示", "正在发送...");
                    final Sentsms sentsms = new Sentsms();
                    sentsms.Request((Context)smsActivity.this, "-1", smsActivity.this.smsTxt);
                    sentsms.SetOnResultListener(new Sentsms.onSentsmsListener() {
                        @Override
                        public int OnResultHandle(final int n, final String s, final List list) {
                            ProgressDialogHint.Dismiss();
                            ToastHint.show((Context)smsActivity.this, s);
                            return 0;
                        }
                    });
                }
                else {
                    ToastHint.show((Context)smsActivity.this, "您没填写内容");
                }
                smsActivity.this.smsbigTxt.setText((CharSequence)"");
                smsActivity.this.sentTople.setText((CharSequence)"");
            }
        });
        (this.packet = (Button)this.findViewById(R.id.btn_sendall)).setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                smsActivity.this.smsTxt = smsActivity.this.smsbigTxt.getText().toString();
                if (smsActivity.this.smsTxt.equals("")) {
                    ToastHint.show((Context)smsActivity.this, "没填写内容");
                    return;
                }
                final byte[] bytes = (String.valueOf(SettingManager.UserName) + "," + smsActivity.this.smsTxt).getBytes();
                final byte[] array = new byte[12 + bytes.length];
                DataPackage.intToBytes(38, array, 0);
                DataPackage.intToBytes(0, array, 4);
                DataPackage.intToBytes(bytes.length, array, 8);
                System.arraycopy(bytes, 0, array, 12, bytes.length);
                SocketManager.getInstance().sendMessage(array);
                smsActivity.this.smsbigTxt.setText((CharSequence)"");
                ToastHint.show((Context)smsActivity.this, "消息已发送");
            }
        });
        this.sentTople.setInputType(0);
        this.sentTople.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                ProgressDialogHint.Show((Context)smsActivity.this, "提示", "正在获取列表...");
                HttpProtocalManager.getInstance().GetPeopleList(new GetPeopleListTask.onResultListener() {
                    @Override
                    public void ResultListener(final int n, final String s, final List<PeopleBean> mdraftBean) {
                        ProgressDialogHint.Dismiss();
                        if (mdraftBean != null) {
                            smsActivity.this.mdraftBean = mdraftBean;
                            final String[] array = new String[smsActivity.this.mdraftBean.size()];
                            for (int i = 0; i < smsActivity.this.mdraftBean.size(); ++i) {
                                array[i] = smsActivity.this.mdraftBean.get(i).getUsername();
                            }
                            System.out.println(array);
                            smsActivity.this.mstr = array;
                            smsActivity.this.setMultiChoiceItems(array);
                        }
                    }
                });
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        MeetingParam.PeopleName = "";
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        super.onResume();
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public void setMultiChoiceItems(final String[] array) {
        if (this.isFinishing()) {
            return;
        }
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
        final boolean[] array2 = new boolean[array.length];
        Arrays.fill(array2, false);
        (this.mDialog = new AlertDialog.Builder((Context)this).setTitle((CharSequence)"选择联系人").setIcon(android.R.drawable.ic_dialog_alert).setMultiChoiceItems((CharSequence[])array, array2, (DialogInterface.OnMultiChoiceClickListener)new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n, final boolean b) {
            }
        }).setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                smsActivity.this.mReceiverList.clear();
                String string = "";
                for (int i = 0; i < array2.length; ++i) {
                    if (array2[i]) {
                        smsActivity.this.mReceiverList.add(smsActivity.this.mdraftBean.get(i).getSocketID());
                        string = String.valueOf(string) + array[i] + ",";
                    }
                }
                smsActivity.this.sentTople.setText((CharSequence)string);
                smsActivity.access$3(smsActivity.this, null);
            }
        }).setNegativeButton((CharSequence)"取消", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                smsActivity.access$3(smsActivity.this, null);
            }
        }).create()).show();
    }
}
