package com.edusoho.yunketang.base.animation.FadeEnter;

import android.animation.ObjectAnimator;
import android.view.View;

import com.edusoho.yunketang.base.animation.BaseAnimatorSet;

public class FadeEnter extends BaseAnimatorSet {
	@Override
	public void setAnimation(View view) {
		animatorSet.playTogether(//
				ObjectAnimator.ofFloat(view, "alpha", 0, 1).setDuration(duration));
	}
}
