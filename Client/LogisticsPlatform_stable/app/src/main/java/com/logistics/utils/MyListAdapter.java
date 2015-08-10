package com.logistics.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by lizhuo on 2015/5/26.
 */
public class MyListAdapter extends ArrayAdapter {
    private Context mContext;
    private int id;
    private List<String[]> items;

    public MyListAdapter(Context context, int resource, List<String[]> list) {
        super(context, resource, list);
        mContext = context;
        id = resource;
        items = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}
