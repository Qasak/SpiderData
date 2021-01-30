package com.spiderdata.modules.controller;

import com.spiderdata.service.Impl.ChinaRegionCrawlerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/26 19:33
 */


@RestController
public class ChinaRegionsController {
    @Autowired
    private ChinaRegionCrawlerImpl chinaRegionCrawlerImpl;

    @GetMapping("/chinaregion")
    public String hello() {
        chinaRegionCrawlerImpl.chinaRegionToSql();
        return "chinaregion";
    }

}
