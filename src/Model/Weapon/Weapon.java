package Model.Weapon;

import Model.Coordinate;
import Model.Player.Player;

public interface Weapon {
    ShotResult apply(Player targetPlayer, Coordinate targetCoordinate);
    String getName();
    Integer getUsesLeft();
}
