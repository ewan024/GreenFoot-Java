import greenfoot.*;

import java.util.List;

/**
 * @author Sjaak Smetsers & Renske Smetsers-Weeda
 * @version 3.0 -- 20-01-2017
 */
public class MyDodo extends Dodo {
    private int myNrOfEggsHatched;

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
        return !(borderAhead() || fenceAhead());
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

        for (int nrStepsTaken = 0; nrStepsTaken < distance; nrStepsTaken++) {
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

    public void turn180() {
        turnRight();
        turnRight();
    }

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

    public void goToEgg(Egg egg) {
        jump(egg.getX() - getX());
        turnRight();
        jump(egg.getY() - getY());
        turnLeft();
        if (onEgg()) {
            pickUpEgg();
        }
    }

    public void printFenceLocation() {

        for (Fence fence: fences()) {
            System.out.println(fence.getX() + ", " + fence.getY());
        }
    }

    public Egg nearestEgg() {
        Egg closest = null;
        int closestEgg = Integer.MAX_VALUE;

        for (Egg egg : eggOnWorld()) {
            int eggX = Math.abs(egg.getX() - getX());
            int eggY = Math.abs(egg.getY() - getY());
            int distance = eggX + eggY;

            if (distance <= closestEgg) {
                closestEgg = distance;
                closest = egg;
//                System.out.println(eggX + ", " + eggY);
            }
        }

        return closest;
    }

    public void collectAllEggs() {

        Egg egg = nearestEgg();

        while (egg != null) {
            goToEgg(egg);
            egg = nearestEgg();
            if (onEgg()) {
                System.out.println(getEgg().getValue());
            }
        }
    }
}