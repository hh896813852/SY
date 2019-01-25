package com.edusoho.yunketang.helper;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.base.animation.FlipEnter.FlipTopEnter;
import com.edusoho.yunketang.base.animation.FlipExit.FlipVerticalExit;
import com.edusoho.yunketang.bean.Version;
import com.edusoho.yunketang.http.SYDataListener;
import com.edusoho.yunketang.http.SYDataTransport;
import com.edusoho.yunketang.utils.AppUtil;
import com.edusoho.yunketang.utils.DialogUtil;
import com.edusoho.yunketang.widget.dialog.SimpleDialog;
import com.edusoho.yunketang.widget.dialog.UpdateDialog;

import java.io.File;

/**
 * @author huhao on 19/1/24.
 */
public class UpdateHelper {
    public static void checkUpdate(Context context, boolean needNotice, boolean needProgressDialog) {
        int appVersionCode = AppUtil.getAppVersionCode(context);
        SYDataTransport service = SYDataTransport.create(SYConstants.CHECK_VERSION);
//        service.addParam("type", 1); // 1-Android 2-ios
        if (needProgressDialog) {
            service.addProgressing(context, "检查中...");
        }
        service.execute(new SYDataListener<Version>() {
            @Override
            public void onSuccess(Version version) {
                if (version != null) {
                    String downloadUrl = version.fileUrl;
                    int latestVersion = version.versionCode;
                    if (latestVersion <= appVersionCode) {
                        if (needNotice) {
                            ToastHelper.showSingleToast(context, "当前已是最新版本");
                        }
                    } else {
                        String apkName = "sy_edu_update_" + AppUtil.getAppVersionCode(context) + ".apk";
                        DialogUtil.showSimpleAnimDialog(context, "有新版本可以更新哦！", "取消", "更新", new FlipTopEnter(), new FlipVerticalExit(), new SimpleDialog.OnSimpleClickListener() {
                            @Override
                            public void OnLeftBtnClicked(SimpleDialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void OnRightBtnClicked(SimpleDialog dialog) {
                                long downloadId = downloadApk(context, downloadUrl, apkName);
                                AppPreferences.setAppUpdateId(downloadId);
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFail(int status, String failMessage) {
                if (needNotice) {
                    super.onFail(status, failMessage);
                }
            }
        }, Version.class);
    }

    /**
     * 首页检测更新
     *
     * @param context
     */
    public static void checkUpdateIndex(Context context) {
        int appVersionCode = AppUtil.getAppVersionCode(context);
        SYDataTransport service = SYDataTransport.create(SYConstants.CHECK_VERSION);
        service.execute(new SYDataListener<Version>() {
            @Override
            public void onSuccess(Version version) {
                if (version != null) {
                    String downloadUrl = version.fileUrl;
                    int latestVersion = version.versionCode;
                    if (latestVersion > appVersionCode) {
                        String apkName = "sy_edu_update_" + AppUtil.getAppVersionCode(context) + ".apk";
                        DialogUtil.showUpdateDialog(context, version.force,version.versionName, version.content, new UpdateDialog.OnClickListener() {

                            @Override
                            public void OnNotUpdateClicked(UpdateDialog dialog) {
                                dialog.dismiss();
                            }

                            @Override
                            public void OnUpdateClicked(UpdateDialog dialog) {
                                long downloadId = downloadApk(context, downloadUrl, apkName);
                                AppPreferences.setAppUpdateId(downloadId);
                                dialog.dismiss();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFail(int status, String failMessage) {

            }
        }, Version.class);
    }

    public static long downloadApk(Context context, String apkUrl, String apkName) {
        try {
            File file = new File(Environment.getExternalStorageDirectory() + "/Download");
            if (file != null && !file.exists()) {
                file.mkdirs();
            }
        } catch (Throwable e) {
        }
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(apkUrl));
//        req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        //req.setAllowedOverRoaming(false);

        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //设置文件的保存的位置[三种方式]
        //第一种
        //file:///storage/emulated/0/Android/data/your-package/files/Download/update.apk
//        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, apkName);
//        req.setDestinationInExternalFilesDir(context, Environment.getExternalStorageDirectory() + "/Download", apkName);
        //第二种
        //file:///storage/emulated/0/Download/update.apk
        req.setDestinationInExternalPublicDir("Download", apkName);
        //第三种 自定义文件路径
        //req.setDestinationUri()
        // 设置一些基本显示信息
        req.setTitle("上元在线");
        req.setDescription("正在下载上元在线APP");
        req.setMimeType("application/vnd.android.package-archive");
        //加入下载队列
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return dm.enqueue(req);
    }
}
