package com.edusoho.yunketang;

/**
 * Created by huhao on 18/11/8.
 */
public class SYConstants {
    public static final int SYJY_LOGIN = 1;
    public static final int SYZX_LOGIN = 2;
    public static final int SYKJ_LOGIN = 3;

    public static final int PAGE_SIZE = 10;
    public static final int PAGE_SIZE_20 = 20;
//    public static String HTTP_URL = "http://39.105.174.45:8087/shangyuan-app-web/"; // 刘健云服务器
//    public static String HTTP_URL = "http://192.168.0.253:8087/shangyuan-app-web/"; // 刘健本地环境
//    public static String HTTP_URL = "http://192.168.0.128:8088/shangyuan-app-web/"; // 余斌本地环
//    public static String HTTP_URL = "http://47.99.198.187:8087/shangyuan-app-web/"; // 上元教育测试环境
    public static String HTTP_URL = "http://sy.233863.com/shangyuan-app-web/";   // 上元教育正式环境
    public static String HTTP_URL_ONLINE = "http://www.233863.com/";     // 上元在线
    public static String HTTP_URL_ACCOUNTANT = "http://www.sykjxy.com/"; // 上元会计

    /**
     * 验证手机号码是否已经注册
     */
    public static final String VALIDATE_MOBILE = HTTP_URL + "app/validateMobile";
    /**
     * 发送验证码
     * GET
     */
    public static final String SEND_SMS = HTTP_URL + "qcloud/sms/smsSingleSender?mobile=%s";
    /**
     * 注册
     */
    public static final String USER_REGISTER = HTTP_URL + "app/register";
    /**
     * 登录（上元教育）
     */
    public static final String USER_SYJY_LOGIN = HTTP_URL + "app/syjy/login";
    /**
     * 登录（上元会计）
     */
    public static final String USER_SYKJ_LOGIN = HTTP_URL + "app/sykj/login";
    /**
     * 登录（上元在线）
     */
    public static final String USER_SYZX_LOGIN = HTTP_URL + "app/syzx/login";
    /**
     * 上元教育支付宝支付
     */
    public static final String SY_ALIPAY = HTTP_URL + "app/pay/alipay/alipayPayParams";
    /**
     * 上元教育微信支付
     */
    public static final String SY_WXPAY = HTTP_URL + "app/pay/wxpay/wxPayParams";
    /**
     * 行业类型
     */
    public static final String BUSINESS_TYPE = HTTP_URL + "app/business/type/business";
    /**
     * 所有课程数据
     */
    public static final String ALL_COURSE = HTTP_URL + "app/course/query/all";
    /**
     * 行业模块查询
     */
    public static final String MODULE_QUERY = HTTP_URL + "app/moudle/query";
    /**
     * 我的错题分页查询
     */
    public static final String MY_FAULTS = HTTP_URL + "app/homework/error/query/page";
    /**
     * 一键查询所有我的错题
     */
    public static final String HOMEWORK_ALL_FAULTS = HTTP_URL + "app/homework/error/user/page/query";
    /**
     * 删除我的错题
     */
    public static final String DELETE_FAULT_QUESTION = HTTP_URL + "app/homework/error/delete";
    /**
     * 模块练习分页查询
     */
    public static final String MODULE_EXERCISE = HTTP_URL + "app/homework/moudle/query/page";
    /**
     * 模块练习分页查询（未登录）
     */
    public static final String MODULE_EXERCISE_NOT_LOGIN = HTTP_URL + "app/homework/module/query/page/unlogin";
    /**
     * 查询试卷题干
     */
    public static final String QUESTION_STEM_QUERY = HTTP_URL + "app/examination/query";
    /**
     * 根据题目类型，查询全部题目
     */
    public static final String QUESTION_QUERY = HTTP_URL + "app/examination/query/question/type";
    /**
     * 告诉后台当前页面和已完成题目数量
     */
    public static final String HOMEWORK_REDIS = HTTP_URL + "app/homework/redis";
    /**
     * 题目 添加收藏
     */
    public static final String QUESTION_ADD_COLLECTION = HTTP_URL + "app/homework/star/save";
    /**
     * 题目 取消收藏
     */
    public static final String QUESTION_CANCEL_COLLECTION = HTTP_URL + "app/homework/star/cancel";
    /**
     * 题目提交
     */
    public static final String QUESTION_COMMIT = HTTP_URL + "app/homework/save";
    /**
     * 试卷最终提交
     */
    public static final String EXAMINATION_COMMIT = HTTP_URL + "app/homework/examination/save";
    /**
     * 整套题目提交
     */
    public static final String EXAMINATION_COMMIT_ALL = HTTP_URL + "app/homework/save/perfectly";
    /**
     * 查看答题报告
     */
    public static final String CHECK_REPORT = HTTP_URL + "app/homework/examination/report";
    /**
     * 再做一遍（模块）
     */
    public static final String DO_AGAIN_IN_MODULE = HTTP_URL + "app/homework/do/again/module";
    /**
     * 再做一遍（班级）
     */
    public static final String DO_AGAIN_IN_CLASS = HTTP_URL + "app/homework/do/again/class";
    /**
     * 我的班级
     */
    public static final String MY_CLASS = HTTP_URL + "app/class/classList";
    /**
     * 班级课程列表
     */
    public static final String CLASS_COURSE_LIST = HTTP_URL + "app/class/courseDetailList";
    /**
     * 班级课程作业列表
     */
    public static final String CLASS_HOMEWORK = HTTP_URL + "app/homework/class/query/page";
    /**
     * 课程上课日期
     * GET
     */
    public static final String CLASS_DATE = HTTP_URL + "app/class/queryClassDayList?classId=%s";
    /**
     * 图片上传
     */
    public static final String PIC_UPLOAD = HTTP_URL + "app/oss/qiniu/upload";
    /**
     * 我的收藏（题库）
     */
    public static final String MY_COLLECT_HOMEWORK = HTTP_URL + "app/homework/star/query/page";
    /**
     * 某套试卷全部收藏的题目
     */
    public static final String ALL_MY_EXAM_COLLECT = HTTP_URL + "app/homework/star/user/page/query";
    /**
     * 保存用户信息
     */
    public static final String SAVE_PERSONAL_INFO = HTTP_URL + "app/student/save";
    /**
     * 我购买的试卷
     */
    public static final String MY_BOUGHT_EXAM = HTTP_URL + "app/homework/pay/list";
    /**
     * 模块练习排行
     */
    public static final String MODULE_RANK = HTTP_URL + "app/homework/module/rank";
    /**
     * 班级排名
     */
    public static final String CLASS_RANK = HTTP_URL + "app/homework/class/rank";
    /**
     * 未读消息数量
     */
    public static final String UNREAD_MSG_COUNT = HTTP_URL + "app/studenthomeworkmessage/getcountofnotread";
    /**
     * 我的消息
     */
    public static final String MY_MESSAGE = HTTP_URL + "app/studenthomeworkmessage/listforstudent";
    /**
     * 老师批注的题目
     */
    public static final String TEACHER_POSLIL = HTTP_URL + "app/studenthomeworkmessage/getmistakeInfoformistakeId";
    /**
     * 扫码考勤
     * GET
     */
    public static final String CHECKING_IN = "http://%s&stuId=%s&stuLongitude=%s&stuLatitude=%s&source=1";

