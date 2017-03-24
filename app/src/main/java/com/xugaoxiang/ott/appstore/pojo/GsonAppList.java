package com.xugaoxiang.ott.appstore.pojo;

import java.util.List;

/**
 * 应用列表json解析
 * 包括所有应用，推荐应用，排行版，不同分类的应用
 */
public class GsonAppList {

    /**
     * id : 15
     * appName : 悦跑圈
     * icon_url : /static/images/market/d90752f74d2e6d326e6f6e8332125426.jpg
     * type : 游戏
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String appName;
        private String icon_url;
        private String type;

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
    }
}
