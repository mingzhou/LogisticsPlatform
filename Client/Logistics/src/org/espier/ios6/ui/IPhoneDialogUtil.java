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
import android.view.Gravity;
import android.widget.Button;

import com.logistics.R;


public class IPhoneDialogUtil {
    public static final int ID_CANCEL = 0;
    public static final int ID_OK = 1;
    public static final int ID_DOWNLOAD = 2;
    public static final int ID_DELETE = 3;
//    private static final String TAG = "IPhoneDialogUtil";

    /*
    public static void showDownloadDialog(Context context,
            Button.OnClickListener downloadLister) {
        float textSize = 16;
        float textHeight = 120;
        final Context finalContext = context;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            textSize = 16;
        }

        try {
            textHeight = context.getResources().getDimension(
                    R.dimen.espier_browser_promot_msg_height);
        } catch (Exception e) {
            textHeight = 120;
        }

        new IPhoneDialog.BuilderEx(context)
        .setTitle(R.string.espier_browser_promot_install_title)
        .setMessage(R.string.espier_browser_promot_install_msg)
        .setMessageHeight((int)textHeight)
        .setMessageGravity(Gravity.LEFT)
        .setShowIcon(false)
        .setButtonTextSize(textSize)
        .setPositiveButton(
                R.string.ok,
                downloadLister)
        .setNeutralButton(
                R.string.iphone_dlg_promot_learn_more,
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String uri = finalContext.getResources().getString(
                                    R.string.espier_browser_web);
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(uri));
                            finalContext.startActivity(intent);
                        } catch (Exception e) {
                            Log.e(TAG, e.toString());
                        }
                    }
                })
        .setNegativeButton(
                R.string.cancel,
                null)
        .show();
    }

    public static void showDeleteDialog(Context context,
            Button.OnClickListener downloadLister,
            Button.OnClickListener deleteLister) {
        float textSize = 16;
        float textHeight = 120;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            textSize = 16;
        }

        try {
            textHeight = context.getResources().getDimension(
                    R.dimen.espier_browser_promot_msg_height);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            textHeight = 120;
        }

        new IPhoneDialog.BuilderEx(context)
        .setTitle(R.string.espier_browser_promot_delete_title)
        .setMessage(R.string.espier_browser_promot_delete_msg)
        .setMessageGravity(Gravity.LEFT)
        .setMessageHeight((int)textHeight)
        .setShowIcon(false)
        .setButtonTextSize(textSize)
        .setPositiveButton(
                R.string.iphone_dlg_promot_download,
                downloadLister)
        .setNeutralButton(
                R.string.iphone_dlg_promot_delete,
                deleteLister)
        .setNegativeButton(
                R.string.cancel,
                null)
        .show();
    }*/

    public static void showConfirmDialog(Context context,
            int msgId,
            Button.OnClickListener confirmLister,
            Button.OnClickListener cancelLister) {
        /*float textSize = 16;
        float textHeight = 120;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textSize = 16;
        }

        try {
            textHeight = context.getResources().getDimension(
                    R.dimen.espier_browser_promot_msg_height);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textHeight = 120;
        }*/

        new IPhoneDialog.BuilderEx(context)
        .setMessage(msgId)
        .setMessageGravity(Gravity.CENTER)
        .setShowIcon(false)
        .setPositiveButton(
                R.string.ok,
                confirmLister)
        .setNegativeButton(
                R.string.cancel,
                null)
        .show();
    }

    public static void showGetRemoveApphupDialog(Context context,
            String msgId, String title,
            Button.OnClickListener confirmLister,
            Button.OnClickListener cancelLister,int sure) {

        new IPhoneDialog.BuilderEx(context)
        .setTitle(title)
        .setMessage(msgId)
        .setMessageGravity(Gravity.CENTER)
        .setShowIcon(false)
        .setPositiveButton(
                sure,
                confirmLister)
        .setNegativeButton(
                R.string.cancel,
                cancelLister).setCancelable(false).show();

    }
//
    public static void showConfirmCheckPadDialog(Context context,
            String msgId, String title,
            Button.OnClickListener confirmLister,
            Button.OnClickListener cancelLister,IosLikeToggleButton likebutton,boolean ischeckek)
    {

        /*float textSize = 16;
        float textHeight = 120;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textSize = 16;
        }

        try {
            textHeight = context.getResources().getDimension(
                    R.dimen.espier_browser_promot_msg_height);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textHeight = 120;
        }*/

        new IPhoneDialog.BuilderEx(context)
        .setTitle(title)
        .setMessage(msgId)
        .setMessageGravity(Gravity.CENTER)
        .setShowIcon(false)
        .setPositiveButton(
                R.string.ok,
                confirmLister)
        .setNegativeButton(
                R.string.cancel,
                cancelLister).setCancelable(false).ipadshows(likebutton, ischeckek);;
    }
    public static void showConfirmDialog(Context context,
            String msgId, String title,
            Button.OnClickListener confirmLister,
            Button.OnClickListener cancelLister) {
        /*float textSize = 16;
        float textHeight = 120;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textSize = 16;
        }

        try {
            textHeight = context.getResources().getDimension(
                    R.dimen.espier_browser_promot_msg_height);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textHeight = 120;
        }*/

        new IPhoneDialog.BuilderEx(context)
        .setTitle(title)
        .setMessage(msgId)
        .setMessageGravity(Gravity.CENTER)
        .setShowIcon(false)
        .setPositiveButton(
                R.string.ok,
                confirmLister)
        .setNegativeButton(
                R.string.cancel,
                cancelLister).setCancelable(false).show();
//        .(new OnKeyListener() {
//
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode,
//                    KeyEvent keyEvent) {
//                if (keyCode == KeyEvent.KEYCODE_BACK
//                        || keyCode == KeyEvent.KEYCODE_HOME) {
//                    onKeyListener.onKeyClick();
//                } else if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//                    return true;
//                }
//                return false;
//            }
//
//        });
    }


