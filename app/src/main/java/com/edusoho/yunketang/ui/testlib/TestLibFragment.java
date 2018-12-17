package com.edusoho.yunketang.ui.testlib;

import android.content.Intent;
import android.databinding.ObservableField;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.SYBaseAdapter;
import com.edusoho.yunketang.base.BaseDialog;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.MagicIndicatorBuilder;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.bean.Business;
import com.edusoho.yunketang.bean.BusinessModule;
import com.edusoho.yunketang.bean.EducationCourse;
import com.edusoho.yunketang.databinding.FragmentTestLibBinding;
import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.ui.login.LoginActivity;
import com.edusoho.yunketang.utils.DensityUtil;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.utils.statusbar.StatusBarUtil;
import com.edusoho.yunketang.widget.CircleBarView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author huhao on 2018/7/4
 */
@Layout(value = R.layout.fragment_test_lib)
public class TestLibFragment extends BaseFragment<FragmentTestLibBinding> {
    public ObservableField<Boolean> isFirstSelect = new ObservableField<>(false);

    private List<String> tagList1 = new ArrayList<>();
    private List<String> tagList2 = new ArrayList<>();
    private List<String> tagList3 = new ArrayList<>();
    private List<List<List<EducationCourse>>> allCourseList = new ArrayList<>(); // 所有课程列表
    private List<Business> businessList = new ArrayList<>(); // 行业列表
    private List<EducationCourse> levelList = new ArrayList<>(); // 职业/等级列表
    private List<EducationCourse> courseList = new ArrayList<>();// 课程列表
    private EducationCourse selectedCourse; // 选择的课程

    private List<String> labels = new ArrayList<>();
    private MagicIndicatorBuilder.MagicIndicatorConfiguration configuration;
    private CommonNavigator commonNavigator;

    public ObservableField<String> businessName = new ObservableField<>(); // 选择的行业
    public ObservableField<String> levelName = new ObservableField<>();    // 选择的职业等级

