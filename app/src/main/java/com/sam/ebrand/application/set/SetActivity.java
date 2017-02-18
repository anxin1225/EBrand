package com.sam.ebrand.application.set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sam.ebrand.R;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.ServerFontsManager;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.manage.SubLcdManager;
import com.sam.ebrand.manage.SubLcdMsg;
import com.sam.ebrand.meetingNetwork.manage.DownloaderManager;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.ExitApplication;
import com.sam.ebrand.util.FileUtils;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 2016/11/16.
 */

public class SetActivity extends Activity implements View.OnClickListener, SocketManager.Observer {
    protected static final boolean SoundEnabled = false;
    private static boolean mGetBitmapThreadRunning;
    Bitmap bitmap;
    View.OnTouchListener gestureListener;
    private Handler handler;
    private boolean isMain;
    private ImageButton mAboutInfreeBtn;
    private RelativeLayout mAboutInfreeView;
    private BackgroundAdapter mBackgroundAdpater;
    private ArrayList<BackgroundBitmapInfo> mBackgroundBmpList;
    private Gallery mBackgroundGallery;
    private RelativeLayout mBackgroundLinearLayout;
    private Button mBitmapBackgroundSelectBtn;
    private ImageButton mChangeWallpaperBtn;
    private RelativeLayout mChangeWallpaperView;
    private ImageButton mClearDataBtn;
    private CheckBox mCompanyBoldCheck;
    private ImageView mCompanyColor;
    private EditText mCompanyEditText;
    private Spinner mCompanyFontSelect;
    private EditText mCompanySize;
    private BackgroundBitmapInfo mCurrentSelectItem;
    private Button mDrawDefaultSublcdBtn;
    private ArrayAdapter<CharSequence> mFontAdapter;
    private String[] mFontRealName;
    GestureDetector mGestureDetector;
    private ImageButton mGoBackBtn;
    private ImageButton mHardwareVersionBtn;
    private RelativeLayout mHardwareVersionView;
    private TextView mIDInfo;
    private String mIpAddr;
    private CheckBox mJobBoldCheck;
    private ImageView mJobColor;
    private EditText mJobEditText;
    private Spinner mJobFontSelect;
    private EditText mJobSize;
    private UsernameLayout mLayoutview;
    private List<WallpaperManager.Wallpaper> mListWall;
    private ImageButton mMainBtn;
    private Button mModifyConfirmBtn;
    private CheckBox mNameBoldCheck;
    private EditText mNameSize;
    private ImageButton mNameshowBtn;
    private ImageButton mNetworkSettingBtn;
    private Button mPureBackgroundColorSelectBtn;
    private Button mSaveIpAddrBtn;
    private Animation mScaleToLargeAnim;
    private EditText mServerAddrText;
    private RelativeLayout mServerIpAddrSettingView;
    private ImageButton mServerIpSettingBtn;
    private RelativeLayout mSettingBg;
    private ImageButton mSoftwareVersionBtn;
    private RelativeLayout mSoftwareVersionView;
    private Button mStartUserLayoutBtn;
    private ToggleButton mSublcdBacklingBtn;
    private ImageButton mSublcdSettingBtn;
    private RelativeLayout mSublcdSettingView;
    private ImageButton mSystemNoticeBtn;
    private ImageView mUserNameColor;
    private EditText mUserNameEditText;
    private String mUserNameFontName;
    private Spinner mUserNameFontSelect;
    private RelativeLayout mUserNameLayoutView;
    private RelativeLayout mUserNameSettingView;
    private ImageView mUsernamebmpIV;
    private RelativeLayout mUsernamebmpView;
    private WallpaperManager mWallManager;
    private Gallery mWallpaperGallery;
    private ImageButton mWelcomeBtn;
    private int miBackgroundColor;
    private int miCompanyColor;
    private int miCompanyFontPos;
    private long miCurrentBakcPressTime;
    private int miCurrentSelectItem;
    private int miJobColor;
    private int miJobFontPos;
    private int miNameColor;
    private int miNameFontPos;
    private int miNameSize;
    String murl;

