package com.willpower.touch.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.willpower.touch.R;

/**
 * Created by Administrator on 2017/9/27.
 */

public abstract class BaseImageView extends AppCompatImageView implements GestureDetector.OnGestureListener {

    //-------------------------------------------constant--------------------------------------//
    /**
     * click
     */
    protected static final int DEFAULT_SELECT_COLOR = Color.LTGRAY;
    protected static final int DEFAULT_NORMAL_COLOR = Color.TRANSPARENT;
    protected static final int DEFAULT_COVER_COLOR = Color.parseColor("#60FFFFFF");
    //clickModel
    protected static final int CLICK_MODEL_CHANGE = 1;
    protected static final int CLICK_MODEL_COVER = 2;
    /**
     * Shadow
     */
    protected static final int DEFAULT_SHADOW_COLOR = Color.DKGRAY;
    protected static final float DEFAULT_SHADOW_RADIOS = 3;
    protected static final boolean DEFAULT_NO_SHADOW = false;
    protected static final float DEFAULT_SHADOW_OFFSET_X = 0;
    protected static final float DEFAULT_SHADOW_OFFSET_Y = 0;
    /**
     * Ripple
     */
    protected static final long DEFAULT_RIPPLE_DURATION = 500;
    protected static final int DEFAULT_RIPPLE_COLOR = Color.parseColor("#ffffff");
    protected static final int DEFAULT_RIPPLE_ALPHA = 50;
    protected static final boolean DEFAULT_NO_RIPPLE = false;
    protected static final int POSITION_DOWN = 1;
    protected static final int POSITION_UP = 2;
    /**
     * Stroke
     */
    protected static final float DEFAULT_STROKE_WIDTH = 0;
    protected static final int DEFAULT_STROKE_COLOR = Color.parseColor("#ffffff");
    //-----------------------------------------------------variable-------------------------------------------
    /**
     * click
     */
    protected int selectColor, normalColor, coverColor, clickModel;
    protected boolean isTouching;
    /**
     * shadow
     */
    protected int shadowColor;
    protected float shadowRadios;
    protected boolean appNoShadow;
    protected float shadowOffsetX, shadowOffsetY;
    /**
     * Ripple
     */
    protected long rippleDuration;
    protected int rippleColor, rippleAlpha;
    protected boolean appNoRipple;
    protected int ripplePosition;
    protected boolean isDrawingRipple;
    //inside
    protected float rippleRadios;
    protected float rippleX;
    protected float rippleY;
    /**
     * Stroke
     */
    protected float strokeWidth;
    protected int strokeColor;
    /* Fillet(圆角)
*/
    protected float filletX;
    protected float filletY;

    /**
     * 监听手势
     */
    protected GestureDetector mGestureDetector;

    /**
     * params
     */
    protected int viewWidth;
    protected int viewHeight;


    public BaseImageView(Context context) {
        super(context);
    }

