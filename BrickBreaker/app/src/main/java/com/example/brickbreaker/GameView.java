package com.example.brickbreaker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {
    private Thread thread;
    public boolean isRunning;
    private Background background;
    private Paint paint;
    public static float screenRatioX, screenRatioY;
    private GameActivity gameActivity;
    private int screenX, screenY;
    public int score = 0;
    private Paddle paddle;
    private Ball ball;
    private double speed = 4;
    private double randomX, randomY;
    private boolean isBallMoving = false;
    public int lives = 3;
    public int level = 1;
    private MapGenerator mapGenerator;
    int rows = 6;
    int columns = 3;
    int totalBricks;
    boolean isGameOver = false;
    Canvas canvas;
    boolean needNewLevel = false;
    int collisionCounter;
    int whenToResetCollCounter;
    int numOfExtraLives = 3;
    PowerUp life;
    PowerUp paddleLengthen;
    PowerUp paddleShort;
    PowerUp takeLife;
    int originalPaddleWidth;
    private SoundPool soundPool;
    private int sound;

    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super(gameActivity);
        this.gameActivity = gameActivity;
        this.screenX = screenX;
        this.screenY = screenY;
        screenRatioX = 1080f / screenX;
        screenRatioY = 1920f / screenY;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().
                    setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).
                    setUsage(AudioAttributes.USAGE_GAME).
                    build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        } else {
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        }
        sound = soundPool.load(gameActivity, R.raw.hit, 1);
        mapGenerator = new MapGenerator(rows, columns);
        totalBricks = rows * columns;
        background = new Background(screenX, screenY, getResources());
        paddle = new Paddle(screenX, getResources());
        ball = new Ball(screenX, screenY, getResources());
        life = new Life(-50, -50, getResources(), false);
        paddleLengthen = new PaddleLengthen(-50, -50, getResources(), false);
        paddleShort = new PaddleShort(-50, -50, getResources(), false);
        takeLife = new TakeLife(-50, -50, getResources(), false);
        paint = new Paint();
        paint.setTextSize(50);
        paint.setColor(Color.WHITE);
        originalPaddleWidth = paddle.width;
    }


    private void draw() {
        if (getHolder().getSurface().isValid()) {
            canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background.background, background.x, background.y, paint);
            canvas.drawText("Current score: " + score, screenX / 3, 50, paint);
            canvas.drawText("Lives: " + lives, screenX / 4 * 3, 50, paint);
            canvas.drawText("Level: " + level, screenX / 40, 50, paint);
            if (life.isDropping && ball.isMoving) {
                canvas.drawBitmap(life.getPowerUp(), life.positionX, life.positionY, paint);
            }
            if (paddleLengthen.isDropping && ball.isMoving) {
                canvas.drawBitmap(paddleLengthen.getPowerUp(), paddleLengthen.positionX, paddleLengthen.positionY, paint);
            }
            if (paddleShort.isDropping && ball.isMoving) {
                canvas.drawBitmap(paddleShort.getPowerUp(), paddleShort.positionX, paddleShort.positionY, paint);
            }
            if (takeLife.isDropping && ball.isMoving) {
                canvas.drawBitmap(takeLife.getPowerUp(), takeLife.positionX, takeLife.positionY, paint);
            }
            if (totalBricks <= 0) {
                resetBallAndPaddle();
                canvas.drawText("Level completed!", screenX / 3, screenY / 2, paint);
                canvas.drawText("Click anywhere to continue..", screenX / 4, screenY / 2 + screenY / 6, paint);
                if (needNewLevel) {
                    resetBallAndPaddle();
                    newLevel(level);
                    mapGenerator = new MapGenerator(++rows, ++columns);
                    totalBricks = rows * columns;
                    needNewLevel = false;
                }
                getHolder().unlockCanvasAndPost(canvas);
                return;
            }
            canvas.drawBitmap(paddle.getPaddle(), paddle.x, paddle.y, paint);
            canvas.drawBitmap(ball.getBall(), ball.positionX, ball.positionY, paint);
            mapGenerator.draw(canvas, paint);
            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void newLevel(int level) {
        if (level <= 3) {
            whenToResetCollCounter = 5;
            increaseBallSpeed(collisionCounter);
            this.numOfExtraLives = 3;
        }
        if (level > 3 && level <= 6) {
            rows += 1;
            speed += 2;
            this.numOfExtraLives = 4;
        }
        if (level > 6 && level < 15) {
            rows += 2;
            columns += 1;
            speed += 3;
            this.numOfExtraLives = 5;
        }
    }

    void resetBallAndPaddle() {
        ball = new Ball(screenX, screenY, getResources());
        paddle = new Paddle(screenX, getResources());
    }

    @Override
    public void run() {
        while (isRunning) {
            if (paddle.isMovingLeft && !ball.isMoving || paddle.isMovingRight && !ball.isMoving) {
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

    void increaseBallSpeed(int collisionCounter) {
        if (collisionCounter >= whenToResetCollCounter) {
            randomY += 0.5;
        }
    }

    void checkGameOver() {
        if (lives <= 0) {
            isGameOver = true;
        }
    }

    void checkTakeLifeCollision() {
        Rect takeLifeRect = new Rect(takeLife.positionX, takeLife.positionY, takeLife.positionX + takeLife.width, takeLife.positionY + takeLife.width);
        Rect paddleRect = new Rect(paddle.x, paddle.y, paddle.x + paddle.width, paddle.y + paddle.height);
        if (Rect.intersects(takeLifeRect, paddleRect)) {
            takeAwayLife();
            paddleLengthen.isDropping = false;
            paddleLengthen.positionX = -50;
            paddleLengthen.positionY = -50;
        } else if (paddleLengthen.positionY >= screenY) {
            paddleLengthen.isDropping = false;
            paddleLengthen.positionX = -50;
            paddleLengthen.positionY = -50;
        }
    }

    private void takeAwayLife() {
        lives--;
    }

    void checkPaddleLengthenCollision() {
        Rect paddleLRect = new Rect(paddleLengthen.positionX, paddleLengthen.positionY, paddleLengthen.positionX + paddleLengthen.width, paddleLengthen.positionY + paddleLengthen.width);
        Rect paddleRect = new Rect(paddle.x, paddle.y, paddle.x + paddle.width, paddle.y + paddle.height);
        if (Rect.intersects(paddleLRect, paddleRect)) {
            increasePaddleWidth();
            paddleLengthen.isDropping = false;
            paddleLengthen.positionX = -50;
            paddleLengthen.positionY = -50;
        } else if (paddleLengthen.positionY >= screenY) {
            paddleLengthen.isDropping = false;
            paddleLengthen.positionX = -50;
            paddleLengthen.positionY = -50;
        }
    }

    void checkPaddleShortCollision() {
        Rect paddleSRect = new Rect(paddleShort.positionX, paddleShort.positionY, paddleShort.positionX + paddleShort.width, paddleShort.positionY + paddleShort.width);
        Rect paddleRect = new Rect(paddle.x, paddle.y, paddle.x + paddle.width, paddle.y + paddle.height);
        if (Rect.intersects(paddleSRect, paddleRect)) {
            decreasePaddleWidth();
            paddleShort.isDropping = false;
            paddleShort.positionX = -50;
            paddleShort.positionY = -50;
        } else if (paddleShort.positionY >= screenY) {
            paddleShort.isDropping = false;
            paddleShort.positionX = -50;
            paddleShort.positionY = -50;
        }
    }

    void checkLifeCollision() {
        Rect lifeRect = new Rect(life.positionX, life.positionY, life.positionX + life.width, life.positionY + life.width);
        Rect paddleRect = new Rect(paddle.x, paddle.y, paddle.x + paddle.width, paddle.y + paddle.height);

        if (Rect.intersects(lifeRect, paddleRect)) {
            lives++;
            numOfExtraLives--;
            life.isDropping = false;
            life.positionX = -50;
            life.positionY = -50;
        } else if (life.positionY >= screenY) {
            life.isDropping = false;
            life.positionX = -50;
            life.positionY = -50;
        }
    }

    private void update() {
        checkGameOver();
        if (life.isDropping && ball.isMoving) {
            life.positionY += 10;
            checkLifeCollision();
        }
        if (paddleLengthen.isDropping && ball.isMoving) {
            paddleLengthen.positionY += 10;
            checkPaddleLengthenCollision();
        }
        if (paddleShort.isDropping && ball.isMoving) {
            paddleShort.positionY += 30;
            checkPaddleShortCollision();
        }
        if (takeLife.isDropping && ball.isMoving) {
            takeLife.positionY += 30;
            checkTakeLifeCollision();
        }
        if (isGameOver) {
            Intent intent = new Intent().setClass(gameActivity.getApplication(), GameOverActivity.class);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            String stringScore = String.valueOf(score);
            intent.putExtra("SCORE", stringScore);
            gameActivity.getApplication().startActivity(intent);
        }
        if (paddle.isMovingRight) {
            paddle.x += 15;
            checkBorder();
        }
        if (paddle.isMovingLeft) {
            paddle.x -= 15;
            checkBorder();
        }
        checkBallCollision();
        if (ball.isMoving) {
            ball.positionY += randomY;
            ball.positionX += randomX;
        }
        if (ball.isMoving) {
            checkBrickCollision();
        }
    }

    void dropExtraLife(int positionX, int positionY) {
        life = new Life(positionX, positionY, getResources(), true);
    }

    void dropPaddleLengthen(int positionX, int positionY) {
        paddleLengthen = new PaddleLengthen(positionX, positionY, getResources(), true);
    }

    void dropPaddleShort(int positionX, int positionY) {
        paddleShort = new PaddleShort(positionX, positionY, getResources(), true);
    }

    void dropTakeLife(int positionX, int positionY) {
        takeLife = new TakeLife(positionX, positionY, getResources(), true);
    }

    void increasePaddleWidth() {
        if (paddle.width < screenX / 5 * 2) {
            paddle.width += 75;
            paddle.paddle = Bitmap.createScaledBitmap(paddle.paddle, paddle.width, paddle.height, false);
        }
    }

    void decreasePaddleWidth() {
        if (paddle.width > originalPaddleWidth / 3 * 2) {
            paddle.width -= 75;
            paddle.paddle = Bitmap.createScaledBitmap(paddle.paddle, paddle.width, paddle.height, false);
        }
    }

    private void reset() {
        ball = new Ball(screenX, screenY, getResources());
        paddle = new Paddle(screenX, getResources());
        lives--;
    }

    private void checkBorder() {
        if (paddle.x < 0) {
            paddle.x = 0;
        }
        if (paddle.x >= screenX - paddle.width) {
            paddle.x = screenX - paddle.width;
        }
    }

    private boolean checkIfPowerUp() {
        Random random = new Random();
        int randomValue = random.nextInt((10 - 1) + 1) + 1;
        if (randomValue <= 2) {
            return true;
        } else {
            return false;
        }
    }

    private void checkBrickCollision() {
        A:
        for (int i = 0; i < mapGenerator.map.length; i++) {
            for (int j = 0; j < mapGenerator.map[0].length; j++) {
                if (mapGenerator.map[i][j] > 0) {
                    int xCord = j * mapGenerator.width + 80;
                    int yCord = i * mapGenerator.height + 50;
                    int w = mapGenerator.width;
                    int h = mapGenerator.height;
                    Rect rect = new Rect(xCord, yCord, xCord + w, yCord + h);
                    Rect ballRect = new Rect(ball.positionX, ball.positionY, ball.positionX + ball.width, ball.positionY + ball.width);
                    if (Rect.intersects(rect, ballRect)) {
                        soundPool.play(sound, 1, 1, 0, 0, 1);
                        try {
                            if (numOfExtraLives > 0 && !life.isDropping) {
                                if (checkIfPowerUp()) {
                                    dropExtraLife(xCord + xCord / 2, yCord + yCord / 2);
                                }
                            }
                            if (!paddleLengthen.isDropping) {
                                if (checkIfPowerUp()) {
                                    dropPaddleLengthen(xCord + xCord / 2, yCord + yCord / 2);
                                }
                            }
                            if (!paddleShort.isDropping) {
                                if (checkIfPowerUp()) {
                                    dropPaddleShort(xCord + xCord / 2, yCord + yCord / 2);
                                }
                            }
                            if (!takeLife.isDropping) {
                                if (checkIfPowerUp()) {
                                    dropTakeLife(xCord + xCord / 2, yCord + yCord / 2);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        collisionCounter++;
                        mapGenerator.setBrickValue(0, i, j);
                        score++;
                        totalBricks--;
                        if (ball.positionX + ball.width - 1 <= rect.left || ball.positionX + 1 >= rect.right) {
                            randomX *= -1;
                        }

                        if (ball.positionY + 1 >= rect.top || ball.positionY + ball.width - 1 <= rect.bottom) {
                            randomY *= -1;
                        }
                        break A;
                    }
                }
            }
        }
    }

    private void checkBallCollision() {
        Rect ballRect = new Rect(ball.positionX, ball.positionY, ball.positionX + ball.width, ball.positionY + ball.width);
        Rect paddleRect = new Rect(paddle.x, paddle.y, paddle.x + paddle.width, paddle.y + paddle.height);
        if (Rect.intersects(ballRect, paddleRect)) {
            if (ball.positionY + ball.width >= paddle.x) {
                if (randomY < 30) {
                    System.out.println("RandomY: " + randomY);
                    randomY += 3;
                }
                if (ball.positionX + ball.width / 2 < paddle.x + paddle.width / 15) {
                    if (randomX > 0) {
                        randomX += 2;
                        randomX *= -1;
                    }
                }
                if (ball.positionX + ball.width / 2 > paddle.x + paddle.width / 15 * 14) {
                    if (randomX < 0) {
                        randomX -= 2;
                        randomX *= -1;
                    }
                }
                randomY *= -1;
            }

        }
        //Left Border
        if (ball.positionX <= 1) {
            randomX = randomX * (-1);
        }
        //Right Border
        if (ball.positionX >= screenX - ball.width - 1) {
            randomX = randomX * (-1);
        }
        //Top Border
        if (ball.positionY <= 50) {
            randomY = randomY * (-1);
        }
        if (screenY - ball.positionY - ball.width < screenY - paddle.y - paddle.height / 2 && ball.isMoving) {
            if (lives > 0) {
                reset();
            } else {
                isGameOver = true;
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (totalBricks <= 0) {
                    level++;
                    needNewLevel = true;
                    break;
                }
                if (event.getX() < screenX / 2) {
                    paddle.isMovingLeft = true;
                } else {
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