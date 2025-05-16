import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.List;
import java.util.ArrayList;
/**
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 01-01-2017
 */
public class SurpriseEgg extends Egg
{
    private static int MAX_VALUE = 1000;

    public SurpriseEgg () {
        super( Greenfoot.getRandomNumber( MAX_VALUE ) + 1 );
    }    

    public static List<SurpriseEgg> generateListOfSurpriseEggs( int size, World world ) {
        int emptyCells = world.getHeight() * world.getWidth() - world.numberOfObjects();
        if ( size <= emptyCells ) {
            List<SurpriseEgg> generatedEggs = new ArrayList<SurpriseEgg> ( size );
            int eggNr = 0;
            while ( eggNr < size ) {
                SurpriseEgg newEgg = new SurpriseEgg ();
                placeEgg( newEgg,  Greenfoot.getRandomNumber( emptyCells - eggNr ), world );
                generatedEggs.add( newEgg );
                eggNr++;
            }
            return generatedEggs;
        } else {
            showError( "Too many surprise eggs ordered", world );
            return new ArrayList<SurpriseEgg>();
        }
    }
    
    private static void placeEgg( Egg egg, int pos, World world ) {
        for( int y = 0; y < world.getHeight(); y++){
            for (int x = 0; x < world.getWidth(); x++){
                if ( world.getObjectsAt(x, y, null).size() == 0 ) {
                    if ( pos == 0 ) {
                        world.addObject( egg, x, y);
                    } else {
                        pos--;
                    }
                }
            }
        }
    }
    private static void showError ( String err_msg, World world ) {
        Message.showMessage(  new Alert( err_msg ), world );
    }

}
