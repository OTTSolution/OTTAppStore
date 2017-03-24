package com.xugaoxiang.ott.appstore.module.appinfo;


import com.xugaoxiang.ott.appstore.pojo.GsonAppInfo;
import com.xugaoxiang.ott.appstore.pojo.OrmApp;
import com.xugaoxiang.ott.appstore.module.base.BaseView;
import com.lzy.okserver.download.DownloadInfo;


/**
 *ApplicationDetailsView的方法
 */
public interface ApplicationInfoView extends BaseView {

    /**
     * 通过网络获取应用详情
     * @param gson
     */
    void showAppDetails(GsonAppInfo gson);

    /**
     * 通过本地数据库获取应用详情
     * @param ormApp
     */
    void showAppDetails(OrmApp ormApp);

    /**
     * 通过查询packageInfo信息获取应用详情
     * @param appSize
     */
    void showAppDetails(long appSize);

    void refreshView(DownloadInfo downloadInfo);

    void installSuccess();

    void uninstallSuccess();

    void updateSuccess();

}
