package com.willpower.touch.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.willpower.touch.R;
import com.willpower.touch.utils.AnimUtils;
import com.willpower.touch.utils.ScreenUtils;

import static android.view.Gravity.CENTER;
import static com.willpower.touch.utils.AnimUtils.DEFAULT_DURATION;

/**
 * Created by Administrator on 2017/8/23.
 */

public class AppButton extends AppCompatButton implements GestureDetector.OnGestureListener{
    private static final int DEFAULT_SELECTOR = Color.parseColor("#60000000");
    private static final int DEFAULT_NORMAL = Color.parseColor("#00000000");

    private static final int DEFAULT_SHADOW_COLOR = Color.parseColor("#99000000");

    private static final float DEFAULT_SHADOW_RADIO = 0;
    /**
     * shadow
     */
    private boolean drawShadow;
    private int shadowColor;
    private float shadowRadio;

    /**
     * touch_color
     */
    private int selector;
    private int normal;
    private int color_rect;

    /**
     * radio
     */
    private float radioX;
    private float radioY;

    /**
     * 监听手势
     */
    private GestureDetector mGestureDetector;

    /**
     * 波纹效果
     */
    private boolean isDrawRipple;

    private int viewAlpha;

    private int viewRippleColor;

    private boolean isDrawingRipple;

    private float rippleRadios;

    private float rippleX;

    private float rippleY;

    private int appButtonDuration;

    private int width;

    private int height;

    private boolean canClickable;


    public AppButton(Context context) {
        super(context);
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
        setCanClickable(true);
        mGestureDetector = new GestureDetector(context, this);
    }

    public AppButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = null;
        if (attrs != null) {
            ta = context.obtainStyledAttributes(attrs, R.styleable.AppButton);
        }
        selector = ta.getColor(R.styleable.AppButton_selector, DEFAULT_SELECTOR);
        normal = ta.getColor(R.styleable.AppButton_normal, DEFAULT_NORMAL);
        radioX = ScreenUtils.dp2px(ta.getDimension(R.styleable.AppButton_radioX, 0));
        radioY = ScreenUtils.dp2px(ta.getDimension(R.styleable.AppButton_radioY, 0));
        shadowColor = ta.getColor(R.styleable.AppButton_shadowColor, DEFAULT_SHADOW_COLOR);
        shadowRadio = ta.getFloat(R.styleable.AppButton_shadowRadio, DEFAULT_SHADOW_RADIO);
        isDrawRipple = ta.getBoolean(R.styleable.AppButton_appButtonIsDrawRipple, true);
        viewAlpha = ta.getInt(R.styleable.AppButton_appButtonViewAlpha, 70);
        viewRippleColor = ta.getInteger(R.styleable.AppButton_appButtonViewRippleColor, Color.WHITE);
        appButtonDuration = ta.getInteger(R.styleable.AppButton_appButtonDuration,DEFAULT_DURATION);
        ta.recycle();
        if (shadowRadio > 0) {
            drawShadow = true;
        } else {
            drawShadow = false;
        }
        color_rect = normal;
        setBackgroundColor(DEFAULT_NORMAL);
        setGravity(CENTER);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        width = getWidth();
        height = getHeight();
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color_rect);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        RectF rect;
        if (drawShadow) {
            setLayerType(LAYER_TYPE_SOFTWARE,null);
            rect = new RectF(shadowRadio, shadowRadio, width - shadowRadio, height - shadowRadio);
            paint.setShadowLayer(shadowRadio, 0, 0, shadowColor);
        } else {
            rect = new RectF(0, 0, width, height);
        }
        if (radioX > 0 || radioY > 0) {
            canvas.drawRoundRect(rect, radioX, radioY, paint);
        } else {
            canvas.drawRect(rect, paint);
        }
        super.onDraw(canvas);
        if (isDrawingRipple) {//绘制波纹
            RectF rectRect = new RectF(rippleX - rippleRadios, rippleY - rippleRadios, rippleX + rippleRadios, rippleY + rippleRadios);
            Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ripplePaint.setColor(viewRippleColor);
            ripplePaint.setAlpha(viewAlpha);
            canvas.drawOval(rectRect, ripplePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (isCanClickable()) {
                setColor_rect(normal);
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (isCanClickable()) {
            setColor_rect(selector);
        }
        if (isDrawRipple) {
            rippleX = e.getX();
            rippleY = e.getY();
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (isCanClickable()) {
            setColor_rect(normal);
            performClick();
        }
        if (isDrawRipple) {
            startRippleAnim();
        }
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
        if (isCanClickable()) {
            performLongClick();
        }
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Auto-generated method stub
        return false;
    }
    /**
     * 波纹动画
     */
    private void startRippleAnim() {
        isDrawingRipple = true;//开始绘制波纹的标识
        AnimUtils.rippleAnim(width, rippleX, appButtonDuration, new AnimUtils.OnRippleAnimListener() {
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

    private void setColor_rect(int color_rect) {
        this.color_rect = color_rect;
        postInvalidate();
    }

    public void setSelector(int selector) {
        this.selector = selector;
        postInvalidate();
    }

    public void setNormal(int normal) {
        this.normal = normal;
        setColor_rect(normal);
    }

    public void setDrawShadow(boolean drawShadow) {
        this.drawShadow = drawShadow;
        postInvalidate();
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
        postInvalidate();
    }

    public void setShadowRadio(float shadowRadio) {
        this.shadowRadio = shadowRadio;
        postInvalidate();
    }

    public void setRadioX(float radioX) {
        this.radioX = radioX;
        postInvalidate();
    }

    public void setRadioY(float radioY) {
        this.radioY = radioY;
        postInvalidate();
    }

    public boolean isCanClickable() {
        return canClickable;
    }

    public void setCanClickable(boolean canClickable) {
        this.canClickable = canClickable;
    }
}
