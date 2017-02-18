package com.sam.ebrand.meetingNetwork.http.app;

import android.os.AsyncTask;
import android.util.Log;

import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.meetingNetwork.ParamDown;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sam on 2016/12/5.
 */

public class UploadSignPictureTask extends AsyncTask<byte[], Void, ParamDown>
{
    public static final String TAG = "UploadComment";
    private OnResultListener mListener;

    public UploadSignPictureTask(final OnResultListener mListener) {
        this.mListener = mListener;
    }

    private String MakeJsonContentParam(final String s) throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("avatarUrl", (Object)s);
        jsonObject.put("mgID", (Object) SettingManager.getInstance().readSetting("mgID", "", "").toString());
        return jsonObject.toString();
    }

    protected ParamDown doInBackground(final byte[]... p0) {
        //
        // This method could not be decompiled.
        //
        // Original Bytecode:
        //
        //     0: aload_1
        //     1: iconst_0
        //     2: aaload
        //     3: astore_2
        //     4: new             Lcom/Infree/MeetingNetwork/win/wincomm/http/ParamUp;
        //     7: dup
        //     8: invokespecial   com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.<init>:()V
        //    11: astore_3
        //    12: aload_3
        //    13: iconst_1
        //    14: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.cookieAction:I
        //    17: invokestatic    com/Infree/MeetingNetwork/manager/SettingManager.getInstance:()Lcom/Infree/MeetingNetwork/manager/SettingManager;
        //    20: ldc             "mgID"
        //    22: ldc             ""
        //    24: ldc             ""
        //    26: invokevirtual   com/Infree/MeetingNetwork/manager/SettingManager.readSetting:(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    29: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //    32: astore          4
        //    34: aload_3
        //    35: new             Ljava/lang/StringBuilder;
        //    38: dup
        //    39: ldc             "upload_picture&mgID="
        //    41: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //    44: aload           4
        //    46: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    49: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    52: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.type:Ljava/lang/String;
        //    55: aload_3
        //    56: iconst_1
        //    57: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.contentType:I
        //    60: aload_3
        //    61: aload_2
        //    62: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.binaryData:[B
        //    65: aload_3
        //    66: aload           4
        //    68: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.content:Ljava/lang/String;
        //    71: aload_3
        //    72: new             Ljava/util/HashMap;
        //    75: dup
        //    76: invokespecial   java/util/HashMap.<init>:()V
        //    79: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.mHeaderParamsMap:Ljava/util/Map;
        //    82: aload_3
        //    83: getfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.mHeaderParamsMap:Ljava/util/Map;
        //    86: ldc             "datatype"
        //    88: ldc             "1"
        //    90: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    95: pop
        //    96: aload_3
        //    97: aload_3
        //    98: getfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.type:Ljava/lang/String;
        //   101: invokestatic    com/Infree/MeetingNetwork/http/logic/HttpApp.GetURLFromType:(Ljava/lang/String;)Ljava/lang/String;
        //   104: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.type:Ljava/lang/String;
        //   107: aload_3
        //   108: invokestatic    com/Infree/MeetingNetwork/win/wincomm/http/HttpPlatform.HttpPost:(Lcom/Infree/MeetingNetwork/win/wincomm/http/ParamUp;)Lcom/Infree/MeetingNetwork/win/wincomm/http/ParamDown;
        //   111: astore          6
        //   113: aload           6
        //   115: ifnonnull       120
        //   118: aconst_null
        //   119: areturn
        //   120: aload           6
        //   122: getfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamDown.result:Ljava/lang/String;
        //   125: ldc             "\r\n\r\n\r\n"
        //   127: ldc             ""
        //   129: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //   132: astore          7
        //   134: ldc             "UploadComment"
        //   136: aload           7
        //   138: invokestatic    android/util/Log.e:(Ljava/lang/String;Ljava/lang/String;)I
        //   141: pop
        //   142: ldc             ""
        //   144: astore          9
        //   146: new             Lorg/json/JSONObject;
        //   149: dup
        //   150: aload           7
        //   152: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //   155: ldc             "avatarUrl"
        //   157: invokevirtual   org/json/JSONObject.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   160: astore          13
        //   162: aload           13
        //   164: ldc             ""
        //   166: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   169: ifne            187
        //   172: aload           13
        //   174: ldc             "\\"
        //   176: ldc             ""
        //   178: invokevirtual   java/lang/String.replace:(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;
        //   181: astore          14
        //   183: aload           14
        //   185: astore          9
        //   187: aload           9
        //   189: ldc             ""
        //   191: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   194: ifne            275
        //   197: new             Lcom/Infree/MeetingNetwork/win/wincomm/http/ParamUp;
        //   200: dup
        //   201: invokespecial   com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.<init>:()V
        //   204: astore          11
        //   206: aload           11
        //   208: iconst_2
        //   209: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.cookieAction:I
        //   212: aload           11
        //   214: ldc             "user_checkin"
        //   216: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.type:Ljava/lang/String;
        //   219: aload           11
        //   221: iconst_0
        //   222: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.contentType:I
        //   225: aload           11
        //   227: aload_0
        //   228: aload           9
        //   230: invokespecial   com/Infree/MeetingNetwork/http/app/UploadSignPictureTask.MakeJsonContentParam:(Ljava/lang/String;)Ljava/lang/String;
        //   233: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.content:Ljava/lang/String;
        //   236: aload           11
        //   238: aload           11
        //   240: getfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.type:Ljava/lang/String;
        //   243: invokestatic    com/Infree/MeetingNetwork/http/logic/HttpApp.GetURLFromType:(Ljava/lang/String;)Ljava/lang/String;
        //   246: putfield        com/Infree/MeetingNetwork/win/wincomm/http/ParamUp.type:Ljava/lang/String;
        //   249: aload           11
        //   251: invokestatic    com/Infree/MeetingNetwork/win/wincomm/http/HttpPlatform.HttpPost:(Lcom/Infree/MeetingNetwork/win/wincomm/http/ParamUp;)Lcom/Infree/MeetingNetwork/win/wincomm/http/ParamDown;
        //   254: areturn
        //   255: astore          10
        //   257: aload           10
        //   259: invokevirtual   org/json/JSONException.printStackTrace:()V
        //   262: goto            187
        //   265: astore          12
        //   267: aload           12
        //   269: invokevirtual   org/json/JSONException.printStackTrace:()V
        //   272: goto            236
        //   275: aconst_null
        //   276: areturn
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ------------------------
        //  146    183    255    265    Lorg/json/JSONException;
        //  225    236    265    275    Lorg/json/JSONException;
        //
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }

    protected void onPostExecute(final ParamDown paramDown) {
        super.onPostExecute(paramDown);
        if (!this.isCancelled() && this.mListener != null) {
            if (paramDown == null) {
                this.mListener.ResultListener(-1, "", "");
            }
            else {
                final String replace = paramDown.result.replace("\r\n\r\n\r\n", "");
                Log.e("UploadComment", replace);
                int int1 = -1;
                String string = "";
                String string2 = "";
                while (true) {
                    try {
                        final JSONObject jsonObject = new JSONObject(replace);
                        int1 = jsonObject.getInt("code");
                        string = jsonObject.getString("msg");
                        string2 = jsonObject.getString("userId");
                        this.mListener.ResultListener(int1, string, string2);
                    }
                    catch (JSONException ex) {
                        ex.printStackTrace();
                        continue;
                    }
                    break;
                }
            }
        }
    }

    public interface OnResultListener
    {
        void CancelListener();

        void ResultListener(final int p0, final String p1, final String p2);
    }
}
