package com.ml.gxc.configuration;

import com.ml.gxc.configuration.bean.DbInfo;
import com.ml.gxc.configuration.bean.MapperBean;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Configuration {

    private DbInfo conf = new DbInfo();

    private Map<String, MapperBean> map = new HashMap<>();

    public Configuration() {
        readConf();
        reacMapper();
    }

    public void readConf(){
        Properties properties = new Properties();
        InputStream is =  this.getClass().getClassLoader().getResourceAsStream("db.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        conf.setDriver(properties.getProperty("jdbc.driver"));
        conf.setUrl(properties.getProperty("jdbc.url"));
        conf.setUsername(properties.getProperty("jdbc.username"));
        conf.setPassword(properties.getProperty("jdbc.password"));
    }

    public void reacMapper() {
        String path = this.getClass().getClassLoader().getResource("").getPath()+"mapping/com/ml/gxc/mapping";
        File file = new File(path);
        String[] fileNames =  file.list();
        Arrays.asList(fileNames).stream().forEach(filename -> readXml(filename, path));
    }

    private void readXml(String filename, String path) {
        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(new File(path + "/" + filename));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        String namespace = root.attributeValue("namespace");
        //resultMap 元素
        List<Element> list = root.elements("resultMap");
        list.stream().forEach(e -> {
           String id =  e.attributeValue("id");
           String type =  e.attributeValue("type");
           MapperBean mapperBean = new MapperBean(namespace, id, type);
           map.put(namespace+id, mapperBean);
        });

        List<Element> list2 = root.elements("select");
        list2.stream().forEach(e -> {
            String id =  e.attributeValue("id");
            String resultMap =  e.attributeValue("resultMap");
            String sql = e.getTextTrim();
            MapperBean bean = new MapperBean(namespace, id, resultMap, sql);
            map.put(namespace+id, bean);
        });

    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        System.out.println(conf.conf.getDriver());
        System.out.println(conf.conf.getUrl());
        System.out.println(conf.conf.getUsername());
        System.out.println(conf.conf.getPassword());
        conf.map.forEach((k,v) -> {
            System.out.println(k);
            System.out.println(v.getId() + " " + v.getNamespace() + " " + v.getResultMap() + " " + v.getSql() + " " + v.getType());
        });
    }
}
