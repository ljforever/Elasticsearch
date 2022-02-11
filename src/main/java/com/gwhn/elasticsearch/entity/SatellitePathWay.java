package com.gwhn.elasticsearch.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 卫星轨道数据对象
 * @author zhanghp17he@foxmail.com
 * @date 2019/07/11
 * */
@TableName("T_SATELLITE_PATHWAYDATA")
public class SatellitePathWay extends Model<SatellitePathWay> implements Serializable {

    @TableId("satellite_pathwayid")
    private String satellitePathwayId;

    @TableField("satellite_acceptsite")
    private String satelliteAcceptsite;

    @TableField("satellitename")
    private String satelliteName;

    @TableField(value = "entertime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enterTime;

    @TableField(value = "outtime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date outTime;

    @TableField("scan_area")
    private String scanArea;

    @TableField("whether_include_hunan")
    private String whetherIncludeHunan;

    @TableField("tabulationperson")
    private String tabulationPerson;

    @TableField("approveperson")
    private String approvePerson;

    @TableField("addtime")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date addTime;

    public String getSatellitePathwayId() {
        return satellitePathwayId;
    }

    public void setSatellitePathwayId(String satellitePathwayId) {
        this.satellitePathwayId = satellitePathwayId;
    }

    public String getSatelliteAcceptsite() {
        return satelliteAcceptsite;
    }

    public void setSatelliteAcceptsite(String satelliteAcceptsite) {
        this.satelliteAcceptsite = satelliteAcceptsite;
    }

    public String getSatelliteName() {
        return satelliteName;
    }

    public void setSatelliteName(String satelliteName) {
        this.satelliteName = satelliteName;
    }

    public Date getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(Date enterTime) {
        this.enterTime = enterTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public String getScanArea() {
        return scanArea;
    }

    public void setScanArea(String scanArea) {
        this.scanArea = scanArea;
    }

    public String getWhetherIncludeHunan() {
        return whetherIncludeHunan;
    }

    public void setWhetherIncludeHunan(String whetherIncludeHunan) {
        this.whetherIncludeHunan = whetherIncludeHunan;
    }

    public String getTabulationPerson() {
        return tabulationPerson;
    }

    public void setTabulationPerson(String tabulationPerson) {
        this.tabulationPerson = tabulationPerson;
    }

    public String getApprovePerson() {
        return approvePerson;
    }

    public void setApprovePerson(String approvePerson) {
        this.approvePerson = approvePerson;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}
