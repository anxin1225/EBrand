package com.sam.ebrand.meetingNetwork.http.app;

import android.os.AsyncTask;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.http.HttpPlatform;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 2016/12/5.
 */

public class GetMeetingTitleTask extends AsyncTask<Void, Void, ParamDown>
{
    private OnResultListener mResultListener;

    public GetMeetingTitleTask(final OnResultListener mResultListener) {
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
        paramUp.type = "get_meeting_title";
        paramUp.contentType = 0;
        while (true) {
            try {
                paramUp.content = this.MakeJsonContentParam();
                paramUp.type = HttpPlatform.GetURLFromType(paramUp.type);
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
                this.mResultListener.ResultListener(-1, "", "");
            }
            else {
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                int int1 = -1;
                String string = "";
                String string2 = "";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        string2 = jsonObject.getString("title");
                        this.mResultListener.ResultListener(int1, string, string2);
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
        void ResultListener(final int p0, final String p1, final String p2);
    }
}
