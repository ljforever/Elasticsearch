package com.gwhn.elasticsearch.service.SatellitePathWay;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gwhn.elasticsearch.entity.SatellitePathWay;

import java.util.List;

/**
 * @author banxian
 * @date 2021/1/14 9:34
 */
public interface SatellitePathWayService {
    public boolean addOrUpdateSatellitePathWay(SatellitePathWay satellitePathWay);

    public boolean addOrUpdateSatellitePathWayList(List<SatellitePathWay> satellitePathWayList);

    public int deleteSatellitePathWay(int id);

    public boolean deleteSatellitePathWayList(List<String> idList) ;

    public IPage<SatellitePathWay> getSatellitePathWayListByPage(SatellitePathWay satellitePathWay,int pageNum, int rows, String sort, String order);

    public List<String> getSatellitePathWayNameList();
}
