package com.gwhn.elasticsearch.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gwhn.elasticsearch.entity.SatellitePathWay;
import com.gwhn.elasticsearch.service.SatellitePathWay.SatellitePathWayService;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author banxian
 * @date 2022/2/10 14:03
 */
@RestController
@RequestMapping("/api/satellitepathway")
public class ESSatellitePathWayController {
    @Resource(name = "satellitePathWayServiceImpl")
    private SatellitePathWayService satellitePathWayService;

    @Autowired
    private RestHighLevelClient client;

    String index = "users";

    /**
     * 条件查询排序分页
     *
     * @param satellitePathWay
     * @param page
     * @param order
     * @param rows
     * @param sort
     * @return
     * @author banxian
     * @date: 2021/1/21 14:30
     */
    @ResponseBody
    @RequestMapping(value = "/es/list", method = RequestMethod.POST)
    public IPage<SatellitePathWay> getSatellitePathWayListByPage(SatellitePathWay satellitePathWay, Integer page, Integer rows, String sort, String order) {
        System.out.println("第" + page + "页");
        System.out.println("每页" + rows + "个");
        System.out.println("sort:" + sort);
        System.out.println("order:" + order);
        return satellitePathWayService.getSatellitePathWayListByPage(satellitePathWay, page, rows, sort, order);
    }
}
