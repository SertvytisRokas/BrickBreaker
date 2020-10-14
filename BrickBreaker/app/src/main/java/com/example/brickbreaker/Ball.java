package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static com.example.brickbreaker.GameView.screenRatioX;
import static com.example.brickbreaker.GameView.screenRatioY;

public class Ball {

    int width;
    int height;
    int positionX;
    int positionY;
    double directionX;
    double directionY;
    Bitmap ball;
    boolean isMoving = false;

    public Ball(int screenX, int screenY, Resources resource) {

        ball = BitmapFactory.decodeResource(resource, R.drawable.ball);

        width = ball.getWidth();
        height = ball.getHeight();

        width /=5;
        height /=5;

        ball = Bitmap.createScaledBitmap(ball, width, height, false);

        positionX = screenX / 2 - width / 2;
        positionY = screenY - height / 2 - 175;

    }

    Bitmap getBall(){
        return ball;
    }
}
