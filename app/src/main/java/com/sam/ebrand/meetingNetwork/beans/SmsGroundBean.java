package com.sam.ebrand.meetingNetwork.beans;

/**
 * Created by sam on 2016/11/23.
 */

public class SmsGroundBean
{
    private String add_time;
    private String avatarUrl;
    private String content;
    private String sendername;
    private int smsId;
    private String userId;

    public String getAdd_time() {
        return this.add_time;
    }

    public String getAvatarUrl() {
        return this.avatarUrl;
    }

    public String getContent() {
        return this.content;
    }

    public String getSendername() {
        return this.sendername;
    }

    public int getSmsId() {
        return this.smsId;
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

    public void setSendername(final String sendername) {
        this.sendername = sendername;
    }

    public void setSmsId(final int smsId) {
        this.smsId = smsId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }
}
