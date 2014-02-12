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

import org.espier.ios6.ui.utils.Common;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.logistics.R;

public class IosLikeListContainer extends LinearLayout {

    public static final int CHOICE_MODE_NONE = ListView.CHOICE_MODE_NONE;
    public static final int CHOICE_MODE_SINGLE = ListView.CHOICE_MODE_SINGLE;
    public static final int CHOICE_MODE_MULTIPLE = ListView.CHOICE_MODE_MULTIPLE;

    private LinearLayout mTitleLayout;
    private TextView mTitleTextView;
    private ListView mListView;
    private TextView mDescriptionTextView;
    private static  Drawable noticeImageNumbers;
    private IosLikeToggleButton.OnCheckedChangeListener mOnCheckedChangeListener;
    private boolean haveTitle;

    private boolean isEnabled;

    public IosLikeListContainer(Context context) {
        this(context, null);
    }
    public IosLikeListContainer(Context context,boolean isTitle) {
        this(context, null);
        haveTitle=isTitle;
    }


    public IosLikeListContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);

        isEnabled = true;
        setupViews(context);
    }

    private void setupViews(Context context) {
        final LayoutInflater li = LayoutInflater.from(context);

        // lmf edited  old margin is 10
        int margin = Common.dip2px(context, 7);

        final LayoutParams lpTitle = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lpTitle.setMargins(margin, margin, margin, 0);

        mTitleLayout = (LinearLayout) li.inflate(
                R.layout.list_container_header, null);
        mTitleTextView = (TextView) mTitleLayout
                .findViewById(R.id.list_container_header_text);
        setTitle(null);
        addView(mTitleLayout, lpTitle);

        final LayoutParams lpList = new LayoutParams(lpTitle);
        if(haveTitle){
            lpList.setMargins(margin, 0, margin, margin);
        }else{
            lpList.setMargins(margin, margin, margin, margin);
        }
        mListView = (ListView) li.inflate(R.layout.list_container_listview,
                null);
        addView(mListView, lpList);

        final LayoutParams lpDescription = new LayoutParams(lpTitle);
        lpDescription.setMargins(margin, 0, margin, margin);
        mDescriptionTextView = (TextView) li.inflate(
                R.layout.list_container_descliption, null);
        setDescription(null);
        addView(mDescriptionTextView, lpDescription);
    }

    public void setChoiceMode(int choiceMode) {
        mListView.setChoiceMode(choiceMode);
    }

    public void genListView(List<SettingInfo> infos) {

        if (infos != null) {
            final SettingInfoAdapter adapter = new SettingInfoAdapter(
                    getContext(), infos);

            if (mOnCheckedChangeListener != null)
                adapter.setOnCheckedChangeListener(mOnCheckedChangeListener);
            mListView.setAdapter(adapter);
            mListView.setOnItemClickListener(adapter);
            IosLikeListView.setListViewHeightBasedOnChildren(mListView);
            // XXX :
        }
    }

    public ListView getListView() {
        return mListView;
    }

    public void addToListView(List<SettingInfo> infos) {

        final SettingInfoAdapter adapter = (SettingInfoAdapter) mListView
                .getAdapter();
        if (adapter == null)
            genListView(infos);
        else {
            for (SettingInfo info : infos) {
                adapter.add(info);
            }
            IosLikeListView.setListViewHeightBasedOnChildren(mListView);
        }
    }

    public void removeFromListView(List<SettingInfo> infos) {

        final SettingInfoAdapter adapter = (SettingInfoAdapter) mListView
                .getAdapter();

        if (adapter == null)
            genListView(infos);
        else {
            for (SettingInfo info : infos) {
                adapter.remove(info);
            }
            IosLikeListView.setListViewHeightBasedOnChildren(mListView);
        }
    }

    public void clearListView() {
        final SettingInfoAdapter adapter = (SettingInfoAdapter) mListView
                .getAdapter();
        if (adapter != null) {
            adapter.clear();
            IosLikeListView.setListViewHeightBasedOnChildren(mListView);
        }
    }

    public void onResume() {
        final SettingInfoAdapter adapter = (SettingInfoAdapter) mListView
                .getAdapter();
        adapter.notifyDataSetChanged();
        mListView.invalidateViews();
        adapter.getNoticeNumber(IosLikeListContainer.noticeImageNumbers);
        if(isEnabled){
            adapter.setItemEnabled(true);
        }else{
            adapter.setItemEnabled(false);
        }
    }

    public void setTitle(CharSequence text) {
        if (text != null && text.length() != 0)
            mTitleTextView.setVisibility(View.VISIBLE);
        else
            mTitleTextView.setVisibility(View.GONE);

        mTitleTextView.setText(text);
    }

    public void setTitle(int resId) {
        Resources res = getContext().getResources();
        String str = res.getString(resId);
        setTitle(str);
    }

    public void setTitleContent(View v) {
        if (mTitleLayout != null) {
            mTitleLayout.addView(v);
        }
    }

    public void setDescription(CharSequence text) {
        if (text != null && text.length() != 0)
            mDescriptionTextView.setVisibility(View.VISIBLE);
        else
            mDescriptionTextView.setVisibility(View.GONE);

        mDescriptionTextView.setText(text);
    }

    public static void setnoticeImageNumbers(Drawable b) {
        IosLikeListContainer.noticeImageNumbers = b;
    }
    public void setNoticeImages(Drawable b)
    {
        setnoticeImageNumbers(b);
        final SettingInfoAdapter adapter = (SettingInfoAdapter) mListView
                .getAdapter();
        adapter.getNoticeImages(IosLikeListContainer.noticeImageNumbers);
    }


    public void setOnCheckedChangeListener(IosLikeToggleButton.OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
        if (mOnCheckedChangeListener != null) {
            final SettingInfoAdapter adapter = (SettingInfoAdapter) mListView
                    .getAdapter();
            if (adapter != null)
                adapter.setOnCheckedChangeListener(mOnCheckedChangeListener);
        }
    }

       public void updateListView(){
           mListView.invalidateViews();
        }

    public void setItemIsEnable(boolean flag){
        if (this.isEnabled != flag)
            this.isEnabled = flag;
    }
}
