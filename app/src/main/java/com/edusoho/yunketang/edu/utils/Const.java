package com.edusoho.yunketang.edu.utils;


public class Const {
    public static final long ONE_DAY_MILLISECOND = 24 * 60 * 60 * 1000;

    //popular热门 recommended推荐 latest最新
    public static final String[] SORT = {
            "latest", "popular", "recommended"
    };

    public static final String[] COURSE_INFO_TITLE = {
            "课程", "教师", "评价"
    };

    public static final String[] MY_COURSE_TITLE = {
            "在学课程", "收藏课程", "已学课程"
    };

    /**
     * 学堂，在学课程
     */
    public static final String[] SCHOOL_ROOM_COURSE = {
            "正在学习", "已学完"
    };

    public static final String[] MESSAGE_TAB_TITLE = {
            "通知", "私信"
    };

    /**
     * 考试题目类别
     */
    public static final String[] TESTPAPER_QUESTION_TYPE = {
            "多选题", "判断题", "问答题", "填空题", "材料题", "单选题", "不定项题"
    };

    /**
     * 考试题目 fragment
     */
    public static final String[] TESTPAPER_QUESTIONS = {
            "ChoiceFragment",
            "DetermineFragment",
            "EssayFragment",
            "FillFragment",
            "MaterialFragment",
            "SingleChoiceFragment",
            "UncertainChoiceFragment"
    };

    public static final String[] MY_COURSE_FRAGMENT = {
            "LearnCourseFragment",
            "FavoriteCourseFragment",
            "LearnedCourseFragment"
    };

    public static final String[] SCHOOLROOM_COURSE_FRAGMENT = {
            "LearnCourseFragment",
            "LearnedCourseFragmentHorizontal",
    };

    public static final String[] MESSAGE_FRAGMENT_LIST = {
            "MessageFragment",
            "LetterFragment"
    };

    public static final String[] COURSE_INFO_FRAGMENT = {
            "CourseInfoFragment",
            "TeacherInfoFragment",
            "ReviewInfoFragment"
    };

    //默认分页数量
    public static final int LIMIT               = 10;
    public static final int NEWS_LIMIT          = 15;
    public static final int STUDY_PROCESS_LIMIT = 32768;

    public static final String EQUIP_TYPE      = "Android";
    public static final String BIND_USER_ID    = "bind_user_id";
    public static final String SHOW_SCH_SPLASH = "show_sch_splash";

    public static final int SHOW_KEYBOARD = 1;
    public static final int HIDE_KEYBOARD = 2;

    //Plugin事件
    public static final int OPEN_COURSE_CHAT = 3;

    public static final String AUDIO_EXTENSION = ".mp3";
    public static final String MEDIA_IMAGE     = "图片";
    public static final String MEDIA_AUDIO     = "语音";
    public static final String GET_PUSH_DATA   = "get_push_data";
    public static final String FROM_ID         = "from_id";
    public static final String NEWS_TYPE       = "news_type";
    public static final String NEW_ITEM_INFO   = "new_item_info";

    //public static final int ADD_DISCUSS_MSG = 1;
    public static final int ADD_MSG                = 2;
    public static final int ADD_ARTICLE_CREATE_MAG = 3;
    public static final int ADD_THREAD_POST        = 4;
    public static final int ADD_COURSE_MSG         = 5;
    public static final int ADD_DISCOUNT_PASS      = 6;
    public static final int ADD_COURSE_DISCUSS_MSG = 7;
    public static final int ADD_CLASSROOM_MSG      = 8;
    public static final int ADD_CHAT_MSGS          = 9;
    public static final int ADD_BULLETIN_MSG       = 10;
    public static final int CLEAN_RECORD           = 11;
    public static final int REFRESH_LIST           = 12;
    public static final int QUESTION_CREATED       = 13;
    public static final int QUESTION_ANSWERD       = 14;

    public static final int UPDATE_CHAT_MSG = 0x1000;

    public static final int SWITCH_TAB = 9;

    public static final int    NEW_FANS                    = 0x0A;
    public static final String ADD_CHAT_MSG_DESTINATION    = "add_chat_msg_type";
    public static final String ADD_DISCUSS_MSG_DESTINATION = "add_discuss_msg_destination";
    public static final String ADD_THREAD_POST_DESTINATION = "add_thread_post_destination";

