package com.sam.ebrand.application.meeting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sam.ebrand.R;
import com.sam.ebrand.application.ThreadPool;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.meeting.adapt.DraftFileAdapter;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.application.thread.FileType;
import com.sam.ebrand.application.thread.GetAgendaTask;
import com.sam.ebrand.application.thread.GetDraftTask;
import com.sam.ebrand.application.thread.GetScheduleTask;
import com.sam.ebrand.application.thread.LoadSDCardFileTask;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.MeetingFileManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.NetWorkRes;
import com.sam.ebrand.meetingNetwork.beans.DraftBean;
import com.sam.ebrand.meetingNetwork.manage.DownloaderManager;
import com.sam.ebrand.meetingNetwork.manage.HttpDownloader;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/11/19.
 */

public class MeetinginfoActivity extends Activity implements SocketManager.Observer, View.OnClickListener
{
    private ImageButton mBtnBrowsNative;
    private ImageButton mBtnBrowsParent;
    private ImageButton mBtnBrowsUdisk;
    private ImageButton mBtnClear;
    private String mCurrentUdiskPath;
    private List<DraftBean> mDraftBean;
    private ListView mDraftFilesView;
    private DraftFileAdapter mFileListAdapter;
    private ImageButton mGoBackBtn;
    private ImageButton mMainBtn;
    private RelativeLayout mMeetingDraftBg;
    private ImageButton mSystemNoticeBtn;
    private List<DraftBean> mUdiskList;
    private ImageButton mWelcomeBtn;

    public MeetinginfoActivity() {
        this.mCurrentUdiskPath = "/mnt/usb_storage";
    }

    static /* synthetic */ void access$4(final MeetinginfoActivity meetinginfoActivity, final String mCurrentUdiskPath) {
        meetinginfoActivity.mCurrentUdiskPath = mCurrentUdiskPath;
    }

