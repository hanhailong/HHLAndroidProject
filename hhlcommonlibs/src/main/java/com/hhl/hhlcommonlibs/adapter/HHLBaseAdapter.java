package com.hhl.hhlcommonlibs.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 目前只支持两种类型的view
 * Created by hailonghan on 15/7/10.
 */
public abstract class HHLBaseAdapter<T, H extends HHLBaseAdapterHelper> extends BaseAdapter {

    private static final String TAG = HHLBaseAdapter.class.getSimpleName();

    //上下文
    protected final Context mContext;

    //数据源
    protected final List<T> mData;

    //item布局
    protected
    @LayoutRes
    int mLayoutId;

    //扩展，为了显示底部加载更多进度条
    protected boolean mShowBottomProgressBar = false;

    //添加多个Item支持，不影响之前添加的底部ProgressBar
    protected MultiItemTypeSupport<T> mMultiItemTypeSupport;

    protected HHLBaseAdapter(Context context, @LayoutRes int layoutId) {
        this(context, layoutId, null);
    }

    protected HHLBaseAdapter(Context context, @LayoutRes int layoutId, List<T> data) {
        this.mContext = context;
        this.mLayoutId = layoutId;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
    }

    //这里为了添加支持多个layout布局，不能传layoutId了
    protected HHLBaseAdapter(Context context, MultiItemTypeSupport<T> multiItemTypeSupport) {
        this(context,null,multiItemTypeSupport);
    }

    protected HHLBaseAdapter(Context context, List<T> data, MultiItemTypeSupport<T> multiItemTypeSupport) {
        this.mContext = context;
        this.mData = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.mMultiItemTypeSupport = multiItemTypeSupport;
    }

    @Override
    public int getCount() {
        int extra = mShowBottomProgressBar ? 1 : 0;
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
        if (mMultiItemTypeSupport != null) {
            return mMultiItemTypeSupport.getViewTypeCount() + 1;
        }
        return 2;
    }

    @Override
    public int getItemViewType(int position) {

        if (position >= this.mData.size()) {//底部进度条
            return 0;
        }
        return (mMultiItemTypeSupport != null) ? mMultiItemTypeSupport.getItemViewType(position, getItem(position)) : 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == 0) {//代表不是底部进度条
            return createBottomProgressBar(convertView);
        }

        H adapterHelper = getHHLAdapterHelper(position, convertView, parent);
        T item = getItem(position);
        //将helper和item绑定到一块
        convert(adapterHelper, item);
        return adapterHelper.getView();
    }

    private View createBottomProgressBar(View convertView) {
        if (convertView == null) {
            FrameLayout container = new FrameLayout(mContext);
            container.setForegroundGravity(Gravity.CENTER);
            LoadingProgressBar progressBar = new LoadingProgressBar(mContext);
            container.addView(progressBar);
            convertView = container;
        }
        return convertView;
    }

    /**************添加一些公共的方法*******************/
    public void add(T elem)
    {
        mData.add(elem);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem)
    {
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem)
    {
        set(mData.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem)
    {
        mData.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem)
    {
        mData.remove(elem);
        notifyDataSetChanged();
    }

    public void remove(int index)
    {
        mData.remove(index);
        notifyDataSetChanged();
    }

    public void replaceAll(List<T> elem)
    {
        mData.clear();
        mData.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem)
    {
        return mData.contains(elem);
    }

    /** Clear data list */
    public void clear()
    {
        mData.clear();
        notifyDataSetChanged();
    }

    public void showIndeterminateProgress(boolean display)
    {
        if (display == mShowBottomProgressBar)
            return;
        mShowBottomProgressBar = display;
        notifyDataSetChanged();
    }

    /**
     * 将数据和adapter辅助类绑定到一块
     *
     * @param adapterHelper
     * @param item
     */
    protected abstract void convert(H adapterHelper, T item);

    /**
     * 获取到Adapter辅助类
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    protected abstract H getHHLAdapterHelper(int position, View convertView, ViewGroup parent);
}
