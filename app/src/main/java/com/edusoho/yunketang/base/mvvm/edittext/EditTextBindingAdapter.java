package com.edusoho.yunketang.base.mvvm.edittext;

import android.databinding.BindingAdapter;
import android.widget.EditText;

import com.edusoho.yunketang.helper.EditFilterHelper;

public class EditTextBindingAdapter {

    @BindingAdapter({"filterEmoji"})
    public static void filterEmoji(EditText editText, boolean filter) {
        if (filter) {
            EditFilterHelper.addFilter(editText);
        }
    }
}
