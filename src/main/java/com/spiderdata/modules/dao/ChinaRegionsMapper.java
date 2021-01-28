package com.spiderdata.modules.dao;

import com.spiderdata.modules.pojo.ChinaRegions;

public interface ChinaRegionsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ChinaRegions record);

    int insertSelective(ChinaRegions record);

    ChinaRegions selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChinaRegions record);

    int updateByPrimaryKey(ChinaRegions record);
}