package com.liqi.roulettedemo.widget;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by lqf on 3/31/20
 */
public class Density {

    public static int sp2px(Context context, int sp) {

        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                sp, context.getResources().getDisplayMetrics());
    }

    public static int dp2px(Context context, float dp) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, context.getResources().getDisplayMetrics());
    }

}
