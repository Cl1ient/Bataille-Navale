package Model.Weapon;

import Model.Coordinate;

public interface Weapon {
    ShotResult apply(Player targetPlayer, Coordinate targetCoordinate);
    String getName();
    Integer getUsesLeft();
}
