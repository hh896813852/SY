package com.edusoho.yunketang.edu.core;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.edusoho.yunketang.R;
import com.edusoho.yunketang.edu.bean.MessageType;
import com.edusoho.yunketang.edu.listener.CoreEngineMsgCallback;
import com.edusoho.yunketang.edu.listener.NormalCallback;
import com.edusoho.yunketang.edu.listener.PluginFragmentCallback;
import com.edusoho.yunketang.edu.listener.PluginRunCallback;
import com.edusoho.yunketang.edu.model.MessageModel;
import com.edusoho.yunketang.edu.model.PluginModel;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by JesseHuang on 15/4/23.
 */
public class CoreEngine {
    private Context mContext;
    public static final String PLUGIN = "plugin";
    public static final String INSTALL = "app_install";
    private static CoreEngine engine;
    private MessageEngine messageEngine;


    private ConcurrentHashMap<String, ArrayList<CoreEngineMsgCallback>> mMessageMap;
    private HashMap<String, PluginModel> mPluginModelHashMap;

    private CoreEngine(Context context) {
        mContext = context;
        init();
    }

    public void receiveMsg(String msgId, CoreEngineMsgCallback callback) {
        ArrayList<CoreEngineMsgCallback> callbackList = mMessageMap.get(msgId);
        if (callbackList == null) {
            callbackList = new ArrayList<CoreEngineMsgCallback>();
        }
        callbackList.add(callback);
        mMessageMap.put(msgId, callbackList);
    }

    public MessageEngine getMessageEngine() {
        return messageEngine;
    }

    public void removeMsg(String msgId) {
        mMessageMap.remove(msgId);
    }

    public void sendMsg(String msgId, MessageModel messageModel) {
        ArrayList<CoreEngineMsgCallback> callbackList = mMessageMap.get(msgId);
        if (callbackList != null) {
            for (CoreEngineMsgCallback callback : callbackList) {
                callback.invoke(messageModel);
            }
        }
    }

    public void registPlugin(String name, Class targetClass) {
        PluginModel pluginModel = new PluginModel();
        pluginModel.packAge = targetClass.getName();
        pluginModel.version = "1.0.0";
        pluginModel.name = name;
        mPluginModelHashMap.put(name, pluginModel);
    }

    public static CoreEngine create(Context context) {
        synchronized (CoreEngine.class) {
            if (engine == null) {
                engine = new CoreEngine(context);
            }
        }
        return engine;
    }

