package com.spiderdata.modules.pojo;

import java.util.Date;

public class ChinaRegions {
    private Long id;

    private String code;

    private String name;

    private Byte type;

    private String parentCode;

    private Byte isDelete;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public ChinaRegions(Long id, String code, String name, Byte type, String parentCode, Byte isDelete, Date createTime, Date updateTime) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.type = type;
        this.parentCode = parentCode;
        this.isDelete = isDelete;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }
    public ChinaRegions(){}
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}