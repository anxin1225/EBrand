package com.sam.ebrand.application.notice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.application.NoticeRecordActivity;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.param.SampleParam;
import com.sam.ebrand.util.ExitApplication;

import java.io.UnsupportedEncodingException;

/**
 * Created by sam on 2016/11/17.
 */

public class NoticeActivity extends Activity implements SocketManager.Observer
{
    static TextView NoticeTxt;
    public static boolean isNoticeActivity;
    private RelativeLayout mNoticeBg;

    static {
        NoticeActivity.isNoticeActivity = false;
    }

    public static void startActivity(final Context context, final String noticeStr) {
        SampleParam.NoticeStr = noticeStr;
        context.startActivity(new Intent(context, (Class)NoticeActivity.class));
        NoticeActivity.isNoticeActivity = true;
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
        final byte[] array = (byte[])o;
        switch (n) {
            default: {}
            case 14: {
                try {
                    NoticeActivity.NoticeTxt.setText((CharSequence)("通知:" + new String(array, "gbk")));
                    return;
                }
                catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    return;
                }
            }
        }
    }

    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.notice);
        ExitApplication.getInstance().addActivity(this);
        NoticeActivity.NoticeTxt = (TextView)this.findViewById(R.id.NoticeTxt);
        if (!SampleParam.NoticeStr.equals("")) {
            NoticeActivity.NoticeTxt.setText((CharSequence)("通知： " + SampleParam.NoticeStr));
        }
        else {
            NoticeActivity.NoticeTxt.setText((CharSequence)"没有通知");
        }
        final LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.NoticeLinearLayout);
        linearLayout.setBackgroundDrawable(BackgroundManager.getInstance().getBackground());
        NetWorkRes.NetWork();
        (this.mNoticeBg = (RelativeLayout)this.findViewById(R.id.notice_bg)).setBackgroundDrawable((Drawable)BackgroundManager.getInstance().getBitmapDrawable((Context)this, 2130837795));
        linearLayout.setOnClickListener((View.OnClickListener)new View.OnClickListener() {
            public void onClick(final View view) {
                NoticeActivity.this.getWindow().getDecorView().setSystemUiVisibility(0x8 | NoticeActivity.this.getWindow().getDecorView().getSystemUiVisibility());
                NoticeActivity.this.finish();
            }
        });
        ((Button)this.findViewById(R.id.NoticeBtn)).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                NoticeActivity.this.startActivity(new Intent((Context)NoticeActivity.this, NoticeRecordActivity.class));
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        NoticeActivity.isNoticeActivity = false;
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        super.onResume();
    }
}
