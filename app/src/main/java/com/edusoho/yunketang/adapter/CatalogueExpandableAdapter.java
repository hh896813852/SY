package com.edusoho.yunketang.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.bean.CourseItem;
import com.edusoho.yunketang.edu.bean.CourseTask;
import com.edusoho.yunketang.utils.DateUtils;

import java.util.List;

/**
 * Created by huhao on 2019/11/13
 */

public class CatalogueExpandableAdapter extends BaseExpandableListAdapter {

    private List<CourseItem> expandableList;
    private Context context;
    private LayoutInflater inflater;
    private Drawable iconExpand;
    private Drawable iconUnExpand;
    private boolean isCourseMember;
    private int tryLookable;

    public CatalogueExpandableAdapter(Context context, List<CourseItem> expandableList, int tryLookable) {
        this(context, expandableList, false, tryLookable);
    }

    public CatalogueExpandableAdapter(Context context, List<CourseItem> expandableList, boolean isCourseMember, int tryLookable) {
        super();
        this.context = context;
        this.expandableList = expandableList;
        this.isCourseMember = isCourseMember;
        this.tryLookable = tryLookable;
        inflater = LayoutInflater.from(context);
        iconExpand = ContextCompat.getDrawable(context, R.drawable.icon_arrow_down);
        iconUnExpand = ContextCompat.getDrawable(context, R.drawable.icon_arrow_up);
    }

    public void setIsCourseMember(boolean isCourseMember) {
        this.isCourseMember = isCourseMember;
    }

    /**
     * 取得分组数
     *
     * @return 组数
     */
    @Override
    public int getGroupCount() {
        return expandableList.size();
    }

    /**
     * 取得指定分组的子元素数
     *
     * @param groupPosition ：要取得子元素个数的分组位置
     * @return:指定分组的子元素个数
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        if (expandableList.get(groupPosition).childList == null) {
            return 0;
        }
        return expandableList.get(groupPosition).childList.size();
    }

    /**
     * 取得与给定分组关联的数据
     *
     * @param groupPosition 分组的位置
     * @return 指定分组的数据
     */
    @Override
    public Object getGroup(int groupPosition) {
        return expandableList.get(groupPosition);
    }

