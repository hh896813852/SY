package com.edusoho.yunketang.edu.order.dialog.coupons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.widget.ESBottomDialog;

import java.util.List;

/**
 * Created by DF on 2017/4/14.
 * 优惠券选择界面
 */
public class CouponsDialog extends ESBottomDialog
        implements ESBottomDialog.BottomDialogContentView {

    private ListView               mCoupons;
    private CouponsAdapter         mAdapter;
    private List<OrderInfo.Coupon> mList;
    private OrderInfo              mOrderInfo;
    private int mPosition = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(this);
    }

    @Override
    public View getContentView(ViewGroup parentView) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_coupons, parentView, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mCoupons = (ListView) view.findViewById(R.id.rv_content);
        mAdapter = new CouponsAdapter(getContext(), mList, mOrderInfo);
        mCoupons.setAdapter(mAdapter);
        mCoupons.setOnItemClickListener(getOnItemClickListener());
    }

    private AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderInfo.Coupon coupon = mList.get(position);
                if (mPosition == position) {
                    coupon.isSelector = !coupon.isSelector;
                } else {
                    coupon.isSelector = true;
                    mList.get(mPosition).isSelector = false;
                }
                mAdapter.notifyDataSetChanged();
                mPosition = position;
            }
        };
    }

    public void setData(List<OrderInfo.Coupon> list, OrderInfo orderInfo) {
        mOrderInfo = orderInfo;
        mList = list;
    }

    @Override
    public void setButtonState(TextView btn) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mList.get(mPosition).isSelector) {
                    ((ModifyView) getActivity()).setPriceView(mList.get(mPosition));
                } else {
                    ((ModifyView) getActivity()).setPriceView(null);
                }
                dismiss();
            }
        });
    }

    @Override
    public boolean showConfirm() {
        return true;
    }

    public interface ModifyView {

        void setPriceView(OrderInfo.Coupon coupon);

    }
}
