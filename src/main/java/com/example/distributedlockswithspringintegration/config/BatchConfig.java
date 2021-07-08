package com.example.distributedlockswithspringintegration.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.LockRegistry;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
public class BatchConfig {
    private String region = null;

    @Autowired
    private RegionConfig regionConfig;

    public String getRegion() {
        if (region == null) {
            List<String> regions = Arrays.asList(regionConfig.getRegions().split(","));
            Collections.shuffle(regions);
            region = regions.get(0);
        }
        return region;
    }


    @Bean
    LockRepository defaultLockRegistry(DataSource dataSource) {
        DefaultLockRepository lockRepository = new DefaultLockRepository(dataSource);
        lockRepository.setRegion(getRegion());
        return lockRepository;
    }

    @Bean
    JdbcLockRegistry jdbcLockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);
    }

    @Bean
    public LockRegistryLeaderInitiator lockRegistryLeaderInitiator(LockRegistry locks) {
        return new LockRegistryLeaderInitiator(locks);
    }
}
