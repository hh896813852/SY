package com.edusoho.yunketang.widget.materialcalendarview.decorator;

import com.edusoho.yunketang.widget.materialcalendarview.CalendarDay;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewDecorator;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewFacade;

/**
 * Decorate several days with a dot
 */
public class DisableDecorator implements DayViewDecorator {

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return true;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(true);
    }
}
