package com.sam.ebrand.application.call;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sam.ebrand.R;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.meetingNetwork.http.app.GetCall;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import java.util.List;

/**
 * Created by sam on 2016/11/19.
 */

public class CallActivity extends Activity implements View.OnClickListener, SocketManager.Observer
{
    private String callString;
    private int coffeeClick;
    private int flowerClick;
    private ImageButton mCallConfirmBtn;
    private ImageView mCoffeBtn;
    private ImageView mFlowerBtn;
    private ImageButton mGoBackBtn;
    private ImageButton mMainBtn;
    private ImageView mMicBtn;
    private ImageView mPaperBtn;
    private ImageView mPenBtn;
    private EditText mServiceCallStr;
    private ImageButton mSystemNoticeBtn;
    private ImageView mTeaBtn;
    private ImageView mWaitorBtn;
    private ImageView mWaternBtn;
    private ImageButton mWelcomeBtn;
    private int microClick;
    private int name_serverClick;
    private int panClick;
    private int paperClick;
    private int teaClick;
    private int waterClick;

    public CallActivity() {
        this.callString = "";
        this.teaClick = 1;
        this.coffeeClick = 1;
        this.waterClick = 1;
        this.microClick = 1;
        this.panClick = 1;
        this.paperClick = 1;
        this.flowerClick = 1;
        this.name_serverClick = 1;
    }

    private void GetCall() {
        final GetCall getCall = new GetCall();
        final String string = this.mServiceCallStr.getText().toString();
        if (string.equals("")) {
            ToastHint.show((Context)this, "请先选择您需要的服务,谢谢!");
            return;
        }
        if ("14" != "") {
            ProgressDialogHint.Show((Context)this, "提示", "正在呼叫...");
            getCall.Request((Context)this, "14", string);
            getCall.SetOnResultListener((GetCall.onSignListener)new GetCall.onSignListener() {
                @Override
                public int OnResultHandle(final int n, final String s, final List list) {
                    ProgressDialogHint.Dismiss();
                    if (n == 0) {
                        CallActivity.this.finish();
                    }
                    ToastHint.show((Context)CallActivity.this, s);
                    return 0;
                }
            });
            return;
        }
        ToastHint.show((Context)this, "您还没签到吧？请先签到，才能使用呼叫服务，谢谢!");
    }

