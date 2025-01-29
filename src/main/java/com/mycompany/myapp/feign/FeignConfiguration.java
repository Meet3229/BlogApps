package com.mycompany.myapp.feign;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import feign.okhttp.OkHttpClient;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Configuration
@EnableFeignClients(basePackages = "com.omnidya")
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {


	
    /**
     * Set the Feign specific log level to log client REST requests.
     */
    @Bean
    feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }
    
    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }


}

