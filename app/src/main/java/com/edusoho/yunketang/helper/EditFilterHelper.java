package com.edusoho.yunketang.helper;

import android.content.Context;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

public class EditFilterHelper {

    public static void addFilter(EditText editText) {
//        InputFilter[] filters = {getInputFilterProhibitEmoji(editText.getContext()), getInputFilterProhibitSP(editText.getContext())};
        InputFilter[] filters = {getInputFilterProhibitEmoji(editText.getContext())};
        editText.setFilters(filters);
    }

    private static InputFilter getInputFilterProhibitEmoji(Context context) {
        return (source, start, end, dest, dstart, dend) -> {
            StringBuffer buffer = new StringBuffer();
            for (int i = start; i < end; i++) {
                char codePoint = source.charAt(i);
                if (!getIsEmoji(codePoint)) {
                    buffer.append(codePoint);
                } else {
                    ToastHelper.showSingleToast(context, "不能输入表情符号！");
                    i++;
                }
            }
            if (source instanceof Spanned) {
                SpannableString sp = new SpannableString(buffer);
                TextUtils.copySpansFrom((Spanned) source, start, end, null, sp, 0);
                return sp;
            } else {
                return buffer;
            }
        };
    }

    private static boolean getIsEmoji(char codePoint) {
        return codePoint != 0x0 && codePoint != 0x9 && codePoint != 0xA && codePoint != 0xD && (codePoint < 0x20 || codePoint > 0xD7FF) && (codePoint < 0xE000 || codePoint > 0xFFFD);
    }

    private static InputFilter getInputFilterProhibitSP(Context context) {
        return (source, start, end, dest, dstart, dend) -> {
            StringBuffer buffer = new StringBuffer();
            for (int i = start; i < end; i++) {
                char codePoint = source.charAt(i);
                if (!getIsSp(codePoint)) {
                    buffer.append(codePoint);
                } else {
                    ToastHelper.showSingleToast(context, "不能输入特殊字符！");
                    i++;
                }
            }
            if (source instanceof Spanned) {
                SpannableString sp = new SpannableString(buffer);
                TextUtils.copySpansFrom((Spanned) source, start, end, null,
                        sp, 0);
                return sp;
            } else {
                return buffer;
            }
        };
    }

    private static boolean getIsSp(char codePoint) {
        return Character.getType(codePoint) > Character.LETTER_NUMBER;
    }
}
