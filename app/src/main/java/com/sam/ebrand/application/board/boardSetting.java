package com.sam.ebrand.application.board;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.sam.ebrand.R;
import com.sam.ebrand.application.WelcomeActivity;
import com.sam.ebrand.application.mainActivity;
import com.sam.ebrand.application.notice.NoticeActivity;
import com.sam.ebrand.application.set.BackgroundBitmapInfo;
import com.sam.ebrand.application.set.ColorPickerDialog;
import com.sam.ebrand.application.set.SetActivity;
import com.sam.ebrand.application.set.UsernameLayout;
import com.sam.ebrand.manage.BackgroundManager;
import com.sam.ebrand.manage.ServerFontsManager;
import com.sam.ebrand.manage.SettingManager;
import com.sam.ebrand.manage.SocketManager;
import com.sam.ebrand.manage.SocketNoticeManager;
import com.sam.ebrand.param.MeetingParam;
import com.sam.ebrand.util.FileUtils;
import com.sam.ebrand.widget.ProgressDialogHint;
import com.sam.ebrand.widget.ToastHint;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by sam on 2016/11/18.
 */

public class boardSetting extends Activity implements View.OnClickListener, SocketManager.Observer
{
    private static boolean mGetBitmapThreadRunning;
    private boolean bAutoImport;
    private Handler handler;
    private BackgroundAdapter mBackgroundAdpater;
    private ArrayList<BackgroundBitmapInfo> mBackgroundBmpList;
    private Gallery mBackgroundGallery;
    private RelativeLayout mBackgroundLinearLayout;
    private Button mBitmapBackgroundSelectBtn;
    private CheckBox mCompanyBoldCheck;
    private ImageView mCompanyColor;
    private EditText mCompanyEditText;
    private Spinner mCompanyFontSelect;
    private String mCompanyNameFont;
    private EditText mCompanySize;
    private BackgroundBitmapInfo mCurrentSelectItem;
    private ArrayAdapter<CharSequence> mFontAdapter;
    private String[] mFontRealName;
    private CheckBox mJobBoldCheck;
    private ImageView mJobColor;
    private EditText mJobEditText;
    private Spinner mJobFontSelect;
    private String mJobNameFont;
    private EditText mJobSize;
    private UsernameLayout mLayoutview;
    private Button mModifyConfirmBtn;
    private CheckBox mNameBoldCheck;
    private EditText mNameSize;
    private Button mPureBackgroundColorSelectBtn;
    private Button mStartUserLayoutBtn;
    private ImageView mUserNameColor;
    private EditText mUserNameEditText;
    private String mUserNameFont;
    private String mUserNameFontName;
    private Spinner mUserNameFontSelect;
    private RelativeLayout mUserNameLayoutView;
    private RelativeLayout mUserNameSettingView;
    private ImageView mainBtn;
    private int miBackgroundColor;
    private int miCompanyColor;
    private int miCompanyFontPos;
    private int miCompanySize;
    private long miCurrentBakcPressTime;
    private int miCurrentSelectItem;
    private int miJobColor;
    private int miJobFontPos;
    private int miJobSize;
    private int miNameColor;
    private int miNameFontPos;
    private int miNameSize;
    private ImageButton notice;
    private ImageView returnBtn;
    private ImageButton screensaver;

    public boardSetting() {
        this.bAutoImport = false;
        this.handler = new Handler() {
            public void handleMessage(final Message message) {
                switch (message.what) {
                    default: {}
                    case 255: {
                        ToastHint.show((Context)boardSetting.this, "设置铭牌成功");
                        boardSetting.this.mModifyConfirmBtn.setEnabled(true);
                        final Intent intent = new Intent((Context)boardSetting.this, (Class)SetActivity.class);
                        intent.putExtra("showanme", true);
                        boardSetting.this.startActivity(intent);
                    }
                    case 3: {
                        ProgressDialogHint.Dismiss();
                        boardSetting.this.mBackgroundAdpater.notifyDataSetChanged();
                        if (boardSetting.this.mBackgroundBmpList.size() > 0) {
                            boardSetting.this.mBackgroundLinearLayout.setVisibility(0);
                            return;
                        }
                        ToastHint.show((Context)boardSetting.this, "没有铭牌背景图片");
                    }
                }
            }
        };
    }

