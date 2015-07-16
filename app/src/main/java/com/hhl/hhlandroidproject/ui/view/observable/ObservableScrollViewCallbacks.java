package com.hhl.hhlandroidproject.ui.view.observable;


public interface ObservableScrollViewCallbacks {
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging);

    public void onDownMotionEvent();

    public void onUpOrCancelMotionEvent(ScrollState scrollState);
}
