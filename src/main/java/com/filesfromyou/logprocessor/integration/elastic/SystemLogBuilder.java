package com.filesfromyou.logprocessor.integration.elastic;

import org.springframework.stereotype.Component;

@Component
public class SystemLogBuilder extends ElasticBuilder {

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        from("{{logprocessor.camel.source.clientsystemlog}}")
                .to("json-validator:systemLogSchema.json")
                .convertBodyTo(byte[].class)
                .to("{{logprocessor.camel.target.clientsystemlog}}");
    }
}
