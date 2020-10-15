package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.example.brickbreaker.GameView.screenRatioX;
import static com.example.brickbreaker.GameView.screenRatioY;

public class Paddle {

    int x, y, width, height;
    Bitmap paddle;
    boolean isMovingLeft = false;
    boolean isMovingRight = false;

    Paddle(int screenX, Resources resource){



        paddle = BitmapFactory.decodeResource(resource, R.drawable.paddle);

        width = paddle.getWidth();
        height = paddle.getHeight();
        width /= 20;

        width = (int) (width * screenRatioX);
        height -= (int) (height * screenRatioY);

        paddle = Bitmap.createScaledBitmap(paddle, width, height, false);

        y = (int) (2400 * screenRatioY);
        x = screenX / 2 - width/2;

    }

    Bitmap getPaddle(){
        return paddle;
    }

}
