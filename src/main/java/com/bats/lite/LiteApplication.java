package com.bats.lite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

@Slf4j
@EnableDiscoveryClient
@EnableAutoConfiguration
@EnableCaching
@EnableFeignClients
@SpringBootApplication
@EnableConfigurationProperties
public class LiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteApplication.class, args);
        int mb = 1024 * 1024;
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long xmx = memoryBean.getHeapMemoryUsage().getMax() / mb;
        long xms = memoryBean.getHeapMemoryUsage().getInit() / mb;
        System.out.print("\n");
        log.info("Initial Memory (xms): {}mb", xms);
        log.info("Max Memory (xmx): {}mb\n", xmx);
    }
}