    static /* synthetic */ void access$10(final boardSetting boardSetting, final int miCompanyFontPos) {
        boardSetting.miCompanyFontPos = miCompanyFontPos;
    }

    static /* synthetic */ void access$11(final boardSetting boardSetting, final String mCompanyNameFont) {
        boardSetting.mCompanyNameFont = mCompanyNameFont;
    }

    static /* synthetic */ void access$12(final boardSetting boardSetting, final int miJobFontPos) {
        boardSetting.miJobFontPos = miJobFontPos;
    }

    static /* synthetic */ void access$13(final boardSetting boardSetting, final String mJobNameFont) {
        boardSetting.mJobNameFont = mJobNameFont;
    }

    static /* synthetic */ void access$14(final boardSetting boardSetting, final BackgroundBitmapInfo mCurrentSelectItem) {
        boardSetting.mCurrentSelectItem = mCurrentSelectItem;
    }

    static /* synthetic */ void access$15(final boardSetting boardSetting, final int miCurrentSelectItem) {
        boardSetting.miCurrentSelectItem = miCurrentSelectItem;
    }

    static /* synthetic */ void access$19(final boardSetting boardSetting, final int miBackgroundColor) {
        boardSetting.miBackgroundColor = miBackgroundColor;
    }

    static /* synthetic */ void access$20(final boardSetting boardSetting, final int miNameColor) {
        boardSetting.miNameColor = miNameColor;
    }

    static /* synthetic */ void access$22(final boardSetting boardSetting, final int miJobColor) {
        boardSetting.miJobColor = miJobColor;
    }

    static /* synthetic */ void access$24(final boardSetting boardSetting, final int miCompanyColor) {
        boardSetting.miCompanyColor = miCompanyColor;
    }

    static /* synthetic */ void access$4(final boolean mGetBitmapThreadRunning) {
        boardSetting.mGetBitmapThreadRunning = mGetBitmapThreadRunning;
    }

    static /* synthetic */ void access$6(final boardSetting boardSetting, final int miNameFontPos) {
        boardSetting.miNameFontPos = miNameFontPos;
    }

    static /* synthetic */ void access$8(final boardSetting boardSetting, final String mUserNameFont) {
        boardSetting.mUserNameFont = mUserNameFont;
    }

    private void init() {
        final SharedPreferences sharedPrefrences = SettingManager.getInstance().getSharedPrefrences();
        final String string = sharedPrefrences.getString("NOCustomUserName", "");
        final String string2 = sharedPrefrences.getString("customCompanyName", "");
        final String string3 = sharedPrefrences.getString("customJobName", "");
        final int int1 = sharedPrefrences.getInt("NOCustomUserNameColor", -256);
        final int int2 = sharedPrefrences.getInt("customCompanyColor", -256);
        final int int3 = sharedPrefrences.getInt("customJobColor", -256);
        this.miNameColor = int1;
        this.miCompanyColor = int2;
        this.miJobColor = int3;
        final int int4 = sharedPrefrences.getInt("NOCustomUserNameSize", 20);
        final int int5 = sharedPrefrences.getInt("customCompanySize", 6);
        final int int6 = sharedPrefrences.getInt("customJobSize", 6);
        this.miNameSize = int4;
        this.miJobSize = int6;
        this.miCompanySize = int5;
        final int int7 = sharedPrefrences.getInt("NOCustomUserNameFont", 2);
        final int int8 = sharedPrefrences.getInt("customCompanyFont", 2);
        final int int9 = sharedPrefrences.getInt("customJobFont", 2);
        this.mUserNameEditText.setText((CharSequence)string);
        this.mCompanyEditText.setText((CharSequence)string2);
        this.mJobEditText.setText((CharSequence)string3);
        this.mNameSize.setText((CharSequence)String.valueOf(int4));
        this.mCompanySize.setText((CharSequence)String.valueOf(int5));
        this.mJobSize.setText((CharSequence)String.valueOf(int6));
        this.mUserNameColor.setBackgroundColor(int1);
        this.mCompanyColor.setBackgroundColor(int2);
        this.mJobColor.setBackgroundColor(int3);
        if (int7 >= 0 && int7 < 8) {
            this.mUserNameFontSelect.setSelection(int7);
        }
        if (int8 >= 0 && int8 < 8) {
            this.mCompanyFontSelect.setSelection(int8);
        }
        if (int9 >= 0 && int9 < 8) {
            this.mJobFontSelect.setSelection(int9);
        }
        this.miJobFontPos = int9;
        this.miCompanyFontPos = int8;
        this.miNameFontPos = int7;
    }

