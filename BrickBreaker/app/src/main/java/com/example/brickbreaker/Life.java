package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Life extends PowerUp {

    int width;

    public Life(int positionX, int positionY, Resources resource, boolean isDropping) {
        super(positionX, positionY, resource, isDropping);
        powerUp = BitmapFactory.decodeResource(resource, R.drawable.life);
        width = powerUp.getWidth();
        width /= 3;
        powerUp = Bitmap.createScaledBitmap(powerUp, width, width, false);
    }
}
