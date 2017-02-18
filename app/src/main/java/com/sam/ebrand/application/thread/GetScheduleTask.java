package com.sam.ebrand.application.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.http.HttpPlatform;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 2016/11/21.
 */

public class GetScheduleTask extends AsyncTask<Void, Void, ParamDown>
{
    private OnResultListener mResultListener;

    public GetScheduleTask(final OnResultListener mResultListener) {
        this.mResultListener = mResultListener;
    }

    private String MakeJsonContentParam() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("mgID", (Object) SettingManager.getInstance().readSetting("mgID", "", "").toString());
        return jsonObject.toString();
    }

    public void cancel() {
        super.cancel(true);
    }

    protected ParamDown doInBackground(final Void... array) {
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 2;
        paramUp.type = "metting_get_agenda_list";
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

    protected void onPostExecute(final ParamDown paramDown) {
        super.onPostExecute(paramDown);
        if (!this.isCancelled() && this.mResultListener != null) {
            if (paramDown == null) {
                this.mResultListener.ResultListener(-1, "", "", "");
            }
            else {
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                Log.e("file", replace);
                int int1 = -1;
                String string = "";
                String string2 = "";
                String string3 = "";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        string2 = jsonObject.getString("docUrl");
                        string3 = jsonObject.getString("docName");
                        this.mResultListener.ResultListener(int1, string, string2, string3);
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
        void ResultListener(final int p0, final String p1, final String p2, final String p3);
    }
}
