package com.MDS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;

@SpringBootApplication(scanBasePackages= {"com.MDS"})
@EnableAutoConfiguration(exclude = {GsonAutoConfiguration.class})
public class LocationMasterDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationMasterDataServiceApplication.class, args);
	}
}
