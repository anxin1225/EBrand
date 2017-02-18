package com.sam.ebrand.meetingNetwork.beans;

import com.sam.ebrand.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/11/23.
 */

public class SmsBean
{
    private long MessageId;
    private String add_time;
    private String avatarUrl;
    private String content;
    private String isRead;
    private String sendername;
    private String smsId;
    private String type;
    private String userId;

    public SmsBean() {
    }

    public SmsBean(final JSONObject jsonObject) throws JSONException {
        this.smsId = JSONUtil.getString(jsonObject, "smsId", "");
        this.sendername = JSONUtil.getString(jsonObject, "senderName", "");
        this.content = JSONUtil.getString(jsonObject, "content", "").replace("\\r\\n", "");
        this.add_time = JSONUtil.getString(jsonObject, "add_time", "");
        this.userId = JSONUtil.getString(jsonObject, "senderId", "");
        this.avatarUrl = JSONUtil.getString(jsonObject, "avatarUrl", "");
        this.type = JSONUtil.getString(jsonObject, "", "");
        this.isRead = JSONUtil.getString(jsonObject, "ifRead", "");
    }

    public static List<SmsBean> constructArrayList(final JSONArray jsonArray) throws JSONException {
        if(jsonArray == null)
            return null;
        int length = jsonArray.length();
        if(length<1)
            return null;
        ArrayList list = new ArrayList<DraftBean>(length);

        for (int i=0;i<length;i++){
            list.add(new SmsBean(jsonArray.getJSONObject(i)));
        }
        return (List<SmsBean>)list;
    }

    public String getAdd_time() {
        return this.add_time;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getContent() {
        return this.content;
    }

    public String getIsRead() {
        return this.isRead;
    }

    public long getMessageId() {
        return this.MessageId;
    }

    public String getSendername() {
        return this.sendername;
    }

    public String getSmsId() {
        return this.smsId;
    }

    public String getType() {
        return this.type;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setAdd_time(final String add_time) {
        this.add_time = add_time;
    }

    public void setAvatarUrl(final String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public void setIsRead(final String isRead) {
        this.isRead = isRead;
    }

    public void setMessageId(final long messageId) {
        this.MessageId = messageId;
    }

    public void setSendername(final String sendername) {
        this.sendername = sendername;
    }

    public void setSmsId(final String smsId) {
        this.smsId = smsId;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}
