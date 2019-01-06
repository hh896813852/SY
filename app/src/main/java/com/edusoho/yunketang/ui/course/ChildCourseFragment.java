package com.edusoho.yunketang.ui.course;

import android.content.Intent;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.edusoho.yunketang.helper.AppPreferences;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.FirstCourseAdapter;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Course;
import com.edusoho.yunketang.databinding.FragmentOnlineCourseBinding;
import com.edusoho.yunketang.edu.WebViewActivity;
import com.edusoho.yunketang.edu.utils.SharedPreferencesHelper;
import com.edusoho.yunketang.edu.utils.SharedPreferencesUtils;
import com.edusoho.yunketang.helper.ListViewHelper;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.MainTabActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.StringUtils;
import com.edusoho.yunketang.utils.glide.GlideRoundTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/11/8 0008.
 */
@Layout(value = R.layout.fragment_online_course)
public class ChildCourseFragment extends BaseFragment<FragmentOnlineCourseBinding> {
    private int status; // 1、上元在线 2、上元会计
    private static final int COURSE = 0;
    private static final int LIVE = 1;
    private static final int CLASSROOM = 2;
    private String showStudentNumEnabled;

    private Course firstCourse;
    public List<Course> courseList = new ArrayList<>();
    private LayoutInflater layoutInflater;

    public ObservableField<String> title = new ObservableField<>();

    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            LinearLayout containerLayout = view.findViewById(R.id.containerLayout);
            containerLayout.removeAllViews();
            for (int i = 0; i < courseList.get(position).data.size(); i++) {
                if (i > 2) { // 每个行业只展示3个课程
                    break;
                }
                Course.Discovery discovery = courseList.get(position).data.get(i);
                View innerView = layoutInflater.inflate(R.layout.item_inner_course, null);
                ImageView courseImage = innerView.findViewById(R.id.courseImage);
                TextView discountView = innerView.findViewById(R.id.discountView);
                TextView courseTitleView = innerView.findViewById(R.id.courseTitleView);
                TextView moneyView = innerView.findViewById(R.id.moneyView);
                TextView joinCountView = innerView.findViewById(R.id.joinCountView);
                Glide.with(getSupportedActivity()).load(discovery.cover.middle).placeholder(R.drawable.bg_load_default_4x3).transform(new GlideRoundTransform(getSupportedActivity(), 4)).into(courseImage);
                courseTitleView.setText(discovery.title);
                if (COURSE == getItemType(position) || LIVE == getItemType(position)) {
                    if (discovery.minCoursePrice > 0) {
                        moneyView.setText(discovery.minCoursePrice2.getPriceWithUnit());
                    } else {
                        moneyView.setText("免费");
                    }
                    if (discovery.discount == 10F) {
                        discountView.setVisibility(View.GONE);
                    } else {
                        discountView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (discovery.price > 0) {
                        moneyView.setText(discovery.price2.getPriceWithUnit());
                    } else {
                        moneyView.setText("免费");
                    }
                }
                joinCountView.setText(discovery.studentNum + "人参与");
                // item点击事件
                innerView.setOnClickListener(v -> {
                    // 跳转课程详情页
                    Intent intent = new Intent(getSupportedActivity(), CourseDetailsActivity.class);
                    intent.putExtra(CourseDetailsActivity.COURSE_TYPE, status);
                    intent.putExtra(CourseDetailsActivity.COURSE_ID, discovery.id);
                    getSupportedActivity().startActivity(intent);
                });
                containerLayout.addView(innerView);
            }
            view.findViewById(R.id.lookMoreView).setOnClickListener(v -> {
                setMoreClickListener(courseList.get(position).orderType == null ? "recommend" : courseList.get(position).orderType, courseList.get(position).type, courseList.get(position).categoryId);
            });
            return view;
        }

