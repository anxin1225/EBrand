package com.sam.ebrand.manage;

import android.os.AsyncTask;

import com.sam.ebrand.application.thread.GetAgendaTask;
import com.sam.ebrand.application.thread.GetDraftTask;
import com.sam.ebrand.application.thread.GetPeopleListTask;
import com.sam.ebrand.application.thread.GetScheduleTask;
import com.sam.ebrand.engine.LCDEngine;
import com.sam.ebrand.meetingNetwork.beans.PeopleBean;
import com.sam.ebrand.meetingNetwork.http.app.GetMeetingTitleTask;
import com.sam.ebrand.meetingNetwork.http.app.GetMingpaiBackgroundTask;
import com.sam.ebrand.meetingNetwork.http.app.GetMingpaiInfoTask;
import com.sam.ebrand.meetingNetwork.http.app.GetPortSettingTask;
import com.sam.ebrand.meetingNetwork.http.app.GetVoteTask;
import com.sam.ebrand.meetingNetwork.http.app.UploadCommentsTask;
import com.sam.ebrand.meetingNetwork.http.app.UploadPortSettingTask;
import com.sam.ebrand.meetingNetwork.http.app.UploadSignPictureTask;
import com.sam.ebrand.meetingNetwork.http.app.UploadVoteTask;
import com.sam.ebrand.meetingNetwork.http.app.VGAOutRequestTask;

import java.util.List;

/**
 * Created by sam on 2016/11/21.
 */

public class HttpProtocalManager
{
    private static HttpProtocalManager instance;
    private GetAgendaTask mGetAgendaTask;
    private GetDraftTask mGetDraftTask;
    private GetMeetingTitleTask mGetMeetingTask;
    private GetMingpaiBackgroundTask mGetMingpaiBackgroundTask;
    private GetMingpaiInfoTask mGetMingpaiInfoTask;
    private GetPeopleListTask mGetPeopleListTask;
    private GetPortSettingTask mGetPortSettingTask;
    private GetVoteTask mGetVoteTask;
    private GetScheduleTask mGetsScheduleTask;
    private UploadCommentsTask mUploadCommentsTask;
    private UploadPortSettingTask mUploadPortSettingTask;
    private UploadSignPictureTask mUploadSignPictureTask;
    private UploadVoteTask mUploadVoteTask;
    private VGAOutRequestTask mVgaOutRequestTask;

    private HttpProtocalManager() {
        this.mGetMeetingTask = null;
        this.mGetAgendaTask = null;
        this.mGetsScheduleTask = null;
        this.mGetDraftTask = null;
        this.mGetPeopleListTask = null;
        this.mGetVoteTask = null;
        this.mGetMingpaiInfoTask = null;
        this.mVgaOutRequestTask = null;
        this.mUploadVoteTask = null;
        this.mGetMingpaiBackgroundTask = null;
        this.mUploadPortSettingTask = null;
        this.mGetPortSettingTask = null;
        this.mUploadCommentsTask = null;
        this.mUploadSignPictureTask = null;
    }

    public static HttpProtocalManager getInstance() {
        if (HttpProtocalManager.instance == null) {
            HttpProtocalManager.instance = new HttpProtocalManager();
        }
        return HttpProtocalManager.instance;
    }

    public void CancelGetVote() {
        if (this.mGetVoteTask != null && this.mGetVoteTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetVoteTask.cancel(true);
        }
        this.mGetVoteTask = null;
    }

    public void GetAgenda(final GetAgendaTask.OnResultListener onResultListener) {
        if (this.mGetAgendaTask != null && this.mGetAgendaTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetAgendaTask.cancel();
        }
        this.mGetAgendaTask = (GetAgendaTask)new GetAgendaTask(onResultListener).execute(new Void[0]);
    }

    public void GetDraft(final GetDraftTask.onResultListener onResultListener) {
        if (this.mGetDraftTask != null && this.mGetDraftTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetDraftTask.cancel();
        }
        this.mGetDraftTask = (GetDraftTask)new GetDraftTask(onResultListener).execute(new Void[0]);
    }

