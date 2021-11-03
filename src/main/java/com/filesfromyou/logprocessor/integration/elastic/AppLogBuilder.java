package com.filesfromyou.logprocessor.integration.elastic;

import com.filesfromyou.logprocessor.service.filemanager.UploadDirectory;
import org.springframework.stereotype.Component;

@Component
public class AppLogBuilder extends ElasticBuilder {

    private final String INDEX_NAME = "clientapplog";
    private final UploadDirectory DIRECTORY = UploadDirectory.DETAIL;

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        String sourceFileUri = buildFileUri(DIRECTORY);
        String targetElasticUri = buildElasticUri(Operation.INDEX, INDEX_NAME);
        String targetFileUriForElasticFailures = buildFileUriForElasticFailures();
        from(sourceFileUri)
                .split(body().tokenize("\n"))
                .streaming()
                .convertBodyTo(byte[].class)
                .doTry()
                    .to(targetElasticUri)
                .doCatch(Exception.class)
                    .to(targetFileUriForElasticFailures)
                .end();
    }

    private String buildFileUriForElasticFailures() {
        return "file:" +
                DIRECTORY.getPath() + "/error" +
                "?fileExist=Append" +
                "&appendChars=\\n";
    }
}
