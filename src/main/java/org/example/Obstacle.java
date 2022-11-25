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
                for (int i = 0; i < 25; i++) {
                    this.obstacles.add(new Position(10 + i, 10));
                }
                break;
            case 2:
                for (int i = 0; i < 25; i++) {
                    this.obstacles.add(new Position(20 + i, 10));
                }
                break;
            case 3:
                for (int i = 6; i < 18; i++) {
                    this.obstacles.add(new Position(40, i));
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

