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

package org.espier.ios6.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * AuxConfigs
 */
public class AuxConfigs {
    private static AuxConfigs mInstance;
    private static final float mPhonePadActualSizeDivPoint = 6.5f;
    private static final float mPhonePadActualSizeDivPointAux = 4.6f;
    private static final int STANDARD_ICON_SIZE = 89;

//    private static final float ICON_ITEM_SCALE = 0.73f;
    public static final float FOLDER_TOP_SCALE = 0.15f;

    private static final String AUX_PACKAGE_NAME = "cn.fmsoft.launcherAux";

    private String mAuxPackage = AUX_PACKAGE_NAME;
    private Resources mAuxResources;
    private Context mAuxContext;

    private final SharedPreferences mSharedPreferences;
    // layout params
    public int screenDensity;
    public float scaleFactor;
    public int iconWidth = STANDARD_ICON_SIZE;
    public int iconHeight = STANDARD_ICON_SIZE;

    public int itemWidth = STANDARD_ICON_SIZE;
    public int itemHeight = STANDARD_ICON_SIZE;
    public int itemPaddingTop = 16;
    public int itemTextSize = 18;

    int desktopMaxCellX;
    int desktopMaxCellY;
    int folderMaxCellXPortrait;
    int folderMaxCellYPortrait;
    int folderMaxCellXLandScape;
    int folderMaxCellYLandScape;

    public static boolean isPad = false;

    public boolean supportLandscapePortrait = false;

    public static AuxConfigs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new AuxConfigs(context);
        }
        return mInstance;
    }

    public static void unInstance() {
        mInstance = null;
    }

    private AuxConfigs(Context context) {
        mSharedPreferences = context.getSharedPreferences(
                Common.PREFERENCE_FILENAME, Common.MODE_PRIVATE);
        mAuxResources = getAuxResources(context);
        if (mAuxResources != null) {
            try {
                mAuxContext = context.createPackageContext(mAuxPackage,
                        Context.CONTEXT_IGNORE_SECURITY);
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (mAuxResources == null || mAuxContext == null) {
            mAuxResources = context.getResources();
            mAuxPackage = context.getPackageName();
            mAuxContext = context;
        }

        initLayoutConfigs(context);
    }

    private void initLayoutConfigs(Context context) {

        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        float density = dm.density;// (0.75/1.0/1.5/2.0)
        float scaleDensity = dm.scaledDensity;// (0.75/1.0/1.5/2.0)
        int densityDPI = dm.densityDpi;// (120/160/240/320)
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        float xdpi = dm.xdpi > densityDPI ? dm.xdpi : densityDPI;
        float ydpi = dm.ydpi > densityDPI ? dm.ydpi : densityDPI;
        double x = Math.pow(dm.widthPixels / xdpi, 2);
        double y = Math.pow(dm.heightPixels / ydpi, 2);
        double actualSize = Math.sqrt(x + y);

        MyLogUtil.d("tag", "=================dm.xdpi=" + dm.xdpi + "==dm.ydpi="
                + dm.ydpi + "==densityDPI==" + densityDPI + "===="
                + dm.scaledDensity + "===size1==" + actualSize);

        if (actualSize >= mPhonePadActualSizeDivPointAux
                && actualSize <= mPhonePadActualSizeDivPoint) {
            actualSize = Math.sqrt(Math.pow(dm.widthPixels / dm.xdpi, 2)
                    + Math.pow(dm.heightPixels / dm.ydpi, 2));
        }
        MyLogUtil.d("tag", "===screenWidth==" + screenWidth + "==screenHeight=="
                + screenHeight + "==density==" + density + "=actualSize="
                + actualSize);

        if (mSharedPreferences.contains(Common.PAD_SWITCH)) {
            if (mSharedPreferences.getBoolean(Common.PAD_SWITCH, false)) {
                isPad = true;
                initPadLayout(context, screenWidth, screenHeight, scaleDensity);
                MyLogUtil.d("masa", "set to pad by SharedPreferences");
            } else {
                isPad = false;
                initPhoneLayout(context, screenWidth, screenHeight,
                        scaleDensity);
                MyLogUtil.d("masa", "set to phone by SharedPreferences");
            }
        } else {
            if (actualSize > mPhonePadActualSizeDivPoint) {
                isPad = true;
                initPadLayout(context, screenWidth, screenHeight, scaleDensity);
                MyLogUtil.d("masa", "set to pad by calc");
            } else {
                isPad = false;
                initPhoneLayout(context, screenWidth, screenHeight,
                        scaleDensity);
                MyLogUtil.d("masa", "set to phone by calc");
            }
        }
    }

    private void initPhoneLayout(Context context, int w, int h, float density) {
    }

    private void initPadLayout(Context context, int w, int h, float density) {
    }

    private Resources getAuxResources(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Resources resources = null;
        try {
            resources = packageManager.getResourcesForApplication(mAuxPackage);
        } catch (NameNotFoundException e) {
            // e.printStackTrace();
        }

        return resources;
    }
}
