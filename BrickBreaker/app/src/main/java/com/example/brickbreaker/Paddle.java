package com.example.brickbreaker;

import android.content.ClipData;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import static com.example.brickbreaker.GameActivity.coordinateY;
import static com.example.brickbreaker.GameView.screenRatioX;
import static com.example.brickbreaker.GameView.screenRatioY;

public class Paddle {

    int x, y, width, height;
    Bitmap paddle;
    boolean isMovingLeft = false;
    boolean isMovingRight = false;
    private int xDelta;

    private ImageView image;
    private ViewGroup mainLayout;

    Paddle(int screenX, Resources resource){



        paddle = BitmapFactory.decodeResource(resource, R.drawable.paddle);

        width = paddle.getWidth();
        height = paddle.getHeight();
        width /= 15;

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
