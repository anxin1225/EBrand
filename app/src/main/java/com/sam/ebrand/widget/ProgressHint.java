package com.sam.ebrand.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by sam on 2016/11/10.
 */

public class ProgressHint
{
    private static ProgressDialog mProgressDialog;

    static {
        ProgressHint.mProgressDialog = null;
    }

    public static void Dismiss() {
        if (ProgressHint.mProgressDialog != null) {
            ProgressHint.mProgressDialog.dismiss();
            ProgressHint.mProgressDialog = null;
        }
    }

    public static void Show(final Context context, final String title, final String message) {
        if (ProgressHint.mProgressDialog != null) {
            ProgressHint.mProgressDialog.dismiss();
            ProgressHint.mProgressDialog = null;
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle((CharSequence)title);
        mProgressDialog.setMessage((CharSequence)message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        ProgressHint.mProgressDialog = mProgressDialog;
    }
}
