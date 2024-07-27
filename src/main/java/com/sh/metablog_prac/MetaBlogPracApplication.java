package com.sh.metablog_prac;

import com.sh.metablog_prac.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class MetaBlogPracApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaBlogPracApplication.class, args);
    }

}
