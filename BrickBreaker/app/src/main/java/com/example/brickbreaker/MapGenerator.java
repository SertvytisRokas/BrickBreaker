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

    int map[][];
    int width, height;

    public MapGenerator(int row, int col){
        map = new int[row][col];
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                map[i][j] = 1;
            }
        }

        width = (screenX-screenX/10)/col;
        height = (screenY/4)/row;
    }

    public void draw(Canvas canvas, Paint paint){
        for(int i=0; i<map.length; i++){
            for(int j=0; j<map[0].length; j++){
                if(map[i][j] > 0){
                    canvas.drawBitmap(createBrick(width-10, height-10, false), j*width+80, i*height+50, paint);
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

    public void setBrickValue(int value, int row, int col){
        map[row][col] = value;
    }



}
