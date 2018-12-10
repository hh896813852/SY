package com.edusoho.yunketang.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author huhao on 2018/1/18.
 */

public class DateUtils {

    /**
     * 1: 表示现在的时间大于被比较的时间
     * 0：表示现在的时间小于被比较的时间
     * -1: 报错
     * 注：yyyy-MM-dd HH:mm:ss 其中 HH 表示24小时制， hh 表示12小时制
     */
    public static int compareNowDate(String compareDateString, String format) {
        // 格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date compareDate = CurrentTime.parse(compareDateString);
            if ((System.currentTimeMillis() - compareDate.getTime()) > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 1: 表示第一个时间大于第二个的时间
     * 0：表示第一个时间小于第e二个的时间
     * -1: 报错
     */
    public static int compareTwoDate(String firstTime, String twoTime, String format) {
        // 格式化时间
        SimpleDateFormat CurrentTime = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date firstDate = CurrentTime.parse(firstTime);
            Date twoDate = CurrentTime.parse(twoTime);
            if ((firstDate.getTime() - twoDate.getTime()) > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 调此方法输入所要转换的时间，输入例如（"2014年06月14日16时09分00秒"）返回时间戳（秒）
     */
    public static String getTimestamp(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date date = dateFormat.parse(time);
            return String.valueOf(date.getTime()).substring(0, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 调此方法输入所要转换的时间，输入例如（"2014年06月14日16时09分00秒"）返回时间戳(毫秒)
     */
    public static String getTimestampMilli(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date date = dateFormat.parse(time);
            return String.valueOf(date.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param timestamp 某一时间戳 单位：毫秒
     * @return <0:过去 0：今天 1：明天 2：后天 >3：大于后天
     */
    public static int getDateType(long timestamp) {
        // 时间差
        long deltaT = Long.parseLong(dataFormat(timestamp)) - Long.parseLong(dataFormat(System.currentTimeMillis()));
        return (int) (deltaT / 86400);
    }

    /**
     * 时间戳取整（例：将2018/01/09 11:22:33 的时间戳格式化为2018/01/09 00:00:00的时间戳）
     */
    private static String dataFormat(long timestamp) {
        return getTimestamp(formatDate(timestamp, "yyyy/MM/dd"), "yyyy/MM/dd");
    }

    /**
     * 输入Date 输出（"2014年06月14日16时09分00秒"）
     */
    public static String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(date);
    }

    /**
     * 输入"2014年06月14日16时09分00秒" 输出 Date
     */
    public static Date toDate(String dateStr, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 输入时间戳（1402733340）转化为 Date
     */
    public static Date toDate(long dateStamp) {
        Calendar calendar = getCalendar(dateStamp);
        return calendar.getTime();
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014年06月14日16时09分00秒"）
     *
     * @param timestamp 时间戳，单位：毫秒
     */
    public static String formatDate(long timestamp, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        return dateFormat.format(new Date(timestamp));
    }

    /**
     * 打招呼
     *
     * @param timestamp 时间戳，单位：毫秒
     */
    public static String hello(long timestamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("HH", Locale.CHINA);
        String times = sdr.format(new Date(timestamp));
        int hour = Integer.valueOf(times);
        if (hour < 6) {
            return "凌晨好！";
        }
        if (hour >= 6 && hour < 12) {
            return "上午好！ ";
        }
        if (hour >= 12 && hour < 18) {
            return "下午好！ ";
        }
        if (hour >= 18) {
            return "晚上好！ ";
        }
        return "";
    }

    /**
     * 返回日("1516261426000" -> 18)
     * 注意：timestamp 单位：毫秒
     */
    public static String getDay(long timestamp) {
        int day = 0;
        try {
            day = getCalendar(timestamp).get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(day);
    }

    /**
     * 返回日 (Date -> day)
     */
    public static String getDay(Date date) {
        int day = 0;
        try {
            day = getCalendar(date).get(Calendar.DAY_OF_MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return String.valueOf(day);
    }


    /**
     * 输入时间戳变星期
     *
     * @param timestamp 单位：毫秒
     */
    public static String getWeek(long timestamp) {
        int week = 0;
        try {
            week = getCalendar(timestamp).get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getWeek(week);
    }

    /**
     * 输入日期如（2014年06月14日16时09分00秒）返回（星期数）
     */
    public static String getWeek(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        int week = 0;
        try {
            Date date = dateFormat.parse(time);
            week = getCalendar(date).get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getWeek(week);
    }

    /**
     * 输入Date 返回（星期数）
     */
    public static String getWeek(Date date) {
        int week = 0;
        try {
            week = getCalendar(date).get(Calendar.DAY_OF_WEEK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getWeek(week);
    }

    private static String getWeek(int week) {
        switch (week) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }

    /**
     * 秒 -> 分钟 ：秒
     * 秒 -> 小时：分钟 ：秒
     */
    public static String second2Min(int time) {
        String timeStr;
        int hour;
        int minute;
        int second;
        if (time <= 0) {
            return "00:00";
        } else {
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

    private static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    public static String getPostDays(String postTime) {
        long l = 1;
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(postTime);
            l = (new Date().getTime() - date.getTime()) / (1000);

            if (l > 30 * 24 * 60 * 60) {
                return new SimpleDateFormat("yyyy-MM-dd").format(date);
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
     * 上个月的Calendar
     */
    public static Calendar getLastMonthCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -1);
        return calendar;
    }

    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public static Calendar getCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * @param timestamp 时间戳，单位：毫秒
     */
    public static Calendar getCalendar(long timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar;
    }

    /**
     * @param time   ex:1991-09-09
     * @param format ex:"yyyy-MM-dd"
     */
    public static Calendar getCalendar(String time, String format) {
        return getCalendar(Long.parseLong(getTimestampMilli(time, format)));
    }

    /**
     * 根据提供的年月获取该月份的最后一天
     */
    public static int getLastDayOfMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        SimpleDateFormat format = new SimpleDateFormat("dd", Locale.CHINA);
        return Integer.valueOf(format.format(cal.getTime()));
    }
}
