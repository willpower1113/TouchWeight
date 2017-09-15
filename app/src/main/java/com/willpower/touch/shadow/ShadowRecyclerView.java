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
    private static final int DEFAULT_SHADOW_RADIO = 5;

    private static final int MODEL_ALL = 1;
    private static final int MODEL_BOTTOM = 2;
    /**
     * shadow
     */
    private boolean hasShadow;
    private int shadowsColor;
    private float shadowsRadio;
    /**
     * radio
     */
    private float radios;

    /**
     * background
     */
    private int viewColor;

    /**
     * shadowModel
     */
    private int mShadowModel;

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
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowRecyclerView);
        shadowsColor = ta.getColor(R.styleable.ShadowRecyclerView_shadowsColor, DEFAULT_SHADOW_COLOR);
        shadowsRadio = ta.getInt(R.styleable.ShadowRecyclerView_shadowsRadio, DEFAULT_SHADOW_RADIO);
        viewColor = ta.getColor(R.styleable.ShadowRecyclerView_viewColor, DEFAULT_VIEW_COLOR);
        radios = ta.getInt(R.styleable.ShadowRecyclerView_radios, 0);
        mShadowModel = ta.getInt(R.styleable.ShadowRecyclerView_mShadowModel, MODEL_ALL);
        ta.recycle();

        hasShadow = shadowsRadio > 0 ? true : false;
    }


    @Override
    public void onDraw(Canvas canvas) {
        setBackgroundColor(Color.TRANSPARENT);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(viewColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (hasShadow) {
            setPadding((int)shadowsRadio,(int)shadowsRadio,(int)shadowsRadio,(int)shadowsRadio);
            setLayerType(LAYER_TYPE_SOFTWARE, null);//开启硬件加速
        }
        RectF rect;
        if (mShadowModel == MODEL_BOTTOM) {//只有底部有阴影
            rect = new RectF(0, 0, getWidth(), getHeight() - shadowsRadio);
            setPadding(0, 0, 0, (int) shadowsRadio);
        } else {//都有阴影
            rect = new RectF(shadowsRadio, shadowsRadio, getWidth() - shadowsRadio, getHeight() - shadowsRadio);
        }
        paint.setShadowLayer(shadowsRadio, 0, 0, shadowsColor);
        if (radios > 0) {
            canvas.drawRoundRect(rect, radios, radios, paint);
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
