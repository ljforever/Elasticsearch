package com.gwhn.elasticsearch.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * @author banxian
 * @date 2022/2/10 16:12
 */
public class ConfigUtils {

    private static String esConfigFileName = "elasticsearch.properties";

    private static String esClusterName;

    private static String esClusterDiscoverHostName;

    private static String clientTransportSniff;

    private static String esUserName;

    private static String esPassword;

    private static Properties properties = new Properties();

    static {
        try{
            ClassLoader classLoader = ConfigUtils.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream(esConfigFileName);
            properties.load(inputStream);
            init();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    static void init(){
        esUserName = properties.getProperty("elastic.username");
        esPassword = properties.getProperty("elastic.password");
        esClusterName = properties.getProperty("elastic.cluster.name");
        esClusterDiscoverHostName = properties.getProperty("elastic.cluster.discover.hostname");
        clientTransportSniff = properties.getProperty("elastic.cluster.clientTransportSniff");
        if(esClusterName==""||clientTransportSniff.equals("")){
            throw new RuntimeException("elasticsearch 集群参数为空异常!");
        }

        if(esUserName.equals("")||esPassword.equals("")){
            throw new RuntimeException("elasticsearch 集群登录用户名和密码不能为空!");
        }
    }

    public static String getEsConfigFileName() {
        return esConfigFileName;
    }

    public static void setEsConfigFileName(String esConfigFileName) {
        ConfigUtils.esConfigFileName = esConfigFileName;
    }

    public static String getEsClusterName() {
        return esClusterName;
    }

    public static void setEsClusterName(String esClusterName) {
        ConfigUtils.esClusterName = esClusterName;
    }

    public static String getEsClusterDiscoverHostName() {
        return esClusterDiscoverHostName;
    }

    public static void setEsClusterDiscoverHostName(String esClusterDiscoverHostName) {
        ConfigUtils.esClusterDiscoverHostName = esClusterDiscoverHostName;
    }

    public static String getClientTransportSniff() {
        return clientTransportSniff;
    }

    public static void setClientTransportSniff(String clientTransportSniff) {
        ConfigUtils.clientTransportSniff = clientTransportSniff;
    }

    public static String getEsUserName() {
        return esUserName;
    }

    public static void setEsUserName(String esUserName) {
        ConfigUtils.esUserName = esUserName;
    }

    public static String getEsPassword() {
        return esPassword;
    }

    public static void setEsPassword(String esPassword) {
        ConfigUtils.esPassword = esPassword;
    }

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        ConfigUtils.properties = properties;
    }
}
