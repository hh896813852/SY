package com.edusoho.yunketang.widget.materialcalendarview.decorator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * @author huhao on 2018/1/18.
 */

public class DrawableSpan implements LineBackgroundSpan {
    private Bitmap bitmap;

    public DrawableSpan(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence text, int start, int end, int lnum) {
        c.drawBitmap(bitmap, left + ((right - left) / 4) * 3 - 9, bottom - 10, p);
    }
}
