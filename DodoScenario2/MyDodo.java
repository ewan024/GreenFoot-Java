import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.1 -- 08-07-2017
 */
public class MyDodo extends Dodo
{

    public MyDodo() {
        this ( EAST );
    }

    public MyDodo( int init_direction ) {
        super ( init_direction );
    }

    public void act() {
    }

    /**
     * Move one cell forward in the current direction.
     * 
     * <P> Initial: Dodo is somewhere in the world
     * <P> Final: If possible, Dodo has moved forward one cell
     *
     */
    public void move() {
        if ( canMove() ) {
            step();
        } else {
            showError( "I'm stuck!" );
        }
    }

    /**
     * Test if Dodo can move forward, 
     * i.e. there are no obstructions or end of world in the cell in front of her.
     * 
     * <p> Initial:   Dodo is somewhere in the world
     * <p> Final:     Same as initial situation
     * 
     * @return  boolean true if Dodo can move (thus, no obstructions ahead)
     *                  false if Dodo can't move
     *                      i.e. there is an obstruction or end of world ahead
     */
    public boolean canMove() {
        if ( borderAhead() ){
            return false;
        } else {
            return true;
        }
    }

    /**
     * Move given number of cells forward in the current direction.
     * 
     * <p> Initial:   
     * <p> Final:  
     * 
     * @param   int distance: the number of steps made
     */
    public void jump( int distance ) {
        int nrStepsTaken = 0;               // set counter to 0
        while ( nrStepsTaken < distance ) { // check if more steps must be taken  
            move();                         // take a step
            nrStepsTaken++;                 // increment the counter
        }
    }

    /**
     * Dodo moves forward and sits on the egg.
     * 
     * <p> Initial:   Somewhere in a cell ahead of Dodo lies a egg. 
     *                The cells between Dodo and the egg are empty.
     * <p> Final:     Dodo has moved forward and is sitting on the egg.
     *                      Dodo is facing original direction.
     * 
     */
    public void gotoEgg() {
        move();

    }
    
     /**
     * Test there is a grain in the cell in front of Dodo 
     * 
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     * 
     * @return  boolean true if there is a grain in the cell in front of Dodo
     *                  false else otherwise
     */
    public boolean grainAhead() {
        // D
        if ( onGrain() ){
            // Dodo goes back to initial situation before returning value
            // E
            return true;
        } else {
            // Dodo goes back to initial situation before returning value
            // F
            return false;
        }
    } 
    
    /**
     * Test if Dodo can lay an egg.
     *          (there is not already an egg in the cell)
     * 
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     * 
     * @return boolean true if Dodo can lay an egg (no egg there)
     *                 false if Dodo can't lay an egg
     *                      (already an egg in the cell)
     */
    public boolean canLayEgg( ){
        if( onEgg() ) {
            return false;
        }else{
            return true;
        }
    }  
    
}
