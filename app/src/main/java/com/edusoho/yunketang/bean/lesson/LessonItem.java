package com.edusoho.yunketang.bean.lesson;

import com.edusoho.yunketang.bean.M3U8DbModel;
import com.edusoho.yunketang.edu.bean.CourseItem;

import java.io.Serializable;

/**
 * Created by howzhi on 14-5-24.
 */
public class LessonItem<T> implements Serializable {
    public static final int FREE = 1;

    public int    id;
    public int    courseId;
    public int    number;
    public int    seq;
    public int    free;
    public String status;
    public String title;
    public String summary;
    public String tag;
    public String type;
    public T      content;
    public int    mediaId;
    public String mediaSource;
    public String mediaUri;
    public String headUrl;
    public String length;
    public int    userId;
    public String remainTime;
    public String createdTime;
    public String itemType;
    public String startTime;
    public String endTime;
    public String replayStatus;
    public String mediaStorage;
    public String mediaConvertStatus;
    public String audioUri;
    public String videoDefinitionName;

    public M3U8DbModel m3u8Model;
    public int         groupId;
    public UploadFile  uploadFile;
    public boolean     isSelected;

    public enum ItemType {
        LESSON, CHAPTER, UNIT, EMPTY;

        public static ItemType cover(String name) {
            ItemType type = EMPTY;
            try {
                type = valueOf(name.toUpperCase());
            } catch (Exception e) {
                return EMPTY;
            }
            return type;
        }
    }

    public enum MediaSourceType {
        YOUKU, SELF, TUDOU, EMPTY, QQVIDEO, FALLBACK, NETEASEOPENCOURSE;

        public static MediaSourceType cover(String name) {
            MediaSourceType type = EMPTY;
            try {
                type = valueOf(name.toUpperCase());
            } catch (Exception e) {
                return EMPTY;
            }
            return type;
        }
    }

    public LessonItem parse(CourseItem courseItem, int courseId) {
        this.courseId = courseId;
        this.type = courseItem.type;
        this.title = courseItem.title;
        if (type.toUpperCase().equals(ItemType.CHAPTER.toString()) || type.toUpperCase().equals(ItemType.UNIT.toString())) {
            this.itemType = "chapter";
        }
        if ("task".equals(courseItem.type) && courseItem.task != null) {
            this.id = courseItem.task.id;
            this.status = courseItem.task.status;
            this.mediaSource = courseItem.task.mediaSource;
            this.itemType = "lesson";
            this.type = courseItem.task.type;
            this.replayStatus = courseItem.task.activity != null ? courseItem.task.activity.replayStatus : "";
            if (courseItem.task.activity != null) {
                this.mediaStorage = courseItem.task.activity.mediaStorage;
            }
        }
        return this;
    }
}
