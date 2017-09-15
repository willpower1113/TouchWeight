package com.willpower.touch.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.LinearInterpolator;

/**
 * Created by Administrator on 2017/9/15.
 */

public class AnimUtils {

    public final static int DEFAULT_DURATION = 600;


    /*** View波纹动画 start **********************************/
    /**
     * @param viewSize 控件的长度
     * @param rippleX  手指点击的x坐标
     * @param duration 动画时间
     * @param listener 动画监听
     */
    public static void rippleAnim(int viewSize, float rippleX, int duration, final OnRippleAnimListener listener) {
        float maxLength;
        if (rippleX > viewSize / 2) {
            maxLength = rippleX;
        } else {
            maxLength = viewSize - rippleX;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, maxLength);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (listener != null) {
                    listener.onAnimUpdate((Float) animation.getAnimatedValue(), animation.getAnimatedFraction());
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimFinish();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        valueAnimator.start();
    }

    public interface OnRippleAnimListener {
        void onAnimUpdate(float value, float progress);

        void onAnimFinish();
    }
    /*** View波纹动画 end **********************************/
}
