package com.gwhn.elasticsearch.config;

import com.gwhn.elasticsearch.exception.ESIoException;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;

/**
 * @author banxian
 * @date 2022/2/10 16:50
 */
public class HighLevelClient {

    private static final String CLUSTER_HOSTNAME_PORT = ConfigUtils.getEsClusterDiscoverHostName();
    private static final String ES_USERNAME = ConfigUtils.getEsUserName();
    private static final String ES_PASSWORD = ConfigUtils.getEsPassword();

    public static RestHighLevelClient restHighLevelClient;

    public static RestHighLevelClient getClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(ES_USERNAME, ES_PASSWORD));

        String[] nodes = CLUSTER_HOSTNAME_PORT.split(",");
        HttpHost[] httpHosts = new HttpHost[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            String hostName = StringUtils.substringBeforeLast(nodes[i], ":");
            String port = StringUtils.substringAfterLast(nodes[i], ":");
            httpHosts[i] = new HttpHost(hostName, Integer.valueOf(port));
        }

        RestClientBuilder builder = RestClient.builder(httpHosts);

        builder.setHttpClientConfigCallback(f -> f.setDefaultCredentialsProvider(credentialsProvider));
        restHighLevelClient = new RestHighLevelClient(builder);
        return restHighLevelClient;
    }

    /**
     * 关闭连接
     */
    public static void closeRestHighLevelClient() throws ESIoException {
        if (null != restHighLevelClient) {
            try {
                restHighLevelClient.close();
            } catch (IOException e) {
                throw new ESIoException ("RestHighLevelClient Client close exception", e);
            }
        }
    }
}