    private void getCallString() {
        this.mServiceCallStr.setText((CharSequence)this.callString);
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            default: {}
            case R.id.btn_tea: {
                if (this.teaClick == 1) {
                    this.callString = String.valueOf(this.callString) + "茶,";
                    ++this.teaClick;
                    this.mTeaBtn.setBackgroundResource(R.drawable.tea2);
                }
                else {
                    this.teaClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("茶,", "");
                    this.mTeaBtn.setBackgroundResource(R.drawable.tea1);
                }
                this.getCallString();
            }
            case R.id.btn_coffe: {
                if (this.coffeeClick == 1) {
                    this.callString = String.valueOf(this.callString) + "咖啡,";
                    ++this.coffeeClick;
                    this.mCoffeBtn.setBackgroundResource(R.drawable.coffe2);
                }
                else {
                    this.coffeeClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("咖啡,", "");
                    this.mCoffeBtn.setBackgroundResource(R.drawable.coffe1);
                }
                this.getCallString();
            }
            case R.id.btn_water: {
                if (this.waterClick == 1) {
                    this.callString = String.valueOf(this.callString) + "水,";
                    ++this.waterClick;
                    this.mWaternBtn.setBackgroundResource(R.drawable.waitor2);
                }
                else {
                    this.waterClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("水,", "");
                    this.mWaternBtn.setBackgroundResource(R.drawable.waitor1);
                }
                this.getCallString();
            }
            case R.id.btn_mic: {
                if (this.microClick == 1) {
                    this.callString = String.valueOf(this.callString) + "麦克风,";
                    ++this.microClick;
                    this.mMicBtn.setBackgroundResource(R.drawable.mic2);
                }
                else {
                    this.microClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("麦克风,", "");
                    this.mMicBtn.setBackgroundResource(R.drawable.mic1);
                }
                this.getCallString();
            }
            case R.id.btn_pen: {
                if (this.panClick == 1) {
                    this.callString = String.valueOf(this.callString) + "笔,";
                    ++this.panClick;
                    this.mPenBtn.setBackgroundResource(R.drawable.pen2);
                }
                else {
                    this.panClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("笔,", "");
                    this.mPenBtn.setBackgroundResource(R.drawable.pen1);
                }
                this.getCallString();
            }
            case R.id.btn_callconfirm: {
                this.GetCall();
            }
            case R.id.btn_paper: {
                if (this.paperClick == 1) {
                    this.callString = String.valueOf(this.callString) + "纸,";
                    ++this.paperClick;
                    this.mPaperBtn.setBackgroundResource(R.drawable.paper2);
                }
                else {
                    this.paperClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("纸,", "");
                    this.mPaperBtn.setBackgroundResource(R.drawable.paper1);
                }
                this.getCallString();
            }
            case R.id.btn_flower: {
                if (this.flowerClick == 1) {
                    this.callString = String.valueOf(this.callString) + "花,";
                    ++this.flowerClick;
                    this.mFlowerBtn.setBackgroundResource(R.drawable.flower2);
                }
                else {
                    this.flowerClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("花,", "");
                    this.mFlowerBtn.setBackgroundResource(R.drawable.flower1);
                }
                this.getCallString();
            }
            case R.id.btn_waitor: {
                if (this.name_serverClick == 1) {
                    this.callString = String.valueOf(this.callString) + "服务人员,";
                    ++this.name_serverClick;
                    this.mWaitorBtn.setBackgroundResource(R.drawable.waitor2);
                }
                else {
                    this.name_serverClick = 1;
                    this.callString = this.mServiceCallStr.getText().toString();
                    this.callString = this.callString.replace("服务人员,", "");
                    this.mWaitorBtn.setBackgroundResource(R.drawable.waitor1);
                }
                this.getCallString();
            }
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
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(R.layout.call_modi);
        (this.mMainBtn = (ImageButton)this.findViewById(R.id.btn_main)).setOnClickListener((View.OnClickListener)this);
        (this.mWelcomeBtn = (ImageButton)this.findViewById(R.id.btn_welcome)).setOnClickListener((View.OnClickListener)this);
        this.mSystemNoticeBtn = (ImageButton)this.findViewById(R.id.btn_systemnotice);
        this.mGoBackBtn = (ImageButton)this.findViewById(R.id.btn_goback);
        this.mSystemNoticeBtn.setOnClickListener((View.OnClickListener)this);
        this.mGoBackBtn.setOnClickListener((View.OnClickListener)this);
        this.mServiceCallStr = (EditText)this.findViewById(R.id.serviceCallEdText);
        (this.mCallConfirmBtn = (ImageButton)this.findViewById(R.id.btn_callconfirm)).setOnClickListener((View.OnClickListener)this);
        (this.mCoffeBtn = (ImageView)this.findViewById(R.id.btn_coffe)).setOnClickListener((View.OnClickListener)this);
        (this.mTeaBtn = (ImageView)this.findViewById(R.id.btn_tea)).setOnClickListener((View.OnClickListener)this);
        (this.mFlowerBtn = (ImageView)this.findViewById(R.id.btn_flower)).setOnClickListener((View.OnClickListener)this);
        (this.mWaitorBtn = (ImageView)this.findViewById(R.id.btn_waitor)).setOnClickListener((View.OnClickListener)this);
        (this.mMicBtn = (ImageView)this.findViewById(R.id.btn_mic)).setOnClickListener((View.OnClickListener)this);
        (this.mPenBtn = (ImageView)this.findViewById(R.id.btn_pen)).setOnClickListener((View.OnClickListener)this);
        (this.mPaperBtn = (ImageView)this.findViewById(R.id.btn_paper)).setOnClickListener((View.OnClickListener)this);
        (this.mWaternBtn = (ImageView)this.findViewById(R.id.btn_water)).setOnClickListener((View.OnClickListener)this);
        ((LinearLayout)this.findViewById(R.id.mainbackground)).setBackgroundDrawable(BackgroundManager.getInstance().getBackground());
        NetWorkRes.NetWork();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        super.onResume();
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }
}
