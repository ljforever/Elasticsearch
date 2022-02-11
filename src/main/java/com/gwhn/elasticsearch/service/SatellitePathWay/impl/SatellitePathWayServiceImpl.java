package com.gwhn.elasticsearch.service.SatellitePathWay.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gwhn.elasticsearch.entity.SatellitePathWay;
import com.gwhn.elasticsearch.mapper.SatellitePathWayMapper;
import com.gwhn.elasticsearch.service.SatellitePathWay.SatellitePathWayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 卫星轨道报实现类
 * @author banxian
 * @date 2021/1/14 9:35
 */
@Service
public class SatellitePathWayServiceImpl extends ServiceImpl<SatellitePathWayMapper, SatellitePathWay> implements SatellitePathWayService {

    @Resource(name = "satellitePathWayMapper")
    private SatellitePathWayMapper satellitePathWayMapper;

    @Transactional
    @Override
    public boolean addOrUpdateSatellitePathWay(SatellitePathWay satellitePathWay) {
        return satellitePathWay.insertOrUpdate();
    }

    @Transactional
    @Override
    public boolean addOrUpdateSatellitePathWayList(List<SatellitePathWay> satellitePathWayList) {
        return saveOrUpdateBatch(satellitePathWayList);
    }

    @Transactional
    @Override
    public int deleteSatellitePathWay(int id) {
        return satellitePathWayMapper.deleteById(id);
    }

    @Transactional
    @Override
    public boolean deleteSatellitePathWayList(List<String> idList) {
      try {
          return removeByIds(idList);
      }catch (Exception e){
          return false;
      }
    }

    /**
     * 根据卫星名、是否过境湖南、出入境时间进行分页查询并排序
     * @param satellitePathWay
     * @param sort
     * @param order
     * @param rows
     * @param page
     * @return
     * @author banxian
     * @date: 2021/1/14 15:49
     */
    @Transactional
    @Override
    public IPage<SatellitePathWay> getSatellitePathWayListByPage(SatellitePathWay satellitePathWay, int page, int rows, String sort, String order) {
        IPage<SatellitePathWay> allPage = new Page<>(page, rows);
        QueryWrapper<SatellitePathWay> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(satellitePathWay.getSatelliteName())) {
            queryWrapper.like("satellitename", satellitePathWay.getSatelliteName());
        }
        if (StringUtils.isNotBlank(satellitePathWay.getWhetherIncludeHunan())) {
            System.out.println(satellitePathWay.getWhetherIncludeHunan());
            queryWrapper.like("whether_include_hunan", satellitePathWay.getWhetherIncludeHunan());
        }
        if (satellitePathWay.getEnterTime() != null) {
            System.out.println(satellitePathWay.getEnterTime());
            queryWrapper.ge("entertime", satellitePathWay.getEnterTime());
        }
        if (satellitePathWay.getOutTime() != null) {
            System.out.println(satellitePathWay.getOutTime());
            queryWrapper.le("outtime",satellitePathWay.getOutTime());
        }
        if (sort != null && order != null) {
            if (order.equals("asc")) {
                queryWrapper.orderBy(true, true, com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(sort));
            }
            if (order.equals("desc")) {
                queryWrapper.orderBy(true, false, com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline(sort));
            }
        } else {
            queryWrapper.orderBy(true, false, com.baomidou.mybatisplus.core.toolkit.StringUtils.camelToUnderline("addtime"));
        }
        allPage = page(allPage,queryWrapper);
        return allPage;
    }

    @Override
    @Transactional
    public List<String> getSatellitePathWayNameList() {
        return satellitePathWayMapper.selectSatellitePathWayName();
    }


}
