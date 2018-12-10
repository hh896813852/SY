package com.edusoho.yunketang.bean.seting;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CloudVideoSetting implements Serializable {

    private WatermarkSetting   watermarkSetting;
    private FingerPrintSetting fingerPrintSetting;

    public WatermarkSetting getWatermarkSetting() {
        return watermarkSetting;
    }

    public void setWatermarkSetting(WatermarkSetting watermarkSetting) {
        this.watermarkSetting = watermarkSetting;
    }

    public FingerPrintSetting getFingerPrintSetting() {
        return fingerPrintSetting;
    }

    public void setFingerPrintSetting(FingerPrintSetting fingerPrintSetting) {
        this.fingerPrintSetting = fingerPrintSetting;
    }

    public static class WatermarkSetting {

        @SerializedName("video_watermark")
        private int    videoWatermark;
        @SerializedName("video_watermark_image")
        private String videoWatermarkImage;
        @SerializedName("video_embed_watermark_image")
        private String videoEmbedWatermarkImage;
        @SerializedName("video_watermark_position")
        private String videoWatermarkPosition;

        public int getVideoWatermark() {
            return videoWatermark;
        }

        public String getVideoWatermarkImage() {
            return videoWatermarkImage;
        }

        public String getVideoEmbedWatermarkImage() {
            return videoEmbedWatermarkImage;
        }

        public String getVideoWatermarkPosition() {
            return videoWatermarkPosition;
        }
    }

    public static class FingerPrintSetting {

        @SerializedName("video_fingerprint")
        private int   videoFingerprint;
        @SerializedName("video_fingerprint_time")
        private float videoFingerprintTime;

        public int getVideoFingerprint() {
            return videoFingerprint;
        }

        public float getVideoFingerprintTime() {
            return videoFingerprintTime;
        }
    }
}
