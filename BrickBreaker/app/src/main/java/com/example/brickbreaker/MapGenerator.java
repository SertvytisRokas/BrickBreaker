package com.example.brickbreaker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

import static com.example.brickbreaker.GameActivity.screenX;
import static com.example.brickbreaker.GameActivity.screenY;

public class MapGenerator {

    public int map[][];
    public int width;
    public int height;
    public List<Coordinates> coordinatesList = new ArrayList<>();

    public MapGenerator(int row, int col){
        map = new int[row][col];
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                map[i][j] = 1;
            }
        }

        width = screenX/col;
        height = screenX/row;

        generateCoordinates(row, col);

        for(Coordinates coordinates : coordinatesList){
            System.out.println(coordinates.toString());
        }

    }

    public void generateCoordinates(int width, int height) {
        for(int i=30; i<screenX; i+=width+width/5){
            for(int j=30; j<screenY/2; j+=height+height/5){
                if(i+width < screenX && j+height < screenY/2) {
                    Coordinates coordinate = new Coordinates(i, j);
                    coordinatesList.add(coordinate);
                }
            }

        }
    }

    public void draw(Canvas canvas){
        Paint paint = new Paint();
        int coordListIt = 0;
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                if(map[i][j] > 0){
                    canvas.drawBitmap(createBrick(width, height, false), coordinatesList.get(coordListIt).x, coordinatesList.get(coordListIt).y, paint);
                }
            }
            coordListIt++;
        }
    }

    public void drawLvl1(Canvas canvas){
        Paint paint = new Paint();
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                if(map[i][j] > 0){
                    //canvas.drawBitmap(createBrick());

                }
            }
        }

    }


        public static Bitmap createBrick(int width, int height, boolean isDouble){
        Bitmap brick = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(brick);
        Paint paint = new Paint();
        int newColor= Color.RED;
        paint.setColor(newColor);
        canvas.drawRect(0f, 0f, width, height, paint);
        return brick;
    }


}
