package com.rugbyInfo.rugbyInfoApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@SpringBootApplication
public class RugbyInfoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(RugbyInfoApiApplication.class, args);
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        HiddenHttpMethodFilter filter = new HiddenHttpMethodFilter();
        return filter;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
