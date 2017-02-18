package com.sam.ebrand.meetingNetwork.socket.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import com.sam.ebrand.meetingNetwork.beans.SmsBean;

import java.util.ArrayList;

/**
 * Created by sam on 2016/11/23.
 */

public class MeetingDBManager
{
    private static MeetingDBManager _instance;
    private static MeetingDBOpenHelper kugameDbHelper;

    private void checkInitialized() {
        if (MeetingDBManager.kugameDbHelper == null) {
            throw new Error("DBManager is not initialized.");
        }
    }

    public static MeetingDBManager getInstance() {
        synchronized (MeetingDBManager.class) {
            if (MeetingDBManager._instance == null) {
                MeetingDBManager._instance = new MeetingDBManager();
            }
            return MeetingDBManager._instance;
        }
    }

    public int UpdataMessage(final String s) {
        try {
            this.checkInitialized();
            MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("UPDATE " + MeetingDBOpenHelper.TABLE_MESSAGE + " SET " + "isRead" + "=1" + " WHERE " + "userName" + "=?", new Object[] { s });
            return 1;
        }
        catch (SQLException ex) {
            ex.printStackTrace();
            return -1;
        }
    }

    public void clearDb() {
        this.resetMessageTable();
    }

    public void deleteFriendById(final long n, final int n2) {
        this.checkInitialized();
        MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("DELETE FROM " + MeetingDBOpenHelper.TABLE_FRIEND + " WHERE " + "userId" + "=?" + " AND " + "friend_type" + "=?", new Object[] { n, n2 });
    }

    public void deleteMessageByGroup(final String s) {
        this.checkInitialized();
        MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("DELETE FROM " + MeetingDBOpenHelper.TABLE_MESSAGE + " WHERE " + "userName" + "=? ", new Object[] { s });
    }

    public void deleteMessageById(final long n) {
        this.checkInitialized();
        MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("DELETE FROM " + MeetingDBOpenHelper.TABLE_MESSAGE + " WHERE " + "id" + "=?", new Object[] { n });
    }

