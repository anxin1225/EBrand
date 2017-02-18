package com.sam.ebrand.meetingNetwork.http.logic;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.*;
import org.apache.http.params.*;

/**
 * Created by sam on 2016/11/21.
 */

public class CustomHttpClient
{
    private static CustomHttpClient instance;
    private HttpClient mClient;

    static {
        CustomHttpClient.instance = null;
    }

    private CustomHttpClient() {
        final BasicHttpParams basicHttpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(basicHttpParams, (ProtocolVersion)HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset((HttpParams)basicHttpParams, "UTF-8");
        HttpProtocolParams.setUseExpectContinue((HttpParams)basicHttpParams, true);
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", (SocketFactory)PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", (SocketFactory) SSLSocketFactory.getSocketFactory(), 443));
        this.mClient = (HttpClient)new DefaultHttpClient((ClientConnectionManager)new ThreadSafeClientConnManager((HttpParams)basicHttpParams, schemeRegistry), (HttpParams)basicHttpParams);
    }

    public static CustomHttpClient getInstance() {
        if (CustomHttpClient.instance == null) {
            CustomHttpClient.instance = new CustomHttpClient();
        }
        return CustomHttpClient.instance;
    }

    public HttpClient getHttpClient() {
        return this.mClient;
    }
}