    // ----------------------------------------   以上为上元教育接口  ------------------------------------- //

    /**
     * 发送验证码（上元在线）
     */
    public static final String SEND_ONLINE_SMS = HTTP_URL_ONLINE + "api/sms_center";
    /**
     * 发送验证码（上元会计）
     */
    public static final String SEND_ACCOUNTANT_SMS = HTTP_URL_ACCOUNTANT + "api/sms_center";

    /**
     * 注册 （上元在线）
     */
    public static final String USER_ONLINE_REGISTER = HTTP_URL_ONLINE + "api/user";
    /**
     * 注册 （上元会计）
     */
    public static final String USER_ACCOUNTANT_REGISTER = HTTP_URL_ACCOUNTANT + "api/user";
    /**
     * 上元在线banner
     */
    public static final String ONLINE_BANNER = HTTP_URL_ONLINE + "mapi_v2/School/getSchoolBanner";
    /**
     * 上元会计banner
     */
    public static final String ACCOUNTANT_BANNER = HTTP_URL_ACCOUNTANT + "mapi_v2/School/getSchoolBanner";
    /**
     * 上元在线首页数据
     */
    public static final String ONLINE_CHANNELS = HTTP_URL_ONLINE + "api/app/channels";
    /**
     * 上元会计首页数据
     */
    public static final String ACCOUNTANT_CHANNELS = HTTP_URL_ACCOUNTANT + "api/app/channels";

