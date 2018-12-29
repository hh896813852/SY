package com.edusoho.yunketang.edu.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.utils.AppUtil;
import com.edusoho.yunketang.edu.webview.bridgeadapter.AbstractJsBridgeAdapterWebView;
import com.edusoho.yunketang.edu.webview.bridgeadapter.BridgeWebChromeClient;
import com.edusoho.yunketang.edu.webview.bridgeadapter.bridge.BridgePluginContext;
import com.edusoho.yunketang.edu.widget.PopupDialog;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.io.File;

/**
 * Created by howzhi on 15/8/10.
 */
public class ESWebChromeClient extends BridgeWebChromeClient {

    public static final int FILECHOOSER_RESULTCODE = 0x10;

    public ESWebChromeClient(AbstractJsBridgeAdapterWebView webView) {
        super(webView);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        this.openFileChooser(uploadMsg, "*/*");
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        this.openFileChooser(uploadMsg, acceptType, null);
    }

    public void openFileChooser(final ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(acceptType);

        mWebview.getActitiy().startActivityForResult(i, 0);

        mWebview.getBridgePluginContext().startActivityForResult(
                new BridgePluginContext.Callback() {
                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        Uri result = data == null || resultCode != Activity.RESULT_OK ? null : data.getData();
                        uploadMsg.onReceiveValue(compressImage(mActivity.getApplicationContext(), result));
                    }
                },
                Intent.createChooser(i, "File Browser"),
                0
        );
    }

    public static Uri compressImage(Context context, Uri uri) {
        if (uri == null || TextUtils.isEmpty(uri.getPath())) {
            return null;
        }
        String path = AppUtil.getPath(context, uri);
        Bitmap bitmap = AppUtil.getBitmapFromFile(new File(path));
        if (bitmap == null) {
            return uri;
        }
        bitmap = AppUtil.scaleImage(bitmap, SYApplication.getInstance().screenW, 0);

        File cacheDir = AppUtil.getAppCacheDir();
        File newUriFile = new File(cacheDir, "uploadAvatarTemp.png");
        AppUtil.saveBitmap2FileWithQuality(bitmap, newUriFile.getAbsolutePath(), 80);

        return Uri.fromFile(newUriFile);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        PopupDialog.createNormal(mActivity, "提示:", message).show();
        result.cancel();
        return true;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        mActivity.setTitle(title);
    }
}
