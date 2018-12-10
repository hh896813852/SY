package com.edusoho.yunketang.edu.order.confirm;


import android.text.TextUtils;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.api.CourseSetApi;
import com.edusoho.yunketang.edu.api.OrderApi;
import com.edusoho.yunketang.edu.base.BaseActivity;
import com.edusoho.yunketang.edu.bean.CourseSet;
import com.edusoho.yunketang.edu.bean.OrderInfo;
import com.edusoho.yunketang.edu.http.HttpUtils;
import com.edusoho.yunketang.edu.http.SubscriberProcessor;
import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.navi.NaviLifecycle;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by DF on 2017/4/6.
 */

public class ConfirmOrderPresenter implements ConfirmOrderContract.Presenter {

    protected ConfirmOrderContract.View mView;
    private   int                       mCourseId;
    private   int                       mCourseSetId;

    protected final LifecycleProvider<ActivityEvent> mFragmentLifeProvider;

    public ConfirmOrderPresenter(ConfirmOrderContract.View view, int courseSetId, int courseId) {
        this.mView = view;
        mFragmentLifeProvider = NaviLifecycle.createActivityLifecycleProvider((BaseActivity) view);
        this.mCourseId = courseId;
        this.mCourseSetId = courseSetId;
    }

    @Override
    public void subscribe() {
        HttpUtils.getInstance()
                .createApi(CourseSetApi.class)
                .getCourseSet(mCourseSetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mFragmentLifeProvider.<CourseSet>bindToLifecycle())
                .doOnNext(new Action1<CourseSet>() {
                    @Override
                    public void call(CourseSet courseSet) {
                        mView.showTopView(courseSet);
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<CourseSet, Observable<OrderInfo>>() {
                    @Override
                    public Observable<OrderInfo> call(CourseSet courseSet) {
                        return HttpUtils.getInstance()
                                .addTokenHeader(TextUtils.isEmpty(SYApplication.getInstance().token) ? SYApplication.getInstance().getUser().syzxToken : SYApplication.getInstance().token)
                                .createApi(OrderApi.class)
                                .postOrderInfo("course", mCourseId);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SubscriberProcessor<OrderInfo>() {
                    @Override
                    public void onError(String e) {
                        mView.showProcessDialog(false);
                        mView.showToastAndFinish(R.string.confirm_order_error_hint);
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

    @Override
    public void unsubscribe() {

    }

}
