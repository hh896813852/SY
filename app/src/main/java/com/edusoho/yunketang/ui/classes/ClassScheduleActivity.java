package com.edusoho.yunketang.ui.classes;

import android.databinding.ObservableField;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CommonViewPagerAdapter;
import com.edusoho.yunketang.base.BaseActivity;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.MagicIndicatorPageListener;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.ActivityClassScheduleBinding;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.DateUtils;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.widget.materialcalendarview.CalendarDay;
import com.edusoho.yunketang.widget.materialcalendarview.CalendarMode;
import com.edusoho.yunketang.widget.materialcalendarview.MaterialCalendarView;
import com.edusoho.yunketang.widget.materialcalendarview.decorator.DisableDecorator;
import com.edusoho.yunketang.widget.materialcalendarview.decorator.HasCourseDecorator;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Layout(value = R.layout.activity_class_schedule, title = "我的课表")
public class ClassScheduleActivity extends BaseActivity<ActivityClassScheduleBinding> {
    public static final String CLASS_ID = "class_id";
    public String classId;
    private CommonNavigator commonNavigator;
    private CommonNavigator commonNavigator2;

    private TimePickerView pickerView;
    private int calendarHeight;
    private MaterialCalendarView calendarView;
    private List<CalendarDay> hasCourseDays = new ArrayList<>();

