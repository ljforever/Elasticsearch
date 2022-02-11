package com.gwhn.elasticsearch.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * @author banxian
 * @date 2021/1/21 15:56
 */
public class YereeXmlContext {

    /**
     * 根据一二三级字节点的key，迭代到第三级节点根据三级key获取值
     * <!-- 网络配置-->
     * <module name="Http">
     *    <group name="FileUpload">
     *        <configValue key="TempDir">D:\\temp\\upload\\</configValue>
     *    </group>
     * </module>
     *
     * @param key1
     * @param key2
     * @param key3
     * @return
     * @author banxian
     * @date: 2021/1/21 19:51
     */
    public String getXmlValue(String key1, String key2, String key3) {
        try {
            //1.创建Reader对象
            SAXReader reader = new SAXReader();
            //2.加载xml
            String directoryPath = Thread.currentThread().getContextClassLoader().getResource("config.xml").getPath();//获取项目根目录
            Document document = reader.read(new File(directoryPath));
            //3.获取根节点
            Element rootElement = document.getRootElement();
            Element moduleElement = getTargetElement(key1, rootElement);
            Element groupElement = getTargetElement(key2, moduleElement);
            Element configElement = getTargetElement(key3, groupElement);
            Attribute attribute = (Attribute) configElement.attributes().get(0);
            String key = attribute.getValue();
            Text text3 = (Text) configElement.content().get(0);
            String value = text3.getText();
            return value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取和targetKey匹配的一个子节点
     *
     * @param targetKey
     * @param parentElement
     * @return
     * @author banxian
     * @date: 2021/1/21 19:53
     */
    public Element getTargetElement(String targetKey, Element parentElement) {
        Iterator iterator = parentElement.elementIterator();
        Element childElement = null;
        while (iterator.hasNext()) {
            Element element = (Element) iterator.next();
            Attribute attribute = (Attribute) element.attributes().get(0);
            String key = attribute.getValue();
            if (key.equals(targetKey) || key == targetKey) {
                childElement = element;
                break;
            }
        }
        return childElement;
    }
}
