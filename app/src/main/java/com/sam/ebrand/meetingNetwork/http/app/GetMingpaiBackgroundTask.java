package com.sam.ebrand.meetingNetwork.http.app;

import android.os.AsyncTask;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.http.HttpPlatform;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 2016/12/5.
 */

public class GetMingpaiBackgroundTask extends AsyncTask<Void, Void, ParamDown>
{
    private OnResultListener mListener;

    public GetMingpaiBackgroundTask(final OnResultListener mListener) {
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
        paramUp.type = "decice_get_background_url";
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
                this.mListener.ResultListener(-1, "", "", "");
            }
            else {
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                int int1 = -1;
                String string = "";
                String string2 = "url";
                String string3 = "new_vision";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        string2 = jsonObject.getString("backgroundUrl");
                        string3 = jsonObject.getString("new_vision");
                        this.mListener.ResultListener(int1, string, string2, string3);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
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

        void ResultListener(final int p0, final String p1, final String p2, final String p3);
    }
}
