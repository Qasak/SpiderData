package com.spiderdata.modules.controller;

import com.spiderdata.modules.Utils.Impl.BiliUtil;
import com.spiderdata.service.Impl.BiliDanmakuCrawlerImpl;
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
    private BiliDanmakuCrawlerImpl biliDanmakuCrawlerImpl;
    @Autowired
    private BiliUtil biliUtil;
    @GetMapping("/danmaku")
    public String hello(@RequestParam String BV,
                        @RequestParam(required = false, defaultValue = "") String fromDate,
                        @RequestParam(required = false, defaultValue = "") String toDate) {
        biliDanmakuCrawlerImpl.recordDanmakuStream(BV, fromDate, toDate);
        return "danmaku done";
    }
    @GetMapping("/av2bv")
    public String getUid(@RequestParam String av) {
        return BiliUtil.AvToBv(Integer.parseInt(av)).asString();
    }
}
