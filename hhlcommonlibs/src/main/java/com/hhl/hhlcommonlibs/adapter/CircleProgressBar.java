package com.hhl.hhlcommonlibs.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 两个颜色的小球循环旋转，
 * 目前只支持代码设置颜色、最大半径、最小半径等属性
 * version 1.0
 * //TODO 2.0 添加xml文件支持属性
 * Created by HanHailong on 15/7/21.
 */
public class CircleProgressBar extends View {

    private final static int DEFAULT_MAX_RADIUS = 15;
    private final static int DEFAULT_MIN_RADIUS = 5;
    private final static int DEFAULT_DISTANCE = 20;

    private final static int DEFAULT_ONE_BALL_COLOR = Color.GREEN;
    private final static int DEFAULT_TWO_BALL_COLOR = Color.BLUE;

    private final static int DEFAULT_ANIMATOR_DURATION = 1200;

    //画笔
    private Paint mPaint;

    //球的最大半径
    private float maxRadius = DEFAULT_MAX_RADIUS;
    //球的最小半径
    private float minRadius = DEFAULT_MIN_RADIUS;

    //两球旋转的范围距离
    private int distance = DEFAULT_DISTANCE;

    //动画的时间
    private long duration = DEFAULT_ANIMATOR_DURATION;

    private Ball mOneBall;
    private Ball mTwoBall;

    private float mCenterX;
    private float mCenterY;

    private AnimatorSet animatorSet;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mOneBall = new Ball();
        mTwoBall = new Ball();

        mOneBall.setColor(DEFAULT_ONE_BALL_COLOR);
        mTwoBall.setColor(DEFAULT_TWO_BALL_COLOR);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        configAnimator();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 2;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画两个点
        if (mOneBall.getRadius() > mTwoBall.getRadius()) {
            mPaint.setColor(mTwoBall.getColor());
            canvas.drawCircle(mTwoBall.getCenterX(), mCenterY, mTwoBall.getRadius(), mPaint);

            mPaint.setColor(mOneBall.getColor());
            canvas.drawCircle(mOneBall.getCenterX(), mCenterY, mOneBall.getRadius(), mPaint);
        } else {
            mPaint.setColor(mOneBall.getColor());
            canvas.drawCircle(mOneBall.getCenterX(), mCenterY, mOneBall.getRadius(), mPaint);

            mPaint.setColor(mTwoBall.getColor());
            canvas.drawCircle(mTwoBall.getCenterX(), mCenterY, mTwoBall.getRadius(), mPaint);
        }
    }

    private void configAnimator() {

        float centerRadius = (maxRadius + minRadius) * 0.5f;

        ObjectAnimator redScaleAnimator = ObjectAnimator.ofFloat(mOneBall, "radius",
                centerRadius, maxRadius, centerRadius, minRadius, centerRadius);
        redScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ValueAnimator redCenterAnimator = ValueAnimator.ofFloat(-1, 0, 1, 0, -1);
        redCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        redCenterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float x = mCenterX + (distance) * value;
                mOneBall.setCenterX(x);
                invalidate();
            }
        });

        ObjectAnimator yellowScaleAnimator = ObjectAnimator.ofFloat(mTwoBall, "radius", centerRadius, minRadius,
                centerRadius, maxRadius, centerRadius);
        yellowScaleAnimator.setRepeatCount(ValueAnimator.INFINITE);

        ValueAnimator yellowCenterAnimator = ValueAnimator.ofFloat(1, 0, -1, 0, 1);
        yellowCenterAnimator.setRepeatCount(ValueAnimator.INFINITE);
        yellowCenterAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                float x = mCenterX + (distance) * value;
                mTwoBall.setCenterX(x);
            }
        });

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(redScaleAnimator, redCenterAnimator, yellowScaleAnimator, yellowCenterAnimator);
        animatorSet.setDuration(DEFAULT_ANIMATOR_DURATION);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
    }

    public class Ball {
        private float radius;//半径
        private float centerX;//圆心
        private int color;//颜色

        public float getRadius() {
            return radius;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public float getCenterX() {
            return centerX;
        }

        public void setCenterX(float centerX) {
            this.centerX = centerX;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);
            if (v == GONE || v == INVISIBLE) {
                stopAnimator();
            } else {
                startAnimator();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int v) {
        super.onVisibilityChanged(changedView, v);
        if (v == GONE || v == INVISIBLE) {
            stopAnimator();
        } else {
            startAnimator();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }

    /**
     * 设置第一个球的颜色
     *
     * @param color
     */
    public void setOneBallColor(@ColorInt int color) {
        mOneBall.setColor(color);
    }

    /**
     * 设置第二个球的颜色
     *
     * @param color
     */
    public void setmTwoBallColor(@ColorInt int color) {
        mTwoBall.setColor(color);
    }

    /**
     * 设置球的最大半径
     *
     * @param maxRadius
     */
    public void setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
        configAnimator();
    }

    /**
     * 设置球的最小半径
     *
     * @param minRadius
     */
    public void setMinRadius(float minRadius) {
        this.minRadius = minRadius;
        configAnimator();
    }

    /**
     * 设置两个球旋转的最大范围距离
     *
     * @param distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setDuration(long duration) {
        this.duration = duration;
        if (animatorSet != null) {
            animatorSet.setDuration(duration);
        }
    }

    /**
     * 开始动画
     */
    public void startAnimator() {
        if (getVisibility() != VISIBLE) return;

        if (animatorSet.isRunning()) return;

        if (animatorSet != null) {
            animatorSet.start();
        }
    }

    /**
     * 结束停止动画
     */
    public void stopAnimator() {
        if (animatorSet != null) {
            animatorSet.end();
        }
    }
}
