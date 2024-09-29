package com.tbaruth.todocore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ExecutorConfig {

  @Bean
  public ExecutorService getGenExecutor() {
    return Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("GenExec-", 0).factory());
  }
}
