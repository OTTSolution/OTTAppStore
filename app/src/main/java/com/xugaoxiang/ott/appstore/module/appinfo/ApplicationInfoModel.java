package com.xugaoxiang.ott.appstore.module.appinfo;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.MyApplication;
import com.xugaoxiang.ott.appstore.module.base.BaseModel;
import com.xugaoxiang.ott.appstore.pojo.GsonAppInfo;
import com.xugaoxiang.ott.appstore.pojo.OrmApp;
import com.xugaoxiang.ott.appstore.contract.JsonConvert;
import com.xugaoxiang.ott.appstore.util.ApkManagerUtil;
import com.xugaoxiang.ott.appstore.util.log.Log;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okrx.RxAdapter;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.listener.DownloadListener;


import org.litepal.crud.DataSupport;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 应用详情的model
 */
public class ApplicationInfoModel extends BaseModel {

    private PackageManager pm;

    public void getAppDetails(final int appId, final ApplicationInfoView mView) {
        OkGo.get(Constants.BASE_URL + Constants.URL_APP_INFO)
                .params("id", appId)
                .getCall(new JsonConvert<>(GsonAppInfo.class), RxAdapter.<GsonAppInfo>create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GsonAppInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.showToast("网络错误");
                    }

                    @Override
                    public void onNext(GsonAppInfo gson) {
                        if (gson.getId() > 0){
                            int status = checkAppStatus(gson.getPackageName(), gson.getVersion());
                            gson.setStatus(status);
                            mView.showAppDetails(gson);
                        }
                        else
                            mView.showToast("数据异常");
                    }
                });
    }

    @SuppressWarnings("WrongConstant")
    public int checkAppStatus(String packageName, String version) {
        pm = MyApplication.getContext().getPackageManager();
        List<PackageInfo> appList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo app : appList){
            if (TextUtils.equals(packageName, app.packageName)) {
                if(!TextUtils.equals(version, app.versionName))//版本号不同则需要升级
                    return Constants.APP_STATUS_NEED_UPDATE;
                else
                    return Constants.APP_STATUS_INSTALLED;
            }
        }
        return Constants.APP_STATUS_UNINSTALLED;
    }

    public void installApp(DownloadManager downloadManager, DownloadListener listener, int appId) {
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(appId + "");
        if (downloadInfo != null)
        {
            downloadManager.restartTask(appId + "");
            downloadInfo.setListener(listener);
        }
        else
        {
            Log.i("Constants.BASE_URL：" + Constants.BASE_URL);
            String url = Constants.BASE_URL + Constants.URL_DOWNLOAD_APP + appId;
            Log.i("下载地址：" + url);
            GetRequest request = OkGo.get(url);
            downloadManager.addTask(appId + ".apk", appId + "", request, listener);
            Log.i("添加任务task："+ appId);
        }
    }

    public void uninstallApp(String packageName) {
        Toast.makeText(MyApplication.getContext(), "正在卸载", Toast.LENGTH_SHORT).show();
        if (MyApplication.isSlient)
            ApkManagerUtil.uninstallAppSlient(packageName);
        else
            ApkManagerUtil.uninstallApp(packageName);
    }

    public void updateApp(DownloadManager downloadManager, DownloadListener listener, int appId) {
        DownloadInfo downloadInfo = downloadManager.getDownloadInfo(appId + "");
        if (downloadInfo != null)
        {
            downloadManager.restartTask(appId + "");
            downloadInfo.setListener(listener);
        }
        else
        {
            Log.i("Constants.BASE_URL：" + Constants.BASE_URL);
            String url = Constants.BASE_URL + Constants.URL_DOWNLOAD_APP + appId;
            Log.i("更新地址：" + url);
            GetRequest request = OkGo.get(url);
            downloadManager.addTask(appId + ".apk", appId + "", request, listener);
        }
    }

    public void openApp(String packageName) {
        ApkManagerUtil.openApp(packageName);
    }

    /**
     * 从数据库查询app信息
     * @param packageName
     * @return
     */
    public OrmApp retrieveRecords(String packageName) {
        List<OrmApp> ormApps = DataSupport.where("packagename = ?", packageName).limit(1).find(OrmApp.class, true);
        if (ormApps.size() > 0)
            return ormApps.get(0);
        else
            return null;
    }

}
