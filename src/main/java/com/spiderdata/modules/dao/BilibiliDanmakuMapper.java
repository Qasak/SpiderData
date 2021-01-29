package com.spiderdata.modules.dao;

import com.spiderdata.modules.pojo.BilibiliDanmaku;

public interface BilibiliDanmakuMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BilibiliDanmaku record);

    int insertSelective(BilibiliDanmaku record);

    BilibiliDanmaku selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BilibiliDanmaku record);

    int updateByPrimaryKey(BilibiliDanmaku record);
}