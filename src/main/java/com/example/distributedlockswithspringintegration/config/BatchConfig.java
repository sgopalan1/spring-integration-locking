package com.example.distributedlockswithspringintegration.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

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
}