    public int getFlags() {
        return 0;
    }

    public void handleCommand(final int n, final byte[] array) {
    }

    public void notice(final int n, final Object o) {
    }

    public void onBackPressed() {
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        final long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.miCurrentBakcPressTime >= 500L) {
            this.miCurrentBakcPressTime = currentTimeMillis;
            if (this.bAutoImport) {
                this.finish();
                return;
            }
            if (this.mBackgroundLinearLayout.isShown()) {
                this.mBackgroundLinearLayout.setVisibility(View.GONE);
                return;
            }
            if (this.mUserNameLayoutView.isShown()) {
                this.mUserNameLayoutView.setVisibility(View.GONE);
                this.mLayoutview.saveCurrentLayout();
                this.mUserNameSettingView.setVisibility(View.VISIBLE);
                return;
            }
            if (this.mUserNameSettingView.isShown()) {
                this.finish();
            }
        }
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.btn_initPos: {
                this.mLayoutview.InitPos();
            }
            case R.id.btn_bitmapbackground: {
                ProgressDialogHint.Show((Context)this, "请稍候……", "正在加载图片……");
                if (!boardSetting.mGetBitmapThreadRunning) {
                    new getBackgroundBitmapThread().start();
                    return;
                }
                break;
            }
            case R.id.btn_modify_confirm: {
                this.mLayoutview.saveToBitmapAndRefresh();
                final SharedPreferences.Editor editor = SettingManager.getInstance().getEditor();
                editor.putInt("CustomUserNameColor", -1);
                editor.putString("CustomUserNameFont", this.mUserNameFontName);
                editor.putInt("CustomUserNameSize", this.miNameSize);
                editor.commit();
                this.mModifyConfirmBtn.setEnabled(false);
            }
            case R.id.btn_purecolor: {
                new ColorPickerDialog((Context)this, (ColorPickerDialog.OnColorChangedListener)new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        boardSetting.access$19(boardSetting.this, backgroundColor);
                        boardSetting.this.mLayoutview.setBackgroundColor(backgroundColor);
                        SettingManager.getInstance().writeSetting("boardbackground", new StringBuilder().append(backgroundColor).toString());
                    }
                }, this.miBackgroundColor).show();
            }
            case R.id.name_color: {
                new ColorPickerDialog((Context)this, (ColorPickerDialog.OnColorChangedListener)new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        boardSetting.access$20(boardSetting.this, backgroundColor);
                        boardSetting.this.mUserNameColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miNameColor).show();
            }
            case R.id.job_color: {
                new ColorPickerDialog((Context)this, (ColorPickerDialog.OnColorChangedListener)new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        boardSetting.access$22(boardSetting.this, backgroundColor);
                        boardSetting.this.mJobColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miJobColor).show();
            }
            case R.id.company_color: {
                new ColorPickerDialog((Context)this, (ColorPickerDialog.OnColorChangedListener)new ColorPickerDialog.OnColorChangedListener() {
                    @Override
                    public void colorChanged(final int backgroundColor) {
                        boardSetting.access$24(boardSetting.this, backgroundColor);
                        boardSetting.this.mCompanyColor.setBackgroundColor(backgroundColor);
                    }
                }, this.miCompanyColor).show();
            }
            case R.id.btn_pagesetting: {
                final String string = this.mUserNameEditText.getText().toString();
                final String string2 = this.mJobEditText.getText().toString();
                final String string3 = this.mCompanyEditText.getText().toString();
                if (string.equals("") && string2.equals("") && string3.equals("")) {
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
                final SharedPreferences.Editor editor2 = SettingManager.getInstance().getEditor();
                editor2.putString("NOCustomUserName", string);
                editor2.putInt("NOCustomUserNameColor", this.miNameColor);
                editor2.putInt("NOCustomUserNameFont", this.miNameFontPos);
                editor2.putInt("NOCustomUserNameSize", int1);
                editor2.putString("customCompanyName", string3);
                editor2.putInt("customCompanyColor", this.miCompanyColor);
                editor2.putInt("customCompanyFont", this.miCompanyFontPos);
                editor2.putInt("customCompanySize", int2);
                editor2.putString("customJobName", string2);
                editor2.putInt("customJobColor", this.miJobColor);
                editor2.putInt("customJobFont", this.miJobFontPos);
                editor2.putInt("customJobSize", int3);
                editor2.commit();
                ServerFontsManager.getInstance().getTypeface(this.mUserNameFont);
                this.mUserNameLayoutView.setVisibility(View.VISIBLE);
                if (!string.equals("") && string2.equals("") && string3.equals("")) {
                    final Typeface typeface = ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miNameFontPos]);
                    this.mUserNameLayoutView.setVisibility(View.VISIBLE);
                    this.mLayoutview.SetName(string, this.miNameColor, typeface);
                    this.mUserNameSettingView.setVisibility(View.GONE);
                }
                else {
                    this.mLayoutview.setNameFontAndColor(string, string2, string3, ServerFontsManager.getInstance().getTypeface(this.mUserNameFont), ServerFontsManager.getInstance().getTypeface(this.mJobNameFont), ServerFontsManager.getInstance().getTypeface(this.mCompanyNameFont), this.miNameColor, this.miJobColor, this.miCompanyColor, int1, int3, int2, this.mNameBoldCheck.isChecked(), this.mJobBoldCheck.isChecked(), this.mCompanyBoldCheck.isChecked());
                }
                this.mUserNameSettingView.setVisibility(View.GONE);
                final String s4 = (String)SettingManager.getInstance().readSetting("boardbackground", "", "-65536");
                if (SocketNoticeManager.isNum(s4)) {
                    this.mLayoutview.setBackgroundColor(Integer.parseInt(s4));
                    return;
                }
                final Bitmap decodeFile = BitmapFactory.decodeFile(s4);
                if (decodeFile != null) {
                    this.mLayoutview.setBackgroundBitmap(decodeFile);
                    return;
                }
                this.mLayoutview.setBackgroundColor(-65536);
            }
            case R.id.btn_main: {
                this.startActivity(new Intent((Context)this, (Class)mainActivity.class));
            }
            case R.id.btn_goback: {
                final Intent intent = new Intent((Context)this, (Class)WelcomeActivity.class);
                intent.setFlags(67108864);
                this.startActivity(intent);
            }
            case R.id.btn_systemnotice: {
                this.startActivity(new Intent((Context)this, (Class)NoticeActivity.class));
            }
            case 2131427511: {
                this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
                this.finish();
            }
        }
    }

    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        this.setContentView(R.layout.namesetting);
        ((RelativeLayout)this.findViewById(R.id.mainbackground)).setBackgroundDrawable((Drawable) BackgroundManager.getInstance().getBackground());
        this.mBackgroundBmpList = new ArrayList<BackgroundBitmapInfo>();
        this.mUserNameFontSelect = (Spinner)this.findViewById(R.id.name_font_select);
        this.mFontAdapter = (ArrayAdapter<CharSequence>)ArrayAdapter.createFromResource((Context)this, R.array.fonts_array, android.R.layout.simple_spinner_item);
        this.mFontRealName = this.getResources().getStringArray( R.array.fonts_array);
        this.mFontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.mUserNameFontSelect.setAdapter((SpinnerAdapter)this.mFontAdapter);
        this.mUserNameFontSelect.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                boardSetting.access$6(boardSetting.this, n);
                final TextView textView = (TextView)view;
                if (textView != null) {
                    textView.setTextSize(25.0f);
                }
                boardSetting.access$8(boardSetting.this, boardSetting.this.mFontRealName[n]);
                Log.e("Font", boardSetting.this.mUserNameFont);
            }

            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        (this.mCompanyFontSelect = (Spinner)this.findViewById(R.id.company_font_select)).setAdapter((SpinnerAdapter)this.mFontAdapter);
        this.mCompanyFontSelect.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                boardSetting.access$10(boardSetting.this, n);
                final TextView textView = (TextView)view;
                if (textView != null) {
                    textView.setTextSize(25.0f);
                }
                boardSetting.access$11(boardSetting.this, boardSetting.this.mFontRealName[n]);
            }

            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        (this.mJobFontSelect = (Spinner)this.findViewById(R.id.job_font_select)).setAdapter((SpinnerAdapter)this.mFontAdapter);
        this.mJobFontSelect.setOnItemSelectedListener((AdapterView.OnItemSelectedListener)new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                boardSetting.access$12(boardSetting.this, n);
                final TextView textView = (TextView)view;
                if (textView != null) {
                    textView.setTextSize(25.0f);
                }
                boardSetting.access$13(boardSetting.this, boardSetting.this.mFontRealName[n]);
            }

            public void onNothingSelected(final AdapterView<?> adapterView) {
            }
        });
        this.mUserNameEditText = (EditText)this.findViewById(R.id.edtext_username);
        this.mCompanyEditText = (EditText)this.findViewById(R.id.edtext_company);
        this.mJobEditText = (EditText)this.findViewById(R.id.edtext_job);
        (this.mUserNameColor = (ImageView)this.findViewById(R.id.name_color)).setOnClickListener((View.OnClickListener)this);
        (this.mCompanyColor = (ImageView)this.findViewById(R.id.company_color)).setOnClickListener((View.OnClickListener)this);
        (this.mJobColor = (ImageView)this.findViewById(R.id.job_color)).setOnClickListener((View.OnClickListener)this);
        (this.mStartUserLayoutBtn = (Button)this.findViewById(R.id.btn_pagesetting)).setOnClickListener((View.OnClickListener)this);
        this.mUserNameLayoutView = (RelativeLayout)this.findViewById(R.id.view_usernamebmp_layout);
        this.mLayoutview = (UsernameLayout)this.findViewById(R.id.username_layout);
        this.miNameColor = -256;
        this.miJobColor = -256;
        this.miCompanyColor = -256;
        this.miBackgroundColor = -65536;
        (this.mPureBackgroundColorSelectBtn = (Button)this.findViewById(R.id.btn_purecolor)).setOnClickListener((View.OnClickListener)this);
        (this.mModifyConfirmBtn = (Button)this.findViewById(R.id.btn_modify_confirm)).setOnClickListener(this);
        this.mJobSize = (EditText)this.findViewById(R.id.edtext_jobsize);
        this.mCompanySize = (EditText)this.findViewById(R.id.edtext_companysize);
        this.mNameSize = (EditText)this.findViewById(R.id.edtext_namesize);
        this.mNameBoldCheck = (CheckBox)this.findViewById(R.id.btn_namebold);
        this.mCompanyBoldCheck = (CheckBox)this.findViewById(R.id.btn_companybold);
        this.mJobBoldCheck = (CheckBox)this.findViewById(R.id.btn_jobbold);
        this.mUserNameSettingView = (RelativeLayout)this.findViewById(R.id.view_namesetting);
        (this.mBitmapBackgroundSelectBtn = (Button)this.findViewById(R.id.btn_bitmapbackground)).setOnClickListener(this);
        this.mBackgroundGallery = (Gallery)this.findViewById(R.id.gallery_bitmapbackground);
        this.mBackgroundAdpater = new BackgroundAdapter((Context)this);
        this.mBackgroundGallery.setAdapter((SpinnerAdapter)this.mBackgroundAdpater);
        this.mBackgroundGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                boardSetting.access$14(boardSetting.this, boardSetting.this.mBackgroundBmpList.get(n));
                boardSetting.access$15(boardSetting.this, n);
                ((Dialog)new AlertDialog.Builder((Context)boardSetting.this).setTitle((CharSequence)"提示").setMessage((CharSequence)"要进行什么操作？").setPositiveButton((CharSequence)"使用", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final Bitmap decodeFile = BitmapFactory.decodeFile(boardSetting.this.mCurrentSelectItem.mFileName);
                        SettingManager.getInstance().writeSetting("boardbackground", boardSetting.this.mCurrentSelectItem.mFileName);
                        if (decodeFile != null) {
                            boardSetting.this.mLayoutview.setBackgroundBitmap(decodeFile);
                        }
                        boardSetting.this.mBackgroundLinearLayout.setVisibility(8);
                        dialogInterface.cancel();
                    }
                }).setNegativeButton((CharSequence)"删除", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                        final File file = new File(boardSetting.this.mCurrentSelectItem.mFileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        boardSetting.access$14(boardSetting.this, null);
                        boardSetting.this.mBackgroundBmpList.remove(boardSetting.this.miCurrentSelectItem);
                        boardSetting.this.mBackgroundAdpater.notifyDataSetChanged();
                        dialogInterface.cancel();
                    }
                }).create()).show();
            }
        });
        this.mBackgroundLinearLayout = (RelativeLayout)this.findViewById(R.id.backgroundgallary_layout);
        ((Button)this.findViewById(R.id.btn_initPos)).setOnClickListener(this);
        this.init();
        this.mainBtn = (ImageView)this.findViewById(R.id.btn_main);
        this.returnBtn = (ImageView)this.findViewById(R.id.btn_goback);
        this.notice = (ImageButton)this.findViewById(R.id.btn_systemnotice);
        this.screensaver = (ImageButton)this.findViewById(R.id.btn_welcome);
        this.mainBtn.setOnClickListener(this);
        this.returnBtn.setOnClickListener(this);
        this.notice.setOnClickListener(this);
        this.screensaver.setOnClickListener(this);
        final Bundle extras = this.getIntent().getExtras();
        if (extras != null) {
            this.bAutoImport = true;
            final String string = extras.getString("Name");
            final String string2 = extras.getString("Company");
            final String string3 = extras.getString("Job");
            if (!string.equals("") && !string3.equals("") && !string2.equals("")) {
                this.mUserNameLayoutView.setVisibility(View.VISIBLE);
                this.mLayoutview.setNameFontAndColor(string, string3, string2, ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miNameFontPos]), ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miJobFontPos]), ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miCompanyFontPos]), this.miNameColor, this.miJobColor, this.miCompanyColor, this.miNameSize, this.miJobSize, this.miCompanySize, this.mNameBoldCheck.isChecked(), this.mJobBoldCheck.isChecked(), this.mCompanyBoldCheck.isChecked());
                this.mUserNameSettingView.setVisibility(View.GONE);
                final String s = (String)SettingManager.getInstance().readSetting("boardbackground", "", "-65536");
                if (SocketNoticeManager.isNum(s)) {
                    this.mLayoutview.setBackgroundColor(Integer.parseInt(s));
                }
                else {
                    final Bitmap decodeFile = BitmapFactory.decodeFile(s);
                    if (decodeFile != null) {
                        this.mLayoutview.setBackgroundBitmap(decodeFile);
                        return;
                    }
                    this.mLayoutview.setBackgroundColor(-65536);
                }
            }
            else if (!string.equals("") && string3.equals("") && string2.equals("")) {
                final Typeface typeface = ServerFontsManager.getInstance().getTypeface(this.mFontRealName[this.miNameFontPos]);
                this.mUserNameLayoutView.setVisibility(View.VISIBLE);
                this.mLayoutview.SetName(string, this.miNameColor, typeface);
                this.mUserNameSettingView.setVisibility(View.GONE);
                final String s2 = (String)SettingManager.getInstance().readSetting("boardbackground", "", "-65536");
                if (SocketNoticeManager.isNum(s2)) {
                    this.mLayoutview.setBackgroundColor(Integer.parseInt(s2));
                    return;
                }
                final Bitmap decodeFile2 = BitmapFactory.decodeFile(s2);
                if (decodeFile2 != null) {
                    this.mLayoutview.setBackgroundBitmap(decodeFile2);
                    return;
                }
                this.mLayoutview.setBackgroundColor(-65536);
            }
        }
    }

    protected void onDestroy() {
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        super.onDestroy();
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
        this.getWindow().getDecorView().setSystemUiVisibility(0x8 | this.getWindow().getDecorView().getSystemUiVisibility());
        SocketManager.getInstance().detach((SocketManager.Observer)this);
        super.onPause();
    }

    protected void onResume() {
        SocketManager.getInstance().attach((SocketManager.Observer)this, (Context)this);
        super.onResume();
    }

    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    private class BackgroundAdapter extends BaseAdapter
    {
        private Context mContext;

        public BackgroundAdapter(final Context mContext) {
            this.mContext = mContext;
        }

        public int getCount() {
            return boardSetting.this.mBackgroundBmpList.size();
        }

        public Object getItem(final int n) {
            return n;
        }

        public long getItemId(final int n) {
            return 0L;
        }

        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            final ImageView imageView = new ImageView(this.mContext);
            imageView.setImageBitmap(boardSetting.this.mBackgroundBmpList.get(n).mBitmap);
            imageView.setAdjustViewBounds(true);
            imageView.setLayoutParams(new Gallery.LayoutParams(-2, -2));
            return (View)imageView;
        }
    }

    private class getBackgroundBitmapThread extends Thread
    {
        @Override
        public void run() {
            super.run();
            boardSetting.access$4(true);
            for (int i = 0; i < boardSetting.this.mBackgroundBmpList.size(); ++i) {
                final Bitmap mBitmap = boardSetting.this.mBackgroundBmpList.get(i).mBitmap;
                if (mBitmap != null && !mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
            }
            boardSetting.this.mBackgroundBmpList.clear();
            final String string = String.valueOf(MeetingParam.SDCARD_ROOT) + "mpbackground/";
            final File file = new File(string);
            if (!file.exists()) {
                file.mkdir();
            }
            final String[] list = new File("/mnt/extsd/导入资料/铭牌背景/").list();
            if (list != null) {
                for (int j = 0; j < list.length; ++j) {
                    Log.e("sdcardfile", list[j]);
                    final int length = list[j].length();
                    if (list[j].subSequence(length - 4, length).equals(".png")) {
                        FileUtils.CopyFile(String.valueOf("/mnt/extsd/导入资料/铭牌背景/") + list[j], String.valueOf(string) + list[j]);
                    }
                }
            }
            final String[] list2 = new File("/mnt/usb_storage/导入资料/铭牌背景/").list();
            if (list2 != null) {
                for (int k = 0; k < list2.length; ++k) {
                    Log.e("sdcardfile", list2[k]);
                    final int length2 = list2[k].length();
                    if (list2[k].subSequence(length2 - 4, length2).equals(".png")) {
                        FileUtils.CopyFile(String.valueOf("/mnt/usb_storage/导入资料/铭牌背景/") + list2[k], String.valueOf(string) + list2[k]);
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
                            boardSetting.this.mBackgroundBmpList.add(new BackgroundBitmapInfo(decodeFile, String.valueOf(string) + list3[l]));
                        }
                    }
                }
            }
            boardSetting.this.handler.obtainMessage(3).sendToTarget();
            boardSetting.access$4(false);
        }
    }
}