    //public message type
    public static final String TESTPAPER_REFRESH_DATA    = "testpaper_refresh_data";
    public static final String LOGIN_SUCCESS             = "login_success";
    public static final String REFRESH_MY_FRAGMENT       = "refresh_my_fragment";
    public static final String USER_UPDATE               = "user_update";
    public static final String CLEAR_APP_CACHE           = "clear_app_cache";
    public static final String THIRD_PARTY_LOGIN_SUCCESS = "third_party_login_success";
    public static final String LOGOUT_SUCCESS            = "logout_success";
    public static final String REFRESH_REVIEWS           = "refresh_review";
    public static final String REFRESH_FRIEND_LIST       = "refresh_friend_list";
    public static final String CLEAR_HISTORY             = "clear_history";
    public static final String DELETE_FRIEND             = "delete_friend";
    public static final String DIALOG_DISMISS            = "dialog_dismiss";
    public static final String SCROLL_STATE_SAVE         = "scroll_state_save";

    //课程及班级界面消息类型
    public static final String WEB_BACK_REFRESH = "webview_back_update";

    public static final String FULL_SCREEN            = "course_full_screen";
    public static final String SCREEN_LOCK            = "screen_lock";
    public static final String COURSE_HASTRIAL        = "course_hastrial";//是否有试学
    public static final String COURSE_HASTRIAL_RESULT = "course_hastrial_result";
    public static final String COURSE_REFRESH         = "course_refresh";//刷新课程Activity
    public static final String LESSON_STATUS_REFRESH  = "lesson_status_refresh";//刷新课时状态
    public static final String SOURCE                 = "source";
    public static final String CLASSROOM_ID           = "Classroom_id";//班级id


    public static final String COURSE_START         = "course_start";//开始课程
    public static final String COURSE_CHANGE        = "course_change";//课程切换
    public static final String COURSE_CHANGE_OBJECT = "course_change_object";//课程切换

    public static final String COURSE_CHANGE_TITLE         = "course_change_title";//课程名字
    public static final String COURSE_CHANGE_STATE         = "course_change_state";//课程状态
    public static final String COURSE_CHANGE_STATE_NONE    = "0";//课程状态,未开始
    public static final String COURSE_CHANGE_STATE_STARTED = "1";//课程状态,已开始
    public static final String COURSE_CHANGE_STATE_FINISH  = "2";//课程状态,学完了

    public static final String COURSE_SHOW_BAR = "course_show_bar";//导航栏
    public static final String COURSE_HIDE_BAR = "course_hide_bar";//导航栏
    public static final String COURSE_PAUSE    = "course_pause";//暂停课程后的UI处理
    public static final String PAY_SUCCESS     = "pay_success";//暂停课程后的UI处理

    public static final int     OK           = 200;
    public static final int     ERROR_200    = 200;
    public static final boolean memCacheNo   = false;
    public static final boolean fileCacheYes = true;
    public static final int     TIMEOUT      = 10000;

    public static final String RESULT_OK        = "ok";
    public static final String SHOW_STUDENT_NUM = "opened";
    public static final String RESULT_SUCCESS   = "success";
    public static final String RESULT_ERROR     = "error";
    public static final String CLIENT_CLOSE     = "client_closed";

    public static final String DB_NAME = "edusoho";

    public static final String MOBILE_REGIST     = "http://open.edusoho.com/mobstat/installed";
    public static final String REGIST            = "User/regist";
    public static final String REGIST_DEVICE     = "School/registDevice";
    public static final String DOWNLOAD_URL      = "School/getDownloadUrl";
    public static final String APP_UPDATE        = "http://www.edusoho.com/version/%s-android";
    public static final String COURSES           = "Course/getCourses";
    public static final String COURSE            = "Course/getCourse";
    public static final String LIVE_COURSES      = "Course/getAllLiveCourses";
    public static final String COURSE_MEMBER     = "Course/getCourseMember";
    public static final String FLASH_APK         = "School/getFlashApk";
    public static final String MY_TESTPAPER      = "Testpaper/myTestpaper";
    public static final String COURSELESSON      = "Lesson/getLesson";
    public static final String DOWNLOAD_MATERIAL = "%sLesson/downMaterial?courseId=%d&materialId=%d&token=%s";
    public static final String UPLOAD_IMAGE      = "Testpaper/uploadQuestionImage";

