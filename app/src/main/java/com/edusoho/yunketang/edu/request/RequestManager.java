package com.edusoho.yunketang.edu.request;



import com.edusoho.yunketang.edu.bean.RequestUrl;
import com.edusoho.yunketang.edu.request.model.Request;
import com.edusoho.yunketang.edu.request.model.Response;
import com.edusoho.yunketang.edu.webview.ESWebView;

import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.regex.Pattern;

/**
 * Created by howzhi on 15/4/28.
 */
public abstract class RequestManager {

    protected Hashtable<String, Response> mResoucrCache;
    private LinkedHashMap<Pattern, RequestHandler> mRequestHandlerList;

    protected ScheduledThreadPoolExecutor mWorkExecutor;
    private static final int MAX_POOL = 10;
    protected ESWebView mWebView;

    public RequestManager() {
        mResoucrCache = new Hashtable<>();
        mRequestHandlerList = new LinkedHashMap<>();
        mWorkExecutor = new ScheduledThreadPoolExecutor(MAX_POOL);
    }

    public void destroy() {
        mResoucrCache.clear();
        mRequestHandlerList.clear();
        mWorkExecutor.shutdown();
    }

    protected void executeTask(Task task) {
        mWorkExecutor.execute(task);
    }

    protected class Task implements Runnable {

        public Task(Object... params) {
        }

        @Override
        public void run() {
        }
    }

    public void setWebView(ESWebView webView) {
        this.mWebView = webView;
    }

    public abstract void updateApp(RequestUrl requestUrl, final RequestCallback<Boolean> callback);

    public abstract void get(Request request, RequestCallback callback);

    public abstract <T> T blockGet(Request request, RequestCallback<T> callback);

    public abstract <T> T blocPost(Request request, RequestCallback<T> callback);

    public abstract <T> void downloadResource(Request request, RequestCallback<T> callback);

    public abstract void post(Request request, RequestCallback callback);

    public void registHandler(String pattern, RequestHandler handler) {
        mRequestHandlerList.put(Pattern.compile(pattern), handler);
    }

    protected void handleRequest(Request request, Response response) {
        for (Pattern filter : mRequestHandlerList.keySet()) {
            if (filter.matcher(request.url).find()) {
                mRequestHandlerList.get(filter).handler(request, response);
                return;
            }
        }
    }
}
