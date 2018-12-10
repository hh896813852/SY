package com.edusoho.yunketang.ui.course;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.FragmentBriefIntroBinding;
import com.edusoho.yunketang.edu.bean.CourseProject;
import com.edusoho.yunketang.edu.bean.innerbean.Teacher;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.widget.FlowLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by zy on 2018/11/9 0009.
 */
@Layout(value = R.layout.fragment_brief_intro)
public class BriefIntroFragment extends BaseFragment<FragmentBriefIntroBinding> {

    private CourseProject courseProject;

    public ObservableField<String> courseTitle = new ObservableField<>();
    public ObservableField<String> studentNum = new ObservableField<>();
    public ObservableField<Integer> starNum = new ObservableField<>(0);
    public ObservableField<Boolean> isFree = new ObservableField<>(false);
    public ObservableField<String> priceText = new ObservableField<>();
    public ObservableField<Boolean> hasService = new ObservableField<>(false);
    public ObservableField<CharSequence> briefText = new ObservableField<>();
    public ObservableField<String> teacherAvatar = new ObservableField<>();
    public ObservableField<String> teacherName = new ObservableField<>();
    public ObservableField<String> teacherInfo = new ObservableField<>();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 刷新界面
     */
    public void refreshView(CourseProject courseProject) {
        this.courseProject = courseProject;
        courseTitle.set(courseProject.courseSet.title);
        studentNum.set(courseProject.courseSet.studentNum + "人");
        starNum.set(new BigDecimal(courseProject.rating).setScale(0, RoundingMode.HALF_UP).intValue());
        briefText.set(TextUtils.isEmpty(Html.fromHtml(courseProject.courseSet.summary)) ? "暂无简介" : Html.fromHtml(courseProject.courseSet.summary));
        // 显示价格
        showPriceView();
        // 显示服务
        showService(courseProject.services);
        // 显示讲师
        showTeacher();
    }

    /**
     * 显示服务
     */
    private void showService(CourseProject.Service[] services) {
        hasService.set(services.length > 0);
        if (getDataBinding() == null) {
            return;
        }
        // 清空子View
        getDataBinding().flowLayout.removeAllViews();
        for (CourseProject.Service service : services) {
            TextView serviceView = new TextView(getSupportedActivity());
            serviceView.setText(service.fullName);
            serviceView.setTextSize(14);
            serviceView.setTextColor(ContextCompat.getColor(getSupportedActivity(), R.color.text_black));
            // 设置drawableLeft
            Drawable drawableLeft = ContextCompat.getDrawable(getSupportedActivity(), R.drawable.icon_picked);
            serviceView.setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null);
            serviceView.setCompoundDrawablePadding(5);
            // 设置Margin
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, DensityUtil.dip2px(getSupportedActivity(), 12), DensityUtil.dip2px(getSupportedActivity(), 10));//4个参数按顺序分别是左上右下
            serviceView.setLayoutParams(layoutParams);
            getDataBinding().flowLayout.addView(serviceView);
        }
    }

    /**
     * 显示价格
     */
    private void showPriceView() {
        isFree.set(courseProject.isFree == 1);
        if (courseProject.isFree == 1) { // 免费
            priceText.set("免费");
        } else {
            if (courseProject.price2.getPrice() == courseProject.originPrice2.getPrice()) {
                priceText.set(courseProject.price2.getPriceWithUnit());
                return;
            }
            priceText.set(courseProject.price2.getPriceWithUnit());
        }
    }

    /**
     * 显示讲师
     */
    private void showTeacher() {
        Teacher teacher = courseProject.teachers[0];
        teacherAvatar.set(teacher.avatar.middle);
        teacherName.set(teacher.nickname);
        teacherInfo.set(teacher.title);
    }
}
