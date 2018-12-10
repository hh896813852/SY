package com.edusoho.yunketang.widget.materialcalendarview.decorator;

import android.content.Context;
import android.graphics.Color;

import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.widget.materialcalendarview.CalendarDay;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewDecorator;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewFacade;
import com.edusoho.yunketang.widget.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class HasCourseDecorator implements DayViewDecorator {
    private HashSet<CalendarDay> dates;
    private Context context;

    public HasCourseDecorator(Context context, Collection<CalendarDay> dates) {
        this.context = context;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(DensityUtil.dip2px(context, 3), Color.parseColor("#17A884")));
    }
}
