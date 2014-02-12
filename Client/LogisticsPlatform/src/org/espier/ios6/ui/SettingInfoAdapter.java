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

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.logistics.R;

public class SettingInfoAdapter extends ArrayAdapter<SettingInfo> implements
        OnItemClickListener, OnSeekBarChangeListener,
        IosLikeToggleButton.OnCheckedChangeListener {

    public static class TagInfo {
        SettingInfo info;
        TextView header;
        View bodySpring;
        SeekBar bodySeekbar;
        ImageView bodyImage;
        TextView bodyText;
        IosLikeToggleButton footerToggle;
        TextView footerText;
        TextView footerLabel;
    }

    private final LayoutInflater mInflater;
    public boolean textleft=false;
    public Drawable noticenumberImage;
    private static Drawable sDSelect;
    private static Drawable sDUnselect;
    private static Drawable sDBrightMore;
    private static Drawable sDBrightLess;
    private static Drawable sDSoundOn;
    private static Drawable sDSoundOff;
    private boolean font_colors=false;
    private boolean voices = false;
    protected IosLikeToggleButton.OnCheckedChangeListener mCheckedChangeListener;

    /**
     * OnlineMsgModel instance
     */
    // private OnlineMsgModel mMessageModel;

    private Context mContext;
    private boolean isEnabled;

    public SettingInfoAdapter(Context context, List<SettingInfo> infos) {
        super(context, 0, infos);
        isEnabled = true;

        mInflater = LayoutInflater.from(context);

        init(context);
    }

    private void init(Context context) {
        /*
         * LauncherApplication app = (LauncherApplication) ((Activity) context)
         * .getApplication(); Launcher launcher = app.getLauncher();
         * mMessageModel = launcher.getMessageModel();
         */

        mContext = context;
        setupDrawables(context);
    }

    private void setupDrawables(Context context) {
        if (sDSelect == null) {
            Resources resources = context.getResources();
            sDSelect = resources.getDrawable(R.drawable.select);
            sDUnselect = resources.getDrawable(R.drawable.unselect);
            sDBrightMore = resources.getDrawable(R.drawable.bright_more);
            sDBrightLess = resources.getDrawable(R.drawable.bright_less);
            sDSoundOn = resources.getDrawable(R.drawable.sound_on);
            sDSoundOff = resources.getDrawable(R.drawable.sound_off);

            if (sDSelect instanceof BitmapDrawable) {
                ((BitmapDrawable) sDSelect).setGravity(Gravity.CENTER);
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final SettingInfo info = getItem(position);

        if(info.getType() == SettingInfo.TYPE_CUSTOM_VIEW) {
            if (convertView == null) {
                int layoutId = mContext.getResources().getIdentifier(
                        info.getTitle(), "layout", mContext.getPackageName());
                convertView = mInflater.inflate(layoutId, null);
            }
            return convertView;
        }

        final TagInfo tag;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.setting_line, null);

            tag = new TagInfo();

            tag.header = (TextView) convertView.findViewById(R.id.header);
            // tag.header.getPaint().setFakeBoldText(true);

            tag.bodySpring = convertView.findViewById(R.id.body_spring);
            tag.bodySeekbar = (SeekBar) convertView
                    .findViewById(R.id.body_seekbar);
            tag.bodyImage = (ImageView) convertView
                    .findViewById(R.id.body_image);
            tag.bodyText = (TextView) convertView.findViewById(R.id.body_text);

            // xml textStyle: bold don't have effect to chinese.
            tag.bodyText.getPaint().setFakeBoldText(true);

            tag.footerToggle = (IosLikeToggleButton) convertView
                    .findViewById(R.id.toggle_footer);
            tag.footerText = (TextView) convertView
                    .findViewById(R.id.text_footer);
            tag.footerLabel = (TextView) convertView
                    .findViewById(R.id.label_footer);

            convertView.setTag(tag);
        } else {
            tag = (TagInfo) convertView.getTag();
        }

        final TextView header = tag.header;

        final View bodySpring = tag.bodySpring;
        final SeekBar bodySeekbar = tag.bodySeekbar;
        final ImageView bodyImage = tag.bodyImage;
        final TextView bodyText = tag.bodyText;

        final IosLikeToggleButton footerToggle = tag.footerToggle;
        final TextView footerText = tag.footerText;
        final TextView footerLabel = tag.footerLabel;

        final SettingInfo formerInfo = tag.info;

        if (info != formerInfo) {
            // hide former line member
            if (formerInfo != null) {
                switch (formerInfo.getType()) {
                case SettingInfo.TYPE_SWITCH:
                    hideHeader(header);
                    hideBody(bodySpring);
                    hideBody(bodyImage);
                    hideBody(bodyText);
                    hideFooter(footerToggle);
                    hideFooter(footerLabel);
                    break;

                case SettingInfo.TYPE_SIMPLE_ACTIVITY:
                case SettingInfo.TYPE_STATUS_ACTIVITY:
                case SettingInfo.TYPE_CHOICE:
                case SettingInfo.TYPE_WIFI_CHOICE:
                case SettingInfo.TYPE_LABEL:
                case SettingInfo.TYPE_ENTER:
                case SettingInfo.TYPE_LABLE_WITHOUT_ICON:
                case SettingInfo.TYPE_CHOICE_WITH_FOOT:

                    hideHeader(header);
                    hideBody(bodySpring);
                    hideBody(bodyImage);
                    hideBody(bodyText);
                    hideFooter(footerText);
                    hideFooter(footerLabel);
                    break;

                case SettingInfo.TYPE_VOLUME_SETTING:
                case SettingInfo.TYPE_BRIGHTNESS_SETTING:
                    hideHeader(header);
                    hideBody(bodySeekbar);
                    hideBody(bodyImage);
                    hideBody(bodyText);
                    hideFooter(footerText);
                    hideFooter(footerLabel);
                    break;

                default:
                    throw new IllegalArgumentException("Unsupport type: "
                            + formerInfo.getType());
                }
                tag.info = info;


            }

            // show current line member
            Drawable[] d = info.getFootDrawable();

            switch (info.getType()) {
            case SettingInfo.TYPE_SWITCH: {
                hideAllFoot(footerText, footerLabel, footerToggle);
                showHeader(info.getId(), header, info.getTitle(),
                        info.getIcon());
                showBody(bodySpring);
                showFooter(footerToggle, info);
                break;
            }

            case SettingInfo.TYPE_SIMPLE_ACTIVITY:
            case SettingInfo.TYPE_STATUS_ACTIVITY:
            case SettingInfo.TYPE_ENTER: {

                boolean select = false;
                Object obj = info.getValue(getContext());
                if (obj != null) {
                    select = Boolean.parseBoolean(obj.toString());
                }

                hideAllFoot(footerText, footerLabel, footerToggle);

                String ID = info.getId();
                if(ID.equals("110")||ID.equals("111")||ID.equals("112")||ID.equals("113")||ID.equals("114")||ID.equals("115"))
                {
                    showHeader(info.getId(), header, info.getTitle(),
                            select ? sDSelect : sDUnselect);
                    textleft = true;
                }
                else
                {
                    showHeader(info.getId(), header, info.getTitle(),
                            info.getIcon());
                    if(ID.equals("font_colors"))
                    {
                        font_colors = true;
                    }

                }
                showBody(bodySpring);
                showFooter(footerText, info.getFoot(), d[0], d[1], d[2], d[3]);
                break;
            }

            case SettingInfo.TYPE_LABEL: {
                hideAllFoot(footerText, footerLabel, footerToggle);
                showHeader(info.getId(), header, info.getTitle(),
                        info.getIcon());
                showBody(bodySpring);
                showFooter(footerLabel, (String) info.getValue(getContext()),
                        d[0], d[1], d[2], d[3]);
                break;
            }

             case SettingInfo.TYPE_LABLE_WITHOUT_ICON: {
                    hideAllFoot(footerText, footerLabel, footerToggle);
                    showHeader(info.getId(), header, info.getTitle(),
                            info.getIcon());
                    showBody(bodySpring);
                    footerText.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    showFooter(footerText, info.getFoot());
                    break;
                }

            case SettingInfo.TYPE_CHOICE: {
                boolean select = false;
                Object obj = info.getValue(getContext());
                if (obj != null) {
                    select = Boolean.parseBoolean(obj.toString());
                }
                showHeader(info.getId(), header, info.getTitle(),
                        select ? sDSelect : sDUnselect);

                showBody(bodySpring);
            }
                break;

            case SettingInfo.TYPE_CHOICE_WITH_FOOT: {
                boolean select = false;
                Object obj = info.getValue(getContext());
                if (obj != null) {
                    select = Boolean.parseBoolean(obj.toString());
                }
                showHeader(info.getId(), header, info.getTitle(),
                        select ? sDSelect : sDUnselect);

                showBody(bodySpring);
                footerText.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                showFooter(footerText, info.getFoot());
            }
                break;

            case SettingInfo.TYPE_WIFI_CHOICE:
                SettingInfo.WifiValueType wifiValue = (SettingInfo.WifiValueType) info
                        .getValue(getContext());
                showHeader(info.getId(), header, info.getTitle(),
                        wifiValue.isChoosed() ? sDSelect : sDUnselect);
                Log.v("end_wife_choice", info.getTitle());
                showBody(bodySpring);
                // showFooter(footerText, (String) info.getValue(getContext()),
                // null);
                break;

            case SettingInfo.TYPE_VOLUME_SETTING:
                voices = true;
                showHeader(info.getId(), header, info.getTitle(), sDSoundOff);
                Log.v("end_setting", info.getTitle());
                showBody(bodySeekbar, info);
                showFooter(footerText, null, d[0], d[1], sDSoundOn, d[3]);
                break;

            case SettingInfo.TYPE_BRIGHTNESS_SETTING:
                showHeader(info.getId(), header, null, sDBrightLess);
                Log.v("endss_", String.valueOf(null));
                showBody(bodySeekbar, info);
                showFooter(footerText, null, d[0], d[1], sDBrightMore, d[3]);
                break;

            case SettingInfo.TYPE_IMAGE_CHIOCE:
                showBody(bodyText, bodyImage, info.getTitle(), info.getIcon());
                Log.v("end_iamge_", info.getTitle());
                break;

            default:
                throw new IllegalArgumentException("Unsupport type: "
                        + info.getType());
            }
        }

        return convertView;
    }

    private void hideAllFoot(TextView text, TextView label,
            IosLikeToggleButton toggle) {
        hideFooter(text);
        hideFooter(label);
        hideFooter(toggle);
    }

    private void showHeader(String id, TextView header, CharSequence text,
            Drawable ld) {
        // get text drawable bounds
        int h = (int) (header.getTextSize() * IosLikeConstant.TEXTVIEW_LEFT_DRAWABLE_FACTOR);

//        Drawable rd = null;
        // houhh for statusbar modify.

        /*
         * if (HubActivity.HUB_ID_NOTIFICATION.equals(id)) { PushMsgManager
         * mPushMsgManager = new PushMsgManager( mContext);bottom int num=0;
         * if(mPushMsgManager!=null){
         * if(mPushMsgManager.getUnReadNote("0")!=null){
         * num=mPushMsgManager.getUnReadNote("0").size(); } } // rd =
         * ShortcutIconUtils.drawUnreademblemsDrawable(mContext, //
         * mMessageModel.getUnreadMsgNum()); rd =
         * ShortcutIconUtils.drawUnreademblemsDrawable(mContext, // num);
         */
        //
        if (id.equals("notification") && noticenumberImage != null) {
            noticenumberImage.setBounds(0, 0, 100, h);

        }
        // if (rd != null) {
        // rd.setBounds(0, 0, h, h);noticenumberImage
        // }
        else {
            noticenumberImage = null;
            if (ld != null) {
                ld.setBounds(0, 0, h, h);
            }

            // rd = ShortcutIconUtils.drawUnreademblemsDrawable(mContext,
            // mMessageModel.getUnreadMsgNum());

            if (id.equals("notification") && noticenumberImage != null) {
                noticenumberImage.setBounds(0, 0, 100, h);
                // Log.v(h, String)
            } else {
                noticenumberImage = null;
            }
        }
        if (ld != null) {
            ld.setBounds(0, 0, h, h);
        }
        header.setVisibility(View.VISIBLE);
        header.setText(text);

        if(!isEnabled){
            header.setTextColor(Color.GRAY);
            header.setClickable(true);
            header.setFocusable(true);
            header.setEnabled(true);
        }else{
            header.setTextColor(Color.BLACK);
            header.setClickable(false);
            header.setFocusable(false);
            header.setEnabled(false);
        }
        // header.setCompoundDrawablesWithIntrinsicBounds(ld, null,
        // noticenumberImage, null);

        // header.setCompoundDrawablesWithIntrinsicBounds(ld, null,
        // noticenumberImage, null);

        if(voices){
            header.setCompoundDrawablesWithIntrinsicBounds(ld, null, noticenumberImage, null);
        } else{
            header.setCompoundDrawables(ld, null, noticenumberImage, null);
        }
    }

    public void getNoticeNumber(Drawable d) {
        this.noticenumberImage = d;
    }

    private void hideHeader(TextView header) {
        header.setVisibility(View.GONE);
    }

    private void showBody(View bodySpring) {
        bodySpring.setVisibility(View.VISIBLE);
    }

    private void hideBody(View bodySpring) {
        bodySpring.setVisibility(View.GONE);
    }

    private void showBody(SeekBar bodySeekbar, SettingInfo info) {
        bodySeekbar.setTag(info);
        bodySeekbar.setVisibility(View.VISIBLE);
        bodySeekbar.setOnSeekBarChangeListener(SettingInfoAdapter.this);
        bodySeekbar.setProgress((Integer) info.getValue(getContext()));
    }

    private void hideBody(SeekBar bodySeekbar) {
        bodySeekbar.setVisibility(View.GONE);
        bodySeekbar.setOnSeekBarChangeListener(null);
        bodySeekbar.setTag(null);
    }

    private void hideBody(TextView bodyText) {
        bodyText.setVisibility(View.GONE);
    }

    private void hideBody(ImageView bodyImage) {
        bodyImage.setVisibility(View.GONE);
    }

    private void showBody(TextView bodyText, ImageView bodyImage,
            CharSequence text, Drawable drawable) {
        if (null != drawable) {
            bodyText.setVisibility(View.GONE);
            bodyImage.setVisibility(View.VISIBLE);
            bodyImage.setImageDrawable(drawable);
        } else {
            bodyImage.setVisibility(View.GONE);
            bodyText.setVisibility(View.VISIBLE);
            bodyText.setText(text);
        }
    }

       private void showFooter(TextView footerText, CharSequence text) {
            footerText.setVisibility(View.VISIBLE);
            footerText.setText(text);
       }

    private void showFooter(TextView footerText, CharSequence text, Drawable l,
            Drawable t, Drawable r, Drawable b) {
        footerText.setVisibility(View.VISIBLE);


        footerText.setText(text);
//        footerText.set

        Drawable[] drawable = footerText.getCompoundDrawables();
        if (l != null) {
            drawable[0] = l;
        }

        if (t != null) {
            drawable[1] = t;
        }

        if (r != null) {
            drawable[2] = r;
        }

        if (b != null) {
            drawable[3] = b;
        }

        if(textleft)
        {
        drawable[0].setBounds(0, 0, 60, 60);
        footerText.setCompoundDrawables(drawable[0],
                null, null,null);
        textleft=false;
        }
        else
        {

            if(font_colors)
            {
                drawable[0].setBounds(0, 0, 60, 60);
                footerText.setCompoundDrawables(drawable[0],
                        drawable[1], drawable[2], drawable[3]);
                font_colors=false;
            }
            else
            {
            footerText.setCompoundDrawablesWithIntrinsicBounds(drawable[0],
                    drawable[1], drawable[2], drawable[3]);
            }

        }
    }

    private void hideFooter(TextView footerText) {
        footerText.setVisibility(View.GONE);
    }

    private void showFooter(IosLikeToggleButton footerToggle, SettingInfo info) {
        boolean checked = false;
        Object o = info.getValue(mContext);
        if (o != null && (o instanceof Boolean)) {
            checked = Boolean.valueOf(o.toString());
        } else {
            checked = info.isToggleOn();
        }
        footerToggle.setChecked(checked);
        footerToggle.setTag(info);
        footerToggle.setVisibility(View.VISIBLE);
        footerToggle.setOnCheckedChangeListener(SettingInfoAdapter.this);
    }

    private void hideFooter(IosLikeToggleButton footerToggle) {
        footerToggle.setVisibility(View.GONE);
        footerToggle.setOnCheckedChangeListener(null);
        footerToggle.setTag(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
            long id) {
        final SettingInfo info = (SettingInfo) parent
                .getItemAtPosition(position);
        switch (info.getType()) {
        case SettingInfo.TYPE_SIMPLE_ACTIVITY:
        case SettingInfo.TYPE_STATUS_ACTIVITY:
        case SettingInfo.TYPE_LABEL:
        case SettingInfo.TYPE_ENTER:
        case SettingInfo.TYPE_LABLE_WITHOUT_ICON:
            info.onClick(getContext(), null);
            break;
        case SettingInfo.TYPE_CHOICE:
        case SettingInfo.TYPE_WIFI_CHOICE:
        case SettingInfo.TYPE_CHOICE_WITH_FOOT:
            info.onClick(getContext(), true);
            break;
        }
    }

    public void setOnCheckedChangeListener(
            IosLikeToggleButton.OnCheckedChangeListener l) {
        mCheckedChangeListener = l;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {

        if (seekBar.isPressed()) {
            final SettingInfo info = (SettingInfo) seekBar.getTag();
            info.onClick(getContext(), progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (!seekBar.isPressed()) {
            final SettingInfo info = (SettingInfo) seekBar.getTag();
            info.onClick(getContext(), seekBar.getProgress());
        }
    }

    @Override
    public void onCheckedChanged(IosLikeToggleButton buttonView,
            boolean isChecked) {
        final SettingInfo info = (SettingInfo) buttonView.getTag();
        info.onClick(getContext(), isChecked);

        if (mCheckedChangeListener != null) {
            mCheckedChangeListener.onCheckedChanged(buttonView, isChecked);
        }
    }

    public void getNoticeImages(Drawable b) {
        this.noticenumberImage = b;
    }


    // lmf add for searchpagesetting
    public static void setHeaderIcon(TextView header, boolean bool
            ) {

        Drawable mDrawable;
        // get text drawable bounds
        int h = (int) (header.getTextSize() * IosLikeConstant.TEXTVIEW_LEFT_DRAWABLE_FACTOR);
        if(bool){
            mDrawable=sDSelect;
        }else{
            mDrawable=sDUnselect;
        }
        mDrawable.setBounds(0, 0, h, h);
        header.setCompoundDrawables(mDrawable, null, null, null);
    }

    public void setItemEnabled(boolean flag){
        if (this.isEnabled != flag)
            this.isEnabled = flag;
    }

}
