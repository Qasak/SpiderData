package com.spiderdata.service;

import com.alibaba.fastjson.JSONObject;
import com.spiderdata.modules.Utils.DateUtil;
import com.spiderdata.modules.Utils.FileUtil;
import com.spiderdata.modules.Utils.HttpClientUtil;
import com.spiderdata.modules.Utils.YmlUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.python.antlr.ast.Str;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/27 15:02
 */
public class BiliDanmakuCrawler {
    private static Map<String, String> map = YmlUtil.getYmlByFileName("biliconfig.yml");
    private static final String PRE = map.get("url.pre");
    private static final String DM_API = map.get("url.DMApi");
    private static final String COOKIES = map.get("header.cookies");
    private static final String USER_AGENT = map.get("header.UserAgent");
    private static final String ADDR = map.get("dir.addr");
    private static final String PROXY_IP = map.get("proxy.ip");
    private static final String PROXY_PORT = map.get("proxy.port");

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
    public String[] getDanMakuURLs(String cid, String fromDate, String toDate) throws ParseException {
        int days = DateUtil.daysBetween(fromDate, toDate);
        String[] urls = new String[days + 1];
        String nextDay = fromDate;
        for(int i = 0; i <= days; i++) {
            String URL = DM_API + cid + "&date=" + nextDay;
            urls[i] = URL;
            nextDay = DateUtil.getNextDay(nextDay);
        }
        return urls;
    }
    // <d p="弹幕出现时间,模式,字体大小,颜色,发送时间戳,弹幕池,用户Hash,数据库ID">123123</d>
    // <d p="0.13400,1,25,16777215,1442243493,0,a668adff,1210303425">我是欧洲人A路人</d>
    public static void writeContentToFile(String url, String BV, String day, String name) throws Exception{
        Map<String, String> map = new HashMap<>();
        map.put("cookie", COOKIES);
        map.put("User-Agent", USER_AGENT);
        String[] proxy = null;
        if(PROXY_IP != null && PROXY_PORT != null) {
            proxy = new String[]{PROXY_IP, PROXY_PORT};
        }
        String en = HttpClientUtil.doGet(url, map, proxy);
        String c = "\">(.*?)<" ;
        Pattern a = Pattern.compile(c);
        Matcher m = a.matcher(en);
        String dir = ADDR + name + "_" + BV +"\\";
        FileUtil.createDir(dir);
        File file = new File(dir + day + ".xml");
        if(file.exists()){
            file.delete();
        }
        OutputStream fos=new FileOutputStream(dir + "\\" + day + ".xml");
//        while(m.find()){
//            String speak = m.group().replace("\">","") ;
//            speak = speak.replace("<","") ;
//            String str=speak;
//            str+="\n";
//            fos.write(str.getBytes());
//        }
        fos.write(en.getBytes());
    }
    public void recordDanmaku(String BV) {
        recordDanmaku(BV, null, null);
    }
    public void recordDanmaku(String BV, String fromDate) {
        recordDanmaku(BV, fromDate, null);
    }
    public void recordDanmaku(String BV, String fromDate, String toDate) {
        String htmlStr = getHtmlString(PRE + BV);
        String cid = getCid(BV);
        if(fromDate == null && toDate == null) {
            fromDate = getUploadDate(htmlStr);
            toDate = getCurDate();
        } else if(toDate == null) {
            toDate = getCurDate();
        }
        String name = getName(htmlStr);
        String[] urls = null;
        try {
            urls = getDanMakuURLs(cid, fromDate, toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(String url : urls) {

            String day = url.substring(url.indexOf("date=") + 5);
            System.out.println(url);
            try {
                writeContentToFile(url, BV, day, name);
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
        b.recordDanmaku(BV, "2017-10-24");
    }
}
