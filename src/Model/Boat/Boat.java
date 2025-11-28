package Model.Boat;

import Model.Game.Game;
import Model.GridEntity;
import Model.player.Player;

public interface Boat extends GridEntity {
    /**
     *
     * @return true if the boat is sunk
     */
    boolean isSunk();

    /**
     * Marks the specific segment of the ship that was hit.
     * @param index index the position of the ship
     */
    public void onHit(Game game, Player attacker, Player defender, Integer x, Integer y);


    /**
     *
     * @return the name of the boat
     */
    public String getType();
}
