package com.willpower.touch.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.willpower.touch.R;
import com.willpower.touch.utils.AnimUtils;

import static com.willpower.touch.utils.AnimUtils.DEFAULT_DURATION;

/**
 * Created by Administrator on 2017/7/14.
 */

public class AppRelativeLayout extends RelativeLayout implements GestureDetector.OnGestureListener {
    /**
     * 监听手势
     */
    private GestureDetector mGestureDetector;
    /**
     * 点击波纹
     */
    private boolean relativeLayoutIsDrawRipple;
    private boolean isDrawingRipple;
    private float rippleRadios;
    private float rippleX;
    private float rippleY;
    private int relativeLayoutViewAlpha;
    private int relativeLayoutViewRippleColor;
    private int relativeLayoutDuration;
    private float relativeLayoutShadowRadios;
    private int relativeLayoutShadowColor;
    private float relativeLayoutRadios;
    private int relativeLayoutColor;
    private float relativeStrokeWidth;
    private int relativeStrokeColor;
    private int width, height;

    public AppRelativeLayout(Context context) {
        super(context);
    }

    public AppRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        mGestureDetector = new GestureDetector(context, this);
        init(context, attrs);
        setBackgroundColor(Color.TRANSPARENT);
    }

    public AppRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AppRelativeLayout);
        relativeLayoutIsDrawRipple = ta.getBoolean(R.styleable.AppRelativeLayout_relativeLayoutIsDrawRipple, true);
        relativeLayoutViewAlpha = ta.getInt(R.styleable.AppRelativeLayout_relativeLayoutViewAlpha, 70);
        relativeLayoutViewRippleColor = ta.getInteger(R.styleable.AppRelativeLayout_relativeLayoutViewRippleColor, Color.WHITE);
        relativeLayoutDuration = ta.getInteger(R.styleable.AppRelativeLayout_relativeLayoutDuration, DEFAULT_DURATION);
        relativeLayoutShadowRadios = ta.getFloat(R.styleable.AppRelativeLayout_relativeLayoutShadowRadios, 0);
        relativeLayoutShadowColor = ta.getInteger(R.styleable.AppRelativeLayout_relativeLayoutShadowColor, Color.parseColor("#000000"));
        relativeStrokeWidth = ta.getFloat(R.styleable.AppRelativeLayout_relativeStrokeWidth, 0f);
        relativeStrokeColor = ta.getInteger(R.styleable.AppRelativeLayout_relativeStrokeColor, Color.parseColor("#000000"));
        relativeLayoutRadios = ta.getFloat(R.styleable.AppRelativeLayout_relativeLayoutRadios, 0);
        relativeLayoutColor = ta.getInteger(R.styleable.AppRelativeLayout_relativeLayoutColor, Color.WHITE);
        ta.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        if (relativeStrokeWidth > 0) {
            drawStrokeAndFill(canvas);
        } else {
            drawFill(canvas);
        }
        if (isDrawingRipple) {//绘制波纹
            RectF rectRect = new RectF(rippleX - rippleRadios, rippleY - rippleRadios, rippleX + rippleRadios, rippleY + rippleRadios);
            Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ripplePaint.setColor(relativeLayoutViewRippleColor);
            ripplePaint.setAlpha(relativeLayoutViewAlpha);
            canvas.drawOval(rectRect, ripplePaint);
        }
    }


    /**
     * 绘制边框
     */
    private void drawStrokeAndFill(Canvas canvas) {
        //边框
        Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(relativeStrokeWidth);
        stroke.setColor(relativeStrokeColor);
        if (relativeLayoutShadowRadios > 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, stroke);
            stroke.setShadowLayer(relativeLayoutShadowRadios, 0, 0, relativeLayoutShadowColor);
        }
        RectF rectStroke = new RectF(relativeStrokeWidth, relativeStrokeWidth, width - relativeStrokeWidth, height - relativeLayoutShadowRadios - relativeStrokeWidth);

        //主体
        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(relativeLayoutColor);
        RectF rectFill = new RectF(relativeStrokeWidth * 2, relativeStrokeWidth * 2, width - relativeStrokeWidth * 2, height - relativeLayoutShadowRadios - relativeStrokeWidth * 2);
        //绘制
        canvas.drawRoundRect(rectStroke, relativeLayoutRadios, relativeLayoutRadios, stroke);
        canvas.drawRoundRect(rectFill, relativeLayoutRadios, relativeLayoutRadios, fill);
    }

    /**
     * 绘制主体
     */
    private void drawFill(Canvas canvas) {
        //主体
        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);
        fill.setColor(relativeLayoutColor);
        if (relativeLayoutShadowRadios > 0) {
            setLayerType(LAYER_TYPE_SOFTWARE, fill);
            fill.setShadowLayer(relativeLayoutShadowRadios, 0, 0, relativeLayoutShadowColor);
        }
        RectF rectFill = new RectF(1, 1, width - 1, height - relativeLayoutShadowRadios);
        canvas.drawRoundRect(rectFill, relativeLayoutRadios, relativeLayoutRadios, fill);
    }

    /**
     * 波纹动画
     */
    private void startRippleAnim() {
        isDrawingRipple = true;//开始绘制波纹的标识
        AnimUtils.rippleAnim(width, rippleX, relativeLayoutDuration, new AnimUtils.OnRippleAnimListener() {
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
        }
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        if (relativeLayoutIsDrawRipple) {
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
        performClick();
        if (relativeLayoutIsDrawRipple) {
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
        performLongClick();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        // Auto-generated method stub
        return false;
    }

    public void setAppShadowRadios(float relativeLayoutShadowRadios) {
        this.relativeLayoutShadowRadios = relativeLayoutShadowRadios;
        postInvalidate();
    }

    public void setAppShadowColor(int relativeLayoutShadowColor) {
        this.relativeLayoutShadowColor = relativeLayoutShadowColor;
        postInvalidate();
    }

    public void setAppRadios(float relativeLayoutRadios) {
        this.relativeLayoutRadios = relativeLayoutRadios;
        postInvalidate();
    }

    public void setAppColor(int relativeLayoutColor) {
        this.relativeLayoutColor = relativeLayoutColor;
        postInvalidate();
    }
}
