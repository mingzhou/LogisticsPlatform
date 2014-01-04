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

import java.util.ArrayList;
import java.util.List;

import org.espier.ios6.ui.CommonSettingInfo;
import org.espier.ios6.ui.SettingInfo;
import org.espier.ios6.ui.SettingInfoListGetter;
import org.espier.ios6.ui.cb.SimpleActivitySettingCb;
import org.espier.ios6.ui.cb.StatusActivitySettingCb;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.SparseArray;

public class SettingsController {
    protected static final String TAG = "SettingsController";

    private Context mContext;
    private Resources mRes;
    private boolean mUseLanguagePackage = false;

    private SparseArray<ArrayList<SettingInfo>> mSettingMap = new SparseArray<ArrayList<SettingInfo>>();
    private SparseArray<String[]> mGroupInfoMap = new SparseArray<String[]>();
    private static final String[] mNullGroupInfo = new String[]{null, null};

    private static SettingsController mInstance;
    private String mLocale;
    private int mStart, mEnd;

    public static SettingsController getInstance(Context context, int s, int e) {
        final Configuration configuration = context.getResources().getConfiguration();
        final String locale = configuration.locale.toString();
        Log.v(TAG, "getInstance locale ============== " + locale);

        if (mInstance == null) {
            mInstance = new SettingsController(context, s, e);
            mInstance.mLocale = locale;
            mInstance.mStart = s;
            mInstance.mEnd = e;
        } else {
            if(!mInstance.mLocale.equals(locale)) {
                mInstance.mLocale = locale;
                mInstance.init(context, mInstance.mStart, mInstance.mEnd);
            }
        }
        return mInstance;
    }

    public static void reload() {
        if (mInstance != null) {
            mInstance.releaseResource();
        }

        // let next time getInstance() reload resource.
        mInstance = null;
    }

    public static SettingInfo findInfoById(List<SettingInfo> infoList, String id) {
        if (null == infoList || null == id) {
            return null;
        }

        for (SettingInfo info : infoList) {
            if (null == info) {
                continue;
            }

            if (id.equalsIgnoreCase(info.getId())) {
                return info;
            }
        }

        return null;
    }

    public void releaseResource() {
        if (null != mGroupInfoMap) {
            mGroupInfoMap.clear();
            mGroupInfoMap = null;
        }

        if (null != mSettingMap) {
            mSettingMap.clear();
            mSettingMap = null;
        }
    }

    private SettingsController(Context context, int s, int e) {
        init(context, s, e);
    }

    public List<SettingInfo> getSettingInfoList(int resId) {
        return mSettingMap.get(resId);
    }

    public String getGroupTitle(int resId) {
        return mGroupInfoMap.get(resId, mNullGroupInfo)[0];
    }

    public String getGroupDesc(int resId) {
        return mGroupInfoMap.get(resId, mNullGroupInfo)[1];

    }

    private void init(Context context, int settings_begin, int settings_end) {
        mContext = context.getApplicationContext();

        mRes = mContext.getResources();

        mSettingMap.clear();
        mGroupInfoMap.clear();

        for (int i = settings_begin; i < settings_end; ++i) {
            loadSettings(i);
        }
    }

    public void unloadSettings(int resId){
        mSettingMap.remove(resId);
    }

    private String getLocalizedArrayString(TypedArray typedArray, int index) {
        int temp = typedArray.getResourceId(index, -1);
        if (temp != -1) {
            return mRes.getString(temp);
        }
        return typedArray.getString(index);
    }

    public void loadSettings(int resId) {
        TypedArray setting = mRes.obtainTypedArray(resId);
        ArrayList<SettingInfo> settingList = new ArrayList<SettingInfo>();
        mSettingMap.put(resId, settingList);

        for (int i = 0; i < setting.length();) {
            //id, type, activityOrCbClass, description, icon, title
            String id = setting.getString(i++);
            int type = setting.getInt(i++, 0);
            String activityOrCbClass = setting.getString(i++);

            String statusCbClass;
            String title;
            if (!mUseLanguagePackage) {
                statusCbClass = setting.getString(i++);
                title = setting.getString(i+1);
            } else {
                statusCbClass = getLocalizedArrayString(setting, i);
                i++;
                title = getLocalizedArrayString(setting, i + 1);
            }

            Drawable icon = null;
            try {
                icon = setting.getDrawable(i++);
            } catch (Exception e) {
            }
            //skip title
            i++;

            String decs = statusCbClass;

            CommonSettingInfo.SettingCallback settingCb = null;
            StatusActivitySettingCb.StatusGetter statusCb = null;
            Intent intent = null;

            try {
                switch (type) {
                case SettingInfo.TYPE_SWITCH:
                case SettingInfo.TYPE_CHOICE:
                case SettingInfo.TYPE_WIFI_CHOICE:
                case SettingInfo.TYPE_VOLUME_SETTING:
                case SettingInfo.TYPE_BRIGHTNESS_SETTING:
                case SettingInfo.TYPE_LABEL:
                case SettingInfo.TYPE_ENTER:
                case SettingInfo.TYPE_IMAGE_CHIOCE:
                case SettingInfo.TYPE_CHOICE_WITH_FOOT:
                    settingCb = (CommonSettingInfo.SettingCallback) Class.forName(activityOrCbClass)
                            .newInstance();
                    break;

                case SettingInfo.TYPE_SIMPLE_ACTIVITY:
                    intent = new Intent(mContext,
                            Class.forName(activityOrCbClass));
                    settingCb = new SimpleActivitySettingCb(intent);
                    break;

                case SettingInfo.TYPE_STATUS_ACTIVITY:
                    intent = new Intent(mContext,
                            Class.forName(activityOrCbClass));
                    statusCb = (StatusActivitySettingCb.StatusGetter) Class.forName(statusCbClass)
                            .newInstance();
                    settingCb = new StatusActivitySettingCb(intent, statusCb);
                    break;

                case SettingInfo.INNER_TYPE_GROUP_INFO:
                    String[] groupInfo = new String[] {
                            activityOrCbClass, decs,
                    };
                    mGroupInfoMap.put(resId, groupInfo);
                    break;

                case SettingInfo.INNER_TYPE_DYNAMIC:
                    SettingInfoListGetter getter = (SettingInfoListGetter) Class.forName(
                            activityOrCbClass).newInstance();
                    getter.get(mContext, settingList);
                    break;

                default:
                    break;
                }

            } catch (ClassNotFoundException e) {
                // ignore this exception.
                //e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (type < SettingInfo.INNER_TYPE_BEGIN)
                settingList.add(new CommonSettingInfo(id, type, icon, title, decs, settingCb));
        }
        setting.recycle();
    }
}
