package com.filesfromyou.logprocessor.integration.elastic;

import com.filesfromyou.logprocessor.service.UploadDirectory;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

@Component
public class AppLogBuilder extends ElasticBuilder {

    private final String indexName = "clientapplog";
    private final UploadDirectory directory = UploadDirectory.DETAIL;

    @Override
    public void configure() throws Exception {
        super.configureRoute();

        String sourceFileUri = buildFileUri(this.directory);
        String targetElasticUri = buildElasticUri(Operation.INDEX, this.indexName);
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
                this.directory.getPath() + "/error" +
                "?fileExist=Append" +
                "&appendChars=\\n";
    }
}
