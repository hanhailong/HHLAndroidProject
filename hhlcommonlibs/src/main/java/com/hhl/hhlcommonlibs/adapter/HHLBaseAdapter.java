package com.hhl.hhlcommonlibs.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 目前只支持两种类型的view
 * Created by hailonghan on 15/7/10.
 */
public abstract class HHLBaseAdapter<T> extends BaseAdapter {

    private static final String TAG = HHLBaseAdapter.class.getSimpleName();

    //上下文
    protected final Context mContext;

    //数据源
    protected final List<T> mData;

    //扩展，为了显示底部加载更多进度条
    protected boolean mShowBottomProgressBar = false;

    protected HHLBaseAdapter(Context mContext, List<T> data) {
        this.mContext = mContext;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
    }

    @Override
    public int getCount() {
        int extra = mShowBottomProgressBar ? 1 : 0 ;
        return this.mData.size() + extra;
    }

    @Override
    public T getItem(int position) {
        if (position >= this.mData.size()) return null;
        return this.mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //因为要支持两种类型，一种底部进度条
    //TODO 添加多种进度条支持
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= this.mData.size() ? 1 : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
