package com.edusoho.yunketang.base.animation.Attention;

import android.animation.ObjectAnimator;
import android.view.View;

import com.edusoho.yunketang.base.animation.BaseAnimatorSet;

public class Swing extends BaseAnimatorSet {
	public Swing() {
		duration = 1000;
	}

	public Swing(int duration) {
		this.duration = duration;
	}

	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 1, 1, 1, 1, 1, 1, 1, 1),//
				ObjectAnimator.ofFloat(view, "rotation", 0, 10, -10, 6, -6, 3, -3, 0));
	}
}
