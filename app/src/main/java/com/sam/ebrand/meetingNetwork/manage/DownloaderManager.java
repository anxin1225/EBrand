package com.sam.ebrand.meetingNetwork.manage;

import android.util.SparseArray;

/**
 * Created by sam on 2016/11/17.
 */
public class DownloaderManager {

    private static DownloaderManager instance;
    private SparseArray<HttpDownloader> mDownloaderList;

    static {
        DownloaderManager.instance = new DownloaderManager();
    }

    private DownloaderManager() {
        this.mDownloaderList = (SparseArray<HttpDownloader>)new SparseArray();
    }

    public static DownloaderManager getInstance() {
        return DownloaderManager.instance;
    }

    public void addDownloader(final int n, final HttpDownloader httpDownloader) {
        this.mDownloaderList.append(n, httpDownloader);
    }

    public void calcelAllDownloader() {
        for (int i = 0; i < this.mDownloaderList.size(); ++i) {
            ((HttpDownloader)this.mDownloaderList.valueAt(i)).chanceStatus(2);
            this.mDownloaderList.removeAt(i);
        }
    }

    public void cancelDownloader(final int n) {
        final HttpDownloader httpDownloader = (HttpDownloader)this.mDownloaderList.get(n);
        if (httpDownloader != null) {
            httpDownloader.chanceStatus(2);
            this.mDownloaderList.remove(n);
        }
    }
}
