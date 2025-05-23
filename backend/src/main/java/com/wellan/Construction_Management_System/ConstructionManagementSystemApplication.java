package com.wellan.Construction_Management_System;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing
@EnableCaching //啟動快取功能
@SpringBootApplication
public class ConstructionManagementSystemApplication implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(ConstructionManagementSystemApplication.class);

	public ConstructionManagementSystemApplication() {
		logger.info("程式已成功運行");
	}

	public static void main(String[] args) {
		SpringApplication.run(ConstructionManagementSystemApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
	}
}
