package com.gianvittorio.drivermatchingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DriverMatchingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DriverMatchingServiceApplication.class, args);
	}

}
