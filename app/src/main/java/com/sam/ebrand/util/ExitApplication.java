package com.sam.ebrand.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sam on 2016/11/17.
 */
public class ExitApplication {

    private static ExitApplication exitApplication;
    private List<Activity> listActivity;

    private ExitApplication() {
        this.listActivity = new ArrayList<Activity>();
    }

    public static ExitApplication getInstance() {
        if (ExitApplication.exitApplication == null) {
            ExitApplication.exitApplication = new ExitApplication();
        }
        return ExitApplication.exitApplication;
    }

    public void addActivity(final Activity activity) {
        this.listActivity.add(activity);
    }

    public void exit(final Context context) {
        ((Dialog)new AlertDialog.Builder(context).setTitle((CharSequence)"\u786e\u8ba4\u9000\u51fa").setMessage((CharSequence)"\u786e\u5b9a\u8981\u9000\u51fa\u4f1a\u8bae\u7cfb\u7edf?").setPositiveButton((CharSequence)"\u786e\u5b9a", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                final Iterator<Activity> iterator = ExitApplication.this.listActivity.iterator();
                while (iterator.hasNext()) {
                    iterator.next().finish();
                }
                System.exit(0);
            }
        }).setNegativeButton((CharSequence)"\u53d6\u6d88", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
            }
        }).create()).show();
    }
}
