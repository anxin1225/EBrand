package com.sam.ebrand.meetingNetwork.beans;

import android.util.Log;

/**
 * Created by sam on 2016/11/22.
 */
public class CmdDataBody {

    public int cmd;
    public int dataType;
    public byte[] payload_data;
    public int payload_len;

    public CmdDataBody(final int n) {
        this.payload_data = new byte[n];
    }

    public static void marshall(final CmdDataBody cmdDataBody, final byte[] array) {
        DataPackage.intToBytes(cmdDataBody.cmd, array, 0);
        DataPackage.intToBytes(cmdDataBody.dataType, array, 4);
        DataPackage.intToBytes(cmdDataBody.payload_len, array, 8);
        System.arraycopy(cmdDataBody.payload_data, 0, array, 12, cmdDataBody.payload_len);
    }

    public static CmdDataBody unmarshall(final byte[] array) {
        if (array == null) {
            Log.e("CmdDataBody", "unmarshall 出错，参数有问题");
            return null;
        }
        final CmdDataBody cmdDataBody = new CmdDataBody(array.length);
        cmdDataBody.cmd = DataPackage.bytesToInt(array, 0);
        cmdDataBody.dataType = DataPackage.bytesToInt(array, 4);
        cmdDataBody.payload_len = DataPackage.bytesToInt(array, 8);
        System.arraycopy(array, 12, cmdDataBody.payload_data, 0, cmdDataBody.payload_len);
        return cmdDataBody;
    }

    public int getId(final int n) {
        return n;
    }
}
