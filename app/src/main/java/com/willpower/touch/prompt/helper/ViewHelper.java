package com.willpower.touch.prompt.helper;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.willpower.touch.prompt.holder.EmptyViewHolder;
import com.willpower.touch.prompt.holder.ErrorViewHolder;
import com.willpower.touch.prompt.holder.LoadingViewHolder;
import com.willpower.touch.prompt.holder.NoNetworkViewHolder;
import com.willpower.touch.prompt.holder.TimeOutViewHolder;


/**
 * <pre>
 *     desc   : View设置帮助类
 *     version: 1.0
 * </pre>
 */

public class ViewHelper {

    public static final int ERROR = 1;
    public static final int EMPTY = 2;
    public static final int TIMEOUT = 3;
    public static final int NOT_NETWORK = 4;
    public static final int LOADING = 5;


    public TextView getTvTip(int type, View view) {
        switch (type) {
            case ERROR:
                return ((ErrorViewHolder) view.getTag()).tvTip;
            case EMPTY:
                return ((EmptyViewHolder) view.getTag()).tvTip;
            case TIMEOUT:
                return ((TimeOutViewHolder) view.getTag()).tvTip;
            case NOT_NETWORK:
                return ((NoNetworkViewHolder) view.getTag()).tvTip;
            case LOADING:
                return ((LoadingViewHolder) view.getTag()).tvTip;
            default:
                return null;
        }
    }

    public ImageView getImg(int type, View view) {
        switch (type) {
            case ERROR:
                return ((ErrorViewHolder) view.getTag()).ivImg;
            case EMPTY:
                return ((EmptyViewHolder) view.getTag()).ivImg;
            case TIMEOUT:
                return ((TimeOutViewHolder) view.getTag()).ivImg;
            case NOT_NETWORK:
                return ((NoNetworkViewHolder) view.getTag()).ivImg;
            default:
                return null;
        }
    }


}
