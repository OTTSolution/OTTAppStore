package com.xugaoxiang.ott.appstore.module.homepage;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.RemoteException;

import com.xugaoxiang.ott.appstore.constant.Constants;
import com.xugaoxiang.ott.appstore.contract.JsonConvert;
import com.xugaoxiang.ott.appstore.module.base.BaseModel;
import com.xugaoxiang.ott.appstore.pojo.BeanAppInfo;
import com.xugaoxiang.ott.appstore.pojo.GsonAppList;
import com.xugaoxiang.ott.appstore.util.ApkManagerUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okrx.RxAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zero on 2016/9/19.
 */
public class MainModel extends BaseModel {

    private PackageManager pm;

    public void getRecommendedAppList(final MainView mView) {
        OkGo.get(Constants.BASE_URL + Constants.URL_RECOMMEND_APP)
                .getCall(new JsonConvert<>(GsonAppList.class), RxAdapter.<GsonAppList>create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GsonAppList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                        mView.showToast("网络错误");
                    }

                    @Override
                    public void onNext(GsonAppList gson) {
                        mView.hideProgressBar();
                        List<GsonAppList.DataBean> applist = new ArrayList<>();
                        if (gson != null) {
                            applist = gson.getData();
                            mView.showAppList(applist);
                        }
                        if (applist.size() < 1)
                            mView.showToast("暂无数据");
                    }
                });
    }

    public void getAllAppList(final MainView mView) {
        OkGo.get(Constants.BASE_URL + Constants.URL_ALL_APP)
                .getCall(new JsonConvert<>(GsonAppList.class), RxAdapter.<GsonAppList>create())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GsonAppList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                        mView.showToast("网络错误");
                    }

                    @Override
                    public void onNext(GsonAppList gson) {
                        mView.hideProgressBar();
                        List<GsonAppList.DataBean> applist = new ArrayList<>();
                        if (gson != null) {
                            applist = gson.getData();
                            mView.showAppList(applist);
                        }
                        if (applist.size() < 1)
                            mView.showToast("暂无数据");
                    }
                });
    }

    @SuppressWarnings("WrongConstant")
    public void getInstalledApp(Context context, int filter, final MainView mView) {
        pm = context.getPackageManager();
        List<PackageInfo> appList = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        final List<BeanAppInfo> appInfoList = new ArrayList<>();
        switch (filter) {
            case HomoPageFragment.FILTER_ALL_APP:
                for (PackageInfo app : appList) {
                    if (ApkManagerUtil.AppIsNeedToHide(app.packageName))
                        continue;
                    appInfoList.add(getAppInfo(app));
                }

                break;
            case HomoPageFragment.FILTER_SYSTEM_APP:
                for (PackageInfo app : appList) {
                    if ((app.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
                        appInfoList.add(getAppInfo(app));
                }
                break;
            case HomoPageFragment.FILTER_THIRD_APP:
                for (PackageInfo app : appList) {
                    if (ApkManagerUtil.AppIsNeedToHide(app.packageName))
                        continue;
                    //非系统程序
                    if ((app.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
                        appInfoList.add(getAppInfo(app));
                        //本来是系统程序，被用户手动更新后，该系统程序也成为第三方应用程序了
                    else if ((app.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)
                        appInfoList.add(getAppInfo(app));
                }
                break;
            case HomoPageFragment.FILTER_SDCARD_APP:
                for (PackageInfo app : appList) {
                    if (ApkManagerUtil.AppIsNeedToHide(app.packageName))
                        continue;
                    if ((app.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0)
                        appInfoList.add(getAppInfo(app));
                }
            default:
                break;
        }
        Observable.just(appInfoList)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<BeanAppInfo>, Observable<BeanAppInfo>>() {
                    @Override
                    public Observable<BeanAppInfo> call(List<BeanAppInfo> appInfos) {
                        BeanAppInfo[] appArray = appInfos.toArray(new BeanAppInfo[appInfos.size()]);
                        return Observable.from(appArray);
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<BeanAppInfo, BeanAppInfo>() {
                    @Override
                    public BeanAppInfo call(BeanAppInfo appInfo) {
                        queryPacakgeSize(appInfo.getPackageName(), appInfo);
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BeanAppInfo>() {
                    @Override
                    public void onCompleted() {
                        mView.hideProgressBar();
                        mView.showAppManager(appInfoList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideProgressBar();
                    }

                    @Override
                    public void onNext(BeanAppInfo appInfo) {

                    }
                });

    }

    private BeanAppInfo getAppInfo(PackageInfo info) {
        BeanAppInfo appInfo = new BeanAppInfo();
        appInfo.setAppIcon(info.applicationInfo.loadIcon(pm));
        appInfo.setAppName(info.applicationInfo.loadLabel(pm) + "");
        appInfo.setAppVersion(info.versionName);
        appInfo.setPackageName(info.packageName);
        return appInfo;
    }

    /**
     * 查询某个app的大小
     * @param pkgName
     * @throws Exception
     */
    private void queryPacakgeSize(String pkgName, final BeanAppInfo appInfo) {
        if (pkgName != null) {
            try {
                //通过反射机制获得该隐藏函数
                Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
                //调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
                getPackageSizeInfo.invoke(pm, pkgName,new IPackageStatsObserver.Stub(){
                    @Override
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        appInfo.setCacheSize(pStats.cacheSize);
                        appInfo.setDateSize(pStats.dataSize);
                        appInfo.setAppSize(pStats.codeSize);
                        appInfo.setTotalSize(pStats.cacheSize + pStats.dataSize + pStats.codeSize);
                    }
                });
            }
            catch(Exception ex){
                ex.printStackTrace() ;
            }
        }
    }
}