    public static void showConfirmDialogNew(Context context, int titleID,
            int msgID, Button.OnClickListener confirmLister,
            Button.OnClickListener cancelLister) {

        new IPhoneDialog.BuilderEx(context).setTitle(context.getResources().getString(titleID)).setMessage(context.getResources().getString(msgID))
                .setMessageGravity(Gravity.CENTER).setShowIcon(false)
                .setPositiveButton(R.string.ok, confirmLister)
                .setNegativeButton(R.string.cancel, cancelLister)
                .setCancelable(false).show();
    }


    public static void showDownloadError(Context context) {
        showTipsDialog(context,
                R.string.promot_download_error_title,
                R.string.promot_download_error_msg);
    }

    /*public static void showPromoDialog(Context context,
            int titleId, int msgId,
            int positiveId, Button.OnClickListener positiveListener,
            int negativeId, Button.OnClickListener negativeListener) {
        float textSize = 16;
        final Context finalContext = context;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textSize = 16;
        }

        new IPhoneDialog.BuilderEx(context)
        .setTitle(titleId)
        .setMessage(msgId)
        .setMessageGravity(Gravity.CENTER)
        .setShowIcon(false)
        .setButtonTextSize(textSize)
        .setPositiveButton(positiveId, positiveListener)
        .setNegativeButton(negativeId, negativeListener)
        .setNeutralButton(
                R.string.iphone_dlg_promot_learn_more,
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String uri = finalContext.getResources().getString(
                                    R.string.espier_browser_web);
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(uri));
                            finalContext.startActivity(intent);
                        } catch (Exception e) {
                            LogUtil.e(TAG, e.toString());
                        }
                    }
                })
        .show();
    }

    public void showPromoDialog(Context context,
            String title, String msg,
            int positiveId, Button.OnClickListener positiveListener,
            int negativeId, Button.OnClickListener negativeListener) {
        float textSize = 16;
        final Context finalContext = context;

        try {
            textSize = context.getResources().getDimension(
                    R.dimen.iphone_dlg_promot_btn_size);
        } catch (Exception e) {
            LogUtil.e(TAG, e.toString());
            textSize = 16;
        }

        new IPhoneDialog.BuilderEx(context)
        .setTitle(title)
        .setMessage(msg)
        .setMessageGravity(Gravity.CENTER)
        .setShowIcon(false)
        .setButtonTextSize(textSize)
        .setPositiveButton(positiveId, positiveListener)
        .setNegativeButton(negativeId, negativeListener)
        .setNeutralButton(
                R.string.iphone_dlg_promot_learn_more,
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String uri = finalContext.getResources().getString(
                                    R.string.espier_browser_web);
                            Intent intent = new Intent(Intent.ACTION_VIEW,
                                    Uri.parse(uri));
                            finalContext.startActivity(intent);
                        } catch (Exception e) {
                            LogUtil.e(TAG, e.toString());
                        }
                    }
                })
        .show();
    }*/

    public static void showTipsDialog(Context context, int titleId, int msgId) {
        new IPhoneDialog.BuilderEx(context)
            .setTitle(titleId)
            .setMessage(msgId)
            .setMessageGravity(Gravity.CENTER)
            .setShowIcon(false)
            .setPositiveButton(R.string.ok, null)
            .show();
    }

    public static void showTipsDialog(Context context, String title, String msg) {
        new IPhoneDialog.BuilderEx(context)
            .setTitle(title)
            .setMessage(msg)
            .setMessageGravity(Gravity.CENTER)
            .setShowIcon(false)
            .setPositiveButton(R.string.ok, null)
            .show();
    }

    public static void showLicenseDialog(final Context context) {
        /*
        showConfirmDialog(context,
                R.string.need_license,
                new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                LicenseAppClient.buyLicenseApp(context, Launcher.LICENSE_PACKAGE);
            }

        }, new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
            }
        });
        */
    }
}
