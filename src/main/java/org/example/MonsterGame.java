package org.example;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
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

        TextGraphics textGraphics = terminal.newTextGraphics();

        final char playerCharacter = '\u2661';
        final char block = '\u2588';
        final char monster = '\u2620';
        final char pointChar = 'O';
        playIntro(textGraphics, terminal, monster, pointChar);
        int pointsCollected = 0;

        //generate player
        Position player = new Position(40,3);
        terminal.setCursorPosition(player.x, player.y);
        terminal.putCharacter(playerCharacter);

        // Create Obstacles and Border
        Obstacle obstacleObject = new Obstacle();
        List<Position> obstacles = obstacleObject.obstacles;
        Random r = new Random();
        int randomNumber = r.nextInt(1,4);
        obstacleObject.addObstacle(randomNumber);
        obstacleObject.createBorders();
        drawObstacles(obstacles, terminal, block);
        terminal.flush();

        //Monster instantiations
        Position monPos1 = new Position(77, 22);
        terminal.setCursorPosition(monPos1.x, monPos1.y);
        terminal.putCharacter(monster);

        Position monPos2 = new Position(2, 22);
        terminal.setCursorPosition(monPos2.x, monPos2.y);
        terminal.putCharacter(monster);

        // Spawn points
        List<Position> pointsArray = spawnPoints(obstacles);
        generatePointsObjects(obstacles, pointsArray, pointChar, terminal);

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
            // check if acquired point, give him the point, move the objects around to new locations
            if (playerFoundPoint(obstacles, pointsArray, player)) {
                pointsCollected++;
                generatePointsObjects(obstacles, pointsArray, pointChar, terminal);
                terminal.flush();
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
            // redraw point objects just in case monster stepped on it
            generatePointsObjects(obstacles, pointsArray, pointChar, terminal);

            // check if playerCharacter runs into the monster
            if ((monPos1.x == player.x && monPos1.y == player.y)||(monPos2.x == player.x && monPos2.y == player.y)) {
                endGame(terminal, pointsCollected, textGraphics);
                continueReadingInput = false;
            }

            terminal.flush();
        }
    }

    private static void playIntro(TextGraphics textGraphics, Terminal terminal, char monster, char points) throws IOException {
        textGraphics.putString(10, 6, "Welcome to Space Invader Extreme Terminator Blob Collector 9000", SGR.BOLD);
        textGraphics.putString(30, 8, monster + " Monster bad! RUN! " , SGR.BOLD);
        textGraphics.putString(30, 9, points + " Blobs good! Collect!" , SGR.BOLD);
        textGraphics.putString(20, 12, "Press any letter to start! Good Luck!" , SGR.BOLD);
        KeyStroke keyStroke;
        keyStroke = terminal.readInput();
        if (keyStroke.getKeyType() == KeyType.Character)  {
            terminal.clearScreen();
        }
    }

    private static boolean playerFoundPoint(List<Position> obstacles, List<Position> pointsArray, Position player) {
        Random r = new Random();
        for (Position point : pointsArray) {
            if (player.x == point.x && player.y == point.y) {
                pointsArray.remove(point);
                while (true) {
                    Position potentialPoint = new Position(r.nextInt(4,77), r.nextInt(5,21));
                    boolean crashWithObstacle = false;
                    for (Position obstacle : obstacles) {
                        if (potentialPoint.x == obstacle.x && potentialPoint.y == obstacle.y) {
                            crashWithObstacle = true;
                            break;
                        }
                    }
                    if (!crashWithObstacle) {
                        pointsArray.add(potentialPoint);
                        break;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void generatePointsObjects(List<Position> obstacles, List<Position> pointsArray, char pointChar, Terminal terminal) throws IOException {
        boolean isThereObstacle = false;
        for (Position point : pointsArray) {
            for (Position obstacle : obstacles) {
                if ((point.x == obstacle.x && point.y == obstacle.y)) {
                    isThereObstacle = true;
                }
            }
            if (!isThereObstacle) {
                terminal.setCursorPosition(point.x, point.y);
                terminal.putCharacter(pointChar);
            }
        }
    }

    public static List<Position> spawnPoints(List<Position> obstacles) {
        Random r = new Random();
        List<Position> list = new ArrayList<>();
        while (list.size() != 3) {
            Position potentialPoint = new Position(r.nextInt(4,77), r.nextInt(5,21));
            boolean crashWithObstacle = false;
            for (Position obstacle : obstacles) {
                if (potentialPoint.x == obstacle.x && potentialPoint.y == obstacle.y) {
                    crashWithObstacle = true;
                    break;
                }
            }
            if (!crashWithObstacle) {
                list.add(potentialPoint);
            }
        }
        return list;
    }

    public static void endGame(Terminal terminal, int points, TextGraphics textGraphics) throws IOException, InterruptedException {
        terminal.clearScreen(); //30.6
        textGraphics.putString(30, 6, "Game Over!", SGR.BOLD);
        textGraphics.putString(30, 7, "Scored Points: " + points, SGR.BOLD);
        for (int i = 5; i >= 0; i--) {
            terminal.flush();
            textGraphics.putString(30, 8, "Closing game in: " + i, SGR.BOLD);
            Thread.sleep(1000);
        }
        terminal.close();

    }

    public static void drawObstacles(List<Position> obstacles, Terminal terminal, char block) throws IOException {
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
    public static void helpMonMoveAroundObs(Position monPos, Position player, int monOldX, int monOldY, List<Position> obstacles) {
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


