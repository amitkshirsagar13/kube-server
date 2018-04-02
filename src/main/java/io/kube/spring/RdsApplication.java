package io.kube.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RdsApplication {
	public static void main(String[] args) {
		SpringApplication.run(RdsApplication.class, args);
	}
}