        private int getItemType(int position) {
            switch (courseList.get(position).type) {
                case "course":
                    return COURSE;
                case "classroom":
                    return CLASSROOM;
                case "live":
                    return LIVE;
            }
            return COURSE;
        }
    };

    public static ChildCourseFragment newInstance(int status) {
        ChildCourseFragment fragment = new ChildCourseFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        status = getArguments().getInt("status");
        adapter.init(getSupportedActivity(), R.layout.item_course, courseList);
        layoutInflater = LayoutInflater.from(getSupportedActivity());
        loadData();
        // 有缓存，使用缓存数据
        List<Course> cacheData = AppPreferences.getHomeData(status);
        if (cacheData != null && cacheData.size() > 0) {
            refreshData(true, cacheData);
        }

        showStudentNumEnabled = SharedPreferencesUtils.getInstance(getSupportedActivity())
                .open(SharedPreferencesHelper.CourseSetting.XML_NAME)
                .getString(SharedPreferencesHelper.CourseSetting.SHOW_STUDENT_NUM_ENABLED_KEY);
    }

    /**
     * 加载数据
     */
    private void loadData() {
        SYDataTransport.create(status == 1 ? SYConstants.ONLINE_CHANNELS : SYConstants.ACCOUNTANT_CHANNELS, false)
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        courseList.clear();
                        // 将数据中富文本内容转成可被解析的json数据
                        String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                        List<Course> courses = JsonUtil.fromJson(json, new TypeToken<List<Course>>() {
                        });
                        if (courses != null && courses.size() > 0) {
                            // 更新缓存数据
                            AppPreferences.saveHomeData(status, courses);
                            refreshData(false, courses);
                        }
                    }
                });
    }

    /**
     * 刷新数据并刷新界面
     */
    private void refreshData(boolean isCache, List<Course> courses) {
        for (int i = 0; i < courses.size(); i++) {
            if (i == 0) {
                firstCourse = courses.get(i);
            } else {
                if (courses.get(i).data != null && courses.get(i).data.size() > 0) {
                    courseList.add(courses.get(i));
                }
            }
        }
        // 重新赋值类型和顺序，用于item菜单导航
        for (int i = 0; i < courseList.size(); i++) {
            Course course = courseList.get(i);
            course.courseType = status;
            course.courseSort = i;
        }
        if (getActivity() != null) {
            ((MainTabActivity) getActivity()).courseFragment.list.addAll(courseList);
            ((MainTabActivity) getActivity()).courseFragment.adapter.notifyDataSetChanged();
        }
        adapter.notifyDataSetChanged();
        if (isCache) {
            new Handler().postDelayed(() -> {
                // 刷新界面
                refreshView();
                // 如果在当前页面加载完成，则按当前页面来设置高度
                if (getActivity() != null && status == ((MainTabActivity) getActivity()).courseFragment.getDataBinding().vpMain.getCurrentItem() + 1) {
                    resetViewPagerHeight();
                }
            }, 200);
        } else {
            // 刷新界面
            refreshView();
            // 如果在当前页面加载完成，则按当前页面来设置高度
            if (getActivity() != null && status == ((MainTabActivity) getActivity()).courseFragment.getDataBinding().vpMain.getCurrentItem() + 1) {
                resetViewPagerHeight();
            }
        }
    }

    /**
     * scrollview嵌套viewpager，导致viewpager内容不可见。
     * 需要给定viewpager高度。
     * 设置courseFragment的viewpager高度，
     */
    public void resetViewPagerHeight() {
        if (getActivity() != null) {
            int listViewHeight = ListViewHelper.getTotalHeightOfListView(getDataBinding().listView);
            LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) ((MainTabActivity) getActivity()).courseFragment.getDataBinding().vpMain.getLayoutParams();
            linearParams.height = listViewHeight + DensityUtil.dip2px(getSupportedActivity(), 230); // 230 = recyclerView + 一个头部 + 一个底部
            ((MainTabActivity) getActivity()).courseFragment.getDataBinding().vpMain.setLayoutParams(linearParams);
        }
    }

    /**
     * 获得当前位置item到listView顶部距离
     *
     * @param index item下标
     * @return 距离
     */
    public int getItemHeight(int index) {
        return ListViewHelper.getHeightFromItemTopToListViewTop(getDataBinding().listView, index);
    }

    /**
     * 刷新界面
     */
    private void refreshView() {
        title.set(firstCourse.title);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getSupportedActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
        getDataBinding().recycleView.setLayoutManager(layoutManager);
        FirstCourseAdapter adapter = new FirstCourseAdapter(getSupportedActivity(), firstCourse.data);
        getDataBinding().recycleView.setAdapter(adapter);
        // item点击事件
        adapter.setOnItemClickListener((view, position) -> {
            // 跳转课程详情页
            Intent intent = new Intent(getSupportedActivity(), CourseDetailsActivity.class);
            intent.putExtra(CourseDetailsActivity.COURSE_TYPE, status);
            intent.putExtra(CourseDetailsActivity.COURSE_ID, firstCourse.data.get(position).id);
            getSupportedActivity().startActivity(intent);
        });
    }

    /**
     * 第一栏查看更多
     */
    public View.OnClickListener onLookMoreClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setMoreClickListener(firstCourse.orderType == null ? "recommend" : firstCourse.orderType, firstCourse.type, firstCourse.categoryId);
        }
    };

    public void setMoreClickListener(String orderType, String type, int categoryId) {
        final String url;
        switch (type) {
            case "course":
                url = String.format(
                        SYConstants.MOBILE_APP_URL,
                        SYApplication.getInstance().schoolHost,
                        String.format(SYConstants.MOBILE_WEB_COURSES, categoryId, orderType));
                break;
            case "live":
                url = String.format(
                        SYConstants.MOBILE_APP_URL,
                        SYApplication.getInstance().schoolHost,
                        String.format(SYConstants.MOBILE_WEB_LIVE_COURSES, categoryId, orderType));
                break;
            case "classroom":
            default:
                url = String.format(
                        SYConstants.MOBILE_APP_URL,
                        SYApplication.getInstance().schoolHost,
                        String.format(SYConstants.MOBILE_WEB_CLASSROOMS, categoryId, orderType));
        }

        Intent intent = new Intent(getSupportedActivity(), WebViewActivity.class);
//        intent.putExtra(SYConstants.WEB_URL, url + ("1".equals(showStudentNumEnabled) ? "&shouldShowStudentNum=1" : ""));
        intent.putExtra(SYConstants.WEB_URL, url + "&shouldShowStudentNum=1");
        startActivity(intent);
    }
}
