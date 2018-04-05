package com.game.controller;

import com.game.engine.Snake;
import com.game.util.Lobby;
import com.game.util.OutputMessage;
import com.game.util.common.ClientMessage;
import com.game.util.common.EventType;
import com.game.util.common.LobbyMessage;
import com.game.util.common.SnakeMessage;
import com.game.util.common.StatusType;
import com.game.util.snake.enums.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 16.02.2018 #
 ******************************
*/

@Controller
@EnableScheduling
public class GameController {
    private static final String SERVER = "SERVER";

    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private static final String QUEUE = "[QUEUE]";
    private static final String GAMES = "[GAMES]";

    private static final String snakeGameDestination = "/topic/snake/";
    private static final String snakeQueueDestination = "/topic/queue/";

    private static final String LOBBY = "lobby";

    private final Map<String, Lobby> lobbyMap = new ConcurrentHashMap<>();
    private Integer nextLobbyId = 0;

    private final List<String> runningGames = new ArrayList<>();
    private final List<String> availableLobbies = new ArrayList<>();

    private SimpMessagingTemplate template;

    @Autowired
    public GameController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/snake/queue/{username}")
    public void sendLobby(@DestinationVariable("username") String username, ClientMessage message, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        log.debug(QUEUE + sessionId);
        if (availableLobbies.size() > 0) {
            template.convertAndSend(snakeQueueDestination + username, new LobbyMessage(SERVER, "Join lobby", availableLobbies.get(0), EventType.GAME_FOUND));
        } else {
            template.convertAndSend(snakeQueueDestination + username, new LobbyMessage(SERVER, "Create lobby", LOBBY + nextLobbyId, EventType.GAME_CREATE));
        }
    }

    @MessageMapping("/snake/lobby/{topic}")
    public void send(@DestinationVariable("topic") String topic, ClientMessage message, SimpMessageHeaderAccessor headerAccessor) {

        String lobbyId = topic;
        EventType eventType = message.getEventType();

        String sessionId = headerAccessor.getSessionId();

        if (eventType == EventType.HELLO_EVENT) {
            Lobby lobby;
            boolean has2Players = false;
            synchronized (lobbyMap) {
                if (lobbyMap.containsKey(lobbyId)) {
                    lobby = lobbyMap.get(lobbyId);
                    if (lobby.getPlayer1() != null && lobby.getPlayer2() != null) {
                        template.convertAndSend(snakeGameDestination + topic, new SnakeMessage(SERVER, "Go find a lobby", topic, EventType.GAME_FIND_LOBBY));
                        return;
                    } else if (lobby.getPlayer1SessionId().equals(sessionId)) {
                        has2Players = false;
                    } else {
                        lobby.setPlayer2(message.getFrom());
                        lobby.setPlayer2SessionId(sessionId);
                        lobby.setPlayer1Status(StatusType.WAITING);
                        lobby.setPlayer2Status(StatusType.WAITING);
                        has2Players = true;
                        availableLobbies.remove(lobbyId);
                    }
                } else {
                    lobby = new Lobby();
                    lobby.setPlayer1(message.getFrom());
                    lobby.setPlayer1SessionId(sessionId);
                    lobby.setLobbyId(topic);
                    lobbyMap.put(lobbyId, lobby);
                    availableLobbies.add(lobbyId);
                    nextLobbyId++;
                    template.convertAndSend(snakeGameDestination + topic, new SnakeMessage(SERVER, "WAIT FOR PLAYERS", lobbyId, EventType.HELLO_EVENT));
                }
            }
            if (has2Players) {
                Snake snake = new Snake();
                snake.initGame();
                lobby.setSnake(snake);
                template.convertAndSend(snakeGameDestination + topic, new SnakeMessage(SERVER, "INITIALIZE GAME", topic, EventType.START_GAME_EVENT,
                        snake.getSnakeP1(), snake.getSnakeP2(), snake.getWalls(), snake.getApples(), snake.getObstacles(),
                        snake.getP1CurrentDirection(), snake.getP2CurrentDirection()));
            }
        } else if (eventType == EventType.START_GAME_EVENT) {
            if (lobbyMap.containsKey(lobbyId)) {
                Lobby gameLobby = lobbyMap.get(lobbyId);
                if (gameLobby.getPlayer1SessionId().equals(sessionId))
                    gameLobby.setPlayer1Status(StatusType.READY);
                else if (gameLobby.getPlayer2SessionId().equals(sessionId))
                    gameLobby.setPlayer2Status(StatusType.READY);
                if (gameLobby.getPlayer1Status() == StatusType.READY && gameLobby.getPlayer2Status() == StatusType.READY) {
                    addGame(gameLobby);
                    gameLobby.setPlayer1Status(StatusType.IN_GAME);
                    gameLobby.setPlayer2Status(StatusType.IN_GAME);
                    template.convertAndSend(snakeGameDestination + topic, new SnakeMessage(SERVER, "GAME IS RUNNING", topic, EventType.RUNNING_GAME_EVENT));
                }
            }
        } else if (eventType == EventType.RUNNING_GAME_EVENT) {
            if (lobbyMap.containsKey(lobbyId)) {
                Lobby gameLobby = lobbyMap.get(lobbyId);
                if (gameLobby.getPlayer1Status() == StatusType.IN_GAME && gameLobby.getPlayer2Status() == StatusType.IN_GAME) {
                    Snake snake = gameLobby.getSnake();
                    if (message.getFrom().equals(gameLobby.getPlayer1())) {
                        snake.updatePlayer1Direction(message.getNextMove());
                    } else if (message.getFrom().equals(gameLobby.getPlayer2())) {
                        snake.updatePlayer2Direction(message.getNextMove());
                    }
                    if (snake.getCurrentGameState() == GameState.Running) {
                    } else {
                        String winner;
                        if (snake.getWinner())
                            winner = gameLobby.getPlayer2();
                        else
                            winner = gameLobby.getPlayer1();

                        removeGame(gameLobby);
                        template.convertAndSend(snakeGameDestination + topic, new SnakeMessage(SERVER, "GAME OVER", topic, winner, EventType.GAME_OVER_EVENT));
                        gameLobby.setPlayer1Status(StatusType.DISCONNECTED);
                        gameLobby.setPlayer2Status(StatusType.DISCONNECTED);
                    }
                }
            }
        } else {
            template.convertAndSend(snakeGameDestination + topic, new OutputMessage(SERVER, message.getText(), topic));
        }
    }

