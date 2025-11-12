package Model.Boat.Type;

import Model.Boat.Boat;

public class Torpedo implements Boat {
    private static final int Torpedo_SIZE = 2;
    private final Integer size;
    private final Boolean[] hits;
    private final String name = "Torpedo";

    /**
     * Constructor for Torpedo boat
     * Initializes the size and damage array.
     */
    public Torpedo(){
        this.size = Torpedo_SIZE;
        this.hits = new Boolean[this.size];
        for(int i = 0; i<this.size; i++){
            this.hits[i] = false;
        }
    }

    /**
     * Checks if all the index of the boat have been hit.
     * @return true if the boat is sunk
     */
    @Override
    public boolean isSunk(){
        for(boolean hit : hits){
            if(!hit){
                return false;
            }
        }
        return true;
    }

    /**
     * Marks the specific segment of the ship that was hit.
     * @param index index the position of the ship
     */
    @Override
    public void onHit(Integer index){
        this.hits[index] = true;
    }

    /**
     * Returns the size of the boat
     * @return the size of the boat
     */
    public Integer getSize(){
        return this.size;
    }

    /**
     * Returns the name of the boat
     * @return the name of the boat
     */
    public String getName(){
        return name;
    }

}
