package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Ball {

    int width;
    int height;
    int positionX;
    int positionY;
    Bitmap ball;
    boolean isMoving = false;

    public Ball(int screenX, int screenY, Resources resource) {
        ball = BitmapFactory.decodeResource(resource, R.drawable.ball);
        width = ball.getWidth();
        height = ball.getHeight();
        width /= 7;
        height /= 7;
        ball = Bitmap.createScaledBitmap(ball, width, height, false);
        positionX = screenX / 2 - width / 2;
        positionY = screenY - height / 2 - 175;
    }

    Bitmap getBall(){
        return ball;
    }
}
