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

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.logistics.R;

public class RotationLoadDialog extends Dialog {

    private static RotationLoadDialog mDialog;


    private RotationLoadDialog(Context context, int theme) {
        super(context, theme);
    }

    @SuppressWarnings("deprecation")
	public static  RotationLoadDialog createDialog(Context context) {

        if (mDialog == null) {
            mDialog = new RotationLoadDialog(context,
                    R.style.rotation_load_dialog);
            mDialog.setContentView(R.layout.rotation_load_dialog);
            mDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
            LayoutParams p = mDialog.getWindow().getAttributes();
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            Display d = wm.getDefaultDisplay();
            p.width = (int) (d.getWidth() * 0.61);
            mDialog.getWindow().setAttributes(p);

        }
        return mDialog;
    }

    @Override
	public void onWindowFocusChanged(boolean hasFocus) {
        if (mDialog != null) {
            ImageView imageView = (ImageView) mDialog
                    .findViewById(R.id.load_progress_imageview);
            AnimationDrawable animationDrawable = (AnimationDrawable) imageView
                    .getBackground();
            animationDrawable.start();
        }
    }

    public void setMessage(String strMessage) {
        if (mDialog != null) {
            TextView tvMsg = (TextView) mDialog
                    .findViewById(R.id.load_progress_textview);
            if (tvMsg != null) {
                tvMsg.setText(strMessage);
            }
        }
    }

    public static void show(Context context,String msg,boolean canCancel) {
        final RotationLoadDialog    mLoadDialog = RotationLoadDialog.createDialog(context);
        mLoadDialog.setCancelable(canCancel);
        mLoadDialog.setMessage(msg);
        mLoadDialog.show();
    }
}
