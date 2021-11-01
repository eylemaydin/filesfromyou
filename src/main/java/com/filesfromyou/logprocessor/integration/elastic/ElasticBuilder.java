package com.filesfromyou.logprocessor.integration.elastic;


import com.filesfromyou.logprocessor.service.UploadDirectory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ElasticBuilder extends RouteBuilder {

    private final ElasticConfiguration configuration = new ElasticConfiguration();

    public enum Operation {
        INDEX
    }

    @Autowired
    CamelContext camelContext;

    void configureRoute() {
        registerElasticComponent(camelContext);
    }

    public void registerElasticComponent(CamelContext camelContext) {
        ElasticsearchComponent elasticsearchComponent = new ElasticsearchComponent();
        elasticsearchComponent.setHostAddresses(configuration.getHostAddress());

        if (camelContext.hasComponent("elasticsearch-rest") == null) {
            camelContext.addComponent("elasticsearch-rest", elasticsearchComponent);
        }
    }

    String buildElasticUri(Operation operation, String indexName) {
        return "elasticsearch-rest://" +
                this.configuration.getClusterName() +
                "?operation=" + operation +
                "&indexName=" + indexName;
    }

    String buildFileUri(UploadDirectory directory) {
        return "file:" +
                directory.getPath() +
                "?idempotent=true" +
                String.format("&moveFailed=%s", UploadDirectory.ERROR.getPath());
    }
}
