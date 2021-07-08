package com.example.distributedlockswithspringintegration;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.support.locks.LockRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@RestController
@RequiredArgsConstructor
public class LockedController {
    private final ReservationRepository reservationRepository;
    private final LockRegistry lockRegistry;

    @GetMapping("/update/{id}/{name}/{time}")
    public Reservation update(@PathVariable Integer id, @PathVariable String name, @PathVariable Long time) throws InterruptedException {
        Lock lock = lockRegistry.obtain(String.valueOf(id));
        if (lock.tryLock(1, TimeUnit.SECONDS)) {
            try {
                doUpdateFor(id, name);
                Thread.sleep(time);
            } finally {
                lock.unlock();
            }
        }
        return reservationRepository.findById(id).get();
    }

    void doUpdateFor(Integer id, String name) {
        reservationRepository.findById(id).ifPresent(r -> {
            r.setName(name);
            reservationRepository.save(r);
        });
    }

}
