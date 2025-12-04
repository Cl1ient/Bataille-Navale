package Model.Boat.Type;

import Model.Boat.Boat;
import Model.Coordinate;
import Model.EntityType;
import Model.Game.Game;
import Model.player.Player;

public class AirCraftCarrier implements Boat {

    private static final int AIRCRAFTCARRIER_SIZE = 5;
    private final Integer size;
    private Integer m_nbShotReceive;
    private final EntityType m_type = EntityType.AIRCRAFT_CARRIER;

    /**
     * Constructor for AirCraftCarrier boat
     * Initializes the size and damage array.
     */
    public AirCraftCarrier(){
        this.size = AIRCRAFTCARRIER_SIZE;
        this.m_nbShotReceive = 0;
    }

    /**
     * Checks if all the index of the boat have been hit.
     * @return true if the boat is sunk
     */
    @Override
    public boolean isSunk(){
        return size == m_nbShotReceive;
    }


    @Override
    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y){
        // comportement d'un bateau lorsqu'il est touché
        this.m_nbShotReceive ++;
        //defender.getOwnGrid().markHitBoat(new Coordinate(x,y));
        //attacker.getShotGrid().markHitBoat(new Coordinate(x,y));
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
    public EntityType getType(){return this.m_type;}
}
