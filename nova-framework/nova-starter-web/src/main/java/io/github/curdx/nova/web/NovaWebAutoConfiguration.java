package io.github.curdx.nova.web;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class NovaWebAutoConfiguration {

    @Bean
    public GlobalExceptionHandler novaGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
