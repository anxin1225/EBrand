package com.sam.ebrand.application.meeting.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.beans.DraftBean;
import com.sam.ebrand.param.MeetingParam;

import java.util.List;

/**
 * Created by sam on 2016/11/20.
 */

public class DraftFileAdapter extends BaseAdapter
{
    private Context mContext;
    private List<DraftBean> mFileListBean;
    private int mType;
    private boolean mbBrowsUdisk;

    public DraftFileAdapter(final Context mContext, final List<DraftBean> mFileListBean, final int mType) {
        this.mFileListBean = mFileListBean;
        this.mContext = mContext;
        this.mbBrowsUdisk = false;
        this.mType = mType;
    }

    public int getCount() {
        return this.mFileListBean.size();
    }

    public List<DraftBean> getFileList() {
        return this.mFileListBean;
    }

    public Object getItem(final int n) {
        return n;
    }

    public long getItemId(final int n) {
        return n;
    }

    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        FilesView tag;
        if (inflate == null) {
            inflate = LayoutInflater.from(this.mContext).inflate(R.layout.file_grid_item_modify, (ViewGroup)null);
            tag = new FilesView();
            tag.mFileName = (TextView)inflate.findViewById(R.id.txt_filename);
            tag.mFileImg = (ImageView)inflate.findViewById(R.id.view_fileimg);
            tag.mBtnFileImport = (ImageView)inflate.findViewById(R.id.btn_import);
            inflate.setTag((Object)tag);
        }
        else {
            tag = (FilesView)inflate.getTag();
        }
        if (this.mFileListBean.get(n).getDocType() == 2) {
            tag.mFileName.setText((CharSequence)this.mFileListBean.get(n).getRealName());
        }
        else {
            tag.mFileName.setText((CharSequence)this.mFileListBean.get(n).getDocName());
        }
        if (this.mFileListBean.get(n).getFileType() == 1) {
            tag.mFileImg.setBackgroundResource(R.drawable.draft_dirfile);
        }
        else {
            tag.mFileImg.setBackgroundResource(R.drawable.glass_folder_003);
        }
        final String docName = this.mFileListBean.get(n).getDocName();
        final String string = String.valueOf(this.mFileListBean.get(n).getParentpath()) + "/" + docName;
        if (this.mbBrowsUdisk) {
            tag.mBtnFileImport.setVisibility(View.VISIBLE);
            tag.mBtnFileImport.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View view) {
                    if (DraftFileAdapter.this.mType == 1) {
                        SocketManager.mStaticHandler.obtainMessage(11, (Object)new String[] { string, String.valueOf(MeetingParam.SDCARD_UDISK_DOC) + docName }).sendToTarget();
                    }
                    else {
                        if (DraftFileAdapter.this.mType == 2) {
                            SocketManager.mStaticHandler.obtainMessage(11, (Object)new String[] { string, String.valueOf(MeetingParam.SDCARD_UDISK_SCHEDULE) + docName }).sendToTarget();
                            return;
                        }
                        if (DraftFileAdapter.this.mType == 3) {
                            SocketManager.mStaticHandler.obtainMessage(11, (Object)new String[] { string, String.valueOf(MeetingParam.SDCARD_UDISK_RESTAURANT) + docName }).sendToTarget();
                        }
                    }
                }
            });
            return inflate;
        }
        tag.mBtnFileImport.setVisibility(View.GONE);
        return inflate;
    }

    public boolean isUdisk() {
        return this.mbBrowsUdisk;
    }

    public void updateFileList(final List<DraftBean> mFileListBean, final boolean mbBrowsUdisk) {
        this.mFileListBean = mFileListBean;
        this.mbBrowsUdisk = mbBrowsUdisk;
        this.notifyDataSetChanged();
    }

    private static final class FilesView
    {
        ImageView mBtnFileImport;
        ImageView mFileImg;
        TextView mFileName;
    }
}
