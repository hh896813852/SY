package com.edusoho.yunketang.base;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.helper.ToastHelper;
import com.edusoho.yunketang.utils.LogUtil;

import java.lang.reflect.Field;

/**
 * Created by any on 17/6/19.
 */

public class BaseFragment<DataBinding extends ViewDataBinding> extends Fragment {
    DataBinding dataBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Layout layout = getClass().getAnnotation(Layout.class);
        if (layout != null) {
            dataBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), layout.value(), container, false);
            TextView titleView = (TextView) dataBinding.getRoot().findViewById(R.id.actionbarTitle);
            if (titleView != null) {
                titleView.setText(layout.title());
            }
            TextView rightButtonTextView = (TextView) dataBinding.getRoot().findViewById(R.id.textButton);
            if (rightButtonTextView != null) {
                rightButtonTextView.setText(layout.rightButton());
                rightButtonTextView.setOnClickListener(v -> onRightButtonClick());
            }
            bindViewModelToUI();
            return dataBinding.getRoot();
        } else {
            LogUtil.w("DataBinding警告->需要使用数据绑定功能必须使用@Layout注解注入布局");
            return null;
        }
    }

    public void onRightButtonClick() {

    }

    private void bindViewModelToUI() {
        if (dataBinding != null) {
            // 绑定两者
            Field[] declaredFields = dataBinding.getClass().getDeclaredFields();
            for (Field declaredField : declaredFields) {
                if (declaredField.getType().equals(getClass())) {
                    declaredField.setAccessible(true);
                    try {
                        declaredField.set(dataBinding, this);
                    } catch (IllegalAccessException e) {
                    }
                    declaredField.setAccessible(false);
                    break;
                }
            }
        }
    }

    public void showToast(String tip) {
        ToastHelper.showToast(getActivity(), tip);
    }

    public void showSingleToast(String tip) {
        ToastHelper.showSingleToast(getActivity(), tip);
    }

    public BaseActivity getSupportedActivity() {
        return (BaseActivity) getActivity();
    }

    public DataBinding getDataBinding() {
        return dataBinding;
    }
}
