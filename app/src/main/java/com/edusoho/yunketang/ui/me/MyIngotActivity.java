package com.edusoho.yunketang.ui.me;

import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;

@Layout(value = R.layout.activity_my_ingot, title = "")
public class MyIngotActivity extends BaseActivity {
    public static final String INGOT_TYPE = "ingot_type"; // 1、上元在线 2、上元会计

    public ObservableField<String> ingot = new ObservableField<>("0");
    public ObservableField<Integer> ingotType = new ObservableField<>(1);
    public ObservableField<Integer> rechargeIngot = new ObservableField<>(6);
    public ObservableField<String> ingotTypeName = new ObservableField<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        loadData();
    }

    private void loadData() {
        SYDataTransport.create(ingotType.get() == 1 ? SYConstants.ONLINE_MY_COIN : SYConstants.ACCOUNTANT_MY_COIN)
                .directReturn()
                .execute(new SYDataListener(){

                    @Override
                    public void onSuccess(Object data) {

                    }
                });
    }

    private void initView() {
        ingotType.set(getIntent().getIntExtra(INGOT_TYPE, 0));
        setTitleView(ingotType.get() == 1 ? "上元在线元宝" : "上元会计元宝");
        ingotTypeName.set(ingotType.get() == 1 ? "上元在线余额" : "上元会计余额");
    }

    /**
     * 确认充值
     */
    public void onRechargeClick(View view) {

    }

    /**
     * 充值元宝选择
     */
    public void onRechargeIngotClick(View view) {
        rechargeIngot.set(Integer.valueOf(view.getTag().toString()));
    }
}
