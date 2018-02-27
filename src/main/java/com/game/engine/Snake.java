package com.game.engine;

import com.game.util.snake.Coordinate;
import com.game.util.snake.enums.Direction;
import com.game.util.snake.enums.GameState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 17.02.2018 #
 ******************************
*/
public class Snake {
    private static final int GameWidth = 30;
    private static final int GameHeight = 36;

    private Random random = new Random();

    private List<Coordinate> walls = new ArrayList<>();
    private List<Coordinate> snakeP1 = new ArrayList<>();
    private List<Coordinate> snakeP2 = new ArrayList<>();
    private List<Coordinate> apples = new ArrayList<>();
    private List<Coordinate> obstacles = new ArrayList<>();

    private Coordinate p1NextCoordinate;
    private Coordinate p2NextCoordinate;

    private Direction p1CurrentDirection;
    private Direction p2CurrentDirection;

    private GameState currentGameState = GameState.Running;

    private boolean winner;

    public void initGame() {
        AddSnakeP1();
        AddSnakeP2();

        p1CurrentDirection = Direction.SOUTH;
        p2CurrentDirection = Direction.SOUTH;

        p1NextCoordinate = snakeP1.get(0);
        p2NextCoordinate = snakeP2.get(0);

        AddWalls();
        AddObstacles();
        AddApples();
    }

    public void updatePlayer1Direction(Direction p1NextDirection) {
        if (Math.abs(p1NextDirection.ordinal() - p1CurrentDirection.ordinal()) % 2 == 1) {
            p1CurrentDirection = p1NextDirection;
        }
    }

    public void updatePlayer2Direction(Direction p2NextDirection) {
        if (Math.abs(p2NextDirection.ordinal() - p2CurrentDirection.ordinal()) % 2 == 1) {
            p2CurrentDirection = p2NextDirection;
        }
    }

    public void updateCheck() {
        switch (p1CurrentDirection) {
            case WEST:
                UpdateSnake(-1, 0, snakeP1);
                break;
            case SOUTH:
                UpdateSnake(0, 1, snakeP1);
                break;
            case EAST:
                UpdateSnake(1, 0, snakeP1);
                break;
            case NORTH:
                UpdateSnake(0, -1, snakeP1);
                break;
        }
        switch (p2CurrentDirection) {
            case WEST:
                UpdateSnake(-1, 0, snakeP2);
                break;
            case SOUTH:
                UpdateSnake(0, 1, snakeP2);
                break;
            case EAST:
                UpdateSnake(1, 0, snakeP2);
                break;
            case NORTH:
                UpdateSnake(0, -1, snakeP2);
                break;
        }

        checkSnake(snakeP1);
        if (currentGameState == GameState.Lost) {
            winner = false;
            return;

        }
        checkSnake(snakeP2);
        if (currentGameState == GameState.Lost) {
            winner = true;
        }
        p1NextCoordinate = snakeP1.get(0);
        p2NextCoordinate = snakeP2.get(0);
    }

    private void checkSnake(List<Coordinate> snake) {
        Coordinate snakeHeadPosition = snake.get(0);

        for (Coordinate wall : walls) {
            if (snakeHeadPosition.equals(wall)) {
                if (wall.getX() == 0) {
                    snake.get(0).setX(GameWidth - 2);
                    break;
                } else if (wall.getX() == GameWidth - 1) {
                    snake.get(0).setX(1);
                    break;
                } else if (wall.getY() == 0) {
                    snake.get(0).setY(GameHeight - 2);
                    break;
                } else if (wall.getY() == GameHeight - 1) {
                    snake.get(0).setY(1);
                    break;
                }
            }
        }

        for (Coordinate obstacle : obstacles) {
            if (snakeHeadPosition.equals(obstacle)) {
                currentGameState = GameState.Lost;
                return;
            }
        }

        for (int i = 1; i < snake.size(); i++) {
            if (snakeHeadPosition.equals(snake.get(i))) {
                currentGameState = GameState.Lost;
                return;
            }
        }
    }

    private void UpdateSnake(int x, int y, List<Coordinate> snake) {
        snake.add(0, new Coordinate(snake.get(0).getX() + x, snake.get(0).getY() + y));
        snake.remove(snake.size() - 1);
    }

    private void AddApples() {
        Coordinate coordinate = null;

        boolean added = false;

        while (!added) {
            int x = 1 + random.nextInt(GameWidth - 2);
            int y = 1 + random.nextInt(GameHeight - 2);

            coordinate = new Coordinate(x, y);
            boolean collision = false;

            for (Coordinate s : snakeP1) {
                if (s.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            for (Coordinate apple : apples) {
                if (apple.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            for (Coordinate obstacle : obstacles) {
                if (obstacle.equals(coordinate)) {
                    collision = true;
                    break;
                }
            }

            added = !collision;
        }

        apples.add(coordinate);
    }

    private void AddObstacles() {
        obstacles.add(new Coordinate(5, 5));
        obstacles.add(new Coordinate(6, 5));
        obstacles.add(new Coordinate(7, 5));
        obstacles.add(new Coordinate(8, 5));
        obstacles.add(new Coordinate(9, 5));
        obstacles.add(new Coordinate(10, 5));
        obstacles.add(new Coordinate(11, 5));
        obstacles.add(new Coordinate(11, 6));
        obstacles.add(new Coordinate(11, 7));
        obstacles.add(new Coordinate(11, 8));
        obstacles.add(new Coordinate(11, 9));
        obstacles.add(new Coordinate(11, 10));
    }

    private void AddWalls() {
        for (int x = 0; x < GameWidth; x++) {
            walls.add(new Coordinate(x, 0));
            walls.add(new Coordinate(x, GameHeight - 1));
        }
        //Left and right walls
        for (int y = 1; y < GameHeight; y++) {
            walls.add(new Coordinate(0, y));
            walls.add(new Coordinate(GameWidth - 1, y));
        }
    }

    private void AddSnakeP2() {
        snakeP2.clear();
        snakeP2.add(new Coordinate(17, 8));
        snakeP2.add(new Coordinate(18, 8));
    }

    private void AddSnakeP1() {
        snakeP1.clear();
        snakeP1.add(new Coordinate(16, 7));
        snakeP1.add(new Coordinate(15, 7));
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

    public Coordinate getP1NextCoordinate() {
        return p1NextCoordinate;
    }

    public Coordinate getP2NextCoordinate() {
        return p2NextCoordinate;
    }

    public boolean getWinner() {
        return winner;
    }

    public Direction getP1CurrentDirection() {
        return p1CurrentDirection;
    }

    public Direction getP2CurrentDirection() {
        return p2CurrentDirection;
    }

    public GameState getCurrentGameState() {
        return currentGameState;
    }
}
