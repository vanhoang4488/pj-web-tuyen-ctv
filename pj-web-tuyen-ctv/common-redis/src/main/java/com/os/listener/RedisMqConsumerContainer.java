package com.os.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class RedisMqConsumerContainer {

    private ThreadPoolTaskExecutor executor;
    private Map<String, AbstractMsgConsumer> queue;

    private final StringRedisTemplate stringRedisTemplate;

    public static boolean run = true;

    public RedisMqConsumerContainer(StringRedisTemplate stringRedisTemplate){
        this.stringRedisTemplate = stringRedisTemplate;
        queue = new HashMap<>();
    }

    public void addListener(QueueConfiguration configuration){
        QueueName queueName = configuration.getQueueName();
        AbstractMsgConsumer consumer = configuration.getConsumer();
        if(Objects.isNull(queueName)){
            log.info("=====> {}", "RedisMqConsumerContainer, QueueName cannot null");
        }
        else if(Objects.isNull(consumer)){
            log.info("=====> {}", "RedisMqConsumerContainer, Consumer cannot null");
        }
        else
            queue.put(queueName.getKey(), consumer);
    }

    public void start(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(15);
        executor.setQueueCapacity(50);
        executor.setKeepAliveSeconds(20);

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(100);
        executor.initialize();

        // chỉ cho phép đọc.
        queue = Collections.unmodifiableMap(queue);

        // bắt đầu khởi chạy chức năng lắng nghe các thay đổi của hệ thống bằng cách
        // chờ đợi form key-value tương ứng xuất hiện trong hệ thống.
        queue.forEach((key, value) -> {
            executor.submit(new QueueReceiveListener(stringRedisTemplate, key, value));
        });
    }
}
