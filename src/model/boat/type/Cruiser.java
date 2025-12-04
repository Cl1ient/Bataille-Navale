package model.boat.type;

import model.boat.Boat;
import model.EntityType;
import model.game.Game;
import model.player.Player;

public class Cruiser implements Boat {
    private static final int CRUISER_SIZE = 4;
    private final Integer m_size;
    private Integer m_nbShotReceive;
    private final EntityType m_type = EntityType.CRUISER;

    /**
     * Constructor for Cruiser boat
     * Initializes the size and damage array.
     */
    public Cruiser(){
        this.m_size = CRUISER_SIZE;
        this.m_nbShotReceive = 0;
    }

    /**
     * Checks if all the index of the boat have been hit.
     * @return true if the boat is sunk
     */
    @Override
    public boolean isSunk(){
        return this.m_size == this.m_nbShotReceive;
    }


    @Override
    public void onHit(Player attacker, Player defender, Integer x, Integer y){
        this.m_nbShotReceive ++;
        //defender.getOwnGrid().markHitBoat(new Coordinate(x,y));
        //attacker.getShotGrid().markHitBoat(new Coordinate(x,y));
    }

    public EntityType getType(){return this.m_type;}

    public Integer getSize() {return this.m_size;}
}
