package org.aptech.metube.personal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
public class PersonalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalServiceApplication.class, args);
    }
}
