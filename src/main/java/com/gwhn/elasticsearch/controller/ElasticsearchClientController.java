package com.gwhn.elasticsearch.controller;

import com.alibaba.fastjson.JSONObject;
import com.gwhn.elasticsearch.entity.SatellitePathWay;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author banxian
 * @date 2022/2/10 14:59
 */
public class ElasticsearchClientController {

    @Autowired
    private RestHighLevelClient client;

    String index = "users";

    /**
     * 创建索引
     *
     * @throws IOException
     */
    public void createIndex() throws IOException {
        CreateIndexRequest indexRequest = new CreateIndexRequest(index);
        CreateIndexResponse response = client.indices()
                .create(indexRequest, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }

    /**
     * 判断索引是否存在
     *
     * @throws IOException
     */
    public void indexExists() throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 新增文档
     */
    public void addDoc() throws IOException {
        IndexRequest indexRequest = new IndexRequest(index);
        SatellitePathWay satellitePathWay = new SatellitePathWay();
        satellitePathWay.setSatellitePathwayId("123");
        satellitePathWay.setSatelliteName("FY3B");
        String source = JSONObject.toJSONString(satellitePathWay);

    }

    public void search() throws IOException {
        SearchRequest request = new SearchRequest(index);
        SearchSourceBuilder builder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder
                .must(new RangeQueryBuilder("age").from(20).to(30))
                .must(new TermQueryBuilder("id", 3));
        builder.query(boolQueryBuilder);
        request.source(builder);
        System.out.println("搜索语句为: " + request.source().toString());
        SearchResponse search = client.search(request, RequestOptions.DEFAULT);
        System.out.println(search);
        SearchHits hits = search.getHits();
        SearchHit[] hitsArr = hits.getHits();
        for (SearchHit documentFields : hitsArr) {
            System.out.println(documentFields.getSourceAsString());
        }
    }

    public void search2() {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.fetchSource(new String[]{"name", "age"}, new String[]{});
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("hobby", "唱歌");
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "逍遥");
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age");
        rangeQueryBuilder.gte(20);
        rangeQueryBuilder.lte(40);
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(matchQueryBuilder);
        boolBuilder.must(termQueryBuilder);
        boolBuilder.must(rangeQueryBuilder);
        sourceBuilder.query(boolBuilder);
        SearchRequest searchRequest = new SearchRequest(index);
        searchRequest.source(sourceBuilder);
        try {
            System.out.println("搜索语句为: " + searchRequest.source().toString());
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(search);
            SearchHits hits = search.getHits();
            SearchHit[] hitsArr = hits.getHits();
            for (SearchHit documentFields : hitsArr) {
                System.out.println(documentFields.getSourceAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
