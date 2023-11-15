package org.aptech.metube.personal.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageConfig {
    @Autowired
    private ResourceBundleMessageSource messageSource;

    @Bean
    public Translator translator() {
        return new Translator(messageSource);
    }
}