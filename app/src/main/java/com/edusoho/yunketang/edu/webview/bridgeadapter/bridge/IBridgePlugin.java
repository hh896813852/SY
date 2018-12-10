package com.edusoho.yunketang.edu.webview.bridgeadapter.bridge;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by su on 2015/12/9.
 */
public interface IBridgePlugin {

    String getName();

    void initialize(BridgePluginContext pluginContext);

    Object executeAnsy(String action, JSONArray args);

    boolean execute(
            String action, JSONArray args, BridgeCallback callbackContext) throws JSONException;
}
