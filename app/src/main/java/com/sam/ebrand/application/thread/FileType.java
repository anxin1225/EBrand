package com.sam.ebrand.application.thread;

/**
 * Created by sam on 2016/11/21.
 */

public class FileType
{
    public static final int FILE = 2;
    public static final int FOLDER = 1;
    public String mFileName;
    public String mParentPath;
    public int mType;

    public FileType(final String mFileName, final String mParentPath, final int mType) {
        this.mFileName = mFileName;
        this.mParentPath = mParentPath;
        this.mType = mType;
    }
}
