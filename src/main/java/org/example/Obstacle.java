package org.example;
import java.util.ArrayList;

public class Obstacle {

    public ArrayList<Position> obstacles;

    public Obstacle() {
        this.obstacles = new ArrayList<>();
    }

    public void createBorders() {
        for (int i = 0; i < 81; i++) {
            this.obstacles.add(new Position(i,0));
        }
        for (int i = 0; i < 81; i++) {
            this.obstacles.add(new Position(i,24));
        }
        for (int i = 0; i < 25; i++) {
            this.obstacles.add(new Position(0,i));
        }
        for (int i = 0; i < 25; i++) {
            this.obstacles.add(new Position(80,i));
        }
    }
    public void addObstacle() {
        for(int i = 0;i<25;i++){
          this.obstacles.add(new Position(10+i, 10));
       }
        for(int i = 6;i<18;i++){
            this.obstacles.add(new Position(40, i));
        }
        for(int i = 6;i<18;i++){
            this.obstacles.add(new Position(60, i));
        }
    }
//    private static List<Position> generateObstacles(Terminal terminal) {
//        List<Position> obstacles = new ArrayList<>();
//
//
//
//        Position[] obstacles = new Position[50];
//        for(int i = 0;i<50;i++){
//            obstacles[i] = new Position(10+i, 10);
//        }
//
//        // Use obstacles array to print to lanterna
//        for (Position p : obstacles) {
//            terminal.setCursorPosition(p.x, p.y);
//            terminal.putCharacter(block);
//        }
//        return
//    }
}
