package com.willpower.touch.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.willpower.touch.utils.AnimUtils;

/**
 * Created by Administrator on 2017/8/23.
 */

public class AppRoundImageButton extends BaseImageView {

    public AppRoundImageButton(Context context) {
        super(context);
    }

    public AppRoundImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppRoundImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        RectF rectF = new RectF(shadowRadios, shadowRadios, viewWidth - shadowRadios, viewHeight - shadowRadios);
        if (strokeWidth > 0) {
            //绘制主体和边框
            drawStrokeAndFill(canvas, rectF);
        } else {
            //绘制主体
            drawFill(canvas, rectF);
        }
        super.onDraw(canvas);
        //绘制覆盖层
        drawCover(canvas, rectF);
        //绘制波纹
        drawRippleWithAnim(canvas);
    }

    //********************************************************draw********************************************//
    //绘制主体和边框
    protected void drawStrokeAndFill(Canvas canvas, RectF rectF) {
        drawFill(canvas, rectF);
        Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(strokeWidth);
        stroke.setColor(strokeColor);
        if (!appNoShadow) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            stroke.setShadowLayer(shadowRadios, shadowOffsetX, shadowOffsetY, shadowColor);
        }
        canvas.drawOval(rectF, stroke);
    }

    //绘制主体
    protected void drawFill(Canvas canvas, RectF rectF) {
        Paint fill = new Paint(Paint.ANTI_ALIAS_FLAG);
        fill.setStyle(Paint.Style.FILL);
        setPaintColor(fill);
        if (!appNoShadow) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            fill.setShadowLayer(shadowRadios, shadowOffsetX, shadowOffsetY, shadowColor);
        }
        canvas.drawOval(rectF, fill);
    }

    //绘制覆盖层
    protected void drawCover(Canvas canvas, RectF rectF) {
        if (clickModel != CLICK_MODEL_COVER || !isTouching) return;
        Paint cover = new Paint(Paint.ANTI_ALIAS_FLAG);
        cover.setStyle(Paint.Style.FILL);
        cover.setColor(coverColor);
        canvas.drawOval(rectF, cover);
    }

    //绘制波纹
    protected void drawRippleWithAnim(Canvas canvas) {
        if (isDrawingRipple) {
            RectF rectRect = new RectF(viewWidth / 2 - rippleRadios, viewWidth / 2 - rippleRadios, viewWidth / 2 + rippleRadios, viewWidth / 2 + rippleRadios);
            Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ripplePaint.setColor(rippleColor);
            ripplePaint.setAlpha(rippleAlpha);
            canvas.drawOval(rectRect, ripplePaint);
        }
    }

    /*
   设置画笔颜色
    */
    void setPaintColor(Paint paint) {
        if (clickModel == CLICK_MODEL_CHANGE) {
            if (isTouching) {
                paint.setColor(selectColor);
            } else {
                paint.setColor(normalColor);
            }
        } else {
            paint.setColor(normalColor);
        }
    }

    //********************************************************draw********************************************//

    /**
     * 波纹动画
     */
    protected void startRippleAnim() {
        isDrawingRipple = true;//开始绘制波纹的标识
        AnimUtils.rippleAnim(viewWidth, viewWidth / 2, rippleDuration, new  AnimUtils.OnRippleAnimListener() {
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
}
