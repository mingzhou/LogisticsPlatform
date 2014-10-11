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

import org.espier.ios6.ui.utils.Common;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RemoteViews;

import com.logistics.R;


public class NewStatusBarActivity extends Activity {
//    private StatusBarReceiver mStatusBarReceiver;
    private boolean mNeedStatusBar = false;

    private static final String ACTION_REMOTE_STATUSBAR_VIEWS =
        "mobi.espier.launcher.plugin.notifications.SEND_REMOTE_STATUSBAR_VIEWS";
    private static final String ACTION_SEND_REMOTE_EVENT =
        "mobi.espier.launcher.plugin.notifications.RECIVER_REMOTE_TOUCHEVENT";

    private static final String KEY_REMOTE_STATUSBAR_VIEWS = "remote_statusbar_views";

    private static final String ACTION_CALL_UPDATE_STATUSBAR =
        "mobi.espier.launcher.plugin.notifications.CALL_UPDATE_STATUSBAR";

    private static final String KEY_TOUCHEVENT = "TouchEvent";

    private Context mContext;
    private View mStatusBarView;
    private LinearLayout mStatusBarContent;
    private RemoteViews mRemoteViews;
    private Intent mIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
    }

    protected void initStatusBar() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_REMOTE_STATUSBAR_VIEWS);
        registerReceiver(mStatusBarUpdateReceiver, filter);
        mIntent=new Intent(ACTION_SEND_REMOTE_EVENT);
    }

    public void decideToShowStatusBar() {
        SharedPreferences preferences = getSharedPreferences(
                Common.PREFERENCE_FILENAME, Common.MODE_PRIVATE);
        mNeedStatusBar = preferences.getBoolean(Common.STATUSBAR_SWITCH,
                false);
        mStatusBarContent = (LinearLayout) findViewById(R.id.statusbar);

        int status_bar_height = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (status_bar_height >0 ){
            status_bar_height = Resources.getSystem().getDimensionPixelSize(status_bar_height);
        }else{
            status_bar_height = getResources().getDimensionPixelSize(
                    R.dimen.status_bar_height);
        }
        if(null != mStatusBarContent){
            LayoutParams lp=(LayoutParams) mStatusBarContent.getLayoutParams();
            lp.height=status_bar_height;
            mStatusBarContent.setLayoutParams(lp);
        }

        int isVisible = View.GONE;

        if (mNeedStatusBar && isEspierNotificationInstalled()) {
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            isVisible = View.VISIBLE;
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        if (null != mStatusBarContent) mStatusBarContent.setVisibility(isVisible);
    }

    @Override
    protected void onResume() {
        super.onResume();
        {
            decideToShowStatusBar();
            if(mNeedStatusBar){
            initStatusBar();
            sendBroadcast(new Intent(ACTION_CALL_UPDATE_STATUSBAR));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mNeedStatusBar){
        deInitStatusBar();
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    protected void deInitStatusBar() {
        if(mStatusBarUpdateReceiver!=null){
            unregisterReceiver(mStatusBarUpdateReceiver);
        }
    }

    public boolean isEspierNotificationInstalled(){
        boolean ret=false;
        PackageManager mPackageManager=this.getPackageManager();

        ret=(mPackageManager.getLaunchIntentForPackage("mobi.espier.launcher.plugin.notifications")!=null);
        boolean retfree=(mPackageManager.getLaunchIntentForPackage("mobi.espier.launcher.plugin.notificationsfree")!=null);

        return (ret || retfree);
    }
    /**设置状态栏为一半透明黑色渐变，主要用于桌面主界面*/
    protected void setStatusBarBackground(){
        if(mNeedStatusBar && mStatusBarContent!=null){
            mStatusBarContent.setBackgroundResource(R.drawable.trans_status_bar);
            mStatusBarContent.invalidate();
        }
    }
    protected void setStatusBarColor(int Color){
        if(mStatusBarContent!=null){
            mStatusBarContent.setBackgroundColor(Color);
        }
    }

    private BroadcastReceiver mStatusBarUpdateReceiver=new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(ACTION_REMOTE_STATUSBAR_VIEWS.equals(action)){
                mRemoteViews=intent.getParcelableExtra(KEY_REMOTE_STATUSBAR_VIEWS);
                if (mStatusBarView != null){
                    mRemoteViews.reapply(mContext, mStatusBarView);
                    return;
                }
                mStatusBarView = mRemoteViews.apply(mContext, null);

                mStatusBarView.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        mIntent.putExtra(KEY_TOUCHEVENT, event);
                        mContext.sendBroadcast(mIntent);
                        return true;
                    }
                });
                if (mStatusBarView.getParent() == null && mStatusBarContent!=null) {
                    mStatusBarContent.addView(mStatusBarView);
                }else{
                    //error?
                }
            }

        }
    };
}
