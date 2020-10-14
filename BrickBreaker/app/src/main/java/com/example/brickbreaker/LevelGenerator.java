package com.example.brickbreaker;

import java.util.ArrayList;
import java.util.List;

import static com.example.brickbreaker.GameActivity.screenX;
import static com.example.brickbreaker.GameActivity.screenY;

public class LevelGenerator {

    public int x;
    public int y;
    public int row;
    public int col;
    int width, height;
    Coordinates coordinates;
    List<Coordinates> coordinatesList;

    public LevelGenerator(int row, int col) {
        coordinatesList = new ArrayList<>();
        this.row = row;
        this.col = col;

        width = screenX/col - screenX/50;
        height = screenY/row;

    }

    public void generate(){
        for(int i=20; i<screenX; i+=width+10){
            for(int j = 20; j<screenY/2; j+=height+5){
                if(i+width < screenX && j+height < screenY/2) {
                    coordinates = new Coordinates(i, j, true);
                    coordinatesList.add(coordinates);
                }
            }
        }

    }



}
