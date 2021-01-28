package com.spiderdata.modules.controller;

import com.spiderdata.service.ChinaRegionCrawler;
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
    private ChinaRegionCrawler chinaRegionCrawler;

    @GetMapping("/chinaregion")
    public String hello() {
        chinaRegionCrawler.chinaRegionToSql();
        return "chinaregion";
    }

}
