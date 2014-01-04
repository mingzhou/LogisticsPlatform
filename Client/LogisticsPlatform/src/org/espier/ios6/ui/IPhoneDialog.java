/*
 * Copyright (C) 2013 FMSoft (http://www.fmsoft.cn)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.espier.ios6.ui;

import org.espier.ios6.ui.utils.AuxConfigs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.logistics.R;
/**
 * @author mingming
 */
public class IPhoneDialog extends Dialog {

    public final static int DIALOG_BACKGROUND_ALPAH = 255;
    private static int DIALOG_BTN_GAP = 10;
    @SuppressWarnings("unused")
    private final static float DIALOG_BTN_TEXT_SIZE = 20f;
    private final static float DIALOG_BTN_TEXT_SMALL = 16f;
    private final static int DIALOG_SHADOW_COLOR = 0xff000000;
    private final static int DIALOG_BTN_TEXT_MAX = 4;
    private final static int DIALOG_MIDDLE_VIEW_MIN_HEIGHT = 100;
    private final static int DIALOG_MIDDLE_VIEW_DEFAULT_HEIGHT = 120;

    public final static int BUTTON_POSITIVE_ID = 0x1001;
    public final static int BUTTON_NEUTRAL_ID = 0x1002;
    public final static int BUTTON_NEGATIVE_ID = 0x1003;
    public final static int BUTTON_EXTERN_ID = 0x1004;
    private  IosLikeToggleButton ioslikebutton=null;
    private  boolean ischeck;

    private Context mContext;
    private ControlParam mCtrlParam;
    @SuppressWarnings("unused")
    private int mTheme;

    private Button mBtnPositive;
    private Button mBtnNegative;
    private Button mBtnNeutral;
    private Button mBtnExtern;

    private TextView mMsgTextView;
    private TextView mTitleView;
    private ImageView mIconView;

    // private RelativeLayout mParentRelativeContainer;
    private LinearLayout mTopLinearLayout;
    private RelativeLayout mRelativeContainer;
    private FrameLayout mFrameLayout;

    private int mBtnCount = 0;

    private int mRelativeWidth = 0;
    private int mRelativeHeight = 0;
    private int mGap = DIALOG_BTN_GAP;
    private int mRelativePadingLeft = 0;
    private int mRelativePadingRight = 0;

    private LayoutParams mDlgLayoutParams;

    private View mDialogView;
    private int mType;
    private boolean mIgnoreBackKey = false;

    public IPhoneDialog(Context context,int type) {
        this(context, R.style.IPhoneDialog,type);
        mCtrlParam = new ControlParam(context);
        mContext = context;
        mType = type;
        // TODO Auto-generated constructor stub
    }

