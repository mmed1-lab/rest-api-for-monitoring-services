package com.mmed.ws;

import com.mmed.ws.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
@EnableScheduling
public class ApiMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMonitoringApplication.class, args);
	}

}
