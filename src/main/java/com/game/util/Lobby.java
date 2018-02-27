package com.game.util;

import com.game.engine.Snake;
import com.game.util.common.StatusType;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 17.02.2018 #
 ******************************
*/
public class Lobby {
    private String player1;
    private String player2;
    private StatusType player1Status;
    private StatusType player2Status;
    private String lobbyId;
    private String player1SessionId;
    private String player2SessionId;

    private Snake snake;

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public StatusType getPlayer1Status() {
        return player1Status;
    }

    public void setPlayer1Status(StatusType player1Status) {
        this.player1Status = player1Status;
    }

    public StatusType getPlayer2Status() {
        return player2Status;
    }

    public void setPlayer2Status(StatusType player2Status) {
        this.player2Status = player2Status;
    }

    public String getLobbyId() {
        return lobbyId;
    }

    public void setLobbyId(String lobbyId) {
        this.lobbyId = lobbyId;
    }

    public String getPlayer1SessionId() {
        return player1SessionId;
    }

    public void setPlayer1SessionId(String player1SessionId) {
        this.player1SessionId = player1SessionId;
    }

    public String getPlayer2SessionId() {
        return player2SessionId;
    }

    public void setPlayer2SessionId(String player2SessionId) {
        this.player2SessionId = player2SessionId;
    }
}
