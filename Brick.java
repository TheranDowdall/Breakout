//Import java.awt.Color to allow bricks to have different colours
import java.awt.Color;
/**
 * Subclass of Box
 * 
 * @description A box which disappears upon impact,
 * awarding points when the ball collides with it.
 *
 * @author (Theran D)
 * @version (June 8th, 2023)
 */
public class Brick extends Box
{
    //Declare instance variaables for a Brick
    //This includes the amount of points it's
    //worth and if it has been destroyed this level
    private byte bytPointValue;
    private boolean bolRemoved;
    
    /**
     * @description A constructor for a brick.
     * 
     * @perams
     * float x - The x location of the center of the brick 
     * float y - The y location of the center of the brick 
     * int w - The width of the brick
     * int h - The height of the brick
     * 
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    public Brick(float x, float y, int w, int h){
        //Initialize a new box based on created brick
        super(x, y, w, h, Color.BLACK);
    }
    
    /**
     * @description Behaviour for the ball bouncing off of a brick.
     * Brick gets removed after collision.
     * 
     * @perams
     * Ball balBouncer - The ball the brick collided with
     * 
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    @Override
    public void bounce(Ball balBouncer){
        //If brick is already removed, do nothing
        if(this.bolRemoved) return;
        
        //Run default box bounce behaviour
        super.bounce(balBouncer);
        
        //Remove the brick
        remove();
    }
    
    /**
     * @description Removes the brick from this level.
     * 
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    private void remove(){
        //Make the brick appear invisible
        this.labBox.setOpaque(false); 
        
        //Set the brick's status to removed
        this.bolRemoved = true;
        
        //set the point value for colliding with this brick to 0
        this.bytPointValue = 0;
        
        //Increase the amount of bricks broken this level.
        Game.brickBroken();
    }
    
    /**
     * @description Set the colour of the brick for next level.
     * Also makes brick visible and able to be collided with.
     * 
     * @perams
     * byte bytPoint - The amount of points this brick is worth.
     * @author (Theran D)
     * @version (June 8th, 2023)
     */
    public void setPointValue(byte bytPoint){
        //Set the colour of the brick based on its point value
        switch(bytPoint){
            case 1: this.labBox.setBackground(Color.CYAN); break;
            case 2: this.labBox.setBackground(Color.MAGENTA); break;
            case 3: this.labBox.setBackground(Color.BLUE); break;
            case 4: this.labBox.setBackground(Color.GREEN); break;
            case 5: this.labBox.setBackground(Color.YELLOW); break;
            case 6: this.labBox.setBackground(Color.ORANGE); break;
            case 7: this.labBox.setBackground(Color.RED); break;
        }
        
        //Set the point value of the brick
        this.bytPointValue = (byte)(bytPoint * 10);
        
        //Set the brick to be visible and able to collide with
        this.labBox.setOpaque(true);
        this.bolRemoved = false;
    }
    
    //Get the amount of points that this brick is worth
    public byte getPointValue(){
        return this.bytPointValue;
    }
}
