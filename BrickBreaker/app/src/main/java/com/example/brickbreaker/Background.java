package com.example.brickbreaker;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Background {
    int x,y;
    Bitmap background;

    Background(int screenX, int screenY, Resources resource){
        background = BitmapFactory.decodeResource(resource, R.drawable.background);
        background = Bitmap.createScaledBitmap(background, screenX, screenY, false);
    }
}