    @Scheduled(fixedDelay = 300)
    public synchronized void broadcastUpdates() {
        if (runningGames.size() > 0) {
            for (String lobbyId : runningGames) {
                Lobby lobby = lobbyMap.get(lobbyId);
                Snake snake = lobby.getSnake();
//                snake.updatePlayer1Direction(snake.getP1NextCoordinate());
//                snake.updatePlayer2Direction(snake.getP2NextCoordinate());
                snake.updateCheck();

                if (snake.getCurrentGameState() == GameState.Lost) {
                    String winner;
                    if (snake.getWinner())
                        winner = lobby.getPlayer2();
                    else
                        winner = lobby.getPlayer1();

                    removeGame(lobby);
                    template.convertAndSend(snakeGameDestination + lobby.getLobbyId(), new SnakeMessage(SERVER, "GAME OVER", lobby.getLobbyId(), winner, EventType.GAME_OVER_EVENT));
                    lobby.setPlayer1Status(StatusType.DISCONNECTED);
                    lobby.setPlayer2Status(StatusType.DISCONNECTED);
                } else {
                    template.convertAndSend(snakeGameDestination + lobby.getLobbyId(), new SnakeMessage(SERVER, "UPDATE", lobby.getLobbyId(), snake.getP1NextCoordinate(), snake.getGrowP1(), snake.getP2NextCoordinate(), snake.getGrowP2(), snake.getApples()));

//                    Date date = new Date();
//                    System.out.println(date.toString() + " UPDATE SENT " + lobby.getLobbyId());
                }
                if (runningGames.size() == 0)
                    break;
            }
        }
    }

    public synchronized void removeIfWasInGame(String sessionId) {
        for (String lobbyId : lobbyMap.keySet()) {
            Lobby lobby = lobbyMap.get(lobbyId);
            if (lobby.getPlayer1SessionId().equals(sessionId) || lobby.getPlayer2SessionId().equals(sessionId)) {
                if (lobby.getPlayer1SessionId().equals(sessionId)) {
                    template.convertAndSend(snakeGameDestination + lobby.getLobbyId(), new SnakeMessage(SERVER, "GAME OVER", lobby.getLobbyId(), lobby.getPlayer1(), EventType.GAME_OVER_EVENT));
                } else {
                    template.convertAndSend(snakeGameDestination + lobby.getLobbyId(), new SnakeMessage(SERVER, "GAME OVER", lobby.getLobbyId(), lobby.getPlayer2(), EventType.GAME_OVER_EVENT));
                }
                removeGame(lobby);
                return;
            }
        }

    }

    private synchronized void removeGame(Lobby lobby) {
        lobbyMap.remove(lobby.getLobbyId());
        runningGames.remove(lobby.getLobbyId());
    }

    private synchronized void addGame(Lobby lobby) {
        runningGames.add(lobby.getLobbyId());
    }

    public String getRunningGamesCounter() {
        return String.valueOf(runningGames.size());
    }
}
