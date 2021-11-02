package com.filesfromyou.logprocessor.integration.elastic;

import com.filesfromyou.logprocessor.service.UploadDirectory;
import org.springframework.stereotype.Component;

@Component
public class SystemLogBuilder extends ElasticBuilder {

    private final String indexName = "clientsystemlog";

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        String fileUri = buildFileUri(UploadDirectory.SYSTEM);
        String elasticUri = buildElasticUri(Operation.INDEX, this.indexName);
        from(fileUri)
                .convertBodyTo(byte[].class)
                .to(elasticUri);
    }
}
