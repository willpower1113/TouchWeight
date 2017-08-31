package com.willpower.touch.button;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.willpower.touch.R;

import static android.view.Gravity.CENTER;

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

    public AppButton(Context context) {
        super(context);
    }

    public AppButton(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
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
        radioX = dp2px(ta.getDimension(R.styleable.AppButton_radioX, 0));
        radioY = dp2px(ta.getDimension(R.styleable.AppButton_radioY, 0));
        shadowColor = ta.getColor(R.styleable.AppButton_shadowColor, DEFAULT_SHADOW_COLOR);
        shadowRadio = ta.getFloat(R.styleable.AppButton_shadowRadio, DEFAULT_SHADOW_RADIO);
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
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(color_rect);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        RectF rect;
        if (drawShadow) {
            setLayerType(LAYER_TYPE_SOFTWARE,null);
            rect = new RectF(shadowRadio,shadowRadio, getWidth() - shadowRadio, getHeight() - shadowRadio);
            paint.setShadowLayer(shadowRadio, 0, 0, shadowColor);
        } else {
            rect = new RectF(0, 0, getWidth(), getHeight());
        }
        if (radioX > 0 || radioY > 0) {
            canvas.drawRoundRect(rect, radioX, radioY, paint);
        } else {
            canvas.drawRect(rect, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_CANCEL || event.getActionMasked() == MotionEvent.ACTION_UP) {
            setColor_rect(normal);
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        setColor_rect(selector);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        // Auto-generated method stub
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        setColor_rect(normal);
        performClick();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        // Auto-generated method stub
        setColor_rect(normal);
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //长安时，手动触发长安事件
        setColor_rect(normal);
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Auto-generated method stub
        setColor_rect(normal);
        return false;
    }

    private void setColor_rect(int color_rect) {
        this.color_rect = color_rect;
        postInvalidate();
    }

    public void setSelector(int selector) {
        this.selector = selector;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    private static float dp2px(float dp) {
        if (dp == 0) {
            return 0f;
        }
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
