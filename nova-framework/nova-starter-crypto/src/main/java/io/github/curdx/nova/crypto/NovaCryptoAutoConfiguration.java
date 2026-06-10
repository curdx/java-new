package io.github.curdx.nova.crypto;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(NovaCryptoProperties.class)
@ConditionalOnProperty(prefix = "nova.crypto", name = "sm4-key")
public class NovaCryptoAutoConfiguration {

    @Bean
    public SmCryptoService smCryptoService(NovaCryptoProperties properties) {
        SmCryptoService service = new SmCryptoService(properties.getSm4Key());
        Sm4FieldTypeHandler.init(service.sm4());
        return service;
    }
}
