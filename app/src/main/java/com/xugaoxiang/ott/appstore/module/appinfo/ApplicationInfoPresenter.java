package com.xugaoxiang.ott.appstore.module.appinfo;

import com.xugaoxiang.ott.appstore.pojo.OrmApp;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.listener.DownloadListener;

/**
 * 应用详情页面的Presenter
 */
public class ApplicationInfoPresenter {

    private ApplicationInfoView mView;
    private ApplicationInfoModel mModel;

    public ApplicationInfoPresenter(ApplicationInfoView mainView) {
        mView = mainView;
        mModel = new ApplicationInfoModel();
    }

    public void getAppDetails(int appId) {
        mModel.getAppDetails(appId, mView);
    }


    public void installApp(DownloadManager downloadManager, DownloadListener listener, int appId) {
        mModel.installApp(downloadManager, listener, appId);
    }

    public void uninstallApp(String packageName) {
        mModel.uninstallApp(packageName);
    }

    public void updateApp(DownloadManager downloadManager, DownloadListener listener, int appId) {
        mModel.updateApp(downloadManager, listener, appId);
    }

    public void openApp(String packageName) {
        mModel.openApp(packageName);
    }

    public OrmApp retrieveRecords(String packageName) {
        return mModel.retrieveRecords(packageName);
    }
}