    public void GetMeetingTitle(GetMeetingTitleTask.OnResultListener onResultListener) {
        if (this.mGetMeetingTask != null && this.mGetMeetingTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetMeetingTask.cancel();
        }
        if (onResultListener == null) {
            onResultListener = new GetMeetingTitleTask.OnResultListener() {
                @Override
                public void ResultListener(final int n, final String s, final String s2) {
                    if (s2 != null && n != -1) {
                        SettingManager.getInstance().writeSetting("meetingTitle", s2);
                    }
                }
            };
        }
        this.mGetMeetingTask = (GetMeetingTitleTask)new GetMeetingTitleTask(onResultListener).execute(new Void[0]);
    }

    public void GetMingpaiInfo(final GetMingpaiInfoTask.OnResultListener onResultListener) {
        if (this.mGetMingpaiInfoTask != null && this.mGetMingpaiInfoTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetMingpaiInfoTask.cancel(true);
        }
        this.mGetMingpaiInfoTask = (GetMingpaiInfoTask)new GetMingpaiInfoTask(onResultListener).execute(new Void[0]);
    }

    public void GetPeopleList(final GetPeopleListTask.onResultListener onResultListener) {
        if (this.mGetPeopleListTask != null && this.mGetPeopleListTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetPeopleListTask.cancel();
        }
        this.mGetPeopleListTask = (GetPeopleListTask)new GetPeopleListTask(onResultListener).execute(new String[] { "all" });
    }

    public void GetSchedule(final GetScheduleTask.OnResultListener onResultListener) {
        if (this.mGetsScheduleTask != null && this.mGetsScheduleTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetsScheduleTask.cancel();
        }
        this.mGetsScheduleTask = (GetScheduleTask)new GetScheduleTask(onResultListener).execute(new Void[0]);
    }

    public void GetUserInfo() {
        if (this.mGetPeopleListTask != null && this.mGetPeopleListTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetPeopleListTask.cancel();
        }
        this.mGetPeopleListTask = (GetPeopleListTask)new GetPeopleListTask((GetPeopleListTask.onResultListener)new GetPeopleListTask.onResultListener() {
            @Override
            public void ResultListener(final int n, final String s, final List<PeopleBean> list) {
                if (list != null && list.size() > 0) {
                    final PeopleBean peopleBean = list.get(0);
                    final String username = peopleBean.getUsername();
                    final String fontName = peopleBean.getFontName();
                    final int fontColor = peopleBean.getFontColor();
                    final int fontSize = peopleBean.getFontSize();
                    final String rank = peopleBean.getRank();
                    final String welcomeWord = peopleBean.getWelcomeWord();
                    SettingManager.getInstance().beginWrite();
                    SettingManager.getInstance().write("UserJobs", rank);
                    SettingManager.getInstance().write("WelcomeWrod", welcomeWord);
                    SettingManager.getInstance().write("USERNAME", username);
                    SettingManager.getInstance().write("usernamecolor", fontColor);
                    SettingManager.getInstance().write(SettingManager.FONTSIZE, fontSize);
                    SettingManager.getInstance().write(SettingManager.FONTNAME, fontName);
                    SettingManager.getInstance().endWrite();
                    if (username.equals("")) {
                        LCDEngine.customBacklightStatus();
                    }
                }
                else if (n != -1) {
                    SettingManager.getInstance().writeSetting("UserJobs", "");
                    SettingManager.getInstance().writeSetting("WelcomeWrod", "");
                    SettingManager.getInstance().writeSetting("USERNAME", "");
                    LCDEngine.customBacklightStatus();
                }
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] { "" });
    }

    public void GetUserInfo(final GetPeopleListTask.onResultListener onResultListener) {
        if (this.mGetPeopleListTask != null && this.mGetPeopleListTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetPeopleListTask.cancel();
        }
        this.mGetPeopleListTask = (GetPeopleListTask)new GetPeopleListTask(onResultListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] { "" });
    }

