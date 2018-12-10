package com.edusoho.yunketang.widget.materialcalendarview.decorator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

import com.edusoho.yunketang.widget.materialcalendarview.CalendarDay;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewDecorator;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewFacade;

import java.util.Date;

/**
 * Decorate a day by making the text big and bold
 */
public class SelectedDayDecorator implements DayViewDecorator {
    private Drawable drawable;
    private CalendarDay date;

    public void setDate(Context context, Date date) {
//        drawable = context.getResources().getDrawable(R.drawable.calendar_selector);
//        this.date = CalendarDay.from(date);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        view.addSpan(new ForegroundColorSpan(Color.parseColor("#4A8CE3")));
        view.addSpan(new RelativeSizeSpan(1.2f));
    }
}
