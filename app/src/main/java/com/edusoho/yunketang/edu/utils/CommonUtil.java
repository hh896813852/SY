package com.edusoho.yunketang.edu.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.bean.RequestUrl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;


public class CommonUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public static int px2sp(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue / scale + 0.5f);
    }

    public static Bitmap getBitmapFromFile(File file) {
        Bitmap bitmap = null;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), option);
        int width = (int) (SYApplication.getInstance().screenW * 0.5f);
        option.inSampleSize = computeSampleSize(option, -1, width * width);
        option.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
            Log.d(null, "bm->" + bitmap);
        } catch (Exception e) {
            bitmap = null;
        }

        return bitmap;
    }

    public static float getTextViewLength(TextView textView, String text) {
        TextPaint paint = textView.getPaint();
        float textLength = paint.measureText(text);
        return textLength;
    }

    public static boolean inArray(String find, String[] array) {
        if (array == null || array.length == 0) {
            return false;
        }
        int result = Arrays.binarySearch(array, find, new Comparator<String>() {
            @Override
            public int compare(String find, String str) {
                if (str.equals(find)) {
                    return 0;
                }
                return -1;
            }
        });
        return result >= 0;
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static void touchByGestureDetector(
            View view, GestureDetector.SimpleOnGestureListener gestureListener) {
        final GestureDetector gestureDetector = new GestureDetector(gestureListener);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });
    }

    public static void viewTreeObserver(View view, final InputUtils.NormalCallback callback) {
        final ViewTreeObserver observer = view.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                callback.success(null);
                if (observer.isAlive()) {
                    observer.removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    public static ObjectAnimator animForHeight(Object view, int start, int end, int time) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(
                view, "height", start, end);
        objectAnimator.setDuration(time);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.start();
        return objectAnimator;
    }

    public static void animForHeightWithListener(
            Object view, int start, int end, int time, Animator.AnimatorListener animatorListener) {
        ObjectAnimator objectAnimator = animForHeight(view, start, end, time);
        objectAnimator.addListener(animatorListener);
    }

    public static String coverUrlToCacheKey(RequestUrl requestUrl) {
        StringBuilder builder = new StringBuilder(requestUrl.url);


        HashMap<String, String> map = (HashMap<String, String>) requestUrl.params;

        for (String key : map.keySet()) {
            builder.append("&").append(key);
            builder.append("&").append(map.get(key));
        }

        builder.append("&token=").append(requestUrl.heads.get("token"));
        return DigestUtils.md5(builder.toString());
    }

    public static String gzip(String input) {
        String result = null;
        ByteArrayInputStream reader = null;
        GZIPOutputStream gzipOutputStream = null;
        try {
            reader = new ByteArrayInputStream(input.getBytes());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
            gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream);

            int len = -1;
            byte[] buffer = new byte[1024];
            while ((len = reader.read(buffer)) != -1) {
                gzipOutputStream.write(buffer, 0, len);
            }

            result = byteArrayOutputStream.toString("utf-8");
        } catch (Exception e) {
            //nothing
        } finally {
            try {
                reader.close();
                gzipOutputStream.close();
            } catch (Exception e) {
                //nothing}
            }
        }

        return result;
    }

    public static String unGzip(String input) {
        StringBuilder builder = new StringBuilder();
        GZIPInputStream gzipInputStream = null;
        try {
            int len = -1;
            byte[] buffer = new byte[1024];
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input.getBytes());
            gzipInputStream = new GZIPInputStream(byteArrayInputStream);

            while ((len = gzipInputStream.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, len));
            }
        } catch (Exception e) {
            //nothing
        } finally {
            try {
                gzipInputStream.close();
            } catch (Exception e) {
                //nothing}
            }
        }

        return builder.toString();
    }

    public static String coverCourseAbout(String about) {
        return about.replaceAll("<[^>]+>", "");
    }

    public static int getCourseCorverHeight(int width) {
        float scale = (float) width / 480;
        return (int) (270 * scale);
    }

    /**
     * 转换图片长宽比
     *
     * @param width
     * @return
     */

    public static int getImageWidth(int width) {
        float scale = (float) width * 0.4f / 480;
        return (int) (270 * scale);
    }

    public static int getCourseListCoverHeight(int width) {
        float scale = (float) width / 480;
        return (int) (270 * scale);
    }

    public static int getLearnCourseListCoverHeight(int width) {
        float scale = (float) width * 0.9f / 480;
        return (int) (270 * scale);
    }

    public static String coverLessonContent(String content) {
        return content.replaceAll("href=[^=]+\\s", "href='javascript:void();' ");
    }

    public static String coverTime(String time) {
        return "".equals(time) ? "" : time.substring(0, 10);
    }

    public static String goalsToStr(String[] goals) {
        StringBuffer sb = new StringBuffer();
        for (String goal : goals) {
            sb.append("・").append(goal).append("\n");
        }
        if (TextUtils.isEmpty(sb)) {
            return "";
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String audiencesToStr(String[] audiences) {
        StringBuffer sb = new StringBuffer();
        for (String audience : audiences) {
            sb.append("・").append(audience).append("\n");
        }
        if (TextUtils.isEmpty(sb)) {
            return "";
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static void enableBtn(ViewGroup vg, boolean isEnable) {
        int count = vg.getChildCount();
        for (int i = 0; i < count; i++) {
            vg.getChildAt(i).setEnabled(isEnable);
        }
    }

    /**
     * @param layout
     * @param offset
     */
    public static void moveLayout(
            Context context, final View layout, final int offset, int type, int defsize, int time) {
        final int w = layout.getWidth();
        final int h = layout.getHeight();
        final Handler handler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                layout.layout(msg.arg1, 0, w + msg.arg1, h);
            }
        };

        Timer moveTimer = new Timer();
        moveTimer.schedule(new MoveTimerTask(offset, type, defsize) {
            @Override
            public void run() {
                if (step()) {
                    Message msg = handler.obtainMessage();
                    this.mOffset += this.DEF_SIZE;
                    msg.arg1 = this.mOffset;
                    msg.sendToTarget();
                } else {
                    cancel();
                }
            }
        }, 1, time);
    }

    /**
     * @param v1
     * @param v2
     * @return
     * @throws RuntimeException
     */
    public static int compareVersion(String v1, String v2) throws RuntimeException {
        if (v1 == null || v2 == null) {
            return Const.NORMAL_VERSIO;
        }
        String[] v1Versons = v1.split("\\.");
        String[] v2Versons = v2.split("\\.");
        if (v1Versons.length != v2Versons.length) {
            throw new RuntimeException("版本不一致，无法对比");
        }

        int length = v1Versons.length;
        for (int i = 0; i < length; i++) {
            int firstVersion = Integer.parseInt(v1Versons[i]);
            int secoundVersion = Integer.parseInt(v2Versons[i]);
            if (firstVersion > secoundVersion) {
                return Const.HEIGHT_VERSIO;
            }
            if (firstVersion < secoundVersion) {
                return Const.LOW_VERSIO;
            }
        }

        return Const.NORMAL_VERSIO;
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} to a fullscreen opaque
     * Activity.
     * <p/>
     * Call this whenever the background of a translucent Activity has changed
     * to become opaque. Doing so will allow the {@link android.view.Surface} of
     * the Activity behind to be released.
     * <p/>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityFromTranslucent(Activity activity) {
        try {
            Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
            method.setAccessible(true);
            method.invoke(activity);
        } catch (Throwable t) {
        }
    }

    /**
     * Convert a translucent themed Activity
     * {@link android.R.attr#windowIsTranslucent} back from opaque to
     * translucent following a call to
     * {@link #convertActivityFromTranslucent(Activity)} .
     * <p/>
     * Calling this allows the Activity behind this one to be seen again. Once
     * all such Activities have been redrawn
     * <p/>
     * This call has no effect on non-translucent activities or on activities
     * with the {@link android.R.attr#windowIsFloating} attribute.
     */
    public static void convertActivityToTranslucent(Activity activity) {
        try {
            Class<?>[] classes = Activity.class.getDeclaredClasses();
            Class<?> translucentConversionListenerClazz = null;
            for (Class clazz : classes) {
                if (clazz.getSimpleName().contains("TranslucentConversionListener")) {
                    translucentConversionListenerClazz = clazz;
                }
            }
            Method method = Activity.class.getDeclaredMethod("convertToTranslucent",
                    translucentConversionListenerClazz);
            method.setAccessible(true);
            method.invoke(activity, new Object[]{
                    null
            });
        } catch (Throwable t) {
        }
    }

    /**
     * 将服务器端的时间格式转化为milli Second
     *
     * @param time
     * @return
     */
    public static long convertMilliSec(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long returnTime = 0;
        try {
            String tDate = time.split("[+]")[0].replace('T', ' ');
            return sdf.parse(tDate).getTime();

        } catch (Exception ex) {
            Log.d("AppUtil.convertMilliSec", ex.toString());
        }
        return returnTime;
    }

    /**
     * 根据时间转化私信显示的时间
     * 当天显示，18：00
     * 昨天显示，昨天 18：00
     * 比昨天更早，星期几 18：00
     *
     * @param t
     * @return
     */
    public static String convertWeekTime(String t) {
        String result = "";
        try {
            String tDate = t.split("[+]")[0].replace('T', ' ');
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar nowCalendar = Calendar.getInstance();
            Date paramDate = sdf.parse(tDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(paramDate);
            int interval = nowCalendar.get(Calendar.DATE) - calendar.get(Calendar.DATE);
            String postTime = (calendar.get(Calendar.HOUR_OF_DAY) >= 10 ? calendar.get(Calendar.HOUR_OF_DAY) : "0" + calendar.get(Calendar.HOUR)) + ":"
                    + (calendar.get(Calendar.MINUTE) >= 10 ? calendar.get(Calendar.MINUTE) : ("0" + calendar.get(Calendar.MINUTE)));
            if (interval == 0) {
                result = postTime;
            } else if (interval == 1) {
                result = "昨天 " + postTime;
            } else {
                result = (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DATE) + "日 " + postTime;
            }
        } catch (Exception ex) {
            Log.d("AppUtil.getPostDays", ex.toString());
        }
        return result;
    }

    /**
     * 计算发布问题天数,服务端获取时间格式：2014-05-20T22:03:43+08:00
     * 转换为天数或者小时
     */
    public static String getPostDays(String postTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long l = 1;
        try {
            String tDate = postTime.split("[+]")[0].replace('T', ' ');
            long milliSec = 1000;
            Date date = new Date();
            l = (date.getTime() - sdf.parse(tDate).getTime()) / (milliSec);

            //如果大于24返回天数
            if (l > 30 * 24 * 60 * 60) {
                return postTime.split("T")[0];
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
            Log.d("AppUtil.getPostDays", ex.toString());
        }

        return String.valueOf(l) + "秒前";
    }

    /**
     * 将毫秒转换为年月日时分秒
     */
    public static String conver2Date(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(millis);
    }

    /**
     * 将服务器返回的秒转化
     * 当天则显示时分
     * 近期的则显示星期 + 时分
     * 远的则显示年月日时分
     */
    public static final long ONE_WEEK = 1000 * 60 * 60 * 24 * 7;

    public static String convertMills2Date(long millis) {
        String result = "";
        String showTime = "";
        if (millis <= 0) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
            String nowTime = sdf.format(System.currentTimeMillis());
            showTime = sdf.format(millis);
            if (nowTime.substring(0, 8).equals(showTime.substring(0, 8))) {
                // 如果是当天
                return showTime.substring(9);
            } else if (System.currentTimeMillis() - millis < ONE_WEEK) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis);
                switch (calendar.get(Calendar.DAY_OF_WEEK) - 1) {
                    case 1:
                        result = "星期一";
                        break;
                    case 2:
                        result = "星期二";
                        break;
                    case 3:
                        result = "星期三";
                        break;
                    case 4:
                        result = "星期四";
                        break;
                    case 5:
                        result = "星期五";
                        break;
                    case 6:
                        result = "星期六";
                        break;
                    default:
                        result = "星期日";
                        break;
                }
            } else {
                return showTime;
            }
        } catch (Exception ex) {
            Log.e("convertMills2Date", ex.getMessage());
        }
        return result + " " + showTime.substring(9);
    }

    /**
     * 计算多久之后开始直播时间 格式为13位毫秒单位
     */
    public static String getLiveTime(long startTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        long l = 1;
        try {
            Date date = new Date();
            l = (startTime - date.getTime()) / (1000L);

            //如果大于7返回天数
            if (l > 7 * 24 * 60 * 60) {
                return sdf.format(new Date(startTime));
            } else if (l > 24 * 60 * 60) {
                l = l / (24 * 60 * 60);
                return String.valueOf(l) + "天后";
            } else if (l > 60 * 60) {
                l = l / (60 * 60);
                return String.valueOf(l) + "小时后";
            } else if (l > 60) {
                l = l / (60);
                return String.valueOf(l) + "分钟后";
            }
        } catch (Exception ex) {
            Log.d("AppUtil.getTime", ex.toString());
        }

        return String.valueOf(l) + "秒后";
    }

    /**
     * 去掉末尾产生的"\n"
     */
    public static String removeHtml(String strHtml) {
        if (strHtml.length() > 0 && strHtml.contains("\n")) {
            if (strHtml.substring(strHtml.length() - 1, strHtml.length()).equals("\n")) {
                strHtml = strHtml.substring(0, strHtml.length() - 1);
                return removeHtml(strHtml);
            }
        }
        return strHtml;
    }

    /**
     * 图片缩小
     *
     * @param bitmap    图片
     * @param imageSize 图片大小
     * @param degree    图片旋转的角度，如果没有旋转，则为0
     * @param context   context
     * @return
     */
    public static Bitmap scaleImage(Bitmap bitmap, float imageSize, int degree, Context context) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //float density = context.getResources().getDisplayMetrics().density;
        int bounding = Math.round(imageSize);

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;

        Matrix matrix = new Matrix();
        matrix.postScale(xScale, xScale);
        matrix.postRotate((float) degree);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;
    }

    public static Bitmap scaleImageBySize(
            Bitmap bitmap, int imageSize, Context context) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float density = context.getResources().getDisplayMetrics().density;
        int bounding = Math.round(imageSize * density);

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(xScale, yScale);

        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;
    }

    /**
     * 图片压缩到500K(质量压缩)
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, ByteArrayOutputStream baos, int size) {
        image.compress(Bitmap.CompressFormat.JPEG, size, baos);
        Bitmap bitmap = BitmapFactory.decodeByteArray(baos.toByteArray(), 0, baos.toByteArray().length);
        return bitmap;
    }

    /**
     * 创建临时图片文件
     *
     * @param path
     * @param os
     * @return
     */
    public static File createFile(String path, ByteArrayOutputStream os, int imageName) {
        File f = null;
        try {
            f = new File(path, "tmpImage" + imageName + ".jpg");
            f.createNewFile();
            byte[] bytes = os.toByteArray();
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bytes);
        } catch (IOException ioe) {
            Log.d("AppUtil.createFile-->", ioe.toString());
        }
        return f;
    }

    /**
     * 获取图片大小
     *
     * @param bitmap
     * @return 返回字节
     */
    public static int getImageSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getRowBytes() * bitmap.getHeight();
        } else {
            return bitmap.getByteCount();
        }
    }

    /**
     * 获取图片旋转的角度
     *
     * @param imagePath
     * @return
     */
    public static int getImageDegree(String imagePath) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(imagePath);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception ex) {
            Log.d("AppUtil.getImageDegree", ex.toString());
        }
        return degree;
    }

    /**
     * 去掉由于Html.fromHtml产生的'\n'
     *
     * @param spanned
     * @return
     */
    public static CharSequence setHtmlContent(Spanned spanned) {
        if (spanned == null) {
            return "";
        }
        if (spanned.length() > 2 && spanned.subSequence(spanned.length() - 2, spanned.length()).toString().equals("\n\n")) {
            return spanned.subSequence(0, spanned.length() - 2);
        }
        return spanned;
    }

    /**
     * 去掉所有<Img>标签
     *
     * @param content
     * @return
     */
    public static String removeImgTagFromString(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        Matcher m = Pattern.compile("(<img src=\".*?\" .>)").matcher(content);
        new StringBuffer().append("1");
        while (m.find()) {
            content = content.replace(m.group(1), "");
        }
        return content;
    }

    /**
     * 去掉字符串中的\n\type
     *
     * @param content
     * @return
     */
    public static String removeHtmlSpace(String content) {
        if (TextUtils.isEmpty(content)) {
            return "";
        }
        Matcher m = Pattern.compile("\\t|\\n").matcher(content);
        while (m.find()) {
            content = content.replace(m.group(0), "");
        }
        return content;
    }

    public static int computeSampleSize(
            BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(
            BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public static int getNumberLength(int number) {
        int length = 1;
        while (number >= 10) {
            length++;
            number = number / 10;
        }

        return length;
    }

    public static SpannableString getColorTextAfter(String text, String newStr, int color) {
        StringBuffer stringBuffer = new StringBuffer(text);
        int start = stringBuffer.length();
        stringBuffer.append(newStr);
        SpannableString spannableString = new SpannableString(stringBuffer);
        spannableString.setSpan(
                new ForegroundColorSpan(color), start, stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static SpannableString getColorTextBefore(String text, String newStr, int color) {
        StringBuffer stringBuffer = new StringBuffer(text);
        int start = stringBuffer.length();
        stringBuffer.append(newStr);
        SpannableString spannableString = new SpannableString(stringBuffer);
        spannableString.setSpan(
                new ForegroundColorSpan(color), 0, start, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static Intent getViewFileIntent(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.setAction(Intent.ACTION_VIEW);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String type = mimeTypeMap.getMimeTypeFromExtension(
                MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath()));

        intent.setDataAndType(Uri.fromFile(file), type);

        return intent;
    }

    /**
     * 获取系统图片路径
     */

    @TargetApi(19)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static int parseInt(String value) {
        int i = 0;
        if (value == null) {
            return i;
        }
        try {
            i = Integer.parseInt(value);
        } catch (Exception e) {
            i = 0;
        }

        return i;
    }

    public static float parseFloat(String value) {
        float i = 0.0f;
        if (value == null) {
            return i;
        }
        try {
            i = Float.parseFloat(value);
        } catch (Exception e) {
            i = 0.0f;
        }

        return i;
    }

    /**
     * 去掉'\n','\type'
     *
     * @return
     */
    public static String filterSpace(String str) {
        return str.replaceAll("\\n|\\t", "");
    }

    /**
     * 旋转图片
     *
     * @param view
     * @param start
     * @param end
     */
    public static void rotation(View view, float start, float end) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotation", start, end);
        objectAnimator.setDuration(180);
        objectAnimator.start();
    }

    /**
     * 格式化容量
     *
     * @param totalSize
     * @return
     */
    public static String formatSize(long totalSize) {
        Log.d(null, "totalSize->" + totalSize);
        float kb = 1024.0f;
        if (totalSize < (kb * kb)) {
            return String.format("%.1f%s", (totalSize / kb), "KB");
        }

        if (totalSize < (kb * kb * kb)) {
            return String.format("%.1f%s", (totalSize / (kb * kb)), "M");
        }

        return String.format("%.1f%s", (totalSize / (kb * kb * kb)), "G");
    }

    public static String timeFormat(int second) {
        int hh = second / 3600;
        int mm = second % 3600 / 60;
        int ss = second % 60;
        String strTemp = "0";
        if (0 != hh) {
            strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
        } else {
            strTemp = String.format("%02d:%02d", mm, ss);
        }

        return strTemp;
    }

    /**
     * 初始化对话框
     */
    public static ProgressDialog initProgressDialog(Context context, String msg) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static String getFileExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos != -1) {
            return fileName.substring(pos);
        }
        return null;
    }

    public static boolean writeRandomFile(
            File file, InputStream stream, String mode, InputUtils.NormalCallback callback) {
        RandomAccessFile o = null;
        try {
            o = new RandomAccessFile(file, "rw");
            o.seek(file.length());
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
                Log.d(null, "writeRandomFile " + o.length());
                callback.success(new long[]{o.length()});
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static File getCacheFileDir() {
        File sdcard = Environment.getExternalStorageDirectory();
        File workSpace = new File(sdcard, "edusoho");
        if (!workSpace.exists()) {
            workSpace.mkdir();
        }
        return workSpace;
    }

    public static void longToast(Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    public static void shortToast(Context context, String title) {
        Toast.makeText(context, title, Toast.LENGTH_SHORT).show();
    }

    //居中弹出toast
    public static void shortCenterToast(Context context, String title) {
        Toast toast = Toast.makeText(context, title, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static class MoveTimerTask extends TimerTask {
        public static int LEFT  = 0001;
        public static int RIGHT = 0002;

        public  int DEF_SIZE;
        public  int step;
        public  int mOffset;
        private int step_def;
        private int type;

        public MoveTimerTask(int offset, int type, int defsize) {
            this.type = type;
            this.step = offset;
            this.mOffset = type == LEFT ? 0 : offset;
            if (defsize == -1) {
                DEF_SIZE = type == LEFT ? 5 : -5;
            } else {
                DEF_SIZE = type == LEFT ? defsize : -defsize;
            }
            step_def = Math.abs(DEF_SIZE);
        }

        public boolean step() {
            if (step > 0 && step < step_def) {
                DEF_SIZE = type == LEFT ? step : -step;
                step = 0;
                return true;
            }
            step -= step_def;
            return step > 0;
        }

        @Override
        public void run() {

        }
    }

    public static boolean isExitsSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static boolean bundleHasKey(Bundle bundle, String key) {
        for (String element : bundle.keySet()) {
            if (element.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T[] concatArray(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
