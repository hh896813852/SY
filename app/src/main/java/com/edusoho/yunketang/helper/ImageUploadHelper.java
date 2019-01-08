package com.edusoho.yunketang.helper;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.SYConstants;
import com.edusoho.yunketang.utils.FileUtils;
import com.edusoho.yunketang.utils.HttpUtil;
import com.edusoho.yunketang.utils.LogUtil;
import com.edusoho.yunketang.utils.ProgressDialogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author huhao on 2018/7/24
 */
public class ImageUploadHelper {
    private static int uriIndex = 0; // 图片uri集合

    /**
     * 上传图片
     */
    public static void uploadImage(File file, OnSingleImageUploadListener singleImageUploadListener) {
        Map<String, File> files = new HashMap<>();
        files.put("file", file);
        HttpUtil.sendMultipart(SYConstants.PIC_UPLOAD, null, files)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ProgressDialogUtil.hideProgress();
                        singleImageUploadListener.singleImageUploadFailed("上传失败！");
                        LogUtil.i("imageUploadError", "throwable:" + throwable.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("code") == 200) {
                                String dataString = jsonObject.getString("data");
                                if (!TextUtils.isEmpty(dataString)) {
                                    JSONObject jsonObject2 = new JSONObject(dataString);
                                    if (jsonObject2.getInt("code") == 1) {
                                        String url = (String) new JSONObject(jsonObject2.getString("data")).get("url");
                                        if (singleImageUploadListener != null) {
                                            singleImageUploadListener.singleImageUploadSuccess(url);
                                        }
                                    } else {
                                        ToastHelper.showSingleToast(SYApplication.getInstance(), jsonObject2.getString("msg"));
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            LogUtil.i("imageUploadError", "数据解析异常");
                        }
                    }
                });
    }

    private static StringBuilder Urls;
    private static List<String> urlList;

    /**
     * 同步上传多张图片
     */
    public static void uploadImage(Context context, Uri uri, List<Uri> uriList, OnMultiImageUploadListener multiImageUploadListener) {
        if (Urls == null) {
            Urls = new StringBuilder();
        }
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        uploadImage(FileUtils.getFileByUri(context, uri), new OnSingleImageUploadListener() {
            @Override
            public void singleImageUploadSuccess(String url) {
                uriIndex++;
                urlList.add(url);
                if (uriIndex == uriList.size() && multiImageUploadListener != null) { // 上传完毕
                    Urls.append(url);
                    multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                    uriIndex = 0;
                    Urls = null;
                    urlList = null;
                } else { // 还有图片未上传，则继续上传下一张
                    Urls.append(url).append(",");
                    uploadImage(context, uriList.get(uriIndex), uriList, multiImageUploadListener);
                }
            }

            @Override
            public void singleImageUploadFailed(String msg) {
                uriIndex++;
                if (uriIndex == uriList.size() && multiImageUploadListener != null) { // 上传完毕
                    multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                    uriIndex = 0;
                    Urls = null;
                    urlList = null;
                } else { // 还有图片未上传，则继续上传下一张
                    uploadImage(context, uriList.get(uriIndex), uriList, multiImageUploadListener);
                }
            }
        });
    }

    /**
     * 同步上传多张图片
     */
    public static void uploadImage(String path, List<String> pathList, OnMultiImageUploadListener multiImageUploadListener) {
        if (Urls == null) {
            Urls = new StringBuilder();
        }
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        uploadImage(new File(path), new OnSingleImageUploadListener() {
            @Override
            public void singleImageUploadSuccess(String url) {
                uriIndex++;
                urlList.add(url);
                if (uriIndex == pathList.size() && multiImageUploadListener != null) { // 上传完毕
                    Urls.append(url);
                    multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                    uriIndex = 0;
                    Urls = null;
                    urlList = null;
                } else { // 还有图片未上传，则继续上传下一张
                    Urls.append(url).append(",");
                    uploadImage(pathList.get(uriIndex), pathList, multiImageUploadListener);
                }
            }

            @Override
            public void singleImageUploadFailed(String msg) {
                uriIndex++;
                if (uriIndex == pathList.size() && multiImageUploadListener != null) { // 上传完毕
                    multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                    uriIndex = 0;
                    Urls = null;
                    urlList = null;
                } else { // 还有图片未上传，则继续上传下一张
                    uploadImage(pathList.get(uriIndex), pathList, multiImageUploadListener);
                }
            }
        });
    }

    /**
     * 同步上传多张图片
     */
    public static void uploadImage(File file, List<File> FileList, OnMultiImageUploadListener multiImageUploadListener) {
        if (Urls == null) {
            Urls = new StringBuilder();
        }
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        uploadImage(file, new OnSingleImageUploadListener() {
            @Override
            public void singleImageUploadSuccess(String url) {
                uriIndex++;
                urlList.add(url);
                if (uriIndex == FileList.size() && multiImageUploadListener != null) { // 上传完毕
                    Urls.append(url);
                    multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                    uriIndex = 0;
                    Urls = null;
                    urlList = null;
                } else { // 还有图片未上传，则继续上传下一张
                    Urls.append(url).append(",");
                    uploadImage(FileList.get(uriIndex), FileList, multiImageUploadListener);
                }
            }

            @Override
            public void singleImageUploadFailed(String msg) {
                uriIndex++;
                if (uriIndex == FileList.size() && multiImageUploadListener != null) { // 上传完毕
                    multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                    uriIndex = 0;
                    Urls = null;
                    urlList = null;
                } else { // 还有图片未上传，则继续上传下一张
                    uploadImage(FileList.get(uriIndex), FileList, multiImageUploadListener);
                }
            }
        });
    }

    /**
     * 异步上传多张图片
     */
    public static void uploadImageAnsy(List<File> FileList, OnMultiImageUploadListener multiImageUploadListener) {
        if (Urls == null) {
            Urls = new StringBuilder();
        }
        if (urlList == null) {
            urlList = new ArrayList<>();
        }
        for (File file : FileList) {
            uploadImage(file, new OnSingleImageUploadListener() {
                @Override
                public void singleImageUploadSuccess(String url) {
                    uriIndex++;
                    urlList.add(url);
                    if (uriIndex == FileList.size() && multiImageUploadListener != null) { // 上传完毕
                        Urls.append(url);
                        multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                        uriIndex = 0;
                        Urls = null;
                        urlList = null;
                    } else { // 未上传完毕
                        Urls.append(url).append(",");
                    }
                }

                @Override
                public void singleImageUploadFailed(String msg) {
                    uriIndex++;
                    if (uriIndex == FileList.size() && multiImageUploadListener != null) { // 上传完毕
                        multiImageUploadListener.OnMultiImageUploadSuccess(Urls.toString(), urlList);
                        uriIndex = 0;
                        Urls = null;
                        urlList = null;
                    }
                }
            });
        }
    }

    public interface OnSingleImageUploadListener {
        void singleImageUploadSuccess(String url);

        void singleImageUploadFailed(String msg);
    }

    public interface OnMultiImageUploadListener {
        void OnMultiImageUploadSuccess(String urls, List<String> urlList);
    }
}
