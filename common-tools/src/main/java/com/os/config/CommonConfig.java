package com.os.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

@Getter
@Configuration
public class CommonConfig implements SchedulingConfigurer {

    @Value("${redis.prefix}")
    private String redisPrefix;

    /**
     * Bean cung cấp thread pool để thực hiện các tác vụ hệ thống.
     * @return
     */
    @Bean(name = "taskThreadPoolExecutor")
    public ThreadPoolTaskExecutor taskThreadPoolExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(15);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("sys-task-thread-");

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(100);

        // khi số lượng thread vượt quá maxPoolSize.
        // Queue cũng đã đầy.
        // các thread mới submit đến sẽ không bị từ chối và
        // yêu cầu thực hiện trong chính thread phát sinh.
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    @Bean(name = "taskScheduler", destroyMethod = "shutdown")
    public ScheduledExecutorService taskScheduler(){
        int nThread = Runtime.getRuntime().availableProcessors();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(nThread * 2);
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskScheduler());
    }
}
