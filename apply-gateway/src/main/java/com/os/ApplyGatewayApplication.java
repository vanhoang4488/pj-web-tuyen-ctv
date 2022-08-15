package com.os;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;

// Loại bỏ khởi tạo GatewayAutoConfiguration.class để cài đè RoutePredicateHandlerMapping.
@SpringBootApplication(exclude = {GatewayAutoConfiguration.class})
public class ApplyGatewayApplication {

    public static void main(String[] args){

        int cpuNum = Runtime.getRuntime().availableProcessors();

        System.setProperty("reactor.netty.ioWorkerCount", "" + cpuNum * 4);

        System.setProperty("reactor.netty.ioSelectCount", "1");

        SpringApplication.run(ApplyGatewayApplication.class, args);
    }
}
