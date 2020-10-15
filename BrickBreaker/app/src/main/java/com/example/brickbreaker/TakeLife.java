package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class TakeLife extends PowerUp {

    int width;

    public TakeLife(int positionX, int positionY, Resources resources, boolean isDropping) {
        super(positionX, positionY, resources, isDropping);
        powerUp = BitmapFactory.decodeResource(resources, R.drawable.takelife);
        width = powerUp.getWidth();
        width /= 3;
        powerUp = Bitmap.createScaledBitmap(powerUp, width, width, false);
    }
}
