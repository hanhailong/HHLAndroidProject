package com.hhl.hhlandroidproject.util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by HanHailong on 15/7/16.
 */
public class UIScreenUtil {
    public static int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }
}