    private ClassScheduleFragment notLearnCourseFragment = ClassScheduleFragment.newInstance(0);
    private ClassScheduleFragment hasLearnedCourseFragment = ClassScheduleFragment.newInstance(1);
    public ObservableField<Boolean> hasCourse = new ObservableField<>(true);

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener = () -> {
        if (getDataBinding().vpMain.getCurrentItem() == 0) {
            notLearnCourseFragment.refresh();
        } else {
            hasLearnedCourseFragment.refresh();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        classId = getIntent().getStringExtra(CLASS_ID);
        initView();
        loadData();
    }

    /**
     * 初始化
     */
    private void initView() {
        // 指示器控件
        MagicIndicatorBuilder.MagicIndicatorConfiguration configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(this);
        configuration.labels = new String[]{"未上课", "已上课"};
        configuration.labelTextSize = 15;
        configuration.titleNormalColor = R.color.text_black;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(this, 4f);
        configuration.lineWidth = DensityUtil.dip2px(this, 20f);
        // init ViewPager
        getDataBinding().vpMain.setOffscreenPageLimit(1);
        getDataBinding().vpMain.setAdapter(new CommonViewPagerAdapter(getSupportFragmentManager(), notLearnCourseFragment, hasLearnedCourseFragment));
        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().mainTabIndicator));
        // set MagicIndicator
        commonNavigator = MagicIndicatorBuilder.buildCommonNavigator2(this, configuration, new MagicIndicatorBuilder.OnNavigatorClickListener2() {
            @Override
            public void onNavigatorClickListener2(int index, List<TextView> textViews) {
                for (int i = 0; i < textViews.size(); i++) {
                    if (index == i) {
                        textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        ((ColorTransitionPagerTitleView) commonNavigator2.getPagerTitleView(i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        ((ColorTransitionPagerTitleView) commonNavigator2.getPagerTitleView(i)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        ((ColorTransitionPagerTitleView) commonNavigator2.getPagerTitleView(i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        ((ColorTransitionPagerTitleView) commonNavigator2.getPagerTitleView(i)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
                getDataBinding().vpMain.setCurrentItem(index, true);
                if (index == 0) {
                    notLearnCourseFragment.resetViewPagerHeight();
                } else {
                    hasLearnedCourseFragment.resetViewPagerHeight();
                }
                getDataBinding().scrollView.scrollTo(0, 0);
            }
        });
        getDataBinding().mainTabIndicator.setNavigator(commonNavigator);

        getDataBinding().vpMain.addOnPageChangeListener(new MagicIndicatorPageListener(getDataBinding().tabIndicator));
        // set MagicIndicator
        commonNavigator2 = MagicIndicatorBuilder.buildCommonNavigator2(this, configuration, new MagicIndicatorBuilder.OnNavigatorClickListener2() {
            @Override
            public void onNavigatorClickListener2(int index, List<TextView> textViews) {
                for (int i = 0; i < textViews.size(); i++) {
                    if (index == i) {
                        textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                        ((ColorTransitionPagerTitleView) commonNavigator.getPagerTitleView(i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        ((ColorTransitionPagerTitleView) commonNavigator.getPagerTitleView(i)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                    } else {
                        textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        textViews.get(i).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                        ((ColorTransitionPagerTitleView) commonNavigator.getPagerTitleView(i)).setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                        ((ColorTransitionPagerTitleView) commonNavigator.getPagerTitleView(i)).setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
                    }
                }
                getDataBinding().vpMain.setCurrentItem(index, true);
                if (index == 0) {
                    notLearnCourseFragment.resetViewPagerHeight();
                } else {
                    hasLearnedCourseFragment.resetViewPagerHeight();
                }
            }
        });
        getDataBinding().tabIndicator.setNavigator(commonNavigator2);

        ViewTreeObserver observer = getDataBinding().calendarView.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                getDataBinding().calendarView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                calendarHeight = getDataBinding().calendarView.getHeight();
            }
        });

        // 设置scrollView滑动监听
        getDataBinding().scrollView.setOnScrollListener(scrollY -> getDataBinding().titleIndicator.setVisibility(scrollY > calendarHeight ? View.VISIBLE : View.GONE));

        //日历控件
        calendarView = getDataBinding().calendarView;
        //设置周的文本
        calendarView.setWeekDayLabels(new String[]{"日", "一", "二", "三", "四", "五", "六"});
        //设置Title的年月
        calendarView.setTitleFormatter(day -> {
            StringBuffer buffer = new StringBuffer();
            int yearOne = day.getYear();
            int monthOne = day.getMonth();
            buffer.append(yearOne).append("-").append(monthOne);
            return buffer;
        });
        // 全部不可点击
        calendarView.addDecorator(new DisableDecorator());
        // 点击日历标题，弹出时间选择器
        calendarView.setOnTitleClickListener(v -> {
            if (pickerView != null) {
                pickerView.show();
            }
        });
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(String.format(SYConstants.CLASS_DATE, classId))
                .GET()
                .execute(new SYDataListener<List<String>>() {

                    @Override
                    public void onSuccess(List<String> data) {
                        for (String date : data) {
                            String[] strDate = date.split("-");
                            CalendarDay day = CalendarDay.from(Integer.valueOf(strDate[0]), Integer.valueOf(strDate[1]), Integer.valueOf(strDate[2]));
                            hasCourseDays.add(day);
                        }
                        if (data.size() > 0) {
                            // 刷新日历
                            refreshCalendarView();
                            // 刷新日期选择器
                            refreshDatePickView();
                        }
                    }

                    @Override
                    public void onFail(int status, String failMessage) {
                        super.onFail(status, failMessage);
                    }
                }, new TypeToken<List<String>>() {
                });
    }

    /**
     * 刷新时间选择器
     */
    private void refreshDatePickView() {
        CalendarDay firstDay = hasCourseDays.get(0);
        CalendarDay lastDay = hasCourseDays.get(hasCourseDays.size() - 1);

        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //正确设置方式 原因：注意事项有说明
        startDate.set(firstDay.getYear(), firstDay.getMonth() - 1, 1);
        endDate.set(lastDay.getYear(), lastDay.getMonth() - 1, DateUtils.getLastDayOfMonth(lastDay.getYear(), lastDay.getMonth()));

        // 时间选择器
        TimePickerBuilder timePicker = new TimePickerBuilder(this, (date, v) -> {
            // 选中事件回调
            String[] selectedDate1 = DateUtils.formatDate(date.getTime(), "yyyy-MM").split("-");
            calendarView.setCurrentDate(CalendarDay.from(Integer.valueOf(selectedDate1[0]), Integer.valueOf(selectedDate1[1]), 1));
        });
        timePicker.setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setLayoutRes(R.layout.sy_pickerview_time, v -> {
                    v.findViewById(R.id.btnSubmit).setOnClickListener(v1 -> {
                        pickerView.returnData();
                        pickerView.dismiss();
                    });
                    v.findViewById(R.id.btnCancel).setOnClickListener(v12 -> pickerView.dismiss());
                })
                .setDecorView(findViewById(R.id.containerLayout)) // 防止虚拟导航栏覆盖在上面
                .setCancelText("")          // 取消按钮文字
                .setSubmitText("确认")      // 确认按钮文字
                .setContentTextSize(18)     // 滚轮文字大小
                .setTitleSize(16)           // 标题文字大小
                .setTitleText("选择年月")    // 标题文字
                .setOutSideCancelable(true) // 点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)            // 是否循环滚动
                .setTitleColor(Color.BLACK) // 标题文字颜色
                .setDate(selectedDate)      // 如果不设置的话，默认是系统时间
                .setRangDate(startDate, endDate)// 起始终止年月日设定
                .setLabel("", "", "", "", "", "")// 默认设置为年月日时分秒
                .isCenterLabel(false) // 是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)      // 是否显示为对话框样式
                .build();
        pickerView = timePicker.build();
    }

    /**
     * 刷新日历
     */
    private void refreshCalendarView() {
        CalendarDay firstDay = hasCourseDays.get(0);
        CalendarDay lastDay = hasCourseDays.get(hasCourseDays.size() - 1);
        calendarView.state()
                .edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                // 设置一周的第一天是周日还是周一
                .setFirstDayOfWeek(DayOfWeek.of(1))
                // 设置日期范围
                .setMinimumDate(LocalDate.of(firstDay.getYear(), firstDay.getMonth(), 1))
                .setMaximumDate(LocalDate.of(lastDay.getYear(), lastDay.getMonth(), DateUtils.getLastDayOfMonth(lastDay.getYear(), lastDay.getMonth())))
                .commit();
        // 有课的日期
        calendarView.addDecorator(new HasCourseDecorator(this, hasCourseDays));
    }

    // Map<Integer,Boolean> 的替代类
    private SparseBooleanArray statusList = new SparseBooleanArray();

    /**
     * 未上课没有数据，已上课有数据。则显示已上课列表
     * 如果都没有数据，展示该班级尚未排课
     *
     * @param status  0、未上课 1、已上课
     * @param hasData 是否有数据
     */
    public void judgeDataSize(int status, boolean hasData) {
        statusList.put(status, hasData);
        if (statusList.size() == 2) {
            if (!statusList.get(0) && statusList.get(1)) {
                getDataBinding().vpMain.setCurrentItem(1);
                hasLearnedCourseFragment.resetViewPagerHeight();
                hasCourse.set(true);
            } else if (!statusList.get(0) && !statusList.get(1)) {
                hasCourse.set(false);
            } else {
                hasCourse.set(true);
            }
        } else {
            hasCourse.set(true);
        }
    }
}
