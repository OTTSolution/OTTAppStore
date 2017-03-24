package com.xugaoxiang.ott.appstore.constant;

import android.os.Environment;

import java.io.File;

/**
 * Created by zero on 2016/9/19.
 */
public class Constants {

    /**********************************下载地址**********************************************/
    public static final String FILE_DOWNLOAD_PATH = Environment.getExternalStorageDirectory() + File.separator + "AppStoreApk" + File.separator;
    public static final String FILE_APPSERVER = Environment.getExternalStorageDirectory() + File.separator + "MyApp" + File.separator + "AppServer";

    /**********************************接口地址**********************************************/
    public static final String REQUEST_URL_PUBLISHED = "http://202.158.177.67:8080";
    public static final String REQUEST_URL_TEST = "http://10.10.10.200:8080";
    public static String BASE_URL = REQUEST_URL_PUBLISHED;

    public static final String URL_DOWNLOAD_APP = "/api/market/download?id=";
    public static final String URL_APP_INFO = "/api/market/info";
    public static final String URL_RECOMMEND_APP = "/api/market/recommend";
    public static final String URL_ALL_APP = "/api/market/allapp";
    public static final String CATEGORY_LIST = "/api/market/types";
    public static final String APP_LIST_BY_CATEGORY = "/api/market/type";

    /**********************************安装状态**********************************************/
    public static final int APP_STATUS_UNINSTALLED = 0;
    public static final int APP_STATUS_INSTALLED = 1;
    public static final int APP_STATUS_NEED_UPDATE = 2;

    /**********************************应用类别**********************************************/
    public static final int APP_SHOW_TYPE_RECOMMEND = 0;
    public static final int APP_SHOW_TYPE_ALL = 1;
    public static final int APP_SHOW_TYPE_MANAGER = 2;

    /**********************************requestCode**********************************************/
    public static final int ACTIVITY_APPLICATION_INFO = 11;

    /**********************************resultCode**********************************************/
    public static final int NOTIFY_ADAPTER = 11;

    /**********************************fragment栈************************************************/



}
