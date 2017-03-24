package com.xugaoxiang.ott.appstore.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zero on 2016/9/19.
 */
public class GsonAppInfo implements Serializable {

    /**
     * id : 38
     * appName : Testapp1
     * icon_url : /static/images/ca5e73322b58e4db0e350ddbe75d618e.png
     * type : 游戏
     * desc :
     * downloads : 0
     * version : 1.0.0
     * size : 1323
     * packageName : com.longjingtech.ott.testapp1
     * photos : [{"url":"/static/images/Android.png"},{"url":"/static/images/Android.png"}]
     */

    private int id;
    private String appName;
    private String icon_url;
    private String type;
    private String desc;
    private String downloads;
    private String version;
    private String size;//KB
    private String packageName;
    private int status;
    /**
     * url : /static/images/Android.png
     */

    private List<PhotosBean> photos;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDownloads() {
        return downloads;
    }

    public void setDownloads(String downloads) {
        this.downloads = downloads;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosBean> photos) {
        this.photos = photos;
    }

    public static class PhotosBean implements Serializable{
        private String photo_url;

        public String getUrl() {
            return photo_url;
        }

        public void setUrl(String photo_url) {
            this.photo_url = photo_url;
        }
    }
}
