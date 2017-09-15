package com.willpower.touch.prompt.bean;

/**
 * <pre>
 *     desc   : 错误数据对象
 *     version: 1.0
 * </pre>
 */

public class ErrorItem extends BaseItem {

    public ErrorItem(int resId, String tip) {
        setResId(resId);
        setTip(tip);
    }

}
