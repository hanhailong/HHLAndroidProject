package com.hhl.hhlandroidproject.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;

/**
 * 可翻转的圆形卡片
 * Created by HanHailong on 15/7/17.
 */
public class FlipCardView extends RelativeLayout implements View.OnClickListener{

    private CustomFlipViewGroup mViewAboveGroup;
    private CustomFlipViewGroup mViewBehindGroup;

    private View mAboveView;
    private View mBehindView;

    /**
     * 卡片是否正面朝上，默认是打开的
     */
    public boolean isOpened = true;

    /**
     * 卡片是否打开，
     */
    public interface OnFlipOpenedListener{
        void onOpened();
    }

    /**
     * 卡片是否关闭
     */
    public interface OnFlipClosedListener{
        void onClosed();
    }

    public FlipCardView(Context context) {
        super(context);
        init(context);
    }


    public FlipCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FlipCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(21)
    public FlipCardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        LayoutParams behindParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mViewBehindGroup = new CustomFlipViewGroup(context);
        addView(mViewBehindGroup,behindParams);

        LayoutParams aboveParams = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        mViewAboveGroup = new CustomFlipViewGroup(context);
        addView(mViewAboveGroup, aboveParams);

        mViewBehindGroup.setVisibility(View.GONE);
    }

    public void setAboveView(@LayoutRes int res){
        setAboveView(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setAboveView(View v){
        if (mAboveView != null) mViewAboveGroup.removeView(mAboveView);
        mAboveView = v;
        mViewAboveGroup.addView(mAboveView,new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mViewAboveGroup.setOnClickListener(this);
    }

    public void setBehindView(@LayoutRes int res){
        setBehindView(LayoutInflater.from(getContext()).inflate(res, null));
    }

    public void setBehindView(View v){
        if (mBehindView != null) mViewBehindGroup.removeView(mBehindView);
        mBehindView = v;
        mViewBehindGroup.addView(mBehindView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mViewBehindGroup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startFlipCard();
    }

    /**
     * 开始进行卡片翻转
     */
    public void startFlipCard(){

        if (mBehindView == null || mAboveView == null) return;

        if (isOpened){//如果是打开的，就执行关闭翻转
            animateFlip(mViewAboveGroup,mViewBehindGroup);
        }else {//如果是关着的，就执行打开翻转
            animateFlip(mViewBehindGroup,mViewAboveGroup);
        }
    }

    private void animateFlip(final View aboveView, final View behindView){

        aboveView.setVisibility(View.VISIBLE);
        behindView.setVisibility(View.GONE);

        ObjectAnimator flipAboveAnimator = ObjectAnimator.ofFloat(aboveView,"rotationY",0f,90f);
        final ObjectAnimator flipBehindAnimator = ObjectAnimator.ofFloat(behindView,"rotationY",-90f,0f);
        flipBehindAnimator.setInterpolator(new DecelerateInterpolator());
        flipBehindAnimator.setDuration(500);

        flipAboveAnimator.setInterpolator(new AccelerateInterpolator());
        flipAboveAnimator.setDuration(500);
        flipAboveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                aboveView.setVisibility(View.GONE);
                behindView.setVisibility(View.VISIBLE);
                flipBehindAnimator.start();
                isOpened = !isOpened;
            }
        });
        flipAboveAnimator.start();
    }

    private class CustomFlipViewGroup extends FrameLayout{

        public CustomFlipViewGroup(Context context) {
            super(context);
        }

        public CustomFlipViewGroup(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public CustomFlipViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        @TargetApi(21)
        public CustomFlipViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }
    }
}
