package com.sam.ebrand.meetingNetwork.beans;

import com.sam.ebrand.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/11/20.
 */

public class DraftBean
{
    public static final int OTHERTHING = 2;
    public static final int SCHEDULE = 1;
    private String docName;
    private String docRealName;
    private int docSize;
    private String docUrl;
    private int fileType;
    private int mDocType;
    private String parentpath;

    public DraftBean() {
        this.parentpath = "";
        this.mDocType = 0;
    }

    public DraftBean(final JSONObject jsonObject) throws JSONException {
        this.parentpath = "";
        this.docName = JSONUtil.getString(jsonObject, "docName", "");
        this.docUrl = JSONUtil.getString(jsonObject, "docUrl", "").replace("\\", "");
        this.docUrl = this.docUrl.replace("%2f", "/");
        this.docUrl = this.docUrl.replace("%2F", "/");
        this.docUrl = this.docUrl.replace("%3A", ":");
        this.docUrl = this.docUrl.replaceAll("\\+", "%20");
        this.docSize = JSONUtil.getInt(jsonObject, "docSize", 0);
    }

    public static List<DraftBean> constructArrayList(final JSONArray jsonArray) throws JSONException {
        if(jsonArray == null)
            return null;
        int length = jsonArray.length();
        if(length<1)
            return null;
        ArrayList list = new ArrayList<DraftBean>(length);

        for (int i=0;i<length;i++){
            list.add(new DraftBean(jsonArray.getJSONObject(i)));
        }


        return list;
    }

    public String getDocName() {
        return this.docName;
    }

    public int getDocSize() {
        return this.docSize;
    }

    public int getDocType() {
        return this.mDocType;
    }

    public String getDocUrl() {
        return this.docUrl;
    }

    public int getFileType() {
        return this.fileType;
    }

    public String getParentpath() {
        return this.parentpath;
    }

    public String getRealName() {
        return this.docRealName;
    }

    public void setDocName(final String docName) {
        this.docName = docName;
    }

    public void setDocSize(final int docSize) {
        this.docSize = docSize;
    }

    public void setDocType(final int mDocType) {
        this.mDocType = mDocType;
    }

    public void setDocUrl(final String docUrl) {
        this.docUrl = docUrl;
    }

    public void setFileType(final int fileType) {
        this.fileType = fileType;
    }

    public void setParentpath(final String parentpath) {
        this.parentpath = parentpath;
    }

    public void setRealName(final String docRealName) {
        this.docRealName = docRealName;
    }
}
