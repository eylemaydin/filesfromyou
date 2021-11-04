package com.filesfromyou.logprocessor.integration.elastic;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.elasticsearch.ElasticsearchComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class ElasticBuilder extends RouteBuilder {

    @Autowired
    CamelContext camelContext;

    @Value("${logprocessor.elasticsearch.hostaddress}")
    private String hostAddress;

    void configureRoute() {
        registerElasticComponent(camelContext);
    }

    public void registerElasticComponent(CamelContext camelContext) {
        ElasticsearchComponent elasticsearchComponent = new ElasticsearchComponent();
        elasticsearchComponent.setHostAddresses(this.hostAddress);

        if (!hasElasticComponent()) {
            camelContext.addComponent("elasticsearch-rest", elasticsearchComponent);
        }
    }

    private boolean hasElasticComponent() {
        return camelContext.hasComponent("elasticsearch-rest") != null;
    }

}
