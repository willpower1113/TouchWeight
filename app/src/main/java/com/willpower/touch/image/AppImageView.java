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

public class AppImageView extends BaseImageView {

    public AppImageView(Context context) {
        super(context);
    }
    public AppImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        viewWidth = getWidth();
        viewHeight = getHeight();
        super.onDraw(canvas);
        RectF rectF = new RectF(shadowRadios, shadowRadios, viewWidth - shadowRadios, viewHeight - shadowRadios);
        if (strokeWidth > 0) {
            //绘制主体和边框
            drawStrokeAndFill(canvas, rectF);
        }
        //绘制覆盖层
        drawCover(canvas, rectF);
        //绘制波纹
        drawRippleWithAnim(canvas);
    }

    //********************************************************draw********************************************//
    //绘制主体和边框
    @Override
    protected void drawStrokeAndFill(Canvas canvas, RectF rectF) {
        Paint stroke = new Paint(Paint.ANTI_ALIAS_FLAG);
        stroke.setStyle(Paint.Style.STROKE);
        stroke.setStrokeWidth(strokeWidth);
        stroke.setColor(strokeColor);
        if (!appNoShadow) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
            stroke.setShadowLayer(shadowRadios, shadowOffsetX, shadowOffsetY, shadowColor);
        }
        canvas.drawRoundRect(rectF, filletX, filletY, stroke);
    }

    @Override
    protected void drawFill(Canvas canvas, RectF rectF) {
    }

    //绘制覆盖层
    protected void drawCover(Canvas canvas, RectF rectF) {
        if (!isTouching) return;
        Paint cover = new Paint(Paint.ANTI_ALIAS_FLAG);
        cover.setStyle(Paint.Style.FILL);
        cover.setColor(coverColor);
        canvas.drawRoundRect(rectF, filletX, filletY, cover);
    }

    //绘制波纹
    protected void drawRippleWithAnim(Canvas canvas) {
        if (isDrawingRipple) {
            RectF rectRect = new RectF(rippleX - rippleRadios, rippleY - rippleRadios, rippleX + rippleRadios, rippleY + rippleRadios);
            Paint ripplePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            ripplePaint.setColor(rippleColor);
            ripplePaint.setAlpha(rippleAlpha);
            canvas.drawOval(rectRect, ripplePaint);
        }
    }

    //********************************************************draw********************************************//

    /**
     * 波纹动画
     */
    protected void startRippleAnim() {
        isDrawingRipple = true;//开始绘制波纹的标识
        AnimUtils.rippleAnim(viewWidth, viewWidth / 2, rippleDuration, new AnimUtils.OnRippleAnimListener() {
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
