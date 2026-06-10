package io.github.curdx.nova.crypto;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "nova.crypto")
public class NovaCryptoProperties {

    /**
     * SM4 密钥（32 位 hex，即 16 字节）。生产环境务必通过环境变量/密管服务注入，
     * 后续接入 HSM/密钥轮换时此处扩展为 keySource。
     */
    private String sm4Key;
}
