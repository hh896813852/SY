package com.edusoho.yunketang.utils;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class ViewUtils {

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    public static void setProgressBarAnim(ProgressBar progressBar, int progressMax, int progress, int duration) {
        ValueAnimator animator = ValueAnimator.ofInt(0, progress * 100).setDuration(duration);
        progressBar.setMax(progressMax * 100);
        animator.addUpdateListener(valueAnimator -> progressBar.setProgress((int) valueAnimator.getAnimatedValue()));
        animator.start();
    }
}
