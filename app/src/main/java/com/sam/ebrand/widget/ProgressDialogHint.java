package com.sam.ebrand.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by sam on 2016/11/15.
 */
public class ProgressDialogHint {

    private static ProgressDialog mProgressDialog;

    static {
        ProgressDialogHint.mProgressDialog = null;
    }

    public static void Dismiss() {
        if (ProgressDialogHint.mProgressDialog != null) {
            ProgressDialogHint.mProgressDialog.dismiss();
            ProgressDialogHint.mProgressDialog = null;
        }
    }

    public static void Show(final Context context, final String title, final String message) {
        if (ProgressDialogHint.mProgressDialog != null) {
            ProgressDialogHint.mProgressDialog.dismiss();
            ProgressDialogHint.mProgressDialog = null;
        }
        final ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle((CharSequence)title);
        mProgressDialog.setMessage((CharSequence)message);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(true);
        mProgressDialog.show();
        ProgressDialogHint.mProgressDialog = mProgressDialog;
    }
}
