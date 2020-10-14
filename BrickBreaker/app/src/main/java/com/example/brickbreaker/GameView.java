package com.example.brickbreaker;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;

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
    private double speed = 4;
    private double randomX, randomY;
    private boolean isBallMoving = false;
    private boolean isLevelBuilt = false;
    public int lives = 3;
    public int level = 0;
    public List<Coordinates> coordlist = new ArrayList<>();
    public List<Coordinates> savedCoord = new ArrayList<>();
    public List<BrickParameters> brickParametersList = new ArrayList<>();
    public LevelGenerator levelGenerator;
    private MapGenerator mapGenerator;
    int rows = 7;
    int columns = 5;
    int brickColor = Color.RED;
    int index = 0;
    private int xDelta;
    int totalBricks;

    public GameView(GameActivity gameActivity, int screenX, int screenY, int level) {
        super(gameActivity);
        this.gameActivity = gameActivity;
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1080f / screenX;
        screenRatioY = 1920f / screenY;


        mapGenerator = new MapGenerator(rows, columns);
        totalBricks = rows * columns;
        background = new Background(screenX, screenY, getResources());
        paddle = new Paddle(screenX, getResources());
        ball = new Ball(screenX, screenY, getResources());

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
            mapGenerator.draw(canvas, paint);



//            mapGenerator.draw(canvas);
//            for(int i=0; i<coordlist.size(); i++) {
//                canvas.drawBitmap(createBrick(screenX / 10, screenX / 15, false), coordlist.get(i).x, coordlist.get(i).y, paint);
//            }

            //generateBricks(5, 5, screenX/10, screenX/15, canvas);


//            for(Coordinates coordinates:levelGenerator.coordinatesList){
//                if(coordinates.isAlive()){
//                    Bitmap brick = Bitmap.createBitmap(levelGenerator.width, levelGenerator.height, Bitmap.Config.ARGB_8888);
//                    Paint tempPaint = new Paint();
//                    paint.setColor(brickColor);
//                    canvas.drawBitmap(brick, levelGenerator.coordinatesList.get(index).x, levelGenerator.coordinatesList.get(index).y, paint);
//                    System.out.println(levelGenerator.coordinatesList.get(index).toString());
//                }
//                index++;
//            }



            getHolder().unlockCanvasAndPost(canvas);
        }
    }

//        public static Bitmap createBrick(int width, int height, boolean isDouble){
//        Bitmap brick = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(brick);
//        Paint paint = new Paint();
//        int newColor= Color.RED;
//        paint.setColor(newColor);
//        canvas.drawRect(0f, 0f, width, height, paint);
//        return brick;
//    }


    @Override
    public void run() {
        while(isRunning){

            if(paddle.isMovingLeft == true && ball.isMoving == false || paddle.isMovingRight == true && ball.isMoving == false){
                releaseBall(speed);
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
        if(ball.isMoving == true) {
            checkBrickCollision();
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

    private void checkBrickCollision(){
        A: for(int i=0; i<mapGenerator.map.length; i++){
            for(int j=0; j<mapGenerator.map[0].length; j++){
                if(mapGenerator.map[i][j] > 0){
                    int xCord = j*mapGenerator.width + 80;
                    int yCord = i* mapGenerator.height + 50;
                    int w = mapGenerator.width;
                    int h = mapGenerator.height;

                    Rect rect = new Rect(xCord, yCord, xCord +w, yCord +h);
                    Rect ballRect = new Rect(ball.positionX, ball.positionY, ball.positionX + ball.width, ball.positionY + ball.width);
                    if(Rect.intersects(rect, ballRect)){
                        System.out.println("Collision");
                        mapGenerator.setBrickValue(0, i, j);
                        score++;
                        totalBricks--;
                        if(ball.positionX + ball.width-1 <= rect.left || ball.positionX + 1 >= rect.right){
                            randomX*=-1;
                        } else{
                            randomY*=-1;
                        }
                        break A;
                    }




//                    if(ball.positionY <= yCord + h && ball.positionX + ball.width/2 >= xCord && ball.positionX + ball.width/2 <= xCord + w){
//                        score++;
//                        randomY*=-1;
//                        mapGenerator.setBrickValue(0, i, j);
//                        break A;
//                    }
//                    if(ball.positionY >= yCord + ball.width && ball.positionX + ball.width/2 >= xCord && ball.positionX + ball.width/2 <= xCord + w){
//                        score++;
//                        randomY*=-1;
//                        mapGenerator.setBrickValue(0, i, j);
//                        break A;
//                    }
//                    if(ball.positionX <= xCord + w && ball.positionY + ball.width/2 >= yCord && ball.positionY + ball.width/2 <= yCord + h){
//                        randomX*=-1;
//                        score++;
//                        mapGenerator.setBrickValue(0, i, j);
//                        break A;
//                    }
//                    if(ball.positionX >= xCord - ball.width && ball.positionY + ball.width/2 >= yCord && ball.positionY + ball.width/2 <= yCord + h){
//                        randomX*=-1;
//                        score++;
//                        mapGenerator.setBrickValue(0, i, j);
//                        break A;
//                    }

                }
            }
        }
    }

    private void checkBallCollision(){
        //Left
        if(ball.positionX <= 1){
            randomX = randomX * (-1);
        }
        //Right
        if(ball.positionX >= screenX - ball.width - 1){
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