    public ArrayList<SmsBean> getAllMessages() {
        this.checkInitialized();
        final Cursor rawQuery = MeetingDBManager.kugameDbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + MeetingDBOpenHelper.TABLE_MESSAGE, (String[])null);
        if (rawQuery.getCount() == 0) {
            rawQuery.close();
            return null;
        }
        final ArrayList<SmsBean> list = new ArrayList<SmsBean>();
        while (rawQuery.moveToNext()) {
            final SmsBean smsBean = new SmsBean();
            smsBean.setMessageId(rawQuery.getLong(rawQuery.getColumnIndex("id")));
            smsBean.setUserId(rawQuery.getString(rawQuery.getColumnIndex("fromUserId")));
            smsBean.setContent(rawQuery.getString(rawQuery.getColumnIndex("content")));
            smsBean.setAdd_time(rawQuery.getString(rawQuery.getColumnIndex("time")));
            smsBean.setSendername(rawQuery.getString(rawQuery.getColumnIndex("userName")));
            smsBean.setType(rawQuery.getString(rawQuery.getColumnIndex("type")));
            smsBean.setAvatarUrl(rawQuery.getString(rawQuery.getColumnIndex("avatarUrl")));
            smsBean.setSmsId(rawQuery.getString(rawQuery.getColumnIndex("smsId")));
            list.add(smsBean);
        }
        rawQuery.close();
        return list;
    }

    public ArrayList<SmsBean> getNameMessages(final String s) {
        this.checkInitialized();
        final Cursor rawQuery = MeetingDBManager.kugameDbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + MeetingDBOpenHelper.TABLE_MESSAGE + " where " + "userName" + "='" + s + "'", (String[])null);
        if (rawQuery.getCount() == 0) {
            return null;
        }
        final ArrayList<SmsBean> list = new ArrayList<SmsBean>();
        while (rawQuery.moveToNext()) {
            final SmsBean smsBean = new SmsBean();
            smsBean.setMessageId(rawQuery.getLong(rawQuery.getColumnIndex("id")));
            smsBean.setUserId(rawQuery.getString(rawQuery.getColumnIndex("fromUserId")));
            smsBean.setContent(rawQuery.getString(rawQuery.getColumnIndex("content")));
            smsBean.setAdd_time(rawQuery.getString(rawQuery.getColumnIndex("time")));
            smsBean.setSendername(rawQuery.getString(rawQuery.getColumnIndex("userName")));
            smsBean.setType(rawQuery.getString(rawQuery.getColumnIndex("type")));
            smsBean.setAvatarUrl(rawQuery.getString(rawQuery.getColumnIndex("avatarUrl")));
            smsBean.setSmsId(rawQuery.getString(rawQuery.getColumnIndex("smsId")));
            smsBean.setIsRead(rawQuery.getString(rawQuery.getColumnIndex("isRead")));
            list.add(smsBean);
        }
        rawQuery.close();
        MeetingDBManager.kugameDbHelper.close();
        return list;
    }

    public ArrayList<SmsBean> getTimeAllMessages() {
        this.checkInitialized();
        final Cursor rawQuery = MeetingDBManager.kugameDbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + MeetingDBOpenHelper.TABLE_MESSAGE + " ORDER BY " + "time" + " DESC", (String[])null);
        if (rawQuery.getCount() == 0) {
            return null;
        }
        final ArrayList<SmsBean> list = new ArrayList<SmsBean>();
        while (rawQuery.moveToNext()) {
            final SmsBean smsBean = new SmsBean();
            smsBean.setMessageId(rawQuery.getLong(rawQuery.getColumnIndex("id")));
            smsBean.setUserId(rawQuery.getString(rawQuery.getColumnIndex("fromUserId")));
            smsBean.setContent(rawQuery.getString(rawQuery.getColumnIndex("content")));
            smsBean.setAdd_time(rawQuery.getString(rawQuery.getColumnIndex("time")));
            smsBean.setSendername(rawQuery.getString(rawQuery.getColumnIndex("userName")));
            list.add(smsBean);
        }
        rawQuery.close();
        MeetingDBManager.kugameDbHelper.close();
        return list;
    }

    public void initialize(final Context context) {
        MeetingDBManager.kugameDbHelper = new MeetingDBOpenHelper(context);
    }

    public void insertMessage(final SmsBean smsBean) {
        this.checkInitialized();
        if (smsBean == null) {
            return;
        }
        final String smsId = smsBean.getSmsId();
        final Cursor rawQuery = MeetingDBManager.kugameDbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + MeetingDBOpenHelper.TABLE_MESSAGE + " where " + "smsId" + "='" + smsId + "'", (String[])null);
        if (rawQuery.getCount() < 1) {
            final String userId = smsBean.getUserId();
            final String content = smsBean.getContent();
            final String type = smsBean.getType();
            final String sendername = smsBean.getSendername();
            final String avatarUrl = smsBean.getAvatarUrl();
            final String add_time = smsBean.getAdd_time();
            final String isRead = smsBean.getIsRead();
            this.checkInitialized();
            MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("INSERT INTO " + MeetingDBOpenHelper.TABLE_MESSAGE + "(" + "fromUserId" + "," + "content" + "," + "time" + "," + "userName" + "," + "type" + "," + "pictureUrl" + "," + "smsId" + "," + "isRead" + ") VALUES (?,?,?,?,?,?,?,?)", new Object[] { userId, content, add_time, sendername, type, avatarUrl, smsId, isRead });
        }
        rawQuery.close();
        MeetingDBManager.kugameDbHelper.close();
    }

    public void resetAppTable() {
        this.checkInitialized();
        MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("DROP TABLE " + MeetingDBOpenHelper.TABLE_CLASS_APP);
        MeetingDBManager.kugameDbHelper.checkAndInitialize(MeetingDBManager.kugameDbHelper.getWritableDatabase());
    }

    public void resetFriendTable() {
        this.checkInitialized();
        MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("DROP TABLE " + MeetingDBOpenHelper.TABLE_FRIEND);
        MeetingDBManager.kugameDbHelper.checkAndInitialize(MeetingDBManager.kugameDbHelper.getWritableDatabase());
    }

    public void resetMessageTable() {
        this.checkInitialized();
        MeetingDBManager.kugameDbHelper.getWritableDatabase().execSQL("DROP TABLE " + MeetingDBOpenHelper.TABLE_MESSAGE);
        MeetingDBManager.kugameDbHelper.checkAndInitialize(MeetingDBManager.kugameDbHelper.getWritableDatabase());
    }
}
