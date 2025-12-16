package model.boat.type;

import model.Coordinate;
import model.boat.Boat;
import model.EntityType;
import model.player.Player;

public class Cruiser implements Boat {
    private static final int CRUISER_SIZE = 4;
    private final Integer m_size;
    private final boolean[] m_hits;
    private final EntityType m_type = EntityType.CRUISER;

    /**
     * Constructor for Cruiser boat
     * Initializes the size and damage array.
     */
    public Cruiser(){
        this.m_size = CRUISER_SIZE;
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
        this.m_hits[segmentIndex] = true;

        defender.notifyHit(defender, new Coordinate(x, y));
        defender.getOwnGrid().markHitBoat(new Coordinate(x,y));
        attacker.getShotGrid().markHitBoat(new Coordinate(x,y));

        if(this.isSunk()){
            defender.loseOneBoat();
            defender.notifySunkStatus(this);
        }
    }

    @Override
    public EntityType getType(){return this.m_type;}

    @Override
    public Integer getSize() {return this.m_size;}

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

}
