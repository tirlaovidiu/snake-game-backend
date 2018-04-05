package com.game.actuator;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 05.04.2018 #
 ******************************
*/

import com.game.controller.GameController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class SnakeGameInfo implements InfoContributor {

    private final GameController gameController;

    @Autowired
    public SnakeGameInfo(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("games", gameController.getRunningGamesCounter());
    }
}
