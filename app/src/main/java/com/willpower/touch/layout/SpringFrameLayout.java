package com.willpower.touch.layout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.willpower.touch.R;
import com.willpower.touch.utils.AnimUtils;

import static com.willpower.touch.utils.AnimUtils.DEFAULT_DURATION;

/**
 * Created by Administrator on 2017/7/21.
 */

public class SpringFrameLayout extends FrameLayout implements GestureDetector.OnGestureListener {
    /**
     * 监听手势
     */
    private GestureDetector mGestureDetector;

    boolean scaleModel;

    private float scale = 1f;

    @ColorInt
    private int[] backgroundColors;

    private float radio;

    private int width;

    private int height;

    /**
     * 点击波纹
     */
    private boolean isDrawRipple;

    private boolean isDrawingRipple;

    private float rippleRadios;

    private float rippleX;

    private float rippleY;

    private int viewAlpha;

    private int viewRippleColor;

    private int animDuration;


    public SpringFrameLayout(@NonNull Context context) {
        super(context);
    }

    public SpringFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
        isDrawRipple = true;
        setBackgroundColor(Color.TRANSPARENT);
        setWillNotDraw(false);//设置调用OnDraw
        mGestureDetector = new GestureDetector(context, this);
    }

    public SpringFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpringFrameLayout);
        // : 设置背景颜色
        if (ta.getInteger(R.styleable.SpringFrameLayout_backgroundColor, -1) == -1) {
            backgroundColors = new int[]{ta.getInteger(R.styleable.SpringFrameLayout_startColor, R.color.colorAccent), ta.getInteger(R.styleable.SpringFrameLayout_endColor, R.color.colorAccent)};
        } else {
            backgroundColors = new int[]{ta.getInteger(R.styleable.SpringFrameLayout_backgroundColor, R.color.colorAccent)};
        }
        // : 设置角度
        radio = ta.getFloat(R.styleable.SpringFrameLayout_rectRadios, 0f);
        // 设置点击缩放
        scaleModel = ta.getBoolean(R.styleable.SpringFrameLayout_scaleModel,true);
        isDrawRipple = ta.getBoolean(R.styleable.SpringFrameLayout_isDrawRipple, true);
        viewAlpha = ta.getInt(R.styleable.SpringFrameLayout_viewAlpha, 70);
        viewRippleColor = ta.getInteger(R.styleable.SpringFrameLayout_viewRippleColor, Color.WHITE);
        animDuration = ta.getInteger(R.styleable.SpringFrameLayout_animDuration,DEFAULT_DURATION);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setShader(new LinearGradient(0f, (float) height / 2, 0f, (float) height,backgroundColors, null, Shader.TileMode.CLAMP));
        paint.setStyle(Paint.Style.FILL);
        RectF rectF = new RectF(width * (1f - scale), height * (1f - scale), width * scale, height * scale);
        canvas.drawRoundRect(rectF, radio, radio, paint);
        if (isDrawingRipple) {//绘制波纹
            RectF rectR = new RectF(rippleX - rippleRadios, rippleY - rippleRadios, rippleX + rippleRadios, rippleY + rippleRadios);
            Paint rippleP = new Paint(Paint.ANTI_ALIAS_FLAG);
            rippleP.setColor(viewRippleColor);
            rippleP.setAlpha(viewAlpha);
            canvas.drawOval(rectR, rippleP);
        }
    }

    /**
     * 波纹动画
     */
    private void startRippleAnim() {
        isDrawingRipple = true;//开始绘制波纹的标识
        AnimUtils.rippleAnim(width, rippleX, animDuration, new AnimUtils.OnRippleAnimListener() {
            @Override
            public void onAnimUpdate(float value, float progress) {
                rippleRadios = value;
                postInvalidate();
            }
            @Override
            public void onAnimFinish() {
                isDrawingRipple = false;
                postInvalidate();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (scaleModel){
                setScale(1f);
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (scaleModel){
            setScale(0.98f);
            if (isDrawRipple) {
                rippleX = e.getX();
                rippleY = e.getY();
                startRippleAnim();
            }
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (scaleModel){
            setScale(1f);
        }
        performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }


    public void setScale(float scale) {
        this.scale = scale;
        postInvalidate();
    }


    public void setScaleModel(boolean scaleModel) {
        this.scaleModel = scaleModel;
    }
}
