package com.filesfromyou.logprocessor.integration.elastic;

public class ElasticConfiguration {

    private final String clusterName = "elasticsearch";
    private final String hostAddress = "localhost:9200";

    public String getClusterName() {
        return this.clusterName;
    }

    public String getHostAddress() {
        return this.hostAddress;
    }
}
