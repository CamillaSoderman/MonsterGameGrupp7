package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class MonsterGame {
    public static void main(String[] args) throws Exception {
        // Goal: Generera en bana med flera mindre hinder. Slumpmässigt
        // OOP för monster
        // Refaktorera i metoder
        // Bug: Monster kan spawna PÅ obstacle

        // Startpositioner Monster: 2, 22 OCH 77,22

        DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory();
        Terminal terminal = terminalFactory.createTerminal();
        terminal.setCursorVisible(false);

        TextGraphics textGraphics = terminal.newTextGraphics();

        Position player = new Position(40,3);
        final char playerCharacter = '\u2661';
        final char block = '\u2588';
        final char monster = '\u2620';
        terminal.setCursorPosition(player.x, player.y);
        terminal.putCharacter(playerCharacter);

        // Create Obstacles and Border
        Obstacle obstacleObject = new Obstacle();
//        obstacleObject.createBorders();
        List<Position> obstacles = obstacleObject.obstacles;
        obstacleObject.addObstacle();
        obstacleObject.createBorders();
        drawObstacles(obstacles, terminal, block);


//        Random r = new Random();
//        Position monPos = new Position(r.nextInt(40,80), r.nextInt(24));;
        Position monPos = new Position(77, 22);
        terminal.setCursorPosition(monPos.x, monPos.y);
        terminal.putCharacter(monster);

        terminal.flush();

        boolean continueReadingInput = true;
        while (continueReadingInput) {

            KeyStroke keyStroke;
            do {
                Thread.sleep(5); // might throw InterruptedException
                keyStroke = terminal.pollInput();
            } while (keyStroke == null);

            Character c = keyStroke.getCharacter(); // used Character instead of char because it might be null

            if (c == Character.valueOf('q')) {
                continueReadingInput = false;
                terminal.close();
                System.out.println("quit");
            }

            int oldX = player.x; // save old position x
            int oldY = player.y; // save old position y
            switch (keyStroke.getKeyType()) {
                case ArrowDown -> player.y += 1;
                case ArrowUp -> player.y -= 1;
                case ArrowRight -> player.x += 1;
                case ArrowLeft -> player.x -= 1;
            }
            // detect if playerCharacter tries to run into obsticle

            boolean crashIntoObstacle = checkForObstacleCrash(obstacles, player);
            if (crashIntoObstacle) {
                player.x = oldX;
                player.y = oldY;
            }
            else {
                moveObject(oldX, oldY, player, playerCharacter, terminal);
            }

            // MONSTER MOVEMENT
            int monOldX = monPos.x;
            int monOldY = monPos.y;

            setMonsterCoordinates(monPos, player);
            boolean monsterCrash = checkForObstacleCrash(obstacles, monPos);

            // helps monster move around obstacle when hitting it
            if (monsterCrash) {
                helpMonMoveAroundObs(monPos, player, monOldX, monOldY, obstacles);
                moveObject(monOldX, monOldY, monPos, monster, terminal);
            } else {
                moveObject(monOldX, monOldY, monPos, monster, terminal);
            }

            // check if playerCharacter runs into the monster
            if (monPos.x == player.x && monPos.y == player.y) {
                endGame(terminal, 5, textGraphics);
                continueReadingInput = false;
            }

            terminal.flush();
        }
    }

    private static void endGame(Terminal terminal, int points, TextGraphics textGraphics) throws IOException, InterruptedException {
        terminal.clearScreen(); //30.6.
        textGraphics.putString(30, 6, "Game Over!", SGR.BOLD);
        textGraphics.putString(30, 7, "Scored Points: " + points, SGR.BOLD);
        for (int i = 5; i >= 0; i--) {
            terminal.flush();
            textGraphics.putString(30, 8, "Closing game in: " + i, SGR.BOLD);
            Thread.sleep(1000);
        }
        terminal.close();

    }

    private static void drawObstacles(List<Position> obstacles, Terminal terminal, char block) throws IOException {
        for (Position p : obstacles) {
            terminal.setCursorPosition(p.x, p.y);
            terminal.putCharacter(block);
        }
    }

    public static void moveObject(int oldX, int oldY, Position object, char character, Terminal terminal) throws IOException {
        terminal.setCursorPosition(oldX, oldY);
        terminal.putCharacter(' ');
        terminal.setCursorPosition(object.x, object.y);
        terminal.putCharacter(character);
        terminal.flush();
    }
    public static boolean checkForObstacleCrash(List<Position> obstacles, Position player) {
        for (Position p : obstacles) {
            if (p.x == player.x && p.y == player.y) {
                return true;
            }
        }
        return false;
    }
    public static void setMonsterCoordinates(Position monPos, Position player) {
        if (monPos.x > player.x) {
            monPos.x--;
        } else if (monPos.x < player.x){
            monPos.x++;
        }
        if (monPos.y > player.y) {
            monPos.y--;
        } else if (monPos.y < player.y){
            monPos.y++;
        }
    }
    private static void helpMonMoveAroundObs(Position monPos, Position player, int monOldX, int monOldY, List<Position> obstacles) {
        // check if vertical obs
        for (Position obstacle : obstacles) {
            if ((monPos.y + 1) == obstacle.y || (monPos.y - 1) == obstacle.y) {
                if (player.y > monPos.y) {
                    monPos.y = monOldY +1;
                    monPos.x = monOldX;
                    break;
                } else if (player.y < monPos.y){
                    monPos.y = monOldY -1;
                    monPos.x = monOldX;
                    break;
                } else {
                    monPos.y = monOldY;
                    monPos.x = monOldX;
                    break;
                }
            } else if (((monPos.x + 1) == obstacle.x || (monPos.x - 1) == obstacle.x)) {
                if (player.x > monPos.x) {
                    monPos.x = monOldX +1;
                    monPos.y = monOldY;
                    break;
                } else if (player.x < monPos.x) {
                    monPos.x = monOldX -1;
                    monPos.y = monOldY;
                    break;
                } else {
                    monPos.y = monOldY;
                    monPos.x = monOldX;
                    break;
                }
            }

        }
    }


}


