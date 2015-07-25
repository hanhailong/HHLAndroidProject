package com.hhl.hhlandroidproject.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by HanHailong on 15/7/25.
 */
public class BallCycleDrawable extends Drawable implements Drawable.Callback {

    private static final String TAG = BallCycleDrawable.class.getSimpleName();
    // constants
    private static final int MAX_LEVEL = 10000;
    private static final int CENT_LEVEL = MAX_LEVEL / 2;
    private static final int MID_LEVEL = CENT_LEVEL / 2;
    private static final int ALPHA_OPAQUE = 255;
    private static final int ACCELERATION_LEVEL = 2;

    private final static int DEFAULT_MAX_RADIUS = 15;
    private final static int DEFAULT_MIN_RADIUS = 5;

    //球的最大半径
    private float maxRadius = DEFAULT_MAX_RADIUS;
    //球的最小半径
    private float minRadius = DEFAULT_MIN_RADIUS;

    private int acceleration = ACCELERATION_LEVEL;
    private double distance = 0.5 * ACCELERATION_LEVEL * MID_LEVEL * MID_LEVEL;
    private double max_speed; // set in setAcceleration(...);

    private Paint mPaint1;
    private Paint mPaint2;

    private Ball mOneBall;
    private Ball mTwoBall;

    private float mCenterX;
    private float mCenterY;

    private float mWidth;

    private float unit;

    public BallCycleDrawable(int[] colors) {
        mOneBall = new Ball();
        mTwoBall = new Ball();

        initColors(colors);
    }

    private void initColors(int[] colors) {
        // red circle, left up
        mPaint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint1.setColor(colors[0]);
        mPaint1.setAntiAlias(true);
        mOneBall.setColor(colors[0]);

        // blue circle, right down
        mPaint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint2.setColor(colors[1]);
        mPaint2.setAntiAlias(true);
        mTwoBall.setColor(colors[1]);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mOneBall.getRadius() > mTwoBall.getRadius()) {
            canvas.drawCircle(mTwoBall.getCenterX(), mCenterY, mTwoBall.getRadius(), mPaint2);
            canvas.drawCircle(mOneBall.getCenterX(), mCenterY, mOneBall.getRadius(), mPaint1);
        } else {
            canvas.drawCircle(mOneBall.getCenterX(), mCenterY, mOneBall.getRadius(), mPaint1);
            canvas.drawCircle(mTwoBall.getCenterX(), mCenterY, mTwoBall.getRadius(), mPaint2);

        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint1.setAlpha(alpha);
        mPaint2.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint1.setColorFilter(cf);
        mPaint2.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    protected boolean onLevelChange(int level) {

        oneBallChange(level);

        return true;
    }

    private void oneBallChange(int level) {

        level %= MAX_LEVEL / acceleration;

        float centerRadius = (maxRadius + minRadius) * 0.5f;

        if (level < 2500) {
            float oneCenterX = unit;
            float twoCenterX = mWidth - unit;
            float rate = level * 1.0f / 2500;

            oneCenterX += rate * (mWidth - 2 * unit);
            twoCenterX -= rate * (mWidth - 2 * unit);

            mOneBall.setCenterX(oneCenterX);
            mTwoBall.setCenterX(twoCenterX);

            //半径
            if (level < 1250) {
                float oneRadius = centerRadius;
                float twoRadius = centerRadius;

                float rr = level * 1.0f / 1250;
                oneRadius += rr * (maxRadius - centerRadius);
                twoRadius -= rr * (centerRadius - minRadius);

                mOneBall.setRadius(oneRadius);
                mTwoBall.setRadius(twoRadius);
            } else {
                float oneRadius = maxRadius;
                float twoRadius = minRadius;
                float rr = (level - 1250) * 1.0f / 1250;
                oneRadius -= rr * (maxRadius - centerRadius);
                twoRadius += rr * (centerRadius - minRadius);
                mOneBall.setRadius(oneRadius);
                mTwoBall.setRadius(twoRadius);
            }
        } else {
            float oneCenterX = mWidth - unit;
            float twoCenterX = unit;
            float rate = (level - 2500) * 1.0f / 2500;
            oneCenterX -= rate * (mWidth - 2 * unit);
            twoCenterX += rate * (mWidth - 2 * unit);
            mOneBall.setCenterX(oneCenterX);
            mTwoBall.setCenterX(twoCenterX);

            //半径
            if (level < 3750) {//5000-7500
                float oneRadius = centerRadius;
                float twoRadius = centerRadius;

                float rr = (level - 2500) * 1.0f / 1250;
                oneRadius -= rr * (centerRadius - minRadius);
                twoRadius += rr * (maxRadius - centerRadius);


                mOneBall.setRadius(oneRadius);
                mTwoBall.setRadius(twoRadius);
            } else {
                float oneRadius = minRadius;
                float twoRadius = maxRadius;
                float rr = (level - 3750) * 1.0f / 1250;
                oneRadius += rr * (centerRadius - minRadius);
                twoRadius -= rr * (maxRadius - centerRadius);
                mOneBall.setRadius(oneRadius);
                mTwoBall.setRadius(twoRadius);
            }
        }

    }

    @Override
    public void onBoundsChange(Rect bounds) {
        super.setBounds(bounds);
        measureBallProgress(bounds.width(), bounds.height());
        mCenterX = bounds.centerX();
        mCenterY = bounds.centerY();
    }

    /**
     * 测量球的宽度和高度
     *
     * @param width
     * @param height
     */
    private void measureBallProgress(int width, int height) {
        mWidth = width;
        //TODO
        unit = width / 7;

        mOneBall.setCenterX(unit);
        mOneBall.setRadius(unit);

        mTwoBall.setCenterX(width - unit);
        mTwoBall.setRadius(unit);
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
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

    public static class Builder {
        private int[] mColors;

        public Builder(Context context) {
            initDefaults(context);
            return;
        }

        private void initDefaults(Context context) {
            //Default values
            mColors = new int[]{Color.parseColor("#ffdf3e"), Color.parseColor("#40df73")};
            return;
        }

        public Builder colors(int[] colors) {
            if (colors == null || colors.length == 0) {
                throw new IllegalArgumentException("Your color array must contains at least 4 values");
            }

            mColors = colors;
            return this;
        }

        public Drawable build() {
            return new BallCycleDrawable(mColors);
        }
    }

}
