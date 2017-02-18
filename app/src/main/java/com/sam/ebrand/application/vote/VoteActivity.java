package com.sam.ebrand.application.vote;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.beans.VoteBean;
import com.sam.ebrand.meetingNetwork.beans.options;
import com.sam.ebrand.meetingNetwork.http.app.GetVoteTask;
import com.sam.ebrand.meetingNetwork.http.app.UploadVoteTask;
import com.sam.ebrand.widget.ToastHint;

import java.util.List;

/**
 * Created by sam on 2016/12/5.
 */

public class VoteActivity extends Activity implements View.OnClickListener, SocketManager.Observer, UploadVoteTask.OnResultListener
{
    public static String Can_multi;
    public static String mVoteId;
    public static int temp;
    private Button mBtnAgree;
    private Button mBtnGiveup;
    private Button mBtnOppse;
    private ImageButton mGoBackBtn;
    private ImageButton mMainBtn;
    public List<options> mOptions;
    private ImageButton mSystemNoticeBtn;
    private TextView mVoteContent;
    private ImageButton mWelcomeBtn;

    static {
        VoteActivity.mVoteId = "";
        VoteActivity.temp = -1;
    }

    public void CancelListener() {
    }

    public void ResultListener(final int n, final String s, final List<VoteBean> list) {
        ToastHint.show((Context)this, s);
    }

    public void UpdateVote() {
        this.mVoteContent.setText((CharSequence)"");
        VoteActivity.mVoteId = "";
        this.mOptions = null;
        HttpProtocalManager.getInstance().GetVote(new GetVoteTask.OnResultListener() {
            @Override
            public void ResultListener(final int n, final String s, final List<VoteBean> list) {
                if (list != null && list.size() > 0) {
                    final VoteBean voteBean = list.get(0);
                    if (voteBean != null) {
                        VoteActivity.this.mVoteContent.setText((CharSequence)voteBean.getVote_name());
                        VoteActivity.mVoteId = voteBean.getVote_id();
                        VoteActivity.this.mOptions = voteBean.getOption();
                    }
                }
            }
        });
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_main: {
                final Intent intent = new Intent((Context)this, (Class)mainActivity.class);
                intent.setFlags(67108864);
                this.startActivity(intent);
            }
            case R.id.btn_welcome: {
                final Intent intent2 = new Intent((Context)this, (Class)WelcomeActivity.class);
                intent2.setFlags(67108864);
                this.startActivity(intent2);
            }
            case R.id.btn_systemnotice: {
                this.startActivity(new Intent((Context)this, (Class)NoticeActivity.class));
            }
            case R.id.btn_goback: {
                this.finish();
            }
            case R.id.btn_agree: {
                if (this.mOptions != null) {
                    ((Dialog)new AlertDialog.Builder((Context)this).setCancelable(false).setTitle((CharSequence)"提示").setMessage((CharSequence)"是否确定进行投票？确定后将不能修改。").setPositiveButton((CharSequence)"确定", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            for (int i = 0; i < VoteActivity.this.mOptions.size(); ++i) {
                                if (VoteActivity.this.mOptions.get(i).getOptions().equals("赞成")) {
                                    HttpProtocalManager.getInstance().Vote(VoteActivity.this, new String[] { VoteActivity.mVoteId, VoteActivity.this.mOptions.get(i).getOptionId(), "1" });
                                }
                            }
                        }
                    }).setNegativeButton((CharSequence)"取消", (DialogInterface.OnClickListener)new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.cancel();
                        }
                    }).create()).show();
                    return;
                }
                break;
            }
            case R.id.btn_giveup: {
                if (this.mOptions != null) {
                    ((Dialog)new AlertDialog.Builder((Context)this).setCancelable(false).setTitle((CharSequence)"提示").setMessage((CharSequence)"是否确定进行投票？确定后将不能修改。").setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            for (int i = 0; i < VoteActivity.this.mOptions.size(); ++i) {
                                if (VoteActivity.this.mOptions.get(i).getOptions().equals("弃权")) {
                                    HttpProtocalManager.getInstance().Vote(VoteActivity.this, new String[] { VoteActivity.mVoteId, VoteActivity.this.mOptions.get(i).getOptionId(), "1" });
                                }
                            }
                        }
                    }).setNegativeButton((CharSequence)"取消", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.cancel();
                        }
                    }).create()).show();
                    return;
                }
                break;
            }
            case R.id.btn_oppse: {
                if (this.mOptions != null) {
                    ((Dialog)new AlertDialog.Builder((Context)this).setCancelable(false).setTitle((CharSequence)"提示").setMessage((CharSequence)"是否确定进行投票？确定后将不能修改。").setPositiveButton((CharSequence)"确定", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            for (int i = 0; i < VoteActivity.this.mOptions.size(); ++i) {
                                if (VoteActivity.this.mOptions.get(i).getOptions().equals("反对")) {
                                    HttpProtocalManager.getInstance().Vote(VoteActivity.this, new String[] { VoteActivity.mVoteId, VoteActivity.this.mOptions.get(i).getOptionId(), "1" });
                                }
                            }
                        }
                    }).setNegativeButton((CharSequence)"取消", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.cancel();
                        }
                    }).create()).show();
                    return;
                }
                break;
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.setContentView(R.layout.vote_modi);
        this.mBtnAgree = (Button)this.findViewById(R.id.btn_agree);
        this.mBtnOppse = (Button)this.findViewById(R.id.btn_oppse);
        this.mBtnGiveup = (Button)this.findViewById(R.id.btn_giveup);
        this.mBtnAgree.setOnClickListener(this);
        this.mBtnOppse.setOnClickListener(this);
        this.mBtnGiveup.setOnClickListener(this);
        (this.mMainBtn = (ImageButton)this.findViewById(R.id.btn_main)).setOnClickListener(this);
        (this.mWelcomeBtn = (ImageButton)this.findViewById(R.id.btn_welcome)).setOnClickListener(this);
        this.mSystemNoticeBtn = (ImageButton)this.findViewById(R.id.btn_systemnotice);
        this.mGoBackBtn = (ImageButton)this.findViewById(R.id.btn_goback);
        this.mSystemNoticeBtn.setOnClickListener(this);
        this.mGoBackBtn.setOnClickListener(this);
        this.mVoteContent = (TextView)this.findViewById(R.id.vote_content);
        ((LinearLayout)this.findViewById(R.id.mainbackground)).setBackgroundDrawable(BackgroundManager.getInstance().getBackground());
    }

    protected void onDestroy() {
        super.onDestroy();
        HttpProtocalManager.getInstance().CancelGetVote();
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        this.UpdateVote();
        super.onResume();
    }
}
