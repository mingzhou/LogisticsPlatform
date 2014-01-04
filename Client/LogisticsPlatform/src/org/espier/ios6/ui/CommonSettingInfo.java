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

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class CommonSettingInfo implements SettingInfo {

    protected String mId;
    protected int mType;
    protected Drawable mIcon;
    protected String mTitle;
    public String mDesc;

    protected Object mValue;
    protected SettingCallback mSettingCb;

    /**
     * Foot info
     */
    protected String mFoot;
    protected Drawable[] mFootDrawable = new Drawable[4];
    protected boolean mToggle;

    public interface SettingCallback {
        void onClick(Context context, CommonSettingInfo info, Object obj);

        void updateStatus(Context context, CommonSettingInfo info, Object obj);

        Object getValue(Context context, CommonSettingInfo info);
    }

    public CommonSettingInfo(String id, int type, Drawable icon, String title) {
        mId = id;
        mType = type;
        mIcon = icon;
        mTitle = title;
    }

    public CommonSettingInfo(String id, int type, Drawable icon, String title,
            SettingCallback cb) {
        mId = id;
        mType = type;
        mIcon = icon;
        mTitle = title;
        mSettingCb = cb;
    }

    public CommonSettingInfo(String id, int type, Drawable icon, String title,
            String desc, SettingCallback cb) {
        mId = id;
        mType = type;
        mIcon = icon;
        mTitle = title;
        mDesc = desc;
        mSettingCb = cb;
    }

    public String getDesc() {
        return mDesc;
    }

    @Override
    public Drawable getIcon() {
        return mIcon;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getType() {
        return mType;
    }

    @Override
    public String getId() {
        return mId;
    }

    @Override
    public Object getValue(Context context) {
        if (mSettingCb != null) {
            return mSettingCb.getValue(context, this);
        }

        if (mDesc != null) {
            return mDesc;
        }

        return null;
    }

    public String getFoot() {
        return mFoot;
    }

    public void setOnClickCb(SettingCallback cb) {
        mSettingCb = cb;
    }

    public SettingCallback getOnClickCb() {
        return mSettingCb;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setFoot(String foot) {
        mFoot = foot;
    }

    public void setToggle(boolean b) {
        mToggle = b;
    }

    /**
     * 获取开关的当前状态。
     * 默认首先获取自定义回调的处理结果，如果没有注册回调函数，则获取当前的存储结果
     */
    public boolean isToggleOn() {
        return mToggle;
    }

    public void setFootDrawable(Drawable l, Drawable t, Drawable r, Drawable b) {
        mFootDrawable[0] = l;
        mFootDrawable[1] = t;
        mFootDrawable[2] = r;
        mFootDrawable[3] = b;
    }

    public Drawable[] getFootDrawable() {
        return mFootDrawable;
    }

    @Override
    public void onClick(Context context, Object obj) {
        Log.d("CommonSettingInfo", "onClick");

        if (mSettingCb != null) {
            mSettingCb.onClick(context, this, obj);
        }

        if(obj instanceof Boolean){
            setToggle((Boolean)obj);
        }
    }

    @Override
    public void updateStatus(Context context, Object obj) {
        Log.d("CommonSettingInfo", "updateStatus");

        if (mSettingCb != null) {
            mSettingCb.updateStatus(context, this, obj);
        }
    }

}
