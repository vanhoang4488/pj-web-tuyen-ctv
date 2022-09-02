package com.os;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;

import javax.sql.DataSource;

@EnableDiscoveryClient
@SpringBootApplication
public class ManageApplication extends SpringBootServletInitializer {

    public static void main(String[] args){

        new SpringApplicationBuilder(ManageApplication.class)
                .web(WebApplicationType.SERVLET).run(args);
    }

    @Override
    public SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(ManageApplication.class);
    }
}