    private void initview() {
        (this.mDraftFilesView = (ListView)this.findViewById(R.id.draftListView)).setAdapter((ListAdapter)this.mFileListAdapter);
        this.mDraftFilesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                final String docUrl = MeetinginfoActivity.this.mFileListAdapter.getFileList().get(n).getDocUrl();
                if (docUrl != null && !docUrl.equals("")) {
                    final String[] split = docUrl.split("/");
                    final String s = split[-1 + split.length];
                    final String decode;
                    try {
                        if (ThreadPool.getInstance().isDownloading(docUrl)) {
                            SocketManager.mStaticHandler.obtainMessage(1, "文件下载中").sendToTarget();
                            return;
                        }
                        decode = URLDecoder.decode(s, "utf-8");
                        final File file = new File(String.valueOf(MeetingParam.SDCARD_DOC) + decode);
                        if (file.exists() && file.canRead() && file.length() > 0L) {
                            FileUtils.openFile((Context)MeetinginfoActivity.this, file.getPath());
                            return;
                        }
                    }
                    catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                        return;
                    }
                    ThreadPool.getInstance().Execute(new Runnable() {
                        @Override
                        public void run() {
                            if (ThreadPool.getInstance().isDownloading(docUrl)) {
                                SocketManager.mStaticHandler.obtainMessage(1, "文件下载中").sendToTarget();
                                return;
                            }
                            ThreadPool.getInstance().insert(docUrl, decode);
                            final HttpDownloader httpDownloader = new HttpDownloader();
                            final int downloadFile = httpDownloader.downloadFile(docUrl, String.valueOf(MeetingParam.SDCARD_DOC) + decode);
                            ThreadPool.getInstance().remove(docUrl);
                            httpDownloader.release();
                            if (downloadFile != 1) {
                                SocketManager.mStaticHandler.obtainMessage(1, (Object)"文件下载失败！").sendToTarget();
                                return;
                            }
                            FileUtils.openFile((Context)MeetinginfoActivity.this, String.valueOf(MeetingParam.SDCARD_DOC) + decode);
                        }
                    });
                    return;
                }
                final File file2 = new File(String.valueOf(MeetinginfoActivity.this.mFileListAdapter.getFileList().get(n).getParentpath()) + MeetinginfoActivity.this.mFileListAdapter.getFileList().get(n).getDocName());
                if (!file2.isDirectory()) {
                    if (file2.exists()) {
                        Log.e("Directory", "open file path:" + file2.getPath());
                        FileUtils.openFile((Context)MeetinginfoActivity.this, file2.getPath());
                    }
                    return;
                }
                Log.e("Directory", "load sdcard file path:" + file2.getPath());
                if (MeetinginfoActivity.this.mFileListAdapter.isUdisk()) {
                    MeetinginfoActivity.access$4(MeetinginfoActivity.this, file2.getPath());
                    MeetinginfoActivity.this.LoadUdiskFileList(MeetinginfoActivity.this.mCurrentUdiskPath);
                    return;
                }
                ToastHint.show((Context)MeetinginfoActivity.this, "不支持浏览文件夹！");
            }
        });
    }

    public void LoadUdiskFileList(final String s) {
        if (s.equals("/mnt/usb_storage")) {
            this.mBtnBrowsParent.setVisibility(View.INVISIBLE);
        }
        else {
            this.mBtnBrowsParent.setVisibility(View.VISIBLE);
        }
        this.mUdiskList.clear();
        this.mFileListAdapter.notifyDataSetChanged();
        LoadSDCardFileTask.Load(s, new LoadSDCardFileTask.OnResultListener() {
            @Override
            public void Result(final List<FileType> list) {
                if (list != null && list.size() > 0) {
                    for (int i = 0; i < list.size(); ++i) {
                        final DraftBean draftBean = new DraftBean();
                        final FileType fileType = list.get(i);
                        final String mFileName = fileType.mFileName;
                        draftBean.setFileType(list.get(i).mType);
                        draftBean.setDocName(mFileName);
                        draftBean.setParentpath(fileType.mParentPath);
                        MeetinginfoActivity.this.mUdiskList.add(draftBean);
                    }
                    MeetinginfoActivity.this.mFileListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void RefreshFileList() {
        this.mDraftBean.clear();
        this.mFileListAdapter.notifyDataSetChanged();
        LoadSDCardFileTask.Load(MeetingParam.SDCARD_UDISK_DOC, (LoadSDCardFileTask.OnResultListener)new LoadSDCardFileTask.OnResultListener() {
            @Override
            public void Result(final List<FileType> list) {
                for (int i = 0; i < list.size(); ++i) {
                    final DraftBean draftBean = new DraftBean();
                    final FileType fileType = list.get(i);
                    final String mFileName = fileType.mFileName;
                    draftBean.setFileType(list.get(i).mType);
                    draftBean.setDocName(mFileName);
                    draftBean.setParentpath(fileType.mParentPath);
                    MeetinginfoActivity.this.mDraftBean.add(draftBean);
                }
                MeetinginfoActivity.this.mFileListAdapter.notifyDataSetChanged();
            }
        });
        HttpProtocalManager.getInstance().GetDraft(new GetDraftTask.onResultListener() {
            @Override
            public void ResultListener(final int n, final String s, final List<DraftBean> list) {
                if (list != null && list.size() > 0) {
                    MeetinginfoActivity.this.mDraftBean.addAll(list);
                    MeetinginfoActivity.this.mFileListAdapter.notifyDataSetChanged();
                    MeetingFileManager.getDraft(list);
                }
            }
        });
        HttpProtocalManager.getInstance().GetAgenda(new GetAgendaTask.OnResultListener() {
            @Override
            public void ResultListener(final int n, final String s, final String s2, final String realName) {
                final String replaceAll = s2.replace("%2f", "/").replace("%2F", "/").replace("%3A", ":").replaceAll("\\+", "%20");
                if (replaceAll != null && !replaceAll.equals("")) {
                    final String[] split = replaceAll.split("/");
                    final String docName = split[-1 + split.length];
                    final DraftBean draftBean = new DraftBean();
                    draftBean.setDocName(docName);
                    draftBean.setDocUrl(replaceAll);
                    draftBean.setRealName(realName);
                    draftBean.setDocType(2);
                    MeetinginfoActivity.this.mDraftBean.add(draftBean);
                    MeetinginfoActivity.this.mFileListAdapter.notifyDataSetChanged();
                    MeetingFileManager.getAccommodation(replaceAll);
                }
            }
        });
        HttpProtocalManager.getInstance().GetSchedule(new GetScheduleTask.OnResultListener() {
            @Override
            public void ResultListener(final int n, final String s, final String s2, final String realName) {
                final String replaceAll = s2.replace("%2f", "/").replace("%2F", "/").replace("%3A", ":").replaceAll("\\+", "%20");
                if (replaceAll != null && !replaceAll.equals("")) {
                    final String[] split = replaceAll.split("/");
                    final String docName = split[-1 + split.length];
                    final DraftBean draftBean = new DraftBean();
                    draftBean.setDocName(docName);
                    draftBean.setDocUrl(replaceAll);
                    draftBean.setRealName(realName);
                    draftBean.setDocType(2);
                    MeetinginfoActivity.this.mDraftBean.add(draftBean);
                    MeetinginfoActivity.this.mFileListAdapter.notifyDataSetChanged();
                    MeetingFileManager.getMeetingSchedule(replaceAll);
                }
            }
        });
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
            case R.id.btn_browsnative: {
                this.mFileListAdapter.updateFileList(this.mDraftBean, false);
                this.mBtnBrowsNative.setVisibility(View.VISIBLE);
                this.mBtnBrowsUdisk.setVisibility(View.VISIBLE);
                this.mBtnBrowsParent.setVisibility(View.INVISIBLE);
            }
            case R.id.btn_cleardata: {
                ((Dialog)new AlertDialog.Builder((Context)this).setTitle((CharSequence)"提示").setMessage((CharSequence)"是否清空本地会议资料？").setPositiveButton((CharSequence)"确定", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        DownloaderManager.getInstance().calcelAllDownloader();
                        FileUtils.removeFile(MeetingParam.SDCARD_DOC);
                        FileUtils.removeFile(MeetingParam.SDCARD_UDISK_DOC);
                        FileUtils.creatDir(MeetingParam.SDCARD_DOC);
                        FileUtils.creatDir(MeetingParam.SDCARD_UDISK_DOC);
                        ProgressDialogHint.Show((Context)MeetinginfoActivity.this, "提示", "正在删除,请稍后...");
                        new prossss().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
                    }
                }).setNegativeButton((CharSequence)"取消", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                    }
                }).create()).show();
            }
            case R.id.btn_browsudisk: {
                this.mFileListAdapter.updateFileList(this.mUdiskList, true);
                this.LoadUdiskFileList(this.mCurrentUdiskPath);
                this.mBtnBrowsNative.setVisibility(View.VISIBLE);
                this.mBtnBrowsUdisk.setVisibility(View.GONE);
                if (this.mCurrentUdiskPath.equals("/mnt/usb_storage")) {
                    this.mBtnBrowsParent.setVisibility(View.INVISIBLE);
                    return;
                }
                break;
            }
            case R.id.btn_browsparent: {
                final File file = new File(this.mCurrentUdiskPath);
                if (file.exists()) {
                    this.LoadUdiskFileList(this.mCurrentUdiskPath = file.getParent());
                    return;
                }
                break;
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(R.layout.meetinginfo);
        (this.mMainBtn = (ImageButton)this.findViewById(R.id.btn_main)).setOnClickListener((View.OnClickListener)this);
        (this.mWelcomeBtn = (ImageButton)this.findViewById(R.id.btn_welcome)).setOnClickListener(this);
        this.mSystemNoticeBtn = (ImageButton)this.findViewById(R.id.btn_systemnotice);
        this.mGoBackBtn = (ImageButton)this.findViewById(R.id.btn_goback);
        this.mSystemNoticeBtn.setOnClickListener((View.OnClickListener)this);
        this.mGoBackBtn.setOnClickListener((View.OnClickListener)this);
        this.mBtnClear = (ImageButton)this.findViewById(R.id.btn_cleardata);
        this.mBtnBrowsNative = (ImageButton)this.findViewById(R.id.btn_browsnative);
        this.mBtnBrowsParent = (ImageButton)this.findViewById(R.id.btn_browsparent);
        this.mBtnBrowsUdisk = (ImageButton)this.findViewById(R.id.btn_browsudisk);
        this.mBtnClear.setOnClickListener((View.OnClickListener)this);
        this.mBtnBrowsNative.setOnClickListener((View.OnClickListener)this);
        this.mBtnBrowsParent.setOnClickListener((View.OnClickListener)this);
        this.mBtnBrowsUdisk.setOnClickListener((View.OnClickListener)this);
        this.mDraftBean = new ArrayList<DraftBean>();
        this.mUdiskList = new ArrayList<DraftBean>();
        this.mFileListAdapter = new DraftFileAdapter((Context)this, this.mDraftBean, 1);
        ((RelativeLayout)this.findViewById(R.id.background)).setBackgroundDrawable(BackgroundManager.getInstance().getBackground());
        (this.mMeetingDraftBg = (RelativeLayout)this.findViewById(R.id.second_bg)).setBackgroundDrawable(BackgroundManager.getInstance().getBitmapDrawable((Context)this, 2130837752));
        NetWorkRes.NetWork();
        this.initview();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
        SocketManager.getInstance().detach((SocketManager.Observer)this);
    }

    protected void onResume() {
        super.onResume();
        this.RefreshFileList();
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    private class prossss extends AsyncTask<Void, Void, Void>
    {
        protected Void doInBackground(final Void... array) {
            try {
                Thread.sleep(2000L);
                return null;
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(final Void void1) {
            ToastHint.show((Context)MeetinginfoActivity.this, "删除成功!");
            Log.e("Delete", "删除成功");
            ProgressDialogHint.Dismiss();
            MeetinginfoActivity.this.mBtnBrowsNative.setVisibility(View.GONE);
            MeetinginfoActivity.this.mBtnBrowsParent.setVisibility(View.INVISIBLE);
            MeetinginfoActivity.this.mBtnBrowsUdisk.setVisibility(View.VISIBLE);
            MeetinginfoActivity.this.RefreshFileList();
        }
    }
}