    /**
     * 课时类型
     */
    public static final String COURSE_CATALOG_PPT       = "ppt";
    public static final String COURSE_CATALOG_LIVE      = "live";
    public static final String COURSE_CATALOG_TEXT      = "text";
    public static final String COURSE_CATALOG_AUDIO     = "audio";
    public static final String COURSE_CATALOG_VIDEO     = "video";
    public static final String COUSRE_CATALOG_FLASH     = "flash";
    public static final String COURSE_CATALOG_TESTPAPER = "testPaper";
    public static final String COURSE_CATALOG_DOCUMENT  = "document";
    public static final String COURSE_CATALOG_EMPTH     = "empty";

    /**
     * mobile/{code}/version  *
     */
    public static final String MOBILE_APP_VERSION  = "mobile/%s/version?code=%s";
    public static final String MOBILE_APP_RESOURCE = "mobile/%s/resources";
    public static final String MOBILE_APP_URL      = "%smobile/%s";

    /**
     * course status
     */
    public static final String COURSE_CLOSE        = "closed";
    public static final String NETEASE_OPEN_COURSE = "NeteaseOpenCourse";
    public static final String QQ_OPEN_COURSE      = "qqvideo";
    public static final String COURSE_PUBLISHED    = "published";
    public static final String COURSE_SERIALIZE    = "serialize";

    /**
     * Thread
     */
    public static final String GET_THREAD      = "Course/getThread";
    public static final String GET_THREAD_POST = "Course/getThreadPost";
//    public static final String POST_THREAD = "Course/postThread";
//    public static final String CREATE_THREAD = "/api/thread/create";

    /**
     * 获取用户所学课程下的所有问题或者讨论
     */
    public static final String THREADS_BY_USER_COURSE_ID = "Course/getThreadsByUserCourseIds";

    public static final String TESTPAPER_RESULT    = "Testpaper/getTestpaperResult";
    public static final String FAVORITE_QUESTION   = "Testpaper/favoriteQuestion";
    public static final String FINISH_TESTPAPER    = "Testpaper/finishTestpaper";
    public static final String NOTIFICATION        = "User/getUserNotification";
    public static final String LASTER_LEARN_COURSE = "User/getUserLastlearning";
    public static final String LASTER_LEARN_LESSON = "Course/getLearnStatus";

    public static final String MESSAGE_LETTER_SUMMARY = "User/getUserMessages";
    public static final String MESSAGE_LIST           = "User/getMessageList";
    public static final String SEND_LETTER            = "User/sendMessage";
    public static final String GET_CONVERSATION       = "User/getConversationIdByFromIdAndToId";

    /**
     * 获取所有回答
     * 传递参数：courseId，threadId
     */
    public static final String NORMAL_REPLY = "Course/getThreadPost";

    /**
     * 获取教师回复（暂时不用，isElite标示能判断教师回复）
     * 传递参数：courseId，threadId
     */
    public static final String TEACHER_REPLY = "Course/getThreadTeacherPost";

    /**
     * 回复提交
     */
    public static final String REPLY_SUBMIT = "Course/postThread";

    /**
     * 编辑回复提交
     */
    public static final String REPLY_EDIT_SUBMIT = "Course/updatePost";

    /**
     * 分享url
     */
    public static final String SHARD_COURSE_URL = "School/getShradCourseUrl";

    /**
     * 获取一个问题的详细信息（问题标题，内容，发问者，发问时间等）
     * 传递参数：courseId，threadId
     */
    public static final String QUESTION_INFO = "Course/getThread";

    public static final String EDIT_QUESTION_INFO = "Course/updateThread";

