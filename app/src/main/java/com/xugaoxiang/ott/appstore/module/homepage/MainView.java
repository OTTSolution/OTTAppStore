package com.xugaoxiang.ott.appstore.module.homepage;

import com.xugaoxiang.ott.appstore.pojo.BeanAppInfo;
import com.xugaoxiang.ott.appstore.pojo.GsonAppCategoryList;
import com.xugaoxiang.ott.appstore.pojo.GsonAppList;
import com.xugaoxiang.ott.appstore.module.base.BaseView;

import java.util.List;

/**
 *MainView的方法
 */
public interface MainView extends BaseView {

    void showAppList(List<GsonAppList.DataBean> applist);
    
    void showAppManager(List<BeanAppInfo> applist);

    void showAppCategory(GsonAppCategoryList gson);

    void hideProgressBar();

}