    public SetActivity() {
        this.isMain = false;
        this.handler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                    }
                    case 1: {
                        if (message.obj != null) {
                            SetActivity.this.mUsernamebmpIV.setBackgroundDrawable((Drawable) message.obj);
                        }
                        ToastHint.show((Context) SetActivity.this, "加载成功!");
                        ProgressDialogHint.Dismiss();
                    }
                    case 2: {
                        SetActivity.access$1(SetActivity.this, (String) message.obj);
                        new ReConnection().start();
                    }
                    case 3: {
                        ProgressDialogHint.Dismiss();
                        SetActivity.this.mBackgroundAdpater.notifyDataSetChanged();
                        if (SetActivity.this.mBackgroundBmpList.size() > 0) {
                            SetActivity.this.mBackgroundLinearLayout.setVisibility(View.VISIBLE);
                            return;
                        }
                        ToastHint.show((Context) SetActivity.this, "没有找到铭牌背景图片");
                    }
                }
            }
        };
    }

    static /* synthetic */ void access$1(final SetActivity setActivity, final String mIpAddr) {
        setActivity.mIpAddr = mIpAddr;
    }

    static /* synthetic */ void access$10(final SetActivity setActivity, final int miNameFontPos) {
        setActivity.miNameFontPos = miNameFontPos;
    }

    static /* synthetic */ void access$11(final SetActivity setActivity, final int miCompanyFontPos) {
        setActivity.miCompanyFontPos = miCompanyFontPos;
    }

    static /* synthetic */ void access$12(final SetActivity setActivity, final int miJobFontPos) {
        setActivity.miJobFontPos = miJobFontPos;
    }

    static /* synthetic */ void access$13(final SetActivity setActivity, final BackgroundBitmapInfo mCurrentSelectItem) {
        setActivity.mCurrentSelectItem = mCurrentSelectItem;
    }

    static /* synthetic */ void access$14(final SetActivity setActivity, final int miCurrentSelectItem) {
        setActivity.miCurrentSelectItem = miCurrentSelectItem;
    }

    static /* synthetic */ void access$18(final SetActivity setActivity, final int miNameColor) {
        setActivity.miNameColor = miNameColor;
    }

    static /* synthetic */ void access$20(final SetActivity setActivity, final int miJobColor) {
        setActivity.miJobColor = miJobColor;
    }

    static /* synthetic */ void access$22(final SetActivity setActivity, final int miCompanyColor) {
        setActivity.miCompanyColor = miCompanyColor;
    }

    static /* synthetic */ void access$24(final SetActivity setActivity, final int miBackgroundColor) {
        setActivity.miBackgroundColor = miBackgroundColor;
    }

    static /* synthetic */ void access$7(final boolean mGetBitmapThreadRunning) {
        SetActivity.mGetBitmapThreadRunning = mGetBitmapThreadRunning;
    }

    public static Bitmap getBitmapBySDPath(final String s) {
        try {
            final boolean exists = new File(s).exists();
            Bitmap decodeFile = null;
            if (exists) {
                decodeFile = BitmapFactory.decodeFile(s, new BitmapFactory.Options());
            }
            return decodeFile;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void EnableConfirmBtn() {
        this.mModifyConfirmBtn.setEnabled(true);
        final Intent intent = new Intent((Context) this, (Class) WelcomeActivity.class);
        intent.setFlags(67108864);
        this.startActivity(intent);
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
    }

    public void onBackPressed() {
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.miCurrentBakcPressTime < 500L) {
            return;
        }
        this.miCurrentBakcPressTime = currentTimeMillis;
        if (this.mBackgroundLinearLayout.isShown()) {
            this.mBackgroundLinearLayout.setVisibility(View.GONE);
            return;
        }
        if (this.mUserNameLayoutView.isShown()) {
            this.mUserNameLayoutView.setVisibility(View.GONE);
            this.mLayoutview.saveCurrentLayout();
            return;
        }
        if (this.mUsernamebmpView.isShown()) {
            final Intent intent = new Intent((Context) this, (Class) WelcomeActivity.class);
            intent.setFlags(67108864);
            this.startActivity(intent);
            return;
        }
        this.finish();
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_serveraddr: {
                this.mAboutInfreeView.setVisibility(View.GONE);
                this.mChangeWallpaperView.setVisibility(View.GONE);
                this.mSublcdSettingView.setVisibility(View.GONE);
                this.mSoftwareVersionView.setVisibility(View.GONE);
                this.mHardwareVersionView.setVisibility(View.GONE);
                this.mServerIpAddrSettingView.setVisibility(View.VISIBLE);
                this.mServerAddrText.setEnabled(true);
                this.mUserNameSettingView.setVisibility(View.GONE);
            }
            case R.id.btn_saveIpAddr: {
                final String string = this.mServerAddrText.getText().toString();
                string.trim();
                if (!InetAddressUtils.isIPv4Address(string)) {
                    ((Dialog) new AlertDialog.Builder((Context) this).setCancelable(false).setTitle((CharSequence) "提示").setMessage((CharSequence) "输入的IP地址有误！").setPositiveButton((CharSequence) "确定", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.cancel();
                        }
                    }).setNegativeButton((CharSequence) "\u53d6\u6d88", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            dialogInterface.cancel();
                        }
                    }).create()).show();
                    return;
                }
                SettingManager.getInstance().writeSetting("serverip", string);
                this.mServerAddrText.setHint((CharSequence) string);
                this.mServerAddrText.setText((CharSequence) "");
                ToastHint.show(this.getApplicationContext(), "修改服务器地址成功！");
                this.handler.obtainMessage(2, (Object) string).sendToTarget();
            }
            case R.id.btn_toggleBackscreen: {
                if (this.mSublcdBacklingBtn.isChecked()) {
                    SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 2));
                    return;
                }
                SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(0, 2));
            }
            case R.id.btn_softwareversion: {
                this.mAboutInfreeView.setVisibility(View.GONE);
                this.mChangeWallpaperView.setVisibility(View.GONE);
                this.mSublcdSettingView.setVisibility(View.GONE);
                this.mSoftwareVersionView.setVisibility(View.VISIBLE);
                this.mHardwareVersionView.setVisibility(View.GONE);
                this.mServerIpAddrSettingView.setVisibility(View.GONE);
                this.mUserNameSettingView.setVisibility(View.GONE);
            }
            case R.id.btn_hardwareinfo: {
                this.mAboutInfreeView.setVisibility(View.GONE);
                this.mChangeWallpaperView.setVisibility(View.GONE);
                this.mSublcdSettingView.setVisibility(View.GONE);
                this.mSoftwareVersionView.setVisibility(View.GONE);
                this.mHardwareVersionView.setVisibility(View.VISIBLE);
                this.mServerIpAddrSettingView.setVisibility(View.GONE);
                this.mUserNameSettingView.setVisibility(View.GONE);
            }
            case R.id.btn_drawdefault: {
                if (this.mSublcdBacklingBtn.isChecked()) {
                    SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 1));
                    return;
                }
                break;
            }
            case R.id.btn_networksetting: {
                this.mAboutInfreeView.setVisibility(View.GONE);
                this.mChangeWallpaperView.setVisibility(View.GONE);
                this.mSublcdSettingView.setVisibility(View.GONE);
                this.mSoftwareVersionView.setVisibility(View.GONE);
                this.mHardwareVersionView.setVisibility(View.GONE);
                this.mServerIpAddrSettingView.setVisibility(View.GONE);
                this.mUserNameSettingView.setVisibility(View.GONE);
                final Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
                intent.setAction("android.intent.action.VIEW");
                intent.setFlags(270532608);
                this.startActivity(intent);
            }
            case R.id.btn_aboutinfree: {
                this.mAboutInfreeView.setVisibility(View.VISIBLE);
                this.mChangeWallpaperView.setVisibility(View.GONE);
                this.mSublcdSettingView.setVisibility(View.GONE);
                this.mSoftwareVersionView.setVisibility(View.GONE);
                this.mHardwareVersionView.setVisibility(View.GONE);
                this.mServerIpAddrSettingView.setVisibility(View.GONE);
                this.mUserNameSettingView.setVisibility(View.GONE);
            }
            case R.id.btn_changewallpaper: {
                this.mAboutInfreeView.setVisibility(View.GONE);
                this.mChangeWallpaperView.setVisibility(View.VISIBLE);
                this.mSublcdSettingView.setVisibility(View.GONE);
                this.mSoftwareVersionView.setVisibility(View.GONE);
                this.mHardwareVersionView.setVisibility(View.GONE);
                this.mServerIpAddrSettingView.setVisibility(View.GONE);
                this.mUserNameSettingView.setVisibility(View.GONE);
            }
            case R.id.btn_sublcdsetting: {
                this.mAboutInfreeView.setVisibility(View.GONE);
                this.mChangeWallpaperView.setVisibility(View.GONE);
                this.mSublcdSettingView.setVisibility(View.VISIBLE);
                this.mSoftwareVersionView.setVisibility(View.GONE);
                this.mHardwareVersionView.setVisibility(View.GONE);
                this.mServerIpAddrSettingView.setVisibility(View.GONE);
                this.mUserNameSettingView.setVisibility(View.GONE);
            }
            case R.id.btn_cleardata: {
                ((Dialog) new AlertDialog.Builder((Context) this).setTitle((CharSequence) "提示").setMessage((CharSequence) "是否清空会议数据？").setPositiveButton((CharSequence) "确定", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        DownloaderManager.getInstance().calcelAllDownloader();
                        FileUtils.delAllFile(MeetingParam.SDCARD_WHITEBOARD);
                        FileUtils.delAllFile(MeetingParam.SDCARD_DOC);
                        FileUtils.delAllFile(MeetingParam.SDCARD_COMMENT);
                        FileUtils.removeFile(String.valueOf(MeetingParam.SDCARD_ROOT) + "welcome.bmp");
                        FileUtils.removeFile(MeetingParam.SDCARD_WHITEBOARD);
                        FileUtils.removeFile(MeetingParam.SDCARD_DOC);
                        FileUtils.removeFile(MeetingParam.SDCARD_COMMENT);
                        FileUtils.removeFile(MeetingParam.SDCARD_UDISK_DOC);
                        FileUtils.removeFile(MeetingParam.SDCARD_UDISK_RESTAURANT);
                        FileUtils.removeFile(MeetingParam.SDCARD_UDISK_SCHEDULE);
                        FileUtils.creatDir(MeetingParam.SDCARD_ROOT);
                        FileUtils.creatDir(MeetingParam.SDCARD_WHITEBOARD);
                        FileUtils.creatDir(MeetingParam.SDCARD_DOC);
                        FileUtils.creatDir(MeetingParam.SDCARD_COMMENT);
                        FileUtils.creatDir(MeetingParam.SDCARD_UDISK_DOC);
                        FileUtils.creatDir(MeetingParam.SDCARD_UDISK_RESTAURANT);
                        FileUtils.creatDir(MeetingParam.SDCARD_UDISK_SCHEDULE);
                        ProgressDialogHint.Show((Context) SetActivity.this, "提示", "正在删除,请稍后...");
                        new prossss().execute(new Void[0]);
                    }
                }).setNegativeButton((CharSequence) "取消", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                    }
                }).create()).show();
            }
            case R.id.btn_nameshow: {
                final Intent intent2 = new Intent((Context) this, (Class) WelcomeActivity.class);
                intent2.setFlags(67108864);
                this.startActivity(intent2);
            }
            case R.id.btn_main: {
                if (!this.isMain) {
                    final Intent intent3 = new Intent((Context) this,  mainActivity.class);
                    intent3.setFlags(67108864);
                    this.startActivity(intent3);
                    return;
                }
                break;
            }
            case R.id.btn_welcome: {
                final Intent intent4 = new Intent((Context) this, (Class) WelcomeActivity.class);
                intent4.setFlags(67108864);
                this.startActivity(intent4);
            }
            case R.id.btn_systemnotice: {
                this.startActivity(new Intent((Context) this, (Class) NoticeActivity.class));
            }
            case R.id.btn_goback: {
                this.finish();
            }
            case R.id.name_color: {
                new ColorPickerDialog((Context) this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$18(SetActivity.this, backgroundColor);
                        SetActivity.this.mUserNameColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miNameColor).show();
            }
            case R.id.job_color: {
                new ColorPickerDialog((Context) this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$20(SetActivity.this, backgroundColor);
                        SetActivity.this.mJobColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miJobColor).show();
            }
            case R.id.company_color: {
                new ColorPickerDialog((Context) this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$22(SetActivity.this, backgroundColor);
                        SetActivity.this.mCompanyColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miCompanyColor).show();
            }
            case R.id.btn_pagesetting: {
                final String string2 = this.mUserNameEditText.getText().toString();
                final String string3 = this.mJobEditText.getText().toString();
                final String string4 = this.mCompanyEditText.getText().toString();
                if (string2.equals("") && string3.equals("") && string4.equals("")) {
                    ToastHint.show(this.getApplicationContext(), "请输入相关信息");
                    return;
                }
                String s = this.mNameSize.getText().toString();
                if (s.equals("")) {
                    s = this.mNameSize.getHint().toString();
                }
                final int int1 = Integer.parseInt(s);
                this.miNameSize = int1;
                String s2 = this.mCompanySize.getText().toString();
                if (s2.equals("")) {
                    s2 = this.mCompanySize.getHint().toString();
                }
                final int int2 = Integer.parseInt(s2);
                String s3 = this.mJobSize.getText().toString();
                if (s3.equals("")) {
                    s3 = this.mJobSize.getHint().toString();
                }
                final int int3 = Integer.parseInt(s3);
                final SharedPreferences.Editor editor = SettingManager.getInstance().getEditor();
                editor.putString("NOCustomUserName", string2);
                editor.putInt("NOCustomUserNameColor", this.miNameColor);
                editor.putInt("NOCustomUserNameFont", this.miNameFontPos);
                editor.putInt("NOCustomUserNameSize", int1);
                editor.putString("customCompanyName", string4);
                editor.putInt("customCompanyColor", this.miCompanyColor);
                editor.putInt("customCompanyFont", this.miCompanyFontPos);
                editor.putInt("customCompanySize", int2);
                editor.putString("customJobName", string3);
                editor.putInt("customJobColor", this.miJobColor);
                editor.putInt("customJobFont", this.miJobFontPos);
                editor.putInt("customJobSize", int3);
                editor.commit();
                this.mUserNameLayoutView.setVisibility(View.VISIBLE);
                this.mLayoutview.setNameFontAndColor(string2, string3, string4, ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miNameFontPos]), ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miJobFontPos]), ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miCompanyFontPos]), this.miNameColor, this.miJobColor, this.miCompanyColor, int1, int3, int2, this.mNameBoldCheck.isChecked(), this.mJobBoldCheck.isChecked(), this.mCompanyBoldCheck.isChecked());
            }
            case R.id.btn_purecolor: {
                new ColorPickerDialog((Context) this,new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$24(SetActivity.this, backgroundColor);
                        SetActivity.this.mLayoutview.setBackgroundColor(backgroundColor);
                    }
                }, this.miBackgroundColor).show();
            }
            case R.id.btn_modify_confirm: {
                this.mLayoutview.saveToBitmapAndRefresh();
                final SharedPreferences.Editor editor2 = SettingManager.getInstance().getEditor();
                editor2.putInt("CustomUserNameColor", -1);
                editor2.putString("CustomUserNameFont", this.mUserNameFontName);
                editor2.putInt("CustomUserNameSize", this.miNameSize);
                editor2.commit();
                this.mModifyConfirmBtn.setEnabled(false);
            }
            case R.id.btn_bitmapbackground: {
                ProgressDialogHint.Show((Context) this, "请稍候……", "正在加载图片……");
                if (!SetActivity.mGetBitmapThreadRunning) {
                    new getBackgroundBitmapThread().start();
                    return;
                }
                break;
            }
            case R.id.btn_initPos: {
                this.mLayoutview.InitPos();
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(R.layout.set_modi);
        ExitApplication.getInstance().addActivity(this);
        (this.mMainBtn = (ImageButton) this.findViewById(R.id.btn_main)).setOnClickListener( this);
        (this.mWelcomeBtn = (ImageButton) this.findViewById(R.id.btn_welcome)).setOnClickListener( this);
        this.mSystemNoticeBtn = (ImageButton) this.findViewById(R.id.btn_systemnotice);
        this.mGoBackBtn = (ImageButton) this.findViewById(R.id.btn_goback);
        this.mSystemNoticeBtn.setOnClickListener( this);
        this.mGoBackBtn.setOnClickListener( this);
        this.mScaleToLargeAnim = AnimationUtils.loadAnimation((Context) this, R.anim.scale_to_large);
        this.mSublcdSettingBtn = (ImageButton) this.findViewById(R.id.btn_setting);
        this.mAboutInfreeBtn = (ImageButton) this.findViewById(R.id.btn_aboutinfree);
        this.mNameshowBtn = (ImageButton) this.findViewById(R.id.btn_nameshow);
        this.mNetworkSettingBtn = (ImageButton) this.findViewById(R.id.btn_networksetting);
        this.mHardwareVersionBtn = (ImageButton) this.findViewById(R.id.btn_hardwareinfo);
        this.mSoftwareVersionBtn = (ImageButton) this.findViewById(R.id.btn_softwareversion);
        this.mChangeWallpaperBtn = (ImageButton) this.findViewById(R.id.btn_changewallpaper);
        this.mClearDataBtn = (ImageButton) this.findViewById(R.id.btn_cleardata);
        this.mSublcdSettingBtn.setOnClickListener((View.OnClickListener) this);
        this.mAboutInfreeBtn.setOnClickListener((View.OnClickListener) this);
        this.mAboutInfreeBtn.setVisibility(View.GONE);
        this.mNameshowBtn.setOnClickListener((View.OnClickListener) this);
        this.mNetworkSettingBtn.setOnClickListener((View.OnClickListener) this);
        this.mHardwareVersionBtn.setOnClickListener((View.OnClickListener) this);
        this.mSoftwareVersionBtn.setOnClickListener((View.OnClickListener) this);
        this.mChangeWallpaperBtn.setOnClickListener(this);
        this.mClearDataBtn.setOnClickListener(this);
        this.mAboutInfreeView = (RelativeLayout) this.findViewById(R.id.view_aboutme);
        this.mChangeWallpaperView = (RelativeLayout) this.findViewById(R.id.view_wallpaper);
        this.mSublcdSettingView = (RelativeLayout) this.findViewById(R.id.view_backscreensetting);
        (this.mUsernamebmpView = (RelativeLayout) this.findViewById(R.id.view_namesetting)).setOnClickListener( this);
        this.mSoftwareVersionView = (RelativeLayout) this.findViewById(R.id.view_softwareversion);
        this.mHardwareVersionView = (RelativeLayout) this.findViewById(R.id.view_hardwareversion);
        this.mServerIpAddrSettingView = (RelativeLayout) this.findViewById(R.id.view_serveraddrsetting);
        (this.mServerIpSettingBtn = (ImageButton) this.findViewById(R.id.btn_serveraddr)).setOnClickListener( this);
        (this.mSaveIpAddrBtn = (Button) this.findViewById(R.id.btn_saveIpAddr)).setOnClickListener( this);
        (this.mServerAddrText = (EditText) this.findViewById(R.id.txt_ipAddrEditText)).setHint((CharSequence) SettingManager.getInstance().readSetting("serverip", "", "192.168.1.103"));
        this.mServerAddrText.setEnabled(false);
        this.mUsernamebmpIV = (ImageView) this.findViewById(R.id.view_usernamebmp);
        (this.mSublcdBacklingBtn = (ToggleButton) this.findViewById(R.id.btn_toggleBackscreen)).setOnClickListener( this);
        (this.mDrawDefaultSublcdBtn = (Button) this.findViewById(R.id.btn_drawdefault)).setOnClickListener( this);
        this.mDrawDefaultSublcdBtn.setEnabled(true);
        this.mWallpaperGallery = (Gallery) this.findViewById(R.id.gallery_wallpaper);
        this.mWallManager = new WallpaperManager();
        this.mListWall = this.mWallManager.getWallList();
        this.mWallpaperGallery.setAdapter((SpinnerAdapter) new ImageAdapter((Context) this));
        this.mBackgroundBmpList = new ArrayList<BackgroundBitmapInfo>();
        (this.mIDInfo = (TextView) this.findViewById(R.id.txt_mgid)).setText((CharSequence) ("ID:" + (String) SettingManager.getInstance().readSetting("mgID", "", "")));
        this.mWallpaperGallery.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                ((ImageView) view).startAnimation(SetActivity.this.mScaleToLargeAnim);
                if (SetActivity.this.mListWall.get(n).mDrawable) {
                    final int mId = SetActivity.this.mListWall.get(n).mId;
                    SettingManager.getInstance().writeSetting("iil", new StringBuilder().append(mId).toString());
                    BackgroundManager.getInstance().UpdateBackground((Context) SetActivity.this, new StringBuilder().append(mId).toString());
                } else {
                    final String mFileName = SetActivity.this.mListWall.get(n).mFileName;
                    SettingManager.getInstance().writeSetting("iil", mFileName);
                    BackgroundManager.getInstance().UpdateBackground((Context) SetActivity.this, mFileName);
                }
                final Intent intent = new Intent((Context) SetActivity.this, (Class) mainActivity.class);
                intent.setFlags(268468224);
                SetActivity.this.startActivity(intent);
            }
        });
        ((LinearLayout) this.findViewById(R.id.mainbackground)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBackground());
        (this.mSettingBg = (RelativeLayout) this.findViewById(R.id.settingbg)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBitmapDrawable((Context) this, 2130837837));
        this.mUserNameFontSelect = (Spinner) this.findViewById(R.id.name_font_select);
        this.mFontAdapter = (ArrayAdapter<CharSequence>) ArrayAdapter.createFromResource((Context) this, R.array.fonts_array,android.R.layout.simple_spinner_item);
        this.mFontRealName = this.getResources().getStringArray(R.array.fonts_realname);
        this.mFontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mUserNameFontSelect.setAdapter((SpinnerAdapter) this.mFontAdapter);
        this.mUserNameFontSelect.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SetActivity.access$10(SetActivity.this, n);
                final TextView textView = (TextView) view;
                if (textView != null) {
                    textView.setTextSize(16.0f);
                }
            }

            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        (this.mCompanyFontSelect = (Spinner) this.findViewById(R.id.company_font_select)).setAdapter((SpinnerAdapter) this.mFontAdapter);
        this.mCompanyFontSelect.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SetActivity.access$11(SetActivity.this, n);
                final TextView textView = (TextView) view;
                if (textView != null) {
                    textView.setTextSize(16.0f);
                }
            }

            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        (this.mJobFontSelect = (Spinner) this.findViewById(R.id.job_font_select)).setAdapter((SpinnerAdapter) this.mFontAdapter);
        this.mJobFontSelect.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SetActivity.access$12(SetActivity.this, n);
                final TextView textView = (TextView) view;
                if (textView != null) {
                    textView.setTextSize(16.0f);
                }
            }

            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        this.mUserNameEditText = (EditText) this.findViewById(R.id.edtext_username);
        this.mCompanyEditText = (EditText) this.findViewById(R.id.edtext_company);
        this.mJobEditText = (EditText) this.findViewById(R.id.edtext_job);
        (this.mUserNameColor = (ImageView) this.findViewById(R.id.name_color)).setOnClickListener( this);
        (this.mCompanyColor = (ImageView) this.findViewById(R.id.company_color)).setOnClickListener( this);
        (this.mJobColor = (ImageView) this.findViewById(R.id.job_color)).setOnClickListener( this);
        (this.mStartUserLayoutBtn = (Button) this.findViewById(R.id.btn_pagesetting)).setOnClickListener( this);
        this.mUserNameLayoutView = (RelativeLayout) this.findViewById(R.id.view_usernamebmp_layout);
        this.mLayoutview = (UsernameLayout) this.findViewById(R.id.username_layout);
        this.miNameColor = -256;
        this.miJobColor = -256;
        this.miCompanyColor = -256;
        this.miBackgroundColor = -65536;
        (this.mPureBackgroundColorSelectBtn = (Button) this.findViewById(R.id.btn_purecolor)).setOnClickListener( this);
        (this.mModifyConfirmBtn = (Button) this.findViewById(R.id.btn_modify_confirm)).setOnClickListener( this);
        this.mJobSize = (EditText) this.findViewById(R.id.edtext_jobsize);
        this.mCompanySize = (EditText) this.findViewById(R.id.edtext_companysize);
        this.mNameSize = (EditText) this.findViewById(R.id.edtext_namesize);
        this.mNameBoldCheck = (CheckBox) this.findViewById(R.id.btn_namebold);
        this.mCompanyBoldCheck = (CheckBox) this.findViewById(R.id.btn_companybold);
        this.mJobBoldCheck = (CheckBox) this.findViewById(R.id.btn_jobbold);
        this.mUserNameSettingView = (RelativeLayout) this.findViewById(R.id.view_namesetting);
        (this.mBitmapBackgroundSelectBtn = (Button) this.findViewById(R.id.btn_bitmapbackground)).setOnClickListener( this);
        this.mBackgroundGallery = (Gallery) this.findViewById(R.id.gallery_bitmapbackground);
        this.mBackgroundAdpater = new BackgroundAdapter((Context) this);
        this.mBackgroundGallery.setAdapter((SpinnerAdapter) this.mBackgroundAdpater);
        this.mBackgroundGallery.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SetActivity.access$13(SetActivity.this, SetActivity.this.mBackgroundBmpList.get(n));
                SetActivity.access$14(SetActivity.this, n);
                ((Dialog) new AlertDialog.Builder((Context) SetActivity.this).setTitle((CharSequence) "提示").setMessage((CharSequence) "要进行什么操作？").setPositiveButton((CharSequence) "使用",  new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final Bitmap decodeFile = BitmapFactory.decodeFile(SetActivity.this.mCurrentSelectItem.mFileName);
                        if (decodeFile != null) {
                            SetActivity.this.mLayoutview.setBackgroundBitmap(decodeFile);
                        }
                        SetActivity.this.mBackgroundLinearLayout.setVisibility(View.GONE);
                        dialogInterface.cancel();
                    }
                }).setNegativeButton((CharSequence) "删除", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final File file = new File(SetActivity.this.mCurrentSelectItem.mFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        SetActivity.access$13(SetActivity.this, null);
                        SetActivity.this.mBackgroundBmpList.remove(SetActivity.this.miCurrentSelectItem);
                        SetActivity.this.mBackgroundAdpater.notifyDataSetChanged();
                        dialogInterface.cancel();
                    }
                }).create()).show();
            }
        });
        this.mBackgroundLinearLayout = (RelativeLayout) this.findViewById(R.id.backgroundgallary_layout);
        ((Button) this.findViewById(R.id.btn_import)).setOnClickListener( this);
        if (this.getIntent().getExtras() != null) {
            this.mUsernamebmpView.setVisibility(View.VISIBLE);
            final Bitmap bitmapBySDPath = getBitmapBySDPath(MeetingParam.SDCARD_WELCOME_PICTURE);
            if (bitmapBySDPath != null) {
                this.mUsernamebmpIV.setBackgroundDrawable((Drawable) new BitmapDrawable(bitmapBySDPath));
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        final BitmapDrawable bitmapDrawable = (BitmapDrawable) this.mUsernamebmpIV.getBackground();
        this.mUsernamebmpIV.setBackgroundResource(0);
        if (bitmapDrawable != null) {
            bitmapDrawable.setCallback((Drawable.Callback) null);
            if (bitmapDrawable.getBitmap() != null) {
                bitmapDrawable.getBitmap().recycle();
            }
        }
        if (this.mBackgroundBmpList != null) {
            for (int i = 0; i < this.mBackgroundBmpList.size(); ++i) {
                final Bitmap mBitmap = this.mBackgroundBmpList.get(i).mBitmap;
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
            }
        }
    }

    protected void onPause() {
        SocketManager.getInstance().detach((SocketManager.Observer) this);
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer) this, (Context) this);
        this.mDrawDefaultSublcdBtn.setEnabled(true);
        super.onResume();
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public void showUserName() {
        if (this.mUsernamebmpView.isShown()) {
            final Bitmap bitmapBySDPath = getBitmapBySDPath(MeetingParam.SDCARD_WELCOME_PICTURE);
            if (bitmapBySDPath != null) {
                this.mUsernamebmpIV.setBackgroundDrawable((Drawable) new BitmapDrawable(bitmapBySDPath));
            }
        }
    }

    private class BackgroundAdapter extends BaseAdapter {
        private Context mContext;

        public BackgroundAdapter(final Context mContext) {
            this.mContext = mContext;
        }

        public int getCount() {
            return SetActivity.this.mBackgroundBmpList.size();
        }

        public Object getItem(final int n) {
            return n;
        }

        public long getItemId(final int n) {
            return 0L;
        }

        public View getView(final int n, View view, final ViewGroup viewGroup) {
            ImageView imageView;
            if (view == null) {
                imageView = (ImageView) (view = (View) new ImageView(this.mContext));
            } else {
                imageView = (ImageView) view;
            }
            imageView.setImageBitmap(SetActivity.this.mBackgroundBmpList.get(n).mBitmap);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams( new Gallery.LayoutParams(-2, -2));
            return view;
        }
    }

    private class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(final Context mContext) {
            this.mContext = mContext;
        }

        public int getCount() {
            return SetActivity.this.mListWall.size();
        }

        public Object getItem(final int n) {
            return n;
        }

        public long getItemId(final int n) {
            return n;
        }

        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            ImageView imageView;
            if (view == null) {
                imageView = new ImageView(this.mContext);
            } else {
                imageView = (ImageView) view;
            }
            if (SetActivity.this.mListWall.get(n).mDrawable) {
                imageView.setImageResource(SetActivity.this.mListWall.get(n).mSmallid);
            } else {
                final String mFileName = SetActivity.this.mListWall.get(n).mFileName;
                final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
                bitmapFactory$Options.inSampleSize = 6;
                imageView.setImageBitmap(BitmapFactory.decodeFile(mFileName, bitmapFactory$Options));
            }
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams((ViewGroup.LayoutParams) new Gallery.LayoutParams(100, 80));
            return (View) imageView;
        }
    }

    private class ReConnection extends Thread {
        @Override
        public void run() {
            SocketManager.getInstance().ReConnection(SetActivity.this.mIpAddr);
        }
    }

    private class getBackgroundBitmapThread extends Thread {
        @Override
        public void run() {
            SetActivity.access$7(true);
            super.run();
            for (int i = 0; i < SetActivity.this.mBackgroundBmpList.size(); ++i) {
                final Bitmap mBitmap = SetActivity.this.mBackgroundBmpList.get(i).mBitmap;
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
            }
            SetActivity.this.mBackgroundBmpList.clear();
            final String string = String.valueOf(MeetingParam.SDCARD_ROOT) + "mpbackground/";
            final File file = new File(string);
            if (!file.exists()) {
                file.mkdir();
            }
            final String[] list = new File("/mnt/extsd/Meeting/铭牌背景/").list();
            if (list != null) {
                for (int j = 0; j < list.length; ++j) {
                    Log.e("sdcardfile", list[j]);
                    final int length = list[j].length();
                    if (list[j].subSequence(length - 4, length).equals(".png")) {
                        FileUtils.CopyFile(String.valueOf("/mnt/extsd/Meeting/铭牌背景/") + list[j], String.valueOf(string) + list[j]);
                    }
                }
            }
            final String[] list2 = new File("/mnt/usb_storage/Meeting/铭牌背景/").list();
            if (list2 != null) {
                for (int k = 0; k < list2.length; ++k) {
                    Log.e("sdcardfile", list2[k]);
                    final int length2 = list2[k].length();
                    if (list2[k].subSequence(length2 - 4, length2).equals(".png")) {
                        FileUtils.CopyFile(String.valueOf("/mnt/usb_storage/Meeting/铭牌背景/") + list2[k], String.valueOf(string) + list2[k]);
                    }
                }
            }
            final String[] list3 = file.list();
            if (list3 != null) {
                final BitmapFactory.Options bitmapFactory$Options = new BitmapFactory.Options();
                for (int l = 0; l < list3.length; ++l) {
                    final int length3 = list3[l].length();
                    if (list3[l].subSequence(length3 - 4, length3).equals(".png")) {
                        bitmapFactory$Options.inSampleSize = 1;
                        bitmapFactory$Options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(String.valueOf(string) + list3[l], bitmapFactory$Options);
                        if (bitmapFactory$Options.outHeight == 480 && bitmapFactory$Options.outWidth == 800) {
                            bitmapFactory$Options.inJustDecodeBounds = false;
                            bitmapFactory$Options.inSampleSize = 2;
                            final Bitmap decodeFile = BitmapFactory.decodeFile(String.valueOf(string) + list3[l], bitmapFactory$Options);
                            Log.e("sdcardfile", list3[l]);
                            SetActivity.this.mBackgroundBmpList.add(new BackgroundBitmapInfo(decodeFile, String.valueOf(string) + list3[l]));
                        }
                    }
                }
            }
            SetActivity.this.handler.obtainMessage(3).sendToTarget();
            SetActivity.access$7(false);
        }
    }

    private class prossss extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(final Void... array) {
            try {
                Thread.sleep(1000L);
                return null;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(final Void void1) {
            ToastHint.show((Context) SetActivity.this, "删除成功!");
            ProgressDialogHint.Dismiss();
        }
    }
}