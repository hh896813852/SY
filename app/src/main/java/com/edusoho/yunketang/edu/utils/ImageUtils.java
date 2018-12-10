package com.edusoho.yunketang.edu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtils {

    /**
     * Bytes to bitmap.
     *
     * @param bytes The bytes.
     * @return bitmap
     */
    public static Bitmap bytes2Bitmap(final byte[] bytes) {
        return (bytes == null || bytes.length == 0)
                ? null
                : BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
