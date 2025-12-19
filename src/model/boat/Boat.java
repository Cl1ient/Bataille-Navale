package model.boat;

import model.EntityType;
import model.game.Game;
import model.GridEntity;
import model.map.Grid;
import model.player.Player;

public interface Boat extends GridEntity {
    /**
     *
     * @return true if the boat is sunk
     */
    boolean isSunk();


    void onHit(Player attacker, Player defender, Integer x, Integer y, Integer segmentIndex);


    /**
     *
     * @return the size of the boat
     */
    Integer getSize();

    /**
     *
     * @return the name of the boat
     */
    EntityType getType();

    Integer getSegmentsHit();

    void registerToGrid(Grid grid);
}
