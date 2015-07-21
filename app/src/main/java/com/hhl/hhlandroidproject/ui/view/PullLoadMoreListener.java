package com.hhl.hhlandroidproject.ui.view;

import android.widget.AbsListView;

/**
 * Created by HanHailong on 15/7/21.
 */
public abstract class PullLoadMoreListener implements AbsListView.OnScrollListener{

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE){
            //滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)){
                onLoadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
    }

    public abstract void onLoadMore();
}
