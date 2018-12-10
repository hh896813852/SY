package com.edusoho.yunketang.base.animation.ZoomExit;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.View.MeasureSpec;

import com.edusoho.yunketang.base.animation.BaseAnimatorSet;

public class ZoomOutBottomExit extends BaseAnimatorSet {
    public ZoomOutBottomExit() {
        duration = 600;
    }

    @Override
    public void setAnimation(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int h = view.getMeasuredHeight();

        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "alpha", 1, 1, 0),//
                ObjectAnimator.ofFloat(view, "scaleX", 1, 0.475f, 0.1f),//
                ObjectAnimator.ofFloat(view, "scaleY", 1, 0.475f, 0.1f),//
                ObjectAnimator.ofFloat(view, "translationY", 0, -60, h));
    }
}