    public static final String CHECKTOKEN             = "User/loginWithToken";
    public static final String COMPLETE               = "/api/sms_codes";
    public static final String LOGIN                  = "User/login";
    public static final String SMS_SEND               = "User/smsSend";
    public static final String SEND_SMS               = "/api/sms_codes";
    public static final String ADDCOMMENT             = "Course/commitCourse";
    public static final String ABOUT                  = "School/getSchoolInfo";
    public static final String LEARNING               = "Course/getLearningCourse";
    public static final String LEARNING_NEW           = "/api/me/courses";
    public static final String LEARNING_WITHOUT_TOKEN = "Course/getLearningCourseWithoutToken";
    public static final String LEARNED                = "Course/getLearnedCourse";
    public static final String FAVORITES              = "Course/getFavoriteCoruse";
    public static final String PAYCOURSE              = "Order/payCourse";
    public static final String FAVORITE               = "Course/favoriteCourse";
    public static final String UNFAVORITE             = "Course/unFavoriteCourse";
    public static final String CREATE_ORDER           = "Order/createOrder";
    public static final String VIP_ORDER_COURSE       = "Course/vipLearn?courseId=%s";
    public static final String COURSE_UNLEARNCOURSE   = "Course/unLearnCourse?courseId=%s";
    public static final String VIP_ORDER_CLASSROOM    = "ClassRoom/learnByVip?classRoomId=%s";
    public static final String VERIFYSCHOOL           = "/School/getSchoolSite";
    public static final String VERIFYVERSION          = "/systeminfo?version=2";
    public static final String LOGOUT                 = "User/logout";
    public static final String FOLLOWING              = "User/getFollowings";
    public static final String FOLLOWER               = "User/getFollowers";
    public static final String FOLLOW                 = "User/follow";
    public static final String UNFOLLOW               = "User/unfollow";
    public static final String IS_FOLLOWED            = "User/searchUserIsFollowed";

    public static final String SCHOOL_BANNER         = "School/getSchoolBanner";
    public static final String SCHOOL_Announcement   = "School/getSchoolAnnouncement";
    public static final String RECOMMEND_COURSES     = "School/getRecommendCourses";
    public static final String LASTEST_COURSES       = "School/getLatestCourses";
    public static final String WEEK_COURSES          = "School/getWeekRecommendCourses";
    public static final String CATEGORYS             = "Category/getAllCategories";
    public static final String GET_TAGS              = "Category/getTags";
    public static final String USERTERMS             = "School/getUserterms";
    public static final String USERINFO              = "User/getUserInfo";
    public static final String USERINFO_NEW          = "/mapi_v2/User/getUserInfo?userId=%s";
    public static final String TEACHER_INFO          = "/mapi_v2/ClassRoom/getTeachers?classRoomId=%s";
    public static final String REVIEWS               = "Course/getReviews";
    public static final String COURSE_GETCOURSE      = "Course/getCourse?courseId=%s";
    public static final String COURSE_GETREVIEWS     = "Course/getReviews?courseId=%s&limit=%s&start=%s";
    public static final String COURSE_GETREVIEWS_NEW = "/api/courses/%s/reviews?limit=%s&start=%s";
    public static final String COURSE_GETMEMBER      = "/api/courses/%s/members";
    public static final String COURSE_GETCLASSROOM   = "ClassRoom/getClassRoom?id=%s";
    public static final String CLASSROOM_GETREVIEWS  = "ClassRoom/getReviews?classRoomId=%s&limit=%s&start=%s";
    public static final String CLASSROOM_GETMEMBER   = "/api/classrooms/%s/members?limits=1";
    public static final String CLASSROOM_MY          = "ClassRoom/myClassRooms";


    public static final String LESSONS                 = "Lesson/getCourseLessons";
    public static final String DOWN_LESSONS            = "Lesson/getCourseDownLessons";
    public static final String SEARCH_COURSE           = "Course/searchCourse";
    public static final String FAV_NOR_COURSE          = "Course/getFavoriteNormalCourse";
    public static final String MY_LEARNING             = "/api/my/learning";
    public static final String FAV_LIVE_COURSE         = "Course/getFavoriteLiveCourse";
    public static final String COURSE_PROGRESS         = "/api/courses_learn_progress";
    public static final String TEACHER_COURSES         = "Course/getTeacherCourses";
    public static final String COURSE_NOTICE           = "Course/getCourseNotice";
    public static final String UN_LEARN_COURSE         = "Course/unLearnCourse";
    public static final String LESSON_RESOURCE         = "Lesson/getLessonMaterial";
    public static final String LEARN_LESSON            = "Lesson/learnLesson";
    public static final String UNLEARN_LESSON          = "Lesson/unLearnLesson";
    public static final String LEARN_STATUS            = "Lesson/getLearnStatus";
    public static final String VIP_LEARN_COURSE        = "Course/vipLearn";
    public static final String COURSE_COMMITCOURSE     = "Course/commitCourse";
    public static final String COURSE_COMMITCOURSE_NEW = "/api/courses/%s/reviews";
    public static final String USER_DATA_NUMBER        = "User/getUserNum";
    public static final String CLASSROOM_UNLEARN       = "ClassRoom/unlearn";

