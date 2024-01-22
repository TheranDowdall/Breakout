//Import swing, awt and awt.event for rendering
//Import io and util for file reading

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author (Theran D)
 * @version (June 14th, 2023)
 * @description The main center for logic.
 * Ties all of Breakout's individual components together.
 * Runs logic to check collisions, update positions and run
 * main gameplay loop.
 */
public class Game {
    //Declare constants for width and height of game screen
    //Gameplay  window size will adjust to match these constants
    public final static short SCREEN_WIDTH = 1080;
    public final static short SCREEN_HEIGHT = 480;
    private static float fltGameSpeed = 1.0f;

    //Declare private constants for the number of bricks in a level
    //And the time to wait in between game frames
    private final static long FRAME_DELAY = 2000000;
    private final static short NUMBER_OF_BRICKS = 13 * 7;

    //Set static variables for the number of bricks broken and game speed
    private static byte bytBricksBroken = 0;

    //Create JFrame to draw visual game elements onto
    private JFrame fraGameWindow;

    //Instance variables for all visual and functional components of the game
    private Score scrScore; //Score and level UI
    private Box[] arrWalls; //Sides of the screen
    private Brick[][] arrBricks; //Bricks for a level
    private Ball balBall; //The chaotic ball bouncing around screen
    private Paddle padPlayer; //The player-controlled paddle

    //Declare and initialize a variable which tracks if the window has been closed
    private boolean bolClosed = false;

    /**
     * @description Default constructor for a Game object.
     * This constructor initializes all game components and adds GUI components
     * to the game's frame.
     * <p>
     * Additionally, it configures the frame itself.
     * @author (Theran D)
     * @version (June 9th, 2023)
     */
    private Game() {
        //initialize the bricks array
        this.arrBricks = new Brick[7][13];

        //initialize the box array of walls
        this.arrWalls = new Box[3];

        //Initialize the ball
        this.balBall = new Ball();

        //Initialize the paddle
        this.padPlayer = new Paddle();

        //Initialize the game score
        this.scrScore = new Score();

        //Initialize individual wall boxes
        this.arrWalls[0] = new Box(0, SCREEN_HEIGHT / 2, 0, SCREEN_HEIGHT, Color.BLACK);
        this.arrWalls[1] = new Box(SCREEN_WIDTH - 10, SCREEN_HEIGHT / 2, 0, SCREEN_HEIGHT, Color.BLACK);
        this.arrWalls[2] = new Box(SCREEN_WIDTH / 2, 0, SCREEN_WIDTH, 0, Color.BLACK);

        //declare and initialize a helper variable m
        //This will determine vertical location of initialized bricks
        int m = SCREEN_HEIGHT / 10;

        //Initialize every brick in the arrBricks 2D array
        //Take spacing of boxes into consideration
        for (int i = 0; i < this.arrBricks.length; i++) {
            //This loop traverses across a row of bricks
            for (int j = SCREEN_WIDTH / 26, k = 0; k < this.arrBricks[i].length; j += SCREEN_WIDTH / 13, k++) {
                this.arrBricks[i][k] = new Brick(j, m, SCREEN_WIDTH / 14, SCREEN_HEIGHT / 24);
            }

            //Update vertical location to start traversing the next row
            m += SCREEN_HEIGHT / 20;
        }

        //Create a new frame to hold GUIs and initialize its size
        this.fraGameWindow = new JFrame("Breakout");
        this.fraGameWindow.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        //Add every wall visual to the frame
        for (Box b : arrWalls)
            this.fraGameWindow.add(b.getGraphics());

        //Add all brick visuals to the frame
        for (Brick[] a : arrBricks) {
            for (Brick b : a)
                this.fraGameWindow.add(b.getGraphics());
        }

        //Add player and acore graphics to the frame
        this.fraGameWindow.add(padPlayer.getGraphics());
        this.fraGameWindow.add(scrScore.getScoreGraphics());
        this.fraGameWindow.add(scrScore.getLevelGraphics());

        //Add ball graphics to frame
        //ATTENTION: This must be done last
        this.fraGameWindow.add(balBall.getBallGraphics());

        //Set background colour of frame and make it visible
        //Dont allow for window resizing
        this.fraGameWindow.getContentPane().setBackground(Color.BLACK);
        this.fraGameWindow.setVisible(true);
        this.fraGameWindow.setResizable(false);
        this.fraGameWindow.setAlwaysOnTop(true);

        //Set Game Window Image
        Image icon = Toolkit.getDefaultToolkit().getImage("res/DesktopIcon.png");
        this.fraGameWindow.setIconImage(icon);


        //Add special behaviour for closing
        this.fraGameWindow.addWindowListener(new WindowAdapter() {
            //Override close event
            @Override
            public void windowClosing(WindowEvent e) {
                //Set window closed to true
                bolClosed = true;
            }
        });

        //Initialize a KeyListner for the paddle
        //This determines what actions are performed based
        //on specific key events
        this.fraGameWindow.addKeyListener(new KeyAdapter() {
            //Paddle's behaviour when a key is pressed
            @Override
            public void keyPressed(KeyEvent e) {
                padPlayer.keyPress(e);
            }

            //Paddle's behaviour when a key is released
            @Override
            public void keyReleased(KeyEvent e) {
                padPlayer.keyRelease(e);
            }
        });
    }

