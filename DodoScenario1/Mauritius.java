import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.List;

import java.io.IOException;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Mauritius.
 * 
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.1 -- 03-07-2017
 */
public class Mauritius extends World
{
    private static final String WORLD_NAME = "worldEmpty.txt";
    private static File WORLD_FILE = null;

    private static final int MAXWIDTH = 10, MAXHEIGHT = 10, CELLSIZE = 60;

    private Scoreboard theScoreboard = new Scoreboard ( "Moves left:", MAXSTEPS, "Score:", 0);

    public static final int MAXSTEPS = 40;

    private static boolean traceOn = true;

    private static final char
    FENCE      = '#'            ,
    EGG_YELLOW = '$'            ,
    EGG_BLUE   = '.'            ,
    NEST       = '='            ,
    GRAIN      = '+'            ,
    DODO_N     = 'N'            ,
    DODO_S     = 'S'            ,
    DODO_E     = 'E'            ,
    DODO_W     = 'W'            ;

    private static WorldReader WORLD_READER = null;
    private static int WORLD_WIDTH, WORLD_HEIGHT;

    static {
        if ( ! WORLD_NAME.isEmpty() ) {
            WORLD_FILE   = new File ( WorldWriter.WORLD_PATH + WORLD_NAME );           
            initWorldInfo();
        } else {
            WORLD_WIDTH  = MAXWIDTH;
            WORLD_HEIGHT = MAXHEIGHT;
        }            
    }

    private static void initWorldInfo() {
        WORLD_READER = new WorldReader ( WORLD_FILE );
        WORLD_WIDTH  = WORLD_READER.getWorldWidth();
        WORLD_HEIGHT = WORLD_READER.getWorldHeight();
    }

    /**
     * Constructor for objects of class ChickenWorld.
     * 
     */
    public Mauritius() {    
        super(WORLD_WIDTH, WORLD_HEIGHT, CELLSIZE); 
        setPaintOrder (Message.class, Scoreboard.class, Dodo.class, Grain.class,
            Nest.class, Egg.class, Fence.class);        
        populate();
        prepare();
    }

    public static void traceOn() {
        traceOn = true;
    }

    public static void traceOff() {
        traceOn = false;
    }

    public static boolean traceIsOn() {
        return traceOn;
    }

    public void updateScore( int ... scores ){
        theScoreboard.updateScore( scores );
    }

