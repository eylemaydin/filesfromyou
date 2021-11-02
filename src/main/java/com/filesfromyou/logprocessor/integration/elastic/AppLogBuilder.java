package com.filesfromyou.logprocessor.integration.elastic;

import com.filesfromyou.logprocessor.service.UploadDirectory;
import org.springframework.stereotype.Component;

@Component
public class AppLogBuilder extends ElasticBuilder {

    private final String indexName = "clientapplog";

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        String fileUri = buildFileUri(UploadDirectory.DETAIL);
        String elasticUri = buildElasticUri(Operation.INDEX, this.indexName);
        from(fileUri)
                .split(body().tokenize("\n"))
                .streaming()
                .convertBodyTo(byte[].class)
                .to(elasticUri);
    }
}
