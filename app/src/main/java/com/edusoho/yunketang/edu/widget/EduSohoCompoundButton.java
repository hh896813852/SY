package com.edusoho.yunketang.edu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioGroup;

import com.edusoho.yunketang.R;

/**
 * Created by JesseHuang on 15/12/11.
 */
public class EduSohoCompoundButton extends RadioGroup {

    public EduSohoCompoundButton(Context context) {
        super(context);
    }

    public EduSohoCompoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundResource(R.drawable.found_compound_btn);
    }
}
