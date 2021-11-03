package com.filesfromyou.logprocessor.integration.elastic;

import com.filesfromyou.logprocessor.service.filemanager.UploadDirectory;
import org.springframework.stereotype.Component;

@Component
public class SystemLogBuilder extends ElasticBuilder {

    private final String INDEX_NAME = "clientsystemlog";

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        String sourceFileUri = buildFileUri(UploadDirectory.SYSTEM);
        String targetElasticUri = buildElasticUri(Operation.INDEX, INDEX_NAME);
        from(sourceFileUri)
                .convertBodyTo(byte[].class)
                .to(targetElasticUri);
    }
}
