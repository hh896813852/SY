package com.edusoho.yunketang.edu.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by JesseHuang on 2016/12/2.
 */

public class InputUtils {

    public static void showKeyBoard(final EditText editText, final Context context) {
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 200);
    }

    public static void addTextChangedListener(final EditText editText, final NormalCallback<Editable> normalCallback) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                normalCallback.success(s);
            }
        });
    }

    public interface NormalCallback<T> {
        void success(T obj);
    }
}
