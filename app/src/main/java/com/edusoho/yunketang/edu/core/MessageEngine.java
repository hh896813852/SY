package com.edusoho.yunketang.edu.core;

import android.os.Bundle;
import android.util.Log;

import com.edusoho.yunketang.edu.bean.MessageType;
import com.edusoho.yunketang.edu.bean.WidgetMessage;
import com.edusoho.yunketang.edu.utils.InputUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JesseHuang on 15/4/23.
 */
public class MessageEngine {

    private Map<String, MessageCallback> sourceMap;
    private ConcurrentHashMap<String, ArrayList<String>> pubMsgMap;

    private static Object synchronizedObj = new Object();

    private static MessageEngine messageEngine;

    private MessageEngine() {
        pubMsgMap = new ConcurrentHashMap<>();
        sourceMap = new ConcurrentHashMap<>();
    }

    public void destory() {
        messageEngine = null;
        pubMsgMap.clear();
        sourceMap.clear();
    }

    public Map<String, MessageCallback> getSourceMap() {
        return sourceMap;
    }

    public static MessageEngine init() {
        synchronized (synchronizedObj) {
            if (messageEngine == null) {
                messageEngine = new MessageEngine();
            }
        }

        return messageEngine;
    }

    public static MessageEngine getInstance() {
        if (messageEngine == null) {
            messageEngine = init();
        }
        return messageEngine;
    }

    private String getMsgTargetType(Object target) {
        if (target instanceof Class) {
            return ((Class) target).getSimpleName();
        }

        return getObjectType(target);
    }

    private String getRegitstSourceType(MessageCallback source) {
        if (source.getMode() == MessageCallback.REGIST_CLASS) {
            return source.getClass().getSimpleName();
        }
        return getObjectType(source);
    }

    private String getObjectType(Object target) {
        return String.format(
                "%s@%s",
                target.getClass().getSimpleName(),
                Integer.toHexString(target.hashCode())
        );
    }

    public void sendMsgToTaget(int msgType, Bundle body, Object target) {
        String targetName = getMsgTargetType(target);
        MessageType messageType = new MessageType(msgType, targetName);

        MessageCallback messageCallback = sourceMap.get(targetName);
        if (messageCallback == null) {
            return;
        }
        Log.d("flag--", "sendMsgToTaget: " + messageCallback.getClass().getSimpleName());
        messageCallback.invoke(new WidgetMessage(messageType, body));
    }

    public void sendMsgToTagetForCallback(
            int msgType, Bundle body, Class target, InputUtils.NormalCallback callback) {
        String targetName = target.getSimpleName();
        MessageType messageType = new MessageType(msgType, targetName);

        MessageCallback messageCallback = sourceMap.get(targetName);
        if (messageCallback == null) {
            return;
        }
        messageCallback.invoke(new WidgetMessage(messageType, body, callback));
    }

    public void sendMsg(String msgType, Bundle body) {
        ArrayList<String> msgList = pubMsgMap.get(msgType);
        if (msgList == null) {
            return;
        }
        MessageType messageType = new MessageType(msgType);
        Iterator<String> iterator = msgList.iterator();
        while (iterator.hasNext()) {
            String name = iterator.next();
            MessageCallback messageCallback = sourceMap.get(name);
            if (messageCallback == null) {
                iterator.remove();
                continue;
            }
            messageCallback.invoke(new WidgetMessage(messageType, body));
        }
    }

    public void unRegistMessageSource(MessageCallback source) {
        if (source == null) {
            return;
        }
        String targetName = getRegitstSourceType(source);
        sourceMap.remove(targetName);
    }

    public void unRegistPubMessage(MessageType type, MessageCallback source) {
        if (source == null) {
            return;
        }
        ArrayList<String> list = pubMsgMap.get(type.toString());
        String targetName = getRegitstSourceType(source);
        list.remove(targetName);
    }

    public void stopRecvMessage(MessageCallback source) {

    }

    public void resumeRecvMessage(MessageCallback source) {

    }

    public void registMessageSource(MessageCallback source) {
        if (source == null) {
            return;
        }
        String targetName = getRegitstSourceType(source);
        sourceMap.put(targetName, source);
        MessageType[] msgTypes = source.getMsgTypes();
        if (msgTypes == null) {
            return;
        }

        for (MessageType msgType : msgTypes) {
            if (msgType.code != MessageType.NONE) {
                continue;
            }
            //all regist msgtype source device
            ArrayList<String> msgList = pubMsgMap.get(msgType.toString());
            if (msgList == null) {
                msgList = new ArrayList<String>();
            }

            if (!msgList.contains(targetName)) {
                msgList.add(targetName);
            }
            pubMsgMap.put(msgType.toString(), msgList);
        }
    }

    public interface MessageCallback {
        int REGIST_CLASS = 0;
        int REGIST_OBJECT = 1;

        int MSG_PAUSE = 0010;
        int MSG_RESUME = 0011;

        void invoke(WidgetMessage message);

        MessageType[] getMsgTypes();

        int getMode();
    }
}
