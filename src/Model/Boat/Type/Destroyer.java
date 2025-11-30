package Model.Boat.Type;

import Model.Boat.Boat;
import Model.Coordinate;
import Model.Game.Game;
import Model.player.Player;

public class Destroyer implements Boat {

    private static final int DESTROYER_SIZE = 3;
    private final Integer m_size;
    private Integer m_nbShotReceive;
    private final String name = "Destroyer";

    /**
     * Constructor for Destroyer boat
     * Initializes the size and damage array.
     */
    public Destroyer(){
        this.m_size = DESTROYER_SIZE;
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
    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y){
        this.m_nbShotReceive ++;
        //defender.getOwnGrid().markHitBoat(new Coordinate(x,y));
        //attacker.getShotGrid().markHitBoat(new Coordinate(x,y));
    }

    /**
     * Returns the size of the boat
     * @return the size of the boat
     */
    public Integer getSize(){
        return this.m_size;
    }

    /**
     * Returns the name of the boat
     * @return the name of the boat
     */
    public String getType(){
        return name;
    }
}
