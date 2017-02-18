package com.sam.ebrand.meetingNetwork.beans;

/**
 * Created by sam on 2016/11/22.
 */
public class DataPackage {
    public static float bytesToFloat(final byte[] array, final int n) {
        return Float.intBitsToFloat(bytesToInt(array, n));
    }

    public static int bytesToInt(final byte[] array, final int n) {
        return (0xFF & array[n]) + (0xFFFF & array[n + 1] << 8) + (0xFFFFFF & array[n + 2] << 16) + (-1 & array[n + 3] << 24);
    }

    public static short bytesToShort(final byte[] array, final int n) {
        return (short)((0xFF & array[n]) + (0xFFFF & array[n + 1] << 8));
    }

    public static void floatToBytes(final float n, final byte[] array, final int n2) {
        final int floatToIntBits = Float.floatToIntBits(n);
        array[n2] = (byte)floatToIntBits;
        intToBytes(floatToIntBits, array, n2);
    }

    public static void intToBytes(final int n, final byte[] array, final int n2) {
        array[n2] = (byte)(n & 0xFF);
        array[n2 + 1] = (byte)(0xFF & n >> 8);
        array[n2 + 2] = (byte)(0xFF & n >> 16);
        array[n2 + 3] = (byte)(0xFF & n >> 24);
    }

    public static void shortToBytes(final short n, final byte[] array, final int n2) {
        array[n2] = (byte)(n & 0xFF);
        array[n2 + 1] = (byte)(0xFF & n >> 8);
    }
}