    private Actor charToActor( char c ) {
        MyDodo newDodo;
        switch ( c ) {
            case FENCE:
                return new Fence();
            case NEST:
                return new Nest();
            case GRAIN:
                return new Grain();                
            case EGG_YELLOW:
                return new GoldenEgg();
            case EGG_BLUE:
                return new BlueEgg();
            case DODO_N: 
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.NORTH );
                return newDodo;
            case DODO_S:
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.SOUTH );
                return newDodo;
            case DODO_E:
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.EAST );
                return newDodo;
            case DODO_W:
                newDodo = new MyDodo();
                newDodo.setDirection( Dodo.WEST );
                return newDodo;
            default:
                return null;
        }
    }

    private void populate () {
        if ( WORLD_FILE != null ) {
            if ( WORLD_READER == null ) {
                WORLD_READER = new WorldReader ( WORLD_FILE );
            }
            try {
                while (WORLD_READER.hasNext()) {
                    WorldReader.Cell next_cell = WORLD_READER.next();
                    Actor actor = charToActor( next_cell.getChar() );
                    if ( actor != null ) {
                        addObject(actor, next_cell.getX(), next_cell.getY());
                    }
                }
                WORLD_READER.close();
                WORLD_READER = null;
            } catch ( IOException ioe ) {
            }
        }            
    }

    private void removeAllActors() {
        removeObjects( getObjects( null ) );
    }

    private char getActorAt( int x, int y ){
        List<Actor> actors = getObjectsAt(x, y, null);
        if ( actors.size() > 0 ) {
            Actor actor = actors.get( 0 );
            if ( actor instanceof MyDodo ) {
                MyDodo dodo = (MyDodo) actor;
                switch ( dodo.getDirection() ) {
                    case Dodo.NORTH: return DODO_N;
                    case Dodo.SOUTH: return DODO_S;
                    case Dodo.EAST:  return DODO_E;
                    default:    return DODO_W;
                }
            } else if ( actor instanceof Fence ) {
                return FENCE;
            } else if ( actor instanceof GoldenEgg ) {
                return EGG_YELLOW;
            } else if ( actor instanceof BlueEgg ) {
                return EGG_BLUE;
            } else if ( actor instanceof Nest ) {
                return NEST;
            } else if ( actor instanceof Grain ) {
                return GRAIN;
            } else {
                return ' ';
            }
        } else {
            return ' ';
        }
    }

    public void saveToFile() {
        WorldWriter writer = new WorldWriter ( "saved.txt" );
        try {
            writer.write( String.format("%d %d\n", WORLD_WIDTH, WORLD_HEIGHT) );
            for ( int y = 0; y < WORLD_HEIGHT; y++ ) {
                for ( int x = 0; x < WORLD_WIDTH; x++ ) {
                    writer.write( getActorAt( x, y ) );
                }
                writer.write( '\n' );
            }
            writer.close();
        } catch ( IOException ioe ) {
        }
    }

    public void populateFromFile() {
        File world_files = new File ( WorldWriter.WORLD_PATH );
        JFileChooser chooser = new JFileChooser( world_files );
        FileNameExtensionFilter filter = new FileNameExtensionFilter( "Plain text files", "txt" );
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog( null );
        if ( returnVal == JFileChooser.APPROVE_OPTION ) {
            WORLD_FILE = chooser.getSelectedFile();
            initWorldInfo();
            Greenfoot.setWorld( new Mauritius () );
        }
    }

    public static boolean checkCellContent( Actor actor, int x, int y, Class... forbiddenClasses) {
        World world = actor.getWorld();
        List<Actor> allActorsInCell = world.getObjectsAt( x, y, Actor.class );
        allActorsInCell.remove( actor );        
        for ( Actor otherActor: allActorsInCell ) {
            for ( Class forbidden: forbiddenClasses ){
                if ( forbidden.isInstance( otherActor ) ) {
                    showError( world, " cell already occupied " );
                    return false;
                }
            }
        }
        return true;
    }

    private static void showError( World world, String err_msg ) {
        Message.showMessage(  new Alert (err_msg), world );
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        MyDodo myDodo = new MyDodo();
        addObject(myDodo,5,4);
        myDodo.turn180();
        myDodo.turn180();
        myDodo.canLayEgg();
        BlueEgg blueEgg = new BlueEgg();
        addObject(blueEgg,6,4);
        myDodo.setLocation(6,4);
        myDodo.canLayEgg();
        myDodo.setLocation(5,4);
        BlueEgg blueEgg2 = new BlueEgg();
        addObject(blueEgg2,5,4);
        blueEgg2.setLocation(4,4);
        removeObject(blueEgg2);
        removeObject(blueEgg);
        myDodo.setLocation(10,4);
        Fence fence = new Fence();
        addObject(fence,11,4);
        myDodo.setLocation(10,4);
        myDodo.climbOverFence();
        myDodo.setLocation(2,4);
        removeObject(fence);
        myDodo.setLocation(4,5);
        BlueEgg blueEgg3 = new BlueEgg();
        addObject(blueEgg3,6,1);
        BlueEgg blueEgg4 = new BlueEgg();
        addObject(blueEgg4,10,2);
        BlueEgg blueEgg5 = new BlueEgg();
        addObject(blueEgg5,8,8);
        myDodo.setLocation(4,5);
        BlueEgg blueEgg6 = new BlueEgg();
        addObject(blueEgg6,3,3);
        BlueEgg blueEgg7 = new BlueEgg();
        addObject(blueEgg7,8,5);
        BlueEgg blueEgg8 = new BlueEgg();
        addObject(blueEgg8,0,3);
        BlueEgg blueEgg9 = new BlueEgg();
        addObject(blueEgg9,3,2);
        BlueEgg blueEgg10 = new BlueEgg();
        addObject(blueEgg10,5,7);
        BlueEgg blueEgg11 = new BlueEgg();
        addObject(blueEgg11,5,0);
        BlueEgg blueEgg12 = new BlueEgg();
        addObject(blueEgg12,1,2);
        BlueEgg blueEgg13 = new BlueEgg();
        addObject(blueEgg13,6,9);
        BlueEgg blueEgg14 = new BlueEgg();
        addObject(blueEgg14,4,4);
        BlueEgg blueEgg15 = new BlueEgg();
        addObject(blueEgg15,0,5);
        BlueEgg blueEgg16 = new BlueEgg();
        addObject(blueEgg16,8,10);
        BlueEgg blueEgg17 = new BlueEgg();
        addObject(blueEgg17,9,8);
        BlueEgg blueEgg18 = new BlueEgg();
        addObject(blueEgg18,3,9);
        BlueEgg blueEgg19 = new BlueEgg();
        addObject(blueEgg19,1,7);
        BlueEgg blueEgg20 = new BlueEgg();
        addObject(blueEgg20,8,6);
        BlueEgg blueEgg21 = new BlueEgg();
        addObject(blueEgg21,10,10);
        BlueEgg blueEgg22 = new BlueEgg();
        addObject(blueEgg22,10,9);
        BlueEgg blueEgg23 = new BlueEgg();
        addObject(blueEgg23,11,11);
        blueEgg23.setLocation(10,10);
        BlueEgg blueEgg24 = new BlueEgg();
        addObject(blueEgg24,10,10);
        BlueEgg blueEgg25 = new BlueEgg();
        addObject(blueEgg25,11,9);
        BlueEgg blueEgg26 = new BlueEgg();
        addObject(blueEgg26,11,10);
        BlueEgg blueEgg27 = new BlueEgg();
        addObject(blueEgg27,9,9);
        BlueEgg blueEgg28 = new BlueEgg();
        addObject(blueEgg28,9,10);
        BlueEgg blueEgg29 = new BlueEgg();
        addObject(blueEgg29,9,11);
        BlueEgg blueEgg30 = new BlueEgg();
        addObject(blueEgg30,10,11);
        myDodo.setLocation(0,0);
        myDodo.goToEgg();
        myDodo.setLocation(0,0);
        BlueEgg blueEgg31 = new BlueEgg();
        addObject(blueEgg31,8,4);
        BlueEgg blueEgg32 = new BlueEgg();
        addObject(blueEgg32,5,1);
        BlueEgg blueEgg33 = new BlueEgg();
        addObject(blueEgg33,4,5);
        BlueEgg blueEgg34 = new BlueEgg();
        addObject(blueEgg34,5,6);
        BlueEgg blueEgg35 = new BlueEgg();
        addObject(blueEgg35,8,6);
        BlueEgg blueEgg36 = new BlueEgg();
        addObject(blueEgg36,10,3);
        BlueEgg blueEgg37 = new BlueEgg();
        addObject(blueEgg37,10,2);
        BlueEgg blueEgg38 = new BlueEgg();
        addObject(blueEgg38,8,2);
        BlueEgg blueEgg39 = new BlueEgg();
        addObject(blueEgg39,5,4);
        BlueEgg blueEgg40 = new BlueEgg();
        addObject(blueEgg40,3,5);
        BlueEgg blueEgg41 = new BlueEgg();
        addObject(blueEgg41,4,8);
        BlueEgg blueEgg42 = new BlueEgg();
        addObject(blueEgg42,2,10);
        BlueEgg blueEgg43 = new BlueEgg();
        addObject(blueEgg43,8,10);
        BlueEgg blueEgg44 = new BlueEgg();
        addObject(blueEgg44,10,8);
        BlueEgg blueEgg45 = new BlueEgg();
        addObject(blueEgg45,10,5);
        BlueEgg blueEgg46 = new BlueEgg();
        addObject(blueEgg46,10,7);
        BlueEgg blueEgg47 = new BlueEgg();
        addObject(blueEgg47,9,8);
        BlueEgg blueEgg48 = new BlueEgg();
        addObject(blueEgg48,7,9);
        BlueEgg blueEgg49 = new BlueEgg();
        addObject(blueEgg49,7,10);
        BlueEgg blueEgg50 = new BlueEgg();
        addObject(blueEgg50,5,8);
        BlueEgg blueEgg51 = new BlueEgg();
        addObject(blueEgg51,4,6);
        BlueEgg blueEgg52 = new BlueEgg();
        addObject(blueEgg52,3,6);
        BlueEgg blueEgg53 = new BlueEgg();
        addObject(blueEgg53,3,3);
        BlueEgg blueEgg54 = new BlueEgg();
        addObject(blueEgg54,5,2);
        BlueEgg blueEgg55 = new BlueEgg();
        addObject(blueEgg55,6,3);
        BlueEgg blueEgg56 = new BlueEgg();
        addObject(blueEgg56,6,6);
        BlueEgg blueEgg57 = new BlueEgg();
        addObject(blueEgg57,6,6);
        blueEgg51.setLocation(3,6);
        BlueEgg blueEgg58 = new BlueEgg();
        addObject(blueEgg58,3,6);
        BlueEgg blueEgg59 = new BlueEgg();
        addObject(blueEgg59,1,7);
        BlueEgg blueEgg60 = new BlueEgg();
        addObject(blueEgg60,1,8);
        BlueEgg blueEgg61 = new BlueEgg();
        addObject(blueEgg61,0,4);
        BlueEgg blueEgg62 = new BlueEgg();
        addObject(blueEgg62,1,4);
        GoldenEgg goldenEgg = new GoldenEgg();
        addObject(goldenEgg,10,6);
    }
}
