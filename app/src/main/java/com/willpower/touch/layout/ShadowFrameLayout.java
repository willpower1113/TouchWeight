package com.willpower.touch.layout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;

import com.willpower.touch.R;


/**
 * Created by Administrator on 2017/8/31.
 */

public class ShadowFrameLayout extends FrameLayout {
    private static final int DEFAULT_VIEW_COLOR = Color.parseColor("#FFFFFF");
    private static final int DEFAULT_SHADOW_COLOR = Color.parseColor("#99000000");
    private static final float DEFAULT_SHADOW_RADIO = 5.0F;
    private boolean hasShadow;
    private int sfColor;//阴影颜色
    private float sfRadio;//阴影半径
    private float mRadios;//圆角半径
    private int bgColor;//背景颜色

    public ShadowFrameLayout(@NonNull Context context) {
        super(context);
    }

    public ShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        setBackgroundColor(Color.TRANSPARENT);
        init(context, attrs);
    }

    public ShadowFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShadowFrameLayout);
        sfColor = ta.getColor(R.styleable.ShadowFrameLayout_sfColor, DEFAULT_SHADOW_COLOR);
        sfRadio = ta.getDimension(R.styleable.ShadowFrameLayout_sfRadio, DEFAULT_SHADOW_RADIO);
        bgColor = ta.getColor(R.styleable.ShadowFrameLayout_bgColor, DEFAULT_VIEW_COLOR);
        mRadios = dp2px(ta.getDimension(R.styleable.ShadowFrameLayout_mRadios, 0.0F));
        hasShadow = sfRadio > 0.0F;
        ta.recycle();
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        if (hasShadow) {
            setPadding(0, 0, 0, (int) sfRadio);
            setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        }
        RectF rect = new RectF(0, 0, getWidth(), getHeight() - sfRadio);
        paint.setShadowLayer(sfRadio,0, 0, sfColor);
        if (mRadios > 0) {
            canvas.drawRoundRect(rect, mRadios, mRadios, paint);
        } else {
            canvas.drawRect(rect, paint);
        }
        super.onDraw(canvas);
    }

    private static float dp2px(float dp) {
        if (dp == 0.0F) {
            return 0.0F;
        } else {
            Resources r = Resources.getSystem();
            return TypedValue.applyDimension(1, dp, r.getDisplayMetrics());
        }
    }
}
