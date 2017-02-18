package com.sam.ebrand.application;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;

import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.util.ExitApplication;
import com.sam.ebrand.widget.ProgressDialogHint;

/**
 * Created by sam on 2016/11/17.
 */

public class NoticeRecordActivity extends Activity implements View.OnClickListener, SocketManager.Observer
{
    public static final int MEDIA_TYPE_IMAGE = 1;
    private RelativeLayout mNoticerecordBg;
    List<NoticeBean> mdraftBean;
    SurfaceView surfaceView;

    private void getPeopleList() {
        ProgressDialogHint.Show((Context)this, "\u63d0\u793a", "\u6b63\u5728\u4e0b\u8f7d\u6570\u636e...");
        final GetNotice getNotice = new GetNotice();
        getNotice.Request((Context)this);
        getNotice.SetOnResultListener((GetNotice.onGetNoticeListener)new GetNotice.onGetNoticeListener() {
            @Override
            public int OnResultHandle(final int n, final String s, final List<NoticeBean> mdraftBean) {
                if (n == 0) {
                    NoticeRecordActivity.this.mdraftBean = mdraftBean;
                    NoticeRecordActivity.this.initview();
                }
                ProgressDialogHint.Dismiss();
                return 0;
            }
        });
    }

    private void initview() {
        if (this.mdraftBean != null) {
            final GridView gridView = (GridView)this.findViewById(2131427338);
            final NoticeRecordlistAdater adapter = new NoticeRecordlistAdater(this, this.mdraftBean);
            gridView.setSelection(-1 + this.mdraftBean.size());
            gridView.setAdapter((ListAdapter)adapter);
        }
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
    }

    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    public void onClick(final View view) {
        view.getId();
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(2130903061);
        ExitApplication.getInstance().addActivity(this);
        this.mdraftBean = new ArrayList<NoticeBean>();
        NetWorkRes.NetWork();
        this.initview();
        this.getPeopleList();
        ((LinearLayout)this.findViewById(2131427482)).setBackgroundDrawable((Drawable)BackgroundManager.getInstance().getBackground());
        (this.mNoticerecordBg = (RelativeLayout)this.findViewById(2131427483)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBitmapDrawable((Context)this, 2130837833));
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
}
