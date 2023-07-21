package com.example.demo;

import com.example.demo.Service.AppService;
import com.example.demo.models.DisputeData;
import com.example.demo.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

@SpringBootApplication
@Slf4j
public class DemoApplication implements CommandLineRunner {

	@Autowired
	AppProperties properties;

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	AppService appService;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		log.info("properties [{}]", properties);
		List<DisputeData> details = appService.getDisputeData();

		log.info("Data : [{}]", details);


		  appService.init("/Users/onirnarahari/Downloads/dciinvex.csv");
		appService.disputeClaims();
		SpringApplication.exit(applicationContext);

	}
}
