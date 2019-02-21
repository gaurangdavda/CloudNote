package edu.neu.csye6225.spring19.cloudninja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import edu.neu.csye6225.spring19.cloudninja.property.FileStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class CloudNinjaApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudNinjaApplication.class, args);
	}

}
