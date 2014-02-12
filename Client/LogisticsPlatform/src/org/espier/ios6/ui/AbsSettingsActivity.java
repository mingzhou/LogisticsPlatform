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

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.logistics.R;

public abstract class AbsSettingsActivity extends NewStatusBarActivity
        implements OnClickListener {

    private ViewGroup mMainContainer;

    private ViewGroup mLinearContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AuxConfigs.getInstance(this).supportLandscapePortrait
                || !AuxConfigs.isPad) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.ios_abs_setting_activity);

        mMainContainer = (ViewGroup) findViewById(R.id.mainContainer);

        mLinearContainer = (ViewGroup) findViewById(R.id.linearContainer);

        findViewById(R.id.buttonLeft).setOnClickListener(this);
        findViewById(R.id.buttonRight).setOnClickListener(this);


        setupViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AuxConfigs.unInstance();//release ref
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i = 0, count = mMainContainer.getChildCount(); i < count; ++i) {
            try {
                View v = mMainContainer.getChildAt(i);
                if (v instanceof IosLikeListContainer) {
                    final IosLikeListContainer child = (IosLikeListContainer) v;
                    child.onResume();
                }
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    abstract protected void setupViews();

    @Override
	public void setTitle(int resId) {
        ((TextView) findViewById(R.id.title)).setText(resId);
    }

    @Override
	public void setTitle(CharSequence str) {
        ((TextView) findViewById(R.id.title)).setText(str);
    }

    public void enableReturnButton(boolean enable) {
        enableReturnButton(enable, null);
    }

    public Button rightButton()
    {
     Button button=(Button)findViewById(R.id.buttonRight);
     button.setText(R.string.ok);
     return button;

    }

    public void enableReturnButton(boolean enable, String text) {
        Button btn = (Button) findViewById(R.id.buttonLeft);
        if (enable) {
            btn.setVisibility(View.VISIBLE);
            if (text != null) {
                btn.setText(text);
            }
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    public void enableRightButton(boolean enable, OnClickListener clickListener) {
        enableRightButton(enable, null, clickListener);
    }

    public void enableRightButton(boolean enable, String text,
            OnClickListener clickListener) {
        Button btn = (Button) findViewById(R.id.buttonRight);
        if (enable) {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(clickListener);
            if (null != text) {
                btn.setText(text);
            }
            if (null != clickListener) {
                btn.setOnClickListener(clickListener);
            }
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("deprecation")
	public void enableRightButton(boolean enable, Drawable drawable,String text,
            OnClickListener clickListener) {
        Button btn = (Button) findViewById(R.id.buttonRight);
        if (enable) {
            btn.setVisibility(View.VISIBLE);
            btn.setOnClickListener(clickListener);
            if(null != drawable){
                btn.setBackgroundDrawable(drawable);
            }
            if (null != text) {
                btn.setText(text);
            } else {
                btn.setText("");
            }
            if (null != clickListener) {
                btn.setOnClickListener(clickListener);
            }
        } else {
            btn.setVisibility(View.INVISIBLE);
        }
    }

    public void addView(View child) {
        mMainContainer.addView(child);
    }

    public void addView(View child, LayoutParams lp) {
        mMainContainer.addView(child, lp);
    }

    public void addLinearView(View child) {
        mLinearContainer.addView(child);
    }

    public void addLinearView(View child, LayoutParams lp) {
        mLinearContainer.addView(child, lp);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.buttonLeft)
            finish();
    }
}
