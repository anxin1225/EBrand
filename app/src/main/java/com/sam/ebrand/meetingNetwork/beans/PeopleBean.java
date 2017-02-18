package com.sam.ebrand.meetingNetwork.beans;

import android.graphics.Color;

import com.sam.ebrand.util.JSONUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/11/21.
 */

public class PeopleBean
{
    private int authority;
    private String avatar;
    private int blue;
    private int chairman;
    private String company;
    private int fontColor;
    private String fontName;
    private int fontSize;
    private int green;
    private String id;
    private String isadmin;
    private String rank;
    private int red;
    private String signintime;
    private int socket_id;
    private String status;
    private String userid;
    private String username;
    private String welcome_word;

    public PeopleBean(final JSONObject jsonObject) throws JSONException {
        this.id = JSONUtil.getString(jsonObject, "id", "");
        this.userid = JSONUtil.getString(jsonObject, "userid", "");
        this.username = JSONUtil.getString(jsonObject, "username", "");
        this.rank = JSONUtil.getString(jsonObject, "rank", "");
        this.company = JSONUtil.getString(jsonObject, "company", "");
        this.isadmin = JSONUtil.getString(jsonObject, "company", "");
        this.avatar = JSONUtil.getString(jsonObject, "avatar", "");
        this.signintime = JSONUtil.getString(jsonObject, "signintime", "");
        this.status = JSONUtil.getString(jsonObject, "status", "");
        final String string = JSONUtil.getString(jsonObject, "chairman", "0");
        if (string.equals("null")) {
            this.chairman = 0;
        }
        else {
            this.chairman = Integer.parseInt(string);
        }
        this.fontSize = JSONUtil.getInt(jsonObject, "size", 100);
        this.fontName = JSONUtil.getString(jsonObject, "font", "");
        this.red = JSONUtil.getInt(jsonObject, "red", 100);
        this.green = JSONUtil.getInt(jsonObject, "green", 100);
        this.blue = JSONUtil.getInt(jsonObject, "blue", 100);
        this.fontColor = Color.rgb(this.red, this.green, this.blue);
        this.authority = JSONUtil.getInt(jsonObject, "authority", 100);
        this.socket_id = JSONUtil.getInt(jsonObject, "socket_id", 0);
        this.welcome_word = JSONUtil.getString(jsonObject, "welcome_word", "");
        if (this.welcome_word.equals("null")) {
            this.welcome_word = "";
        }
    }

    public static List<PeopleBean> constructArrayList(final JSONArray jsonArray) throws JSONException {
        ArrayList<PeopleBean> list = null;
        int length = 0;
        if(jsonArray == null || (length=jsonArray.length())<1 )
            return null;
        list = new ArrayList<PeopleBean>(length);
        for(int i=0;i<length;i++){
            list.add(new PeopleBean(jsonArray.getJSONObject(i)));
        }

        return list;
    }

    public int getAuthority() {
        return this.authority;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public int getBlue() {
        return this.blue;
    }

    public int getChairman() {
        return this.chairman;
    }

    public String getCompany() {
        return this.company;
    }

    public int getFontColor() {
        return this.fontColor;
    }

    public String getFontName() {
        return this.fontName;
    }

    public int getFontSize() {
        return this.fontSize;
    }

    public int getGreen() {
        return this.green;
    }

    public String getId() {
        return this.id;
    }

    public String getIsadmin() {
        return this.isadmin;
    }

    public String getRank() {
        return this.rank;
    }

    public int getRed() {
        return this.red;
    }

    public String getSignintime() {
        return this.signintime;
    }

    public int getSocketID() {
        return this.socket_id;
    }

    public String getStatus() {
        return this.status;
    }

    public String getUserid() {
        return this.userid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getWelcomeWord() {
        return this.welcome_word;
    }

    public void setAuthority(final int authority) {
        this.authority = authority;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public void setBlue(final int blue) {
        this.blue = blue;
    }

    public void setChairman(final int chairman) {
        this.chairman = chairman;
    }

    public void setCompany(final String company) {
        this.company = company;
    }

    public void setFontColor(final int fontColor) {
        this.fontColor = fontColor;
    }

    public void setFontName(final String fontName) {
        this.fontName = fontName;
    }

    public void setFontSize(final int fontSize) {
        this.fontSize = fontSize;
    }

    public void setGreen(final int green) {
        this.green = green;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public void setIsadmin(final String isadmin) {
        this.isadmin = isadmin;
    }

    public void setRank(final String rank) {
        this.rank = rank;
    }

    public void setRed(final int red) {
        this.red = red;
    }

    public void setSignintime(final String signintime) {
        this.signintime = signintime;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public void setUserid(final String userid) {
        this.userid = userid;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
