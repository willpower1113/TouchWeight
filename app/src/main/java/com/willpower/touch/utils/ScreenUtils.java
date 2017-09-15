package com.willpower.touch.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Administrator on 2017/9/15.
 */

public class ScreenUtils {
    public static float dp2px(float dp) {
        if (dp == 0) {
            return 0f;
        }
        Resources r = Resources.getSystem();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }
}
