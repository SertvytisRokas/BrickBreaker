# Brick Breaker game for Android

An android game based on original Brick Breaker created with Android Studio

## Technology

Android Studio

## Languages
Java and Xml

## Activities Structure

The game consists of 3 activities:  
* *Game Activity* - the main activity where the game is played.  
* *Instructions Activity* - activity which display instructions to the player such as controls, objective and power-ups.
* *Game Over Activity* - activity which shows the total score and gives the player options to try again or leave.

## Installation instructions
The game can be installed either by downloading the brickBreaker.apk file and running it on a local android device or by downloading the whole project and running it via Android Studio on a virtual android device.

## How the game actually works
While in the main activity, whenever the player touches the screen, a ball is initialized to go up the screen in a random direction at random speed (all pre-calculated
 so the game would be playable). Player controls the paddle by touching left or right side of the screen and whenever all the bricks are destroyed the player proceeds to the next level.
Levels get more difficult by increasing the ball speed and the number of bricks. Furthermore, the game has four different power-up types:
* **Extra life** - gives an extra life to the player
* **Take away life** - takes one life away from the player
* **Increase paddle size** - increases the size of the paddle
* **Decrease paddle size** - decreases the size of the paddle

All power-ups have 20% of being dropped from a destroyed brick (extra life can spawn only certain amount of times).