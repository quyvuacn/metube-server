//package org.aptech.metube.videoservice.config;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.cloud.storage.Storage;
//import com.google.cloud.storage.StorageOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//
//import java.io.IOException;
//
///*
//    @author: Dinh Quang Anh
//    Date   : 8/23/2023
//    Project: metube-server
//*/
//@Configuration
//public class CloudConfig {
//    @Bean
//    @Primary
//    public GoogleCredentials googleCredentialsConfig() throws IOException {
//        Resource resource = new ClassPathResource("metube04storage-20a04ba3b976.json");
//        return GoogleCredentials.fromStream(resource.getInputStream());
//    }
//
//    @Bean
//    public Storage storage(GoogleCredentials googleCredentials) {
//        return StorageOptions.newBuilder()
//                .setCredentials(googleCredentials)
//                .build()
//                .getService();
//    }
//}