    /**
     * @description The game's main menu, this will print information about the game
     * and then show the current highscore (if one exists) before prompting the user
     * to begin playing.
     * @author (Theran D)
     * @version (June 14th, 2023)
     */
    public static void mainMenu() {
        //Print welcome message
        System.out.println("Welcome to Breakout!\n\n"
                + "This game tasks you with moving a paddle (gray slab at the bottom of the screen)\n"
                + "left and right by using A and D keys or arrow keys.\n"
                + "There will be a ball bouncing around chaotically,\n"
                + "you lose as soon as it falls below the paddle.\n\n"
                + "Above the paddle will be a grid of bricks.\n"
                + "Break them by hitting them with the ball to gain points.\n"
                + "Each brick you break adds to your score. Some bricks give more points,\n"
                + "the amount each brick gives is for you to discover!\n\n"
                + "If you break every brick, you will go to the next level.\n"
                + "The ball will speed up as you get higher in levels, so be careful.\n\n"
                + "Try to get the best score possible to beat the current highscore, good luck!\n");

        try {
            Scanner sc = new Scanner(new FileReader("Leaderboard.txt"));

            //Display information about current highscore holder
            System.out.println("Current Highscore:\n"
                    //scanner bug
                    + sc.nextInt() + sc.nextLine() + " points, held by " + sc.nextLine() + "\n");
        } catch (FileNotFoundException e) {
            //Exception thrown if Leaderboard.txt has not been created yet
            System.out.println("CURRENT HIGHSCORE NOT FOUND: please play a game for leaderboard to appear.\n");
        } catch (NoSuchElementException e) {
            //Exception thrown by Scanner if some file data has been deleted
            System.out.println("HIGHSCORE DATA DELETED: please play a game for leaderboard to appear.\n");
        } catch (IOException e) {
            System.out.println("ERROR: IO exception encountered.\n");
            e.printStackTrace();
        }

        //Prompt user to enter a key to continue
        System.out.print("Press any KEY then ENTER to continue...");
        new Scanner(System.in).next();

        //Clear the screen
        System.out.print("\u000C");

        //You can only create game instances here,
        //This ensures only one game runs at once.
        new Game().run();
    }

    /**
     * @description Assigns all bricks on screen a semi random point value.
     * Goes through each column and assures it contains 1 brick of each
     * colour but mixes up the order of the colours for each column.
     * @author (Theran D)
     * @version (June 9th, 2023)
     */
    private void generateBoard() {
        //run for every row
        for (int i = 0; i < 13; i++) {
            byte[] bytPointOptions = {1, 2, 3, 4, 5, 6, 7};

            //Sort array from last element to first element
            for (int j = 6; j >= 0; j--) {
                //Randomly assign an index of those remaining
                byte bytLocSwap = (byte) (Math.random() * (j + 1));

                //Swap index chosen and last index (according to j loop)
                byte bytTemp = bytPointOptions[j];
                bytPointOptions[j] = bytPointOptions[bytLocSwap];
                bytPointOptions[bytLocSwap] = bytTemp;

                //Update brick colour
                arrBricks[j][i].setPointValue(bytPointOptions[j]);
            }
        }
    }

    /**
     * @description Regenerates the board, resets the location of the ball and the player
     * and updates current leel display.
     * <p>
     * Also resets bricks broken and increases gamespeed.
     * @author (Theran D)
     * @version (June 9th, 2023)
     */
    private void levelUp() {
        //regenerate the board
        generateBoard();

        //Reset ball and paddle positions
        this.balBall.resetBall();
        this.padPlayer.resetPaddle();

        //Update the current level index
        this.scrScore.updateLevel();

        //Set bricks broken to 0 and increase game speed
        Game.bytBricksBroken = 0;
        Game.fltGameSpeed += 0.25f;
    }

