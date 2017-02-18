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
public class MingpaiBackgroundBean {
    private String backcolor;
    private String company;
    private String companycolor;
    private String companyfont;
    private String companyfontsize;
    private float companyheight;
    private float companyleft;
    private String companymax;
    private float companytop;
    private float companywidth;
    private String isadmin;
    private String rank;
    private String rankcolor;
    private String rankfont;
    private String rankfontsize;
    private float rankheight;
    private float rankleft;
    private String rankmax;
    private float ranktop;
    private float rankwidth;
    private String userbcolor;
    private String username;
    private String usernamecolor;
    private String usernamefont;
    private String usernamefontsize;
    private float usernameheight;
    private float usernameleft;
    private float usernametop;
    private float usernamewidth;
    private String vision;

    public MingpaiBackgroundBean(final JSONObject jsonObject) throws JSONException {
        this.userbcolor = JSONUtil.getString(jsonObject, "userbcolor", "");
        this.backcolor = JSONUtil.getString(jsonObject, "backcolor", "");
        this.username = JSONUtil.getString(jsonObject, "username", "");
        this.rank = JSONUtil.getString(jsonObject, "rank", "");
        this.company = JSONUtil.getString(jsonObject, "company", "");
        this.isadmin = JSONUtil.getString(jsonObject, "isadmin", "");
        this.usernamewidth = Float.valueOf(JSONUtil.getString(jsonObject, "usernamewidth", ""));
        this.usernameheight = Float.valueOf(JSONUtil.getString(jsonObject, "usernameheight", ""));
        this.usernamefontsize = JSONUtil.getString(jsonObject, "usernamefontsize", "");
        this.usernametop = Float.valueOf(JSONUtil.getString(jsonObject, "usernametop", ""));
        this.usernameleft = Float.valueOf(JSONUtil.getString(jsonObject, "usernameleft", ""));
        this.usernamecolor = JSONUtil.getString(jsonObject, "usernamecolor", "");
        this.usernamefont = JSONUtil.getString(jsonObject, "usernamefont", "");
        this.rankwidth = Float.valueOf(JSONUtil.getString(jsonObject, "rankwidth", ""));
        this.rankheight = Float.valueOf(JSONUtil.getString(jsonObject, "rankheight", ""));
        this.ranktop = Float.valueOf(JSONUtil.getString(jsonObject, "ranktop", ""));
        this.rankleft = Float.valueOf(JSONUtil.getString(jsonObject, "rankleft", ""));
        this.rankfontsize = JSONUtil.getString(jsonObject, "rankfontsize", "");
        this.rankcolor = JSONUtil.getString(jsonObject, "rankcolor", "");
        this.rankfont = JSONUtil.getString(jsonObject, "rankfont", "");
        this.companywidth = Float.valueOf(JSONUtil.getString(jsonObject, "companywidth", ""));
        this.companyheight = Float.valueOf(JSONUtil.getString(jsonObject, "companyheight", ""));
        this.companytop = Float.valueOf(JSONUtil.getString(jsonObject, "companytop", ""));
        this.companyleft = Float.valueOf(JSONUtil.getString(jsonObject, "companyleft", ""));
        this.companyfontsize = JSONUtil.getString(jsonObject, "companyfontsize", "");
        this.companycolor = JSONUtil.getString(jsonObject, "companycolor", "");
        this.companyfont = JSONUtil.getString(jsonObject, "companyfont", "");
        this.vision = JSONUtil.getString(jsonObject, "vision", "");
    }

