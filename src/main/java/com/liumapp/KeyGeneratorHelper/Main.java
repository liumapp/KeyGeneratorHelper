package com.liumapp.KeyGeneratorHelper;

import com.liumapp.KeyGeneratorHelper.pattern.KeyPattern;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liumapp on 9/28/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
@Configuration
@EnableAutoConfiguration
public class Main {

    @Bean
    public KeyPattern keyPattern() {
        return new KeyPattern();
    }

}
