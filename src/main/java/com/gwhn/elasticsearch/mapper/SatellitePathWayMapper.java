package com.gwhn.elasticsearch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gwhn.elasticsearch.entity.SatellitePathWay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author banxian
 * @date 2021/1/14 9:33
 */
@Mapper
@Component("satellitePathWayMapper")
public interface SatellitePathWayMapper extends BaseMapper<SatellitePathWay> {

    @Select("select t.SATELLITENAME from t_satellite_pathwaydata t group by t.SATELLITENAME")
    List<String> selectSatellitePathWayName();


}
