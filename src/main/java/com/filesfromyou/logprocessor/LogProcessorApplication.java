package com.filesfromyou.logprocessor;

import com.filesfromyou.logprocessor.service.filemanager.FileManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class LogProcessorApplication implements CommandLineRunner {
	@Resource
	FileManagementService fileManagementService;

	public static void main(String[] args) {
		SpringApplication.run(LogProcessorApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		fileManagementService.init();
	}
}
