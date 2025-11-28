package Model.Weapon;

import Model.Coordinate;
import Model.Player.Player;
import Model.ShotResult;

public interface Weapon {
    ShotResult apply(Player targetPlayer, Coordinate targetCoordinate);
    String getName();
    Integer getUsesLeft();
}
