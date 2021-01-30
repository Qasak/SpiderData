package com.spiderdata.service;

import com.spiderdata.modules.pojo.ChinaRegions;

import java.util.List;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/30 11:13
 */
public interface IChinaRegionCrawler {

    public List<ChinaRegions> chinaRegionCrawler();

    public void chinaRegionToSql();


}
