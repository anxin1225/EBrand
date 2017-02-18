package com.sam.ebrand.meetingNetwork.http;

import android.util.Log;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;
import com.sam.ebrand.meetingNetwork.http.logic.CustomHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

/**
 * Created by sam on 2016/11/22.
 */

public class HttpPlatform
{
    public static final String TAG = "HttpPlatform";
    private static CookieStore cookieStore;

    static {
        HttpPlatform.cookieStore = null;
    }

    public static String GetURLFromType(final String s) {
        return String.valueOf(new StringBuilder("http://").append((String) SettingManager.getInstance().readSetting("serverip", "", "192.168.1.103"))
                .append(":")
                .append(SettingManager.getInstance().readSetting("serverport","","80"))
                .append("/ajax_json.php?act=").toString()) + s;
    }

    public static ParamDown HttpPost(final ParamUp paramParamUp) {
        ParamDown localParamDown = new ParamDown();
        int i = paramParamUp.cookieAction;
        String str1 = paramParamUp.type;
        String str2 = paramParamUp.content;
        int j = paramParamUp.contentType;
        byte[] arrayOfByte = paramParamUp.binaryData;
        Iterator<String> localIterator;
        HttpPost localHttpPost = new HttpPost(str1);
        DefaultHttpClient localDefaultHttpClient = (DefaultHttpClient)CustomHttpClient.getInstance().getHttpClient();
        Log.d("HttpPlatform", "-----------------HttpPost");
        Log.d("HttpPlatform", str1);
        Log.d("HttpPlatform", str2);
        if (i == 2) {
            localDefaultHttpClient.setCookieStore(cookieStore);
        }
        switch (j)
        {
            default:
            case 0:
                for (;;)
                {
                    try
                    {
                        HttpResponse localHttpResponse = localDefaultHttpClient.execute(localHttpPost);
                        if (i == 1) {
                            cookieStore = localDefaultHttpClient.getCookieStore();
                        }
                        localParamDown.result = EntityUtils.toString(localHttpResponse.getEntity());
                        return localParamDown;
                    }
                    catch (ClientProtocolException localClientProtocolException)
                    {
                        localClientProtocolException.printStackTrace();
                        return null;
                    }
                    catch (IOException localIOException)
                    {
                        localIOException.printStackTrace();
                        continue;
                    }
                }
        }
    }
}
