package com.sam.ebrand.meetingNetwork.http.app;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.beans.DraftBean;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;
import com.sam.ebrand.meetingNetwork.http.logic.HttpTimeoutListener;
import com.sam.ebrand.meetingNetwork.http.logic.RespondType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sam on 2016/11/23.
 */

public class SetIP
{
    private HttpApp httpApp;
    private onSetIdListener mListener;

    public SetIP() {
        this.mListener = null;
    }

    private String MakeJsonContentParam(final String s) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        if (!s.equals("")) {
            jsonObject.put("mgID", (Object)s);
            final String string = SettingManager.getInstance().readSetting(SettingManager.SOCKETID, "", "").toString();
            if (!string.equals("")) {
                jsonObject.put("socket_id", (Object)string);
            }
            else {
                jsonObject.put("socket_id", (Object)"0");
            }
            jsonObject.put("ip", true);
        }
        else {
            jsonObject.put("mgID", (Object)SettingManager.getInstance().readSetting("mgID", "", "").toString());
            final String string2 = SettingManager.getInstance().readSetting(SettingManager.SOCKETID, "", "").toString();
            if (!string2.equals("")) {
                jsonObject.put("socket_id", (Object)string2);
            }
            else {
                jsonObject.put("socket_id", (Object)"0");
            }
        }
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
                    final List<DraftBean> constructArrayList = DraftBean.constructArrayList(null);
                    if (this.mListener != null) {
                        this.mListener.OnResultHandle(int1, string, constructArrayList);
                    }
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                    final List<DraftBean> constructArrayList = null;
                    continue;
                }
                break;
            }
        }
    }

    public void Request(final String s) {
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 2;
        paramUp.type = "change_mingpai_ip";
        paramUp.contentType = 0;
        while (true) {
            try {
                paramUp.content = this.MakeJsonContentParam(s);
                (this.httpApp = new HttpApp()).AppRequestBegin(paramUp, (HttpApp.AppOnRstListenerRegister)new HttpApp.AppOnRstListenerRegister() {
                    @Override
                    public int AppRstListenerRegister(final ParamDown paramDown) {
                        SetIP.this.RequestHandle(paramDown);
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

    public void SetOnResultListener(final onSetIdListener mListener) {
        this.mListener = mListener;
    }

    public void SetOnTimeoutListener(final HttpTimeoutListener httpTimeoutListener, final int n) {
    }

    public interface onSetIdListener
    {
        int OnResultHandle(final int p0, final String p1, final List p2);
    }
}
