package com.edusoho.yunketang.edu.utils;

import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by JesseHuang on 2017/4/6.
 */

public class TimeUtils {
    public static final SimpleDateFormat UTC_DATE_FORMAT       = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT    = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat LIVE_TASK_DATE_FORMAT = new SimpleDateFormat("MM月dd号 HH:mm");
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat TIME_FORMAT_HMS       = new SimpleDateFormat("HH:mm:ss");
    public static final SimpleDateFormat TIME_FORMAT_HM        = new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat TIME_FORMAT_YDM       = new SimpleDateFormat("yy/MM/dd");

    public static String getPostDays(String postTime) {
        long l = 1;
        try {
            Date date = UTC_DATE_FORMAT.parse(postTime);
            l = (new Date().getTime() - date.getTime()) / (1000);

            if (l > 30 * 24 * 60 * 60) {
                return SIMPLE_DATE_FORMAT.format(date);
            } else if (l > 24 * 60 * 60) {
                l = l / (24 * 60 * 60);
                return String.valueOf(l) + "天前";
            } else if (l > 60 * 60) {
                l = l / (60 * 60);
                return String.valueOf(l) + "小时前";
            } else if (l > 60) {
                l = l / (60);
                return String.valueOf(l) + "分钟前";
            }
            if (l < 1) {
                return "刚刚";
            }
        } catch (Exception ex) {
            Log.d("DateUtils::getPostDays", ex.toString());
        }
        return String.valueOf(l) + "秒前";
    }

    /**
     * convert to date type
     *
     * @param time UTC TIME, 1970-01-01T08:00:00+08:00
     * @return
     */
    public static Date getUTCtoDate(String time) {
        Date date = new Date();
        try {
            date = UTC_DATE_FORMAT.parse(time);
        } catch (ParseException ex) {

        }
        return date;
    }

    /**
     * convert to millisecond
     *
     * @param time UTC TIME, 1970-01-01T08:00:00+08:00
     * @return
     */
    public static long getMillisecond(String time) {
        return getUTCtoDate(time).getTime();
    }

    /**
     * convert to custom time
     *
     * @param dateFormat UTC TIME, 1970-01-01T08:00:00+08:00
     * @return
     */
    public static String getStringTime(String time, String dateFormat) {
        String customTime = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = UTC_DATE_FORMAT.parse(time);
            customTime = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customTime;
    }

    public static String getSecond2Min(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    /**
     * 毫秒转剩余天数
     *
     * @param time 剩余毫秒
     * @return e.g.5天12小时23分
     */
    public static String getLeftTime(long time) {
        StringBuilder leftTime = new StringBuilder();
        long day = time / 1000 / 60 / 60 / 24;
        if (day > 0) {
            leftTime.append(day).append("天");
        }
        long hour = time / 60 / 60 / 1000 - day * 24;
        if (hour > 0) {
            leftTime.append(hour).append("小时");
        }
        long min = time / 1000 / 60 - day * 24 * 60 - hour * 60;
        if (min > 0) {
            leftTime.append(min).append("分");
        }
        long second = time / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60;
        if (second >= 0) {
            if (second < 10) {
                leftTime.append("0");
            }
            leftTime.append(second).append("秒");
        }
        return leftTime.toString();
    }

    public static String getTime(long time) {
        String liveTime = "";
        try {
            Date date = new Date(time);
            liveTime = LIVE_TASK_DATE_FORMAT.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return liveTime;
    }

    public static String getTime(SimpleDateFormat sdf, long time) {
        String liveTime = "";
        try {
            Date date = new Date(time);
            liveTime = sdf.format(date);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return liveTime;
    }

    public static String getIMTime(long millis) {
        if (DateUtils.isToday(millis)) {
            Date date = new Date(millis);
            return TIME_FORMAT_HM.format(date);
        }

        Calendar calendarSource = Calendar.getInstance();
        calendarSource.setTimeInMillis(millis);
        calendarSource.add(Calendar.DATE, 1);
        if (DateUtils.isToday(calendarSource.getTimeInMillis())) {
            return "昨天";
        }
        calendarSource.setTimeInMillis(millis);
        if (calendarSource.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) {
            switch (calendarSource.get(Calendar.DAY_OF_WEEK)) {
                case Calendar.SUNDAY:
                    return "星期日";
                case Calendar.MONDAY:
                    return "星期一";
                case Calendar.TUESDAY:
                    return "星期二";
                case Calendar.WEDNESDAY:
                    return "星期三";
                case Calendar.THURSDAY:
                    return "星期四";
                case Calendar.FRIDAY:
                    return "星期五";
                case Calendar.SATURDAY:
                    return "星期六";
            }
        }

        Date date = new Date(millis);
        return TIME_FORMAT_YDM.format(date);
    }

    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }
}
