package org.aptech.metube.videoservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

import java.util.Locale;

public class Translator {

    private static ResourceBundleMessageSource messageSource;

    //todo: cấu hình message resource
    @Autowired
    Translator(ResourceBundleMessageSource messageSource) {
        Translator.messageSource = messageSource;
    }

    public static String toLocale(String messageKey) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }
}
