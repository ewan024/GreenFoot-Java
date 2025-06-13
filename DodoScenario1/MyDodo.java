import greenfoot.*;

import java.util.List;

/**
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 20-01-2017
 */
public class MyDodo extends Dodo {
    private int myNrOfEggsHatched;
    private int score = 0;
    private int steps = 0;

    public MyDodo() {
        super(EAST);
        myNrOfEggsHatched = 0;
    }

    public void act() {
    }

    /**
     * Move one cell forward in the current direction.
     *
     * <P> Initial: Dodo is somewhere in the world
     * <P> Final: If possible, Dodo has moved forward one cell
     */
    public void move() {
        if (canMove()) {
            step();
            steps++;
            lowerScore();
        } else {
            showError("I'm stuck!");
        }
    }

    /**
     * Test if Dodo can move forward, (there are no obstructions
     * or end of world in the cell in front of her).
     *
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     *
     * @return boolean true if Dodo can move (no obstructions ahead)
     * false if Dodo can't move
     * (an obstruction or end of world ahead)
     */
    public boolean canMove() {
        return !(borderAhead() || fenceAhead()) && steps < Mauritius.MAXSTEPS;
    }

    /**
     * Hatches the egg in the current cell by removing
     * the egg from the cell.
     * Gives an error message if there is no egg
     *
     * <p> Initial: Dodo is somewhere in the world. There is an egg in Dodo's cell.
     * <p> Final: Dodo is in the same cell. The egg has been removed (hatched).
     */
    public void hatchEgg() {
        if (onEgg()) {
            pickUpEgg();
            myNrOfEggsHatched++;
        } else {
            showError("There was no egg in this cell");
        }
    }

    /**
     * Returns the number of eggs Dodo has hatched so far.
     *
     * @return int number of eggs hatched by Dodo
     */
    public int getNrOfEggsHatched() {
        return myNrOfEggsHatched;
    }

    /**
     * Move given number of cells forward in the current direction.
     *
     * <p> Initial:
     * <p> Final:
     * <p>
     * param   int distance: the number of steps made
     */
    public void jump(int distance) {
        boolean negative = false;

        if (distance < 0) {
            negative = true;
            distance *= -1;
            turn180();
        }

        for (int i = 0; i < distance; i++) {
            while (fenceAhead()) {
                climbOverFence();
            }
            move();
        }

        if (negative) {
            turn180();
        }
    }


    /**
     * Walks to edge of the world printing the coordinates at each step
     *
     * <p> Initial: Dodo is on West side of world facing East.
     * <p> Final:   Dodo is on East side of world facing East.
     * Coordinates of each cell printed in the console.
     */

    public void walkToWorldEdgePrintingCoordinates() {
        while (!borderAhead()) {
            System.out.println(getX() + ", " + getY());
            move();
        }
    }

    /**
     * Test if Dodo can lay an egg.
     * (there is not already an egg in the cell)
     *
     * <p> Initial: Dodo is somewhere in the world
     * <p> Final:   Same as initial situation
     *
     * @return boolean true if Dodo can lay an egg (no egg there)
     * false if Dodo can't lay an egg
     * (already an egg in the cell)
     */

    public boolean canLayEgg() {
        if (onEgg()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Makes the Dodo turn 180 degrees to the right.
     *
     * <p> Initial: Dodo is facing any direction
     * <p> Final:   Dodo is now facing the opposite direction of initial situation
     */

    public void turn180() {
        turnRight();
        turnRight();
    }

    /**
     * Makes the Dodo climb over a fence.
     * (if there is one)
     *
     * <p> Initial: Dodo is standing in front of a fence
     * <p> Final:   Dodo is on the other side of the fence
     */

    public void climbOverFence() {
        if (fenceAhead() && !borderAhead()) {
            turnLeft();
            move();
            turnRight();
            move();
            move();
            turnRight();
            move();
            turnLeft();
        } else {
            showError("Fence not here");
        }
    }

    /**
     * Test if the Dodo has a grain ahead.
     *
     * <p> Initial: Dodo is somewhere on the world
     * <p> Final:   Same as initial situation
     *
     * @return boolean true if there is a grain in front
     * false if there is no grain in front
     */

    public boolean grainAhead() {
        move();
        if (onGrain()) {
            turn180();
            move();
            turn180();
            return true;
        } else {
            turn180();
            move();
            turn180();
            return false;
        }
    }

    /**
     * Makes the Dodo move towards an egg. (and picks it up)
     *
     * <p> Initial: Dodo is somewhere on the world
     * <p> Final:   Dodo has moved to the given egg (and picks that egg up, updating the scoreboard)
     *
     * @param Egg egg = an egg on the world
     */

    public void goToEgg(Egg egg) {
        jump(egg.getX() - getX());
        turnRight();
        jump(egg.getY() - getY());
        turnLeft();
        if (onEgg()) {
            score += egg.getValue();
            pickUpEgg();
        }
    }

    /**
     * Prints out the coordinates of all the fences on the world.
     *
     * <p> Initial: Dodo and fences are somewhere on the world
     * <p> Final:   Same as initial situation
     */

    public void printFenceLocation() {
        for (Fence fence : fences()) {
            System.out.println(fence.getX() + ", " + fence.getY());
        }
    }

    /**
     * Finds the nearest egg to the Dodo.
     *
     * <p> Initial: Dodo and eggs are somewhere on the world
     * <p> Final:   Same as initial situation
     *
     * @return Egg = the closest egg to the Dodo
     */

    public Egg nearestEgg() {
        Egg closestEgg = null;
        int closest = Integer.MAX_VALUE;

        for (Egg egg : eggOnWorld()) {
            int eggX = Math.abs(egg.getX() - getX());
            int eggY = Math.abs(egg.getY() - getY());
            int distance = eggX + eggY;

            if (distance <= closest) {
                closest = distance;
                closestEgg = egg;
//              System.out.println(getEggValue());
            }

        }

        return closestEgg;
    }

    /**
     * The steps the Dodo has taken and the score of Eggs will get updated. (if possible)
     *
     * <p> Initial: Dodo (and eggs) is somewhere on the world
     * <p> Final:   Same as initial situation
     */

    public void lowerScore() {
        Mauritius world = getWorldOfType(Mauritius.class);

        world.updateScore(Mauritius.MAXSTEPS - steps, score);
    }

    /**
     * The Dodo will move and collect all the Eggs until its steps run out
     *
     * <p> Initial: Dodo (and eggs) is somewhere on the world
     * <p> Final:   Dodo will have moved and picked up eggs on the way
     */

    public void collectAllEggs() {
        Egg egg = nearestEgg();
        while (steps < Mauritius.MAXSTEPS && egg != null) {
            goToEgg(egg);
            egg = nearestEgg();
        }
        showError("I'm stuck!");
        lowerScore();
    }
}