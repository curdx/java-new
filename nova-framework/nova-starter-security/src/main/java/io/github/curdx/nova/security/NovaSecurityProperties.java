package io.github.curdx.nova.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@Data
@ConfigurationProperties(prefix = "nova.security")
public class NovaSecurityProperties {

    /**
     * 免登录路径（Ant 风格）。
     */
    private List<String> ignoreUrls = new ArrayList<>(List.of(
            "/auth/login",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/doc.html",
            "/error",
            "/h2-console/**",
            "/actuator/health"
    ));
}