    public void GetVote(final GetVoteTask.OnResultListener onResultListener) {
        if (this.mGetVoteTask != null && this.mGetVoteTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetVoteTask.cancel(true);
        }
        this.mGetVoteTask = (GetVoteTask)new GetVoteTask(onResultListener).execute(new Void[0]);
    }

    public void UploadComment(final byte[] array, final String s, final UploadCommentsTask.OnResultListener onResultListener) {
        if (this.mUploadCommentsTask != null && this.mUploadCommentsTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mUploadCommentsTask.cancel(true);
        }
        this.mUploadCommentsTask = (UploadCommentsTask)new UploadCommentsTask(onResultListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new byte[][] { array, s.getBytes() });
    }

    public void UploadPortSetting(final String[] array) {
        if (this.mUploadPortSettingTask != null && this.mUploadPortSettingTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mUploadPortSettingTask.cancel(true);
        }
        this.mUploadPortSettingTask = (UploadPortSettingTask)new UploadPortSettingTask((UploadPortSettingTask.OnResultListener)new UploadPortSettingTask.OnResultListener() {
            @Override
            public void CancelListener() {
            }

            @Override
            public void ResultListener(final int n, final String s) {
                SocketManager.mStaticHandler.obtainMessage(1, (Object)s).sendToTarget();
            }
        }).execute(new String[][] { array });
    }

    public void UploadSignPicture(final byte[] array, final UploadSignPictureTask.OnResultListener onResultListener) {
        if (this.mUploadSignPictureTask != null && this.mUploadSignPictureTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mUploadSignPictureTask.cancel(true);
        }
        this.mUploadSignPictureTask = (UploadSignPictureTask)new UploadSignPictureTask(onResultListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new byte[][] { array });
    }

    public void VGARequest(final VGAOutRequestTask.OnResultListener onResultListener, final String[] array) {
        if (this.mVgaOutRequestTask != null && this.mVgaOutRequestTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mVgaOutRequestTask.cancel(true);
        }
        this.mVgaOutRequestTask = (VGAOutRequestTask)new VGAOutRequestTask(onResultListener).execute(new String[][] { array });
    }

    public void Vote(final UploadVoteTask.OnResultListener onResultListener, final String[] array) {
        if (this.mUploadVoteTask != null && this.mUploadVoteTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mUploadVoteTask.cancel(true);
        }
        this.mUploadVoteTask = (UploadVoteTask)new UploadVoteTask(onResultListener).execute(new String[][] { array });
    }

    public void getMingpaiBackground(final GetMingpaiBackgroundTask.OnResultListener onResultListener) {
        if (this.mGetMingpaiBackgroundTask != null && this.mGetMingpaiBackgroundTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetMingpaiBackgroundTask.cancel(true);
        }
        this.mGetMingpaiBackgroundTask = (GetMingpaiBackgroundTask)new GetMingpaiBackgroundTask(onResultListener).execute(new Void[0]);
    }

    public void getPortSetting() {
        if (this.mGetPortSettingTask != null && this.mGetPortSettingTask.getStatus() != AsyncTask.Status.FINISHED) {
            this.mGetPortSettingTask.cancel(true);
        }
        this.mGetPortSettingTask = (GetPortSettingTask)new GetPortSettingTask((GetPortSettingTask.OnResultListener)new GetPortSettingTask.OnResultListener() {
            @Override
            public void CancelListener() {
            }

            @Override
            public void ResultListener(final int n, final String s, final String s2, final String s3) {
                if (SocketNoticeManager.isNum(s2)) {
                    SettingManager.getInstance().writeSetting("VGA_INPORT", s2);
                }
                if (SocketNoticeManager.isNum(s3)) {
                    SettingManager.getInstance().writeSetting("VGA_OUTPORT", s3);
                }
            }
        }).execute(new Void[0]);
    }
}
