package com.edusoho.yunketang.edu.request.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by howzhi on 15/7/4.
 */
public class ResourceResponse extends Response<byte[]> {

    public ResourceResponse(byte[] data) {
        this.mData = data;
    }

    @Override
    public InputStream getContent() {
        try {
            return new ByteArrayInputStream(getData());
        } catch (Exception e) {
            return null;
        }
    }
}
