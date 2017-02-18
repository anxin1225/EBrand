package com.sam.ebrand.meetingNetwork.http;

import android.os.AsyncTask;

import com.sam.ebrand.meetingNetwork.ParamDown;
import com.sam.ebrand.meetingNetwork.ParamUp;

/**
 * Created by sam on 2016/11/21.
 */

public class HttpClient
{
    private ParamDown mDownContent;
    private ParamUp mHttpUpContent;
    private OnRstListenerRegister mOnRstListenerRegister;
    private AsyncRequest mRequest;

    public HttpClient() {
        this.mOnRstListenerRegister = null;
    }

    private boolean Begin(final ParamUp mHttpUpContent) {
        this.mHttpUpContent = mHttpUpContent;
        (this.mRequest = new AsyncRequest()).execute(new ParamUp[] { this.mHttpUpContent });
        return true;
    }

    static /* synthetic */ void access$0(final HttpClient httpClient, final ParamDown mDownContent) {
        httpClient.mDownContent = mDownContent;
    }

    public void HttpClientBegin(final ParamUp paramUp, final OnRstListenerRegister mOnRstListenerRegister) {
        this.mOnRstListenerRegister = mOnRstListenerRegister;
        this.Begin(paramUp);
    }

    public void HttpClientCancel() {
        this.mRequest.cancel(true);
    }

    public boolean IsCancelled() {
        return this.mRequest.isCancelled();
    }

    private class AsyncRequest extends AsyncTask<ParamUp, Void, ParamDown>
    {
        protected ParamDown doInBackground(final ParamUp... array) {
            HttpClient.access$0(HttpClient.this, HttpPlatform.HttpPost(array[0]));
            return HttpClient.this.mDownContent;
        }

        protected void onPostExecute(final ParamDown paramDown) {
            if (HttpClient.this.mOnRstListenerRegister != null) {
                HttpClient.this.mOnRstListenerRegister.RstListenerRegister(paramDown);
            }
        }
    }

    public interface OnRstListenerRegister
    {
        int RstListenerRegister(final ParamDown p0);
    }
}
