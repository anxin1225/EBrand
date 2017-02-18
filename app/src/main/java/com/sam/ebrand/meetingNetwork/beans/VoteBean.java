package com.sam.ebrand.meetingNetwork.beans;

import android.view.View;

import com.sam.ebrand.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/12/5.
 */

public class VoteBean
{
    private String begin_date;
    private String can_multi;
    private int commentValue;
    private String end_date;
    private List<options> option;
    private String vote_id;
    private String vote_name;
    private int voted;

    public VoteBean(final JSONObject jsonObject) throws JSONException {
        this.commentValue = 0;
        this.vote_id = JSONUtil.getString(jsonObject, "vote_id", "");
        this.vote_name = JSONUtil.getString(jsonObject, "vote_name", "");
        this.begin_date = JSONUtil.getString(jsonObject, "begin_date", "");
        this.end_date = JSONUtil.getString(jsonObject, "end_date", "");
        this.voted = JSONUtil.getInt(jsonObject, "voted", 0);
        this.can_multi = JSONUtil.getString(jsonObject, "can_multi", "");
        this.option = options.optionsArrayList(JSONUtil.getJSONArray(jsonObject, "options", null));
    }

    public static List<VoteBean> constructArrayList(final JSONArray jsonArray) throws JSONException {
        if(jsonArray == null)
            return null;
        int length = jsonArray.length();
        if(length<1)
            return null;
        ArrayList list = new ArrayList<VoteBean>(length);

        for (int i=0;i<length;i++){
            list.add(new VoteBean(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public String getBegin_date() {
        return this.begin_date;
    }

    public String getCan_multi() {
        return this.can_multi;
    }

    public int getCommentValue() {
        return this.commentValue;
    }

    public String getEnd_date() {
        return this.end_date;
    }

    public List<options> getOption() {
        return this.option;
    }

    public String getVote_id() {
        return this.vote_id;
    }

    public String getVote_name() {
        return this.vote_name;
    }

    public int getVoted() {
        return this.voted;
    }

    public void setBegin_date(final String begin_date) {
        this.begin_date = begin_date;
    }

    public void setCan_multi(final String can_multi) {
        this.can_multi = can_multi;
    }

    public void setCommentValue(final int commentValue) {
        this.commentValue = commentValue;
    }

    public void setEnd_date(final String end_date) {
        this.end_date = end_date;
    }

    public void setOption(final List<options> option) {
        this.option = option;
    }

    public void setVote_id(final String vote_id) {
        this.vote_id = vote_id;
    }

    public void setVote_name(final String vote_name) {
        this.vote_name = vote_name;
    }

    public void setVoted(final int voted) {
        this.voted = voted;
    }
}
