package com.sam.ebrand.application.sms;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.application.thread.GetPeopleListTask;
import com.sam.ebrand.manage.HttpProtocalManager;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.meetingNetwork.beans.DataPackage;
import com.sam.ebrand.meetingNetwork.beans.PeopleBean;
import com.sam.ebrand.meetingNetwork.http.app.Sentsms;
import com.sam.ebrand.widget.ToastHint;

import java.util.List;

/**
 * Created by sam on 2016/12/5.
 */
public class ReceiveMessageDlg {
    public static Dialog mDialog;
    private Button mBtnReply;
    private Context mContext;
    private TextView mMsgContent;
    private EditText mReplyTxt;
    private TextView mSender;

    static {
        ReceiveMessageDlg.mDialog = null;
    }

    public ReceiveMessageDlg(final Context mContext, final UnReadMessageQueue.ShortMessage shortMessage) {
        if (ReceiveMessageDlg.mDialog != null) {
            ReceiveMessageDlg.mDialog.dismiss();
            ReceiveMessageDlg.mDialog = null;
        }
        this.mContext = mContext;
        (ReceiveMessageDlg.mDialog = new Dialog(this.mContext, 2131558400)).setCanceledOnTouchOutside(false);
        ReceiveMessageDlg.mDialog.setCancelable(false);
        ReceiveMessageDlg.mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(final DialogInterface dialogInterface, final int n, final KeyEvent keyEvent) {
                if (n == 4 && keyEvent.getAction() == 1) {
                    ReceiveMessageDlg.this.dismiss();
                }
                return false;
            }
        });
        ReceiveMessageDlg.mDialog.setContentView(R.layout.receivemsgdlg);
        this.mSender = (TextView)ReceiveMessageDlg.mDialog.findViewById(R.id.txt_sender);
        (this.mMsgContent = (TextView)ReceiveMessageDlg.mDialog.findViewById(R.id.txt_msgContent)).setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mSender.setText((CharSequence)shortMessage.mSender);
        this.mMsgContent.setText((CharSequence)shortMessage.mMsgContent);
        this.mReplyTxt = (EditText)ReceiveMessageDlg.mDialog.findViewById(R.id.replay_text);
        (this.mBtnReply = (Button)ReceiveMessageDlg.mDialog.findViewById(R.id.btn_replay)).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View view) {
                final String string = ReceiveMessageDlg.this.mReplyTxt.getText().toString();
                if (string.equals("")) {
                    ToastHint.show(ReceiveMessageDlg.this.mContext, "发送的内容不能为空！");
                    return;
                }
                ReceiveMessageDlg.this.dismiss();
                if (shortMessage.mbAdmin) {
                    final Sentsms sentsms = new Sentsms();
                    sentsms.Request(ReceiveMessageDlg.this.mContext, "-1", string);
                    sentsms.SetOnResultListener(new Sentsms.onSentsmsListener() {
                        @Override
                        public int OnResultHandle(final int n, final String s, final List list) {
                            ToastHint.show(ReceiveMessageDlg.this.mContext, s);
                            return 0;
                        }
                    });
                    return;
                }
                HttpProtocalManager.getInstance().GetPeopleList(new GetPeopleListTask.onResultListener() {
                    @Override
                    public void ResultListener(final int n, final String s, final List<PeopleBean> list) {
                        final String s2 = (String)ReceiveMessageDlg.this.mSender.getText();
                        if (list != null && list.size() > 0) {
                            for (int i = -1 + list.size(); i >= 0; --i) {
                                final PeopleBean peopleBean = list.get(i);
                                if (s2.equals(peopleBean.getUsername())) {
                                    final int socketID = peopleBean.getSocketID();
                                    final StringBuffer sb = new StringBuffer();
                                    sb.append(socketID);
                                    sb.append("|");
                                    sb.append(SettingManager.UserName);
                                    sb.append(",");
                                    sb.append(string);
                                    final byte[] bytes = sb.toString().getBytes();
                                    final byte[] array = new byte[12 + bytes.length];
                                    DataPackage.intToBytes(38, array, 0);
                                    DataPackage.intToBytes(1, array, 4);
                                    DataPackage.intToBytes(bytes.length, array, 8);
                                    System.arraycopy(bytes, 0, array, 12, bytes.length);
                                    SocketManager.getInstance().sendMessage(array);
                                    ToastHint.show(ReceiveMessageDlg.this.mContext, "消息发送成功");
                                    return;
                                }
                            }
                        }
                        ToastHint.show(ReceiveMessageDlg.this.mContext, "消息发送失败，对方断开连接！");
                    }
                });
            }
        });
    }

    public void dismiss() {
        if (ReceiveMessageDlg.mDialog != null) {
            final UnReadMessageQueue.ShortMessage poll = UnReadMessageQueue.getInstance().poll();
            if (poll == null) {
                ReceiveMessageDlg.mDialog.dismiss();
                ReceiveMessageDlg.mDialog = null;
                return;
            }
            this.showNext(poll.mSender, poll.mMsgContent);
        }
    }

    public void show() {
        if (ReceiveMessageDlg.mDialog != null) {
            ReceiveMessageDlg.mDialog.getWindow().setType(2003);
            ReceiveMessageDlg.mDialog.show();
        }
    }

    public void showNext(final String text, final String text2) {
        this.mSender.setText((CharSequence)text);
        this.mMsgContent.setText((CharSequence)text2);
        this.mReplyTxt.setText((CharSequence)"");
    }
}
