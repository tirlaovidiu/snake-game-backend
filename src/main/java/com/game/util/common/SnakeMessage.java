package com.game.util.common;


import com.game.util.snake.Coordinate;
import com.game.util.snake.enums.Direction;

import java.util.ArrayList;
import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 23.02.2018 #
 ******************************
*/
public class SnakeMessage {
    private String from;
    private String message;
    private String topic;
    private EventType event;
    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snakeP1 = new ArrayList<>();
    private List<Coordinate> snakeP2 = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();
    private List<Coordinate> obstacles = new ArrayList<>();

    private Coordinate p1NextCoordinate;
    private Coordinate p2NextCoordinate;

    private Direction p1CurrentDirection;
    private Direction p2CurrentDirection;
    private String winner;

    public SnakeMessage(String from, String message, String topic, EventType event) {
        this.from = from;
        this.message = message;
        this.topic = topic;
        this.event = event;
    }

    public SnakeMessage(String from, String message, String topic, EventType event, List<Coordinate> snakeP1, List<Coordinate> snakeP2, List<Coordinate> walls, List<Coordinate> apples, List<Coordinate> obstacles, Direction p1CurrentDirection, Direction p2CurrentDirection) {
        this.from = from;
        this.message = message;
        this.topic = topic;
        this.event = event;
        this.snakeP1 = snakeP1;
        this.snakeP2 = snakeP2;
        this.walls = walls;
        this.apples = apples;
        this.obstacles = obstacles;
        this.p1CurrentDirection = p1CurrentDirection;
        this.p2CurrentDirection = p2CurrentDirection;
    }

    public SnakeMessage(String from, String message, String topic, Coordinate p1NextCoordinate, Coordinate p2NextCoordinate) {
        this.from = from;
        this.message = message;
        this.topic = topic;
        this.p1NextCoordinate = p1NextCoordinate;
        this.p2NextCoordinate = p2NextCoordinate;
    }

    public SnakeMessage(String from, String message, String topic, String winner, EventType event) {
        this.from = from;
        this.message = message;
        this.topic = topic;
        this.winner = winner;
        this.event = event;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public void setWalls(List<Coordinate> walls) {
        this.walls = walls;
    }

    public void setSnakeP1(List<Coordinate> snakeP1) {
        this.snakeP1 = snakeP1;
    }

    public void setSnakeP2(List<Coordinate> snakeP2) {
        this.snakeP2 = snakeP2;
    }

    public void setApples(List<Coordinate> apples) {
        this.apples = apples;
    }

    public void setObstacles(List<Coordinate> obstacles) {
        this.obstacles = obstacles;
    }


    public void setP1CurrentDirection(Direction p1CurrentDirection) {
        this.p1CurrentDirection = p1CurrentDirection;
    }

    public void setP2CurrentDirection(Direction p2CurrentDirection) {
        this.p2CurrentDirection = p2CurrentDirection;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public String getTopic() {
        return topic;
    }

    public EventType getEvent() {
        return event;
    }

    public List<Coordinate> getWalls() {
        return walls;
    }

    public List<Coordinate> getSnakeP1() {
        return snakeP1;
    }

    public List<Coordinate> getSnakeP2() {
        return snakeP2;
    }

    public List<Coordinate> getApples() {
        return apples;
    }

    public List<Coordinate> getObstacles() {
        return obstacles;
    }


    public Direction getP1CurrentDirection() {
        return p1CurrentDirection;
    }

    public Direction getP2CurrentDirection() {
        return p2CurrentDirection;
    }

    public String getWinner() {
        return winner;
    }

    public Coordinate getP1NextCoordinate() {
        return p1NextCoordinate;
    }

    public void setP1NextCoordinate(Coordinate p1NextCoordinate) {
        this.p1NextCoordinate = p1NextCoordinate;
    }

    public Coordinate getP2NextCoordinate() {
        return p2NextCoordinate;
    }

    public void setP2NextCoordinate(Coordinate p2NextCoordinate) {
        this.p2NextCoordinate = p2NextCoordinate;
    }
}
