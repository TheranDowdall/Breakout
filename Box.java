//Import javax.swing and java.awt to support drawing 
//and graphics support
import javax.swing.*;
import java.awt.*;

/**
 * Superclass for Paddle and Brick.
 *
 * @description A rectangle drawn onscreen capable of
 * colliding with the ball. These colisions cause
 * the ball to bounce in specific ways
 * (see Ball.changeDir()).
 * 
 * @author (Theran D)
 * @version (June 8th, 2023)
 */
public class Box
{
    //Set protected variables including the box's graphics
    //the x and y locations of the box
    //and its width
    protected JLabel labBox;
    protected float fltLocationX;
    protected float fltLocationY;
    protected short shrWidth;
    
    //Set box height to be private, this is due to height
    //not being required to be referenced outside of this class
    private short shrHeight;
    
    /**
     * @description A constructor which builds a box designed 
     * to store text information (used by Score class).
     * 
     * @perams
     * int x - The x location of the top left corner of the box 
     * int y - The y location of the top left corner of the box 
     * int w - The width of the box
     * int h - The height of the box
     * int t - The size of the text inside the box
     * 
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    public Box(int x, int y, int w, int h, int t){
        //Create new graphicks for the box
        this.labBox = new JLabel();
        
        //Set the size and location of the box
        this.labBox.setLocation(x, y);
        this.labBox.setSize(w, h);
        
        //Initialize the box's text attributes
        this.labBox.setFont(new Font("Arial", Font.BOLD, t));
        this.labBox.setForeground(Color.WHITE);
        
        //Set the location and dimensions of the box
        this.fltLocationX = x+(w/2f);
        this.fltLocationY = y+(h/2);
        this.shrWidth = (short)(w/2);
        this.shrHeight = (short)(h/2);
    }
    
    /**
     * @description A constructor which builds a box designed to
     * have the ball bounce off of it (used by subclasses).
     * 
     * @perams
     * float x - The x location of the center of the box 
     * float y - The y location of the center of the box 
     * int w - The width of the box
     * int h - The height of the box
     * Color c - The colour the box will be filled in with
     * 
     * @author (Theran D)
     * @version (June 6th, 2023)
     */
    public Box(float x, float y, int w, int h, Color c){
        //Create a new box onscreen and set it's dimensions
        this.labBox = new JLabel();
        this.labBox.setLocation((int)(x-w/2f), (int)(y-h/2f));
        this.labBox.setSize(w, h);
        this.labBox.setBackground(c);
        this.labBox.setOpaque(true);
        
        //Set instance variables to hold box dimension information
        this.fltLocationX = x;
        this.fltLocationY = y;
        this.shrWidth = (short)(w/2);
        this.shrHeight = (short)(h/2);
    }
 
    /**
     * @description A method which gets the distance from the
     * ball's center, in x and y directions of the collision with
     * the box. Negative X means the collision happened on the
     * ball's left side and negative Y means the ball was above 
     * the box during the collision.
     * 
     * @perams
     * Ball balBouncer - The ball that the box is colliding with
     * 
     * @author (Theran D)
     * @version (June 7th, 2023)
     */
    public void bounce(Ball balBouncer){
        //Initialize placeholder variables for the 
        //X and Y distance from collision coordinate
        float fltDistX = 0f;
        float fltDistY = 0f;
        
        //Determine the X displacement of the ball
        //relative to the box
        if(balBouncer.getX() > getRightEdge()) 
            fltDistX = balBouncer.getX() - getRightEdge();
        else if(balBouncer.getX() < getLeftEdge()) 
            fltDistX = balBouncer.getX() - getLeftEdge();

        //Determine the Y displacement of the ball
        //relative to the box
        if(balBouncer.getY() > getBottomEdge()) 
            fltDistY = balBouncer.getY() - getBottomEdge();
        else if(balBouncer.getY() < getTopEdge()) 
            fltDistY = balBouncer.getY() - getTopEdge();
        
        //Determine the ball's new direction of travel
        balBouncer.changeDir(fltDistX, fltDistY);
    }
    
    //Get the JLabel used to draw a box onscreen
    public JLabel getGraphics(){
        return this.labBox;
    }
    
    //Returns the x value of the left edge of a box
    public float getLeftEdge(){
        return this.fltLocationX-(this.shrWidth);
    }
    
    //Returns the x value of the right edge of a box
    public float getRightEdge(){
        return this.fltLocationX+(this.shrWidth);
    }
    
    //Returns the y value of the top edge of a box
    public float getTopEdge(){
        return this.fltLocationY-(this.shrHeight);
    }
    
    //Returns the y value of the bottom edge of a box
    public float getBottomEdge(){
        return this.fltLocationY+(this.shrHeight);
    }
}