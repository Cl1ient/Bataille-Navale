package model.game;

import model.Coordinate;
import model.boat.Boat;
import model.player.Player;

public interface GameMediator {
    void handleShipSunk(Player defender, Boat sunkBoat);
    void handleHit(Player defender, Coordinate hitCoordinate);
    void handleBlackHoleHit(Player defender, Coordinate hitCoordinate);
    void notifyTrapPlacementError();
}
