package com.sam.ebrand.meetingNetwork.http.logic;

import android.content.Context;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.http.AES;
import com.sam.ebrand.meetingNetwork.http.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 2016/11/21.
 */

public class HttpApp
{
    private static final String AES_KEY = "7d25da286a4e35984c0eb9382ebfb4db";
    public static final String API_VERSION = "1.0";
    public static final String OS_SIGN = "android";
    private HttpClient mHttpClient;
    private ParamUp mHttpUpContent;
    private AppOnRstListenerRegister mOnRstListenerRegister;

    public HttpApp() {
        this.mOnRstListenerRegister = null;
        this.mHttpClient = new HttpClient();
    }

    public HttpApp(final Context context) {
        this.mOnRstListenerRegister = null;
        this.mHttpClient = new HttpClient();
    }

    public static String AES_Encode(final String s) {
        try {
            return AES.Aes_encode("7d25da286a4e35984c0eb9382ebfb4db", s);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static String GetURLFromType(final String s) {
        return String.valueOf(new StringBuilder("http://")
                .append((String) SettingManager.getInstance().readSetting("serverip", "", "192.168.1.103"))
                .append(":")
                .append(SettingManager.getInstance().readSetting("serverport","","80"))
                .append("/ajax_json.php?act=").toString()) + s;
    }

    public static void putCommonParam(final JSONObject jsonObject) throws JSONException {
        jsonObject.put("in_sign", (Object)"android");
    }

    public static void putHashParam(final JSONObject jsonObject) throws JSONException {
    }

    public void AppRequestBegin(final ParamUp mHttpUpContent, final AppOnRstListenerRegister mOnRstListenerRegister) {
        this.mOnRstListenerRegister = mOnRstListenerRegister;
        this.mHttpUpContent = mHttpUpContent;
        this.mHttpUpContent.type = GetURLFromType(mHttpUpContent.type);
        this.mHttpClient.HttpClientBegin(this.mHttpUpContent, (HttpClient.OnRstListenerRegister)new HttpClient.OnRstListenerRegister() {
            @Override
            public int RstListenerRegister(final ParamDown paramDown) {
                HttpApp.this.mOnRstListenerRegister.AppRstListenerRegister(paramDown);
                return 1;
            }
        });
    }

    public void AppRequestCancel() {
        this.mHttpClient.HttpClientCancel();
    }

    public boolean AppRequestIsCancel() {
        return this.mHttpClient.IsCancelled();
    }

    public interface AppOnRstListenerRegister
    {
        int AppRstListenerRegister(final ParamDown p0);
    }
}
