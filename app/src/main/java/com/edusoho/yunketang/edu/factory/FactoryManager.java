package com.edusoho.yunketang.edu.factory;

import android.content.Context;
import android.util.Log;

import com.edusoho.yunketang.edu.factory.provider.AbstractProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Hashtable;

/**
 * Created by su on 2015/12/27.
 */
public class FactoryManager {

    private static Object mLock = new Object();
    private static FactoryManager mFactoryManager;
    private static String TARGET = "FactoryManager";
    private Context mContext;
    protected Hashtable<String, IService> mFactoryMap;

    private FactoryManager() {
        mFactoryMap = new Hashtable<>();
    }

    public FactoryManager initContext(Context context) {
        this.mContext = context;
        return this;
    }

    public void reInitContext(String target, Context context) {
        TARGET = target;
        FactoryManager newFactoryManager = new FactoryManager();
        newFactoryManager.initContext(context);
        if (mFactoryManager != null) {
            for (String name : mFactoryMap.keySet()) {
                IService service = mFactoryMap.get(name);
                newFactoryManager.addFactory(service);
            }
        }

        mFactoryManager.clear();
        mFactoryManager = newFactoryManager;
    }

    public static FactoryManager getInstance() {
        synchronized (mLock) {
            if (mFactoryManager == null) {
                mFactoryManager = new FactoryManager();
            }
        }
        return mFactoryManager;
    }

    public <T extends IService> T create(Class tClass) {
        T factory = (T) mFactoryManager.getFactory(tClass.getSimpleName());
        if (factory != null) {
            Log.d(FactoryManager.class.getName(), "get from " + tClass);
            return factory;
        }

        try {
            factory = (T) mFactoryManager.getFactoryInstance(tClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(tClass.getName() + " Factory create fail");
        }

        mFactoryManager.addFactory(factory);
        return factory;
    }

    private Object getFactoryInstance(Class tClass) throws
            InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        if (AbstractProvider.class.isAssignableFrom(tClass)) {
            if (mContext == null) {
                throw new RuntimeException("create provider must init Context");
            }
            Constructor constructor = tClass.getConstructor(Context.class);
            return constructor.newInstance(mContext);
        }
        return tClass.newInstance();
    }

    public IService getFactory(String servcieName) {
        return mFactoryMap.get(servcieName);
    }

    public void addFactory(IService factrory) {
        mFactoryMap.put(factrory.getId(), factrory);
    }

    public void clear() {
        mFactoryMap.clear();
    }
}
