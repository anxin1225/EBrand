package com.sam.ebrand.application;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by sam on 2016/11/10.
 */

public class shortcutService extends Service implements View.OnTouchListener, View.OnClickListener
{
    private static shortcutService instance;

    static {
        shortcutService.instance = null;
    }

    public static shortcutService getInstance() {
        return shortcutService.instance;
    }

    private void refresh() {
    }

    public void Visible(final boolean b) {
    }

    public IBinder onBind(final Intent intent) {
        return null;
    }

    public void onClick(final View view) {
    }

    public void onCreate() {
        super.onCreate();
        (shortcutService.instance = this).Visible(false);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onStart(final Intent intent, final int n) {
        super.onStart(intent, n);
        this.refresh();
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return true;
    }

    public void refreshView(final int n, final int n2) {
    }
}
