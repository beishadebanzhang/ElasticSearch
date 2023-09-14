package com.example.elasticsearch.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.util.Arrays;

/**
 * 文档操作
 */
public class DocTest {
    public static void main(String[] args) throws Exception {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http")));
        addDoc(client);
        editDoc(client);
        getDoc(client);
        delDoc(client);
        client.close();
    }

    /**
     * 新增文档
     */
    public static void addDoc(RestHighLevelClient client) throws Exception {
        IndexRequest request = new IndexRequest();
        request.index("user").id("1001");
        User user = User.builder()
                .name("张三")
                .age(30)
                .sex("男")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String productJson = objectMapper.writeValueAsString(user);
        // 添加文档数据，数据格式为 JSON 格式
        request.source(productJson, XContentType.JSON);
        // 客户端发送请求，获取响应对象
        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
        // 批量操作
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(request);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("took:" + bulkResponse.getTook());
        System.out.println("items:" + Arrays.toString(bulkResponse.getItems()));
    }

    /**
     * 修改文档
     */
    public static void editDoc(RestHighLevelClient client) throws Exception {
        UpdateRequest request = new UpdateRequest();
        request.index("user").id("1001");
        request.doc(XContentType.JSON, "sex", "女");
        // 客户端发送请求，获取响应对象
        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
        System.out.println("_index:" + response.getIndex());
        System.out.println("_id:" + response.getId());
        System.out.println("_result:" + response.getResult());
    }

    /**
     * 查询文档
     */
    public static void getDoc(RestHighLevelClient client) throws Exception {
        GetRequest request = new GetRequest().index("user").id("1001");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println("_index:" + response.getIndex());
        System.out.println("_type:" + response.getType());
        System.out.println("_id:" + response.getId());
        System.out.println("source:" + response.getSourceAsString());
    }

    /**
     * 删除文档
     */
    public static void delDoc(RestHighLevelClient client) throws Exception {
        DeleteRequest request = new DeleteRequest().index("user").id("1001");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.toString());
        // 批量操作
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(request);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        System.out.println("took:" + bulkResponse.getTook());
        System.out.println("items:" + Arrays.toString(bulkResponse.getItems()));
    }
}
