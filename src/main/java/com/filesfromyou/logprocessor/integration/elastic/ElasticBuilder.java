package com.filesfromyou.logprocessor.integration.elastic;

import com.filesfromyou.logprocessor.service.UploadDirectory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class ElasticBuilder extends RouteBuilder {

    public enum Operation {
        INDEX
    }

    @Autowired
    CamelContext camelContext;

    @Value("${elasticsearch.hostaddress}")
    private String hostAddress;

    @Value("${elasticsearch.clustername}")
    private String clusterName;

    void configureRoute() {
        registerElasticComponent(camelContext);
    }

    public void registerElasticComponent(CamelContext camelContext) {
        ElasticsearchComponent elasticsearchComponent = new ElasticsearchComponent();
        elasticsearchComponent.setHostAddresses(this.hostAddress);

        if (camelContext.hasComponent("elasticsearch-rest") == null) {
            camelContext.addComponent("elasticsearch-rest", elasticsearchComponent);
        }
    }

    String buildElasticUri(Operation operation, String indexName) {
        return "elasticsearch-rest://" +
                this.clusterName +
                "?operation=" + operation +
                "&indexName=" + indexName;
    }

    String buildFileUri(UploadDirectory directory) {
        return "file:" +
                directory.getPath() +
                "?idempotent=true" +
                "&moveFailed=error";
    }
}
