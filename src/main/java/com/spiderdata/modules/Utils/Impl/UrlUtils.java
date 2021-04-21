package com.spiderdata.modules.Utils.Impl;

import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/2/4 22:39
 */

public class UrlUtils {
    private static String url = "https://cdn.sy-valve.com/play/";
    private static String file = "C:\\Users\\qasak\\Documents\\bili\\urls.txt";
    public static void main(String[] args) { //"6019873e9b6bc77ea2391521"
                                            // "6019b53a9b6bc77ea2391521"
        BigInteger start = new BigInteger( "5e9eb173c2a9a83be56edf14", 16);
        for(int i = 0; i < 4096; i++) {
            System.out.println(i + " " + start.toString(16));
            System.out.println(doGet(url + start.toString(16)));
            if(doGet(url + start.toString(16))) {
                FileUtils.appendContentToFile(url + start.toString(16) + "\n", file);
            }
            start = start.add(BigInteger.valueOf(1));
        }
    }
    public static boolean doGet(String url) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            URI uri = builder.build();
            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);
            HttpHost host = null;
            RequestConfig requestConfig = RequestConfig.custom()
                    .setProxy(host)
                    .setConnectTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectionRequestTimeout(3000)
                    .build();
            httpGet.setConfig(requestConfig);


            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                return true;
            } else {
                return false;
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return false;
    }
}
