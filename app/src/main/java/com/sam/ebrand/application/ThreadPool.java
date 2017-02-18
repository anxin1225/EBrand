package com.sam.ebrand.application;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sam on 2016/11/18.
 */
public class ThreadPool {

    private static ThreadPool instance;
    private ConcurrentHashMap<String, String> mDownloadingFile;
    private ExecutorService mThreadPool;

    static {
        ThreadPool.instance = null;
    }

    private ThreadPool() {
        this.mThreadPool = Executors.newCachedThreadPool();
        this.mDownloadingFile = new ConcurrentHashMap<String, String>();
    }

    public static ThreadPool getInstance() {
        if (ThreadPool.instance == null) {
            ThreadPool.instance = new ThreadPool();
        }
        return ThreadPool.instance;
    }

    public void Execute(final Runnable runnable) {
        this.mThreadPool.submit(runnable);
    }

    public void insert(final String s, final String s2) {
        this.mDownloadingFile.put(s, s2);
    }

    public boolean isDownloading(final String s) {
        return this.mDownloadingFile.containsKey(s);
    }

    public void remove(final String s) {
        this.mDownloadingFile.remove(s);
    }
}
