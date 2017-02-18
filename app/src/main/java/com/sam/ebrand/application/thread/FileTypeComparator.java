package com.sam.ebrand.application.thread;

import java.util.Comparator;

/**
 * Created by sam on 2016/11/21.
 */

public class FileTypeComparator implements Comparator<FileType>
{
    @Override
    public int compare(final FileType fileType, final FileType fileType2) {
        if (fileType.mType < fileType2.mType) {
            return -1;
        }
        if (fileType.mType == fileType2.mType) {
            return fileType.mFileName.compareToIgnoreCase(fileType2.mFileName);
        }
        return 1;
    }
}
