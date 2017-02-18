package com.sam.ebrand.meetingNetwork;

import java.util.Map;

/**
 * Created by sam on 2016/11/21.
 */
public class ParamUp
{
    public static final int CONTENT_TYPE_BINARY = 1;
    public static final int CONTENT_TYPE_JSON = 0;
    public static final int COOKIE_ACTION_GET = 1;
    public static final int COOKIE_ACTION_NULL = 0;
    public static final int COOKIE_ACTION_SET = 2;
    public byte[] binaryData;
    public String content;
    public int contentType;
    public int cookieAction;
    public Map<String, String> mHeaderParamsMap;
    public String type;

    public ParamUp() {
        this.cookieAction = 0;
        this.type = "";
        this.content = "";
        this.contentType = 0;
        this.mHeaderParamsMap = null;
    }
}
