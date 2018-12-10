package com.edusoho.yunketang.widget.materialcalendarview.decorator;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.widget.materialcalendarview.CalendarDay;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewDecorator;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewFacade;

/**
 * Use a custom selector
 */
public class SelectorDecorator implements DayViewDecorator {

    private final Drawable drawable;

    public SelectorDecorator(Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.calendar_selector);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
}