package com.spiderdata.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.spiderdata.modules.dao.ChinaRegionsMapper;
import com.spiderdata.modules.pojo.ChinaRegions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Wangjs
 * @version 1.0
 * @date 2021/1/25 18:31
 */

@Service
public class ChinaRegionCrawlerImpl {

    @Autowired
    private ChinaRegionsMapper chinaRegionsMapper;

    private static final String URL = "http://preview.www.mca.gov.cn/article/sj/xzqh/2020/2020/202101041104.html";
    public List<ChinaRegions> chinaRegionCrawler() {
        List<ChinaRegions> regionsInfoList = new ArrayList<>();
        Document document = null;
        try {
            document = Jsoup.connect(URL).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element element = document.getElementsByTag("tbody").get(0);
        String provinceCode = "";
        String cityCode = "";
        if(Objects.nonNull(element)) {
            Elements trs = element.getElementsByTag("tr");
            for(int i = 3; i < trs.size(); i++) {
                Elements tds = trs.get(i).getElementsByTag("td");
                if(tds.size() < 3) {
                    continue;
                }
                // code
                Element td1 = tds.get(1);
                // name
                Element td2 = tds.get(2);
                if(td1.text().isEmpty()) {
                    continue;
                }
                ChinaRegions chinaRegions = new ChinaRegions();
                chinaRegions.setCode(td1.text());
                chinaRegions.setName(td2.text());
                if(td1.classNames().contains("xl7032423")) {

                    // 市级
                    if(td2.toString().contains("span")) {
                        chinaRegions.setType((byte) 2);
                        chinaRegions.setParentCode(provinceCode);
                        regionsInfoList.add(chinaRegions);
                        cityCode = td1.text();
                    } else {
                        // 省级
                        chinaRegions.setType((byte) 1);
                        chinaRegions.setParentCode("");
                        regionsInfoList.add(chinaRegions);
                        provinceCode = td1.text();
                    }
                    // 区级
                } else {
                    chinaRegions.setType((byte) 3);
                    chinaRegions.setParentCode(cityCode);
                    regionsInfoList.add(chinaRegions);
                }
            }
        }
        return regionsInfoList;
    }

    public void chinaRegionToSql() {
        List<ChinaRegions> list = chinaRegionCrawler();
        System.out.println(JSONArray.toJSONString(list));
        long idx = 0;
        for(ChinaRegions l : list) {
            Date d = new Date();
            l.setId(idx++);
            l.setIsDelete((byte) 0);
            l.setCreateTime(d);
            l.setUpdateTime(d);
            chinaRegionsMapper.insert(l);
        }
        System.out.println("ok");
    }

    public static void main(String[] args) throws IOException {
        ChinaRegionCrawlerImpl c = new ChinaRegionCrawlerImpl();
        c.chinaRegionToSql();
    }
}
