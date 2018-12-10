package com.edusoho.yunketang.base.animation.SlideExit;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;

import com.edusoho.yunketang.base.animation.BaseAnimatorSet;

public class SlideTopExit extends BaseAnimatorSet {

    public SlideTopExit() {
    }

    public SlideTopExit(int duration) {
        this.duration = duration;
    }

    @Override
    public void setAnimation(View view) {
        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "translationY", 0, -250 * dm.density), //
                ObjectAnimator.ofFloat(view, "alpha", 1, 0));
    }
}
