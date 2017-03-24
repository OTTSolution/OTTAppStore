package com.xugaoxiang.ott.appstore.module.homepage;

import android.content.Context;

/**
 * 主页面的Presenter
 */
public class MainPresenter {

    private MainView mView;
    private MainModel mModel;


    public MainPresenter(MainView mainView) {
        mView = mainView;
        mModel = new MainModel();
    }

    public void getRecommendedAppList() {
        mModel.getRecommendedAppList(mView);
    }

    public void getAllAppList() {
        mModel.getAllAppList(mView);
    }

    public void getInstalledApp(Context context, int filter) {
        mModel.getInstalledApp(context, filter, mView);
    }



    /*public void getData(){
        Observable<GsonAppList> recommendedApp = mRequestMethod.recommendedApp();
        Observable<GsonAppList> appRankList = mRequestMethod.appRankList();
        Observable<GsonAppCategoryList> appCategoryList = mRequestMethod.appCategoryList();
        Observable.zip(recommendedApp, appRankList, appCategoryList, new Func3<GsonAppList, GsonAppList, GsonAppCategoryList, ZipBean>() {
            @Override
            public ZipBean call(RecommendedAppGson recommendedApp, AppRankListGson appRankList, GsonAppCategoryList appCategoryList) {
                ZipBean zipBean = new ZipBean(recommendedApp, appRankList, appCategoryList);
                return null;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZipBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ZipBean zipBean) {

                    }
                });
    }

    class ZipBean{
        public RecommendedAppGson recommendedApp;
        public AppRankListGson appRankList;
        public GsonAppCategoryList appCategoryList;

        public ZipBean(RecommendedAppGson recommendedApp, AppRankListGson appRankList, GsonAppCategoryList appCategoryList) {
            this.recommendedApp = recommendedApp;
            this.appRankList = appRankList;
            this.appCategoryList = appCategoryList;
        }
    }*/
}
