package com.game.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 23.02.2018 #
 ******************************
*/
public class SnakeTimer {
    private static final Log log = LogFactory.getLog(SnakeTimer.class);
    private static ConcurrentHashMap<String, Snake> snakes = new ConcurrentHashMap<>();
    private static Timer gameTimer = null;
    private static final long TICK_DELAY = 100;

    public static synchronized void addSnake(String lobbyId, Snake snake) {
        if (snakes.size() == 0) {
            startTimer();
        }
        snakes.put(lobbyId, snake);
    }

    private static void startTimer() {
        gameTimer = new Timer(SnakeTimer.class.getSimpleName() + " Timer");
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    tick();
                } catch (Throwable e) {
                    log.error("Caught to prevent timer from shutting down", e);
                }
            }
        }, TICK_DELAY, TICK_DELAY);
    }

    private static void tick() {
        for (String lobby : snakes.keySet()) {
            Snake snake = snakes.get(lobby);
        }
    }
}
