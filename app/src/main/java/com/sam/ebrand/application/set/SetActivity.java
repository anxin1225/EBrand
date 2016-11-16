package com.sam.ebrand.application.set;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.manage.SubLcdManager;
import com.sam.ebrand.manage.SubLcdMsg;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import java.io.File;
import java.util.ArrayList;

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
                        ToastHint.show((Context) SetActivity.this, "\u52a0\u8f7d\u6210\u529f!");
                        ProgressDialogHint.Dismiss();
                    }
                    case 2: {
                        SetActivity.access$1(SetActivity.this, (String) message.obj);
                        new ReConnection(null).start();
                    }
                    case 3: {
                        ProgressDialogHint.Dismiss();
                        SetActivity.this.mBackgroundAdpater.notifyDataSetChanged();
                        if (SetActivity.this.mBackgroundBmpList.size() > 0) {
                            SetActivity.this.mBackgroundLinearLayout.setVisibility(0);
                            return;
                        }
                        ToastHint.show((Context) SetActivity.this, "\u6ca1\u6709\u627e\u5230\u94ed\u724c\u80cc\u666f\u56fe\u7247");
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
            this.mBackgroundLinearLayout.setVisibility(8);
            return;
        }
        if (this.mUserNameLayoutView.isShown()) {
            this.mUserNameLayoutView.setVisibility(8);
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
            case 2131427528: {
                this.mAboutInfreeView.setVisibility(8);
                this.mChangeWallpaperView.setVisibility(8);
                this.mSublcdSettingView.setVisibility(8);
                this.mSoftwareVersionView.setVisibility(8);
                this.mHardwareVersionView.setVisibility(8);
                this.mServerIpAddrSettingView.setVisibility(0);
                this.mServerAddrText.setEnabled(true);
                this.mUserNameSettingView.setVisibility(8);
            }
            case 2131427545: {
                final String string = this.mServerAddrText.getText().toString();
                string.trim();
                if (!InetAddressUtils.isIPv4Address(string)) {
                    ((Dialog) new AlertDialog.Builder((Context) this).setCancelable(false).setTitle((CharSequence) "\u63d0\u793a").setMessage((CharSequence) "\u8f93\u5165\u7684IP\u5730\u5740\u6709\u8bef\uff01").setPositiveButton((CharSequence) "\u786e\u5b9a", (DialogInterface$OnClickListener) new DialogInterface$OnClickListener() {
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
                ToastHint.show(this.getApplicationContext(), "\u4fee\u6539\u670d\u52a1\u5668\u5730\u5740\u6210\u529f\uff01");
                this.handler.obtainMessage(2, (Object) string).sendToTarget();
            }
            case 2131427534: {
                if (this.mSublcdBacklingBtn.isChecked()) {
                    SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 2));
                    return;
                }
                SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(0, 2));
            }
            case 2131427526: {
                this.mAboutInfreeView.setVisibility(8);
                this.mChangeWallpaperView.setVisibility(8);
                this.mSublcdSettingView.setVisibility(8);
                this.mSoftwareVersionView.setVisibility(0);
                this.mHardwareVersionView.setVisibility(8);
                this.mServerIpAddrSettingView.setVisibility(8);
                this.mUserNameSettingView.setVisibility(8);
            }
            case 2131427527: {
                this.mAboutInfreeView.setVisibility(8);
                this.mChangeWallpaperView.setVisibility(8);
                this.mSublcdSettingView.setVisibility(8);
                this.mSoftwareVersionView.setVisibility(8);
                this.mHardwareVersionView.setVisibility(0);
                this.mServerIpAddrSettingView.setVisibility(8);
                this.mUserNameSettingView.setVisibility(8);
            }
            case 2131427535: {
                if (this.mSublcdBacklingBtn.isChecked()) {
                    SubLcdManager.getInstance().OperateSubLcd(new SubLcdMsg(1, 1));
                    return;
                }
                break;
            }
            case 2131427522: {
                this.mAboutInfreeView.setVisibility(8);
                this.mChangeWallpaperView.setVisibility(8);
                this.mSublcdSettingView.setVisibility(8);
                this.mSoftwareVersionView.setVisibility(8);
                this.mHardwareVersionView.setVisibility(8);
                this.mServerIpAddrSettingView.setVisibility(8);
                this.mUserNameSettingView.setVisibility(8);
                final Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.android.settings", "com.android.settings.Settings"));
                intent.setAction("android.intent.action.VIEW");
                intent.setFlags(270532608);
                this.startActivity(intent);
            }
            case 2131427521: {
                this.mAboutInfreeView.setVisibility(0);
                this.mChangeWallpaperView.setVisibility(8);
                this.mSublcdSettingView.setVisibility(8);
                this.mSoftwareVersionView.setVisibility(8);
                this.mHardwareVersionView.setVisibility(8);
                this.mServerIpAddrSettingView.setVisibility(8);
                this.mUserNameSettingView.setVisibility(8);
            }
            case 2131427523: {
                this.mAboutInfreeView.setVisibility(8);
                this.mChangeWallpaperView.setVisibility(0);
                this.mSublcdSettingView.setVisibility(8);
                this.mSoftwareVersionView.setVisibility(8);
                this.mHardwareVersionView.setVisibility(8);
                this.mServerIpAddrSettingView.setVisibility(8);
                this.mUserNameSettingView.setVisibility(8);
            }
            case 2131427525: {
                this.mAboutInfreeView.setVisibility(8);
                this.mChangeWallpaperView.setVisibility(8);
                this.mSublcdSettingView.setVisibility(0);
                this.mSoftwareVersionView.setVisibility(8);
                this.mHardwareVersionView.setVisibility(8);
                this.mServerIpAddrSettingView.setVisibility(8);
                this.mUserNameSettingView.setVisibility(8);
            }
            case 2131427440: {
                ((Dialog) new AlertDialog.Builder((Context) this).setTitle((CharSequence) "\u63d0\u793a").setMessage((CharSequence) "\u662f\u5426\u6e05\u7a7a\u4f1a\u8bae\u6570\u636e\uff1f").setPositiveButton((CharSequence) "\u786e\u5b9a", (DialogInterface$OnClickListener) new DialogInterface$OnClickListener() {
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
                        ProgressDialogHint.Show((Context) SetActivity.this, "\u63d0\u793a", "\u6b63\u5728\u5220\u9664,\u8bf7\u7a0d\u540e...");
                        new prossss(null).execute((Object[]) new Void[0]);
                    }
                }).setNegativeButton((CharSequence) "\u53d6\u6d88", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                    }
                }).create()).show();
            }
            case 2131427524: {
                final Intent intent2 = new Intent((Context) this, (Class) WelcomeActivity.class);
                intent2.setFlags(67108864);
                this.startActivity(intent2);
            }
            case 2131427508: {
                if (!this.isMain) {
                    final Intent intent3 = new Intent((Context) this, (Class) mainActivity.class);
                    intent3.setFlags(67108864);
                    this.startActivity(intent3);
                    return;
                }
                break;
            }
            case 2131427509: {
                final Intent intent4 = new Intent((Context) this, (Class) WelcomeActivity.class);
                intent4.setFlags(67108864);
                this.startActivity(intent4);
            }
            case 2131427510: {
                this.startActivity(new Intent((Context) this, (Class) NoticeActivity.class));
            }
            case 2131427511: {
                this.finish();
            }
            case 2131427468: {
                new ColorPickerDialog((Context) this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$18(SetActivity.this, backgroundColor);
                        SetActivity.this.mUserNameColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miNameColor).show();
            }
            case 2131427470: {
                new ColorPickerDialog((Context) this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$20(SetActivity.this, backgroundColor);
                        SetActivity.this.mJobColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miJobColor).show();
            }
            case 2131427469: {
                new ColorPickerDialog((Context) this, new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$22(SetActivity.this, backgroundColor);
                        SetActivity.this.mCompanyColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miCompanyColor).show();
            }
            case 2131427477: {
                final String string2 = this.mUserNameEditText.getText().toString();
                final String string3 = this.mJobEditText.getText().toString();
                final String string4 = this.mCompanyEditText.getText().toString();
                if (string2.equals("") && string3.equals("") && string4.equals("")) {
                    ToastHint.show(this.getApplicationContext(), "\u8bf7\u8f93\u5165\u76f8\u5173\u4fe1\u606f");
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
                this.mUserNameLayoutView.setVisibility(0);
                this.mLayoutview.setNameFontAndColor(string2, string3, string4, ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miNameFontPos]), ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miJobFontPos]), ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miCompanyFontPos]), this.miNameColor, this.miJobColor, this.miCompanyColor, int1, int3, int2, this.mNameBoldCheck.isChecked(), this.mJobBoldCheck.isChecked(), this.mCompanyBoldCheck.isChecked());
            }
            case 2131427453: {
                new ColorPickerDialog((Context) this,new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        SetActivity.access$24(SetActivity.this, backgroundColor);
                        SetActivity.this.mLayoutview.setBackgroundColor(backgroundColor);
                    }
                }, this.miBackgroundColor).show();
            }
            case 2131427455: {
                this.mLayoutview.saveToBitmapAndRefresh();
                final SharedPreferences.Editor editor2 = SettingManager.getInstance().getEditor();
                editor2.putInt("CustomUserNameColor", -1);
                editor2.putString("CustomUserNameFont", this.mUserNameFontName);
                editor2.putInt("CustomUserNameSize", this.miNameSize);
                editor2.commit();
                this.mModifyConfirmBtn.setEnabled(false);
            }
            case 2131427454: {
                ProgressDialogHint.Show((Context) this, "\u8bf7\u7a0d\u5019\u2026\u2026", "\u6b63\u5728\u52a0\u8f7d\u56fe\u7247\u2026\u2026");
                if (!SetActivity.mGetBitmapThreadRunning) {
                    new getBackgroundBitmapThread((getBackgroundBitmapThread) null).start();
                    return;
                }
                break;
            }
            case 2131427452: {
                this.mLayoutview.InitPos();
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(2130903072);
        ExitApplication.getInstance().addActivity(this);
        (this.mMainBtn = (ImageButton) this.findViewById(2131427508)).setOnClickListener((View$OnClickListener) this);
        (this.mWelcomeBtn = (ImageButton) this.findViewById(2131427509)).setOnClickListener((View$OnClickListener) this);
        this.mSystemNoticeBtn = (ImageButton) this.findViewById(2131427510);
        this.mGoBackBtn = (ImageButton) this.findViewById(2131427511);
        this.mSystemNoticeBtn.setOnClickListener((View$OnClickListener) this);
        this.mGoBackBtn.setOnClickListener((View$OnClickListener) this);
        this.mScaleToLargeAnim = AnimationUtils.loadAnimation((Context) this, 2130968576);
        this.mSublcdSettingBtn = (ImageButton) this.findViewById(2131427525);
        this.mAboutInfreeBtn = (ImageButton) this.findViewById(2131427521);
        this.mNameshowBtn = (ImageButton) this.findViewById(2131427524);
        this.mNetworkSettingBtn = (ImageButton) this.findViewById(2131427522);
        this.mHardwareVersionBtn = (ImageButton) this.findViewById(2131427527);
        this.mSoftwareVersionBtn = (ImageButton) this.findViewById(2131427526);
        this.mChangeWallpaperBtn = (ImageButton) this.findViewById(2131427523);
        this.mClearDataBtn = (ImageButton) this.findViewById(2131427440);
        this.mSublcdSettingBtn.setOnClickListener((View$OnClickListener) this);
        this.mAboutInfreeBtn.setOnClickListener((View$OnClickListener) this);
        this.mAboutInfreeBtn.setVisibility(8);
        this.mNameshowBtn.setOnClickListener((View$OnClickListener) this);
        this.mNetworkSettingBtn.setOnClickListener((View$OnClickListener) this);
        this.mHardwareVersionBtn.setOnClickListener((View$OnClickListener) this);
        this.mSoftwareVersionBtn.setOnClickListener((View$OnClickListener) this);
        this.mChangeWallpaperBtn.setOnClickListener((View$OnClickListener) this);
        this.mClearDataBtn.setOnClickListener((View$OnClickListener) this);
        this.mAboutInfreeView = (RelativeLayout) this.findViewById(2131427529);
        this.mChangeWallpaperView = (RelativeLayout) this.findViewById(2131427531);
        this.mSublcdSettingView = (RelativeLayout) this.findViewById(2131427533);
        (this.mUsernamebmpView = (RelativeLayout) this.findViewById(2131427516)).setOnClickListener((View$OnClickListener) this);
        this.mSoftwareVersionView = (RelativeLayout) this.findViewById(2131427536);
        this.mHardwareVersionView = (RelativeLayout) this.findViewById(2131427538);
        this.mServerIpAddrSettingView = (RelativeLayout) this.findViewById(2131427543);
        (this.mServerIpSettingBtn = (ImageButton) this.findViewById(2131427528)).setOnClickListener((View$OnClickListener) this);
        (this.mSaveIpAddrBtn = (Button) this.findViewById(2131427545)).setOnClickListener((View$OnClickListener) this);
        (this.mServerAddrText = (EditText) this.findViewById(2131427544)).setHint((CharSequence) SettingManager.getInstance().readSetting("serverip", "", "192.168.1.103"));
        this.mServerAddrText.setEnabled(false);
        this.mUsernamebmpIV = (ImageView) this.findViewById(2131427517);
        (this.mSublcdBacklingBtn = (ToggleButton) this.findViewById(2131427534)).setOnClickListener((View$OnClickListener) this);
        (this.mDrawDefaultSublcdBtn = (Button) this.findViewById(2131427535)).setOnClickListener((View$OnClickListener) this);
        this.mDrawDefaultSublcdBtn.setEnabled(true);
        this.mWallpaperGallery = (Gallery) this.findViewById(2131427532);
        this.mWallManager = new WallpaperManager();
        this.mListWall = this.mWallManager.getWallList();
        this.mWallpaperGallery.setAdapter((SpinnerAdapter) new ImageAdapter((Context) this));
        this.mBackgroundBmpList = new ArrayList<BackgroundBitmapInfo>();
        (this.mIDInfo = (TextView) this.findViewById(2131427530)).setText((CharSequence) ("ID:" + (String) SettingManager.getInstance().readSetting("mgID", "", "")));
        this.mWallpaperGallery.setOnItemClickListener((AdapterView$OnItemClickListener) new AdapterView$OnItemClickListener() {
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
        ((LinearLayout) this.findViewById(2131427377)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBackground());
        (this.mSettingBg = (RelativeLayout) this.findViewById(2131427518)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBitmapDrawable((Context) this, 2130837837));
        this.mUserNameFontSelect = (Spinner) this.findViewById(2131427465);
        this.mFontAdapter = (ArrayAdapter<CharSequence>) ArrayAdapter.createFromResource((Context) this, 2131165187, 17367048);
        this.mFontRealName = this.getResources().getStringArray(2131165187);
        this.mFontAdapter.setDropDownViewResource(17367049);
        this.mUserNameFontSelect.setAdapter((SpinnerAdapter) this.mFontAdapter);
        this.mUserNameFontSelect.setOnItemSelectedListener((AdapterView$OnItemSelectedListener) new AdapterView$OnItemSelectedListener() {
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
        (this.mCompanyFontSelect = (Spinner) this.findViewById(2131427466)).setAdapter((SpinnerAdapter) this.mFontAdapter);
        this.mCompanyFontSelect.setOnItemSelectedListener((AdapterView$OnItemSelectedListener) new AdapterView$OnItemSelectedListener() {
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
        (this.mJobFontSelect = (Spinner) this.findViewById(2131427467)).setAdapter((SpinnerAdapter) this.mFontAdapter);
        this.mJobFontSelect.setOnItemSelectedListener((AdapterView$OnItemSelectedListener) new AdapterView$OnItemSelectedListener() {
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
        this.mUserNameEditText = (EditText) this.findViewById(2131427462);
        this.mCompanyEditText = (EditText) this.findViewById(2131427463);
        this.mJobEditText = (EditText) this.findViewById(2131427464);
        (this.mUserNameColor = (ImageView) this.findViewById(2131427468)).setOnClickListener((View$OnClickListener) this);
        (this.mCompanyColor = (ImageView) this.findViewById(2131427469)).setOnClickListener((View$OnClickListener) this);
        (this.mJobColor = (ImageView) this.findViewById(2131427470)).setOnClickListener((View$OnClickListener) this);
        (this.mStartUserLayoutBtn = (Button) this.findViewById(2131427477)).setOnClickListener((View$OnClickListener) this);
        this.mUserNameLayoutView = (RelativeLayout) this.findViewById(2131427450);
        this.mLayoutview = (UsernameLayout) this.findViewById(2131427451);
        this.miNameColor = -256;
        this.miJobColor = -256;
        this.miCompanyColor = -256;
        this.miBackgroundColor = -65536;
        (this.mPureBackgroundColorSelectBtn = (Button) this.findViewById(2131427453)).setOnClickListener((View$OnClickListener) this);
        (this.mModifyConfirmBtn = (Button) this.findViewById(2131427455)).setOnClickListener((View$OnClickListener) this);
        this.mJobSize = (EditText) this.findViewById(2131427473);
        this.mCompanySize = (EditText) this.findViewById(2131427472);
        this.mNameSize = (EditText) this.findViewById(2131427471);
        this.mNameBoldCheck = (CheckBox) this.findViewById(2131427474);
        this.mCompanyBoldCheck = (CheckBox) this.findViewById(2131427475);
        this.mJobBoldCheck = (CheckBox) this.findViewById(2131427476);
        this.mUserNameSettingView = (RelativeLayout) this.findViewById(2131427458);
        (this.mBitmapBackgroundSelectBtn = (Button) this.findViewById(2131427454)).setOnClickListener((View$OnClickListener) this);
        this.mBackgroundGallery = (Gallery) this.findViewById(2131427457);
        this.mBackgroundAdpater = new BackgroundAdapter((Context) this);
        this.mBackgroundGallery.setAdapter((SpinnerAdapter) this.mBackgroundAdpater);
        this.mBackgroundGallery.setOnItemClickListener((AdapterView$OnItemClickListener) new AdapterView$OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                SetActivity.access$13(SetActivity.this, SetActivity.this.mBackgroundBmpList.get(n));
                SetActivity.access$14(SetActivity.this, n);
                ((Dialog) new AlertDialog$Builder((Context) SetActivity.this).setTitle((CharSequence) "\u63d0\u793a").setMessage((CharSequence) "\u8981\u8fdb\u884c\u4ec0\u4e48\u64cd\u4f5c\uff1f").setPositiveButton((CharSequence) "\u4f7f\u7528", (DialogInterface$OnClickListener) new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final Bitmap decodeFile = BitmapFactory.decodeFile(SetActivity.this.mCurrentSelectItem.mFileName);
                        if (decodeFile != null) {
                            SetActivity.this.mLayoutview.setBackgroundBitmap(decodeFile);
                        }
                        SetActivity.this.mBackgroundLinearLayout.setVisibility(8);
                        dialogInterface.cancel();
                    }
                }).setNegativeButton((CharSequence) "\u5220\u9664", (DialogInterface$OnClickListener) new DialogInterface$OnClickListener() {
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
        this.mBackgroundLinearLayout = (RelativeLayout) this.findViewById(2131427456);
        ((Button) this.findViewById(2131427452)).setOnClickListener((View$OnClickListener) this);
        if (this.getIntent().getExtras() != null) {
            this.mUsernamebmpView.setVisibility(0);
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
            bitmapDrawable.setCallback((Drawable$Callback) null);
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
            imageView.setLayoutParams((ViewGroup$LayoutParams) new Gallery$LayoutParams(-2, -2));
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
                final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
                bitmapFactory$Options.inSampleSize = 6;
                imageView.setImageBitmap(BitmapFactory.decodeFile(mFileName, bitmapFactory$Options));
            }
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams((ViewGroup$LayoutParams) new Gallery$LayoutParams(100, 80));
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
            final String[] list = new File("/mnt/extsd/Meeting/\u94ed\u724c\u80cc\u666f/").list();
            if (list != null) {
                for (int j = 0; j < list.length; ++j) {
                    Log.e("sdcardfile", list[j]);
                    final int length = list[j].length();
                    if (list[j].subSequence(length - 4, length).equals(".png")) {
                        FileUtils.CopyFile(String.valueOf("/mnt/extsd/Meeting/\u94ed\u724c\u80cc\u666f/") + list[j], String.valueOf(string) + list[j]);
                    }
                }
            }
            final String[] list2 = new File("/mnt/usb_storage/Meeting/\u94ed\u724c\u80cc\u666f/").list();
            if (list2 != null) {
                for (int k = 0; k < list2.length; ++k) {
                    Log.e("sdcardfile", list2[k]);
                    final int length2 = list2[k].length();
                    if (list2[k].subSequence(length2 - 4, length2).equals(".png")) {
                        FileUtils.CopyFile(String.valueOf("/mnt/usb_storage/Meeting/\u94ed\u724c\u80cc\u666f/") + list2[k], String.valueOf(string) + list2[k]);
                    }
                }
            }
            final String[] list3 = file.list();
            if (list3 != null) {
                final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
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
            ToastHint.show((Context) SetActivity.this, "\u5220\u9664\u6210\u529f!");
            ProgressDialogHint.Dismiss();
        }
    }
}