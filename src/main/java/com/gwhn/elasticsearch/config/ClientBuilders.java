package com.gwhn.elasticsearch.config;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author banxian
 * @date 2022/2/10 16:20
 */
public class ClientBuilders {

    private static final String CLUSTER_HOSTNAME_PORT = ConfigUtils.getEsClusterDiscoverHostName();

    private HttpHost createHttpHost(String ip) {
        return HttpHost.create(ip);
    }

    public RestClientBuilder getBulider() {
        String[] ipHosts = CLUSTER_HOSTNAME_PORT.split(",");
        List<HttpHost> httpHostList = Stream.of(ipHosts).map(this::createHttpHost).collect(Collectors.toList());
        HttpHost[] httpHosts = httpHostList.toArray(new HttpHost[httpHostList.size()]);
        RestClientBuilder builder = RestClient.builder(httpHosts);
        return builder;
    }

    public static RestClientBuilder getBuilder(){
        String[] hostNamePort = CLUSTER_HOSTNAME_PORT.split(",");
        String host;
        int port;
        String[] temp;
        RestClientBuilder restClientBuilder = null;

        if(hostNamePort.length!=0){
            for(String hostPort:hostNamePort){
                temp = hostPort.split(":");
                host = temp[0].trim();
                port = Integer.parseInt(temp[1].trim());
                restClientBuilder = RestClient.builder(new HttpHost(host,port,"http"));
            }
        }


        /*RestClientBuilder 在构建 RestClient 实例时可以设置以下的可选配置参数*/

        /*1.设置请求头，避免每个请求都必须指定*/
        Header[] headers = new Header[]{new BasicHeader("header","value")};
        restClientBuilder.setDefaultHeaders(headers);

         /*2.设置在同一请求进行多次尝试时应该遵守的超时时间。默认值为30秒，与默认`socket`超时相同。
            如果自定义设置了`socket`超时，则应该相应地调整最大重试超时。*/

        //restClientBuilder.setMaxRetryTimeoutMillis(10000);
        /*3.设置每次节点发生故障时收到通知的侦听器。内部嗅探到故障时被启用。*/
        restClientBuilder.setFailureListener(new RestClient.FailureListener());

         /*4.设置修改默认请求配置的回调（例如：请求超时，认证，或者其他
         [org.apache.http.client.config.RequestConfig.Builder](https://hc.apache.org/httpcomponents-client-ga/httpclient/apidocs/org/apache/http/client/config/RequestConfig.Builder.html)
         设置）。
         */
        restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
            @Override
            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder builder) {
                return builder.setSocketTimeout(10000);
            }
        });

        /*5.//设置修改 http 客户端配置的回调（例如：ssl 加密通讯，或其他任何
        [org.apache.http.impl.nio.client.HttpAsyncClientBuilder](http://hc.apache.org/httpcomponents-asyncclient-dev/httpasyncclient/apidocs/org/apache/http/impl/nio/client/HttpAsyncClientBuilder.html)
         设置）*/
        restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
            @Override
            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpAsyncClientBuilder) {
                return httpAsyncClientBuilder.setProxy(new HttpHost("proxy",9000,"http"));
            }
        });
        return restClientBuilder;
    }
}
