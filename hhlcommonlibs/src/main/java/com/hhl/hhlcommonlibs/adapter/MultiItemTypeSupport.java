package com.hhl.hhlcommonlibs.adapter;

/**
 * Created by hailonghan on 15/7/10.
 */
public interface MultiItemTypeSupport<T> {

    int getLayoutId(int position, T t);

    int getViewTypeCount();

    int getItemViewType(int position, T t);

}
