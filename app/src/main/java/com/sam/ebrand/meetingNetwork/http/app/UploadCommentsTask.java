package com.sam.ebrand.meetingNetwork.http.app;

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
 * Created by sam on 2016/12/5.
 */

public class UploadCommentsTask extends AsyncTask<byte[], Void, ParamDown>
{
    public static final String TAG = "UploadComment";
    private OnResultListener mListener;

    public UploadCommentsTask(final OnResultListener mListener) {
        this.mListener = mListener;
    }

    protected ParamDown doInBackground(final byte[]... array) {
        final byte[] binaryData = array[0];
        final String s = new String(array[1]);
        final ParamUp paramUp = new ParamUp();
        paramUp.cookieAction = 1;
        paramUp.type = "upload_pizhu&mgID=" + SettingManager.getInstance().readSetting("mgID", "", "").toString() + "&type=" + s;
        paramUp.contentType = 1;
        paramUp.binaryData = binaryData;
        paramUp.content = "";
        paramUp.type = HttpApp.GetURLFromType(paramUp.type);
        return HttpPlatform.HttpPost(paramUp);
    }

    protected void onPostExecute(final ParamDown paramDown) {
        super.onPostExecute(paramDown);
        if (!this.isCancelled() && this.mListener != null) {
            if (paramDown == null) {
                this.mListener.ResultListener(-1, "");
            }
            else {
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                Log.e("UploadComment", replace);
                int int1 = -1;
                String string = "";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        this.mListener.ResultListener(int1, string);
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

        void ResultListener(final int p0, final String p1);
    }
}
