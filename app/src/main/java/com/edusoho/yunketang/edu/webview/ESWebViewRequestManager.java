package com.edusoho.yunketang.edu.webview;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.edusoho.yunketang.SYApplication;
import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.edu.bean.htmlapp.AppMeta;
import com.edusoho.yunketang.edu.bean.htmlapp.UpdateAppMeta;
import com.edusoho.yunketang.edu.request.RequestCallback;
import com.edusoho.yunketang.edu.request.RequestHandler;
import com.edusoho.yunketang.edu.request.RequestManager;
import com.edusoho.yunketang.edu.request.model.Request;
import com.edusoho.yunketang.edu.request.model.ResourceResponse;
import com.edusoho.yunketang.edu.request.model.Response;
import com.edusoho.yunketang.edu.utils.AppUtil;
import com.edusoho.yunketang.edu.utils.CommonUtil;
import com.edusoho.yunketang.edu.utils.Const;
import com.edusoho.yunketang.edu.utils.FileUtils;
import com.edusoho.yunketang.edu.utils.Promise;
import com.edusoho.yunketang.edu.utils.PromiseCallback;
import com.edusoho.yunketang.edu.utils.VolleySingleton;
import com.edusoho.yunketang.utils.volley.BaseVolleyRequest;
import com.edusoho.yunketang.utils.volley.StringVolleyRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;

/**
 * Created by howzhi on 15/4/29.
 */
public class ESWebViewRequestManager extends RequestManager {

    private static final String TAG = "ESWebViewRequestManager";
    private        Context                         mContext;
    private        String                          mUserAgent;
    private        String                          mCode;
    private        HttpClient                      mHttpClient;
    private static HashMap<String, RequestManager> mManagerMap;

    static {
        mManagerMap = new HashMap<>(5);
    }

    public static void clear() {
        for (RequestManager manager : mManagerMap.values()) {
            manager.destroy();
        }
        mManagerMap.clear();
    }

    public static RequestManager getRequestManager(Context context, String code) {
        RequestManager instance = mManagerMap.get(code);
        if (instance == null) {
            instance = new ESWebViewRequestManager(context, code);
            mManagerMap.put(code, instance);
        }

        return instance;
    }

    private ESWebViewRequestManager(Context context, String code) {
        super();
        this.mContext = context;
        this.mCode = code;
        this.mUserAgent = String.format("%s%s%s", Build.MODEL, " Android-kuozhi ", Build.VERSION.SDK);
        initHttpClient();
        registHandler(".+/mapi_v2/.+", new ApiRequestHandler());
        registHandler(".+", new WebViewRequestHandler());
    }