    /**
     * 上元在线课程详情1
     */
    public static final String ONLINE_COURSE_DETAILS = HTTP_URL_ONLINE + "api/course_sets/%s";
    /**
     * 上元会计课程详情1
     */
    public static final String ACCOUNTANT_COURSE_DETAILS = HTTP_URL_ACCOUNTANT + "api/course_sets/%s";
    /**
     * 上元在线课程详情2
     */
    public static final String ONLINE_COURSES = HTTP_URL_ONLINE + "api/course_sets/%s/courses";
    /**
     * 上元会计课程详情2
     */
    public static final String ACCOUNTANT_COURSES = HTTP_URL_ACCOUNTANT + "api/course_sets/%s/courses";

    /**
     * 上元在线课程目录
     */
    public static final String ONLINE_COURSE_CATALOGUE = HTTP_URL_ONLINE + "api/courses/%s/items?onlyPublished=1";
    /**
     * 上元会计课程目录
     */
    public static final String ACCOUNTANT_COURSE_CATALOGUE = HTTP_URL_ACCOUNTANT + "api/courses/%s/items?onlyPublished=1";

    /**
     * 上元在线课程评价
     */
    public static final String ONLINE_COURSE_EVALUATE = HTTP_URL_ONLINE + "api/course_sets/%s/reviews?offset=%s&limit=%s";
    /**
     * 上元会计课程评价
     */
    public static final String ACCOUNTANT_COURSE_EVALUATE = HTTP_URL_ACCOUNTANT + "api/course_sets/%s/reviews?offset=%s&limit=%s";

    /**
     * 上元在线 加入的项目（可能包含多个课程）
     * GET
     */
    public static final String ONLINE_COURSE_MEMBERS = HTTP_URL_ONLINE + "api/me/course_sets/%s/course_members";
    /**
     * 上元会计 加入的项目（可能包含多个课程）
     * GET
     */
    public static final String ACCOUNTANT_COURSE_MEMBERS = HTTP_URL_ACCOUNTANT + "api/me/course_sets/%s/course_members";

    /**
     * 上元在线 加入的课程
     * GET
     */
    public static final String ONLINE_COURSE_MEMBER = HTTP_URL_ONLINE + "api/me/course_members/%s";
    /**
     * 上元会计 加入的课程
     * GET
     */
    public static final String ACCOUNTANT_COURSE_MEMBER = HTTP_URL_ACCOUNTANT + "api/me/course_members/%s";

    /**
     * 上元在线是 退出课程
     * DELETE请求
     */
    public static final String ONLINE_COURSE_EXIT = HTTP_URL_ONLINE + "api/me/course_members/%s";
    /**
     * 上元会计 退出课程
     * DELETE请求
     */
    public static final String ACCOUNTANT_COURSE_EXIT = HTTP_URL_ACCOUNTANT + "api/me/course_members/%s";

    /**
     * 上元在线 获取课程
     */
    public static final String ONLINE_GET_LESSON = HTTP_URL_ONLINE + "api/lessons/%s?hls_encryption=1";
    /**
     * 上元会计 获取课程
     */
    public static final String ACCOUNTANT_GET_LESSON = HTTP_URL_ACCOUNTANT + "api/lessons/%s?hls_encryption=1";

    /**
     * 上元在线 是否收藏
     * GET请求
     */
    public static final String ONLINE_IS_COLLECT = HTTP_URL_ONLINE + "api/me/favorite_course_sets/%s";
    /**
     * 上元会计 是否收藏
     * GET请求
     */
    public static final String ACCOUNTANT_IS_COLLECT = HTTP_URL_ACCOUNTANT + "api/me/favorite_course_sets/%s";

    /**
     * 上元在线 取消收藏
     * DELETE请求
     */
    public static final String ONLINE_COLLECT_CANCEL = HTTP_URL_ONLINE + "api/me/favorite_course_sets/%s";
    /**
     * 上元会计 取消收藏
     * DELETE请求
     */
    public static final String ACCOUNTANT_COLLECT_CANCEL = HTTP_URL_ACCOUNTANT + "api/me/favorite_course_sets/%s";

    /**
     * 上元在线 收藏
     * POST表单请求
     */
    public static final String ONLINE_COLLECT = HTTP_URL_ONLINE + "api/me/favorite_course_sets";
    /**
     * 上元会计 收藏
     * POST表单请求
     */
    public static final String ACCOUNTANT_COLLECT = HTTP_URL_ACCOUNTANT + "api/me/favorite_course_sets";

