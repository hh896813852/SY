package com.edusoho.yunketang.edu.utils;

/**
 * Created by JesseHuang on 2017/3/27.
 */

public class Constants {
    public static final int  LIMIT                = 10;
    public static final long TESTPAPER_LIMIT_TIME = 30 * 365 * 24 * 60 * 60 * 1000;

    public static class HttpCode {
        public static final int BAD_REQUEST           = 400;
        public static final int UNAUTHORIZED          = 401;
        public static final int NOT_FOUND             = 404;
        public static final int METHOD_NOT_ALLOWED    = 405;
        public static final int TOO_MANY_REQUESTS     = 429;
        public static final int INTERNAL_SERVER_ERROR = 500;
        public static final int SERVICE_UNAVAILABLE   = 503;
    }

    public static class LiveTaskReplayStatus {
        public static final String VIDEO_GENERATED = "videoGenerated";
        public static final String GENERATED       = "generated";
        public static final String UNGENERATED     = "ungenerated";
    }

    public static class LiveProvider {
        public static final String GENSEE    = "gensee";
        public static final String TALKFUN   = "talkfun";
        public static final String LONGINUS  = "longinus";
        public static final String APOLLO    = "apollo";
        public static final String BAIJIAYUN = "baijiayun";
    }

    public static class VipBuyType {
        public static final String BUY     = "buy";
        public static final String RENEW   = "renew";
        public static final String UPGRADE = "upgrade";
    }

    public static class CouponTargetType {
        public static final String ALL       = "all";
        public static final String VIP       = "vip";
        public static final String COURSE    = "course";
        public static final String CLASSROOM = "classroom";
    }

    public static class BuyType {
        public static final String YEAR  = "Year";
        public static final String MONTH = "month";
    }

    public static class ValidateType {
        public static final String MOBILE_REGISTER       = "mobile_register";
        public static final String MOBILE_RESET_PASSWORD = "mobile_reset_password";
    }

    public static class SmsType {
        public static final String REGISTER = "register";
        public static final String RESET    = "reset";
    }

    public static class HttpError {
        public static final int VALIDATE_FAILED  = 4030301;
        public static final int VALIDATE_EXPIRED = 4030302;
        public static final int PARAMS_MISS      = 4030303;
        public static final int USER_EXIST       = 4030107;
        public static final int NO_MORE_TRY      = 4030602;
        public static final int USER_NOT_EXIST   = 4040104;
    }

    public static class OrderType {
        public static final String CREATED  = "created";
        public static final String SUCCESS  = "success";
        public static final String FINISHED = "finished";
        public static final String PAID     = "paid";
        public static final String REFUNDED = "refunded";
        public static final String CLOSED   = "closed";
    }

    public static class OrderTargetType {
        public static final String COURSE    = "course";
        public static final String CLASSROOM = "classroom";
        public static final String VIP       = "vip";
    }

    public static class TradeGetway {
        public static final String WECHAT_PAY = "WechatPay_MWeb";
        public static final String ALIPAY     = "Alipay_LegacyWap";
    }

    public static class Testpaper {
        public static final class TestMode {
            public static final String NORMAL   = "normal";
            public static final String REALTIME = "realTime";
        }

        public static final class Result {
            /**
             * 批完
             */
            public static final String FINISHED  = "finished";
            /**
             * 未做
             */
            public static final String NODO      = "nodo";
            /**
             * 待批阅
             */
            public static final String REVIEWING = "reviewing";

            public static final String DOING = "doing";
        }

        public static final class State {
            public static final int DO       = 0;
            public static final int ANALYSIS = 1;
        }

        public static final class OptionState {
            public static final int UNSELECTED = 0;
            public static final int SELECTED   = 1;
            public static final int RIGHT      = 2;
            public static final int WRONG      = 3;
        }

        public static final class DoType {
            public static final String DO   = "do";
            public static final String REDO = "redo";
        }
    }
}
