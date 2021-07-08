package com.example.distributedlockswithspringintegration;

import com.example.distributedlockswithspringintegration.config.BatchConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Scheduler {
    @Autowired
    LockRegistryLeaderInitiator leaderInitiator;
    @Autowired
    private BatchConfig batchConfig;

    @Scheduled(initialDelay = 2000, fixedDelay = 4500)
    public void scheduleTask1(){
        // This will only run if this instance is "the leader"
        if (!isLeaderOfRegion("task1")) return;

        System.out.println("Executing task 1");
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 3000)
    public void scheduleTask2(){
        // This will only run if this instance is "the leader"
        if (!isLeaderOfRegion("task2")) return;

        System.out.println("Executing task 2");
    }

    private boolean isLeaderOfRegion(String region) {
        return hasRegion(region) && isLeader();
    }

    private boolean hasRegion(String region) {
        return region.equals(batchConfig.getRegion());
    }

    private boolean isLeader() {
        return leaderInitiator.getContext().isLeader();
    }
}
