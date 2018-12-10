package com.edusoho.yunketang.edu.factory;


/**
 * Created by su on 2015/12/28.
 */
public abstract class AbstractFactrory implements IService {

    public String getId() {
        return getClass().getSimpleName();
    }
}
