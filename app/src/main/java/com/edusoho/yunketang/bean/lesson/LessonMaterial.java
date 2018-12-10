package com.edusoho.yunketang.bean.lesson;

import java.io.Serializable;

/**
 * Created by howzhi on 14-9-17.
 */
public class LessonMaterial implements Serializable{
    public int id;
    public int courseId;
    public int lessonId;
    public int userId;
    public int fileId;
    public int fileSize;
    public String title;
    public String description;
    public String fileMime;
    public String fileUri;
    public String createdTime;
    public String link;
}
