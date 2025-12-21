package model.boat.type;

import model.Coordinate;
import model.boat.Boat;
import model.EntityType;
import model.map.Grid;
import model.player.Player;

public class Submarine implements Boat {
    private static final int m_SUBMARINE_SIZE = 3;
    private final Integer m_size;
    private final boolean[] m_hits;
    private final EntityType m_type = EntityType.SUBMARINE;

    /**
     * Constructor for Submarine boat
     * Initializes the size and damage array.
     */
    public Submarine(){
        this.m_size = m_SUBMARINE_SIZE;
        this.m_hits = new boolean[this.m_size];
    }

    /**
     * Checks if all the index of the boat have been hit.
     * @return true if the boat is sunk
     */
    @Override
    public boolean isSunk(){
        for (boolean hit : m_hits) {
            if (!hit) return false;
        }
        return true;
    }


    @Override
    public void onHit(Player attacker, Player defender, Integer x, Integer y, Integer segmentIndex){
        System.out.println("Submarine hit");
        this.m_hits[segmentIndex] = true;

        defender.notifyHit(defender, new Coordinate(x, y));
        defender.getOwnGrid().markHitBoat(new Coordinate(x,y));
        attacker.getShotGrid().markHitBoat(new Coordinate(x,y));
        if(this.isSunk()){
            System.out.println("Submarine is sunk dans le if");
            defender.loseOneBoat();
            defender.notifySunkStatus(this);
        }
    }

    /**
     * Returns the size of the boat
     * @return the size of the boat
     */
    @Override
    public Integer getSize(){
        return this.m_size;
    }

    /**
     * Returns the name of the boat
     * @return the name of the boat
     */
    @Override
    public EntityType getType(){return this.m_type;}

    @Override
    public Integer getSegmentsHit(){
        int count = 0;
        for (boolean hit : m_hits) {
            if (hit) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void registerToGrid(Grid grid) {
        grid.addBoatToMemory(this);
    }

}
