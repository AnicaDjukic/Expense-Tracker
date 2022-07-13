package com.expense.ExpenseTracker.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class ExecutorConfig {

    @Value("${async.executor.thread.core_pool_size}")
    private int corePoolSize;
    @Value("${async.executor.thread.max_pool_size}")
    private int maxPoolSize;
    @Value("${async.executor.thread.queue_capacity}")
    private int queueCapacity;
    @Value("${async.executor.thread.name.prefix}")
    private String namePrefix;

    @Bean(name = "asyncServiceExecutor")
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new VisibleThreadPoolTaskExecutor();
        //Configure the number of core threads
        executor.setCorePoolSize(corePoolSize);
        //Configure maximum threads
        executor.setMaxPoolSize(maxPoolSize);
        //Configure queue size
        executor.setQueueCapacity(queueCapacity);
        //Configure the name prefix of threads in the thread pool
        executor.setThreadNamePrefix(namePrefix);

        // Rejection policy: how to handle new tasks when the pool has reached max size
        // CALLER_RUNS: the task is not executed in the new thread, but in the thread of the caller
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //Perform initialization
        executor.initialize();
        return executor;
    }
}
