package com.edusoho.yunketang.edu.factory.provider;

import android.content.Context;

import com.edusoho.yunketang.edu.factory.IService;


/**
 * Created by su on 2015/12/30.
 */
public abstract class AbstractProvider implements IService {

    protected Context mContext;

    public AbstractProvider(Context context)
    {
        this.mContext = context;
    }

    @Override
    public String getId() {
        return getClass().getSimpleName();
    }
}
