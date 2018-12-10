package com.edusoho.yunketang.edu.order.confirm;


import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.api.ClassroomApi;
import com.edusoho.yunketang.edu.api.OrderApi;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by RexXiang on 2017/7/5.
 */

public class ConfirmClassOrderPresenter extends ConfirmOrderPresenter {

    private int mClassroomId;

    ConfirmClassOrderPresenter(ConfirmOrderContract.View view, int classroomId) {
        super(view, 0, 0);
        this.mClassroomId = classroomId;
    }

    @Override
    public void subscribe() {
        HttpUtils.getInstance()
                .addTokenHeader(SYApplication.getInstance().token)
                .createApi(ClassroomApi.class)
                .getClassroom(mClassroomId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mFragmentLifeProvider.<Classroom>bindToLifecycle())
                .doOnNext(new Action1<Classroom>() {
                    @Override
                    public void call(Classroom classroom) {
                        mView.showTopView(classroom);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<Classroom, Observable<OrderInfo>>() {
                    @Override
                    public Observable<OrderInfo> call(Classroom classroom) {
                        return HttpUtils.getInstance()
                                .addTokenHeader(SYApplication.getInstance().token)
                                .createApi(OrderApi.class)
                                .postOrderInfo("classroom", mClassroomId);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberProcessor<OrderInfo>() {
                    @Override
                    public void onError(String message) {
                        mView.showProcessDialog(false);
                        mView.showToastAndFinish(R.string.confirm_class_order_error_hint);
                    }

                    @Override
                    public void onNext(OrderInfo orderInfo) {
                        mView.showProcessDialog(false);
                        if (orderInfo != null) {
                            mView.showPriceView(orderInfo);
                        }
                    }
                });
    }
}
