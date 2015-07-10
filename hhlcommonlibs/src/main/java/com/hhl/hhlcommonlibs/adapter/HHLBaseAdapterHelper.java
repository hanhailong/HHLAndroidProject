package com.hhl.hhlcommonlibs.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 万能适配器辅助类
 * Created by hailonghan on 15/7/10.
 */
public class HHLBaseAdapterHelper {

    //缓存View的
    private final SparseArray<View> mViews;

    //上下文
    private final Context mContext;

    //item
    private View mConvertView;

    //layoutId
    public  @LayoutRes int layoutId;

    //当前的position
    private int mPosition;

    protected HHLBaseAdapterHelper(Context context,int position,ViewGroup parent,@LayoutRes int layoutId){
        mViews = new SparseArray<View>();
        this.mContext = context;
        this.mPosition = position;
        this.layoutId = layoutId;
        mConvertView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        //设置TAG
        mConvertView.setTag(this);
    }

    static HHLBaseAdapterHelper get(Context context, int position, View convertView, ViewGroup parent,@LayoutRes int layoutId){
        if (convertView == null){
            return new HHLBaseAdapterHelper(context,position,parent,layoutId);
        }

        HHLBaseAdapterHelper helper = (HHLBaseAdapterHelper) convertView.getTag();

        if (helper.layoutId != layoutId){
            return new HHLBaseAdapterHelper(context,position,parent,layoutId);
        }

        //这里必须设置
        helper.mPosition = position;

        return helper;
    }

    /**
     * 获取convertView
     * @return
     */
    public View getView(){
        return mConvertView;
    }

    public int getPosition(){
        return mPosition;
    }

    public <T extends View> T getView(int viewId){
        return reusedView(viewId);
    }

    private <T extends View> T reusedView(int viewId) {
        View view = mViews.get(viewId);

        if (view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId,view);
        }

        return (T) view;
    }

    /******************提供辅助类，链式语法，写起来更容易***********************/

    public HHLBaseAdapterHelper setText(int viewId,String value){
        TextView textView = reusedView(viewId);
        textView.setText(value);
        return this;
    }

    public HHLBaseAdapterHelper setText(int viewId,@StringRes int value){
        TextView textView = reusedView(viewId);
        textView.setText(value);
        return this;
    }

    //TODO ，可以自己根据的需求扩展


}
