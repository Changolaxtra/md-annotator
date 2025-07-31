package com.changolaxtra.tools.annotator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.changolaxtra.tools.annotator.repository")
public class MdAnnotatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdAnnotatorApplication.class, args);
	}

}
