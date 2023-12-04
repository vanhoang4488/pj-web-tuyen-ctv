package vanhoang.project.config;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "mysql-binlog.db")
public class BinLogClientConfig {
    private String hostName;
    private Integer port;
    private String username;
    private String password;
    private Integer corePoolSize;
    private Integer maxPoolSize;
    private Integer queueCapacity;
    private Integer keepAliveSeconds;

    @Bean
    public BinaryLogClient getBinaryLogClient() {
        BinaryLogClient binaryLogClient =
                new BinaryLogClient(this.hostName, this.port, this.username, this.password);
        binaryLogClient.registerEventListener(new CustomBinLogEventListener());
        return binaryLogClient;
    }

    @Bean
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(this.corePoolSize);
        executor.setMaxPoolSize(this.maxPoolSize);
        executor.setQueueCapacity(this.queueCapacity);
        executor.setKeepAliveSeconds(this.keepAliveSeconds);
        executor.setThreadGroupName("binlog-task-executor--");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }


}
