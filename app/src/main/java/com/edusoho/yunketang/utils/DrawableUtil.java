package com.edusoho.yunketang.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * @author huhao on 2016/12/8.
 */
public class DrawableUtil {
    public static GradientDrawable createRoundRect(int color, int corner) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(corner);
        drawable.setColor(color);
        return drawable;
    }

    public static StateListDrawable createSelectorDrawable(Drawable pressedDrawable, Drawable normalDrawable) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, pressedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_focused}, pressedDrawable);
        stateListDrawable.addState(new int[]{}, normalDrawable);
        return stateListDrawable;
    }
}
