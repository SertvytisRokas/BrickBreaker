package com.example.brickbreaker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;
    public static int coordinateX, coordinateY;
    public static int screenX, screenY;

    private int xDelta;
    private ViewGroup mainLayout;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        screenX = point.x;
        screenY = point.y;
        gameView = new GameView(this, point.x, point.y, 1);
        coordinateX = point.x;
        coordinateY = point.y;

        setContentView(gameView);
    }


    @Override
    protected void onPause(){
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        gameView.resume();
    }
}