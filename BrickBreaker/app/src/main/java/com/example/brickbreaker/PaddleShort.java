package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class PaddleShort extends PowerUp {

    public int width;

    public PaddleShort(int positionX, int positionY, Resources resources, boolean isDropping) {
        super(positionX, positionY, resources, isDropping);
        powerUp = BitmapFactory.decodeResource(resources, R.drawable.paddledecrease);
        width = powerUp.getWidth();
        width /= 3;
        powerUp = Bitmap.createScaledBitmap(powerUp, width, width, false);
    }
}
