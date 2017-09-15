package com.willpower.touch.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.willpower.touch.R;
import com.willpower.touch.utils.AnimUtils;

import static com.willpower.touch.utils.AnimUtils.DEFAULT_DURATION;


/**
 * Created by Administrator on 2017/9/1.
 */

public class ShadowRoundImageView extends AppCompatImageView implements GestureDetector.OnGestureListener {
    private static final int DEFAULT_DARK_SIZE = 3;
    private static final int DEFAULT_DARK_COLOR = Color.parseColor("#cccccc");
    private static final int FLAG_SELECT = 1;
    private static final int FLAG_NORMAL = 0;
    /**
     * oval
     */
    private int colorNormal;//背景色
    private int colorSelector;
    /**
     * 阴影
     */
    private int colorShadow;//阴影色
    private boolean hasShadow;//默认false

    private int clickFlag;//点击标识

    /**
     * 波纹效果
     */
    private boolean SRIIsDrawRipple;

    private int SRIViewAlpha;

    private int SRIViewRippleColor;

    private boolean isDrawingRipple;

    private float rippleRadios;

    private float rippleX;

    private float rippleY;

    private int SRIDuration;

    private int width;

    private int height;

    /**
     * 监听手势
     */
    private GestureDetector mGestureDetector;

    public ShadowRoundImageView(Context context) {
        super(context);
    }

    public ShadowRoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
    }

    public ShadowRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowRoundImageView);
        hasShadow = ta.getBoolean(R.styleable.ShadowRoundImageView_hasShadow, false);
        colorShadow = ta.getColor(R.styleable.ShadowRoundImageView_colorShadow, DEFAULT_DARK_COLOR);
        colorNormal = ta.getColor(R.styleable.ShadowRoundImageView_colorNormal, Color.WHITE);
        colorSelector = ta.getColor(R.styleable.ShadowRoundImageView_colorSelector, Color.LTGRAY);
        SRIIsDrawRipple = ta.getBoolean(R.styleable.ShadowRoundImageView_SRIIsDrawRipple, false);
        SRIViewAlpha = ta.getInt(R.styleable.ShadowRoundImageView_SRIViewAlpha, 70);
        SRIViewRippleColor = ta.getInteger(R.styleable.ShadowRoundImageView_SRIViewRippleColor, Color.WHITE);
        SRIDuration = ta.getInteger(R.styleable.ShadowRoundImageView_SRIDuration,DEFAULT_DURATION);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setBackgroundColor(Color.TRANSPARENT);
        width = getWidth();
        height = getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if (hasShadow) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
            paint.setShadowLayer(DEFAULT_DARK_SIZE, 0, 0, colorShadow);
        }
        if (clickFlag == FLAG_NORMAL) {
            paint.setColor(colorNormal);
        } else {
            paint.setColor(colorSelector);
        }
        RectF rectF = new RectF(DEFAULT_DARK_SIZE, DEFAULT_DARK_SIZE, width - DEFAULT_DARK_SIZE, height - DEFAULT_DARK_SIZE);
        canvas.drawOval(rectF, paint);
        super.onDraw(canvas);
        if (isDrawingRipple) {//绘制波纹
            RectF rectRect = new RectF(rippleX - rippleRadios, rippleY - rippleRadios, rippleX + rippleRadios, rippleY + rippleRadios);
            Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ripplePaint.setColor(SRIViewRippleColor);
            ripplePaint.setAlpha(SRIViewAlpha);
            canvas.drawOval(rectRect, ripplePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP) {
            setClickFlag(FLAG_NORMAL);
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        setClickFlag(FLAG_SELECT);
        if (SRIIsDrawRipple) {
            rippleX = e.getX();
            rippleY = e.getY();
            startRippleAnim();
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        setClickFlag(FLAG_NORMAL);
        performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //  Auto-generated method stub
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长安时，手动触发长安事件
        performLongClick();

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //  Auto-generated method stub
        return false;
    }


    private void setClickFlag(int clickFlag) {
        this.clickFlag = clickFlag;
        postInvalidate();
    }

    /**
     * 波纹动画
     */
    private void startRippleAnim() {
        isDrawingRipple = true;//开始绘制波纹的标识
        AnimUtils.rippleAnim(width, rippleX, SRIDuration, new AnimUtils.OnRippleAnimListener() {
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

    public void setColorNormal(int colorNormal) {
        this.colorNormal = colorNormal;
        postInvalidate();
    }

    public void setColorSelector(int colorSelector) {
        this.colorSelector = colorSelector;
        postInvalidate();
    }

    public void setColorShadow(int colorShadow) {
        this.colorShadow = colorShadow;
        postInvalidate();
    }

    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
        postInvalidate();
    }
}
