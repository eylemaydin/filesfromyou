package com.filesfromyou.logprocessor.integration.elastic;

import org.springframework.stereotype.Component;

@Component
public class AppLogBuilder extends ElasticBuilder {

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        from("{{logprocessor.camel.source.clientapplog}}")
                .split(body().tokenize("\n"))
                .streaming()
                .doTry()
                    .to("json-validator:appLogSchema.json")
                    .convertBodyTo(byte[].class)
                    .to("{{logprocessor.camel.target.clientapplog}}")
                .doCatch(Exception.class)
                    .to("{{logprocessor.camel.target.clientapplog.failure}}")
                .end();
    }
}
