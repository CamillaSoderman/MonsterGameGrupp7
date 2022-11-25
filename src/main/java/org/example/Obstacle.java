package org.example;
import java.util.ArrayList;

public class Obstacle {

    public ArrayList<Position> obstacles;

    public Obstacle() {
        this.obstacles = new ArrayList<>();
    }

    public void createBorders() {
        for (int i = 0; i < 81; i++) {
            this.obstacles.add(new Position(i, 0));
        }
        for (int i = 0; i < 81; i++) {
            this.obstacles.add(new Position(i, 24));
        }
        for (int i = 0; i < 25; i++) {
            this.obstacles.add(new Position(0, i));
        }
        for (int i = 0; i < 25; i++) {
            this.obstacles.add(new Position(80, i));
        }
    }

    public void addObstacle(int level) {
        switch (level) {
            case 1:
                for (int i = 10; i < 20; i++) {
                    this.obstacles.add(new Position(20, i));
                }
                for (int i = 5; i < 7; i++) {
                    this.obstacles.add(new Position(40, i));
                }
                for (int i = 7; i < 15; i++) {
                    this.obstacles.add(new Position(60, i));
                }
                for (int i = 30; i < 50; i++) {
                    this.obstacles.add(new Position(i, 15));
                }
                break;
            case 2:
                for (int i = 10; i < 20; i++) {
                    this.obstacles.add(new Position(60, i));
                }
                for (int i = 10; i < 20; i++) {
                    this.obstacles.add(new Position(12, i));
                }
                for (int i = 30; i < 50; i++) {
                    this.obstacles.add(new Position(i, 20));
                }
                for (int i = 30; i < 50; i++) {
                    this.obstacles.add(new Position(i, 7));
                }
                break;
            case 3:
                for (int i = 6; i < 17; i++) {
                    this.obstacles.add(new Position(60, i));
                }
                for (int i = 10; i < 20; i++) {
                    this.obstacles.add(new Position(12, i));
                }
                for (int i = 15; i < 30; i++) {
                    this.obstacles.add(new Position(i, 6));
                }
                for (int i = 30; i < 50; i++) {
                    this.obstacles.add(new Position(i, 14));
                }
                break;
        }
    }
}
        // horizontell linje
//        for(int i = 0;i<25;i++){
//          this.obstacles.add(new Position(10+i, 10));
//       }
//        //vertical linje
//        for(int i = 6;i<18;i++){
//            this.obstacles.add(new Position(40, i));
//        }


//

