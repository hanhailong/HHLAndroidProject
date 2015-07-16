package com.hhl.hhlandroidproject.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Point;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by HanHailong on 15/7/16.
 */
public class HHLViewDragLayout extends LinearLayout {

    private ViewDragHelper mViewDragHelper;

    private TextView mDragView;
    private TextView mAutoBackView;
    private TextView mEdgeView;

    private Point mOriginPoint = new Point();

    public HHLViewDragLayout(Context context) {
        super(context);
        init(context);
    }

    public HHLViewDragLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HHLViewDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public HHLViewDragLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public boolean tryCaptureView(View child, int pointerId) {
                return true;
            }

            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {

                int leftBound = getPaddingLeft();
                int rightBound = getWidth() - child.getWidth() - getPaddingRight();

                int newLeft = Math.min(Math.max(left,leftBound),rightBound);

                return newLeft;
            }

            @Override
            public int clampViewPositionVertical(View child, int top, int dy) {
                int topBound = getPaddingTop();
                int bottomBound = getHeight() - child.getHeight() - getPaddingBottom();
                int newTop = Math.min(Math.max(top,topBound),bottomBound);
                return newTop;
            }

            //手指释放时回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                if (releasedChild == mAutoBackView){
                    mViewDragHelper.settleCapturedViewAt(mOriginPoint.x,mOriginPoint.y);
                    invalidate();
                }
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                mViewDragHelper.captureChildView(mEdgeView,pointerId);
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                return super.getViewHorizontalDragRange(child);
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mViewDragHelper.continueSettling(true)){
            invalidate();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mDragView = (TextView) getChildAt(0);
        mAutoBackView = (TextView) getChildAt(1);
        mEdgeView = (TextView) getChildAt(2);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        mOriginPoint.x = mAutoBackView.getLeft();
        mOriginPoint.y = mAutoBackView.getTop();
    }
}
