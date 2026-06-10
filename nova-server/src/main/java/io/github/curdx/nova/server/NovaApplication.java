package io.github.curdx.nova.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.curdx.nova")
@MapperScan("io.github.curdx.nova.module.**.mapper")
public class NovaApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovaApplication.class, args);
    }
}
