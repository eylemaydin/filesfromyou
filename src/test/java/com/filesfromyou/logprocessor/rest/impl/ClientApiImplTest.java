package com.filesfromyou.logprocessor.rest.impl;

import com.filesfromyou.logprocessor.service.filemanager.FileManagementService;
import org.apache.camel.ProducerTemplate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ClientApiImplTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FileManagementService fileManagementService;

    @Value("${logprocessor.folder.testroot}")
    private String testFolder;

    @Value("${logprocessor.folder.clientapplog}")
    private String appLogFolder;

    @Value("${logprocessor.folder.clientsystemlog}")
    private String systemLogFolder;

    @Value("${logprocessor.camel.folder.result}")
    private String resultPath;

    @Value("${logprocessor.camel.folder.error}")
    private String errorPath;

    @Autowired
    ProducerTemplate producerTemplate;

    @AfterEach
    void clearAfterEach() throws IOException {
        FileSystemUtils.deleteRecursively(Path.of(testFolder));
    }

    @Test
    void saveSystemInformation() throws Exception {
        FileManagementService spyColorSelector = spy(fileManagementService);
        doThrow(new RuntimeException()).when(spyColorSelector).save(any(String.class), any(String.class), any(Path.class));

        String id = "3da5b52a-1387-4e93-b197-91db5f001047";
        String clientId = "05846552-4fe2-491d-b5ab-86e734be3250";
        String body = "{\"id\":\"" + id + "\",\"clientId\":\"" + clientId + "\",\"host\":{\"name\":null,\"cpu\":null,\"ip\":null,\"architecture\":null,\"os\":null},\"@timestamp\":null}";
        String fileName = (clientId + id).hashCode() + ".log";

        mockMvc.perform(post("/client/1/system")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("{\"success\":true,\"message\":\"Saved system log successfully!\"}")
                );

        then(fileManagementService).should().save(body, fileName, Path.of(systemLogFolder));
    }

    @Test
    void processSystemFile() throws IOException, InterruptedException {
        String sourceFileName = "sys_log_file.log";
        setupTestForCamelActions(systemLogFolder, sourceFileName);

        File targetFile = Path.of(systemLogFolder).resolve(resultPath).resolve(sourceFileName).toFile();
        assertTrue(targetFile.exists());
    }

    @Test
    void processInvalidSystemFile() throws IOException, InterruptedException {
        String sourceFileName = "sys_log_file_invalid_schema.log";
        setupTestForCamelActions(systemLogFolder, sourceFileName);

        File targetFile = Path.of(systemLogFolder).resolve(errorPath).resolve(sourceFileName).toFile();
        assertTrue(targetFile.exists());
    }

    @Test
    void uploadLogFile() throws Exception {
        FileManagementService spyColorSelector = spy(fileManagementService);
        doThrow(new RuntimeException()).when(spyColorSelector).save(any(MultipartFile.class), any(Path.class));

        String uuidAsString = UUID.randomUUID().toString();
        MockMultipartFile mockFile = new MockMultipartFile(
                "file",
                uuidAsString + ".log",
                "multipart/form-data",
                "{\"content\":\"some dummy content here\"}".getBytes()
        );

        mockMvc.perform(multipart("/client/1/logs").file(mockFile))
                .andExpect(status().isOk())
                .andExpect(
                        content().json("{\"success\":true,\"message\":\"Uploaded the file successfully!\"}")
                );

        then(fileManagementService).should().save(mockFile, Path.of(appLogFolder));
    }

    @Test
    void processLogFile() throws IOException, InterruptedException {
        String sourceFileName = "app_log_file.log";
        setupTestForCamelActions(appLogFolder, sourceFileName);

        assertSuccessfulResult(sourceFileName);
        assertUnsuccessfulResult(sourceFileName);
    }

    private void assertSuccessfulResult(String sourceFileName) throws IOException {
        Path targetPath = Path.of(appLogFolder).resolve(resultPath).resolve(sourceFileName);
        File targetFile = targetPath.toFile();
        assertTrue(targetFile.exists());

        try (Stream<String> fileStream = Files.lines(targetPath)) {
            int noOfLines = (int) fileStream.count();
            assertEquals(2, noOfLines);
        }
    }

    private void assertUnsuccessfulResult(String sourceFileName) throws IOException {
        Path targetPath = Path.of(appLogFolder).resolve(errorPath).resolve(sourceFileName);
        File targetFile = targetPath.toFile();
        assertTrue(targetFile.exists());

        try (Stream<String> fileStream = Files.lines(targetPath)) {
            int noOfLines = (int) fileStream.count();
            assertEquals(3, noOfLines);
        }
    }

    private void setupTestForCamelActions(String folderPath, String sourceFileName) throws IOException, InterruptedException {
        Path source = Path.of("src/test/resources/test_files").resolve(sourceFileName);
        Path target = Path.of(folderPath).resolve(sourceFileName);
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        // Wait for a few seconds so the camel routes can finalize the job
        Thread.sleep(3000);
    }
}