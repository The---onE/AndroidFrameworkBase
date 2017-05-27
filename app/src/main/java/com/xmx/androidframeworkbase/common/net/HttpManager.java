package com.xmx.androidframeworkbase.common.net;

import android.os.AsyncTask;

import com.xmx.androidframeworkbase.utils.StrUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by The_onE on 2017/5/26.
 * 用于管理HTTP请求，使用OkHttp插件
 */

public class HttpManager {
    private static HttpManager instance;

    public synchronized static HttpManager getInstance() {
        if (null == instance) {
            instance = new HttpManager();
        }
        return instance;
    }

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    public String makeGetRequest(String url, Map<String, String> params) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            list.add(key + "=" + value);
        }
        return url + "?" + StrUtil.join(list, "&");
    }

    public void get(final String url, final Map<String, String> params,
                    final HttpGetCallback callback) {
        // 开启新线程
        new AsyncTask<String, Exception, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    String re = url;
                    if (params != null) {
                        re = makeGetRequest(url, params);
                    }
                    // 生成Get请求
                    Request request = new Request.Builder()
                            .url(re)
                            .build();
                    // 获取Get响应
                    Response response = client.newCall(request).execute();
                    // 返回响应结果
                    return response.body().string();
                } catch (Exception e) {
                    publishProgress(e);
                    return null;
                }
            }

            @Override
            protected void onProgressUpdate(Exception... values) {
                super.onProgressUpdate(values);
                // 请求过程出现异常，请求失败
                callback.fail(values[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    // 请求成功，返回响应结果
                    callback.success(s);
                }
            }
        }.execute();
    }

    public void post(final String url, final String json, final HttpPostCallback callback) {
        // 开启新线程
        new AsyncTask<String, Exception, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    // 生成Post请求
                    RequestBody body = RequestBody.create(JSON, json);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    // 获取Post响应
                    Response response = client.newCall(request).execute();
                    // 返回响应结果
                    return response.body().string();
                } catch (Exception e) {
                    publishProgress(e);
                    return null;
                }
            }

            @Override
            protected void onProgressUpdate(Exception... values) {
                super.onProgressUpdate(values);
                // 请求过程出现异常，请求失败
                callback.fail(values[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s != null) {
                    // 请求成功，返回响应结果
                    callback.success(s);
                }
            }
        }.execute();
    }
}
