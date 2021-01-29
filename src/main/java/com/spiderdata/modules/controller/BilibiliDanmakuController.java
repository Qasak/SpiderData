package com.spiderdata.modules.controller;

import com.spiderdata.service.BiliDanmakuCrawler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/29 15:06
 */
@RestController
public class BilibiliDanmakuController {
    private static String file = "C:\\Users\\qasak\\Documents\\bili\\2012-02-25.xml";
    @Autowired
    private BiliDanmakuCrawler biliDanmakuCrawler;
    @GetMapping("/danmaku")
    public String hello(@RequestParam String BV) {
        biliDanmakuCrawler.recordDanmakuStream(BV);
        return "danmaku done";
    }
}
