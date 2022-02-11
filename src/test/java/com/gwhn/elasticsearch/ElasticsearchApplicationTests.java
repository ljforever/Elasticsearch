package com.gwhn.elasticsearch;

import com.gwhn.elasticsearch.config.HighLevelClient;
import com.gwhn.elasticsearch.config.HighLevelClientAPI;
import com.gwhn.elasticsearch.entity.UserBean;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SpringBootTest
class ElasticsearchApplicationTests {
    /**
     * 多线程测试HighLevelClient连接
     */
    @Test
    public void testRestClient() {

        //System.out.println(TransportClientBuild.getClient());
        System.setProperty("es.set.netty.runtime.available.processors", "false");

        ExecutorService executorService = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        for (int i = 0; i < 10; i++) {
            int index = i;

            if (!executorService.isShutdown()) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(("第" + index + "次获取到了连接对象————地址:" + HighLevelClient.getClient()));
                    }
                });
            }

        }
        executorService.shutdown();

        //关闭线程池
        try {
            while (!executorService.awaitTermination(10000, TimeUnit.MILLISECONDS)) {
                System.out.println("10秒没有执行完，强制关闭线程池");
                executorService.shutdownNow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * es 新增文档
     */
    @Test
    public void testAddDoc() {
        HighLevelClientAPI es = new HighLevelClientAPI();
        Map jsonMap = new HashMap<String, Object>();
        jsonMap.put("name", "张三11");
        jsonMap.put("age", 33);
        jsonMap.put("addr", "浦东 上海11");
        jsonMap.put("message", "胜多负少的 message 多少分的13");
        try {
            System.out.println("====11111===========");
            es.addDoc(jsonMap, "intr_index", "1");
            System.out.println("====22222===========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新文档
     */
    @Test
    public void testUpdateDoc() {
        HighLevelClientAPI es = new HighLevelClientAPI();
        Map jsonMap = new HashMap<String, Object>();
        jsonMap.put("name", "张三22");
        jsonMap.put("age", 11);
        jsonMap.put("addr", "上海浦东22");
        jsonMap.put("message", "胜多负少的 message 22 多少分的22");
        try {
            System.out.println("====11111===========");
            es.updateDoc(jsonMap, "intr_index", "1");
            System.out.println("====22222===========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 批量增删改查文档
     */
    @Test
    public void testBulkDoc() {
        List<UserBean> ls_add = new ArrayList<UserBean>();
        List<UserBean> ls_up = new ArrayList<UserBean>();
        List<UserBean> ls_del = new ArrayList<UserBean>();

        ls_add.add(new UserBean("1", "lisi88", 88, "上海闵行88", "message 第三方三的订单88"));
        ls_add.add(new UserBean("2", "lisi9", 99, "上海闵行88", "message 第三方三的订单88"));
        //ls_add.add(new UserBean("6", "lisi6", 26, "上海闵行6", "message 第三方三的订单6"));

        ls_up.add(new UserBean("3", "lisi2", 11, "上海闵行11", "message 第三方三的订单11"));
        ls_up.add(new UserBean("4", "lisi4", 25, "上海闵行4", "message 第三方三的订单4"));

        ls_del.add(new UserBean("4"));
        ls_del.add(new UserBean("5"));

        HighLevelClientAPI es = new HighLevelClientAPI();

        try {
            System.out.println("====testBulkDoc 11111===========");
            es.bulkDoc("intr_index", ls_add, ls_up, ls_del);
            System.out.println("====testBulkDoc 22222===========");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据id查询文档
     */
    @Test
    public void testGetDocById() {
        try {
            HighLevelClientAPI es = new HighLevelClientAPI();
            Map<String, Object> map = es.getDocById("intr_index", "2");

            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                Object mapValue = entry.getValue();
                System.out.println(mapKey + "===:===" + mapValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更加字段名和字段值查询文档，包含分页操作
     */
    @Test
    public void testSearchMatch() {
        try {
            HighLevelClientAPI es = new HighLevelClientAPI();
            List<UserBean> ls = es.searchMatch("intr_index", "name", "张三13", 0, 10);
            for (int i = 0; i < ls.size(); i++) {
                UserBean u = ls.get(i);
                System.out.println("testSearchMatch===" + u.getId() + "==" + u.getName() + "=" + u.getAge() + "==" + u.getAddr() + "==" + u.getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
