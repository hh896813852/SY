package com.edusoho.yunketang.edu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.edusoho.yunketang.edu.bean.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by JesseHuang on 2017/4/21.
 */

public class BaseActivity<T extends BasePresenter> extends ESNaviAppCompatActivity implements BaseView<T> {

    public T mPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        StatService.onPause(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onReceiveMessage(MessageEvent messageEvent) {

    }

    @Override
    public void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void close() {
        finish();
    }
}
