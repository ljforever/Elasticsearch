package com.gwhn.elasticsearch.config;

import com.alibaba.fastjson.JSONObject;
import com.gwhn.elasticsearch.config.HighLevelClient;
import com.gwhn.elasticsearch.entity.UserBean;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.xcontent.XContentType;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HighLevelClientAPI {

    RestHighLevelClient highLevelClient = HighLevelClient.getClient();

    /**
     * 根据索引名删除索引
     *
     * @param indexName 索引名
     * @return
     * @throws IOException
     */
    public boolean deleteIndex(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse respone = highLevelClient.indices().delete(request, RequestOptions.DEFAULT);
        boolean isSuccess = respone.isAcknowledged();
        return isSuccess;
    }

    /**
     * 判断索引是否存在
     *
     * @param indexName 索引名
     * @return
     * @throws IOException
     */
    public boolean existsIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        boolean exists = highLevelClient.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }

    /**
     * 新增文档，如果返回结果为CREATED，新增文档，如果返回结果是UPDATED，更新文档
     *
     * @param jsonMap   索引字段key和value
     * @param indexName 索引名名称
     * @param rowId     文档id，指定生成的文档id，如果为空，es会自动生成id
     * @throws IOException
     */
    public String addDoc(Map<String, Object> jsonMap, String indexName, String rowId) throws IOException {
        IndexRequest indexRequest = new IndexRequest(indexName).id(rowId).source(jsonMap);
        IndexResponse response = highLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        return result.toString();
    }

    /**
     * 根据文档id，删除文档，如果返回结果deleted，删除成功，如果返回结果是not_found，文档不存在，删除失败
     *
     * @param indexName 索引名
     * @param id        文档id
     * @throws IOException
     */
    public String deleteDoc(String indexName, String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(indexName, id);
        DeleteResponse response = highLevelClient.delete(deleteRequest, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        return result.toString();
    }

    /**
     * 根据文档id，更新文档，如果返回结果为UPDATED，更新成功，否则更新失败
     *
     * @param jsonMap   待更新的文档信息
     * @param indexName 索引名
     * @param rowId     索引id
     * @throws IOException
     */
    public String updateDoc(Map<String, Object> jsonMap, String indexName, String rowId) throws IOException {
        UpdateRequest request = new UpdateRequest(indexName, rowId).doc(jsonMap);
        UpdateResponse response = highLevelClient.update(request, RequestOptions.DEFAULT);
        DocWriteResponse.Result result = response.getResult();
        System.out.println("=====" + result.toString());
        return result.toString();
    }

    /**
     * 批量操作,如果返回结果为SUCCESS，则全部记录操作成功，否则至少一条记录操作失败,并返回失败的日志
     *
     * @param indexName 索引名称
     * @param add       新增文档
     * @param up        修改文档
     * @param del       删除文档
     * @throws IOException
     */
    public String bulkDoc(String indexName, List<UserBean> add, List<UserBean> up, List<UserBean> del) throws IOException {
        BulkRequest request = new BulkRequest();
        for (UserBean user : add) {
            request.add(new IndexRequest(indexName).id(user.getId())
                    .source(XContentType.JSON, "name", user.getName(), "age", user.getAge(), "addr", user.getAddr(), "message", user.getMessage()));
        }

        for (UserBean user : up) {
            request.add(new UpdateRequest(indexName, user.getId())
                    .doc(XContentType.JSON, "name", user.getName(), "age", user.getAge(), "addr", user.getAddr(), "message", user.getMessage()));
        }

        for (UserBean user : del) {
            request.add(new DeleteRequest(indexName, user.getId()));
        }
        BulkResponse bulkResponse = highLevelClient.bulk(request, RequestOptions.DEFAULT);

        // 如果至少有一个操作失败，此方法返回true
        if (bulkResponse.hasFailures()) {
            StringBuffer sb = new StringBuffer("");
            for (BulkItemResponse bulkItemResponse : bulkResponse) {
                //指示给定操作是否失败
                if (bulkItemResponse.isFailed()) {
                    //检索失败操作的失败
                    BulkItemResponse.Failure failure = bulkItemResponse.getFailure();
                    sb.append(failure.toString()).append("\n");
                }
            }
            System.out.println("=bulk error="+sb.toString());
            return sb.toString();
        } else {
            return "SUCCESS";
        }
    }

    /**
     * 根据索引名和文档id，查询文档数据
     *
     * @param indexName 索引名
     * @param rowId     文档id
     * @return 文档属性数据
     * @throws IOException
     */
    public Map<String, Object> getDocById(String indexName, String rowId) throws IOException {
        GetRequest getRequest = new GetRequest(indexName, rowId);
        GetResponse response = highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> map = response.getSource();
        map.put("id", rowId);
        return map;
    }

    /**
     * 分页match查询
     *
     * @param indexName 索引名称
     * @param fieldName 查询字段名
     * @param fileValue 查询字段值
     * @param startPage 开始页面，从零开始
     * @param maxSize   每页最大记录数
     * @return
     * @throws IOException
     */
    public List<UserBean> searchMatch(String indexName, String fieldName, String fileValue, int startPage, int maxSize) throws IOException {
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchQuery(fieldName, fileValue));
        searchSourceBuilder.from(startPage);
        searchSourceBuilder.size(maxSize);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = highLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //System.out.println("=11=" + JSONObject.toJSON(response));
        SearchHit[] hits = response.getHits().getHits();
        List<UserBean> userList = new LinkedList<>();
        for (SearchHit hit : hits) {
            UserBean user = JSONObject.parseObject(hit.getSourceAsString(), UserBean.class);
            user.setId(hit.getId());
            userList.add(user);
        }
        return userList;
    }
}
