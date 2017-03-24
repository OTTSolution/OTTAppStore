package com.xugaoxiang.ott.appstore.pojo;

import org.litepal.crud.DataSupport;

/**
 * Created by zero on 2016/9/27.
 */

public class OrmPhoto extends DataSupport {

    private String url;

    private OrmApp app;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OrmApp getApp() {
        return app;
    }

    public void setApp(OrmApp app) {
        this.app = app;
    }
}