    public static final int    PUSH_VERSION           = 2;
    public static final String PUSH_HOST              = "http://tui.edusoho.net/v2";
    public static final String ANONYMOUS_BIND         = "/auth/anonymousBind";
    public static final String BIND                   = "/auth/bind";
    public static final String UNBIND                 = "/auth/unBind";
    public static final String SEND                   = "/message/send";
    public static final String GET_UPLOAD_INFO        = "/file/%s/upload/url?length=%d&filename=%s";
    public static final String SAVE_UPLOAD_INFO       = "/file/%d/upload/succeed";
    public static final String GET_LATEST_OFFLINE_MSG = "/message/%d/newest/list";
    public static final String LESSON_IDS             = "lesson_ids";
    public static final String STUDENT_LIST           = "/api/courses/%s/members";
    /**
     * 获取课程状态（是否包含资料，学习状态）
     */
    public static final String LESSON_STATUS          = "Lesson/getLessonStatus";
    /**
     * 获取课程目录
     */
    public static final String LESSON_CATALOG         = "/mapi_v2/Lesson/getCourseLessons";
    /**
     * 获取考试课程详情
     */
    public static final String TESTPAPER_INFO         = "Lesson/getTestpaperInfo";

    /**
     * 获取考试课程详情
     */
    public static final String TESTPAPER_FULL_INFO       = "Testpaper/doTestpaper";
    public static final String RE_DO_TESTPAPER_FULL_INFO = "Testpaper/reDoTestpaper";
    public static final String SHOW_TESTPAPER            = "Testpaper/showTestpaper";

    /**
     * 检查优惠码
     */
    public static final String COURSE_CODE = "Course/coupon";

    //意见反馈
    public static final String SUGGESTION = "School/sendSuggestion";
    public static final String SCHOOL_APP = "School/getSchoolApps";

    public static final Object[] QUICK_COMMENTS = {
            "不错", 2.0f,
            "感觉很好", 3.0f,
            "课程内容很好", 4.0f,
            "再接再厉，很喜欢这个课程的内容", 5.0f
    };


    public static final int LEARNING_REQUEST      = 0x1001;
    public static final int LEARNING_RESULT       = 0x1002;
    public static final int NORMAL_RESULT_REFRESH = 0x1003;

    public static final int FAVORITE_REQUEST     = 0x1004;
    public static final int COURSEINFO_REQUEST   = 0x1005;
    public static final int COURSELESSON_REQUEST = 0x1001;

    public static final int CACHE_CODE   = 0x1111;
    public static final int CACHE_PROT   = 8800;
    public static final int WEB_RES_PROT = 8801;

    //服务器api版本
    public static final int NORMAL_VERSIO = 0;
    public static final int HEIGHT_VERSIO = 1;
    public static final int LOW_VERSIO    = -1;

    public static final String DEFAULT_SCHOOL = "defaultSchool";
    public static final String PUBLISHED      = "published";

    public static final String QUESTION_URL               = "question_url";
    public static final String COURSE_ID                  = "courseId";
    public static final String QUESTION_USER_ID           = "question_user_id";
    public static final String TESTPAPER_DO_TYPE          = "do_type";
    public static final String FREE                       = "free";
    public static final String LESSON_ID                  = "lessonId";
    public static final String LESSON_NAME                = "lesson_name";
    public static final String VIDEO_TYPE                 = "video_type";
    public static final String CLOUD_VIDEO_CONVERT_STATUS = "cloud_video_convert_status";
    public static final String QUESTION_TYPE              = "question_type";
    public static final String STREAM_URL                 = "mediaUrl";
    public static final String MEDIA_URL                  = "streamUrls";
    public static final String HEAD_URL                   = "headUrl";
    public static final String MEDIA_ID                   = "mediaId";
    public static final String STATUS                     = "status";
    public static final String MEDIA_SOURCE               = "mediaSource";
    public static final String LESSON_TYPE                = "type";
    public static final String ACTIONBAR_TITLE            = "title";
    public static final String ENTITY                     = "ENTITY";
    public static final String IS_STUDENT                 = "is_student";
    public static final String LIST_JSON                  = "list_json";
    public static final String IS_LEARN                   = "is_learn";
    public static final String mTestpaperResultId         = "testpaperResultId";

