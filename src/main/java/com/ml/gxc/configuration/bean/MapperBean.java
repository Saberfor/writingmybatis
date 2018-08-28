package com.ml.gxc.configuration.bean;

import lombok.Data;

@Data
public class MapperBean {

    private String namespace;
    private String id;
    private String type;
    private String resultMap;
    private String sql;

    public MapperBean(String namespace, String id, String type) {
        this.namespace = namespace;
        this.id = id;
        this.type = type;
    }

    public MapperBean(String namespace, String id, String resultMap, String sql) {
        this.namespace = namespace;
        this.id = id;
        this.resultMap = resultMap;
        this.sql = sql;
    }
}
