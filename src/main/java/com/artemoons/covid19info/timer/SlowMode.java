package com.artemoons.covid19info.timer;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Timer;
import java.util.TimerTask;

@Service
@Slf4j
public class SlowMode {

    @Value("${slowmode.timeout}")
    private long timeout;

    @Getter
    private boolean enabled = false;

    private void sleep() {
        try {
            enabled = true;
            Thread.sleep(timeout);
            enabled = false;
        } catch (InterruptedException ex) {
            log.error("Interrupted exception occurred: {}", ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public void createNewTimerTask() {

        TimerTask task = new TimerTask() {
            public void run() {
                log.info("---=== [ Slow mode activated for {} minutes ] ===---", timeout / 60000);
                sleep();
                log.info("---=== [ Slow mode deactivated ] ===---");
                log.info("Slow mode set to {}.", enabled);
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(task, 0L);
    }
}