    public void runPluginFromFragmentFroResult(
            String pluginName, Fragment fragment, int requestCode, Bundle bundle) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setClassName(fragment.getActivity(), pluginModel.packAge);
            if (bundle != null) {
                startIntent.putExtras(bundle);
            }
            fragment.startActivityForResult(startIntent, requestCode);
        }
    }

    public void runPluginFromFragmentForResultWithCallback(
            String pluginName, Fragment fragment, int requestCode, PluginRunCallback callback) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setClassName(fragment.getActivity(), pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }

            fragment.startActivityForResult(startIntent, requestCode);
        }
    }

    public void runNormalPluginForResult(
            String pluginName, Activity serverActivity, int requestCode, PluginRunCallback callback) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }

            serverActivity.startActivityForResult(startIntent, requestCode);
        }
    }

    public void runService(String serviceName, Context serverActivity, PluginRunCallback callback) {
        PluginModel pluginModel = mPluginModelHashMap.get(serviceName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }

            serverActivity.startService(startIntent);
        }
    }

    public boolean isPlugin(String pluginName) {
        return mPluginModelHashMap.containsKey(pluginName);
    }

    public String getPluginPkgName(String pluginName) {
        return mPluginModelHashMap.get(pluginName).packAge;
    }

    public Fragment runPluginWithFragmentByBundle(
            String pluginName, Context context, Bundle bundle) {
        Fragment fragment = null;
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            fragment = Fragment.instantiate(context, pluginModel.packAge);
            fragment.setArguments(bundle);

            return fragment;
        }
        return null;
    }

    public Fragment runPluginWithFragment(
            String pluginName, Context context, PluginFragmentCallback callback) {
        Fragment fragment;
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            fragment = Fragment.instantiate(context, pluginModel.packAge);
            if (callback != null) {
                Bundle bundle = new Bundle();
                fragment.setArguments(bundle);
                callback.setArguments(bundle);
            }

            return fragment;
        }
        return null;
    }

    public View runNormalPluginInGroup(
            String pluginName, ActivityGroup serverActivity, PluginRunCallback callback
    ) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }
            Window window = serverActivity.getLocalActivityManager()
                    .startActivity(pluginName, startIntent);
            return window.getDecorView();
        }
        return new View(serverActivity);
    }

    public void runNormalPlugin(
            String pluginName, Context serverActivity, PluginRunCallback callback) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }

            serverActivity.startActivity(startIntent);
        }
    }

    public void runNormalPlugin(
            String pluginName, Context serverActivity, PluginRunCallback callback, int flags) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setFlags(flags);
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }

            serverActivity.startActivity(startIntent);
        }
    }

    public void runNormalPluginWithAnim(
            String pluginName, Context serverActivity, PluginRunCallback callback, NormalCallback normalCallback) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (callback != null) {
                callback.setIntentDate(startIntent);
            }

            serverActivity.startActivity(startIntent);
            normalCallback.success(null);
        }
    }

    public void runNormalPluginWithBundle(
            String pluginName, Context serverActivity, Bundle bundle) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (bundle != null) {
                startIntent.putExtras(bundle);
            }
            serverActivity.startActivity(startIntent);
        }
    }

    public void runNormalPluginWithBundleForResult(
            String pluginName, Activity serverActivity, Bundle bundle, int requestCode) {
        PluginModel pluginModel = mPluginModelHashMap.get(pluginName);
        if (pluginModel != null) {
            Intent startIntent = new Intent();
            startIntent.setClassName(serverActivity, pluginModel.packAge);
            if (bundle != null) {
                startIntent.putExtras(bundle);
            }

            serverActivity.startActivityForResult(startIntent, requestCode);
        }
    }

    public File getPluginFile(String pluginName) {
        return new File(mContext.getFilesDir(), pluginName);
    }

    public void registMsgSrc(MessageEngine.MessageCallback source) {
        messageEngine.registMessageSource(source);
    }

    public void unRegistMessageSource(MessageEngine.MessageCallback source) {
        messageEngine.unRegistMessageSource(source);
    }

    public void unRegistPubMessage(MessageType messageType, MessageEngine.MessageCallback source) {
        messageEngine.unRegistPubMessage(messageType, source);
    }

    private void init() {
        messageEngine = MessageEngine.init();
        mMessageMap = new ConcurrentHashMap<String, ArrayList<CoreEngineMsgCallback>>();
        initPluginFromXml();

        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 1);
            ActivityInfo[] activities = packageInfo.activities;
            Resources resources = packageManager.getResourcesForActivity(
                    new ComponentName(mContext.getPackageName(), activities[0].name));

            XmlResourceParser xmlResourceParser = resources.getXml(R.xml.plugins);
            HashMap<String, PluginModel> otherPluginMap = parsePluginXml(xmlResourceParser);
            mPluginModelHashMap.putAll(otherPluginMap);
        } catch (Exception e) {
            Log.i(null, "no app plugin");
        }
    }

    public void installApkPlugin() {
        try {
            copyPluginFromAsset(getAssetPlugins(PLUGIN));
            copyInstallApkFromAsset(getAssetPlugins(INSTALL), mContext.getDir(INSTALL, Context.MODE_PRIVATE).getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, PluginModel> parsePluginXml(XmlResourceParser parser) {
        PluginModel pluginModel = null;
        HashMap<String, PluginModel> pluginModels = null;
        try {
            int type = parser.getEventType();
            while (type != XmlPullParser.END_DOCUMENT) {
                switch (type) {
                    case XmlPullParser.START_DOCUMENT:
                        pluginModels = new HashMap<String, PluginModel>();
                        break;
                    case XmlPullParser.START_TAG:
                        if ("plugin".equals(parser.getName())) {
                            String name = parser.getAttributeValue(null, "name");
                            String version = parser.getAttributeValue(null, "version");
                            String packAge = parser.getAttributeValue(null, "package");
                            pluginModel = new PluginModel();
                            pluginModel.packAge = packAge;
                            pluginModel.version = version;
                            pluginModel.name = name;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("plugin".equals(parser.getName())) {
                            pluginModels.put(pluginModel.name, pluginModel);
                        }
                        break;
                }
                type = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pluginModels;
    }

    private void initPluginFromXml() {
        XmlResourceParser parser = mContext.getResources().getXml(R.xml.core_plugins);
        mPluginModelHashMap = parsePluginXml(parser);
    }

    public String[] getAssetPlugins(String dirName) throws IOException {
        AssetManager assetManager = mContext.getAssets();
        return assetManager.list(dirName);
    }

    public void copyPluginFromAsset(String[] dirPath) throws Exception {
        AssetManager assetManager = mContext.getAssets();
        File pluginDir = getPluginDir();
        for (String path : dirPath) {
            OutputStream target = new FileOutputStream(new File(pluginDir, path));
            copyFile(assetManager.open(PLUGIN + "/" + path), target);
        }
    }

    public void installApkFromAssetByPlugin(String installDir) {
        try {
            copyInstallApkFromAsset(getAssetPlugins(INSTALL), installDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void copyInstallApkFromAsset(String[] dirPath, String installDir) throws Exception {
        AssetManager assetManager = mContext.getAssets();
        for (String path : dirPath) {
            OutputStream target = new FileOutputStream(new File(installDir, path));
            copyFile(assetManager.open(INSTALL + "/" + path), target);
        }
    }

    private File getPluginDir() {
        File pluginDir = new File(mContext.getFilesDir(), PLUGIN);
        if (!pluginDir.exists()) {
            pluginDir.mkdir();
        }
        return pluginDir;
    }

    private void copyFile(InputStream src, OutputStream target) {
        int len = -1;
        byte[] buffer = new byte[1024];
        try {
            while ((len = src.read(buffer)) != -1) {
                target.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                src.close();
                target.close();
            } catch (Exception e) {
            }
        }
    }
}
