package com.willpower.touch.prompt.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.willpower.main.R;


public class TimeOutViewHolder extends BaseHolder {

    public ImageView ivImg;

    public TimeOutViewHolder(View view) {
        tvTip = (TextView) view.findViewById(R.id.tv_message);
        ivImg = (ImageView) view.findViewById(R.id.iv_img);
    }


}
