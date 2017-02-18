package com.sam.ebrand.meetingNetwork.http.app;

import android.os.AsyncTask;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.beans.MingpaiBackgroundBean;
import com.sam.ebrand.meetingNetwork.http.HttpPlatform;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sam on 2016/12/5.
 */

public class GetMingpaiInfoTask extends AsyncTask<Void, Void, ParamDown>
{
    private OnResultListener mListener;

    public GetMingpaiInfoTask(final OnResultListener mListener) {
        this.mListener = mListener;
    }

    private String MakeJsonContentParam() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("mgID", (Object) SettingManager.getInstance().readSetting("mgID", "", "").toString());
        jsonObject.put("vision", (Object)"0");
        return jsonObject.toString();
    }

    protected ParamDown doInBackground(final Void... array) {
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 2;
        paramUp.type = "decice_get_background_info";
        paramUp.contentType = 0;
        while (true) {
            try {
                paramUp.content = this.MakeJsonContentParam();
                paramUp.type = HttpApp.GetURLFromType(paramUp.type);
                return HttpPlatform.HttpPost(paramUp);
            }
            catch (JSONException ex) {
                ex.printStackTrace();
                continue;
            }
        }
    }

    protected void onCancelled() {
        super.onCancelled();
        if (this.mListener != null) {
            this.mListener.CancelListener();
        }
    }

    protected void onPostExecute(final ParamDown paramDown) {
        super.onPostExecute(paramDown);
        if (!this.isCancelled() && this.mListener != null) {
            if (paramDown == null) {
                this.mListener.ResultListener(-1, "", null);
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
                        final List<MingpaiBackgroundBean> constructArrayList = MingpaiBackgroundBean.constructArrayList(jsonObject.getJSONArray("data"));
                        this.mListener.ResultListener(int1, string, constructArrayList);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                        final List<MingpaiBackgroundBean> constructArrayList = null;
                        continue;
                    }
                    break;
                }
            }
        }
    }

    public interface OnResultListener
    {
        void CancelListener();

        void ResultListener(final int p0, final String p1, final List<MingpaiBackgroundBean> p2);
    }
}
