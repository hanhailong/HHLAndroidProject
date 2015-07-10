package com.hhl.hhlcommonlibs.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import static com.hhl.hhlcommonlibs.adapter.HHLBaseAdapterHelper.get;

/**
 * Created by hailonghan on 15/7/10.
 */
public abstract class HHLQuickAdapter<T> extends HHLBaseAdapter<T,HHLBaseAdapterHelper>{

    protected HHLQuickAdapter(Context context,@LayoutRes int layoutId){
        super(context,layoutId);
    }

    protected HHLQuickAdapter(Context mContext,@LayoutRes int layoutId, List<T> data) {
        super(mContext,layoutId,data);
    }

    protected HHLQuickAdapter(Context context,MultiItemTypeSupport<T> multiItemTypeSupport){
        super(context,multiItemTypeSupport);
    }

    protected HHLQuickAdapter(Context context,List<T> data,MultiItemTypeSupport<T> multiItemTypeSupport){
        super(context,data,multiItemTypeSupport);
    }

    @Override
    protected HHLBaseAdapterHelper getHHLAdapterHelper(int position, View convertView, ViewGroup parent) {
        if (mMultiItemTypeSupport != null){
            return get(mContext,position,convertView,parent,mMultiItemTypeSupport.getLayoutId(position,mData.get(position)));
        }
        return get(mContext, position, convertView, parent,mLayoutId);
    }
}
