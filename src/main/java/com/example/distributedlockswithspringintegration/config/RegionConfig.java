package com.example.distributedlockswithspringintegration.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("batch-jobs")
@Getter
public class RegionConfig {
    private String regions;
}
