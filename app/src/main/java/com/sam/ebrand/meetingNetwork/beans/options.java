package com.sam.ebrand.meetingNetwork.beans;

import com.sam.ebrand.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/12/5.
 */
public class options {
    private String optionId;
    private String options;
    private String vote_id;

    public options(final JSONObject jsonObject) throws JSONException {
        this.options = JSONUtil.getString(jsonObject, "options", "");
        this.optionId = JSONUtil.getString(jsonObject, "optionId", "");
        this.vote_id = JSONUtil.getString(jsonObject, "vote_id", "");
    }

    public static List<options> optionsArrayList(final JSONArray jsonArray) throws JSONException {

        if(jsonArray == null)
            return null;
        int length = jsonArray.length();
        if(length<1)
            return null;
        ArrayList list = new ArrayList<options>(length);

        for (int i=0;i<length;i++){
            list.add(new options(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public String getOptionId() {
        return this.optionId;
    }

    public String getOptions() {
        return this.options;
    }

    public String getVote_id() {
        return this.vote_id;
    }

    public void setOptionId(final String optionId) {
        this.optionId = optionId;
    }

    public void setOptions(final String options) {
        this.options = options;
    }

    public void setVote_id(final String vote_id) {
        this.vote_id = vote_id;
    }
}
