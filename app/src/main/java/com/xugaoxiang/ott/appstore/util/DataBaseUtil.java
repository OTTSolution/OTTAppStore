package com.xugaoxiang.ott.appstore.util;



import android.support.annotation.NonNull;

import com.xugaoxiang.ott.appstore.pojo.GsonAppInfo;
import com.xugaoxiang.ott.appstore.pojo.OrmApp;
import com.xugaoxiang.ott.appstore.pojo.OrmPhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zero on 2016/10/14.
 */

public class DataBaseUtil {

    /**
     * 保存app信息到数据库
     * @param gson
     * @return
     */
    public static void createRecords(GsonAppInfo gson) {
        OrmApp app = gsonToOrmBean(gson);
        boolean isSuccess = app.save();
    }

    /**
     * 更新app信息到数据库
     * @param gson
     * @return
     */
    public static void updateRecords(GsonAppInfo gson) {
        OrmApp app = gsonToOrmBean(gson);
        int rowsAffected = app.updateAll("appId = ?", app.getAppId() + "");
    }

    @NonNull
    private static OrmApp gsonToOrmBean(GsonAppInfo gson) {
        List<GsonAppInfo.PhotosBean> photoList = gson.getPhotos();
        List<OrmPhoto> ormPhotoList = new ArrayList<>();
        if (photoList != null && photoList.size() > 0) {
            for (GsonAppInfo.PhotosBean bean : photoList) {
                OrmPhoto photo = new OrmPhoto();
                photo.setUrl(bean.getUrl());
                photo.save();
                ormPhotoList.add(photo);
            }
        }
        OrmApp app = new OrmApp();
        app.setAppId(gson.getId());
        app.setName(gson.getAppName());
        app.setIcon(gson.getIcon_url());
        app.setType(gson.getType());
        app.setContent(gson.getDesc());
        app.setDownloadCount(gson.getDownloads());
        app.setVersion(gson.getVersion());
        app.setFileSize(Integer.parseInt(gson.getSize())/1048576);
        app.setPhotoCount(ormPhotoList.size());
        app.setPackageName(gson.getPackageName());
        app.getPhotoList().addAll(ormPhotoList);
        return app;
    }


}
