package com.edusoho.yunketang.utils;

import android.os.Bundle;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.edusoho.yunketang.edu.core.MessageEngine;
import com.edusoho.yunketang.edu.utils.Const;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;

/**
 * Created by su on 2016/2/3.
 */
public class RequestUtil {

    public static String handleRequestError(String jsonStr) throws RequestErrorException {
        LinkedTreeMap errorResult = null;
        try {
            errorResult = new Gson().fromJson(jsonStr, LinkedTreeMap.class);
        } catch (Exception e) {
        }
        if (errorResult == null) {
            return jsonStr;
        }
        if (errorResult.containsKey("error")) {
            LinkedTreeMap<String, String> errorMap = (LinkedTreeMap<String, String>) errorResult.get("error");

            if ("not_login".equals(errorMap.get("name"))) {
                MessageEngine.getInstance().sendMsg(Const.TOKEN_LOSE, new Bundle());
                return null;
            }

            if (errorMap.containsKey("message") && errorMap.containsKey("name")) {
                return errorMap.get("message");
            }

            if (errorMap.containsKey("message") && errorMap.containsKey("code")) {
                throw new RequestErrorException();
            }
        }

        if (errorResult.containsKey("code")) {
            String message = errorResult.containsKey("message") ? errorResult.get("message").toString() : "";
            if (!TextUtils.isEmpty(message) && message.startsWith("API Token 已过期")) {
                MessageEngine.getInstance().sendMsg(Const.TOKEN_LOSE, new Bundle());
                return null;
            }

            return jsonStr;
        }

        return jsonStr;
    }

    public static String handleRequestHttpError(String data) {
        try {
            LinkedHashMap errorResult = null;
            try {
                errorResult = new Gson().fromJson(data, LinkedHashMap.class);
            } catch (Exception e) {
            }
            if (errorResult == null) {
                return data;
            }
            if (errorResult.containsKey("code")) {
                String message = errorResult.containsKey("message") ? errorResult.get("message").toString() : "";
                String webServerCode = new DecimalFormat("0").format(errorResult.get("code"));
                if (!TextUtils.isEmpty(message) && "500".equals(webServerCode)) {
                    return message;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static String handleRequestError(byte[] data) throws RequestErrorException {
        String jsonStr = "";
        try {
            jsonStr = new String(data, "utf-8");
            jsonStr = handleRequestError(jsonStr);
        } catch (Exception e) {
            return jsonStr;
        }
        return jsonStr;
    }

    public static class RequestErrorException extends Exception {
    }
}
