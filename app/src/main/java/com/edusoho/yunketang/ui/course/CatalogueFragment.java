package com.edusoho.yunketang.ui.course;

import android.content.Intent;

import com.edusoho.yunketang.bean.Course;
import com.edusoho.yunketang.helper.AppPreferences;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.R;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.adapter.CatalogueExpandableAdapter;
import com.edusoho.yunketang.base.BaseFragment;
import com.edusoho.yunketang.base.annotation.Layout;
import com.edusoho.yunketang.databinding.FragmentCatalogueBinding;
import com.edusoho.yunketang.edu.bean.CourseItem;
import com.edusoho.yunketang.edu.bean.CourseTask;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.AppUtil;
import com.edusoho.yunketang.utils.JsonUtil;
import com.edusoho.yunketang.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2018/11/9 0009.
 */
@Layout(value = R.layout.fragment_catalogue)
public class CatalogueFragment extends BaseFragment<FragmentCatalogueBinding> {
    private List<CourseItem> courseItems;
    public List<CourseItem> expandableList = new ArrayList<>();
    private CatalogueExpandableAdapter expandableAdapter;
    private boolean isCourseMember;
    private int courseType;
    private int courseProjectId;

    @Override
    public void onResume() {
        super.onResume();
        if (courseType != 0 && courseProjectId != 0) {
            loadCatalogueData(courseType, courseProjectId);
        }
    }

    /**
     * 初始化ExpandableListView
     */
    private void initExpandView() {
        if (getActivity() == null) {
            return;
        }
        expandableAdapter = new CatalogueExpandableAdapter(getSupportedActivity(), expandableList, ((CourseDetailsActivity) getActivity()).courseProject.tryLookable);
        getDataBinding().expandableView.setAdapter(expandableAdapter);
        // 去掉分割线
        getDataBinding().expandableView.setDivider(null);
        // 子项点击
        getDataBinding().expandableView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            CourseTask currentTask = expandableList.get(groupPosition).childList.get(childPosition).task;
            // 检查是否可学习
            if (checkLearnAble(currentTask)) {
                Intent intent = new Intent(getSupportedActivity(), CoursePlayerActivity.class);
                intent.putExtra(CoursePlayerActivity.COURSE_PROJECT, ((CourseDetailsActivity) getActivity()).courseProject);
                intent.putExtra(CoursePlayerActivity.COURSE_COVER, ((CourseDetailsActivity) getActivity()).coverUrl.get());
                intent.putExtra(CoursePlayerActivity.COURSE_TYPE, courseType);
                intent.putExtra(CoursePlayerActivity.COURSE_ID, ((CourseDetailsActivity) getActivity()).courseId);
                intent.putExtra(CoursePlayerActivity.COURSE_TASK, currentTask);
                intent.putExtra(CoursePlayerActivity.COURSE_CATALOGUE, JsonUtil.toJson(expandableList));
                startActivity(intent);
            }
            return false;
        });
    }

    /**
     * 加载课程目录
     */
    public void loadCatalogueData(int courseType, int courseProjectId) {
        if (getActivity() == null) {
            return;
        }
        if (expandableAdapter == null) {
            initExpandView();
        }
        this.courseType = courseType;
        this.courseProjectId = courseProjectId;
        SYDataTransport.create(String.format(courseType == 1 ? SYConstants.ONLINE_COURSE_CATALOGUE : SYConstants.ACCOUNTANT_COURSE_CATALOGUE, courseProjectId))
                .isGET()
                .execute(new SYDataListener<String>() {

                    @Override
                    public void onSuccess(String data) {
                        expandableList.clear();
                        try {
                            // 将数据中富文本内容转成可被解析的json数据
                            String json = StringUtils.jsonStringConvert(StringUtils.replaceBlank(data));
                            courseItems = JsonUtil.fromJson(json, new TypeToken<List<CourseItem>>() {
                            });
                            if (courseItems != null && courseItems.size() > 0) {
                                splitData(courseItems);
                            }
                        } catch (Exception e) {

                        }
                    }
                });
    }

    /**
     * 设置是否加入了学习
     */
    public void setIsCourseMember(boolean isCourseMember) {
        this.isCourseMember = isCourseMember;
        expandableAdapter.setIsCourseMember(isCourseMember);
        expandableAdapter.notifyDataSetChanged();
    }

    /**
     * 分离出头部和身体
     */
    private void splitData(List<CourseItem> list) {
        int groupId = 0;
        for (CourseItem courseItem : list) {
            // 添加组
            if ("chapter".equals(courseItem.type)) {
                groupId++;
                expandableList.add(courseItem);
            } else { // 添加成员
                if (groupId == 0) { // 没有组，创建一个空的组
                    groupId++;
                    CourseItem item = new CourseItem();
                    item.type = "nullGroup";
                    if (item.childList == null) {
                        item.childList = new ArrayList<>();
                    }
                    item.childList.add(courseItem);
                    expandableList.add(item);
                } else {
                    if (expandableList.get(groupId - 1).childList == null) {
                        expandableList.get(groupId - 1).childList = new ArrayList<>();
                    }
                    expandableList.get(groupId - 1).childList.add(courseItem);
                }
            }
        }
        expandableAdapter.notifyDataSetChanged();
        // 默认全部展开
        for (int i = 0; i < expandableList.size(); i++) {
            getDataBinding().expandableView.expandGroup(i);
        }
    }

    /**
     * 检查是否可学习
     */
    private boolean checkLearnAble(CourseTask currentTask) {
        if (currentTask.isFree == 1) { // 免费
            return true;
        } else {
            if (isCourseMember) { // 已加入（已购买）
                return true;
            } else {
                // 可试看、video、cloud
                if (((CourseDetailsActivity) getActivity()).courseProject.tryLookable == 1 && currentTask.type.equals("video") && "cloud".equals(currentTask.activity.mediaStorage)) {
                    return true;
                } else if (AppUtil.isForegroundActivity(getSupportedActivity())) {
                    showToast("请先加入学习");
                    return false;
                }
            }
        }
        return false;
    }
}
