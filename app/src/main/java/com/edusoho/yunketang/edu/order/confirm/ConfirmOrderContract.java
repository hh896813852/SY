package com.edusoho.yunketang.edu.order.confirm;

import com.edusoho.yunketang.edu.base.BasePresenter;
import com.edusoho.yunketang.edu.base.BaseView;
import com.edusoho.yunketang.edu.bean.Classroom;
import com.edusoho.yunketang.edu.bean.CourseSet;
import com.edusoho.yunketang.edu.bean.OrderInfo;

/**
 * Created by DF on 2017/4/5.
 */

public interface ConfirmOrderContract {

    interface View extends BaseView<Presenter> {

        void showPriceView(OrderInfo orderInfo);

        void showProcessDialog(boolean isShow);

        void showTopView(CourseSet courseSet);

        void showTopView(Classroom classroom);

        void showToastAndFinish(int content);

        void showToastAndFinish(String content);
    }

    interface Presenter extends BasePresenter {
    }

}
