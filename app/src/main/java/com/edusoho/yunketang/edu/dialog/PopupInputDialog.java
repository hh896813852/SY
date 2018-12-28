package com.edusoho.yunketang.edu.dialog;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import com.edusoho.yunketang.R;

/**
 * Created by howzhi on 15/7/24.
 */
public class PopupInputDialog extends PopupDialog {

    protected EditText mInputEdit;
    public PopupInputDialog(Context context, int style, int theme, PopupClickListener clickListener) {
        super(context, style, theme, clickListener);
    }

    @Override
    protected void initView() {
        mInputEdit = (EditText) findViewById(R.id.popup_input);
        super.initView();
    }

    public void setInputType(String type) {
        if ("password".equals(type)) {
            mInputEdit.setHint("请输入密码");
            mInputEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public String getInputString() {
        return mInputEdit.getText().toString();
    }

    public static PopupInputDialog create(Context context, String title, String msg, String type) {
        PopupInputDialog dlg = new PopupInputDialog(
                context, R.layout.popup_input, R.style.loadDlgTheme, null);
        dlg.setInputType(type);
        dlg.setTitle(title);
        dlg.setMessage(msg);

        return dlg;
    }
}
