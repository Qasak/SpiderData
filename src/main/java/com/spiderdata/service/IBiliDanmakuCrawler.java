package com.spiderdata.service;

import java.text.ParseException;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/30 11:13
 */
public interface IBiliDanmakuCrawler {

    /**
     *
     *
     *
     * @param
     * @return
     * */
    public String getHtmlString(String url);

    public String getCid(String BV);

    public String getUploadDate(String htmlContent);

    public String getCurDate();

    public String getName(String htmlContent);

    public String[] getDanMakuURLs(String cid, String fromDate, String toDate) throws ParseException;

    public String getDanmakuContent(String url);

    public void writeContent(String url, String BV, String day, String name);

    public void recordDanmakuStream(int av);

    public void recordDanmakuStream(int av, String fromDate);

    public void recordDanmakuStream(String BV);

    public void recordDanmakuStream(String BV, String fromDate);


    /**
     * 对外暴露接口，记录弹幕信息到数据库
     * */
    public void recordDanmakuStream(String BV, String fromDate, String toDate);

    public void recordDanmakuToFile(int av);

    public void recordDanmakuToFile(int av, String fromDate);

    public void recordDanmakuToFile(String BV);

    public void recordDanmakuToFile(String BV, String fromDate);


    /**
     * 对外暴露接口，记录弹幕信息到xml文件
     * */
    public void recordDanmakuToFile(String BV, String fromDate, String toDate);

    public void biliDanmakuFileToSql(String BV, String fileName);

    public void biliDanmakuStreamToSql(String url, String BV);

    public void xmlToSql(String content, String BV);


    // TODO: 视频标题里某个关键词出现的次数

}
