package com.spiderdata.service.Impl;

import com.alibaba.fastjson.JSONObject;
import com.spiderdata.modules.Utils.*;
import com.spiderdata.modules.dao.BilibiliDanmakuMapper;
import com.spiderdata.modules.pojo.BilibiliDanmaku;
import com.spiderdata.service.IBiliDanmakuCrawler;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
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

@Service
public class BiliDanmakuCrawlerImpl implements IBiliDanmakuCrawler {
    @Autowired
    private BilibiliDanmakuMapper bilibiliDanmakuMapper;
    private static Map<String, String> map = YmlUtil.getYmlByFileName("biliconfig.yml");
    private static final String PRE = map.get("url.pre");
    private static final String DM_API = map.get("url.DMApi");
    private static final String COOKIES = map.get("header.cookies");
    private static final String USER_AGENT = map.get("header.UserAgent");
    private static final String ADDR = map.get("dir.addr");
    private static final String PROXY_IP = map.get("proxy.ip");
    private static final String PROXY_PORT = map.get("proxy.port");

    @Override
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


    @Override
    public String getCid(String BV) {
        String result = HttpClientUtil.doGet("https://api.bilibili.com/x/player/pagelist?bvid="+BV+"&jsonp=jsonp");
        return JSONObject.parseObject(result).getJSONArray("data").getJSONObject(0).getString("cid");
    }


    @Override
    public String getUploadDate(String htmlContent) {
        int l = htmlContent.indexOf("\"uploadDate\" content=") + 22;
        int r = l + 10;
        return htmlContent.substring(l, r);
    }

    @Override
    public String getCurDate() {
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    @Override
    public String getName(String htmlContent) {
        String tmp = htmlContent.substring(htmlContent.indexOf("\"og:title\" content=\"") + 20);
        return tmp.substring(0, tmp.indexOf('_'));
    }

    @Override
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

    @Override
    public String getDanmakuContent(String url) {
        Map<String, String> map = new HashMap<>();
        map.put("cookie", COOKIES);
        map.put("User-Agent", USER_AGENT);
        String[] proxy = null;
        if(PROXY_IP != null && PROXY_PORT != null) {
            proxy = new String[]{PROXY_IP, PROXY_PORT};
        }
        return HttpClientUtil.doGet(url, map, proxy);
    }


    // <d p="弹幕出现时间,模式,字体大小,颜色,发送时间戳,弹幕池,用户Hash,数据库ID">123123</d>
    // <d p="0.13400,1,25,16777215,1442243493,0,a668adff,1210303425">我是欧洲人A路人</d>

    @Override
    public void writeContent(String url, String BV, String day, String name){
        String content = getDanmakuContent(url);
        String dir = ADDR + name + "_" + BV +"\\";
        String filePath = dir + day + ".xml";
        FileUtil.createDir(dir);
        FileUtil.createFile(filePath);
        FileUtil.writeContentToFile(content, filePath);
    }

    @Override
    public void recordDanmakuStream(int av) {
        String BV = BiliUtil.AvToBv(av).asString();
        recordDanmakuStream(BV, "", "");
    }
    @Override
    public void recordDanmakuStream(int av, String fromDate) {
        String BV = BiliUtil.AvToBv(av).asString();
        recordDanmakuStream(BV, fromDate, "");
    }
    @Override
    public void recordDanmakuStream(String BV) {
        recordDanmakuStream(BV, "", "");
    }

    @Override
    public void recordDanmakuStream(String BV, String fromDate) {
        recordDanmakuStream(BV, fromDate, "");
    }

    @Override
    public void recordDanmakuStream(String BV, String fromDate, String toDate) {
        String cid = getCid(BV);
        int n = fromDate.length();
        int m = toDate.length();
        System.out.println(fromDate);
        if((n == 0 && m == 0)) {
            String htmlStr = getHtmlString(PRE + BV);
            fromDate = getUploadDate(htmlStr);
            toDate = getCurDate();
        } else if(m == 0) {
            toDate = getCurDate();
        }
        String[] urls = null;
        try {
            urls = getDanMakuURLs(cid, fromDate, toDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(String url : urls) {
            try {
                biliDanmakuStreamToSql(url, BV);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void recordDanmakuToFile(int av) {
        String BV = BiliUtil.AvToBv(av).asString();
        recordDanmakuToFile(BV, null, null);
    }
    @Override
    public void recordDanmakuToFile(int av, String fromDate) {
        String BV = BiliUtil.AvToBv(av).asString();
        recordDanmakuToFile(BV, fromDate, null);
    }
    @Override
    public void recordDanmakuToFile(String BV) {
        recordDanmakuToFile(BV, null, null);
    }

    @Override
    public void recordDanmakuToFile(String BV, String fromDate) {
        recordDanmakuToFile(BV, fromDate, null);
    }

    @Override
    public void recordDanmakuToFile(String BV, String fromDate, String toDate) {
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
                writeContent(url, BV, day, name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void biliDanmakuFileToSql(String BV, String fileName) {
        String content = null;
        try {
            content = XmlUtil.getAll(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        xmlToSql(content, BV);
    }

    @Override
    public void biliDanmakuStreamToSql(String url, String BV) {
        String content = getDanmakuContent(url);
        xmlToSql(content, BV);
    }

    @Override
    public void xmlToSql(String content, String BV) {
        String c = "\"(.*?)</d" ;
        Pattern a = Pattern.compile(c);
        Matcher m = a.matcher(content);
        while(m.find()) {
            String line = m.group();
            line = line.replace("</d", "");
            line = line.replace("\"", "");

            String[] str = line.split(">");
            // 1.0 encoding=UTF-8? 跳过这19个字符
            if (str[0].length() == 19) {
                continue;
            }
            String[] p = str[0].split(",");
            long biliDbId = Long.parseLong(p[7]);
            Date sendTime = new Date(Long.parseLong(p[4]) * 1000);
            String danmaku = str[1];
            String userHash = p[6];
            float appearTime = Float.parseFloat(p[0]);
            System.out.println(biliDbId + " " + sendTime + " " + danmaku + " " + userHash + " " + appearTime);
            BilibiliDanmaku b = new BilibiliDanmaku(
                    null,
                    BV,
                    biliDbId,
                    sendTime,
                    danmaku,
                    userHash,
                    appearTime);
            bilibiliDanmakuMapper.insert(b);
        }
    }


    public static void main(String[] args) {
        String url = "https://www.bilibili.com/video/BV1qt411j7fV";
        BiliDanmakuCrawlerImpl b = new BiliDanmakuCrawlerImpl();
        b.recordDanmakuStream("BV1sU4y1s7bs");
//        FileUtil.createFile();

    }
}
