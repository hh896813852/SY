package com.edusoho.yunketang.edu.http;

import android.text.TextUtils;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.edusoho.yunketang.edu.bean.ErrorResult;
import com.edusoho.yunketang.edu.bean.MessageEvent;
import com.edusoho.yunketang.edu.utils.Constants;
import com.edusoho.yunketang.edu.utils.GsonUtils;
import com.edusoho.yunketang.edu.utils.helper.ErrorHelper;

import org.greenrobot.eventbus.EventBus;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by JesseHuang on 2017/5/29.
 */

public class SubscriberProcessor<T> extends Subscriber<T> {

    public SubscriberProcessor() {
    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onCompleted() {

    }

    @Override
    final public void onError(Throwable e) {
        String json;
        try {
            if (e instanceof JsonSyntaxException) {
                onError(ErrorHelper.getMessage(ErrorHelper.JSON_ERROR));
                return;
            }
            if (e instanceof UnknownHostException || e instanceof SocketTimeoutException || e instanceof ConnectException) {
                onError(ErrorHelper.getMessage(ErrorHelper.INTERNAL_TIMEOUT_OR_BAD));
                return;
            }
            if (e instanceof HttpException) {
                if (((HttpException) e).code() == Constants.HttpCode.UNAUTHORIZED) {
                    EventBus.getDefault().post(new MessageEvent<>(MessageEvent.CREDENTIAL_EXPIRED));
                    return;
                }
                json = ((HttpException) e).response().errorBody().string();
                ErrorResult error = GsonUtils.parseJson(json, new TypeToken<ErrorResult>() {
                });
                if (error != null) {
                    onError(error.error);
                }

                if (error != null && error.error != null) {
                    if (!TextUtils.isEmpty(ErrorHelper.getErrorMessage(error.error.code))) {
                        onError(ErrorHelper.getMessage(error.error.code));
                    } else {
                        onError(error.error.message);
                    }

                }
            }
        } catch (Exception ex) {
            onError(ErrorHelper.getMessage());
        }
    }

    /**
     * 以后需要去掉
     *
     * @param message errorMessage
     */
    @Deprecated
    public void onError(String message) {

    }

    public void onError(ErrorResult.Error error) {

    }
}
