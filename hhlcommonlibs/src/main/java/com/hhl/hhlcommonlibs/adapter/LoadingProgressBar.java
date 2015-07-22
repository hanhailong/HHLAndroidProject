package com.hhl.hhlcommonlibs.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 目前只支持代码设置属性
 * Created by HanHailong on 15/7/21.
 */
public class LoadingProgressBar extends View {

    private Paint mPaint1, mPaint2;

    private int mColor1 = Color.RED;
    private int mColor2 = Color.YELLOW;

    private int mCenterX1;
    private int mCenterX2;

    private float maxRadius = 15;
    private float minRadius = 5;
    private float mRadius1 = maxRadius;
    private float mRadius2 = minRadius;

    private int distance = 30;

    private boolean isRight1 = true;
    private boolean isRight2 = !isRight1;

    public LoadingProgressBar(Context context) {
        super(context);
        init(context);
    }


    public LoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LoadingProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public LoadingProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(mColor1);

        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(mColor2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX1 = getWidth() / 2;
        mCenterX2 = getWidth() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        if (isRight1) {
            if (mCenterX1 < (getWidth() / 2 + distance)) {
                mCenterX1++;
            } else {
                isRight1 = false;
                isRight2 = !isRight1;
            }
        } else {
            if (mCenterX1 > (getWidth() / 2 - distance)) {
                mCenterX1--;
            } else {
                isRight1 = true;
                isRight2 = !isRight1;
            }
        }
        calculateRadius1(isRight1);

        if (isRight2) {
            if (mCenterX2 < (getWidth() / 2 + distance)) {
                mCenterX2++;
            }
        } else {
            if (mCenterX2 > (getWidth() / 2 - distance)) {
                mCenterX2--;
            }
        }
        calculateRadius2(isRight2);

        if (isRight1){
            //画两个点
            canvas.drawCircle(mCenterX2, getHeight() / 2, mRadius2, mPaint2);
            canvas.drawCircle(mCenterX1, getHeight() / 2, mRadius1, mPaint1);
        }else{
            //画两个点
            canvas.drawCircle(mCenterX1, getHeight() / 2, mRadius1, mPaint1);
            canvas.drawCircle(mCenterX2, getHeight() / 2, mRadius2, mPaint2);
        }

        //不停的话
        postInvalidate();
    }

    private void calculateRadius1(boolean isRight) {
        if (isRight) {//向右
            float rate = (maxRadius - minRadius) / (2 * distance);
            mRadius1 = maxRadius - Math.abs(mCenterX1 - getWidth() / 2) * rate;
        } else {//向左
            float rate = (maxRadius - minRadius) / (2 * distance);
            mRadius1 = minRadius + Math.abs(mCenterX1 - getWidth() / 2) * rate;
        }
    }

    private void calculateRadius2(boolean isRight) {
        if (isRight) {//向右
            float rate = (maxRadius - minRadius) / (2 * distance);
            mRadius2 = maxRadius - Math.abs(mCenterX2 - getWidth() / 2) * rate;
        } else {//向左
            float rate = (maxRadius - minRadius) / (2 * distance);
            mRadius2 = minRadius + Math.abs(mCenterX2 - getWidth() / 2) * rate;
        }
    }
}
