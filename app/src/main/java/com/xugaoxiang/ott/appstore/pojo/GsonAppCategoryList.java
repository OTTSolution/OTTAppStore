package com.xugaoxiang.ott.appstore.pojo;

import java.util.List;

/**
 * 应用分类列表
 */
public class GsonAppCategoryList {

    /**
     * id : 1
     * type_name : 游戏
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
        private String type_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
