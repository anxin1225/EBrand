package com.sam.ebrand.application.thread;

import android.os.AsyncTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by sam on 2016/11/21.
 */

public class LoadSDCardFileTask extends AsyncTask<String, Void, List<FileType>>
{
    private static LoadSDCardFileTask instance;
    private OnResultListener mListener;

    static {
        LoadSDCardFileTask.instance = null;
    }

    private LoadSDCardFileTask(final OnResultListener mListener) {
        this.mListener = mListener;
    }

    public static void Load(final String s, final OnResultListener onResultListener) {
        if (LoadSDCardFileTask.instance != null && LoadSDCardFileTask.instance.getStatus().equals((Object)AsyncTask.Status.FINISHED)) {
            LoadSDCardFileTask.instance.cancel(true);
        }
        LoadSDCardFileTask.instance = (LoadSDCardFileTask)new LoadSDCardFileTask(onResultListener).executeOnExecutor(LoadSDCardFileTask.THREAD_POOL_EXECUTOR, new String[] { s });
    }

    protected List<FileType> doInBackground(final String... array) {
        final File file = new File(array[0]);
        final ArrayList<FileType> list = new ArrayList<FileType>();
        if (file.exists()) {
            final String[] list2 = file.list();
            if (list2 != null) {
                for (int i = 0; i < list2.length; ++i) {
                    final File file2 = new File(String.valueOf(array[0]) + "/" + list2[i]);
                    if (file2.exists()) {
                        int n;
                        if (file2.isDirectory()) {
                            n = 1;
                        }
                        else {
                            n = 2;
                        }
                        list.add(new FileType(list2[i], String.valueOf(array[0]) + "/", n));
                    }
                }
            }
            Collections.sort(list, new FileTypeComparator());
        }
        return list;
    }

    protected void onPostExecute(final List<FileType> list) {
        super.onPostExecute(list);
        if (!this.isCancelled() && this.mListener != null) {
            this.mListener.Result(list);
        }
    }

    public interface OnResultListener
    {
        void Result(final List<FileType> p0);
    }
}