    private List<BusinessModule> moduleList = new ArrayList<>();
    public SYBaseAdapter adapter = new SYBaseAdapter() {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            ImageView moduleImage = view.findViewById(R.id.moduleImage);
            switch (moduleList.get(position).modelName) {
                case "每日一练":
                    moduleImage.setImageResource(R.drawable.bg_exercise_mryl);
                    break;
                case "每周一测":
                    moduleImage.setImageResource(R.drawable.bg_exercise_mzyc);
                    break;
                case "模拟考试":
                    moduleImage.setImageResource(R.drawable.bg_exercise_mnks);
                    break;
                case "历年真题":
                    moduleImage.setImageResource(R.drawable.bg_exercise_lnzt);
                    break;
                case "章节练习":
                    moduleImage.setImageResource(R.drawable.bg_exercise_zjlx);
                    break;
                case "专项练习":
                    moduleImage.setImageResource(R.drawable.bg_exercise_zxlx);
                    break;
                case "听力训练":
                    moduleImage.setImageResource(R.drawable.bg_exercise_tlxl);
                    break;
                case "阅读练习":
                    moduleImage.setImageResource(R.drawable.bg_exercise_ydlx);
                    break;
                case "我的错题":
                    moduleImage.setImageResource(R.drawable.bg_exercise_wdct);
                    break;
            }
            return view;
        }
    };
    public AdapterView.OnItemClickListener onItemClick = (parent, view, position, id) -> {
        if (!SYApplication.getInstance().isLogin()) {
            BaseDialog dialog = DialogUtil.showAnimationDialog(getSupportedActivity(), R.layout.dialog_not_login);
            dialog.findViewById(R.id.cancelView).setOnClickListener(v -> dialog.dismiss());
            dialog.findViewById(R.id.loginView).setOnClickListener(v -> {
                startActivity(new Intent(getSupportedActivity(), LoginActivity.class));
                dialog.dismiss();
            });
            return;
        }
        int modelId = moduleList.get(position).modelId;
        switch (moduleList.get(position).modelName) {
            case "每日一练":
                Intent intent1 = new Intent(getSupportedActivity(), PracticeActivity.class);
                intent1.putExtra(PracticeActivity.MODULE_ID, modelId);
                intent1.putExtra(PracticeActivity.SELECTED_COURSE, selectedCourse);
                startActivity(intent1);
                break;
            case "每周一测":
                Intent intent2 = new Intent(getSupportedActivity(), PracticeActivity.class);
                intent2.putExtra(PracticeActivity.MODULE_ID, modelId);
                intent2.putExtra(PracticeActivity.SELECTED_COURSE, selectedCourse);
                startActivity(intent2);
                break;
            case "模拟考试":
                break;
            case "历年真题":
                Intent intent4 = new Intent(getSupportedActivity(), PastExamActivity.class);
                intent4.putExtra(PracticeActivity.MODULE_ID, modelId);
                intent4.putExtra(PracticeActivity.SELECTED_COURSE, selectedCourse);
                startActivity(intent4);
                break;
            case "章节练习":
                Intent intent5 = new Intent(getSupportedActivity(), PracticeActivity.class);
                intent5.putExtra(PracticeActivity.MODULE_ID, modelId);
                intent5.putExtra(PracticeActivity.SELECTED_COURSE, selectedCourse);
                startActivity(intent5);
                break;
            case "专项练习":
                break;
            case "听力训练":
                break;
            case "阅读练习":
                break;
            case "我的错题":
                Intent intent9 = new Intent(getSupportedActivity(), MyFaultsActivity.class);
                startActivity(intent9);
                break;
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化
        initView();
        // 加载行业类型
        loadBusinessType();
        // 加载所有行业、职业等级的课程
        loadAllCourse();
    }

    private void initView() {
        isFirstSelect.set(AppPreferences.getSelectedCourse() == null || AppPreferences.getSelectedCourse().businessId == 0);
        if (isFirstSelect.get() && getSupportedActivity() != null) {
            StatusBarUtil.setImmersiveStatusBar(getSupportedActivity(), true);
        }

        adapter.init(getSupportedActivity(), R.layout.item_business_module, moduleList);

        selectedCourse = new EducationCourse();

        configuration = new MagicIndicatorBuilder.MagicIndicatorConfiguration(getSupportedActivity());
        configuration.isAdjustMode = false;
        configuration.lineMode = LinePagerIndicator.MODE_EXACTLY;
        configuration.lineHeight = DensityUtil.dip2px(getSupportedActivity(), 4f);
        configuration.lineWidth = DensityUtil.dip2px(getSupportedActivity(), 20f);
        configuration.lineColor = Color.parseColor("#ffffff");
        configuration.titleNormalColor = R.color.text_white;
        configuration.titleSelectedColor = R.color.text_white;
        configuration.labelTextSize = 15;
        configuration.labels = labels.toArray(new String[0]);

        // set MagicIndicator
        commonNavigator = MagicIndicatorBuilder.buildCommonNavigator(getSupportedActivity(), configuration, index -> {
            getDataBinding().mainTabIndicator.onPageSelected(index);
            getDataBinding().mainTabIndicator.onPageScrolled(index, 0, 0);
            selectedCourse.courseId = courseList.get(index).courseId;
            selectedCourse.courseName = courseList.get(index).courseName;
            AppPreferences.setSelectedCourse(selectedCourse);
        });
        getDataBinding().mainTabIndicator.setNavigator(commonNavigator);

        DecimalFormat decimalFormat = new DecimalFormat("0");
        // 设置根据进度改变文字的TextView
        getDataBinding().circleView.setTextView(getDataBinding().progressText);
        // 设置动画进度监听来改变文字和进度条颜色
        getDataBinding().circleView.setOnAnimationListener(new CircleBarView.OnAnimationListener() {
            @Override
            public String howToChangeText(float interpolatedTime, float progressNum, float maxNum) {
                return decimalFormat.format(interpolatedTime * progressNum / maxNum * 100);
            }

            @Override
            public void howToChangeProgressColor(Paint paint, float interpolatedTime, float updateNum, float maxNum) {

            }
        });
        // 设置最大值
        getDataBinding().circleView.setMaxNum(100);
        // 设置进度和动画执行时间，并开始动画
        getDataBinding().circleView.setProgressNum(80, 2000);

        // 行业选择
        getDataBinding().tagLayout1.setOnItemClickListener((View v, String tag, int position) -> {
            // 获取当前business
            selectedCourse.businessId = businessList.get(position).businessId;
            selectedCourse.businessName = businessList.get(position).businessName;
            // 获取当前职业等级列表
            levelList = getLevelList(selectedCourse.businessId);
            // 默认获取第一个职业等级
            selectedCourse.levelId = levelList.get(0).levelId;
            selectedCourse.levelName = levelList.get(0).levelName;
            // 刷新职业等级列表
            refreshLevel();
            // 默认获取第一个职业等级的课程列表
            courseList = getCourseList(selectedCourse.businessId, selectedCourse.levelId);
            // 默认获取第一个课程
            selectedCourse.courseId = courseList.get(0).courseId;
            selectedCourse.courseName = courseList.get(0).courseName;
            // 刷新课程列表
            refreshCourse();
        });
        // 职业/等级选择
        getDataBinding().tagLayout2.setOnItemClickListener((View v, String tag, int position) -> {
            // 获取当前职业等级
            selectedCourse.levelId = levelList.get(position).levelId;
            selectedCourse.levelName = levelList.get(position).levelName;
            // 获取当前课程列表
            courseList = getCourseList(selectedCourse.businessId, selectedCourse.levelId);
            // 默认获取第一个课程
            selectedCourse.courseId = courseList.get(0).courseId;
            selectedCourse.courseName = courseList.get(0).courseName;
            // 刷新课程列表
            refreshCourse();
        });
        // 课程选择
        getDataBinding().tagLayout3.setOnItemClickListener((View v, String tag, int position) -> {
            // 获取当前课程
            selectedCourse.courseId = courseList.get(position).courseId;
            selectedCourse.courseName = courseList.get(position).courseName;
        });
    }

    /**
     * 加载行业类型
     */
    private void loadBusinessType() {
        SYDataTransport.create(SYConstants.BUSINESS_TYPE)
                .execute(new SYDataListener<List<Business>>() {

                    @Override
                    public void onSuccess(List<Business> data) {
                        businessList = data;
                        refreshBusiness();
                    }
                }, new TypeToken<List<Business>>() {
                });
    }

    /**
     * 加载所有行业、职业等级的课程
     */
    private void loadAllCourse() {
        SYDataTransport.create(SYConstants.ALL_COURSE)
                .execute(new SYDataListener<List<List<List<EducationCourse>>>>() {

                    @Override
                    public void onSuccess(List<List<List<EducationCourse>>> data) {
                        allCourseList = data;
                        // 用户未选择的情况下，以第一个课程为默认课程
                        EducationCourse defaultCourse = allCourseList.get(0).get(0).get(0);
                        // 获取默认行业
                        selectedCourse.businessId = AppPreferences.getSelectedCourse() != null && AppPreferences.getSelectedCourse().businessId != 0 ? AppPreferences.getSelectedCourse().businessId : defaultCourse.businessId;
                        selectedCourse.businessName = AppPreferences.getSelectedCourse() != null && !TextUtils.isEmpty(AppPreferences.getSelectedCourse().businessName) ? AppPreferences.getSelectedCourse().businessName : defaultCourse.businessName;
                        // 获取默认等级列表
                        levelList = getLevelList(selectedCourse.businessId);
                        // 获取默认职业等级
                        selectedCourse.levelId = AppPreferences.getSelectedCourse() != null && AppPreferences.getSelectedCourse().levelId != 0 ? AppPreferences.getSelectedCourse().levelId : defaultCourse.levelId;
                        selectedCourse.levelName = AppPreferences.getSelectedCourse() != null && !TextUtils.isEmpty(AppPreferences.getSelectedCourse().levelName) ? AppPreferences.getSelectedCourse().levelName : defaultCourse.levelName;
                        // 刷新职业等级列表
                        refreshLevel();
                        // 获取默认职业等级的课程列表
                        courseList = getCourseList(selectedCourse.businessId, selectedCourse.levelId);
                        // 获取默认课程
                        selectedCourse.courseId = AppPreferences.getSelectedCourse() != null && AppPreferences.getSelectedCourse().courseId != 0 ? AppPreferences.getSelectedCourse().courseId : defaultCourse.courseId;
                        selectedCourse.courseName = AppPreferences.getSelectedCourse() != null && !TextUtils.isEmpty(AppPreferences.getSelectedCourse().courseName) ? AppPreferences.getSelectedCourse().courseName : defaultCourse.courseName;
                        // 刷新课程列表
                        refreshCourse();
                        // 刷新界面
                        refreshView();
                        // 加载行业题库模块
                        loadBusinessModule();
                    }
                }, new TypeToken<List<List<List<EducationCourse>>>>() {
                });
    }

    /**
     * 加载当前行业的题库模块
     */
    private void loadBusinessModule() {
        SYDataTransport.create(SYConstants.MODULE_QUERY)
                .addParam("businessType", selectedCourse.businessId)
                .execute(new SYDataListener<List<BusinessModule>>() {

                    @Override
                    public void onSuccess(List<BusinessModule> data) {
                        moduleList.clear();
                        moduleList.addAll(data);
                        // 自己添加我的错题模块
                        BusinessModule faultModule = new BusinessModule();
                        faultModule.modelName = "我的错题";
                        moduleList.add(faultModule);
                        adapter.notifyDataSetChanged();
                    }
                }, new TypeToken<List<BusinessModule>>() {
                });
    }

    /**
     * 获取职业等级列表
     */
    private List<EducationCourse> getLevelList(int businessId) {
        levelList.clear();
        List<List<EducationCourse>> currentBusinessCourse = getCurrentBusinessCourse(businessId);
        if (currentBusinessCourse != null) {
            for (List<EducationCourse> childList : currentBusinessCourse) {
                for (EducationCourse course : childList) {
                    EducationCourse level = new EducationCourse();
                    level.businessId = course.businessId;
                    level.levelId = course.levelId;
                    level.levelName = course.levelName;
                    levelList.add(level);
                    break;
                }
            }
        }
        return levelList;
    }

    /**
     * 获取课程列表
     */
    private List<EducationCourse> getCourseList(int businessId, int levelId) {
        courseList.clear();
        List<List<EducationCourse>> currentBusinessCourse = getCurrentBusinessCourse(businessId);
        if (currentBusinessCourse != null) {
            for (List<EducationCourse> childList : currentBusinessCourse) {
                for (EducationCourse course : childList) {
                    if (course.levelId == levelId) {
                        courseList.addAll(childList);
                        return courseList;
                    }
                }
            }
        }
        return courseList;
    }

    /**
     * 获取当前行业所有课程
     */
    private List<List<EducationCourse>> getCurrentBusinessCourse(int businessId) {
        for (List<List<EducationCourse>> parentList : allCourseList) {
            for (List<EducationCourse> childList : parentList) {
                for (EducationCourse course : childList) {
                    if (course.businessId == businessId) {
                        return parentList;
                    }
                }
            }
        }
        return null;
    }

    /**
     * 刷新行业
     */
    private void refreshBusiness() {
        tagList1.clear();
        for (Business business : businessList) {
            tagList1.add(business.businessName);
        }
        getDataBinding().tagLayout1.setTags(tagList1, AppPreferences.getSelectedCourse() == null ? 0 : AppPreferences.getSelectedCourse().businessId - 1);
    }

    /**
     * 刷新职业等级
     */
    private void refreshLevel() {
        tagList2.clear();
        for (EducationCourse level : levelList) {
            tagList2.add(level.levelName);
        }
        getDataBinding().tagLayout2.setTags(tagList2);
        if (AppPreferences.getSelectedCourse() != null) {
            for (int i = 0; i < levelList.size(); i++) {
                if (levelList.get(i).businessId == AppPreferences.getSelectedCourse().businessId && levelList.get(i).levelId == AppPreferences.getSelectedCourse().levelId) {
                    getDataBinding().tagLayout2.setTags(tagList2, i);
                    break;
                }
            }
        }
    }

    /**
     * 刷新课程
     */
    private void refreshCourse() {
        tagList3.clear();
        for (EducationCourse course : courseList) {
            tagList3.add(course.courseName);
        }
        getDataBinding().tagLayout3.setTags(tagList3);
        if (AppPreferences.getSelectedCourse() != null) {
            for (int i = 0; i < courseList.size(); i++) {
                if (courseList.get(i).businessId == AppPreferences.getSelectedCourse().businessId && courseList.get(i).levelId == AppPreferences.getSelectedCourse().levelId && courseList.get(i).courseId == AppPreferences.getSelectedCourse().courseId) {
                    getDataBinding().tagLayout3.setTags(tagList3, i);
                    break;
                }
            }
        }
    }

    /**
     * 刷新界面
     */
    private void refreshView() {
        businessName.set(selectedCourse.businessName);
        levelName.set(selectedCourse.levelName);

        labels.clear();
        labels.addAll(tagList3);
        configuration.labels = labels.toArray(new String[0]);
        commonNavigator.notifyDataSetChanged();
        for (int i = 0; i < courseList.size(); i++) {
            if (courseList.get(i).courseId == selectedCourse.courseId) {
                getDataBinding().mainTabIndicator.onPageSelected(i);
                getDataBinding().mainTabIndicator.onPageScrolled(i, 0, 0);
            }
        }
    }

    /**
     * 课程类型选择
     */
    public View.OnClickListener onCoursePickClicked = v -> {
        getDataBinding().layout.setVisibility(View.VISIBLE);
        getDataBinding().bgLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getSupportedActivity(), R.anim.slide_out_from_right);
        getDataBinding().layout.startAnimation(animation);

        refreshCourse();
    };

    /**
     * 背景点击关闭菜单
     */
    public View.OnClickListener onBgClicked = v -> {
        Animation animation = AnimationUtils.loadAnimation(getSupportedActivity(), R.anim.slide_in_from_right);
        getDataBinding().layout.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                getDataBinding().layout.setVisibility(View.GONE);
                getDataBinding().bgLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    };

    /**
     * 课程选择完成
     */
    public View.OnClickListener onCoursePickFinishClicked = v -> {
        onBgClicked.onClick(null);
        // 加载行业题库模块
        loadBusinessModule();
        // 保存课程
        AppPreferences.setSelectedCourse(selectedCourse);
        // 刷新界面
        refreshView();
    };

    /**
     * 分类点击
     */
    public View.OnClickListener onCategoryClicked = v -> {
        if (allCourseList.size() == 0) {
            showSingleToast("正在加载课程信息，请稍后...");
            return;
        }
        int businessId = Integer.valueOf(v.getTag().toString());
        switch (businessId) {
            case 1: // 会计
                selectedCourse.businessName = "会计";
                break;
            case 2: // 建工
                selectedCourse.businessName = "建工";
                break;
            case 3: // 外语
                selectedCourse.businessName = "外语";
                break;
            case 4: // 教师教育
                selectedCourse.businessName = "教育";
                break;
            case 5: // 人力资源
                selectedCourse.businessName = "人力资源";
                break;
            case 6: // 学历
                selectedCourse.businessName = "学历考试";
                break;
        }
        selectedCourse.businessId = businessId;
        if (businessList.size() > 5) {
            selectedCourse.businessName = businessList.get(businessId - 1).businessName;
        }
        // 获取选择的行业
        EducationCourse defaultCourse = allCourseList.get(businessId - 1).get(0).get(0);
        // 获取选择的等级列表
        levelList = getLevelList(selectedCourse.businessId);
        // 获取选择的职业等级
        selectedCourse.levelId = AppPreferences.getSelectedCourse() != null && AppPreferences.getSelectedCourse().levelId != 0 ? AppPreferences.getSelectedCourse().levelId : defaultCourse.levelId;
        selectedCourse.levelName = AppPreferences.getSelectedCourse() != null && !TextUtils.isEmpty(AppPreferences.getSelectedCourse().levelName) ? AppPreferences.getSelectedCourse().levelName : defaultCourse.levelName;
        // 刷新职业等级列表
        refreshLevel();
        // 获取默认职业等级的课程列表
        courseList = getCourseList(selectedCourse.businessId, selectedCourse.levelId);
        // 获取默认课程
        selectedCourse.courseId = AppPreferences.getSelectedCourse() != null && AppPreferences.getSelectedCourse().courseId != 0 ? AppPreferences.getSelectedCourse().courseId : defaultCourse.courseId;
        selectedCourse.courseName = AppPreferences.getSelectedCourse() != null && !TextUtils.isEmpty(AppPreferences.getSelectedCourse().courseName) ? AppPreferences.getSelectedCourse().courseName : defaultCourse.courseName;
        // 刷新课程列表
        refreshCourse();
        AppPreferences.setSelectedCourse(selectedCourse);
        // 刷新行业
        refreshBusiness();
        // 加载行业题库模块
        loadBusinessModule();
        // 刷新界面
        refreshView();
        isFirstSelect.set(false);
        StatusBarUtil.setTranslucentStatus(getSupportedActivity());
    };
}
