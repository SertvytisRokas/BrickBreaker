package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;

public class PowerUp {

    int positionX;
    int positionY;
    Bitmap powerUp;
    boolean isDropping;
    int width;

    public PowerUp(int positionX, int positionY, Resources resources, boolean isDropping) {

        this.positionX = positionX;
        this.positionY = positionY;
        this.isDropping = isDropping;

    }

    Bitmap getPowerUp() {
        return powerUp;
    }

}