    private void initHttpClient() {
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 50);
        //超时
        HttpConnectionParams.setConnectionTimeout(params, 3000);
        HttpConnectionParams.setSoTimeout(params, 3000);
        SchemeRegistry schReg = new SchemeRegistry();
        schReg.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        mHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, schReg), params);
    }

    public void setUserAgent(String userAgent) {
        this.mUserAgent = userAgent;
    }

    private HttpGet getHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("User-Agent", mUserAgent);

        return httpGet;
    }

    @Override
    public void updateApp(RequestUrl requestUrl, final RequestCallback<Boolean> callback) {
        VolleySingleton volley = VolleySingleton.getInstance(mContext);
        volley.getRequestQueue();
        StringVolleyRequest stringRequest = new StringVolleyRequest(
                Method.GET, requestUrl, new Listener<String>() {
            @Override
            public void onResponse(String response) {
                handlerUpdateApp(response).then(new PromiseCallback<Boolean>() {
                    @Override
                    public Promise invoke(Boolean success) {
                        if (callback != null) {
                            callback.onResponse(new Response<Boolean>(success));
                        }
                        return null;
                    }
                });
            }
        }, null);
        stringRequest.setTag(requestUrl.url);
        volley.addToRequestQueue(stringRequest);
    }

    private Promise handlerUpdateApp(String appVersion) {
        UpdateAppMeta appMeta = null;
        try {
            appMeta = new Gson().fromJson(appVersion, UpdateAppMeta.class);
        } catch (JsonSyntaxException e) {
        }

        AppMeta localAppMeta = getLocalApp(mCode);
        Promise promise = new Promise();
        if (appMeta == null) {
            return promise;
        }

        if (localAppMeta == null
                || Const.LOW_VERSIO == CommonUtil.compareVersion(localAppMeta.version, appMeta.version)) {
            promise = updateAppResource(appMeta.resource);
        }

        return promise;
    }

    private Promise updateAppResource(String resourceUrl) {
        final Promise promise = new Promise();
        downloadResource(new Request(resourceUrl), new RequestCallback<Boolean>() {
            @Override
            public Boolean onResponse(Response<Boolean> response) {
                promise.resolve(response.getData());
                return null;
            }
        });

        return promise;
    }

    @Override
    public void destroy() {
        super.destroy();
        mHttpClient.getConnectionManager().shutdown();
    }

    @Override
    public <T> T blocPost(Request request, RequestCallback<T> callback) {
        return null;
    }

    @Override
    public <T> T blockGet(Request request, RequestCallback<T> callback) {
        Response<T> response = new Response<>();
        handleRequest(request, response);

        return callback.onResponse(response);
    }

    private String getFileExtension(String filePath) {
        return MimeTypeMap.getFileExtensionFromUrl(filePath);
    }

    private String getFileMime(String extension) {
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        return mimeType == null ? "text/html" : mimeType;
    }

    private File getResourceStorage(String host) {
        File storage = AppUtil.getHtmlPluginStorage(mContext, host);
        File srcDir = new File(storage, mCode);
        if (!srcDir.exists()) {
            srcDir.mkdirs();
        }

        return srcDir;
    }

    private File saveFile(File storage, String name, String url) {
        File cache = new File(storage, name + "_temp");
        HttpGet httpGet = getHttpGet(url);
        try {
            if (cache.exists()) {
                httpGet.setHeader("Range", "bytes=" + cache.length());
            }

            HttpResponse response = mHttpClient.execute(httpGet);
            if (AppUtil.saveStreamToFile(response.getEntity().getContent(), cache, true)) {
                File realFile = new File(cache.getAbsolutePath().replace("_temp", ""));
                if (cache.renameTo(realFile)) {
                    return realFile;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpGet.abort();
        }

        return null;
    }

    public AppMeta getLocalApp(String appCode) {
        File schoolStorage = AppUtil.getHtmlPluginStorage(mContext, SYApplication.getInstance().domain);
        File appDir = new File(schoolStorage, appCode);

        if (appDir.exists()) {
            StringBuilder appVersionString = FileUtils.readFile(
                    new File(appDir, "version.json").getAbsolutePath(), "utf-8");
            if (appVersionString == null) {
                return null;
            }
            try {
                return new Gson().fromJson(appVersionString.toString(), AppMeta.class);
            } catch (Exception e) {
            }
        }

        return null;
    }

    @Override
    public void downloadResource(final Request request, final RequestCallback callback) {
        mWorkExecutor.execute(new Runnable() {
            @Override
            public void run() {
                File appZipStorage = AppUtil.getAppZipStorage(mContext);
                File saveFile = saveFile(appZipStorage, request.getName(), request.url);

                File schoolStorage = AppUtil.getHtmlPluginStorage(mContext, SYApplication.getInstance().domain);
                File schoolAppFile = new File(schoolStorage, mCode);
                if (AppUtil.unZipFile(schoolAppFile, saveFile)) {
                    callback.onResponse(new Response(true));
                    return;
                }
                callback.onResponse(new Response(false));
            }
        });
    }

    @Override
    public void get(final Request request, final RequestCallback callback) {
    }

    @Override
    public void post(Request request, RequestCallback callback) {

    }

    private File getResourceFile(String host, String fileName) {
        File storage = getResourceStorage(host);
        File cache = new File(storage, fileName);

        return cache;
    }

    public class ApiRequestHandler implements RequestHandler {
        private VolleySingleton mVolley;
        private String[] API_FILTERS = new String[]{"/mapi_v2/User/uploadAvatar"};

        public ApiRequestHandler() {
            this.mVolley = VolleySingleton.getInstance(mContext);
        }

        @Override
        public void handler(Request request, Response response) {
            Log.d(TAG, "api handler :" + request.url);
            String path = request.getPath();

            if (filter(path)) {
                return;
            }
            if (request.getPath().endsWith(String.format(Const.MOBILE_APP_URL, "/", mCode))) {
                File cache = getResourceFile(request.getHost(), "index.html");
                if (cache.exists()) {
                    handlerResponse(cache, response);
                }
                return;
            }

            handlerApiRequest(request, response);
        }

        private boolean filter(String url) {
            return CommonUtil.inArray(url, API_FILTERS);
        }

        private void handlerApiRequest(Request request, Response proxyResponse) {
            mVolley.getRequestQueue();
            final RequestUrl requestUrl = new RequestUrl(request.url);
            requestUrl.setHeads(new String[]{
                    "token", SYApplication.getInstance().token
                    , "Auth-Token", ""});

            RequestFuture<String> future = RequestFuture.newFuture();
            StringVolleyRequest stringRequest = new StringVolleyRequest(Method.GET, requestUrl, future, future);

            if (mWebView.getLoadType() == ESWebView.LOAD_FROM_CACHE
                    || mWebView.getWebView().isGoBack()) {
                stringRequest.setCacheUseMode(BaseVolleyRequest.ALWAYS_USE_CACHE);
                Log.d(TAG, "use cache");
            }
            stringRequest.setTag(requestUrl.url);
            mVolley.addToRequestQueue(stringRequest);

            String result = "";
            try {
                result = future.get();
            } catch (Exception e) {
            }
            proxyResponse.setEncoding("utf-8");
            proxyResponse.setContent(new ByteArrayInputStream(result.getBytes()));
        }
    }

    private void handlerResponse(File file, Response response) {
        try {
            String extension = getFileExtension(file.getName());
            response.setEncoding("utf-8");
            response.setMimeType(getFileMime(extension));
            response.setContent(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class WebViewRequestHandler implements RequestHandler {
        private final String[] MIME_FILTERS = {"html", "js", "css"};

        private boolean filterMime(String fileName) {
            String extension = getFileExtension(fileName);
            return CommonUtil.inArray(extension, MIME_FILTERS);
        }

        @Override
        public void handler(Request request, Response response) {
            String path = request.getPath(mCode);

            Response cacheResponse = mResoucrCache.get(path);
            if (cacheResponse != null) {
                Log.d(TAG, "mem cache :" + request.url);
                response.setResponse(cacheResponse);
                return;
            }

            File cache = getResourceFile(request.getHost(), path);
            if (!cache.exists()) {
                return;
            }

            Log.d(TAG, "file cache :" + request.url);
            handlerResponse(cache, response);
            if (!filterMime(cache.getName())) {
                return;
            }
            //不需要内存缓存
            //executeTask(new SaveResourceCacheTask(path, cache));
        }
    }

    private class SaveResourceCacheTask extends Task {

        private String path;
        private File   cache;

        public SaveResourceCacheTask(String path, File cache) {
            this.path = path;
            this.cache = cache;
        }

        @Override
        public void run() {
            setResourceCache(path, cache);
        }

        private void setResourceCache(String path, File cache) {
            try {
                String extension = getFileExtension(cache.getName());
                String mime = getFileMime(extension);

                byte[] fileData = EntityUtils.toByteArray(new FileEntity(cache, extension));
                Response response = new ResourceResponse(fileData);
                response.setEncoding("utf-8");
                response.setMimeType(mime);
                mResoucrCache.put(path, response);
                Log.d(TAG, "set mem cache :" + path);
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }
    }
}
