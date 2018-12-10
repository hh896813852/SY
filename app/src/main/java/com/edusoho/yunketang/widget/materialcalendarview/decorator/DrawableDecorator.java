package com.edusoho.yunketang.widget.materialcalendarview.decorator;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.edusoho.yunketang.widget.materialcalendarview.CalendarDay;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewDecorator;
import com.edusoho.yunketang.widget.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Decorate several days with a dot
 */
public class DrawableDecorator implements DayViewDecorator {

    private Bitmap bitmap;
    private HashSet<CalendarDay> dates;

    public DrawableDecorator(Drawable drawable, Collection<CalendarDay> dates) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        this.bitmap = bd.getBitmap();
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DrawableSpan(bitmap));
    }
}
