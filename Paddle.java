//Import Color and event to draw the paddle in colour and handle events.

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Subclass of Box.
 *
 * @author (Theran D)
 * @version (June 9th, 2023)
 * @description A box with the ability to move horizontally.
 * Functions as the player in the Breakout game.
 * The paddle can only deflect the ball upwards.
 */
public class Paddle extends Box {
    //Priate instance variables to dictate which keys are currently held
    private boolean bolLeftHeld;
    private boolean bolRightHeld;
    private boolean bolAHeld;
    private boolean bolDHeld;

    //Default constructor for a paddle
    public Paddle() {
        //Call superclass constructor
        //This will allow for the creation of a JLabel which can be rendered
        //onscreen as well as initializes dimensions of the paddle
        super(Game.SCREEN_WIDTH / 2, 11 * Game.SCREEN_HEIGHT / 12,
                Game.SCREEN_WIDTH / 4, Game.SCREEN_HEIGHT / 36, Color.gray);
    }

    //Reset the location of the paddle to be at the horizontal center of the screen
    public void resetPaddle() {
        //Set the horizontal position to the middle of the screen
        this.fltLocationX = Game.SCREEN_WIDTH / 2f;

        //Update the location of the paddle in the game's display
        this.labBox.setLocation((int) fltLocationX, (int) (fltLocationY));
    }

    /**
     * @description A method which updates the positon of the paddle
     * according to what direction is currently being held.
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    public void onUpdate() {
        //Temporary variables used to determine if there are any keys
        //held which would cause the paddle to move left or right.
        boolean bolL = bolLeftHeld || bolAHeld;
        boolean bolR = bolRightHeld || bolDHeld;

        //Update paddle position based on held direction
        //If left and right are both held, paddle will not move
        if (bolR)
            this.fltLocationX += 2 * Game.getGameSpeed();
        else if (bolL)
            this.fltLocationX -= 2 * Game.getGameSpeed();

        //Clamp the paddle's position so that it cannot move offscreen
        this.fltLocationX = Math.max(this.shrWidth, this.fltLocationX);
        this.fltLocationX = Math.min(Game.SCREEN_WIDTH - this.shrWidth, fltLocationX);

        //Update the position of the paddle onscreen
        this.labBox.setLocation((int) getLeftEdge(), (int) getTopEdge());
    }

    /**
     * @description A method which determines the horizontal distance between
     * the center of the paddle and where it collided with the ball.
     * This is used to determine the new direction of the ball after collision.
     * <p>
     * Positive distance refers to ball being to the right of paddle center,
     * negative distance means the ball hit left of the paddle's center.
     * @perams Ball balBouncer - The ball which collided with the paddle.
     * @author (Theran D)
     * @version (June 9th, 2023)
     */
    @Override
    public void bounce(Ball balBouncer) {
        //Determine where the ball hit the paddle horizontally
        //0 - Paddle center, -1 - left edge, 1 - right edge
        float fltDistX = (balBouncer.getX() - this.fltLocationX) / (this.shrWidth);

        //Ensure the ratio of distance is between -1 and 1
        fltDistX = Math.max(fltDistX, -1.0f);
        fltDistX = Math.min(fltDistX, 1.0f);

        //Take the square root of the distance
        //In the direction changing algorithm this will cause the ball to favour
        //more horizontal trajectories
        if (fltDistX > 0f)
            fltDistX = (float) Math.sqrt(fltDistX);
        else if (fltDistX < 0f)
            fltDistX = -1 * (float) Math.sqrt(Math.abs(fltDistX));

        //Send calculated value to Ball class for new direction assessment
        balBouncer.changeDir(fltDistX);
    }

    public void keyPress(KeyEvent e) {
        //Determine what key was pressed
        switch (e.getKeyCode()) {
            //Left key is being held
            case KeyEvent.VK_LEFT:
                bolLeftHeld = true;
                break;

            //Right key is being held
            case KeyEvent.VK_RIGHT:
                bolRightHeld = true;
                break;

            //A key is being held
            case KeyEvent.VK_A:
                bolAHeld = true;
                break;

            //D key is being held
            case KeyEvent.VK_D:
                bolDHeld = true;
                break;
        }
    }

    public void keyRelease(KeyEvent e) {
        //Detemine what key has been released
        switch (e.getKeyCode()) {
            //Left key is no longer held
            case KeyEvent.VK_LEFT:
                bolLeftHeld = false;
                break;

            //Right key is no longer held
            case KeyEvent.VK_RIGHT:
                bolRightHeld = false;
                break;

            //A key is no longer held
            case KeyEvent.VK_A:
                bolAHeld = false;
                break;

            //D key is no longer held
            case KeyEvent.VK_D:
                bolDHeld = false;
                break;
        }
    }
}
