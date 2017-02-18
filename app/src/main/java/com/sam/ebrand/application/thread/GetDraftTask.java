package com.sam.ebrand.application.thread;

import android.os.AsyncTask;
import android.util.Log;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.beans.DraftBean;
import com.sam.ebrand.meetingNetwork.http.HttpPlatform;
import com.sam.ebrand.meetingNetwork.http.logic.HttpApp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by sam on 2016/11/21.
 */

public class GetDraftTask extends AsyncTask<Void, Void, ParamDown>
{
    public static final String TAG = "GetDraftTask";
    private onResultListener mResultListener;

    public GetDraftTask(final onResultListener mResultListener) {
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
        if (this.isCancelled()) {
            return null;
        }
        Log.e("GetDraftTask", "start");
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 2;
        paramUp.type = "metting_get_doc_list";
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
        if (!this.isCancelled()) {
            Log.e("GetDraftTask", "end");
            if (this.mResultListener != null) {
                if (paramDown == null) {
                    this.mResultListener.ResultListener(-1, "", null);
                    return;
                }
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                int int1 = -1;
                String string = "";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        final List<DraftBean> constructArrayList = DraftBean.constructArrayList(jsonObject.getJSONArray("list"));
                        this.mResultListener.ResultListener(int1, string, constructArrayList);
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
    }

    public interface onResultListener
    {
        void ResultListener(final int p0, final String p1, final List<DraftBean> p2);
    }
}
