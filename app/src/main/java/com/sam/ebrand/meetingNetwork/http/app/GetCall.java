package com.sam.ebrand.meetingNetwork.http.app;

import android.content.Context;
import android.os.Handler;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;
import com.sam.ebrand.meetingNetwork.http.logic.HttpTimeoutListener;
import com.sam.ebrand.meetingNetwork.http.logic.RespondType;

import java.util.List;
import org.json.*;

/**
 * Created by sam on 2016/11/19.
 */

public class GetCall
{
    private HttpApp httpApp;
    private Context mContext;
    private onSignListener mListener;
    private Handler mTimeoutHandler;
    private HttpTimeoutListener mTimeoutListener;
    private Runnable mTimeoutRunnable;
    private int mTimetoutDelay;

    public GetCall() {
        this.mListener = null;
        this.mTimeoutListener = null;
        this.mTimetoutDelay = 30000;
        this.mTimeoutHandler = new Handler();
        this.mTimeoutRunnable = new Runnable() {
            @Override
            public void run() {
                if (GetCall.this.mTimeoutListener != null) {
                    GetCall.this.RequestCancel();
                    GetCall.this.mTimeoutListener.OnTimeoutHandle();
                    GetCall.this.stopTimeoutHandler();
                }
            }
        };
    }

    private String MakeJsonContentParam(final String s, final String s2) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("userId", (Object)s);
        jsonObject.put("content", (Object)s2);
        jsonObject.put("mgID", (Object) SettingManager.getInstance().readSetting("mgID", "", "").toString());
        return jsonObject.toString();
    }

    private void RequestHandle(final ParamDown paramDown) {
        if (paramDown == null) {
            if (this.mListener != null) {
                this.mListener.OnResultHandle(-1, "", RespondType.BINARYDATA);
            }
        }
        else {
            final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
            int int1 = -1;
            String string = "";
            while (true) {
                try {
                    final JSONObject jsonObject = new JSONObject(replace);
                    int1 = jsonObject.getInt("code");
                    string = jsonObject.getString("msg");
                    if (this.mListener != null) {
                        this.mListener.OnResultHandle(int1, string, null);
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    continue;
                }
                break;
            }
        }
    }

    private void startTimeoutHandler() {
        if (this.mTimeoutHandler != null) {
            this.mTimeoutHandler.removeCallbacks(this.mTimeoutRunnable);
            this.mTimeoutHandler.postDelayed(this.mTimeoutRunnable, (long)this.mTimetoutDelay);
        }
    }

    private void stopTimeoutHandler() {
        this.mTimeoutHandler.removeCallbacks(this.mTimeoutRunnable);
    }

    public void Request(final Context mContext, final String s, final String s2) {
        this.mContext = mContext;
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 2;
        paramUp.type = "service_call";
        paramUp.contentType = 0;
        while (true) {
            try {
                paramUp.content = this.MakeJsonContentParam(s, s2);
                this.startTimeoutHandler();
                (this.httpApp = new HttpApp(this.mContext)).AppRequestBegin(paramUp, (HttpApp.AppOnRstListenerRegister)new HttpApp.AppOnRstListenerRegister() {
                    @Override
                    public int AppRstListenerRegister(final ParamDown paramDown) {
                        GetCall.this.stopTimeoutHandler();
                        GetCall.this.RequestHandle(paramDown);
                        return 0;
                    }
                });
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }

    public void RequestCancel() {
        if (this.httpApp != null) {
            this.httpApp.AppRequestCancel();
        }
    }

    public void SetOnResultListener(final onSignListener mListener) {
        this.mListener = mListener;
    }

    public void SetOnTimeoutListener(final HttpTimeoutListener mTimeoutListener, int mTimetoutDelay) {
        this.mTimeoutListener = mTimeoutListener;
        if (mTimetoutDelay == -1) {
            mTimetoutDelay = 30000;
        }
        this.mTimetoutDelay = mTimetoutDelay;
    }

    public interface onSignListener
    {
        int OnResultHandle(final int p0, final String p1, final List p2);
    }
}
