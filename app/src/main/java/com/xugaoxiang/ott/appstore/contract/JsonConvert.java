package com.xugaoxiang.ott.appstore.contract;

import com.google.gson.Gson;
import com.lzy.okgo.convert.Converter;

import okhttp3.Response;

public class JsonConvert<T> implements Converter<T> {

    private Class<T> clazz;


    public JsonConvert(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public T convertSuccess(Response response) throws Exception {
        String responseData = response.body().string();
        return new Gson().fromJson(responseData, clazz);
    }
}