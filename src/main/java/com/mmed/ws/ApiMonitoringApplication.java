package com.mmed.ws;

import com.mmed.ws.config.RsaKeysConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(RsaKeysConfig.class)
public class ApiMonitoringApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiMonitoringApplication.class, args);
	}

}