    /**
     * 取得与指定分组、指定子项目关联的数据
     *
     * @param groupPosition :包含子视图的分组的位置
     * @param childPosition :指定的分组中的子视图的位置
     * @return 与子视图关联的数据
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableList.get(groupPosition).childList.get(childPosition);
    }

    /**
     * 取得指定分组的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
     *
     * @param groupPosition 要取得ID的分组位置
     * @return 与分组关联的ID
     */
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * 取得给定分组中给定子视图的ID.该组ID必须在组中是唯一的.必须不同于其他所有ID（分组及子项目的ID）
     *
     * @param groupPosition 包含子视图的分组的位置
     * @param childPosition 要取得ID的指定的分组中的子视图的位置
     * @return 与子视图关联的ID
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * 是否指定分组视图及其子视图的id对应的后台数据改变也会保持该id
     *
     * @return 是否相同的id总是指向同一个对象
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 去的用于显示给定分组的视图，该方法仅返回分组的视图对象。
     *
     * @param groupPosition :决定返回哪个视图的组位置
     * @param isExpanded    ：该分组是展开状态（true）还是收起状态（false）
     * @param convertView   :如果可能，重用旧的视图对象.使用前你应该保证视图对象为非空，并且是否是合适的类型.
     *                      如果该对象不能转换为可以正确显示数据的视图 ，该方法就创建新视图.不保证使用先前由getGroupView(int,
     *                      boolean,View, ViewGroup)创建的视图.
     * @param parent        :该视图最终从属的父视图
     * @return 指定位置相应的组试图
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expand_group_catalogue, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.chapterTitle = convertView.findViewById(R.id.chapterTitle);
            groupViewHolder.arrowImage = convertView.findViewById(R.id.arrowImage);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if (expandableList.get(groupPosition).type.equals("nullGroup")) {
            groupViewHolder.chapterTitle.setText("课程目录");
        } else {
            groupViewHolder.chapterTitle.setText(expandableList.get(groupPosition).title);
        }
        groupViewHolder.arrowImage.setImageDrawable(isExpanded ? iconExpand : iconUnExpand);
        return convertView;
    }

    /**
     * 取得显示给定分组给定子位置的数据用的视图
     *
     * @param groupPosition :包含要取得子视图的分组位置。
     * @param childPosition ：分组中子视图（要返回的视图）的位置。
     * @param isLastChild   ：该视图是否为组中的最后一个视图。
     * @param convertView   ： 如果可能，重用旧的视图对象，使用前应保证视图对象为非空，且是否是适合的类型。
     *                      如果该对象不能转换为正确显示数据的视图，该方法就创建新视图。
     *                      不保证使用先前由getChildView(int,int,boolean,View,ViewGroup)创建的视图。
     * @param parent        ：该视图最终从属的父视图
     * @return:指定位置相应的子视图。
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_expand_child_catalogue, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.playImage = convertView.findViewById(R.id.playImage);
            childViewHolder.taskNo = convertView.findViewById(R.id.taskNo);
            childViewHolder.taskTitle = convertView.findViewById(R.id.taskTitle);
            childViewHolder.taskFrontTip = convertView.findViewById(R.id.taskFrontTip);
            childViewHolder.taskBackTip = convertView.findViewById(R.id.taskBackTip);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        CourseItem courseItem = expandableList.get(groupPosition).childList.get(childPosition);
        childViewHolder.playImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.icon_pause_white));
        childViewHolder.taskNo.setText(String.valueOf(courseItem.number));
        childViewHolder.taskTitle.setText(String.valueOf(courseItem.title));
        if (courseItem.task == null) {
            convertView.setVisibility(View.GONE);
        } else {
            convertView.setVisibility(View.VISIBLE);
            if ("live".equals(courseItem.task.type)) {
                childViewHolder.taskFrontTip.setVisibility(View.GONE);
                childViewHolder.playImage.setVisibility(View.VISIBLE);
                // 设置直播状态
                setLiveStatus(childViewHolder, courseItem);
            } else {
                childViewHolder.taskFrontTip.setVisibility(View.VISIBLE);
                childViewHolder.playImage.setVisibility(View.GONE);
                childViewHolder.taskBackTip.setText(DateUtils.second2Min(courseItem.task.length));
                if (isShowTryLookable(courseItem.task)) {
                    childViewHolder.taskFrontTip.setText("试看");
                    childViewHolder.taskFrontTip.setTextColor(ContextCompat.getColor(context, R.color.text_yellow));
                } else if (courseItem.task.isFree == 1 && !isCourseMember) {
                    childViewHolder.taskFrontTip.setText("免费");
                    childViewHolder.taskFrontTip.setTextColor(ContextCompat.getColor(context, R.color.theme_color));
                } else {
                    childViewHolder.taskFrontTip.setVisibility(View.GONE);
                }
            }
        }
        return convertView;
    }

    /**
     * 指定位置的子视图是否可选择
     *
     * @param groupPosition 包含要取得子视图的分组位置
     * @param childPosition 分组中子视图的位置
     * @return 是否子视图可选择
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView chapterTitle;
        ImageView arrowImage;
    }

    static class ChildViewHolder {
        ImageView playImage;
        TextView taskNo;
        TextView taskTitle;
        TextView taskFrontTip;
        TextView taskBackTip;
    }

    /**
     * 设置直播状态
     */
    private void setLiveStatus(ChildViewHolder viewHolder, CourseItem item) {
        long currentTime = System.currentTimeMillis();
        long startTime = item.task.startTime * 1000;
        long endTime = item.task.endTime * 1000;
        if (currentTime <= startTime) { // 未开始
            viewHolder.taskBackTip.setTextColor(ContextCompat.getColor(context, R.color.text_dark_gray));
            viewHolder.taskBackTip.setText(DateUtils.formatDate(startTime, "MM月dd号 HH:mm"));
        } else {
            if (currentTime > endTime) { // 已结束
                if ("ungenerated".equals(item.task.activity.replayStatus)) {
                    viewHolder.taskBackTip.setText("已结束");
                    viewHolder.taskBackTip.setTextColor(ContextCompat.getColor(context, R.color.text_gray));
                } else { // 回放
                    viewHolder.taskBackTip.setText("回放");
                    viewHolder.taskBackTip.setTextColor(context.getResources().getColor(R.color.text_yellow));
                }
            } else { // 直播中
                viewHolder.taskBackTip.setText("直播中");
                viewHolder.taskBackTip.setTextColor(context.getResources().getColor(R.color.theme_color));
            }
        }
    }

    private boolean isShowTryLookable(CourseTask task) {
        return !isCourseMember && task.type.equals("video") && task.isFree == 0 && tryLookable == 1
                && task.activity != null && "cloud".equals(task.activity.mediaStorage);
    }
}