    public static final String QUESTION_TITLE       = "question_title";
    public static final String QUESTION_EDIT_RESULT = "question_edit_result";
    public static final String THREAD_ID            = "thread_id";
    public static final String POST_ID              = "post_id";
    public static final String QUESTION_CONTENT     = "question_content";
    public static final String NORMAL_CONTENT       = "content";
    public static final String RICH_ITEM_AGRS       = "rich_item_args";

    public static final String REQUEST_CODE = "reply_type";

    public static final String SCHOOL_ROOM = "User/getSchoolRoom";

    /**
     * 普通回复
     */
    public static final int REPLY = 0x01;

    /**
     * 问题编辑
     */
    public static final int EDIT_QUESTION = 0x02;

    /**
     * 回复编辑
     */
    public static final int EDIT_REPLY = 0x03;

    public static final String COURSE_NOTICES = "Course/getCourseNotices";

    public static final String CACHE_COURSE_TYPE               = "course";
    public static final String CACHE_CLASSROOM_COURSE_IDS_TYPE = "classroom_course_ids";
    public static final String CACHE_USER_TYPE                 = "data";
    public static final String CACHE_LESSON_TYPE               = "lesson";

    public static final String CACHE_KEY_TYPE = "m3u8_key";

    public static final int NET_WIFI    = 0;
    public static final int NET_3G_WIFI = 1;
    public static final int NET_NONE    = 2;

    /**
     * 获取单个回答的内容
     */
    public static final String ONE_REPLY = "Course/getOneThreadPost";

    /**
     * 更新问答 览 数
     */
    public static final String HitThread = "Course/hitThread";

    /**
     * 在学直播课程
     */
    public static final String LIVING_COURSE = "Course/getLiveCourses";
    public static final String LIVE_COURSE   = "Course/getLiveCourse";

    /**
     * html5 url
     */

    public static final String WEB_URL                 = "web_url";
    public static final String MY_LEARN                = "main#/mylearn";
    public static final String MY_INFO                 = "main#/myinfo";
    public static final String MY_FAVORITE             = "main#/myfavorite";
    public static final String USER_PROFILE            = "main#/userinfo/%d";
    public static final String MOBILE_WEB_COURSE       = "main#/course/%d";
    public static final String MOBILE_WEB_CLASSROOMS   = "main#/classroomlist/?categoryId=%d&orderType=%s";
    public static final String MOBILE_WEB_COURSES      = "main#/courselist/normal/?categoryId=%d&orderType=%s";
    public static final String MOBILE_WEB_LIVE_COURSES = "main#/courselist/live/?categoryId=%d&orderType=%s";
    public static final String MOBILE_SEARCH           = "main#/search";
    public static final String ANNOUNCEMENT            = "main#/coursenotice/course/%d";
    public static final String ARTICLE_CONTENT         = "%smobile/main#/article/%d";
    public static final String CLASSROOM_ANNOUNCEMENT  = "main#/coursenotice/classroom/%d";
    public static final String COURSE_ANNOUNCEMENT     = "main#/coursenotice/course/%d";
    public static final String CLASSROOM_COURSES       = "main#/classroom/%d";
    public static final String CLASSROOM_MEMBER_LIST   = "main#/studentlist/classroom/%d";
    public static final String COURSE_MEMBER_LIST      = "main#/studentlist/course/%d";
    public static final String TEACHER_MANAGERMENT     = "main#/todolist/%d";
    public static final String HTML5_LESSON            = "main#/lesson/%d/%d";
    public static final String HTML5_POINT_INFO        = "/h5/reward_point/rule";


    public static final String HAVE_ADD_TRUE  = "friend";
    public static final String HAVE_ADD_FALSE = "none";
    public static final String HAVE_ADD_WAIT  = "following";
    public static final String HAVE_ADD_ME    = "follower";
    public static final String NO_USER        = "no-user";

    public static final String CLEAR_WEBVIEW_CACHE = "clear_webview_cache";

    /**
     * api
     */

