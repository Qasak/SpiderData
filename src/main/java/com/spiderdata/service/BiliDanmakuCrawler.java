package com.spiderdata.service;

import com.alibaba.fastjson.JSONObject;
import com.spiderdata.modules.Utils.DateUtil;
import com.spiderdata.modules.Utils.FileUtil;
import com.spiderdata.modules.Utils.HttpClientUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/27 15:02
 */
public class BiliDanmakuCrawler {
    private static String PRE = "https://www.bilibili.com/video/";
    private static String DMApi = "https://api.bilibili.com/x/v2/dm/history?type=1&oid=";
    private static String cookies = "finger=158939783; _uuid=DE8235D8-8550-81BE-ADF3-F698B2DF856536004infoc; buvid3=5B16B126-8FAB-4037-B144-320E8CBDFC6B155826infoc; sid=ij5ha9c8; rpdid=|(um~RJ|l~~m0J'ulm|llJJ~m; LIVE_BUVID=AUTO9215905813485624; blackside_state=1; CURRENT_FNVAL=80; DedeUserID=11689723; DedeUserID__ckMd5=1a2c4273157904b0; SESSDATA=e4e1c1db%2C1622175408%2C16c7d*b1; bili_jct=a15d734323f864cbf1871ea7e1f7263e; fingerprint3=1bb45130b449763996b51220debb436c; buivd_fp=5B16B126-8FAB-4037-B144-320E8CBDFC6B155826infoc; buvid_fp_plain=5B16B126-8FAB-4037-B144-320E8CBDFC6B155826infoc; balh_server_inner=__custom__; balh_is_closed=; CURRENT_QUALITY=116; fingerprint=808a8738b5d287030fb5e53c1f3dfee5; fingerprint_s=d089dc07f74b49e27b28a8a5992ff378; buvid_fp=5B16B126-8FAB-4037-B144-320E8CBDFC6B155826infoc; bp_video_offset_11689723=483132276899750738; bsource=search_google; PVID=1; bfe_id=5112800f2e3d3cf17a473918472e345c";
    private static String UserAgent = "Mozilla/5.0(Windows NT 10.0;WOW64) AppleWebKit/537.36(KHTML,likeGecko)Chrome/63.0.3239.132Safari/537.36";
    private static String addr = "C:\\Users\\qasak\\Documents\\bili\\";

    public String getHtmlString(String url) {
        Connection.Response resp = null;
        try {
            resp = Jsoup.connect(url)
                    .timeout(60000)
                    .method(Connection.Method.GET)
                    .maxBodySize(0)
                    .followRedirects(false)
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(resp.bodyAsBytes());
    }

    public String getCid(String BV) {
        String result = HttpClientUtil.doGet("https://api.bilibili.com/x/player/pagelist?bvid="+BV+"&jsonp=jsonp");
        return JSONObject.parseObject(result).getJSONArray("data").getJSONObject(0).getString("cid");
    }

    public String getUploadDate(String htmlContent) {
        int l = htmlContent.indexOf("\"uploadDate\" content=") + 22;
        int r = l + 10;
        return htmlContent.substring(l, r);
    }
    public String getCurDate() {
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        return dateFormat.format(date);
    }

    public String getName(String htmlContent) {
        String tmp = htmlContent.substring(htmlContent.indexOf("\"og:title\" content=\"") + 20);
        return tmp.substring(0, tmp.indexOf('_'));
    }
    public String[] getDanMakuURLs(String cid, String uploadDate, String endDate) throws ParseException {
        int days = DateUtil.daysBetween(uploadDate, endDate);
        String[] urls = new String[days + 1];
        String nextDay = uploadDate;
        for(int i = 0; i <= days; i++) {
            String URL = DMApi + cid + "&date=" + nextDay;
            urls[i] = URL;
            nextDay = DateUtil.getNextDay(nextDay);
        }
        return urls;
    }

    public static void getContentToFile(String url, String BV, String day, String name) throws Exception{
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault() ;
        HttpGet httpGet1 = new HttpGet(url);
        httpGet1.addHeader("cookie", cookies);
        httpGet1.addHeader("User-Agent", UserAgent);
        CloseableHttpResponse httpResponse1 = closeableHttpClient.execute(httpGet1) ;
        String en = EntityUtils.toString(httpResponse1.getEntity(), "UTF-8");
        String c = "\">(.*?)<" ;
        Pattern a = Pattern.compile(c);
        Matcher m = a.matcher(en);
        String dir = "C:\\Users\\qasak\\Documents\\bili\\" + name + "_" + BV +"\\";
        FileUtil.createDir(dir);
        File file = new File(dir + day + ".txt");
        if(file.exists()){
            file.delete();
        }
        OutputStream fos=new FileOutputStream(dir + "\\" + day + ".txt");
        while(m.find()){
            String speak = m.group().replace("\">","") ;
            speak = speak.replace("<","") ;
            String str=speak;
            str+="\n";
            fos.write(str.getBytes());
        }
    }

    public void recordDanmaku(String BV) {
        String htmlStr = getHtmlString(PRE + BV);
        String cid = getCid(BV);
        String uploadDate = getUploadDate(htmlStr);
        String name = getName(htmlStr);
        String[] urls = null;
        try {
            urls = getDanMakuURLs(cid, uploadDate, getCurDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(String url : urls) {
            String day = url.substring(url.indexOf("date=") + 5);
            System.out.println(url);
            try {
                getContentToFile(url, BV, day, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        BiliDanmakuCrawler b = new BiliDanmakuCrawler();
        String BV = "BV1qs41117pt";
//        System.out.println(b.getCid(BV));
        b.recordDanmaku(BV);
    }
}
