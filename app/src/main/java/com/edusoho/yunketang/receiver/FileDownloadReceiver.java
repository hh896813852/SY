package com.edusoho.yunketang.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import com.edusoho.yunketang.helper.AppPreferences;
import com.edusoho.yunketang.helper.ToastHelper;
import com.edusoho.yunketang.utils.AppUtil;

import java.io.File;

/**
 * @author huhao on 2018/8/1.
 * DownloadManager下载，广播监听
 */
public class FileDownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        String action = intent.getAction();
        if (action.equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downLoadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1L);
            long appUpdateId = AppPreferences.getAppUpdateId();
            if (downLoadId == appUpdateId) { // 提示用户是否安装
                Uri downloadFileUri = manager.getUriForDownloadedFile(downLoadId);
                if (downloadFileUri != null) {
                    String path = Environment.getExternalStorageDirectory() + "/Download/sy_edu_update_" + AppUtil.getAppVersionCode(context) + ".apk";
                    AppUtil.installApk(context, new File(path));
                } else {
                    ToastHelper.showToast(context, "版本更新失败");
                }
            }
        } else if (action.equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {

        }
    }
}
