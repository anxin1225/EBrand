package com.sam.ebrand.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/12/1.
 */
public class FileItem {

    public static final int FILE_TYPE_AUDIO = 4;
    public static final int FILE_TYPE_DIR = 1;
    public static final int FILE_TYPE_DOC = 5;
    public static final int FILE_TYPE_EXCEL = 7;
    public static final int FILE_TYPE_IMG = 2;
    public static final int FILE_TYPE_PDF = 6;
    public static final int FILE_TYPE_VIDEO = 3;
    private int dir;
    private String filename;
    private int filetype;
    private String fileurl;
    private String path;

    public FileItem() {
        this.dir = 0;
        this.filetype = 1;
    }

    public FileItem(final JSONObject jsonObject) throws JSONException {
        this.dir = 0;
        this.filetype = 1;
        this.path = JSONUtil.getString(jsonObject, "path", "");
        this.fileurl = JSONUtil.getString(jsonObject, "fullpath", "");
        this.fileurl = this.fileurl.replace("%2f", "/");
        this.fileurl = this.fileurl.replace("%2F", "/");
        this.fileurl = this.fileurl.replace("%3A", ":");
        this.fileurl = this.fileurl.replaceAll("\\+", "%20");
        this.dir = JSONUtil.getInt(jsonObject, "is_dir", 0);
        this.filename = this.path.substring(this.path.lastIndexOf("/") + 1);
        final String mimeType = this.getMIMEType(new File(this.path));
        if (mimeType.equals("*/*")) {
            this.filetype = 1;
        }
        else {
            if (mimeType.equals("video/mp4")) {
                this.filetype = 3;
                return;
            }
            if (mimeType.equals("application/pdf")) {
                this.filetype = 6;
                return;
            }
            if (mimeType.equals("application/msword")) {
                this.filetype = 5;
                return;
            }
            if (mimeType.equals("application/vnd.ms-excel")) {
                this.filetype = 7;
                return;
            }
            if (mimeType.equals("image/jpeg") || mimeType.equals("image/jpeg") || mimeType.equals("image/png") || mimeType.equals("image/bmp")) {
                this.filetype = 2;
            }
        }
    }

    public static List<FileItem> constructArrayList(final JSONArray jsonArray) throws JSONException {

        if(jsonArray == null)
            return null;
        int length = jsonArray.length();
        if(length<1)
            return null;
        ArrayList list = new ArrayList<FileItem>(length);

        for (int i=0;i<length;i++){
            list.add(new FileItem(jsonArray.getJSONObject(i)));
        }
        return list;
    }

    private String getMIMEType(final File file) {
        final String[][] array = { { ".3gp", "video/3gpp" }, { ".apk", "application/vnd.android.package-archive" }, { ".asf", "video/x-ms-asf" }, { ".avi", "video/x-msvideo" }, { ".bin", "application/octet-stream" }, { ".bmp", "image/bmp" }, { ".c", "text/plain" }, { ".class", "application/octet-stream" }, { ".conf", "text/plain" }, { ".cpp", "text/plain" }, { ".doc", "application/msword" }, { ".xls", "application/vnd.ms-excel" }, { ".exe", "application/octet-stream" }, { ".gif", "image/gif" }, { ".gtar", "application/x-gtar" }, { ".gz", "application/x-gzip" }, { ".h", "text/plain" }, { ".htm", "text/html" }, { ".html", "text/html" }, { ".jar", "application/java-archive" }, { ".java", "text/plain" }, { ".jpeg", "image/jpeg" }, { ".jpg", "image/jpeg" }, { ".js", "application/x-javascript" }, { ".log", "text/plain" }, { ".m3u", "audio/x-mpegurl" }, { ".m4a", "audio/mp4a-latm" }, { ".m4b", "audio/mp4a-latm" }, { ".m4p", "audio/mp4a-latm" }, { ".m4u", "video/vnd.mpegurl" }, { ".m4v", "video/x-m4v" }, { ".mov", "video/quicktime" }, { ".mp2", "audio/x-mpeg" }, { ".mp3", "audio/x-mpeg" }, { ".mp4", "video/mp4" }, { ".mpc", "application/vnd.mpohun.certificate" }, { ".mpe", "video/mpeg" }, { ".mpeg", "video/mpeg" }, { ".mpg", "video/mpeg" }, { ".mpg4", "video/mp4" }, { ".mpga", "audio/mpeg" }, { ".msg", "application/vnd.ms-outlook" }, { ".ogg", "audio/ogg" }, { ".pdf", "application/pdf" }, { ".png", "image/png" }, { ".pps", "application/vnd.ms-powerpoint" }, { ".ppt", "application/vnd.ms-powerpoint" }, { ".prop", "text/plain" }, { ".rar", "application/x-rar-compressed" }, { ".rc", "text/plain" }, { ".rmvb", "audio/x-pn-realaudio" }, { ".rtf", "application/rtf" }, { ".sh", "text/plain" }, { ".tar", "application/x-tar" }, { ".tgz", "application/x-compressed" }, { ".txt", "text/plain" }, { ".wav", "audio/x-wav" }, { ".wma", "audio/x-ms-wma" }, { ".wmv", "audio/x-ms-wmv" }, { ".wps", "application/vnd.ms-works" }, { ".xml", "text/plain" }, { ".z", "application/x-compress" }, { ".zip", "application/zip" }, { "", "*/*" } };
        String s = "*/*";
        final String name = file.getName();
        final int lastIndex = name.lastIndexOf(".");
        if (lastIndex < 0) {
            return s;
        }
        final String lowerCase = name.substring(lastIndex, name.length()).toLowerCase();
        if (lowerCase == "") {
            return s;
        }
        for (int i = 0; i < array.length; ++i) {
            if (lowerCase.equals(array[i][0])) {
                s = array[i][1];
            }
        }
        return s;
    }

    public int getDir() {
        return this.dir;
    }

    public String getFilename() {
        return this.filename;
    }

    public int getFiletype() {
        return this.filetype;
    }

    public String getFileurl() {
        return this.fileurl;
    }

    public String getFullpath() {
        return this.path;
    }

    public String getPath() {
        return this.path;
    }

    public void setDir(final int dir) {
        this.dir = dir;
    }

    public void setFilename(final String filename) {
        this.filename = filename;
    }

    public void setFiletype(final int filetype) {
        this.filetype = filetype;
    }

    public void setFileurl(final String fileurl) {
        this.fileurl = fileurl;
    }

    public void setFullpath(final String path) {
        this.path = path;
    }

    public void setPath(final String path) {
        this.path = path;
    }
}