    /**
     * 上元在线 获取验证图片和token
     * POST表单请求
     */
    public static final String ONLINE_GET_VALIDATE_PIC = HTTP_URL_ONLINE + "api/drag_captcha";
    /**
     * 上元会计 获取验证图片和token
     * POST表单请求
     */
    public static final String ACCOUNTANT_GET_VALIDATE_PIC = HTTP_URL_ACCOUNTANT + "api/drag_captcha";

    /**
     * 上元在线 验证图片
     * GET
     */
    public static final String ONLINE_VALIDATE_CAPTCHA = HTTP_URL_ONLINE + "api/drag_captcha/%s";
    /**
     * 上元会计 验证图片
     * GET
     */
    public static final String ACCOUNTANT_VALIDATE_CAPTCHA = HTTP_URL_ACCOUNTANT + "api/drag_captcha/%s";

    /**
     * 上元在线 发布评价
     * POST
     */
    public static final String ONLINE_EVALUATE = HTTP_URL_ONLINE + "api/courses/%s/reviews";
    /**
     * 上元会计 发布评价
     * POST
     */
    public static final String ACCOUNTANT_EVALUATE = HTTP_URL_ACCOUNTANT + "api/courses/%s/reviews";
    /**
     * 上元在线 我的收藏
     * GET
     */
    public static final String ONLINE_VIDEO_COLLECTION = HTTP_URL_ONLINE + "api/me/favorite_course_sets?offset=0&limit=1000";
    /**
     * 上元会计 我的收藏
     * GET
     */
    public static final String ACCOUNTANT_VIDEO_COLLECTION = HTTP_URL_ACCOUNTANT + "api/me/favorite_course_sets?offset=0&limit=1000";
    /**
     * 上元在线 我的元宝
     * POST
     */
    public static final String ONLINE_MY_COIN = HTTP_URL_ONLINE + "mapi_v2/User/getUserCoin";
    /**
     * 上元会计 我的元宝
     * POST
     */
    public static final String ACCOUNTANT_MY_COIN = HTTP_URL_ACCOUNTANT + "mapi_v2/User/getUserCoin";
    /**
     * 上元在线 我购买的视频
     * POST
     */
    public static final String ONLINE_MY_VIDEO = HTTP_URL_ONLINE + "api/me/courses?offset=0&limit=1000";
    /**
     * 上元会计 我购买的视频
     * POST
     */
    public static final String ACCOUNTANT_MY_VIDEO = HTTP_URL_ACCOUNTANT + "api/me/courses?offset=0&limit=1000";

    public static final String MOBILE_APP_VERSION = "mobile/%s/version?code=%s";
    public static final String MOBILE_APP_RESOURCE = "mobile/%s/resources";
    public static final String MOBILE_APP_URL = "%smobile/%s";
    /**
     * html5 url
     */
    public static final String WEB_URL = "web_url";
    public static final String MY_LEARN = "main#/mylearn";
    public static final String MY_INFO = "main#/myinfo";
    public static final String MY_FAVORITE = "main#/myfavorite";
    public static final String USER_PROFILE = "main#/userinfo/%d";
    public static final String MOBILE_WEB_COURSE = "main#/course/%d";
    public static final String MOBILE_WEB_CLASSROOMS = "main#/classroomlist/?categoryId=%d&orderType=%s";
    public static final String MOBILE_WEB_COURSES = "main#/courselist/normal/?categoryId=%d&orderType=%s";
    public static final String MOBILE_WEB_LIVE_COURSES = "main#/courselist/live/?categoryId=%d&orderType=%s";
    public static final String MOBILE_SEARCH = "main#/search";
    public static final String ANNOUNCEMENT = "main#/coursenotice/course/%d";
    public static final String ARTICLE_CONTENT = "%smobile/main#/article/%d";
    public static final String CLASSROOM_ANNOUNCEMENT = "main#/coursenotice/classroom/%d";
    public static final String COURSE_ANNOUNCEMENT = "main#/coursenotice/course/%d";
    public static final String CLASSROOM_COURSES = "main#/classroom/%d";
    public static final String CLASSROOM_MEMBER_LIST = "main#/studentlist/classroom/%d";
    public static final String COURSE_MEMBER_LIST = "main#/studentlist/course/%d";
    public static final String TEACHER_MANAGERMENT = "main#/todolist/%d";
    public static final String HTML5_LESSON = "main#/lesson/%d/%d";
    public static final String HTML5_POINT_INFO = "/h5/reward_point/rule";
}
