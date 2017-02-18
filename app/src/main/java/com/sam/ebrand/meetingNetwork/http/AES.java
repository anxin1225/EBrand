package com.sam.ebrand.meetingNetwork.http;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by sam on 2016/11/21.
 */

public class AES
{
    private static final String HEX = "0123456789abcdef";

    public static String Aes_decode(final String s, final String s2) throws Exception {
        return decrypt(s, s2);
    }

    public static String Aes_encode(final String s, final String s2) throws Exception {
        return encrypt(s, s2);
    }

    private static void appendHex(final StringBuffer sb, final byte b) {
        sb.append("0123456789abcdef".charAt(0xF & b >> 4)).append("0123456789abcdef".charAt(b & 0xF));
    }

    public static String decrypt(final String s, final String s2) throws Exception {
        return new String(decrypt(getRawKey(s.getBytes()), toByte(s2)));
    }

    private static byte[] decrypt(final byte[] array, final byte[] array2) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(array, "AES");
        final Cipher instance = Cipher.getInstance("AES");
        instance.init(2, secretKeySpec);
        return instance.doFinal(array2);
    }

    public static String encrypt(final String s, final String s2) throws Exception {
        return toHex(encrypt(getRawKey(s.getBytes()), s2.getBytes()));
    }

    private static byte[] encrypt(final byte[] array, final byte[] array2) throws Exception {
        final SecretKeySpec secretKeySpec = new SecretKeySpec(array, "AES");
        final Cipher instance = Cipher.getInstance("AES");
        instance.init(1, secretKeySpec);
        return instance.doFinal(array2);
    }

    public static String fromHex(final String s) {
        return new String(toByte(s));
    }

    private static byte[] getRawKey(final byte[] array) throws Exception {
        return array;
    }

    public static byte[] toByte(final String s) {
        final int n = s.length() / 2;
        final byte[] array = new byte[n];
        for (int i = 0; i < n; ++i) {
            array[i] = (byte)(Object)Integer.valueOf(s.substring(i * 2, 2 + i * 2), 16);
        }
        return array;
    }

    public static String toHex(final String s) {
        return toHex(s.getBytes());
    }

    public static String toHex(final byte[] array) {
        if (array == null) {
            return "";
        }
        final StringBuffer sb = new StringBuffer(2 * array.length);
        for (int i = 0; i < array.length; ++i) {
            appendHex(sb, array[i]);
        }
        return sb.toString();
    }
}
