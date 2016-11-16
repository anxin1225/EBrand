package com.sam.ebrand.application.set;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/11/16.
 */

public class WallpaperManager
{
    Integer[] DrawID;
    Integer[] SmallDrawID;

    public WallpaperManager() {
        final Integer[] smallDrawID = { 2130837804, 2130837808, 2130837809, 2130837810, 2130837811, 2130837812, 2130837813, 2130837814, 2130837815, 2130837805, 2130837806, 2130837807 };
        final Integer[] drawID = { 2130837741, 2130837768, 2130837769, 2130837770, 2130837771, 2130837772, 2130837773, 2130837774, 2130837775, 2130837766, 2130837788, 2130837767 };
        this.SmallDrawID = smallDrawID;
        this.DrawID = drawID;
    }

    public List<String> getPictures(final String s) {
        ArrayList<String> list = new ArrayList<String>();
        final File[] listFiles = new File(s).listFiles();
        if (listFiles == null) {
            list = null;
        }
        else {
            for (int i = 0; i < listFiles.length; ++i) {
                final File file = listFiles[i];
                if (file.isFile()) {
                    try {
                        final int lastIndex = file.getPath().lastIndexOf(".");
                        if (lastIndex > 0) {
                            final String substring = file.getPath().substring(lastIndex);
                            if (substring.toLowerCase().equals(".png") || substring.toLowerCase().equals(".jpg")) {
                                list.add(file.getPath());
                            }
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        return list;
    }

    public List<Wallpaper> getWallList() {
        final ArrayList<Wallpaper> list = new ArrayList<Wallpaper>();
        for (int i = 0; i < this.SmallDrawID.length; ++i) {
            final Wallpaper wallpaper = new Wallpaper();
            wallpaper.mDrawable = true;
            wallpaper.mSmallid = this.SmallDrawID[i];
            wallpaper.mId = this.DrawID[i];
            list.add(wallpaper);
        }
        final List<String> pictures = this.getPictures("/mnt/sdcard/wallpaper");
        if (pictures != null && pictures.size() > 0) {
            for (int j = 0; j < pictures.size(); ++j) {
                final Wallpaper wallpaper2 = new Wallpaper();
                wallpaper2.mDrawable = false;
                wallpaper2.mFileName = pictures.get(j);
                list.add(wallpaper2);
            }
        }
        return list;
    }

    public class Wallpaper
    {
        Boolean mDrawable;
        String mFileName;
        int mId;
        int mSmallid;

        public Wallpaper() {
            this.mId = 0;
            this.mSmallid = 0;
            this.mFileName = "";
        }
    }
}