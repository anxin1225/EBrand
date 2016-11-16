package com.sam.ebrand.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sam.ebrand.R;

/**
 * Created by sam on 2016/11/15.
 */
public class ToastHint {
    public static void show(final Context context, final int n) {
        Toast.makeText(context, context.getResources().getText(n), Toast.LENGTH_SHORT).show();
    }

    public static void show(final Context context, final String text) {
        if (context != null) {
            final View inflate = LayoutInflater.from(context).inflate(R.layout.setfont, (ViewGroup)null);
            ((TextView)inflate.findViewById(R.id.setFont)).setText((CharSequence)text);
            final Toast toast = new Toast(context);
            toast.setDuration(0);
            toast.setView(inflate);
            toast.setGravity(17, 0, 0);
            toast.show();
        }
    }

    public static void showBottom(final Context context, final String text) {
        final View inflate = LayoutInflater.from(context).inflate(R.layout.setfont, (ViewGroup)null);
        ((TextView)inflate.findViewById(R.id.setFont)).setText((CharSequence)text);
        final Toast toast = new Toast(context);
        toast.setDuration(1);
        toast.setView(inflate);
        toast.setGravity(80, 0, 0);
        toast.show();
    }

    public static void showShort(final Context context, final String s) {
        Toast.makeText(context, (CharSequence)s, 0).show();
    }

    public static void showdraft(final Context context, final String text) {
        if (context != null) {
            final View inflate = LayoutInflater.from(context).inflate(R.layout.setfont, (ViewGroup)null);
            ((TextView)inflate.findViewById(R.id.setFont)).setText((CharSequence)text);
            final Toast toast = new Toast(context);
            toast.setDuration(1);
            toast.setView(inflate);
            toast.setGravity(17, 0, 0);
            toast.show();
        }
    }
}