    public static final String SMS_CODES                 = "/api/sms_codes";
    public static final String CHANGE_PASSWORD           = "/api/users/password";
    public static final String EMAILS                    = "/api/emails";
    public static final String GET_API_TOKEN             = "/api/mobileschools/token";
    public static final String USERS                     = "/api/users";
    public static final String MY_FRIEND                 = "/api/me/friends";
    public static final String ADD_FRIEND                = "/api/users/%d/followers";
    public static final String GET_RELATIONSHIP          = "/api/users/%d/friendship?toIds=%s";
    public static final String SCHOOL_APPS               = "/api/mobileschools/apps";
    public static final String GET_SCHOOL_APP            = "/api/mobileschools/app/%d";
    public static final String NEW_FOLLOWER_NOTIFICATION = "/api/me/notifications";
    public static final String DISCUSSION_GROUP          = "/api/me/chatrooms";
    public static final String CLASSROOM_MEMBERS         = "/api/classrooms/%d/members";
    public static final String COURSE_MEMBERS            = "/api/courses/%d/members";
    public static final String ROLE_IN_COURSE            = "/api/courses/%d/membership/%d";

    public static final String LESSON_NOTE     = "/api/courses/%d/notes";
    public static final String GET_LESSON_NOTE = "/api/courses/%d/notes";

    public static final String BIND_LOGIN      = "/api/users/bind_login";
    public static final String BIND_VALIDATION = "/api/tokens";

    public static final String HOMEWORK_CONTENT        = "/api/homework/%d";
    public static final String HOMEWORK_CONTENT_RESULT = "/api/homework/%d/result";
    public static final String HOMEWORK_RESULT         = "/api/homework_results/%d";
    public static final String FILE_UPLOAD             = "/api/upload/%s";

    public static final String EXERCISE_CONTENT        = "/api/exercise/%d";
    public static final String EXERCISE_CONTENT_RESULT = "/api/exercise/%d/result";
    public static final String EXERCISE_RESULT         = "/api/exercise_results/%d";

    public static final String ARTICEL_MENU = "/api/article_categories";
    public static final String ARTICELS     = "/api/articles";

    public static final String UPLOAD_AUDIO_CACHE_FILE       = "/audio";
    public static final String UPLOAD_IMAGE_CACHE_FILE       = "/image";
    public static final String UPLOAD_IMAGE_CACHE_THUMB_FILE = "/image/thumb";

    public static final String MY_COURSES               = "/api/me/courses?";
    public static final String COURSE_LEARNING_DYNAMICS = "/api/course/%d/status";

    public static final String CREATE_THREAD    = "/api/chaos_threads";
    public static final String THREAD_POSTS     = "/api/thread/%d/posts?type=%s";
    public static final String CLASSROOM_THREAD = "/api/classroom/thread/%d";
    public static final String POST_THREAD      = "/api/chaos_threads_posts";

    public static final String MY_CREATED_THREADS = "/api/chaos_threads/getThreads";
    public static final String MY_POSTED_THREADS  = "/api/chaos_threads_posts/getThreadPosts";

    public static final String SEND_PLAY_TIME = "/api/lesson/watch_time";
    public static final String LESSON         = "/api/lessons/%d?hls_encryption=1";
    public static final String WEB_LESSON     = "/mapi_v2/mobile/main#/lesson/%s/%s";

    public static final String UPLOAD_SAVED_DATA_USAGE = "http://121.40.188.27:8001/video_player?size=%d&url=%s&source=mobile&type=cacheStats";

    /*
     * intent action
     */
    public static final String INTENT_TARGET   = "intent_target";
    public static final String SWITCH_NEWS_TAB = "switch_news_tab";

    public static final String LESSON_PLUGIN = "android.intent.action.LESSON_PLUGIN";
    public static final String TOKEN_LOSE    = "token_lose";
    public static final String BADGE_UPDATE  = "badge_update";
    public static final String COURSE_TITLE  = "course_title";

    public static String LIVE_HOST = "http://124.160.104.76:10007";

    public static String REMAINT_TIME = "remain_time";

    public static final String TEACHER_TEACH = "/api/me/courses?relation=teaching&start=0&limit=1000";

    public static String BIND_THIRD_URL = "%s/oauth2/login/app?access_token=%s&openid=%s&type=%s&os=Android";

    public static final int VIDEO_SD  = 1;
    public static final int VIDEO_HD  = 2;
    public static final int VIDEO_SHD = 3;

    public static final String UPDATE_MODE_ALERT = "alert";
    public static final String UPDATE_MODE_FORCE = "force";
}
