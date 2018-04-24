package com.MDS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages= {"com.MDS"})
public class LocationMasterDataServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LocationMasterDataServiceApplication.class, args);
	}
}
