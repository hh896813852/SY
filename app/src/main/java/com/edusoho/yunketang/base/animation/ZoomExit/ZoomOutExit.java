package com.edusoho.yunketang.base.animation.ZoomExit;

import android.animation.ObjectAnimator;
import android.view.View;

import com.edusoho.yunketang.base.animation.BaseAnimatorSet;


public class ZoomOutExit extends BaseAnimatorSet {

	public ZoomOutExit() {
	}

	public ZoomOutExit(int duration) {
		this.duration = duration;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 0, 0),//
				ObjectAnimator.ofFloat(view, "scaleX", 1, 0.3f, 0),//
				ObjectAnimator.ofFloat(view, "scaleY", 1, 0.3f, 0));//
	}
}