    public IPhoneDialog(Context context, int theme,int type) {
        super(context, theme);
        mCtrlParam = new ControlParam(context);
        mContext = context;
        mTheme = theme;
        mType = type;
    }

    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        super.onCreate(savedInstanceState);
        DIALOG_BTN_GAP = (int)mContext.
            getResources().getDimension(R.dimen.iphone_dlg_promot_btn_size);
        if (mCtrlParam.mIsJsType){
            mType = BuilderEx.DIALOG_TYPE_SHORT_BUTTON;
        }
        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
            mDialogView = getLayoutInflater().inflate(R.layout.iphone_dlg, null);
        }else{
            mDialogView = getLayoutInflater().inflate(R.layout.iphone_dlg_ex, null);
        }
        this.setContentView(mDialogView);

        mBtnCount = 0;
        this.setOnShowListener(new OnShowListener() {

            @Override
            public void onShow(DialogInterface arg0) {
                // TODO Auto-generated method stub
                doLayout();
            }

        });

        this.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface arg0) {
                // TODO Auto-generated method stub
                BuilderEx.recycle();
            }

        });

        mTitleView = (TextView) findViewById(R.id.title_view);

        mTopLinearLayout = (LinearLayout) findViewById(R.id.iphone_dlg_top_layout);

        mTopLinearLayout.getBackground().setAlpha(mCtrlParam.mBackgroundAlpha);

        if (mTitleView != null) {
            if (mCtrlParam.mTitleResId > 0)
                mTitleView.setText(mCtrlParam.mTitleResId);
            else
                mTitleView.setText(mCtrlParam.mTitleText);
            mTitleView.getPaint().setFakeBoldText(true);
        }

        mFrameLayout = (FrameLayout) findViewById(R.id.frame_container);
        if (mFrameLayout != null) {
            mFrameLayout.removeAllViews();
            if (mCtrlParam.mView != null) {
                Display dsp = this.getWindow().getWindowManager().getDefaultDisplay();

                if(mCtrlParam.mViewHeight != DIALOG_MIDDLE_VIEW_DEFAULT_HEIGHT) {
                    if (mCtrlParam.mViewHeight < DIALOG_MIDDLE_VIEW_MIN_HEIGHT){
                        //mCtrlParam.mViewHeight = DIALOG_MIDDLE_VIEW_MIN_HEIGHT;
                    }else if (mCtrlParam.mViewHeight >= (int) (dsp.getHeight() * 0.5f))
                        mCtrlParam.mViewHeight = (int) (dsp.getHeight() * 0.5f);
                }else{
                     mCtrlParam.mViewHeight = dip2Pixel(mContext, mCtrlParam.mViewHeight);
                }

                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, mCtrlParam.mViewHeight);
                mFrameLayout.addView(mCtrlParam.mView, params);
            } else {
                LinearLayout v = null;
                if (mCtrlParam.mIsJsType)
                    v = (LinearLayout) View.inflate(getContext(),
                            R.layout.iphone_dlg_middle_noicon_view, null);
                else
                    v = (LinearLayout) View.inflate(getContext(),
                            R.layout.iphone_dlg_middle_view, null);

                mMsgTextView = (TextView) v.findViewById(R.id.content_txt_view);
                if (mMsgTextView != null) {
                    if (mCtrlParam.mMsgTextResId > 0)
                        mMsgTextView.setText(mCtrlParam.mMsgTextResId);
                    else
                        mMsgTextView.setText(mCtrlParam.mMsgText);

                    Display dsp = this.getWindow().getWindowManager().getDefaultDisplay();
                    if(mCtrlParam.mMsgHeight != 0) {
                        if (mCtrlParam.mMsgHeight < DIALOG_MIDDLE_VIEW_MIN_HEIGHT){
                            //mCtrlParam.mMsgHeight = DIALOG_MIDDLE_VIEW_MIN_HEIGHT;
                        }else if (mCtrlParam.mMsgHeight >= (int) (dsp.getHeight() * 0.8f))
                            mCtrlParam.mMsgHeight = (int) (dsp.getHeight() * 0.8f);

                        if (mCtrlParam.mMsgText.length() <= 0){
                            mMsgTextView.setVisibility(View.INVISIBLE);
                        }
                        mMsgTextView.setHeight(mCtrlParam.mMsgHeight);
                    }

                    mMsgTextView.setGravity(mCtrlParam.mMsgGravity);
                }

                mIconView = (ImageView) v.findViewById(R.id.custom_icon);
                if (mIconView != null) {
                    if (mCtrlParam.mIconResId > 0)
                        mIconView.setImageResource(mCtrlParam.mIconResId);
                    if (!mCtrlParam.mCanCreateIcon)
                        v.removeView(mIconView);
                }
                mFrameLayout.addView(v);
            }

        }

        mRelativeContainer = (RelativeLayout) findViewById(R.id.button_layout);
        if (mCtrlParam.mCanCreatePositive) {
            mBtnPositive = new Button(getContext());
            if (mBtnPositive != null) {

                mBtnPositive.setId(BUTTON_POSITIVE_ID);
                mBtnPositive.setText(mCtrlParam.mBtnPositiveTextResId);
                mBtnPositive.setTextColor(mCtrlParam.mBtnPositiveTextColor);
                mBtnPositive.setTextSize(mCtrlParam.mBtnTextSize);
                mBtnPositive.setShadowLayer(1f, 1f, 1f, DIALOG_SHADOW_COLOR);
                mBtnPositive.setBackgroundResource(mCtrlParam.mPositiveBkResId);
                mBtnPositive.getBackground().setAlpha(
                        mCtrlParam.mBackgroundAlpha);

                mBtnPositive.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (mCtrlParam.mBtnPositiveListener != null)
                            mCtrlParam.mBtnPositiveListener.onClick(v);
                        dismiss();
                    }

                });

                mRelativeContainer.addView(mBtnPositive);
                mBtnCount++;
            }
        }

        if (!mCtrlParam.mIsJsType) {
            if (mCtrlParam.mCanCreateNeutral) {
                mBtnNeutral = new Button(getContext());
                if (mBtnNeutral != null) {

                    mBtnNeutral.setId(BUTTON_NEUTRAL_ID);
                    mBtnNeutral.setText(mCtrlParam.mBtnNeutralTextResId);
                    mBtnNeutral.setTextColor(mCtrlParam.mBtnNeutralTextColor);
                    mBtnNeutral.setTextSize(mCtrlParam.mBtnTextSize);
                    mBtnNeutral
                            .setBackgroundResource(mCtrlParam.mNeutralBkResId);
                    mBtnNeutral.setShadowLayer(1, 1, 1, DIALOG_SHADOW_COLOR);
                    mBtnNeutral.getBackground().setAlpha(
                            mCtrlParam.mBackgroundAlpha);
                    mBtnNeutral
                            .setOnClickListener(new Button.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    // TODO Auto-generated method stub
                                    mCtrlParam.mBtnNeutralListener.onClick(v);
                                    dismiss();
                                }

                            });

                    mRelativeContainer.addView(mBtnNeutral);
                    mBtnCount++;

                }
            }
        }

        if (mCtrlParam.mCanCreateNegative) {
            mBtnNegative = new Button(getContext());
            if (mBtnNegative != null) {

                mBtnNegative.setId(BUTTON_NEGATIVE_ID);
                mBtnNegative.setText(mCtrlParam.mBtnNegativeTextResId);
                mBtnNegative.setTextColor(mCtrlParam.mBtnNegativeTextColor);
                mBtnNegative.setTextSize(mCtrlParam.mBtnTextSize);
                mBtnNegative.setShadowLayer(1, 1, 1, DIALOG_SHADOW_COLOR);
                mBtnNegative.setBackgroundResource(mCtrlParam.mNegativeBkResId);
                mBtnNegative.getBackground().setAlpha(
                        mCtrlParam.mBackgroundAlpha);
                mBtnNegative.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mCtrlParam.mBtnNegativeListenter.onClick(v);
                        dismiss();
                    }

                });

                mRelativeContainer.addView(mBtnNegative);
                mBtnCount++;
            }
        }

        if (mCtrlParam.mCanCreateExtern) {
            mBtnExtern = new Button(getContext());
            if (mBtnExtern != null) {

                mBtnExtern.setId(BUTTON_EXTERN_ID);
                mBtnExtern.setText(mCtrlParam.mBtnExternTextResId);
                mBtnExtern.setTextColor(mCtrlParam.mBtnExternTextColor);
                mBtnExtern.setTextSize(mCtrlParam.mBtnTextSize);
                mBtnExtern.setShadowLayer(1, 1, 1, DIALOG_SHADOW_COLOR);
                mBtnExtern.setBackgroundResource(mCtrlParam.mExternBkResId);
                mBtnExtern.getBackground().setAlpha(
                        mCtrlParam.mBackgroundAlpha);
                mBtnExtern.setOnClickListener(new Button.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        mCtrlParam.mBtnExternListener.onClick(v);
                        dismiss();
                    }

                });

                mRelativeContainer.addView(mBtnExtern);
                mBtnCount++;
            }
        }
    }

    private int dip2Pixel(Context context, int dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }


    private int getMaxTextBtnId() {
        int id = 0;
        int maxlen = 0;

        if(mBtnPositive != null) {
            id = mBtnPositive.getId();
            maxlen = mBtnPositive.getText().length();
        }

        if(mBtnNeutral != null && mBtnNeutral.getText().length() > maxlen)
            id = mBtnNeutral.getId();

        if(mBtnNegative != null && mBtnNegative.getText().length() > maxlen)
            id = mBtnNegative.getId();

        if(mBtnExtern != null && mBtnExtern.getText().length() > maxlen)
            id = mBtnExtern.getId();

        return id;
    }

    @SuppressWarnings("deprecation")
	public void doLayout() {

        int lrPadding = 0;
        Display dsp = this.getWindow().getWindowManager().getDefaultDisplay();
        int dsp_w = dsp.getWidth();
        int dsp_h = dsp.getHeight();
        mDlgLayoutParams = this.getWindow().getAttributes();
        if (dsp_w > dsp_h) {
            mDlgLayoutParams.width = (int) (dsp.getWidth() * 0.9f);
        } else {
            mDlgLayoutParams.width = (int) (dsp.getWidth() * 0.95f);
        }
        AuxConfigs.getInstance(mContext);
        if (AuxConfigs.isPad){
            mDlgLayoutParams.width = (int)(mTitleView.getTextSize() * 15);
            if (mDlgLayoutParams.width > (dsp_w < dsp_h ? dsp_w : dsp_h) * 0.8f){
                mDlgLayoutParams.width = (int)((dsp_w < dsp_h ? dsp_w : dsp_h) * 0.8f);
            }
        }
        this.getWindow().setAttributes(mDlgLayoutParams);

        mRelativeWidth = this.getWindow().getAttributes().width;
        mRelativeHeight = mRelativeContainer.getHeight();
        mRelativePadingLeft = mRelativeContainer.getPaddingLeft();
        mRelativePadingRight = mRelativeContainer.getPaddingRight();

        lrPadding = 4 * dip2Pixel(mContext, 10);

        if (mRelativeWidth > 0 && mRelativeHeight > 0 && mBtnCount > 0) {
            if (mBtnCount > 1)
                mGap = DIALOG_BTN_GAP;
            else
                mGap = 0;

            int itemWidth;
            if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                itemWidth = (mRelativeWidth - lrPadding - (mBtnCount - 1) * mGap - (mRelativePadingLeft + mRelativePadingRight)) / mBtnCount;
            }else{
                itemWidth = mRelativeWidth;
                mGap = mRelativeHeight/5;
            }
            int max_txt_id = getMaxTextBtnId();

            //updateButtonTextSize();

            if (mCtrlParam.mIsJsType) {
                if (mCtrlParam.mCanCreateNegative) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    params.rightMargin = mGap;

                    if(mBtnNegative.getId() != max_txt_id && max_txt_id > 0) {
                        params.addRule(RelativeLayout.ALIGN_TOP, max_txt_id);
                        params.addRule(RelativeLayout.ALIGN_BOTTOM, max_txt_id);
                    }

                    mBtnNegative.setLayoutParams(params);
                }

                if (mCtrlParam.mCanCreatePositive) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if (mBtnNegative != null) {
                        params.addRule(RelativeLayout.RIGHT_OF, mBtnNegative
                                .getId());
                    }

                    if(mBtnPositive.getId() != max_txt_id && max_txt_id > 0) {
                        params.addRule(RelativeLayout.ALIGN_TOP, max_txt_id);
                        params.addRule(RelativeLayout.ALIGN_BOTTOM, max_txt_id);
                    }

                    mBtnPositive.setLayoutParams(params);
                }
            } else {
                if (mCtrlParam.mCanCreatePositive) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                        params.rightMargin = mGap;
                    }

                    if(mBtnPositive.getId() != max_txt_id && max_txt_id > 0) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.ALIGN_TOP, max_txt_id);
                            params.addRule(RelativeLayout.ALIGN_BOTTOM, max_txt_id);
                        }
                    }

                    mBtnPositive.setLayoutParams(params);
                }

                if (mCtrlParam.mCanCreateNeutral) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if (mBtnPositive != null) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.RIGHT_OF, mBtnPositive
                                .getId());
                        }else{
                            params.addRule(RelativeLayout.BELOW, mBtnPositive
                                    .getId());
                            params.topMargin = mGap;
                        }
                    }
                    if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                        params.rightMargin = mGap;
                    }

                    if(mBtnNeutral.getId() != max_txt_id && max_txt_id > 0) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.ALIGN_TOP, max_txt_id);
                            params.addRule(RelativeLayout.ALIGN_BOTTOM, max_txt_id);
                        }
                    }

                    mBtnNeutral.setLayoutParams(params);
                }

                if (mCtrlParam.mCanCreateNegative) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if (mBtnPositive != null && mBtnNeutral == null) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.RIGHT_OF, mBtnPositive.getId());
                        }else{
                            params.addRule(RelativeLayout.BELOW, mBtnPositive.getId());
                            params.topMargin = mGap;
                        }
                    } else if (mBtnNeutral != null) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.RIGHT_OF, mBtnNeutral.getId());
                        }else{
                            params.addRule(RelativeLayout.BELOW, mBtnNeutral.getId());
                            params.topMargin = mGap;
                        }
                    }

                    if(mBtnNegative.getId() != max_txt_id && max_txt_id > 0) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.ALIGN_TOP, max_txt_id);
                            params.addRule(RelativeLayout.ALIGN_BOTTOM, max_txt_id);
                        }
                    }

                    mBtnNegative.setLayoutParams(params);
                }

                if (mCtrlParam.mCanCreateExtern) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                            itemWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    if (mBtnPositive != null && mBtnNeutral == null && mBtnNegative == null) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.RIGHT_OF, mBtnPositive.getId());
                        }else{
                            params.addRule(RelativeLayout.BELOW, mBtnPositive.getId());
                            params.topMargin = mGap;
                        }
                    } else if (mBtnNeutral != null && mBtnNegative == null) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.RIGHT_OF, mBtnNeutral.getId());
                        }else{
                            params.addRule(RelativeLayout.BELOW, mBtnNeutral.getId());
                            params.topMargin = mGap;
                        }
                    }else if (mBtnNegative != null) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.RIGHT_OF, mBtnNegative.getId());
                        }else{
                            params.addRule(RelativeLayout.BELOW, mBtnNegative.getId());
                            params.topMargin = mGap;
                        }
                    }

                    if(mBtnExtern.getId() != max_txt_id && max_txt_id > 0) {
                        if (mType == BuilderEx.DIALOG_TYPE_SHORT_BUTTON){
                            params.addRule(RelativeLayout.ALIGN_TOP, max_txt_id);
                            params.addRule(RelativeLayout.ALIGN_BOTTOM, max_txt_id);
                        }
                    }

                    mBtnExtern.setLayoutParams(params);
                }

            }
        }

    }

    @SuppressWarnings("unused")
    private void updateButtonTextSize() {
        if(mBtnCount > 2 && ((mBtnPositive != null && mBtnPositive.getText().length() >= DIALOG_BTN_TEXT_MAX)
           || (mBtnNeutral != null && mBtnNeutral.getText().length() >= DIALOG_BTN_TEXT_MAX)
           || (mBtnNegative != null && mBtnNegative.getText().length() >= DIALOG_BTN_TEXT_MAX))) {

            if(mCtrlParam.mCanCreatePositive)
                mBtnPositive.setTextSize(DIALOG_BTN_TEXT_SMALL);
            if(mCtrlParam.mCanCreateNeutral)
                mBtnNeutral.setTextSize(DIALOG_BTN_TEXT_SMALL);
            if(mCtrlParam.mCanCreateNegative)
                mBtnNegative.setTextSize(DIALOG_BTN_TEXT_SMALL);
        } else {
            if(mCtrlParam.mCanCreatePositive)
                mBtnPositive.setTextSize(mCtrlParam.mBtnTextSize);
            if(mCtrlParam.mCanCreateNeutral)
                mBtnNeutral.setTextSize(mCtrlParam.mBtnTextSize);
            if(mCtrlParam.mCanCreateNegative)
                mBtnNegative.setTextSize(mCtrlParam.mBtnTextSize);
        }
    }

    public void setView(View v) {
        mCtrlParam.mView = v;
    }

    public void setViewHeight(int height) {
        mCtrlParam.mViewHeight = height;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //if(mCtrlParam != null)
            //    mCtrlParam.mBtnNegativeListenter.onClick(null);
            if(!mIgnoreBackKey){
                if(ioslikebutton!=null)
                {
                    ioslikebutton.setChecked(!ischeck);
                    ioslikebutton.invalidate();
//                    ioslikebutton=null;
                }
                this.dismiss();
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    public void setIgnoreBackKey(boolean b){
        mIgnoreBackKey = b;
    }

    // Positive
    public void setPositiveButton(int resTextId, Button.OnClickListener listener) {
        mCtrlParam.mBtnPositiveTextResId = resTextId;
        mCtrlParam.mBtnPositiveListener = listener;
        setCanCreatePositiveButton(true);
    }

    public void setCanCreatePositiveButton(boolean canCreate) {
        mCtrlParam.mCanCreatePositive = canCreate;
    }

    public void setPositiveBackground(int resId) {
        mCtrlParam.mPositiveBkResId = resId;
        mCtrlParam.mCanCreatePositive = true;
    }

    public void setPositiveTextColor(int color) {
        mCtrlParam.mBtnPositiveTextColor = color;
    }

    // Negative
    public void setNegativeButton(int resTextId, Button.OnClickListener listener) {
        mCtrlParam.mBtnNegativeTextResId = resTextId;
        mCtrlParam.mBtnNegativeListenter = listener;
        setCanCreateNegativeButton(true);

    }

    public void setCanCreateNegativeButton(boolean canCreate) {
        mCtrlParam.mCanCreateNegative = canCreate;
    }

    public void setNegativeTextColor(int color) {
        mCtrlParam.mBtnNegativeTextColor = color;

    }

    public void setNegativeBackgound(int resId) {
        mCtrlParam.mNegativeBkResId = resId;
    }

    // Negative
    public void setNeutralButton(int resTextId, Button.OnClickListener listener) {

        mCtrlParam.mBtnNeutralListener = listener;
        mCtrlParam.mBtnNeutralTextResId = resTextId;
        setCanCreateNeutralButton(true);
    }

    public void setNeutralBackground(int resId) {
        mCtrlParam.mNeutralBkResId = resId;
    }

    public void setCanCreateNeutralButton(boolean canCreate) {
        mCtrlParam.mCanCreateNeutral = canCreate;
    }

    public void setNeutralTextColor(int color) {
        mCtrlParam.mBtnNeutralTextColor = color;
    }

    // Extern
    public void setExternButton(int resTextId, Button.OnClickListener listener) {

        mCtrlParam.mBtnExternListener = listener;
        mCtrlParam.mBtnExternTextResId = resTextId;
        setCanCreateExternButton(true);
    }

    public void setExternBackground(int resId) {
        mCtrlParam.mExternBkResId = resId;
    }

    public void setCanCreateExternButton(boolean canCreate) {
        mCtrlParam.mCanCreateExtern = canCreate;
    }

    public void setExternTextColor(int color) {
        mCtrlParam.mBtnExternTextColor = color;
    }

    // Message
    public void setMessage(int resId) {
        mCtrlParam.mMsgTextResId = resId;

    }

    public void setMessage(String msg) {
        mCtrlParam.mMsgText = msg;
    }

    public void setMessageGravity(int gravity) {
        mCtrlParam.mMsgGravity = gravity;
    }

    public void setMessageHeight(int height) {
        mCtrlParam.mMsgHeight = height;
    }

    // Title
    @Override
	public void setTitle(int resId) {
        mCtrlParam.mTitleResId = resId;
    }

    public void setTitle(String title) {
        mCtrlParam.mTitleText = title;
    }

    // Icon
    public void setShowIcon(boolean show) {
        mCtrlParam.mCanCreateIcon = show;
    }

    public void setIcon(int resId) {
        mCtrlParam.mIconResId = resId;
    }

    public void setBackgroundAlpha(int alpha) {
        if (alpha > 255)
            mCtrlParam.mBackgroundAlpha = 255;
        else if (alpha < 0)
            mCtrlParam.mBackgroundAlpha = 0;
        else
            mCtrlParam.mBackgroundAlpha = alpha;
    }

    public void setDialogBtnTextSize(final float textSize) {
        mCtrlParam.mBtnTextSize = textSize;
    }

    public void setIsJsType(boolean isJsType) {
        mCtrlParam.mIsJsType = isJsType;
    }

    public static class ControlParam {

        @SuppressWarnings("unused")
        private Context mContext;
        private Dialog mDialog;

        public View mView = null;
        public int mViewHeight = DIALOG_MIDDLE_VIEW_DEFAULT_HEIGHT;
        public int mBtnPositiveTextResId = R.string.iphone_dlg_btn_ok_str;
        public int mPositiveBkResId = R.drawable.iphone_dlg_btn_positive;
        public int mBtnPositiveTextColor = Color.WHITE;

        public int mBtnNegativeTextResId = R.string.iphone_dlg_btn_cancel_str;
        public int mNegativeBkResId = R.drawable.iphone_dlg_btn_negative;
        public int mBtnNegativeTextColor = Color.WHITE;

        public int mBtnNeutralTextResId = 0;
        public int mNeutralBkResId = R.drawable.iphone_dlg_btn_neutral;
        public int mBtnNeutralTextColor = Color.WHITE;

        public int mBtnExternTextResId = 0;
        public int mExternBkResId = R.drawable.iphone_dlg_btn_neutral;
        public int mBtnExternTextColor = Color.WHITE;

        public int mBackgroundAlpha = DIALOG_BACKGROUND_ALPAH;
        public float mBtnTextSize = DIALOG_BTN_TEXT_SMALL;

        public int mMsgTextResId = 0;
        public int mTitleResId = 0;
        public int mIconResId = 0;

        public int mMsgGravity = Gravity.LEFT;
        public int mMsgHeight = 0;

        public String mMsgText = "";
        public String mTitleText = "";

        public boolean mCanCreatePositive = false;
        public boolean mCanCreateNeutral = false;
        public boolean mCanCreateNegative = false;
        public boolean mCanCreateExtern = false;
        public boolean mCanCreateIcon = true;

        public boolean mIsJsType = false;

        public Button.OnClickListener mBtnPositiveListener = new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDialog != null)
                    mDialog.dismiss();
            }

        };

        public Button.OnClickListener mBtnNegativeListenter = new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDialog != null)
                    mDialog.dismiss();
            }

        };

        public Button.OnClickListener mBtnNeutralListener = new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDialog != null)
                    mDialog.dismiss();
            }

        };

        public Button.OnClickListener mBtnExternListener = new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (mDialog != null)
                    mDialog.dismiss();
            }

        };

        public OnCancelListener mOnCancelListener = null;

        public boolean mCancelable = false;

        public ControlParam(Context context) {
            mContext = context;
        }

        public void setDialog(Dialog dlg) {
            mDialog = dlg;
        }

        public void apply() {
            IPhoneDialog d = (IPhoneDialog) mDialog;
            d.setPositiveButton(mBtnPositiveTextResId, mBtnPositiveListener);
            d.setPositiveBackground(mPositiveBkResId);
            d.setPositiveTextColor(mBtnPositiveTextColor);
            d.setCanCreatePositiveButton(mCanCreatePositive);

            d.setNegativeButton(mBtnNegativeTextResId, mBtnNegativeListenter);
            d.setNegativeBackgound(mNegativeBkResId);
            d.setNegativeTextColor(mBtnNegativeTextColor);
            d.setCanCreateNegativeButton(mCanCreateNegative);

            d.setNeutralButton(mBtnNeutralTextResId, mBtnNeutralListener);
            d.setNeutralBackground(mNeutralBkResId);
            d.setNeutralTextColor(mBtnNeutralTextColor);
            d.setCanCreateNeutralButton(mCanCreateNeutral);

            d.setExternButton(mBtnExternTextResId, mBtnExternListener);
            d.setExternBackground(mExternBkResId);
            d.setExternTextColor(mBtnExternTextColor);
            d.setCanCreateExternButton(mCanCreateExtern);

            d.setMessage(mMsgTextResId);
            d.setMessage(mMsgText);
            d.setMessageGravity(mMsgGravity);
            d.setMessageHeight(mMsgHeight);
            d.setTitle(mTitleResId);
            d.setTitle(mTitleText);
            d.setShowIcon(mCanCreateIcon);
            d.setIcon(mIconResId);
            d.setOnCancelListener(mOnCancelListener);
            d.setCancelable(mCancelable);
            d.setView(mView);
            d.setViewHeight(mViewHeight);
            d.setBackgroundAlpha(mBackgroundAlpha);
            d.setDialogBtnTextSize(mBtnTextSize);
            d.setIsJsType(mIsJsType);
        }
    }

    public static class BuilderEx {
        private ControlParam mControlParam;
        private IPhoneDialog mAlertDialogEx;
        private Context mContext;
        private int mTheme;
        private int mType;


        public static final int DIALOG_TYPE_SHORT_BUTTON = 0x1;
        public static final int DIALOG_TYPE_LONG_BUTTON = 0x2;

        private static IPhoneDialog mCurDialog = null;
        private static IPhoneDialog mCurDialogTemp = null;

        private static final float ANIMATION_SCALE_LARGE = 1.05f;
        //Animation will keep a mListener ref to outside so it can not be static
        private final ScaleAnimation mStartScaleAnimation = new ScaleAnimation(
                0.3f,ANIMATION_SCALE_LARGE, 0.3f,ANIMATION_SCALE_LARGE,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        private final ScaleAnimation mSecondScaleAnimation = new ScaleAnimation(
                ANIMATION_SCALE_LARGE,1.0f,ANIMATION_SCALE_LARGE,1.0f,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        private static final int ANIMATION_DURATION = 200;

        public BuilderEx(Context context) {
            this(context, R.style.IPhoneDialog,DIALOG_TYPE_SHORT_BUTTON);
        }

        public BuilderEx(Context context,int type) {
            this(context, R.style.IPhoneDialog,type);
        }

        public BuilderEx(Context context, int theme,int type) {
            mContext = context;
            mTheme = theme;
            mControlParam = new ControlParam(mContext);
            if (type != DIALOG_TYPE_LONG_BUTTON){
                type = DIALOG_TYPE_SHORT_BUTTON;
            }
            mType = type;
        }


//        public boolean onKeyUp(int keyCode, KeyEvent event) {
//            // TODO Auto-generated method stub
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                //if(mCtrlParam != null)
//                //    mCtrlParam.mBtnNegativeListenter.onClick(null);
////                if(!mIgnoreBackKey){
////                    this.dismiss();
////                }
//            }
//            return super.onKeyUp(keyCode, event);
//        }
        private static void recycle() {
            if (mCurDialogTemp == mCurDialog){
                mCurDialog = null;
            }
            mCurDialogTemp = (mCurDialogTemp != mCurDialog) ? mCurDialog:null;
            //System.gc();
        }

        public IPhoneDialog create() {
            mAlertDialogEx = new IPhoneDialog(mContext, mTheme,mType);
            if (mCurDialogTemp == null){
                mCurDialogTemp = mAlertDialogEx;
            }

            mCurDialog = mAlertDialogEx;
            if (mAlertDialogEx != null) {

                if (mControlParam != null) {
                    mControlParam.setDialog(mAlertDialogEx);
                    mControlParam.apply();
                }

                return mAlertDialogEx;
            }
            return null;
        }

        public IPhoneDialog show() {
            // if (mAlertDialogEx == null)
            create();
            mAlertDialogEx.show();
            startIphoneEffectAnimation();

            return mAlertDialogEx;

        }

        public IPhoneDialog ipadshows(IosLikeToggleButton liketooglebutton ,boolean ischeck) {
            // if (mAlertDialogEx == null)
            create();
            mAlertDialogEx.show();
            mAlertDialogEx.ischeck=ischeck;
            mAlertDialogEx.ioslikebutton=liketooglebutton;
            startIphoneEffectAnimation();

            return mAlertDialogEx;

        }
        private void startIphoneEffectAnimation() {
            mStartScaleAnimation.reset();
            mStartScaleAnimation.setDuration(ANIMATION_DURATION);
            mStartScaleAnimation.setFillAfter(true);
            AnimationListener al = new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mSecondScaleAnimation.reset();
                    mSecondScaleAnimation.setDuration(ANIMATION_DURATION);
                    mSecondScaleAnimation.setFillAfter(true);
                    mAlertDialogEx.mDialogView
                            .startAnimation(mSecondScaleAnimation);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            };
            mStartScaleAnimation.setAnimationListener(al);
            mAlertDialogEx.mDialogView.startAnimation(mStartScaleAnimation);
        }

        // Positive
        public BuilderEx setPositiveButton(int resTextId,
                Button.OnClickListener listener) {
            if (listener != null)
                mControlParam.mBtnPositiveListener = listener;
            mControlParam.mBtnPositiveTextResId = resTextId;
            mControlParam.mCanCreatePositive = true;
            return this;
        }

        public BuilderEx setPositiveBackground(int resId) {
            mControlParam.mPositiveBkResId = resId;
            mControlParam.mCanCreatePositive = true;
            return this;
        }

        public BuilderEx setPositiveTextColor(int color) {
            mControlParam.mBtnPositiveTextColor = color;
            return this;
        }

        // Negative
        public BuilderEx setNegativeButton(int resTextId,
                Button.OnClickListener listener) {
            if (listener != null)
                mControlParam.mBtnNegativeListenter = listener;

            mControlParam.mBtnNegativeTextResId = resTextId;
            mControlParam.mCanCreateNegative = true;
            return this;
        }

        public BuilderEx setNegativeTextColor(int color) {
            mControlParam.mBtnNegativeTextColor = color;
            return this;
        }

        public BuilderEx setNegativeBackgound(int resId) {
            mControlParam.mNegativeBkResId = resId;
            mControlParam.mCanCreateNegative = true;
            return this;
        }

        public BuilderEx setNeutralButton(int resTextId,
                Button.OnClickListener listener) {

            if (listener != null)
                mControlParam.mBtnNeutralListener = listener;

            mControlParam.mBtnNeutralTextResId = resTextId;
            mControlParam.mCanCreateNeutral = true;
            return this;
        }

        public BuilderEx setNeutralBackground(int resId) {
            mControlParam.mNeutralBkResId = resId;
            mControlParam.mCanCreateNeutral = true;
            return this;
        }

        // Extern
        public BuilderEx setExternButton(int resTextId,
                Button.OnClickListener listener) {
            if (listener != null)
                mControlParam.mBtnExternListener = listener;
            mControlParam.mBtnExternTextResId = resTextId;
            mControlParam.mCanCreateExtern = true;
            return this;
        }

        public BuilderEx setExternBackground(int resId) {
            mControlParam.mExternBkResId = resId;
            mControlParam.mCanCreateExtern = true;
            return this;
        }

        public BuilderEx setExternTextColor(int color) {
            mControlParam.mBtnExternTextColor = color;
            return this;
        }

        // Message
        public BuilderEx setMessage(int resId) {
            mControlParam.mMsgTextResId = resId;
            return this;
        }

        public BuilderEx setMessage(String msg) {
            mControlParam.mMsgText = msg;
            return this;
        }

        public BuilderEx setMessageGravity(int gravity) {
            mControlParam.mMsgGravity = gravity;
            return this;
        }

        public BuilderEx setMessageHeight(int height) {
            mControlParam.mMsgHeight = height;
            return this;
        }

        // Title
        public BuilderEx setTitle(int resId) {
            mControlParam.mTitleResId = resId;
            return this;
        }

        public BuilderEx setTitle(String title) {
            mControlParam.mTitleText = title;
            return this;
        }

        // Icon
        public BuilderEx setShowIcon(boolean show) {
            mControlParam.mCanCreateIcon = show;
            return this;
        }

        public BuilderEx setIcon(int resId) {
            mControlParam.mIconResId = resId;
            return this;
        }

        public BuilderEx setView(View v) {
            mControlParam.mView = v;
            return this;
        }

        public BuilderEx setViewHeight(int height) {
            mControlParam.mViewHeight = height;
            return this;
        }

        public BuilderEx setOnCancelListener(OnCancelListener listener) {
            mControlParam.mOnCancelListener = listener;
            return this;
        }

        public BuilderEx setCancelable(boolean cancelable) {
            mControlParam.mCancelable = cancelable;
            return this;
        }

        public BuilderEx setBackgroudAlpha(int alpha) {
            mControlParam.mBackgroundAlpha = alpha;
            return this;
        }

        public BuilderEx setEnableJsType(boolean isJsType) {
            mControlParam.mIsJsType = isJsType;
            return this;
        }

        public BuilderEx setButtonTextSize(final float textSize) {
            mControlParam.mBtnTextSize = textSize;
            return this;
        }


        public static Dialog getCurrentDialog() {
            return mCurDialog;
        }
//        OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
//                {
//                 return true;
//                }
//                else
//                {
//                 return false;
//                }
//            }
//        } ;

    }
}
