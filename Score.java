//Import java.Swing to update JLabels from Box class
//Import io and Scanner for file writing 
import javax.swing.*;
import java.io.*;
import java.util.Scanner;

/**
 * @description Stores information about the user's current level/score.
 * Responsible for uploading score to a file if it is a highscore and displaying
 * UI on gameplay screen.
 *
 * @author (Theran D)
 * @version (June 14th, 2023)
 */
public class Score
{
    //Variable which dictates font size of UI components
    private final static byte FONT_SIZE = 20;
    
    //Instance variables to store the current score and level
    private int intCurrScore;
    private byte bytCurrLevel;
    
    //Box instance variables for score and level UI ingame
    private Box boxScoreBox;
    private Box boxLevelBox;
    
    /**
     * @description Default constructor for score, sets initial level and score, as well
     * as creates the UI to display score and level statistics onscreen. 
     *
     * @author (Theran D)
     * @version (June 13th, 2023)
     */
    public Score(){
        //Initialize score and level values.
        this.intCurrScore = 0;
        this.bytCurrLevel = 1;
        
        //Create a box for score data
        this.boxScoreBox = new Box(Game.SCREEN_WIDTH/100, 0, Game.SCREEN_WIDTH/6, 
                                   Game.SCREEN_HEIGHT/10, FONT_SIZE);
        
        //Set text and allignment of score UI
        this.boxScoreBox.getGraphics().setText("SCORE " + intCurrScore);
        this.boxScoreBox.getGraphics().setHorizontalAlignment(SwingConstants.LEFT);
        this.boxScoreBox.getGraphics().setVerticalAlignment(SwingConstants.CENTER);
        
        //Create box for level data
        this.boxLevelBox = new Box(7*Game.SCREEN_WIDTH/8 - Game.SCREEN_WIDTH/100,
                              0, Game.SCREEN_WIDTH/8, Game.SCREEN_HEIGHT/10, FONT_SIZE);
        
        //set text and allignment of level UI
        this.boxLevelBox.getGraphics().setText("LEVEL " + bytCurrLevel);
        this.boxLevelBox.getGraphics().setHorizontalAlignment(SwingConstants.RIGHT);
        this.boxLevelBox.getGraphics().setVerticalAlignment(SwingConstants.CENTER);
    }
    
    /**
     * @description A method which adds the user score to a file if it is higher
     * than the previous highscore. Otherwise, it informs the user that thier score 
     * was too low.
     *
     * @author (Theran D)
     * @version (June 14th, 2023)
     */
    public void addToLeaderboard(){
        //declare all variables that could be used in this method
        File filLeaderboard;
        String strName;
        String strPrevScore;
        BufferedReader br;
        FileWriter filWriter;
        
        try{
            //Initialize file to follow Leaderboard.txt
            filLeaderboard = new File("Leaderboard.txt");
        
            //create the file if it doesn't exist
            if(filLeaderboard.createNewFile()){
                System.out.println("Leaderboard.txt has been created to hold top score.");
            }
            
            //read file data from a buffered reader
            br = new BufferedReader(new FileReader(filLeaderboard));
            
            //Get the score as a string, cast error if value is null
            strPrevScore = br.readLine();
            
            //Check to see if current highscore is valid and higher than current score
            //The reason this doesn't throw an EOFException is because bufferedreader
            //assumes a blank line is null
            if(strPrevScore != null && intCurrScore <= Integer.parseInt(strPrevScore) && br.readLine() != null){
                //print lose message
                System.out.println("Your score didn't beat the highscore, better luck next time.");
                return;
            }
            
            //Otherwise, congratulate the user on new highscore
            System.out.println("Congratulations! you beat the previous highscore.\n"
                              +"Please enter your name for the leaderboard: ");
               
            //Get user's name for file
            strName = new Scanner(System.in).nextLine();
            
            //Ensure name cannot be null
            if(strName.equals("")){
                strName = "Player";
            }
            
            //Create a new fileWriter
            filWriter = new FileWriter(filLeaderboard);
              
            //Write new score data to the file
            filWriter.write(intCurrScore + "\n" + strName);
            
            //close the file
            filWriter.close();
            
            //congratelate user again
            System.out.println("\nHighscore in Leaderboard.txt has been updated.\n"
                              +"Hope you play again " + strName);
        }
        catch(IOException e){
            //Any error here is unexpected, report error
            System.out.println("ERROR: Problem encountered while updating Leaderboard.txt");
            e.printStackTrace();
        }
    }
    
    //Add the point value of destroyed brick to total the update level UI text
    public void updateScore(byte bytPointsAdded){
        this.intCurrScore += bytPointsAdded;
        this.boxScoreBox.getGraphics().setText("SCORE " + intCurrScore);
    }
    
    //Increase level number and redisplay level UI text
    public void updateLevel(){
        this.boxLevelBox.getGraphics().setText("LEVEL " + (++bytCurrLevel));
    }
    
    //Get the graphics for the score box
    public JLabel getScoreGraphics(){
        return this.boxScoreBox.getGraphics();
    }
    
    //Get the graphics for the level box
    public JLabel getLevelGraphics(){
        return this.boxLevelBox.getGraphics();
    }
    
    //Get the score box for collisions
    public Box getScoreBox(){
        return this.boxScoreBox;
    }
    
    //Get the level box for collisions
    public Box getLevelBox(){
        return this.boxLevelBox;
    }
    
    //Get the current score integer value
    public int getScore(){
        return this.intCurrScore;
    }
}