    public static List<MingpaiBackgroundBean> constructArrayList(final JSONArray jsonArray) throws JSONException {
        if(jsonArray == null)
            return null;
        int length = jsonArray.length();
        if(length<1)
            return null;
        ArrayList list = new ArrayList<MingpaiBackgroundBean>(length);

        for (int i=0;i<length;i++){
            list.add(new MingpaiBackgroundBean(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    public String getBackcolor() {
        return this.backcolor;
    }

    public String getCompany() {
        return this.company;
    }

    public String getCompanycolor() {
        return this.companycolor;
    }

    public String getCompanyfont() {
        return this.companyfont;
    }

    public String getCompanyfontsize() {
        return this.companyfontsize;
    }

    public float getCompanyheight() {
        return this.companyheight;
    }

    public float getCompanyleft() {
        return this.companyleft;
    }

    public String getCompanymax() {
        return this.companymax;
    }

    public float getCompanytop() {
        return this.companytop;
    }

    public float getCompanywidth() {
        return this.companywidth;
    }

    public String getIsadmin() {
        return this.isadmin;
    }

    public String getRank() {
        return this.rank;
    }

    public String getRankcolor() {
        return this.rankcolor;
    }

    public String getRankfont() {
        return this.rankfont;
    }

    public String getRankfontsize() {
        return this.rankfontsize;
    }

    public float getRankheight() {
        return this.rankheight;
    }

    public float getRankleft() {
        return this.rankleft;
    }

    public String getRankmax() {
        return this.rankmax;
    }

    public float getRanktop() {
        return this.ranktop;
    }

    public float getRankwidth() {
        return this.rankwidth;
    }

    public String getUserbcolor() {
        return this.userbcolor;
    }

    public String getUsername() {
        return this.username;
    }

    public String getUsernamecolor() {
        return this.usernamecolor;
    }

    public String getUsernamefont() {
        return this.usernamefont;
    }

    public String getUsernamefontsize() {
        return this.usernamefontsize;
    }

    public float getUsernameheight() {
        return this.usernameheight;
    }

    public float getUsernameleft() {
        return this.usernameleft;
    }

    public float getUsernametop() {
        return this.usernametop;
    }

    public float getUsernamewidth() {
        return this.usernamewidth;
    }

    public String getVision() {
        return this.vision;
    }

    public boolean isPureColor() {
        return this.userbcolor.equals("1");
    }

    public void setBackcolor(final String backcolor) {
        this.backcolor = backcolor;
    }

    public void setCompany(final String company) {
        this.company = company;
    }

    public void setCompanycolor(final String companycolor) {
        this.companycolor = companycolor;
    }

    public void setCompanyfont(final String companyfont) {
        this.companyfont = companyfont;
    }

    public void setCompanyfontsize(final String companyfontsize) {
        this.companyfontsize = companyfontsize;
    }

    public void setCompanyheight(final float companyheight) {
        this.companyheight = companyheight;
    }

    public void setCompanyleft(final float companyleft) {
        this.companyleft = companyleft;
    }

    public void setCompanymax(final String companymax) {
        this.companymax = companymax;
    }

    public void setCompanytop(final float companytop) {
        this.companytop = companytop;
    }

    public void setCompanywidth(final float companywidth) {
        this.companywidth = companywidth;
    }

    public void setIsadmin(final String isadmin) {
        this.isadmin = isadmin;
    }

    public void setRank(final String rank) {
        this.rank = rank;
    }

    public void setRankcolor(final String rankcolor) {
        this.rankcolor = rankcolor;
    }

    public void setRankfont(final String rankfont) {
        this.rankfont = rankfont;
    }

    public void setRankfontsize(final String rankfontsize) {
        this.rankfontsize = rankfontsize;
    }

    public void setRankheight(final float rankheight) {
        this.rankheight = rankheight;
    }

    public void setRankleft(final float rankleft) {
        this.rankleft = rankleft;
    }

    public void setRankmax(final String rankmax) {
        this.rankmax = rankmax;
    }

    public void setRanktop(final float ranktop) {
        this.ranktop = ranktop;
    }

    public void setRankwidth(final float rankwidth) {
        this.rankwidth = rankwidth;
    }

    public void setUserbcolor(final String userbcolor) {
        this.userbcolor = userbcolor;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public void setUsernamecolor(final String usernamecolor) {
        this.usernamecolor = usernamecolor;
    }

    public void setUsernamefont(final String usernamefont) {
        this.usernamefont = usernamefont;
    }

    public void setUsernamefontsize(final String usernamefontsize) {
        this.usernamefontsize = usernamefontsize;
    }

    public void setUsernameheight(final float usernameheight) {
        this.usernameheight = usernameheight;
    }

    public void setUsernameleft(final float usernameleft) {
        this.usernameleft = usernameleft;
    }

    public void setUsernametop(final float usernametop) {
        this.usernametop = usernametop;
    }

    public void setUsernamewidth(final float usernamewidth) {
        this.usernamewidth = usernamewidth;
    }

    public void setVision(final String vision) {
        this.vision = vision;
    }
}
