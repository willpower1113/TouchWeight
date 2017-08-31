package com.willpower.touch.shadow;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.willpower.touch.R;

/**
 * Created by Administrator on 2017/8/31.
 */

public class ShadowRecyclerView extends RecyclerView {
    private static final int DEFAULT_VIEW_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_SHADOW_COLOR = Color.parseColor("#99000000");
    private static final float DEFAULT_SHADOW_RADIO = 5;
    /**
     * shadow
     */
    private boolean drawShadow;
    private int shadowColor;
    private float shadowRadio;
    /**
     * radio
     */
    private float radio;

    /**
     * background
     */
    private int viewColor;

    public ShadowRecyclerView(Context context) {
        super(context);
    }

    public ShadowRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ShadowRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AppButton);
        shadowColor = ta.getColor(R.styleable.ShadowRecyclerView_shadowColor, DEFAULT_SHADOW_COLOR);
        shadowRadio = dp2px(ta.getDimension(R.styleable.ShadowRecyclerView_shadowRadio, DEFAULT_SHADOW_RADIO));
        viewColor = ta.getColor(R.styleable.ShadowRecyclerView_viewColor, DEFAULT_VIEW_COLOR);
        radio = dp2px(ta.getDimension(R.styleable.ShadowRecyclerView_radio, 0));
        drawShadow = shadowRadio > 0 ? true : false;
    }


    @Override
    public void onDraw(Canvas canvas) {
        setBackgroundColor(Color.TRANSPARENT);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(viewColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (drawShadow) {
            setPadding((int)shadowRadio,(int)shadowRadio,(int)shadowRadio,(int)shadowRadio);
            setLayerType(LAYER_TYPE_SOFTWARE, null);//开启硬件加速
        }
        RectF rect = new RectF(shadowRadio, shadowRadio, getWidth() - shadowRadio, getHeight() - shadowRadio);
        paint.setShadowLayer(shadowRadio, 0, 0, shadowColor);
        if (radio > 0) {
            canvas.drawRoundRect(rect, radio, radio, paint);
        } else {
            canvas.drawRect(rect, paint);
        }
        super.onDraw(canvas);
    }

    private static float dp2px(float dp) {
        if (dp == 0) {
            return 0f;
        }
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
