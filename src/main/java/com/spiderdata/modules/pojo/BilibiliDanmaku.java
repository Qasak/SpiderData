package com.spiderdata.modules.pojo;

import java.util.Date;

public class BilibiliDanmaku {
    private Long id;

    private String bv;

    private Long biliDbId;

    private Date sendTime;

    private String content;

    private String userHash;

    private Float appearTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBv() {
        return bv;
    }

    public void setBv(String bv) {
        this.bv = bv;
    }

    public Long getBiliDbId() {
        return biliDbId;
    }

    public void setBiliDbId(Long biliDbId) {
        this.biliDbId = biliDbId;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserHash() {
        return userHash;
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public Float getAppearTime() {
        return appearTime;
    }

    public BilibiliDanmaku(Long id, String bv, Long biliDbId, Date sendTime, String content, String userHash, Float appearTime) {
        this.id = id;
        this.bv = bv;
        this.biliDbId = biliDbId;
        this.sendTime = sendTime;
        this.content = content;
        this.userHash = userHash;
        this.appearTime = appearTime;
    }

    public void setAppearTime(Float appearTime) {
        this.appearTime = appearTime;
    }
}