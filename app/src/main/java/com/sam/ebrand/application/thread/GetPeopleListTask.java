package com.sam.ebrand.application.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.beans.PeopleBean;
import com.sam.ebrand.meetingNetwork.http.HttpPlatform;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sam on 2016/11/21.
 */

public class GetPeopleListTask extends AsyncTask<String, Void, ParamDown>
{
    public static final String TAG = "GetPeopleListTask";
    private onResultListener mResultListener;

    public GetPeopleListTask(final onResultListener mResultListener) {
        this.mResultListener = mResultListener;
    }

    private String MakeJsonContentParam(final String s) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("mgID", (Object) SettingManager.getInstance().readSetting("mgID", "", "").toString());
        if (!s.equals("")) {
            jsonObject.put("userId", (Object)s);
        }
        return jsonObject.toString();
    }

    public void cancel() {
        super.cancel(true);
    }

    protected ParamDown doInBackground(final String... array) {
        if (this.isCancelled()) {
            return null;
        }
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 2;
        paramUp.type = "user_get_basic_info";
        paramUp.contentType = 0;
        while (true) {
            try {
                paramUp.content = this.MakeJsonContentParam(array[0]);
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
                this.mResultListener.ResultListener(-1, "", null);
            }
            else {
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                Log.e("GetPeopleListTask", replace);
                int int1 = -1;
                String string = "";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        final List<PeopleBean> constructArrayList = PeopleBean.constructArrayList(jsonObject.getJSONArray("userlist"));
                        this.mResultListener.ResultListener(int1, string, constructArrayList);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                        final List<PeopleBean> constructArrayList = null;
                        continue;
                    }
                    break;
                }
            }
        }
    }

    public interface onResultListener
    {
        void ResultListener(final int p0, final String p1, final List<PeopleBean> p2);
    }
}
