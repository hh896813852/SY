package com.edusoho.yunketang.edu.webview.bridgeadapter.bridge;

import android.util.SparseArray;


import com.edusoho.yunketang.edu.webview.bridgeadapter.AbstractJsBridgeAdapterWebView;

import java.util.Hashtable;
import java.util.List;

/**
 * Created by su on 2015/12/9.
 */
public class PluginManager {

    private SparseArray<Hashtable> mPluginMapArray;

    public PluginManager()
    {
        mPluginMapArray = new SparseArray<>();
    }

    public void registPluginList(AbstractJsBridgeAdapterWebView webView, List<Class> pluginClassList) {
        Hashtable<String, IBridgePlugin> pluginMap = new Hashtable<>();
        for (Class pluginClass : pluginClassList) {
            try {
                IBridgePlugin plugin = (IBridgePlugin) pluginClass.newInstance();
                plugin.initialize(webView.getBridgePluginContext());
                pluginMap.put(plugin.getName(), plugin);
            } catch (Exception e) {

            }
        }

        mPluginMapArray.put(webView.hashCode(), pluginMap);
    }

    public IBridgePlugin getPlugin(int key, String name) {
        Hashtable<String, IBridgePlugin> pluginMap = mPluginMapArray.get(key);
        if (pluginMap == null) {
            return null;
        }

        return pluginMap.get(name);
    }

    public void unRegistPlugin(int key) {
        Hashtable pluginMap = mPluginMapArray.get(key);
        if (pluginMap != null) {
            pluginMap.clear();
        }
    }

    public void destory() {
        if (mPluginMapArray == null || mPluginMapArray.size() == 0){
            return;
        }
        int size = mPluginMapArray.size();
        for (int i = 0; i < size; i++) {
            unRegistPlugin(i);
        }

        mPluginMapArray.clear();
    }

}
