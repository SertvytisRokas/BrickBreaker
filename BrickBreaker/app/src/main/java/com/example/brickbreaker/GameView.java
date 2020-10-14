package com.example.brickbreaker;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Debug;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    public boolean isRunning;
    private Background background;
    private Paint paint;
    public static float screenRatioX, screenRatioY;
    private GameActivity gameActivity;
    private int screenX, screenY, score = 0;
    private Paddle paddle;
    private Ball ball;
    private double speed;
    private double randomX, randomY;
    private boolean isBallMoving = false;
    private boolean isLevelBuilt = false;
    public int lives = 3;
    public int level = 0;
    public List<Coordinates> coordlist = new ArrayList<>();
    public List<Coordinates> savedCoord = new ArrayList<>();
    public List<BrickParameters> brickParametersList = new ArrayList<>();
    public MapGenerator mapGenerator;

    private int xDelta;

    public GameView(GameActivity gameActivity, int screenX, int screenY, int level) {
        super(gameActivity);
        this.gameActivity = gameActivity;

        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1080f / screenX;
        screenRatioY = 1920f / screenY;



        mapGenerator = new MapGenerator(3, 7);
        background = new Background(screenX, screenY, getResources());
        paddle = new Paddle(screenX, getResources());
        ball = new Ball(screenX, screenY, getResources());
//        generateCoordinates(10, 8);
        paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);





    }
    private void draw(){
        if(getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, background.x, background.y, paint);

            canvas.drawBitmap(paddle.getPaddle(), paddle.x, paddle.y, paint);
            canvas.drawBitmap(ball.getBall(), ball.positionX, ball.positionY, paint);
            mapGenerator.draw(canvas);
//            for(int i=0; i<coordlist.size(); i++) {
//                canvas.drawBitmap(createBrick(screenX / 10, screenX / 15, false), coordlist.get(i).x, coordlist.get(i).y, paint);
//            }

            //generateBricks(5, 5, screenX/10, screenX/15, canvas);


            getHolder().unlockCanvasAndPost(canvas);
        }
    }
    @Override
    public void run() {
        while(isRunning){

            if(paddle.isMovingLeft == true && ball.isMoving == false || paddle.isMovingRight == true && ball.isMoving == false){
                releaseBall(2.5);
                ball.isMoving = true;
            }
            update();
            draw();
            try {
                sleep();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update(){


        if (paddle.isMovingRight) {
            paddle.x += 15;
            checkBorder();
           // System.out.println(paddle.x);
        }


        if (paddle.isMovingLeft) {
            paddle.x -= 15;
            checkBorder();
        }
        checkBallCollision();

        if(ball.isMoving == true) {
            ball.positionY += randomY;
            ball.positionX += randomX;
        }

    }

    private void reset(){
        ball = new Ball(screenX, screenY, getResources());
        paddle = new Paddle(screenX, getResources());
        lives--;
    }

    private void gameOver(){
        gameActivity.finish();
    }


    private void checkBorder(){
        if(paddle.x < 0){
            paddle.x = 0;
        }
        if(paddle.x >= screenX - paddle.width){
            paddle.x = screenX - paddle.width;
        }
    }

    private void checkBallCollision(){
        //Left
        if(ball.positionX <= 0){
            randomX = randomX * (-1);
        }
        //Right
        if(ball.positionX >= screenX - ball.width){
            randomX = randomX * (-1);
        }
        //Top
        if(ball.positionY <= 0){
            randomY = randomY * (-1);
        }

        //Paddle
        if(screenY - ball.positionY - ball.width <= screenY - paddle.y && ball.positionX + ball.width/2 >= paddle.x && ball.positionX + ball.width/2 <= paddle.x + paddle.width){
            //Left
            if(ball.positionX + ball.width/2 < paddle.x + paddle.width/15){
                if(randomX > 0){
                    randomX*=-1;
                }
            }

            //Right
            if(ball.positionX + ball.width/2 > paddle.x + paddle.width/15 * 14){
                if(randomX < 0){
                    randomX*=-1;
                }
            }
            randomY*=-1;
        }

        if(screenY - ball.positionY - ball.width < screenY - paddle.y - paddle.height/2 && ball.isMoving){
            if(lives > 0) {
                reset();
            } else{
                gameOver();
            }
        }
    }

    private void sleep() throws InterruptedException {
        Thread.sleep(17);
    }

    public void resume(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void releaseBall(double speed){
        randomX = (-3 + Math.random() * ((-3)-3)) * speed;
        randomY = (-3 + Math.random() * ((-3)-3)) * speed;
        System.out.println(randomX);
        System.out.println(randomY);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(event.getX() < screenX/2){
                    paddle.isMovingLeft = true;
                } else{
                    paddle.isMovingRight = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                paddle.isMovingLeft = false;
                paddle.isMovingRight = false;
                break;
        }
        return true;
    }
}


//    public void generateBricks(int numOfRows, int numOfColumns, int width, int height, Canvas canvas){
//        for(int row = 30; row<screenY/2; row+=screenX/numOfRows){
//            for(int col = 30; col<screenX - screenX/15; col+= screenX/numOfColumns){
//                if(!checkIfBallCollides(col, row, height, width)){
//                    for(Coordinates coordinates : savedCoord) {
//                        if(!(coordinates.x == col && coordinates.y == row)) {
//                            canvas.drawBitmap(createBrick(width, height, false), col, row, paint);
//                        }
//                    }
//                } else{
//                    Coordinates coordinates = new Coordinates(col, row);
//                    savedCoord.add(coordinates);
//                    score++;
//                }
//            }
//        }
//
//    }


//    public boolean checkIfBallCollides(int positionX, int positionY, int height, int width){
//        boolean isCollision = false;
//        if(ball.positionY + ball.width/2 >= positionY && ball.positionY + ball.width/2 <= positionY + height && ball.positionX >= positionX - ball.width){
////            randomX*=-1;
//            isCollision = true;}
//        if(ball.positionY + ball.width/2 >= positionY && ball.positionY + ball.width/2 <= positionY + height && ball.positionX <= positionX + width){
////            randomX*=-1;
//            isCollision = true;}
//        if(ball.positionX + ball.width/2 >= positionX && ball.positionX + ball.width/2 <= positionX + width && ball.positionY <= positionY + height){
////            randomY*=-1;
//            isCollision = true;}
//        if(ball.positionX + ball.width/2 >= positionX && ball.positionX + ball.width/2 <= positionX + width && ball.positionY >= positionY + ball.width){
////            randomY*=-1;
//            isCollision = true;}
//        return isCollision;
//    }

//    public void generateCoordinates(int numOfRows, int numOfColumns) {
//        for (int row = 30; row < screenY / 2; row += screenX/numOfRows) {
//            for (int col = 30; col < screenX - screenX/15; col += screenX/numOfColumns) {
//                Coordinates coordinates = new Coordinates(col, row);
//                coordlist.add(coordinates);
//
//            }
//        }
//    }
//    public static Bitmap createBrick(int width, int height, boolean isDouble){
//        Bitmap brick = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(brick);
//        Paint paint = new Paint();
//        int newColor= Color.RED;
//        paint.setColor(newColor);
//        canvas.drawRect(0f, 0f, width, height, paint);
//        return brick;
//    }



//    public void drawBricks(int level, Canvas canvas)



//    public void buildBricks(int level){
//        Brick testBrick = new Brick(getResources(), false);
//        if(level == 1){
//            bricks = new Brick[100];
//            int counter = 0;
//            for(int row = 30; row<screenY/2; row+=testBrick.height) {
//                for (int col = 30; col < screenX - testBrick.width; col += testBrick.width) {
//                    Brick brick = new Brick(getResources(), false);
//                    bricks[counter] = brick;
//                    counter++;
//                }
//            }
//
//
//
//        }
//    }