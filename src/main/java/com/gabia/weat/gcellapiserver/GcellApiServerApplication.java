package com.gabia.weat.gcellapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.gabia.weat.gcellcommonmodule.annotation.EnableCustomLog;

@EnableCustomLog
@EnableJpaAuditing
@SpringBootApplication
@ConfigurationPropertiesScan
public class GcellApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GcellApiServerApplication.class, args);
	}

}