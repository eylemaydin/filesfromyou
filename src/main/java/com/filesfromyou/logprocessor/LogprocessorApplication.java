package com.filesfromyou.logprocessor;

import com.filesfromyou.logprocessor.service.FileManagementService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class LogprocessorApplication implements CommandLineRunner {
	@Resource
	FileManagementService fileManagementService;

	public static void main(String[] args) {
		SpringApplication.run(LogprocessorApplication.class, args);
	}

	@Override
	public void run(String... arg) throws Exception {
		fileManagementService.init();
	}
}
