package model.boat.type;

import model.Coordinate;
import model.boat.Boat;
import model.EntityType;
import model.player.Player;

public class AirCraftCarrier implements Boat {

    private static final int AIRCRAFTCARRIER_SIZE = 5;
    private final Integer m_size;
    private final boolean[] m_hits;
    private final EntityType m_type = EntityType.AIRCRAFT_CARRIER;

    /**
     * Constructor for AirCraftCarrier boat
     * Initializes the size and damage array.
     */
    public AirCraftCarrier(){
        this.m_size = AIRCRAFTCARRIER_SIZE;
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
}