    /**
     * @description Gets the X and Y distance between a ball
     * and a box, if that distance is less than the ball's radius, they
     * are in contact.
     * @perams Ball balBall - The ball in the collision
     * Box boxBox - The box being checked for collision
     * @returns true if they are in contact (seperation is less than radius)
     * false otherwise.
     * @author (Theran D)
     * @version (June 7th, 2023)
     */
    private boolean checkCollision(Ball balBall, Box boxBox) {
        //Declare variables to keep track of seperation distance
        float fltDistX = 0f;
        float fltDistY = 0f;

        //get X seperation
        if (balBall.getX() > boxBox.getRightEdge())
            fltDistX = balBall.getX() - boxBox.getRightEdge();
        else if (balBall.getX() < boxBox.getLeftEdge())
            fltDistX = boxBox.getLeftEdge() - balBall.getX();

        //get Y seperation
        if (balBall.getY() > boxBox.getBottomEdge())
            fltDistY = balBall.getY() - boxBox.getBottomEdge();
        else if (balBall.getY() < boxBox.getTopEdge())
            fltDistY = boxBox.getTopEdge() - balBall.getY();

        //Return true if the two object's total seperation is less than the ball's radius
        if (Math.sqrt((fltDistX * fltDistX) + (fltDistY * fltDistY)) <= balBall.getRadius())
            return true;

        return false; //false otherwise
    }

    /**
     * @description The main gameplay loop, waits a certain amount of time
     * between draw frames.
     * Updates positions, checks collisions and stops upo
     * @author (Theran D)
     * @version (June 13th, 2023)
     */
    private void run() {
        //Declare and initialize a variable to hold a variable of last frame's draw time
        long lngStartTime;
        lngStartTime = System.nanoTime();

        //Reset ball location and generate initial board
        this.balBall.resetBall();
        generateBoard();

        //Run loop while window not closed and ball not below the paddle
        while (!bolClosed && balBall.getY() < SCREEN_HEIGHT + balBall.getRadius()) {
            //Wait in beween frames
            while (System.nanoTime() - lngStartTime < FRAME_DELAY)

                //Level up if all bricks are broken
                if (bytBricksBroken == NUMBER_OF_BRICKS) {
                    levelUp();
                }

            //check wall collisions
            for (Box b : arrWalls) {
                if (checkCollision(balBall, b)) {
                    b.bounce(balBall);
                }
            }

            //check brick collisions
            for (Brick[] a : arrBricks) {
                for (Brick b : a) {
                    if (checkCollision(balBall, b)) {
                        scrScore.updateScore(b.getPointValue()); //update the score
                        b.bounce(balBall);
                    }
                }
            }

            //Check player collisions
            if (checkCollision(balBall, padPlayer)) {
                padPlayer.bounce(balBall);
            }

            //Make score elements invisible if they are collided with
            if (checkCollision(balBall, scrScore.getScoreBox())) {
                scrScore.getScoreGraphics().setVisible(false);
            } else {
                scrScore.getScoreGraphics().setVisible(true);
            }

            if (checkCollision(balBall, scrScore.getLevelBox())) {
                scrScore.getLevelGraphics().setVisible(false);
            } else {
                scrScore.getLevelGraphics().setVisible(true);
            }

            //Update ball and paddle positions
            padPlayer.onUpdate();
            balBall.onUpdate();

            //Reset time since last frame
            lngStartTime = System.nanoTime();

        }

        this.fraGameWindow.dispose();
        //If the window was closed, exit
        if (bolClosed) {
            System.out.println("It seems you have quit the game.\n"
                    + "Your scrScore will not be recorded.\n\n"
                    + "Hope that you play again sometime.");
            return;
        }
        //Manually close the window
        this.fraGameWindow.dispatchEvent(new WindowEvent(fraGameWindow, WindowEvent.WINDOW_CLOSING));

        //Display game over message
        System.out.println("Game over!\n"
                + "Your final score was " + scrScore.getScore() + ".\n");

        //Add the score to the leaderboard
        scrScore.addToLeaderboard();

        //Say goodbye to the user
        System.out.println("Goodbye!");
    }

    //Increases the amount of bricks broken by 1
    public static void brickBroken() {
        bytBricksBroken++;
    }

    //Gets the current game speed
    public static float getGameSpeed() {
        return fltGameSpeed;
    }
}
