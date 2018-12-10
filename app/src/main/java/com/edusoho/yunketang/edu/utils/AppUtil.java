package com.edusoho.yunketang.edu.utils;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.edusoho.yunketang.SYApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by JesseHuang on 15/4/26.
 */
public class AppUtil {

    public static final String TAG      = "AppUtil";
    private static      String FLAVOR   = "edusoho";
    public static final long   ONE_WEEK = 1000 * 60 * 60 * 24 * 7;

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

    public static long parseLong(String value) {
        long i = 0;
        if (value == null) {
            return i;
        }
        try {
            i = Long.parseLong(value);
        } catch (Exception e) {
            i = 0;
        }

        return i;
    }

    public static void initConfig(String[] config) {
        if (config == null || config.length == 0) {
            return;
        }
        FLAVOR = config[0];
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

    public static Bitmap getBitmapFromFile(File file) {
        return getBitmapFromFile(file, (int) (SYApplication.getInstance().screenW * 0.5f));
    }

    public static Bitmap getBitmapFromFile(File file, int width) {
        Bitmap bitmap;
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), option);
        option.inSampleSize = computeSampleSize(option, -1, width * width);
        option.inJustDecodeBounds = false;
        try {
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), option);
        } catch (Exception e) {
            bitmap = null;
        }

        return bitmap;
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

    /**
     * 获取系统图片路径
     */

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

    public static SpannableString getColorTextAfter(String text, String newStr, int color) {
        StringBuffer stringBuffer = new StringBuffer(text);
        int start = stringBuffer.length();
        stringBuffer.append(newStr);
        SpannableString spannableString = new SpannableString(stringBuffer);
        spannableString.setSpan(
                new ForegroundColorSpan(color), start, stringBuffer.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public static Bitmap compressImage(Bitmap image, ByteArrayOutputStream bos) {
        Bitmap bitmap = null;
        boolean isOutStream = false;
        ByteArrayInputStream isBm = null;
        try {
            int options = 100;
            if (bos == null) {
                isOutStream = true;
                bos = new ByteArrayOutputStream();
            }
            image.compress(Bitmap.CompressFormat.JPEG, options, bos);
            while (bos.toByteArray().length / 1024 > 200) {
                options -= 2;
                bos.reset();
                image.compress(Bitmap.CompressFormat.JPEG, options, bos);
            }
            isBm = new ByteArrayInputStream(bos.toByteArray());
            bitmap = BitmapFactory.decodeStream(isBm, null, null);
        } catch (Exception e) {
            Log.e("compressImage", e.getMessage());
        } finally {
            try {
                if (isBm != null) {
                    isBm.close();
                }
                if (isOutStream && bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
            }
        }
        return bitmap;
    }


    /**
     * 压缩图片并保存
     *
     * @param image    图片
     * @param filePath 保存全路径
     * @param size     压缩指定大小
     */
    public static void saveCompress(Bitmap image, String filePath, long size) {
        int ratio = getRatioSize(image.getWidth(), image.getHeight());
        Bitmap result = Bitmap.createBitmap(image.getWidth() / ratio,
                image.getHeight() / ratio, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, image.getWidth() / ratio, image.getHeight() / ratio);
        canvas.drawBitmap(image, null, rect, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;
        result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > size) {
            baos.reset();
            options -= 10;
            result.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        saveBitmap(result, filePath, options);
        if (!result.isRecycled()) {
            result.recycle();
            result = null;
        }
    }

    private static int getRatioSize(int bitWidth, int bitHeight) {
        int imageMaxWidth = 1280;
        int imageMaxHeight = 960;
        int ratio = 1;
        if (bitWidth > bitHeight && bitWidth > imageMaxWidth) {
            ratio = bitWidth / imageMaxWidth;
        } else if (bitWidth < bitHeight && bitHeight > imageMaxHeight) {
            ratio = bitHeight / imageMaxHeight;
        }
        if (ratio < 1) {
            ratio = 1;
        }
        return ratio;
    }

    private static void saveBitmap(Bitmap bitmap, String path, int quality) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * bitmap转File
     *
     * @param bitmap
     * @param path   file位置
     * @return
     */
    public static File convertBitmap2File(Bitmap bitmap, String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        byte[] bitmapData = bos.toByteArray();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bitmapData);
        fos.flush();
        fos.close();
        return file;
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
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
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

    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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

    public static ProgressDialog initProgressDialog(Context context, String msg) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage(msg);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCancelable(false);
        return mProgressDialog;
    }

    public static boolean isNetConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    public static boolean isWiFiConnect(Context context) {
        ConnectivityManager connManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static boolean saveStreamToFile(
            InputStream inputStream, File file, boolean inClose) {
        byte[] buffer = new byte[1024];
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            int length = -1;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inClose) {
                    inputStream.close();
                }
                outputStream.close();
            } catch (Exception e) {
                //nothing
            }
        }

        return false;
    }

    public static File getAppStorage() {
        File store = getSystemStorage();
        File appStorage = new File(store, "edusoho");
        if (!appStorage.exists()) {
            appStorage.mkdirs();
        }

        return appStorage;
    }

    public static File getAppInstallStorage() {
        File store = getAppStorage();
        File installStore = new File(store, "install");
        if (!installStore.exists()) {
            installStore.mkdirs();
        }

        return installStore;
    }

    public static File getImageStorage() {
        File imageStore = new File(AppUtil.getAppStorage(), Const.UPLOAD_IMAGE_CACHE_FILE);
        if (!imageStore.exists()) {
            imageStore.mkdirs();
        }

        return imageStore;
    }

    public static File getThumbImageStorage() {
        File imageStore = new File(AppUtil.getAppStorage(), Const.UPLOAD_IMAGE_CACHE_THUMB_FILE);
        if (!imageStore.exists()) {
            imageStore.mkdirs();
        }

        return imageStore;
    }

    public static File getHtmlPluginStorage(Context context, String domain) {
        File html5plugin = context.getDir("html5plugin", Context.MODE_PRIVATE);
        File schoolStorage = new File(html5plugin, domain);
        if (!schoolStorage.exists()) {
            schoolStorage.mkdirs();
        }

        return schoolStorage;
    }

    public static File getX3HtmlPluginStorage(Context context, String domain) {
        File html5plugin = context.getDir("x3-html5plugin", Context.MODE_PRIVATE);
        File schoolStorage = new File(html5plugin, domain);
        if (!schoolStorage.exists()) {
            schoolStorage.mkdirs();
        }

        return schoolStorage;
    }

    public static File getSchoolStorage(String host) {
        File store = getAppStorage();
        File schoolStorage = new File(store, host);
        if (!schoolStorage.exists()) {
            schoolStorage.mkdirs();
        }

        return schoolStorage;
    }

    public static File getAppCacheDir() {
        File store = getAppStorage();
        File cacheDir = new File(store, "cache");
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        return cacheDir;
    }


    public static File getAppZipStorage(Context context) {
        File storage = context.getDir("html5plugin", Context.MODE_PRIVATE);
        File srcDir = new File(storage, "appZip");
        if (!srcDir.exists()) {
            srcDir.mkdirs();
        }

        return srcDir;
    }

    public static File getSystemStorage() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getDataDirectory();
        }
        return Environment.getExternalStorageDirectory();
    }

    public static boolean unZipFile(File outFile, InputStream zinInputStream) {
        try {
            ZipInputStream zin = new ZipInputStream(zinInputStream);
            for (ZipEntry e; (e = zin.getNextEntry()) != null; zin.closeEntry()) {
                File file = new File(outFile, e.getName());
                if (e.isDirectory()) {
                    file.mkdirs();
                    continue;
                }

                if (!isFileDirExists(file)) {
                    continue;
                }
                if (AppUtil.saveStreamToFile(zin, file, false)) {
                    Log.d(TAG, String.format("file %s is unzip", e.getName()));
                }
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                zinInputStream.close();
            } catch (Exception e) {
            }
        }
    }

    public static boolean unZipFile(File outFile, File zinFile) {
        if (!outFile.exists()) {
            outFile.mkdir();
        }

        try {
            return unZipFile(outFile, new FileInputStream(zinFile));
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isFileDirExists(File file) {
        if (!file.getParentFile().exists()) {
            return file.getParentFile().mkdirs();
        }

        return true;
    }

    /**
     * Used to build output as Hex
     */
    private static final char[] DIGITS_LOWER = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * encode By MD5
     *
     * @param str
     * @return String
     */
    public static String md5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            return new String(encodeHex(messageDigest.digest()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts an array of bytes into an array of characters representing the hexadecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     */
    private static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // two characters form the hex value.
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS_LOWER[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & data[i]];
        }
        return out;
    }

    public static String encode2(String str) {
        char[] a = str.toCharArray();
        for (int i = 0; i < a.length; i++) {
            a[i] = (char) (a[i] ^ 0x2ff);
        }
        return new String(a);
    }

    public static long convertMilliSec(int time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long returnTime = 0;
        try {
            String tDate = String.valueOf(time).split("[+]")[0].replace('T', ' ');
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
    public static String convertWeekTime(int t) {
        String result = "";
        try {
            String tDate = String.valueOf(t).split("[+]")[0].replace('T', ' ');
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

    public static String convertMills2Date(long millis) {
        String result = "";
        if (millis <= 0) {
            return "";
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yy/MM/dd HH:mm");
            String nowTime = sdf.format(System.currentTimeMillis());
            String showTime = sdf.format(millis);
            if (nowTime.substring(0, 8).equals(showTime.substring(0, 8))) {
                // 如果是当天
                result = showTime.substring(9);
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
                result = showTime.substring(0, 8);
            }
        } catch (Exception ex) {
            Log.e("convertMills2Date", ex.getMessage());
        }
        return result;
    }

    public static String convertTimeZone2Time(String timeZone) {
        String time = "";
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tDate = timeZone.split("[+]")[0].replace('T', ' ');
            time = convertMills2Date(sdf.parse(tDate).getTime());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return time;
        }
    }

    public static String converMillisecond2TimeZone(long millisecond) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        String time = sdf.format(millisecond);
        time = time.replace(' ', 'T').substring(0, time.length() - 2)
                + ":00";
        return time;
    }

    public static long convertUTCTimeToMilliSecond(String timeZone) {
        long time = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String tDate = timeZone.split("[+]")[0].replace('T', ' ');
            time = sdf.parse(tDate).getTime() / 1000;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return time;
        }
    }

    public static long convertTimeZone2Millisecond(String timeZone) {
        long time = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String tDate = timeZone.split("[+]")[0].replace('T', ' ');
            time = sdf.parse(tDate).getTime() / 1000;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return time;
        }
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
     * 计算发布问题天数,服务端获取时间格式：2014-05-20T22:03:43+00:00
     * 转换为天数或者小时
     */
    public static String getPostDaysZero(String postTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long l = 1;
        try {
            String tDate = postTime.split("[+]")[0].replace('T', ' ');
            long milliSec = 1000;
            Date date = new Date();
            l = (date.getTime() - sdf.parse(tDate).getTime()) / (milliSec) - (8 * 60 * 60);

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

    public static ObjectAnimator animForHeight(Object view, int start, int end, int time) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(
                view, "height", start, end);
        objectAnimator.setDuration(time);
        objectAnimator.setInterpolator(new AccelerateInterpolator());
        objectAnimator.start();
        return objectAnimator;
    }

    public static String convertCNTime(String time) {
        String[] times = time.split(":");
        String hour = "";
        String min = times[0];
        String sec = times[1];

        if (Integer.valueOf(min) / 60 > 1) {
            hour = Integer.valueOf(min) / 60 + "";
        } else {
            if (min.length() > 1 && min.substring(0, 1).equals("0")) {
                min = min.substring(1, 2);
            }
        }

        if (hour.length() > 0) {
            return String.format("%s小时%s分%s秒", hour, min, sec);
        } else {
            return String.format("%s分%s秒", min, sec);
        }
    }

    public static String coverCourseAbout(String about) {
        if (TextUtils.isEmpty(about)) {
            about = "";
        }
        return about.replaceAll("<[^>]+>", "");
    }

    public static boolean saveBitmap2FileWithQuality(Bitmap bmp, String filename, int quality) {
        Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
            bmp.compress(format, quality, stream);
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (Exception e) {

                }
            }
        }
        return true;
    }

    public static boolean saveBitmap2File(Bitmap bmp, String filename) {
        return saveBitmap2FileWithQuality(bmp, filename, 100);
    }

    /**
     * 图片旋转
     *
     * @param bitmap    图片
     * @param imageSize 图片大小
     * @param degree    图片旋转的角度，如果没有旋转，则为0
     * @return
     */
    public static Bitmap scaleImage(Bitmap bitmap, float imageSize, int degree) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        //float density = context.getResources().getDisplayMetrics().density;
        int bounding = Math.round(imageSize);

        float xScale = ((float) bounding) / width;
        float yScale = ((float) bounding) / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        if (scale <= 0) {
            scale = 1;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        matrix.postRotate((float) degree);

        return Bitmap.createBitmap(bitmap, 0, 0, width > 0 ? width : 100, height > 0 ? height : 100, matrix, true);
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

    public static int searchInArray(int[] array, int search) {

        if (array == null || array.length == 0) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (search == array[i]) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 图片压缩
     *
     * @param filePath Image Path
     * @return
     */
    public static Bitmap CompressImage(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int requireWidth = 480;
        int requireHeight = 800;
        final int originalWidth = options.outWidth;
        final int originalHeight = options.outHeight;
        options.inSampleSize = 1;
        if (originalWidth > requireWidth || originalHeight > requireHeight) {
            final int widthRatio = Math.round((float) originalWidth / (float) requireHeight);
            final int heightRatio = Math.round((float) originalHeight / (float) requireWidth);
            options.inSampleSize = widthRatio < heightRatio ? widthRatio : heightRatio;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * by suju
     * context 缓存数组
     */
    static WeakHashMap<Integer, Context> contextMap = new WeakHashMap<>();

    public static Context contextAddAssets(Context context) {
        if (contextMap.containsKey(context.hashCode())) {
            return context;
        }

        AssetManager assetManager = context.getAssets();
        String zipName = String.format("assets-%s.zip", getApkVersion(context));
        addZipToAssets(assetManager, new File(context.getFilesDir(), zipName));
        contextMap.put(context.hashCode(), context);
        return context;
    }

    public static void addZipToAssets(AssetManager assetManager, File file) {
        if (!file.exists()) {
            return;
        }
        try {
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getApkVersion(Context context) {
        String version = "1.0.0";
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = packageInfo.versionName;
        } catch (Exception e) {

        }
        return version;
    }


    /**
     * 去除string中的HTML标签
     *
     * @param string
     * @return
     */
    public static String removeHtmlSpan(String string) {
        String htmlRegx = "<[^>]+>";
        Pattern htmlPattern = Pattern.compile(htmlRegx, Pattern.CASE_INSENSITIVE);
        Matcher htmlMatcher = htmlPattern.matcher(string);
        string = htmlMatcher.replaceAll("");
        return string;
    }

    /**
     * 时间戳转日期
     *
     * @param timeStampStr
     * @param format
     * @return
     */
    public static String timeStampToDate(String timeStampStr, String format) {
        String date;
        long unixLong = Long.parseLong(timeStampStr) * 1000;
        if (format == null) {
            format = "yy-MM-dd  HH:mm";
        }
        date = new SimpleDateFormat(format).format(unixLong);
        return date;
    }

    public static String timeStampDiffToDay(int timeStampDiff) {

        if (timeStampDiff > 0) {
            int second = timeStampDiff % 60;
            int totalMin = timeStampDiff / 60;
            if (totalMin > 0) {
                int min = totalMin % 60;
                int totalHour = totalMin / 60;
                if (totalHour > 0) {
                    int hour = totalHour % 24;
                    int totalDay = totalHour / 24;
                    if (totalDay > 0) {
                        int day = totalDay;
                        return String.format("%d天%d小时%d分%d秒", day, hour, min, second);
                    } else {
                        return String.format("%d小时%d分%d秒", hour, min, second);
                    }
                } else {
                    return String.format("%d分%d秒", min, second);
                }
            } else {
                return String.format("%d秒", second);
            }
        } else {
            return "0秒";
        }
    }

    public static String cutString(String str, int length) {
        String string = str;
        if (string.length() <= length) {
            return string;
        } else {
            return string.substring(0, length) + "...";
        }
    }

    public static void setSoftKeyBoard(EditText editText, Activity activity, int status) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getApplicationContext().
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (Const.SHOW_KEYBOARD == status) {
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        } else if (Const.HIDE_KEYBOARD == status) {
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static int getWidthPx(Context context) {
        int result;
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        result = outMetrics.widthPixels;
        return result;
    }

    public static int getHeightPx(Context context) {
        int result;
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        result = outMetrics.heightPixels;
        return result;
    }

    public static int getUnrealScreenHeightPx(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels - getStatusBarHeight(context);
    }

    // 获取手机状态栏高度
    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }

    public static Uri getUriById(Context context, int res) {
        try {
            Context packageContext = context.createPackageContext(context.getPackageName(),
                    Context.CONTEXT_RESTRICTED);
            Resources resources = packageContext.getResources();
            String appPkg = packageContext.getPackageName();
            String resPkg = resources.getResourcePackageName(res);
            String type = resources.getResourceTypeName(res);
            String name = resources.getResourceEntryName(res);


            Uri.Builder uriBuilder = new Uri.Builder();
            uriBuilder.scheme(ContentResolver.SCHEME_ANDROID_RESOURCE);
            uriBuilder.encodedAuthority(appPkg);
            uriBuilder.appendEncodedPath(type);
            if (!appPkg.equals(resPkg)) {
                uriBuilder.appendEncodedPath(resPkg + ":" + name);
            } else {
                uriBuilder.appendEncodedPath(name);
            }
            return uriBuilder.build();

        } catch (Exception e) {
            return null;
        }
    }
}
