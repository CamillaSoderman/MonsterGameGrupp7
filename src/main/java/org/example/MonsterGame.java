package org.example;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
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

        Position player = new Position(40,3);
        final char playerCharacter = '\u2661';
        final char block = '\u2588';
       char monster = '\u2620';
        terminal.setCursorPosition(player.x, player.y);
        terminal.putCharacter(playerCharacter);

        //Monster monsters = new Monster();
        //monsters.getMonster();

        // Create Obstacles and Border
        Obstacle obstacleObject = new Obstacle();
//        obstacleObject.createBorders();
        List<Position> obstacles = obstacleObject.obstacles;
        obstacleObject.addObstacle();
        obstacleObject.createBorders();
        drawObstacles(obstacles, terminal, block);


//        Random r = new Random();
//        Position monPos = new Position(r.nextInt(40,80), r.nextInt(24));;


       // char[] mon = new char[]{monster1, monster2};
        //for(int i= 0; i<mon.length; i++) {


            Position monPos1 = new Position(77, 22);
            terminal.setCursorPosition(monPos1.x, monPos1.y);
            terminal.putCharacter(monster);


            Position monPos2 = new Position(2, 22);
            terminal.setCursorPosition(monPos2.x, monPos2.y);
            terminal.putCharacter(monster);

            terminal.flush();
        //}

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
            int monOldX1 = monPos1.x;
            int monOldY1 = monPos1.y;
            int monOldX2 = monPos2.x;
            int monOldY2 = monPos2.y;

            setMonsterCoordinates(monPos1, player);
            boolean monsterCrash1 = checkForObstacleCrash(obstacles, monPos1);
            setMonsterCoordinates(monPos2, player);
            boolean monsterCrash2 = checkForObstacleCrash(obstacles, monPos2);
            // helps monster move around obstacle when hitting it
            if (monsterCrash1) {
                helpMonMoveAroundObs(monPos1, player, monOldX1, monOldY1, obstacles);
                moveObject(monOldX1, monOldY1, monPos1, monster, terminal);

            } else {
                moveObject(monOldX1, monOldY1, monPos1, monster, terminal);

            }

            if(monsterCrash2) {
                helpMonMoveAroundObs(monPos2, player, monOldX2, monOldY2, obstacles);
                moveObject(monOldX2, monOldY2, monPos2, monster, terminal);
            }else{
                moveObject(monOldX2, monOldY2, monPos2, monster, terminal);

            }
            // check if playerCharacter runs into the monster
            if (monPos1.x == player.x && monPos1.y == player.y||monPos2.x == player.x && monPos2.y == player.y) {
                terminal.close();
                continueReadingInput = false;

            }

            terminal.flush();
        }
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
//        if (!isVertical) {
//                if (player.x > monPos.x) {
//                    monPos.x = monOldX +1;
//                    monPos.y = monOldY;
//                } else if (player.x < monPos.x) {
//                    monPos.x = monOldX -1;
//                    monPos.y = monOldY;
//                } else {
//                    monPos.y = monOldY;
//                    monPos.x = monOldX;
//                }
//        }
    }


}


