package com.os.nacos;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.Properties;

@Component
@Slf4j
public class NacosConfig implements ApplicationRunner {

    @Value("${spring.cloud.nacos.config.server-addr}")
    private String serverAddr;
    @Value("${spring.cloud.nacos.config.namespace}")
    private String namespace;
    @Value("${spring.cloud.nacos.discovery.group}")
    private String discoveryGroup;
    @Value("${server.port}")
    private int port;
    @Value("${spring.application.name}")
    private String appName;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Properties properties = new Properties();
        properties.setProperty("serverAddr", serverAddr);
        properties.setProperty("namespace", namespace);

        NamingService naming = NamingFactory.createNamingService(properties);

        String appId = InetAddress.getLocalHost().getHostAddress();
        log.info("=====> appId: {}", appId);

        appName = discoveryGroup + "@@" + appName;
        log.info("=====> all instance of ({}) is: {}", appName, naming.getAllInstances(appName));
        naming.subscribe(appName, event -> {
            if(event instanceof NamingEvent){
                log.info("=====> serviceName: {}", ((NamingEvent) event).getServiceName());
                log.info("=====> instances of: {}", ((NamingEvent) event).getInstances());
            }
        });
    }
}
