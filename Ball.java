//Import javax.swing and java.awt to allow
//for custom imaage

import javax.swing.*;
import java.awt.*;

/**
 * @author (Theran D)
 * @version (June 12th, 2023)
 * @description A chaotic ball which bounces off of boxes.
 * The ball's speed is determined by the game speed
 * while the direction is calculated in the changeDir()
 * methods.
 */
public class Ball {
    //The radius of the game's ball
    private final byte BALL_RADIUS = 15;
    private final float SPAWN_MULTIPLIER = 0.2f;

    //Variables used for the ball's graphics and information about ball position.
    private BallGraphics graphics;
    private PositionStats posStats;

    //Default constructor for a Ball
    public Ball() {
        //Initialize ball grphics and position stats variables
        this.graphics = new BallGraphics();
        this.posStats = new PositionStats();
    }

    /**
     * @description A method which resets the position and
     * direction of the ball if a level has been started
     * or the ball has been put into an unplayable state.
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    public void resetBall() {
        //Set the ball to sart in the middle of the screen just underneath the bricks
        this.posStats.fltLocationX = Game.SCREEN_WIDTH / 2f;
        this.posStats.fltLocationY = 3 * Game.SCREEN_HEIGHT / 4f;

        //Set the direction of the ball to be a random direction, travelling towards the bricks
        //Set the speed of the ball to be half of it's normal speed.
        this.posStats.fltDirectionX = SPAWN_MULTIPLIER * (float) Math.cos(Math.random() * (2 * Math.PI / 3) + Math.PI / 12);
        this.posStats.fltDirectionY = -SPAWN_MULTIPLIER * (float) Math.sin(Math.acos(this.posStats.fltDirectionX));
    }

    /**
     * @decription A method which updates the position of the ball
     * according to the ball's current direction of travel.
     * @author (Theran D)
     * @version (June 12th, 2023)
     */
    public void onUpdate() {
        //Update the ball's location according to current direction of travel
        this.posStats.fltLocationX += Game.getGameSpeed() * this.posStats.fltDirectionX;
        this.posStats.fltLocationY += Game.getGameSpeed() * this.posStats.fltDirectionY;

        //If the x location of the ball is out of bounds, reset the ball's movement
        if (posStats.fltLocationX < BALL_RADIUS - Game.getGameSpeed()) {
            resetBall();
        } else if (posStats.fltLocationX > Game.SCREEN_WIDTH - (BALL_RADIUS - Game.getGameSpeed())) {
            resetBall();
        }

        //If the ball is moving perfectly horizontally, reset it's movement
        if (posStats.fltDirectionY == 0f) {
            resetBall();
        }

        //This method redraws the ball onscreen
        //The ball doesn't redraw itself automatically because it is a custom JLabel
        this.graphics.repaint();
    }

    /**
     * @decription Changes ball direction based on the ball's initial direction
     * and the direction of the force applied on the ball by the box its in
     * contact with.
     * @perams float fltContactX - The distance from the balls horizontal center of contact point
     * float fltContactX - The distance from the balls vertival center of contact point
     * @author (Theran D)
     * @version (June 9th, 2023)
     */
    public void changeDir(float fltContactX, float fltContactY) {
        //Get the dot product between the ball's direction and the direction of contact
        float fltDot = (fltContactX * posStats.fltDirectionX + fltContactY * posStats.fltDirectionY);

        //Devide dot product by the magnitude of the contact vector
        //This is to get the vector close to a magnitude of 1.0f(the ball's base speed)
        fltDot /= (fltContactX * fltContactX + fltContactY * fltContactY);

        //multiply the dot product by -2 since we will be adding it to original direction
        fltDot *= -2;

        //Determine new direction by multiplying the dot product by amount of force
        //in each axis
        this.posStats.fltDirectionX += (fltDot * fltContactX);
        this.posStats.fltDirectionY += (fltDot * fltContactY);
    }

    /**
     * @decription A method which will update the direction of travel of the ball
     * based on distance from the paddle's center.
     * @author (Theran D)
     * @version (June 9th, 2023)
     */
    public void changeDir(float fltDist) {
        //Set the new angle by multiplying distance from center by an acute angle
        //This means bounces off paddle edges (far from center) will travel
        //more horizontally than vertically
        float fltNewX = fltDist * (float) Math.cos(Math.PI / 12);

        //Set the new X direction based on determined angle, then use it to find a
        //related y direction.
        this.posStats.fltDirectionX = fltNewX;
        this.posStats.fltDirectionY = -1 * (float) Math.sin(Math.acos(fltNewX));
    }

    //Get the x location of the ball
    public float getX() {
        return this.posStats.fltLocationX;
    }

    //Get the y location of the ball
    public float getY() {
        return this.posStats.fltLocationY;
    }

    //Get the radius of the ball
    public byte getRadius() {
        return this.BALL_RADIUS;
    }

    //Get information regarding the visuals used to draw the ball
    public BallGraphics getBallGraphics() {
        return this.graphics;
    }

    /**
     * @author (Theran D)
     * @version (June 6th, 2023)
     * @decription An inner class which houses direction and location information about
     * a ball. Put into an inner class to keep code organized.
     */
    private class PositionStats {
        public float fltDirectionX;
        public float fltDirectionY;
        public float fltLocationX;
        public float fltLocationY;
    }

    /**
     * @author (Theran D)
     * @version (June 6th, 2023)
     * @decription An inner class which extends the JLabel in order to draw the ball as
     * a circle rather than a rectangle.
     */
    private class BallGraphics extends JLabel {
        //gets called every time ball gets painted or repainted
        @Override
        protected void paintComponent(Graphics g) {
            //set ball colour to white
            g.setColor(Color.WHITE);

            //draw a circle given the top corner, length and width
            g.fillOval((int) (posStats.fltLocationX - BALL_RADIUS),
                    (int) (posStats.fltLocationY - BALL_RADIUS),
                    BALL_RADIUS * 2, BALL_RADIUS * 2);
        }
    }
}
