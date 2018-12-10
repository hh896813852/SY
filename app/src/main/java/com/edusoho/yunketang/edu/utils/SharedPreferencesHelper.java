package com.edusoho.yunketang.edu.utils;


public class SharedPreferencesHelper {

    public static class SchoolSetting {
        public static final String XML_NAME            = "School_Setting";
        public static final String COURSE_SETTING_KEY  = "course_setting";
        public static final String USER_SETTING_KEY    = "user_setting";
        public static final String CLOUD_VIDEO_SETTING = "cloud_video_setting";
        public static final String PAYMENT_SETTING     = "payment_setting";
        public static final String VIP_SETTING         = "vip_setting";
    }

    public static class CourseSetting {
        public static final String XML_NAME                     = "course_setting";
        public static final String SHOW_STUDENT_NUM_ENABLED_KEY = "show_student_num_enabled_key";
        public static final String CHAPTER_NAME_KEY             = "chapter_name_key";
        public static final String PART_NAME_KEY                = "part_name_key";
    }

    public static class TaskLearningRecord {
        public static final String XML = "task_learning_record";
    }
}
