package com.xugaoxiang.ott.appstore.pojo;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * Created by zero on 2016/9/21.
 */
public class BeanAppInfo implements Serializable{

    private String packageName;
    private String appName;
    private String appVersion;
    private Drawable appIcon;
    private long cacheSize;
    private long dateSize;
    private long appSize;
    private long totalSize;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public long getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(long cacheSize) {
        this.cacheSize = cacheSize;
    }

    public long getDateSize() {
        return dateSize;
    }

    public void setDateSize(long dateSize) {
        this.dateSize = dateSize;
    }

    public long getAppSize() {
        return appSize;
    }

    public void setAppSize(long appSize) {
        this.appSize = appSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}