    public BaseImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
    }

    public BaseImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseImageView);
        selectColor = ta.getColor(R.styleable.BaseImageView_imageSelectColor, DEFAULT_SELECT_COLOR);
        normalColor = ta.getColor(R.styleable.BaseImageView_imageNormalColor, DEFAULT_NORMAL_COLOR);
        coverColor = ta.getColor(R.styleable.BaseImageView_imageCoverColor, DEFAULT_COVER_COLOR);
        clickModel = ta.getInt(R.styleable.BaseImageView_imageClickModel, CLICK_MODEL_COVER);
        shadowColor = ta.getColor(R.styleable.BaseImageView_imageShadowColor, DEFAULT_SHADOW_COLOR);
        shadowRadios = ta.getFloat(R.styleable.BaseImageView_imageShadowRadios, DEFAULT_SHADOW_RADIOS);
        appNoShadow = ta.getBoolean(R.styleable.BaseImageView_imageNoShadow, DEFAULT_NO_SHADOW);
        shadowOffsetX = ta.getFloat(R.styleable.BaseImageView_imageShadowOffsetX, DEFAULT_SHADOW_OFFSET_X);
        shadowOffsetY = ta.getFloat(R.styleable.BaseImageView_imageShadowOffsetY, DEFAULT_SHADOW_OFFSET_Y);
        rippleDuration = DEFAULT_RIPPLE_DURATION;
        rippleColor = ta.getColor(R.styleable.BaseImageView_imageRippleColor, DEFAULT_RIPPLE_COLOR);
        rippleAlpha = ta.getInt(R.styleable.BaseImageView_imageRippleAlpha, DEFAULT_RIPPLE_ALPHA);
        appNoRipple = ta.getBoolean(R.styleable.BaseImageView_imageNoRipple, DEFAULT_NO_RIPPLE);
        ripplePosition = ta.getInt(R.styleable.BaseImageView_imageRipplePosition, POSITION_DOWN);
        strokeWidth = ta.getFloat(R.styleable.BaseImageView_imageStrokeWidth, DEFAULT_STROKE_WIDTH);
        strokeColor = ta.getColor(R.styleable.BaseImageView_imageStrokeColor, DEFAULT_STROKE_COLOR);
        ta.recycle();
        setBackgroundColor(Color.TRANSPARENT);
        isTouching = false;
        isDrawingRipple = false;
    }


    protected abstract void drawStrokeAndFill(Canvas canvas, RectF rectF);

    protected abstract void drawFill(Canvas canvas, RectF rectF);

    protected abstract void drawCover(Canvas canvas, RectF rectF);

    protected abstract void drawRippleWithAnim(Canvas canvas);

    protected abstract void startRippleAnim();


    //********************************************************TouchEvent********************************************//
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP) {
            setTouching(false);
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        setTouching(true);
        if (!appNoRipple) {
            rippleX = e.getX();
            rippleY = e.getY();
            if (ripplePosition == POSITION_DOWN) {
                startRippleAnim();
            }
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (!appNoRipple && ripplePosition == POSITION_UP) {
            startRippleAnim();
        }
        setTouching(false);
        performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长安时，手动触发长安事件
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Auto-generated method stub
        return false;
    }
    //_______________________________________________________set_____________________________________
    protected void setTouching(boolean isTouching) {
        this.isTouching = isTouching;
        postInvalidate();
    }
    public void setSelectColor(int selectColor) {
        this.selectColor = selectColor;
        postInvalidate();
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
        postInvalidate();
    }

    public void setCoverColor(int coverColor) {
        this.coverColor = coverColor;
        postInvalidate();
    }

    public void setClickModel(int clickModel) {
        this.clickModel = clickModel;
        postInvalidate();
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        postInvalidate();
    }

    public void setShadowRadios(float shadowRadios) {
        this.shadowRadios = shadowRadios;
        postInvalidate();
    }

    public void setAppNoShadow(boolean appNoShadow) {
        this.appNoShadow = appNoShadow;
        postInvalidate();
    }

    public void setShadowOffsetX(float shadowOffsetX) {
        this.shadowOffsetX = shadowOffsetX;
        postInvalidate();
    }

    public void setShadowOffsetY(float shadowOffsetY) {
        this.shadowOffsetY = shadowOffsetY;
        postInvalidate();
    }

    public void setRippleDuration(long rippleDuration) {
        this.rippleDuration = rippleDuration;
        postInvalidate();
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        postInvalidate();
    }

    public void setRippleAlpha(int rippleAlpha) {
        this.rippleAlpha = rippleAlpha;
        postInvalidate();
    }

    public void setAppNoRipple(boolean appNoRipple) {
        this.appNoRipple = appNoRipple;
        postInvalidate();
    }

    public void setRipplePosition(int ripplePosition) {
        this.ripplePosition = ripplePosition;
        postInvalidate();
    }

    public void setDrawingRipple(boolean drawingRipple) {
        isDrawingRipple = drawingRipple;
        postInvalidate();
    }

    public void setRippleRadios(float rippleRadios) {
        this.rippleRadios = rippleRadios;
        postInvalidate();
    }

    public void setRippleX(float rippleX) {
        this.rippleX = rippleX;
        postInvalidate();
    }

    public void setRippleY(float rippleY) {
        this.rippleY = rippleY;
        postInvalidate();
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        postInvalidate();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        postInvalidate();
    }

    public void setFilletX(float filletX) {
        this.filletX = filletX;
        postInvalidate();
    }

    public void setFilletY(float filletY) {
        this.filletY = filletY;
        postInvalidate();
    }
}
